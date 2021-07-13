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

package org.apache.shardingsphere.governance.core.registry.config.service.impl;

import org.apache.shardingsphere.governance.repository.spi.RegistryCenterRepository;
import org.apache.shardingsphere.infra.config.properties.ConfigurationPropertyKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class PropertiesRegistryServiceTest {
    
    private static final String PROPS_YAML = ConfigurationPropertyKey.SQL_SHOW.getKey() + ": false\n";
    
    @Mock
    private RegistryCenterRepository repository;
    
    @Test
    public void assertLoad() {
        when(repository.get("/props")).thenReturn(PROPS_YAML);
        Properties actual = new PropertiesRegistryService(repository).load();
        assertThat(actual.get(ConfigurationPropertyKey.SQL_SHOW.getKey()), is(Boolean.FALSE));
    }
}
