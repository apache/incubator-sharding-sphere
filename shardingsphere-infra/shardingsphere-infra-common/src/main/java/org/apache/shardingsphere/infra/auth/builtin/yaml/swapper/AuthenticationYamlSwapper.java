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

package org.apache.shardingsphere.infra.auth.builtin.yaml.swapper;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.apache.shardingsphere.infra.auth.ShardingSphereUser;
import org.apache.shardingsphere.infra.auth.builtin.DefaultAuthentication;
import org.apache.shardingsphere.infra.auth.builtin.yaml.config.YamlAuthenticationConfiguration;
import org.apache.shardingsphere.infra.auth.builtin.yaml.config.YamlUserConfiguration;
import org.apache.shardingsphere.infra.yaml.swapper.YamlSwapper;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Authentication YAML swapper.
 */
public final class AuthenticationYamlSwapper implements YamlSwapper<YamlAuthenticationConfiguration, DefaultAuthentication> {
    
    @Override
    public YamlAuthenticationConfiguration swapToYamlConfiguration(final DefaultAuthentication data) {
        YamlAuthenticationConfiguration result = new YamlAuthenticationConfiguration();
        Map<String, YamlUserConfiguration> users = new LinkedHashMap<>();
        for (ShardingSphereUser each : data.getUsers()) {
            users.put(each.getUsername(), swapToYamlConfiguration(each));
        }
        result.setUsers(users);
        return result;
    }
    
    private YamlUserConfiguration swapToYamlConfiguration(final ShardingSphereUser data) {
        YamlUserConfiguration result = new YamlUserConfiguration();
        result.setHostname(data.getHostname());
        result.setPassword(data.getPassword());
        String authorizedSchemas = null == data.getAuthorizedSchemas() ? "" : Joiner.on(',').join(data.getAuthorizedSchemas());
        result.setAuthorizedSchemas(authorizedSchemas);
        return result;
    }
    
    @Override
    public DefaultAuthentication swapToObject(final YamlAuthenticationConfiguration yamlConfig) {
        DefaultAuthentication result = new DefaultAuthentication();
        if (null == yamlConfig) {
            return result;
        }
        for (Entry<String, YamlUserConfiguration> entry : yamlConfig.getUsers().entrySet()) {
            result.getUsers().add(swapToObject(entry.getKey(), entry.getValue()));
        }
        return result;
    }
    
    private ShardingSphereUser swapToObject(final String username, final YamlUserConfiguration yamlConfig) {
        if (Strings.isNullOrEmpty(yamlConfig.getAuthorizedSchemas())) {
            return new ShardingSphereUser(username, yamlConfig.getPassword(), null == yamlConfig.getHostname() ? "" : yamlConfig.getHostname(), Collections.emptyList());
        }
        return new ShardingSphereUser(username, yamlConfig.getPassword(), null == yamlConfig.getHostname() ? "" : yamlConfig.getHostname(),
                Splitter.on(',').trimResults().splitToList(yamlConfig.getAuthorizedSchemas()));
    }
}
