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

package org.apache.shardingsphere.scaling.postgresql.component.checker;

import com.google.common.collect.Maps;
import org.apache.shardingsphere.infra.exception.CommonErrorCode;
import org.apache.shardingsphere.scaling.core.common.exception.PrepareFailedException;
import org.apache.shardingsphere.scaling.core.common.sqlbuilder.ScalingSQLBuilder;
import org.apache.shardingsphere.scaling.core.job.check.source.AbstractDataSourceChecker;
import org.apache.shardingsphere.scaling.postgresql.component.PostgreSQLScalingSQLBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * PostgreSQL Data source checker.
 */
public final class PostgreSQLDataSourceChecker extends AbstractDataSourceChecker {
    
    @Override
    public void checkPrivilege(final Collection<? extends DataSource> dataSources) {
        try {
            for (DataSource dataSource : dataSources) {
                String tableName;
                try (Connection connection = dataSource.getConnection()) {
                    ResultSet tables = connection.getMetaData().getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"});
                    if (tables.next()) {
                        tableName = tables.getString(3);
                    } else {
                        throw new PrepareFailedException(CommonErrorCode.SCALING_SOURCE_DATA_SOURCE_NO_TABLES_FIND);
                    }
                    connection.prepareStatement(String.format("SELECT * FROM %s LIMIT 1", tableName)).executeQuery();
                }
            }
        } catch (final SQLException ex) {
            throw new PrepareFailedException(ex, CommonErrorCode.SCALING_SOURCE_DATA_SOURCE_CHECK_PRIVILEGES_FAIL);
        }
    }
    
    @Override
    public void checkVariable(final Collection<? extends DataSource> dataSources) {
    
    }
    
    @Override
    protected ScalingSQLBuilder getSqlBuilder() {
        return new PostgreSQLScalingSQLBuilder(Maps.newHashMap());
    }
}
