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

package org.apache.shardingsphere.orchestration.center.instance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.underlying.common.constant.properties.TypedPropertiesKey;

/**
 * Zookeeper properties enum.
 *
 * @author dongzonglei
 */
@RequiredArgsConstructor
@Getter
public enum ZookeeperPropertiesEnum implements TypedPropertiesKey {
    
    RETRY_INTERVAL_MILLISECONDS("retryIntervalMilliseconds", String.valueOf(500), int.class),
    
    MAX_RETRIES("maxRetries", String.valueOf(3), int.class),
    
    TIME_TO_LIVE_SECONDS("timeToLiveSeconds", String.valueOf(60), int.class),
    
    OPERATION_TIMEOUT_MILLISECONDS("operationTimeoutMilliseconds", String.valueOf(500), int.class),
    
    DIGEST("digest", "", String.class);
    
    private final String key;
    
    private final String defaultValue;
    
    private final Class<?> type;
}
