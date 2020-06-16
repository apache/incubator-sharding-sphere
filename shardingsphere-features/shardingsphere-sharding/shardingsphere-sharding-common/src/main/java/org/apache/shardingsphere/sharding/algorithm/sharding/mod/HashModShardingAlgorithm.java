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

package org.apache.shardingsphere.sharding.algorithm.sharding.mod;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.sharding.algorithm.sharding.ShardingAlgorithmException;
import org.apache.shardingsphere.sharding.api.sharding.ShardingAutoTableAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.Properties;

/**
 * Hash sharding algorithm.
 * 
 * <p>
 *     Shard by `y = z mod v` algorithm with z = hash(x), v is sharding count.
 *     All available targets will be returned if sharding value is {@code RangeShardingValue}.
 * </p>
 */
@Getter
@Setter
public final class HashModShardingAlgorithm implements StandardShardingAlgorithm<Comparable<?>>, ShardingAutoTableAlgorithm {
    
    private static final String SHARDING_COUNT_KEY = "sharding.count";
    
    private Properties props = new Properties();
    
    private int shardingCount;
    
    @Override
    public void init() {
        shardingCount = getShardingCount();
    }
    
    private int getShardingCount() {
        Preconditions.checkNotNull(props.getProperty(SHARDING_COUNT_KEY), "Sharding count cannot be null.");
        return Integer.parseInt(props.getProperty(SHARDING_COUNT_KEY));
    }
    
    @Override
    public String doSharding(final Collection<String> availableTargetNames, final PreciseShardingValue<Comparable<?>> shardingValue) {
        for (String each : availableTargetNames) {
            if (each.endsWith(hashShardingValue(shardingValue.getValue()) % shardingCount + "")) {
                return each;
            }
        }
        throw new ShardingAlgorithmException("Sharding failure, cannot find target name via `%s`", shardingValue);
    }
    
    @Override
    public Collection<String> doSharding(final Collection<String> availableTargetNames, final RangeShardingValue<Comparable<?>> shardingValue) {
        return availableTargetNames;
    }
    
    private long hashShardingValue(final Comparable<?> shardingValue) {
        return Math.abs((long) shardingValue.hashCode());
    }
    
    @Override
    public int getAutoTablesAmount() {
        return shardingCount;
    }
    
    @Override
    public String getType() {
        return "HASH_MOD";
    }
}
