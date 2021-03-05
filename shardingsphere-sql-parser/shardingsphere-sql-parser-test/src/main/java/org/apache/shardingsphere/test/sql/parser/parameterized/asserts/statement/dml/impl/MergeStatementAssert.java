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

package org.apache.shardingsphere.test.sql.parser.parameterized.asserts.statement.dml.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dml.MergeStatement;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.SQLCaseAssertContext;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.expression.ExpressionAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.table.TableAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dml.MergeStatementTestCase;

/**
 * Merge statement assert.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MergeStatementAssert {
    
    /**
     * Assert merge statement is correct with expected parser result.
     *
     * @param assertContext assert context
     * @param actual actual merge statement
     * @param expected expected parser result
     */
    public static void assertIs(final SQLCaseAssertContext assertContext, final MergeStatement actual, final MergeStatementTestCase expected) {
        assertTable(assertContext, actual, expected);
        assertExpression(assertContext, actual, expected);
    }
    
    private static void assertTable(final SQLCaseAssertContext assertContext, final MergeStatement actual, final MergeStatementTestCase expected) {
        if (null != expected.getTables()) {
            TableAssert.assertIs(assertContext, actual.getTables(), expected.getTables());
        }
        if (null != expected.getSubqueryTable()) {
            TableAssert.assertIs(assertContext, actual.getSubqueryTableSegment(), expected.getSubqueryTable());
        }
    }
    
    private static void assertExpression(final SQLCaseAssertContext assertContext, final MergeStatement actual, final MergeStatementTestCase expected) {
        ExpressionAssert.assertExpression(assertContext, actual.getExpr(), expected.getExpr());
    }
    
}
