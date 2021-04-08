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

package org.apache.shardingsphere.db.discovery.common.rule.biulder;

import org.apache.shardingsphere.db.discovery.api.config.DatabaseDiscoveryRuleConfiguration;
import org.apache.shardingsphere.db.discovery.api.config.rule.DatabaseDiscoveryDataSourceRuleConfiguration;
import org.apache.shardingsphere.db.discovery.common.constant.DatabaseDiscoveryOrder;
import org.apache.shardingsphere.db.discovery.common.rule.DatabaseDiscoveryRule;
import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.infra.metadata.user.ShardingSphereUser;
import org.apache.shardingsphere.infra.rule.ShardingSphereRule;
import org.apache.shardingsphere.infra.rule.builder.SchemaRuleBuilder;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Database discovery rule builder.
 */
public final class DatabaseDiscoveryRuleBuilder implements SchemaRuleBuilder<DatabaseDiscoveryRule, DatabaseDiscoveryRuleConfiguration> {
    
    @Override
    public DatabaseDiscoveryRule build(final String schemaName, final Map<String, DataSource> dataSourceMap, final DatabaseType databaseType, 
                                       final DatabaseDiscoveryRuleConfiguration ruleConfig, final Collection<ShardingSphereUser> users, final Collection<ShardingSphereRule> builtRules) {
        Map<String, DataSource> realDataSourceMap = new HashMap<>();
        for (DatabaseDiscoveryDataSourceRuleConfiguration each : ruleConfig.getDataSources()) {
            for (String datasourceName : each.getDataSourceNames()) {
                realDataSourceMap.put(datasourceName, dataSourceMap.get(datasourceName));
            }
        }
        return new DatabaseDiscoveryRule(ruleConfig, databaseType, realDataSourceMap, schemaName);
    }
    
    @Override
    public int getOrder() {
        return DatabaseDiscoveryOrder.ORDER;
    }
    
    @Override
    public Class<DatabaseDiscoveryRuleConfiguration> getTypeClass() {
        return DatabaseDiscoveryRuleConfiguration.class;
    }
    
}
