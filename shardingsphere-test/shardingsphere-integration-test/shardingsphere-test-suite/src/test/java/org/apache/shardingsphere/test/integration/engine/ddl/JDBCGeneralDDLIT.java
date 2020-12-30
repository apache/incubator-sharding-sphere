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

package org.apache.shardingsphere.test.integration.engine.ddl;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.apache.shardingsphere.driver.jdbc.core.connection.ShardingSphereConnection;
import org.apache.shardingsphere.infra.database.type.DatabaseTypeRegistry;
import org.apache.shardingsphere.infra.metadata.schema.model.TableMetaData;
import org.apache.shardingsphere.test.integration.cases.IntegrateTestCaseType;
import org.apache.shardingsphere.test.integration.cases.assertion.ddl.DDLIntegrateTestCaseAssertion;
import org.apache.shardingsphere.test.integration.cases.assertion.root.SQLCaseType;
import org.apache.shardingsphere.test.integration.cases.dataset.metadata.DataSetColumn;
import org.apache.shardingsphere.test.integration.cases.dataset.metadata.DataSetIndex;
import org.apache.shardingsphere.test.integration.cases.dataset.metadata.DataSetMetadata;
import org.apache.shardingsphere.test.integration.engine.util.IntegrateTestParameters;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public final class JDBCGeneralDDLIT extends BaseDDLIT {
    
    public JDBCGeneralDDLIT(final String parentPath, final DDLIntegrateTestCaseAssertion assertion, final String ruleType,
                            final String databaseType, final SQLCaseType caseType, final String sql) throws IOException, JAXBException, SQLException, ParseException {
        super(parentPath, assertion, ruleType, DatabaseTypeRegistry.getActualDatabaseType(databaseType), caseType, sql);
    }
    
    @Parameters(name = "{2} -> {3} -> {4} -> {5}")
    public static Collection<Object[]> getParameters() {
        return IntegrateTestParameters.getParametersWithAssertion(IntegrateTestCaseType.DDL);
    }
    
    @Test
    public void assertExecuteUpdate() throws SQLException {
        assertExecute(true);
    }
    
    @Test
    public void assertExecute() throws SQLException {
        assertExecute(false);
    }
    
    private void assertExecute(final boolean isExecuteUpdate) throws SQLException {
        try (Connection connection = getTargetDataSource().getConnection()) {
            dropTableIfExisted(connection);
            if (!Strings.isNullOrEmpty(((DDLIntegrateTestCaseAssertion) getAssertion()).getInitSQL())) {
                for (String each : Splitter.on(";").trimResults().splitToList(((DDLIntegrateTestCaseAssertion) getAssertion()).getInitSQL())) {
                    connection.prepareStatement(each).executeUpdate();
                }
            }
            if (isExecuteUpdate) {
                if (SQLCaseType.Literal == getCaseType()) {
                    connection.createStatement().executeUpdate(getSql());
                } else {
                    connection.prepareStatement(getSql()).executeUpdate();
                }
            } else {
                if (SQLCaseType.Literal == getCaseType()) {
                    connection.createStatement().execute(getSql());
                } else {
                    connection.prepareStatement(getSql()).execute();
                }
            }
            assertTableMetaData(connection);
            dropTableIfExisted(connection);
        } catch (final SQLException ex) {
            logException(ex);
            throw ex;
        }
    }
    
    protected void assertTableMetaData(final Connection connection) {
        String tableName = ((DDLIntegrateTestCaseAssertion) getAssertion()).getTable();
        Optional<DataSetMetadata> expected = getDataSet().findMetadata(tableName);
        if (!expected.isPresent()) {
            assertFalse(((ShardingSphereConnection) connection).getMetaDataContexts().getDefaultMetaData().getSchema().containsTable(tableName));
        } else {
            TableMetaData actual = ((ShardingSphereConnection) connection).getMetaDataContexts().getDefaultMetaData().getSchema().get(tableName);
            assertTableMetaData(actual, expected.get());
        }
    }
    
    private void assertTableMetaData(final TableMetaData actual, final DataSetMetadata expected) {
        // TODO fill metadata for replica query and other rules
        if (actual.getColumns().isEmpty()) {
            return;
        }
        assertThat(new LinkedList<>(actual.getColumns().keySet()), is(expected.getColumns().stream().map(DataSetColumn::getName).collect(Collectors.toList())));
        assertThat(new LinkedList<>(actual.getIndexes().keySet()), is(expected.getIndexes().stream().map(DataSetIndex::getName).collect(Collectors.toList())));
    }
}
