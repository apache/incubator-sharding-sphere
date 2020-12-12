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

package org.apache.shardingsphere.infra.context.metadata.impl;

import lombok.Getter;
import org.apache.shardingsphere.infra.auth.Authentication;
import org.apache.shardingsphere.infra.auth.AuthenticationEngine;
import org.apache.shardingsphere.infra.auth.builtin.DefaultAuthentication;
import org.apache.shardingsphere.infra.config.properties.ConfigurationProperties;
import org.apache.shardingsphere.infra.context.metadata.MetaDataContexts;
import org.apache.shardingsphere.infra.database.DefaultSchema;
import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.infra.database.type.dialect.MySQLDatabaseType;
import org.apache.shardingsphere.infra.executor.kernel.ExecutorEngine;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Standard meta data contexts.
 */
@Getter
public final class StandardMetaDataContexts implements MetaDataContexts {
    
    private final Map<String, ShardingSphereMetaData> metaDataMap;
    
    private final ExecutorEngine executorEngine;
    
    private final Authentication authentication;
    
    private final ConfigurationProperties props;
    
    private final Map<String, DatabaseType> databaseTypes;
    
    public StandardMetaDataContexts() {
        // TODO MySQLDatabaseType is invalid because it can not update again
        this(new HashMap<>(), null, new DefaultAuthentication(), new ConfigurationProperties(new Properties()), Collections.singletonMap(DefaultSchema.LOGIC_NAME, new MySQLDatabaseType()));
    }
    
    public StandardMetaDataContexts(final Map<String, ShardingSphereMetaData> metaDataMap, 
                                    final ExecutorEngine executorEngine, final Authentication authentication, final ConfigurationProperties props, final Map<String, DatabaseType> databaseTypes) {
        this.metaDataMap = metaDataMap;
        this.executorEngine = executorEngine;
        this.authentication = AuthenticationEngine.findSPIAuthentication().orElse(authentication);
        this.props = props;
        this.databaseTypes = databaseTypes;
    }
    
    @Override
    public DatabaseType getDatabaseType(final String schemaName) {
        return databaseTypes.get(schemaName);
    }
    
    @Override
    public ShardingSphereMetaData getDefaultMetaData() {
        return metaDataMap.get(DefaultSchema.LOGIC_NAME);
    }
    
    @Override
    public void close() {
        executorEngine.close();
    }
}
