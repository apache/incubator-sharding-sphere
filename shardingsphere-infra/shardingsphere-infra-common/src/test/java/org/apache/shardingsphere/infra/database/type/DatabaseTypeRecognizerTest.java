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

package org.apache.shardingsphere.infra.database.type;

import org.apache.shardingsphere.infra.database.type.dialect.H2DatabaseType;
import org.apache.shardingsphere.infra.database.type.dialect.MariaDBDatabaseType;
import org.apache.shardingsphere.infra.database.type.dialect.MySQLDatabaseType;
import org.apache.shardingsphere.infra.database.type.dialect.OracleDatabaseType;
import org.apache.shardingsphere.infra.database.type.dialect.PostgreSQLDatabaseType;
import org.apache.shardingsphere.infra.database.type.dialect.SQL92DatabaseType;
import org.apache.shardingsphere.infra.database.type.dialect.SQLServerDatabaseType;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class DatabaseTypeRecognizerTest {

    @Test
    public void assertGetH2DatabaseType() throws SQLException {
        DataSource datasource = mockDataSource(DatabaseTypeRegistry.getActualDatabaseType("H2"));
        Collection<DataSource> dataSources = Collections.singleton(datasource);
        assertThat(DatabaseTypeRecognizer.getDatabaseType(dataSources).getName(), is("H2"));
    }

    @Test
    public void assertGetMariaDBDatabaseType() throws SQLException {
        DataSource datasource = mockDataSource(DatabaseTypeRegistry.getActualDatabaseType("MariaDB"));
        Collection<DataSource> dataSources = Collections.singleton(datasource);
        assertThat(DatabaseTypeRecognizer.getDatabaseType(dataSources).getName(), is("MariaDB"));
    }

    @Test
    public void assertGetMySQLDatabaseType() throws SQLException {
        DataSource datasource = mockDataSource(DatabaseTypeRegistry.getActualDatabaseType("MySQL"));
        Collection<DataSource> dataSources = Collections.singleton(datasource);
        assertThat(DatabaseTypeRecognizer.getDatabaseType(dataSources).getName(), is("MySQL"));
    }

    @Test
    public void assertGetOracleDatabaseType() throws SQLException {
        DataSource datasource = mockDataSource(DatabaseTypeRegistry.getActualDatabaseType("Oracle"));
        Collection<DataSource> dataSources = Collections.singleton(datasource);
        assertThat(DatabaseTypeRecognizer.getDatabaseType(dataSources).getName(), is("Oracle"));
    }

    @Test
    public void assertGetPostgreSQLDatabaseType() throws SQLException {
        DataSource datasource = mockDataSource(DatabaseTypeRegistry.getActualDatabaseType("PostgreSQL"));
        Collection<DataSource> dataSources = Collections.singleton(datasource);
        assertThat(DatabaseTypeRecognizer.getDatabaseType(dataSources).getName(), is("PostgreSQL"));
    }

    @Test
    public void assertGetSQL92DatabaseType() throws SQLException {
        DataSource datasource = mockDataSource(DatabaseTypeRegistry.getActualDatabaseType("SQL92"));
        Collection<DataSource> dataSources = Collections.singleton(datasource);
        assertThat(DatabaseTypeRecognizer.getDatabaseType(dataSources).getName(), is("SQL92"));
    }

    @Test
    public void assertGetSQLServerDatabaseType() throws SQLException {
        DataSource datasource = mockDataSource(DatabaseTypeRegistry.getActualDatabaseType("SQLServer"));
        Collection<DataSource> dataSources = Collections.singleton(datasource);
        assertThat(DatabaseTypeRecognizer.getDatabaseType(dataSources).getName(), is("SQLServer"));
    }

    @Test
    public void assertGetDefaultDatabaseType() {
        assertThat(DatabaseTypeRecognizer.getDatabaseType(Collections.emptyList()).getName(), is("MySQL"));
    }

    @Test
    public void assertGetDatabaseTypeFromSameDataSources() throws SQLException {
        DataSource datasource1 = mockDataSource(DatabaseTypeRegistry.getActualDatabaseType("MySQL"));
        DataSource datasource2 = mockDataSource(DatabaseTypeRegistry.getActualDatabaseType("MySQL"));
        Collection<DataSource> sameDataSources = Arrays.asList(datasource1, datasource2);
        assertThat(DatabaseTypeRecognizer.getDatabaseType(sameDataSources).getName(), is("MySQL"));
    }

    @Test(expected = IllegalStateException.class)
    public void assertGetDatabaseTypeFromDifferentDataSources() throws SQLException {
        DataSource datasource1 = mockDataSource(DatabaseTypeRegistry.getActualDatabaseType("H2"));
        DataSource datasource2 = mockDataSource(DatabaseTypeRegistry.getActualDatabaseType("Oracle"));
        Collection<DataSource> differentDataSources = Arrays.asList(datasource1, datasource2);
        DatabaseTypeRecognizer.getDatabaseType(differentDataSources);
    }

    private DataSource mockDataSource(final DatabaseType databaseType) throws SQLException {
        DataSource result = mock(DataSource.class);
        Connection connection = mock(Connection.class, RETURNS_DEEP_STUBS);
        when(result.getConnection()).thenReturn(connection);
        if (databaseType instanceof H2DatabaseType) {
            when(connection.getMetaData().getURL()).thenReturn("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;MODE=MySQL");
        } else if (databaseType instanceof MariaDBDatabaseType) {
            when(connection.getMetaData().getURL()).thenReturn("jdbc:mariadb://localhost:3306/test");
        } else if (databaseType instanceof MySQLDatabaseType) {
            when(connection.getMetaData().getURL()).thenReturn("jdbc:mysql://localhost:3306/test");
        } else if (databaseType instanceof OracleDatabaseType) {
            when(connection.getMetaData().getURL()).thenReturn("jdbc:oracle:oci:@127.0.0.1/test");
        } else if (databaseType instanceof PostgreSQLDatabaseType) {
            when(connection.getMetaData().getURL()).thenReturn("jdbc:postgresql://localhost:5432/test");
        } else if (databaseType instanceof SQL92DatabaseType) {
            when(connection.getMetaData().getURL()).thenReturn("jdbc:sqlserver://127.0.0.1;DatabaseName=test");
        } else if (databaseType instanceof SQLServerDatabaseType) {
            when(connection.getMetaData().getURL()).thenReturn("jdbc:microsoft:sqlserver://127.0.0.1;DatabaseName=test");
        }
        return result;
    }

}
