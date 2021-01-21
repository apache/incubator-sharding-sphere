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

import com.google.common.base.Joiner;
import org.apache.shardingsphere.distsql.parser.segment.TableRuleSegment;
import org.apache.shardingsphere.distsql.parser.statement.rdl.AlterShardingRuleStatement;
import org.apache.shardingsphere.governance.core.event.model.rule.RuleConfigurationsPersistEvent;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.infra.eventbus.ShardingSphereEventBus;
import org.apache.shardingsphere.infra.yaml.swapper.YamlRuleConfigurationSwapperEngine;
import org.apache.shardingsphere.proxy.backend.communication.jdbc.connection.BackendConnection;
import org.apache.shardingsphere.proxy.backend.context.ProxyContext;
import org.apache.shardingsphere.proxy.backend.exception.ShardingRuleNotExistedException;
import org.apache.shardingsphere.proxy.backend.exception.ShardingTableRuleExistedException;
import org.apache.shardingsphere.proxy.backend.exception.ShardingTableRuleNotExistedException;
import org.apache.shardingsphere.proxy.backend.response.header.ResponseHeader;
import org.apache.shardingsphere.proxy.backend.response.header.update.UpdateResponseHeader;
import org.apache.shardingsphere.proxy.backend.text.SchemaRequiredBackendHandler;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingAutoTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.converter.ShardingRuleStatementConverter;
import org.apache.shardingsphere.sharding.yaml.config.YamlShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.yaml.config.rule.YamlShardingAutoTableRuleConfiguration;
import org.apache.shardingsphere.sharding.yaml.config.rule.YamlTableRuleConfiguration;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

/**
 * Alter sharding rule backend handler.
 */
public final class AlterShardingRuleBackendHandler extends SchemaRequiredBackendHandler<AlterShardingRuleStatement> {
    
    public AlterShardingRuleBackendHandler(final AlterShardingRuleStatement sqlStatement, final BackendConnection backendConnection) {
        super(sqlStatement, backendConnection);
    }
    
    @Override
    public ResponseHeader execute(final String schemaName, final AlterShardingRuleStatement sqlStatement) {
        Optional<ShardingRuleConfiguration> shardingRuleConfig = ProxyContext.getInstance().getMetaData(schemaName).getRuleMetaData().getConfigurations().stream()
                .filter(each -> each instanceof ShardingRuleConfiguration).map(each -> (ShardingRuleConfiguration) each).findFirst();
        if (!shardingRuleConfig.isPresent()) {
            throw new ShardingRuleNotExistedException();
        }
        check(shardingRuleConfig.get(), sqlStatement);
        Optional<YamlShardingRuleConfiguration> yamlShardingRuleConfig = new YamlRuleConfigurationSwapperEngine().swapToYamlConfigurations(Collections.singleton(shardingRuleConfig.get())).stream()
                .filter(each -> each instanceof YamlShardingRuleConfiguration).map(each -> (YamlShardingRuleConfiguration) each).findFirst();
        if (!yamlShardingRuleConfig.isPresent()) {
            throw new ShardingRuleNotExistedException();
        }
        YamlShardingRuleConfiguration rule = yamlShardingRuleConfig.get();
        alter(rule, sqlStatement);
        Collection<RuleConfiguration> rules = new YamlRuleConfigurationSwapperEngine().swapToRuleConfigurations(Collections.singleton(rule));
        post(schemaName, rules);
        return new UpdateResponseHeader(sqlStatement);
    }
    
    private void check(final ShardingRuleConfiguration shardingRuleConfig, final AlterShardingRuleStatement statement) {
        checkModifyRule(shardingRuleConfig, statement);
        checkAddRule(shardingRuleConfig, statement);
    }
    
    private void checkModifyRule(final ShardingRuleConfiguration shardingRuleConfig, final AlterShardingRuleStatement statement) {
        Collection<String> notExistRules = new LinkedList<>();
        for (TableRuleSegment each : statement.getModifyShardingRules()) {
            Optional<ShardingTableRuleConfiguration> existTable = shardingRuleConfig.getTables().stream().filter(t -> t.getLogicTable().equals(each.getLogicTable())).findFirst();
            if (existTable.isPresent()) {
                continue;
            }
            Optional<ShardingAutoTableRuleConfiguration> existAutoTable = shardingRuleConfig.getAutoTables().stream().filter(t -> t.getLogicTable().equals(each.getLogicTable())).findFirst();
            if (existAutoTable.isPresent()) {
                continue;
            }
            notExistRules.add(each.getLogicTable());
        }
        if (!notExistRules.isEmpty()) {
            throw new ShardingTableRuleNotExistedException(notExistRules);
        }
    }
    
    private void checkAddRule(final ShardingRuleConfiguration shardingRuleConfig, final AlterShardingRuleStatement statement) {
        Collection<String> existRules = new LinkedList<>();
        for (TableRuleSegment each : statement.getModifyShardingRules()) {
            Optional<ShardingTableRuleConfiguration> existTable = shardingRuleConfig.getTables().stream().filter(t -> !t.getLogicTable().equals(each.getLogicTable())).findFirst();
            if (existTable.isPresent()) {
                continue;
            }
            Optional<ShardingAutoTableRuleConfiguration> existAutoTable = shardingRuleConfig.getAutoTables().stream().filter(t -> !t.getLogicTable().equals(each.getLogicTable())).findFirst();
            if (existAutoTable.isPresent()) {
                continue;
            }
            existRules.add(each.getLogicTable());
        }
        if (!existRules.isEmpty()) {
            throw new ShardingTableRuleExistedException(existRules);
        }
    }
    
    private void alter(final YamlShardingRuleConfiguration yamlShardingRuleConfig, final AlterShardingRuleStatement statement) {
        modifyTableRule(yamlShardingRuleConfig, statement);
        addTableRule(yamlShardingRuleConfig, statement);
    }
    
    private void modifyTableRule(final YamlShardingRuleConfiguration yamlShardingRuleConfig, final AlterShardingRuleStatement statement) {
        for (TableRuleSegment each : statement.getModifyShardingRules()) {
            YamlTableRuleConfiguration existTable = yamlShardingRuleConfig.getTables().get(each.getLogicTable());
            if (null != existTable) {
                if (null != each.getDataSources()) {
                    existTable.setActualDataNodes(Joiner.on(",").join(each.getDataSources()));
                }
                if (null != each.getTableStrategy()) {
                    existTable.setTableStrategy(ShardingRuleStatementConverter.createTableStrategyConfiguration(each));
                    yamlShardingRuleConfig.getShardingAlgorithms().put(ShardingRuleStatementConverter.getAlgorithmName(each.getLogicTable(), each.getTableStrategy().getAlgorithmName()),
                            ShardingRuleStatementConverter.createAlgorithmConfiguration(each.getTableStrategy()));
                }
                if (null != each.getDatabaseStrategy()) {
                    existTable.setDatabaseStrategy(ShardingRuleStatementConverter.createDatabaseStrategyConfiguration(each));
                    yamlShardingRuleConfig.getShardingAlgorithms().put(ShardingRuleStatementConverter.getAlgorithmName(each.getLogicTable(), each.getDatabaseStrategy().getAlgorithmName()),
                            ShardingRuleStatementConverter.createAlgorithmConfiguration(each.getDatabaseStrategy()));
                }
                if (null != each.getKeyGenerateStrategy()) {
                    existTable.setKeyGenerateStrategy(ShardingRuleStatementConverter.createKeyGenerateStrategyConfiguration(each));
                    yamlShardingRuleConfig.getShardingAlgorithms().put(ShardingRuleStatementConverter.getAlgorithmName(each.getLogicTable(), each.getKeyGenerateStrategy().getAlgorithmName()),
                            ShardingRuleStatementConverter.createAlgorithmConfiguration(each.getKeyGenerateStrategy()));
                }
                continue;
            }
            YamlShardingAutoTableRuleConfiguration existAutoTable = yamlShardingRuleConfig.getAutoTables().get(each.getLogicTable());
            if (null != each.getDataSources()) {
                existAutoTable.setActualDataSources(Joiner.on(",").join(each.getDataSources()));
            }
            if (null != each.getTableStrategy()) {
                existAutoTable.setShardingStrategy(ShardingRuleStatementConverter.createTableStrategyConfiguration(each));
                yamlShardingRuleConfig.getShardingAlgorithms().put(ShardingRuleStatementConverter.getAlgorithmName(each.getLogicTable(), each.getTableStrategy().getAlgorithmName()),
                        ShardingRuleStatementConverter.createAlgorithmConfiguration(each.getTableStrategy()));
            }
            if (null != each.getKeyGenerateStrategy()) {
                existAutoTable.setKeyGenerateStrategy(ShardingRuleStatementConverter.createKeyGenerateStrategyConfiguration(each));
                yamlShardingRuleConfig.getShardingAlgorithms().put(ShardingRuleStatementConverter.getAlgorithmName(each.getLogicTable(), each.getKeyGenerateStrategy().getAlgorithmName()),
                        ShardingRuleStatementConverter.createAlgorithmConfiguration(each.getKeyGenerateStrategy()));
            }
        }
    }
    
    private void addTableRule(final YamlShardingRuleConfiguration yamlShardingRuleConfig, final AlterShardingRuleStatement statement) {
        for (TableRuleSegment each : statement.getAddShardingRules()) {
            YamlTableRuleConfiguration table = new YamlTableRuleConfiguration();
            if (null != each.getDataSources()) {
                table.setActualDataNodes(Joiner.on(",").join(each.getDataSources()));
            }
            if (null != each.getTableStrategy()) {
                table.setTableStrategy(ShardingRuleStatementConverter.createTableStrategyConfiguration(each));
                yamlShardingRuleConfig.getShardingAlgorithms().put(ShardingRuleStatementConverter.getAlgorithmName(each.getLogicTable(), each.getTableStrategy().getAlgorithmName()),
                        ShardingRuleStatementConverter.createAlgorithmConfiguration(each.getTableStrategy()));
            }
            if (null != each.getDatabaseStrategy()) {
                table.setDatabaseStrategy(ShardingRuleStatementConverter.createDatabaseStrategyConfiguration(each));
                yamlShardingRuleConfig.getShardingAlgorithms().put(ShardingRuleStatementConverter.getAlgorithmName(each.getLogicTable(), each.getDatabaseStrategy().getAlgorithmName()),
                        ShardingRuleStatementConverter.createAlgorithmConfiguration(each.getDatabaseStrategy()));
            }
            if (null != each.getKeyGenerateStrategy()) {
                table.setKeyGenerateStrategy(ShardingRuleStatementConverter.createKeyGenerateStrategyConfiguration(each));
                yamlShardingRuleConfig.getShardingAlgorithms().put(ShardingRuleStatementConverter.getAlgorithmName(each.getLogicTable(), each.getKeyGenerateStrategy().getAlgorithmName()),
                        ShardingRuleStatementConverter.createAlgorithmConfiguration(each.getKeyGenerateStrategy()));
            }
        }
    }
    
    private void post(final String schemaName, final Collection<RuleConfiguration> rules) {
        ShardingSphereEventBus.getInstance().post(new RuleConfigurationsPersistEvent(schemaName, rules));
    }
}
