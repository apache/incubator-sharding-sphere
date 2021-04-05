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

package org.apache.shardingsphere.infra.metadata.auth.builder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.infra.exception.ShardingSphereException;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.infra.metadata.auth.builder.loader.PrivilegeLoader;
import org.apache.shardingsphere.infra.metadata.auth.builder.loader.PrivilegeLoaderEngine;
import org.apache.shardingsphere.infra.metadata.auth.model.privilege.ShardingSpherePrivilege;
import org.apache.shardingsphere.infra.metadata.auth.model.user.ShardingSphereUser;
import org.apache.shardingsphere.infra.rule.ShardingSphereRule;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Privilege builder.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PrivilegeBuilder {
    
    private static final int CPU_CORES = Runtime.getRuntime().availableProcessors();
    
    private static final long FUTURE_GET_TIME_OUT_MILLISECONDS = 5000L;
    
    /**
     * Build privileges.
     *
     * @param databaseType database type
     * @param metaDataList meta data list
     * @param users users
     * @return privileges
     */
    public static Map<ShardingSphereUser, ShardingSpherePrivilege> build(final DatabaseType databaseType, 
                                                                         final Collection<ShardingSphereMetaData> metaDataList, final Collection<ShardingSphereUser> users) {
        if (metaDataList.isEmpty()) {
            return buildDefaultPrivileges(users);
        }
        Optional<PrivilegeLoader> loader = PrivilegeLoaderEngine.findPrivilegeLoader(databaseType);
        return loader.map(optional -> build(metaDataList, users, optional)).orElseGet(() -> buildDefaultPrivileges(users));
    }
    
    private static Map<ShardingSphereUser, ShardingSpherePrivilege> build(final Collection<ShardingSphereMetaData> metaDataList, 
                                                                          final Collection<ShardingSphereUser> users, final PrivilegeLoader loader) {
        Map<ShardingSphereUser, ShardingSpherePrivilege> result = new LinkedHashMap<>();
        for (ShardingSphereMetaData each : metaDataList) {
            result.putAll(build(each, users, loader));
        }
        return result;
    }
    
    private static Map<ShardingSphereUser, ShardingSpherePrivilege> build(final ShardingSphereMetaData metaData, final Collection<ShardingSphereUser> users, final PrivilegeLoader loader) {
        return build(metaData.getName(), metaData.getResource().getAllInstanceDataSources(), metaData.getRuleMetaData().getRules(), users, loader);
    }
    
    /**
     * Build privileges.
     * 
     * @param schemaName schema name
     * @param dataSources data sources
     * @param rules rules
     * @param users users
     * @param loader privilege loader
     * @return privileges
     */
    public static Map<ShardingSphereUser, ShardingSpherePrivilege> build(final String schemaName, final Collection<DataSource> dataSources, 
                                                                         final Collection<ShardingSphereRule> rules, final Collection<ShardingSphereUser> users, final PrivilegeLoader loader) {
        Map<ShardingSphereUser, Collection<ShardingSpherePrivilege>> result = load(dataSources, users, loader);
        checkPrivileges(result);
        return PrivilegeMerger.merge(result, schemaName, rules);
    }
    
    private static Map<ShardingSphereUser, Collection<ShardingSpherePrivilege>> load(final Collection<DataSource> dataSources, 
                                                                                     final Collection<ShardingSphereUser> users, final PrivilegeLoader loader) {
        Map<ShardingSphereUser, Collection<ShardingSpherePrivilege>> result = new LinkedHashMap<>(users.size(), 1);
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(CPU_CORES * 2, dataSources.size()));
        Collection<Future<Map<ShardingSphereUser, ShardingSpherePrivilege>>> futures = new HashSet<>(dataSources.size(), 1);
        for (DataSource each : dataSources) {
            futures.add(executorService.submit(() -> loader.load(users, each)));
        }
        futures.forEach(each -> {
            try {
                fillPrivileges(result, each);
            } catch (final InterruptedException | ExecutionException | TimeoutException ex) {
                throw new IllegalStateException(String.format("Error while loading privilege with %s", each), ex);
            }
        });
        executorService.shutdownNow();
        return result;
    }
    
    /**
     * Build default privileges.
     * 
     * @param users users
     * @return privileges
     */
    public static Map<ShardingSphereUser, ShardingSpherePrivilege> buildDefaultPrivileges(final Collection<ShardingSphereUser> users) {
        Map<ShardingSphereUser, ShardingSpherePrivilege> result = new LinkedHashMap<>(users.size(), 1);
        ShardingSpherePrivilege privilege = new ShardingSpherePrivilege();
        privilege.setSuperPrivilege();
        users.forEach(each -> result.put(each, privilege));
        return result;
    }
    
    private static void fillPrivileges(final Map<ShardingSphereUser, Collection<ShardingSpherePrivilege>> userPrivilegeMap,
                                       final Future<Map<ShardingSphereUser, ShardingSpherePrivilege>> future) throws InterruptedException, ExecutionException, TimeoutException {
        for (Entry<ShardingSphereUser, ShardingSpherePrivilege> entry : future.get(FUTURE_GET_TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS).entrySet()) {
            if (!userPrivilegeMap.containsKey(entry.getKey())) {
                userPrivilegeMap.put(entry.getKey(), new LinkedHashSet<>());
            }
            userPrivilegeMap.get(entry.getKey()).add(entry.getValue());
        }
    }
    
    private static void checkPrivileges(final Map<ShardingSphereUser, Collection<ShardingSpherePrivilege>> userPrivilegeMap) {
        for (Entry<ShardingSphereUser, Collection<ShardingSpherePrivilege>> entry : userPrivilegeMap.entrySet()) {
            for (ShardingSpherePrivilege each : entry.getValue()) {
                if (each.isEmpty()) {
                    throw new ShardingSphereException(String.format("There is no enough privileges for %s on all database instances.", entry.getKey().getGrantee()).toString().replaceAll("%", "%%"));
                }
            }
        }
    }
}
