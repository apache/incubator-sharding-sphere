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

package org.apache.shardingsphere.proxy.backend.text;

import org.apache.shardingsphere.proxy.backend.context.ProxyContext;
import org.apache.shardingsphere.proxy.backend.exception.NoDatabaseSelectedException;
import org.apache.shardingsphere.proxy.backend.exception.UnknownDatabaseException;
import org.apache.shardingsphere.proxy.backend.response.header.ResponseHeader;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;

/**
 * Schema required backend handler.
 * 
 * @param <T> type of SQL statement
 */
public abstract class SchemaRequiredBackendHandler<T extends SQLStatement> extends AbstractBackendHandler<T> {
    
    private final T sqlStatement;
    
    private final String schemaName;

    public SchemaRequiredBackendHandler(final T sqlStatement, final String schemaName) {
        super(sqlStatement, schemaName);
        this.sqlStatement = sqlStatement;
        this.schemaName = schemaName;
    }
    
    @Override
    public final ResponseHeader execute() {
        checkSchema(getSchemaName(schemaName, sqlStatement));
        return super.execute();
    }
    
    private void checkSchema(final String schemaName) {
        if (null == schemaName) {
            throw new NoDatabaseSelectedException();
        }
        if (!ProxyContext.getInstance().schemaExists(schemaName)) {
            throw new UnknownDatabaseException(schemaName);
        }
    }
}
