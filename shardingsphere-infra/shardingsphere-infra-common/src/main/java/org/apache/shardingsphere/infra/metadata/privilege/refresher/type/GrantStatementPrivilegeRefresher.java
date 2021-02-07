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

package org.apache.shardingsphere.infra.metadata.privilege.refresher.type;

import org.apache.shardingsphere.infra.auth.Authentication;
import org.apache.shardingsphere.infra.metadata.privilege.refresher.PrivilegeRefresher;
import org.apache.shardingsphere.infra.metadata.schema.builder.SchemaBuilderMaterials;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;

/**
 * Grant statement privilege refresher.
 */
public final class GrantStatementPrivilegeRefresher implements PrivilegeRefresher {
    
    @Override
    public void refresh(final Authentication authentication, final SQLStatement sqlStatement, final SchemaBuilderMaterials materials) {
    }
}
