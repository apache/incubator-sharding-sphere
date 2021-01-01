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

package org.apache.shardingsphere.test.integration.engine.it.dcl;

import org.apache.shardingsphere.infra.database.type.DatabaseTypeRegistry;
import org.apache.shardingsphere.test.integration.cases.SQLCommandType;
import org.apache.shardingsphere.test.integration.cases.assertion.IntegrateTestCaseAssertion;
import org.apache.shardingsphere.test.integration.engine.param.SQLCaseType;
import org.apache.shardingsphere.test.integration.engine.param.IntegrateTestParameters;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;

public final class GeneralDCLIT extends BaseDCLIT {
    
    public GeneralDCLIT(final String parentPath, final IntegrateTestCaseAssertion assertion, final String scenario,
                        final String databaseType, final SQLCaseType caseType, final String sql) throws IOException, JAXBException, SQLException, ParseException {
        super(parentPath, assertion, scenario, DatabaseTypeRegistry.getActualDatabaseType(databaseType), caseType, sql);
    }
    
    @Parameters(name = "{2} -> {3} -> {4} -> {5}")
    public static Collection<Object[]> getParameters() {
        return IntegrateTestParameters.getAssertionParameters(SQLCommandType.DCL);
    }
    
    @Test
    public void assertExecuteUpdate() throws SQLException {
        try (Connection connection = getTargetDataSource().getConnection()) {
            if (SQLCaseType.Literal == getCaseType()) {
                connection.createStatement().executeUpdate(getSql());
            } else {
                connection.prepareStatement(getSql()).executeUpdate();
            }
        }
    }
    
    @Test
    public void assertExecute() throws SQLException {
        try (Connection connection = getTargetDataSource().getConnection()) {
            if (SQLCaseType.Literal == getCaseType()) {
                connection.createStatement().execute(getSql());
            } else {
                connection.prepareStatement(getSql()).execute();
            }
        }
    }
}
