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

package org.apache.shardingsphere.governance.core.registry.listener;

import org.apache.shardingsphere.governance.core.event.listener.GovernanceListenerFactory;
import org.apache.shardingsphere.governance.core.registry.listener.metadata.MetaDataListener;
import org.apache.shardingsphere.governance.repository.api.RegistryRepository;
import org.apache.shardingsphere.governance.repository.api.listener.DataChangedEvent.Type;
import org.apache.shardingsphere.infra.spi.ShardingSphereServiceLoader;

import java.util.Collection;

/**
 * Registry listener manager.
 */
public final class RegistryListenerManager {
    
    static {
        ShardingSphereServiceLoader.register(GovernanceListenerFactory.class);
    }
    
    private final TerminalStateChangedListener terminalStateChangedListener;
    
    private final DataSourceStateChangedListener dataSourceStateChangedListener;
    
    private final LockChangedListener lockChangedListener;

    private final MetaDataListener metaDataListener;

    private final PropertiesChangedListener propertiesChangedListener;

    private final UserChangedListener userChangedListener;
    
    private final PrivilegeNodeChangedListener privilegeNodeChangedListener;
    
    private final RegistryRepository registryRepository;

    private final Collection<String> schemaNames;
    
    private final Collection<GovernanceListenerFactory> governanceListenerFactories;
    
    public RegistryListenerManager(final RegistryRepository registryRepository, final Collection<String> schemaNames) {
        terminalStateChangedListener = new TerminalStateChangedListener(registryRepository);
        dataSourceStateChangedListener = new DataSourceStateChangedListener(registryRepository, schemaNames);
        lockChangedListener = new LockChangedListener(registryRepository);
        metaDataListener = new MetaDataListener(registryRepository, schemaNames);
        propertiesChangedListener = new PropertiesChangedListener(registryRepository);
        userChangedListener = new UserChangedListener(registryRepository);
        privilegeNodeChangedListener = new PrivilegeNodeChangedListener(registryRepository);
        this.registryRepository = registryRepository;
        this.schemaNames = schemaNames;
        governanceListenerFactories = ShardingSphereServiceLoader.getSingletonServiceInstances(GovernanceListenerFactory.class);
    }
    
    /**
     * Initialize all state changed listeners.
     */
    public void initListeners() {
        terminalStateChangedListener.watch(Type.UPDATED);
        dataSourceStateChangedListener.watch(Type.UPDATED, Type.DELETED, Type.ADDED);
        lockChangedListener.watch(Type.ADDED, Type.DELETED);
        metaDataListener.watch();
        propertiesChangedListener.watch(Type.UPDATED);
        userChangedListener.watch(Type.UPDATED);
        privilegeNodeChangedListener.watch(Type.UPDATED);
        for (GovernanceListenerFactory each : governanceListenerFactories) {
            each.create(registryRepository, schemaNames).watch(each.getWatchTypes().toArray(new Type[0]));
        }
    }
}
