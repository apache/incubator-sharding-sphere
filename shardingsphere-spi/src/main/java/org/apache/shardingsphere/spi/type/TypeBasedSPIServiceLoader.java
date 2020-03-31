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

package org.apache.shardingsphere.spi.type;

import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.spi.ShardingSphereServiceLoader;
import org.apache.shardingsphere.spi.exception.ServiceProviderNotFoundException;

import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Type based SPI service loader.
 * 
 * @param <T> type of algorithm class
 */
@RequiredArgsConstructor
public abstract class TypeBasedSPIServiceLoader<T extends TypeBasedSPI> {
    
    private final Class<T> classType;
    
    /**
     * Create new instance for type based SPI.
     * 
     * @param type type of SPI
     * @param props properties of SPI
     * @return SPI instance
     */
    public final T newService(final String type, final Properties props) {
        Collection<T> typeBasedServices = loadTypeBasedServices(type);
        if (typeBasedServices.isEmpty()) {
            throw new RuntimeException(String.format("Invalid `%s` SPI type `%s`.", classType.getName(), type));
        }
        T result = typeBasedServices.iterator().next();
        result.setProperties(props);
        return result;
    }
    
    /**
     * Create new service by default SPI type.
     *
     * @return type based SPI instance
     */
    public final T newService() {
        T result = loadFirstTypeBasedService();
        result.setProperties(new Properties());
        return result;
    }
    
    private Collection<T> loadTypeBasedServices(final String type) {
        return ShardingSphereServiceLoader.newServiceInstances(classType).stream().filter(each -> type.equalsIgnoreCase(each.getType())).collect(Collectors.toList());
    }
    
    private T loadFirstTypeBasedService() {
        Optional<T> result = ShardingSphereServiceLoader.newServiceInstances(classType).stream().findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        throw new ServiceProviderNotFoundException(classType);
    }
}
