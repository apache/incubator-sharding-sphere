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

package org.apache.shardingsphere.underlying.common.rule;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Data nodes.
 */
@RequiredArgsConstructor
public final class DataNodes {
    
    private final Collection<BaseRule> rules;
    
    /**
     * Get data nodes.
     * 
     * @param tableName table name
     * @return data nodes
     */
    public Collection<DataNode> getDataNodes(final String tableName) {
        Optional<TablesAggregationRule> tablesAggregationRule = rules.stream().filter(each -> each instanceof TablesAggregationRule).findFirst().map(rule -> (TablesAggregationRule) rule);
        if (!tablesAggregationRule.isPresent()) {
            return Collections.emptyList();
        }
        Collection<DataNode> result = new LinkedList<>(tablesAggregationRule.get().getAllDataNodes().get(tableName));
        for (BaseRule each : rules) {
            if (each instanceof TablesAggregationRule) {
                continue;
            }
            for (Entry<String, Collection<String>> entry : each.getDataSourceMapper().entrySet()) {
                Collection<DataNode> dataNodes = find(result, entry.getKey());
                result.removeAll(dataNodes);
                result.addAll(regenerate(dataNodes, entry.getValue()));
            }
        }
        return result;
    }
    
    private Collection<DataNode> find(final Collection<DataNode> dataNodes, final String logicDataSource) {
        return dataNodes.stream().filter(each -> each.getDataSourceName().equals(logicDataSource)).collect(Collectors.toList());
    }
    
    private Collection<DataNode> regenerate(final Collection<DataNode> dataNodes, final Collection<String> actualDataSources) {
        Collection<DataNode> result = new LinkedHashSet<>();
        for (DataNode each : dataNodes) {
            result.addAll(regenerate(actualDataSources, each.getTableName()));
        }
        return result;
    }
    
    private Collection<DataNode> regenerate(final Collection<String> dataSources, final String table) {
        Collection<DataNode> result = new LinkedHashSet<>();
        for (String each : dataSources) {
            result.add(new DataNode(each, table));
        }
        return result;
    }
}
