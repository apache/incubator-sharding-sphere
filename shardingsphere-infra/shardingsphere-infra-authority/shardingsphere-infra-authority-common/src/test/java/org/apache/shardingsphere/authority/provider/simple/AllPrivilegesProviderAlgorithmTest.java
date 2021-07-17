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

import java.util.Collections;
import java.util.Optional;
import org.apache.shardingsphere.authority.model.ShardingSpherePrivileges;
import org.apache.shardingsphere.authority.provider.natived.model.subject.SchemaAccessSubject;
import org.apache.shardingsphere.infra.metadata.user.Grantee;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AllPrivilegesProviderAlgorithmTest {

    @Test
    public void assertFindPrivileges() {
        AllPrivilegesPermittedAuthorityProviderAlgorithm authorityProviderAlgorithm = new AllPrivilegesPermittedAuthorityProviderAlgorithm();
        Optional<ShardingSpherePrivileges> shardingSpherePrivilegesOptional = authorityProviderAlgorithm
            .findPrivileges(new Grantee("TestUser", "testHost"));
        assertNotNull(shardingSpherePrivilegesOptional.get());
        assertTrue(shardingSpherePrivilegesOptional.get().hasPrivileges("testSchema"));
        assertTrue(shardingSpherePrivilegesOptional.get().hasPrivileges(Collections.emptyList()));
        assertTrue(shardingSpherePrivilegesOptional.get()
            .hasPrivileges(new SchemaAccessSubject("testSchema"), Collections.emptyList()));
    }
}
