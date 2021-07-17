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

package org.apache.shardingsphere.test.sql.parser.parameterized.asserts.statement.ddl.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleCommentStatement;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.SQLCaseAssertContext;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.column.ColumnAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.table.TableAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.CommentStatementTestCase;

/**
 * Comment statement assert.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentStatementAssert {
    
    /**
     * Assert comment statement is correct with expected parser result.
     * 
     * @param assertContext assert context
     * @param actual actual comment statement
     * @param expected expected comment statement test case
     */
    public static void assertIs(final SQLCaseAssertContext assertContext, final OracleCommentStatement actual, final CommentStatementTestCase expected) {
        assertTable(assertContext, actual, expected);
        assertColumn(assertContext, actual, expected);
    }
    
    private static void assertTable(final SQLCaseAssertContext assertContext, final OracleCommentStatement actual, final CommentStatementTestCase expected) {
        if (null != expected.getTable()) {
            TableAssert.assertIs(assertContext, actual.getTable(), expected.getTable());
        }
    }

    private static void assertColumn(final SQLCaseAssertContext assertContext, final OracleCommentStatement actual, final CommentStatementTestCase expected) {
        if (null != expected.getColumn()) {
            ColumnAssert.assertIs(assertContext, actual.getColumn(), expected.getColumn());
        }
    }

}
