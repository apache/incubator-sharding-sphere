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

package org.apache.shardingsphere.proxy.backend.text.distsql.rdl.impl;

import org.apache.shardingsphere.distsql.parser.segment.TableRuleSegment;
import org.apache.shardingsphere.distsql.parser.statement.rdl.create.impl.CreateShardingRuleStatement;
import org.apache.shardingsphere.governance.core.event.model.rule.RuleConfigurationsAlteredEvent;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.infra.eventbus.ShardingSphereEventBus;
import org.apache.shardingsphere.infra.yaml.swapper.YamlRuleConfigurationSwapperEngine;
import org.apache.shardingsphere.proxy.backend.exception.ShardingTableRuleNotExistedException;
import org.apache.shardingsphere.proxy.backend.response.header.ResponseHeader;
import org.apache.shardingsphere.proxy.backend.response.header.update.UpdateResponseHeader;
import org.apache.shardingsphere.proxy.backend.text.SchemaRequiredBackendHandler;
import org.apache.shardingsphere.sharding.converter.ShardingRuleStatementConverter;
import org.apache.shardingsphere.sharding.yaml.config.YamlShardingRuleConfiguration;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Create sharding rule backend handler.
 */
public final class CreateShardingRuleBackendHandler extends SchemaRequiredBackendHandler<CreateShardingRuleStatement> {
    
    public CreateShardingRuleBackendHandler(final CreateShardingRuleStatement sqlStatement, final String schemaName) {
        super(sqlStatement, schemaName);
    }
    
    @Override
    public ResponseHeader execute(final String schemaName, final CreateShardingRuleStatement sqlStatement) {
        check(sqlStatement);
        YamlShardingRuleConfiguration config = ShardingRuleStatementConverter.convert(sqlStatement);
        Collection<RuleConfiguration> rules = new YamlRuleConfigurationSwapperEngine().swapToRuleConfigurations(Collections.singleton(config));
        post(schemaName, rules);
        return new UpdateResponseHeader(sqlStatement);
    }
    
    private void check(final CreateShardingRuleStatement sqlStatement) {
        Collection<String> validTables = sqlStatement.getTables().stream().map(TableRuleSegment::getLogicTable).collect(Collectors.toList());
        Collection<String> invalidTables = new LinkedList<>();
        for (Collection<String> each : sqlStatement.getBindingTables()) {
            for (String t : each) {
                if (!validTables.contains(t)) {
                    invalidTables.add(t);
                }
            }
        }
        if (!invalidTables.isEmpty()) {
            throw new ShardingTableRuleNotExistedException(invalidTables);
        }
    }
    
    private void post(final String schemaName, final Collection<RuleConfiguration> rules) {
        ShardingSphereEventBus.postEvent(new RuleConfigurationsAlteredEvent(schemaName, rules));
    }
}
