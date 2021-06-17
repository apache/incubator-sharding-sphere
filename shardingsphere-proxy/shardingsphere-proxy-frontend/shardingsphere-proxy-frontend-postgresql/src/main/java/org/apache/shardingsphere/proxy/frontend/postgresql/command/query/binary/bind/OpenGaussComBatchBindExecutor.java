/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.proxy.frontend.postgresql.command.query.binary.bind;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.db.protocol.binary.BinaryCell;
import org.apache.shardingsphere.db.protocol.packet.DatabasePacket;
import org.apache.shardingsphere.db.protocol.postgresql.constant.PostgreSQLBinaryColumnType;
import org.apache.shardingsphere.db.protocol.postgresql.constant.PostgreSQLColumnFormat;
import org.apache.shardingsphere.db.protocol.postgresql.packet.PostgreSQLPacket;
import org.apache.shardingsphere.db.protocol.postgresql.packet.command.query.PostgreSQLColumnDescription;
import org.apache.shardingsphere.db.protocol.postgresql.packet.command.query.PostgreSQLRowDescriptionPacket;
import org.apache.shardingsphere.db.protocol.postgresql.packet.command.query.binary.bind.OpenGaussComBatchBindPacket;
import org.apache.shardingsphere.db.protocol.postgresql.packet.command.query.binary.bind.PostgreSQLBindCompletePacket;
import org.apache.shardingsphere.db.protocol.postgresql.packet.command.query.text.PostgreSQLDataRowPacket;
import org.apache.shardingsphere.db.protocol.postgresql.packet.generic.PostgreSQLCommandCompletePacket;
import org.apache.shardingsphere.infra.database.type.DatabaseTypeRegistry;
import org.apache.shardingsphere.infra.parser.ShardingSphereSQLParserEngine;
import org.apache.shardingsphere.proxy.backend.communication.DatabaseCommunicationEngine;
import org.apache.shardingsphere.proxy.backend.communication.DatabaseCommunicationEngineFactory;
import org.apache.shardingsphere.proxy.backend.communication.jdbc.connection.BackendConnection;
import org.apache.shardingsphere.proxy.backend.context.ProxyContext;
import org.apache.shardingsphere.proxy.backend.response.data.QueryResponseCell;
import org.apache.shardingsphere.proxy.backend.response.data.QueryResponseRow;
import org.apache.shardingsphere.proxy.backend.response.data.impl.BinaryQueryResponseCell;
import org.apache.shardingsphere.proxy.backend.response.header.ResponseHeader;
import org.apache.shardingsphere.proxy.backend.response.header.query.QueryResponseHeader;
import org.apache.shardingsphere.proxy.backend.response.header.query.impl.QueryHeader;
import org.apache.shardingsphere.proxy.backend.response.header.update.UpdateResponseHeader;
import org.apache.shardingsphere.proxy.frontend.command.executor.QueryCommandExecutor;
import org.apache.shardingsphere.proxy.frontend.command.executor.ResponseType;
import org.apache.shardingsphere.proxy.frontend.postgresql.command.PostgreSQLConnectionContext;
import org.apache.shardingsphere.proxy.frontend.postgresql.command.query.PostgreSQLCommand;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dml.EmptyStatement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public final class OpenGaussComBatchBindExecutor implements QueryCommandExecutor {
    
    private final PostgreSQLConnectionContext connectionContext;
    
    private final OpenGaussComBatchBindPacket packet;
    
    private final BackendConnection backendConnection;
    
    private final List<DatabaseCommunicationEngine> databaseCommunicationEngines = new LinkedList<>();
    
    @Getter
    private volatile ResponseType responseType;
    
    private boolean batchBindComplete;
    
    @Override
    public Collection<DatabasePacket<?>> execute() throws SQLException {
        List<List<Object>> parameters = packet.getParameters();
        for (int i = 0; i < parameters.size(); i++) {
            List<Object> parameter = parameters.get(i);
            init(parameter);
            ResponseHeader responseHeader = databaseCommunicationEngines.get(i).execute();
            if (responseHeader instanceof QueryResponseHeader && connectionContext.getDescribeExecutor().isPresent()) {
                connectionContext.getDescribeExecutor().get().setRowDescriptionPacket(getRowDescriptionPacket((QueryResponseHeader) responseHeader));
            }
            if (responseHeader instanceof UpdateResponseHeader) {
                responseType = ResponseType.UPDATE;
                connectionContext.setUpdateCount(connectionContext.getUpdateCount() + ((UpdateResponseHeader) responseHeader).getUpdateCount());
            }
        }
        return Collections.singletonList(new PostgreSQLBindCompletePacket());
    }
    
    private void init(final List<Object> parameter) {
        databaseCommunicationEngines.add(DatabaseCommunicationEngineFactory.getInstance().newBinaryProtocolInstance(getSqlStatement(), packet.getSql(), parameter, backendConnection));
    }
    
    private SQLStatement getSqlStatement() {
        return connectionContext.getSqlStatement().orElseGet(() -> {
            SQLStatement result = parseSql(packet.getSql(), backendConnection.getSchemaName());
            connectionContext.setSqlStatement(result);
            return result;
        });
    }
    
    private SQLStatement parseSql(final String sql, final String schemaName) {
        if (sql.isEmpty()) {
            return new EmptyStatement();
        }
        ShardingSphereSQLParserEngine sqlStatementParserEngine = new ShardingSphereSQLParserEngine(
                DatabaseTypeRegistry.getTrunkDatabaseTypeName(ProxyContext.getInstance().getMetaDataContexts().getMetaData(schemaName).getResource().getDatabaseType()));
        return sqlStatementParserEngine.parse(sql, true);
    }
    
    private PostgreSQLRowDescriptionPacket getRowDescriptionPacket(final QueryResponseHeader queryResponseHeader) {
        responseType = ResponseType.QUERY;
        Collection<PostgreSQLColumnDescription> columnDescriptions = createColumnDescriptions(queryResponseHeader);
        return new PostgreSQLRowDescriptionPacket(columnDescriptions.size(), columnDescriptions);
    }
    
    private Collection<PostgreSQLColumnDescription> createColumnDescriptions(final QueryResponseHeader queryResponseHeader) {
        Collection<PostgreSQLColumnDescription> result = new LinkedList<>();
        int columnIndex = 0;
        for (QueryHeader each : queryResponseHeader.getQueryHeaders()) {
            result.add(new PostgreSQLColumnDescription(each.getColumnName(), ++columnIndex, each.getColumnType(), each.getColumnLength(), each.getColumnTypeName()));
        }
        return result;
    }
    
    @Override
    public boolean next() throws SQLException {
        Iterator<DatabaseCommunicationEngine> iterator = databaseCommunicationEngines.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().next()) {
                return true;
            } else {
                iterator.remove();
            }
        }
        return !batchBindComplete && (batchBindComplete = true);
    }
    
    @Override
    public PostgreSQLPacket getQueryRowPacket() throws SQLException {
        if (batchBindComplete) {
            String sqlCommand = connectionContext.getSqlStatement().map(SQLStatement::getClass).map(PostgreSQLCommand::valueOf).map(command -> command.map(Enum::name).orElse("")).orElse("");
            return new PostgreSQLCommandCompletePacket(sqlCommand, connectionContext.getUpdateCount());
        }
        QueryResponseRow queryResponseRow = databaseCommunicationEngines.get(0).getQueryResponseRow();
        return new PostgreSQLDataRowPacket(getData(queryResponseRow));
    }
    
    private List<Object> getData(final QueryResponseRow queryResponseRow) {
        Collection<QueryResponseCell> cells = queryResponseRow.getCells();
        List<Object> result = new ArrayList<>(cells.size());
        List<QueryResponseCell> columns = new ArrayList<>(cells);
        for (int i = 0; i < columns.size(); i++) {
            PostgreSQLColumnFormat format = packet.getResultFormatByColumnIndex(i);
            result.add(PostgreSQLColumnFormat.BINARY == format ? createBinaryCell(columns.get(i)) : columns.get(i).getData());
        }
        return result;
    }
    
    private BinaryCell createBinaryCell(final QueryResponseCell cell) {
        return new BinaryCell(PostgreSQLBinaryColumnType.valueOfJDBCType(((BinaryQueryResponseCell) cell).getJdbcType()), cell.getData());
    }
}
