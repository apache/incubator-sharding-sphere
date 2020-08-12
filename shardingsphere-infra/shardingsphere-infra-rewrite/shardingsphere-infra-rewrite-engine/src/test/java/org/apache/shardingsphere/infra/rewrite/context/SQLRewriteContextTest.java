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

package org.apache.shardingsphere.infra.rewrite.context;

import org.apache.shardingsphere.infra.rewrite.parameter.builder.impl.GroupedParameterBuilder;
import org.apache.shardingsphere.infra.rewrite.parameter.builder.impl.StandardParameterBuilder;
import org.apache.shardingsphere.sql.parser.binder.metadata.schema.SchemaMetaData;
import org.apache.shardingsphere.sql.parser.binder.statement.dml.InsertStatementContext;
import org.apache.shardingsphere.sql.parser.binder.statement.dml.SelectStatementContext;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class SQLRewriteContextTest {
    
    @Test
    public void assertInsertStatementContext() {
        InsertStatementContext statementContext = mock(InsertStatementContext.class);
        SQLRewriteContext sqlRewriteContext = new SQLRewriteContext(mock(SchemaMetaData.class), statementContext, "INSERT INTO tbl VALUES (?)", Collections.singletonList(1));
        assertThat(sqlRewriteContext.getParameterBuilder(), instanceOf(GroupedParameterBuilder.class));
    }
    
    @Test
    public void assertNotInsertStatementContext() {
        SelectStatementContext statementContext = mock(SelectStatementContext.class);
        SQLRewriteContext sqlRewriteContext = new SQLRewriteContext(mock(SchemaMetaData.class), statementContext, "INSERT INTO tbl VALUES (?)", Collections.singletonList(1));
        assertThat(sqlRewriteContext.getParameterBuilder(), instanceOf(StandardParameterBuilder.class));
    }
}
