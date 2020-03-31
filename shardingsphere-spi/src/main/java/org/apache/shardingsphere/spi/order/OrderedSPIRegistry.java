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

package org.apache.shardingsphere.spi.order;

import org.apache.shardingsphere.spi.NewInstanceServiceLoader;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Ordered registry.
 */
public final class OrderedSPIRegistry {
    
    /**
     * Get registered classes.
     * 
     * @param orderBasedClass class of order based
     * @param <T> type of order aware class
     * @return registered classes
     */
    @SuppressWarnings("unchecked")
    public static <T extends OrderBasedSPI> Collection<Class<T>> getRegisteredClasses(final Class<T> orderBasedClass) {
        Map<Integer, Class<T>> result = new TreeMap<>();
        for (T each : NewInstanceServiceLoader.newServiceInstances(orderBasedClass)) {
            result.put(each.getOrder(), (Class<T>) each.getClass());
        }
        return result.values();
    }
}
