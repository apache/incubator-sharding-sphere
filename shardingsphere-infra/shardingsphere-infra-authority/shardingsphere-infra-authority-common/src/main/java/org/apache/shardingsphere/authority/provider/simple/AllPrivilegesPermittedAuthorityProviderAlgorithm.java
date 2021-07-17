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

package org.apache.shardingsphere.authority.provider.simple;

import org.apache.shardingsphere.authority.model.AccessSubject;
import org.apache.shardingsphere.authority.model.PrivilegeType;
import org.apache.shardingsphere.authority.model.ShardingSpherePrivileges;
import org.apache.shardingsphere.authority.spi.AuthorityProvideAlgorithm;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.infra.metadata.user.Grantee;
import org.apache.shardingsphere.infra.metadata.user.ShardingSphereUser;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * All privileges permitted authority provider algorithm.
 */
public final class AllPrivilegesPermittedAuthorityProviderAlgorithm implements AuthorityProvideAlgorithm {
    
    private static final ShardingSpherePrivileges INSTANCE = new AllPrivilegesPermittedShardingSpherePrivileges();
    
    @Override
    public void init(final Map<String, ShardingSphereMetaData> mataDataMap, final Collection<ShardingSphereUser> users) {
    }
    
    @Override
    public void refresh(final Map<String, ShardingSphereMetaData> mataDataMap, final Collection<ShardingSphereUser> users) {
    }
    
    @Override
    public Optional<ShardingSpherePrivileges> findPrivileges(final Grantee grantee) {
        return Optional.of(INSTANCE);
    }
    
    @Override
    public String getType() {
        return "ALL_PRIVILEGES_PERMITTED";
    }
    
    private static final class AllPrivilegesPermittedShardingSpherePrivileges implements ShardingSpherePrivileges {
    
        @Override
        public void setSuperPrivilege() {
        }
    
        @Override
        public boolean hasPrivileges(final String schema) {
            return true;
        }
    
        @Override
        public boolean hasPrivileges(final Collection<PrivilegeType> privileges) {
            return true;
        }
    
        @Override
        public boolean hasPrivileges(final AccessSubject accessSubject, final Collection<PrivilegeType> privileges) {
            return true;
        }
    }
}
