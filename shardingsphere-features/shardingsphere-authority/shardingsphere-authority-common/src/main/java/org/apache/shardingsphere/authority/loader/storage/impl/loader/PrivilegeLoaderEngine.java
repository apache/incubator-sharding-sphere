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

package org.apache.shardingsphere.authority.loader.storage.impl.loader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.infra.spi.ShardingSphereServiceLoader;

import java.util.Optional;

/**
 * Privilege loader engine.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PrivilegeLoaderEngine {
    
    static {
        ShardingSphereServiceLoader.register(PrivilegeLoader.class);
    }
    
    /**
     * Find privilege loader.
     *
     * @param databaseType database type
     * @return privilege loader
     */
    public static Optional<PrivilegeLoader> findPrivilegeLoader(final DatabaseType databaseType) {
        for (PrivilegeLoader each : ShardingSphereServiceLoader.getSingletonServiceInstances(PrivilegeLoader.class)) {
            if (each.getType().equals(databaseType.getName())) {
                return Optional.of(each);
            }
        }
        return Optional.empty();
    }
}
