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

package org.apache.shardingsphere.proxy.backend.text.distsql.rdl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.distsql.parser.statement.rdl.RuleDefinitionStatement;
import org.apache.shardingsphere.distsql.parser.statement.rdl.create.AddResourceStatement;
import org.apache.shardingsphere.distsql.parser.statement.rdl.drop.DropResourceStatement;
import org.apache.shardingsphere.infra.context.metadata.impl.StandardMetaDataContexts;
import org.apache.shardingsphere.proxy.backend.communication.jdbc.connection.BackendConnection;
import org.apache.shardingsphere.proxy.backend.context.ProxyContext;
import org.apache.shardingsphere.proxy.backend.text.TextProtocolBackendHandler;
import org.apache.shardingsphere.proxy.backend.text.distsql.rdl.resource.AddResourceBackendHandler;
import org.apache.shardingsphere.proxy.backend.text.distsql.rdl.resource.CreateDatabaseBackendHandler;
import org.apache.shardingsphere.proxy.backend.text.distsql.rdl.resource.DropDatabaseBackendHandler;
import org.apache.shardingsphere.proxy.backend.text.distsql.rdl.resource.DropResourceBackendHandler;
import org.apache.shardingsphere.proxy.backend.text.distsql.rdl.rule.RuleDefinitionBackendHandler;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.CreateDatabaseStatement;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.DropDatabaseStatement;

import java.sql.SQLException;

/**
 * RDL backend handler factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RDLBackendHandlerFactory {
    
    /**
     * Create new instance of RDL backend handler.
     * 
     * @param sqlStatement SQL statement
     * @param backendConnection backend connection
     * @return RDL backend handler
     * @throws SQLException SQL exception
     */
    public static TextProtocolBackendHandler newInstance(final SQLStatement sqlStatement, final BackendConnection backendConnection) throws SQLException {
        TextProtocolBackendHandler result = createRDLBackendHandler(sqlStatement, backendConnection);
        checkRegistryCenterExisted(sqlStatement);
        return result;
    }
    
    private static void checkRegistryCenterExisted(final SQLStatement sqlStatement) throws SQLException {
        if (ProxyContext.getInstance().getMetaDataContexts() instanceof StandardMetaDataContexts) {
            throw new SQLException(String.format("No Registry center to execute `%s` SQL", sqlStatement.getClass().getSimpleName()));
        }
    }
    
    private static TextProtocolBackendHandler createRDLBackendHandler(final SQLStatement sqlStatement, final BackendConnection backendConnection) {
        if (sqlStatement instanceof AddResourceStatement) {
            return new AddResourceBackendHandler((AddResourceStatement) sqlStatement, backendConnection);
        }
        if (sqlStatement instanceof DropResourceStatement) {
            return new DropResourceBackendHandler((DropResourceStatement) sqlStatement, backendConnection);
        }
        if (sqlStatement instanceof CreateDatabaseStatement) {
            return new CreateDatabaseBackendHandler((CreateDatabaseStatement) sqlStatement);
        }
        if (sqlStatement instanceof DropDatabaseStatement) {
            return new DropDatabaseBackendHandler((DropDatabaseStatement) sqlStatement, backendConnection);
        }
        return new RuleDefinitionBackendHandler<>((RuleDefinitionStatement) sqlStatement, backendConnection);
    }
}
