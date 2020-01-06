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

package org.apache.shardingsphere.shardingjdbc.jdbc.core.context;

import lombok.Getter;
import org.apache.shardingsphere.core.rule.ShadowRule;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.EncryptDataSource;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.MasterSlaveDataSource;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.apache.shardingsphere.shardingjdbc.jdbc.metadata.JDBCDataSourceConnectionManager;
import org.apache.shardingsphere.spi.database.type.DatabaseType;
import org.apache.shardingsphere.underlying.common.metadata.datasource.DataSourceMetas;
import org.apache.shardingsphere.underlying.common.metadata.table.init.TableMetaDataInitializer;
import org.apache.shardingsphere.underlying.common.metadata.table.init.TableMetaDataInitializerEntry;
import org.apache.shardingsphere.underlying.common.metadata.table.init.loader.impl.DefaultTableMetaDataLoader;
import org.apache.shardingsphere.underlying.common.rule.BaseRule;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Runtime context for shadow.
 *
 * @author zhyee
 * @author xiayan
 */
@Getter
public final class ShadowRuntimeContext extends AbstractRuntimeContext<ShadowRule> {
    
    private final DataSource actualDataSource;
    
    private final DataSource shadowDataSource;
    
    private final ShadowType shadowType;
    
    public ShadowRuntimeContext(final DataSource actualDataSource, final DataSource shadowDataSource, 
                                final ShadowRule rule, final Properties props, final DatabaseType databaseType) throws SQLException {
        super(actualDataSource, rule, props, databaseType);
        this.actualDataSource = actualDataSource;
        this.shadowDataSource = shadowDataSource;
        if (actualDataSource instanceof MasterSlaveDataSource) {
            shadowType = ShadowType.MASTER_SLAVE;
        } else if (actualDataSource instanceof ShardingDataSource) {
            shadowType = ShadowType.SHARDING;
        } else if (actualDataSource instanceof EncryptDataSource) {
            shadowType = ShadowType.ENCRYPT;
        } else {
            shadowType = ShadowType.RAW;
        }
    }
    
    @Override
    protected TableMetaDataInitializerEntry createTableMetaDataInitializerEntry(final Map<String, DataSource> dataSourceMap, final DataSourceMetas dataSourceMetas) {
        Map<BaseRule, TableMetaDataInitializer> tableMetaDataInitializes = new HashMap<>(1, 1);
        tableMetaDataInitializes.put(getRule(), new DefaultTableMetaDataLoader(dataSourceMetas, new JDBCDataSourceConnectionManager(dataSourceMap.values().iterator().next())));
        return new TableMetaDataInitializerEntry(tableMetaDataInitializes);
    }
    
    public enum ShadowType {
        MASTER_SLAVE, ENCRYPT, SHARDING, RAW
    }
}
