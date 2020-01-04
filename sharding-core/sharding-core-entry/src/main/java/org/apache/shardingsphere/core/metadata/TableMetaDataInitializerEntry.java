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

package org.apache.shardingsphere.core.metadata;

import org.apache.shardingsphere.core.rule.ShardingRule;
import org.apache.shardingsphere.encrypt.metadata.decorator.EncryptTableMetaDataDecorator;
import org.apache.shardingsphere.sharding.execute.metadata.loader.ShardingTableMetaDataLoader;
import org.apache.shardingsphere.underlying.common.constant.properties.PropertiesConstant;
import org.apache.shardingsphere.underlying.common.constant.properties.ShardingSphereProperties;
import org.apache.shardingsphere.underlying.common.metadata.datasource.DataSourceMetas;
import org.apache.shardingsphere.underlying.common.metadata.table.TableMetaData;
import org.apache.shardingsphere.underlying.common.metadata.table.TableMetas;
import org.apache.shardingsphere.underlying.common.metadata.table.loader.ConnectionManager;
import org.apache.shardingsphere.underlying.executor.engine.ExecutorEngine;

import java.sql.SQLException;

/**
 * Table meta data initializer entry.
 *
 * @author zhangliang
 */
public final class TableMetaDataInitializerEntry {
    
    private final ShardingTableMetaDataLoader tableMetaDataLoader;
    
    private final EncryptTableMetaDataDecorator encryptTableMetaDataDecorator;
    
    public TableMetaDataInitializerEntry(final DataSourceMetas dataSourceMetas, 
                                         final ExecutorEngine executorEngine, final ConnectionManager connectionManager, final ShardingSphereProperties properties) {
        tableMetaDataLoader = new ShardingTableMetaDataLoader(dataSourceMetas, executorEngine, 
                connectionManager, properties.<Integer>getValue(PropertiesConstant.MAX_CONNECTIONS_SIZE_PER_QUERY), properties.<Boolean>getValue(PropertiesConstant.CHECK_TABLE_METADATA_ENABLED));
        encryptTableMetaDataDecorator = new EncryptTableMetaDataDecorator();
    }
    
    /**
     * Load table meta data.
     *
     * @param logicTableName logic table name
     * @param shardingRule sharding rule
     * @return table meta data
     * @throws SQLException SQL exception
     */
    public TableMetaData load(final String logicTableName, final ShardingRule shardingRule) throws SQLException {
        return encryptTableMetaDataDecorator.decorate(tableMetaDataLoader.load(logicTableName, shardingRule), logicTableName, shardingRule.getEncryptRule());
    }
    
    /**
     * Load all table meta data.
     *
     * @param shardingRule sharding rule
     * @return all table meta data
     * @throws SQLException SQL exception
     */
    public TableMetas loadAll(final ShardingRule shardingRule) throws SQLException {
        return encryptTableMetaDataDecorator.decorate(tableMetaDataLoader.loadAll(shardingRule), shardingRule.getEncryptRule());
    }
}
