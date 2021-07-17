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
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.order.OrderBySegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.pagination.limit.LimitSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.OutputSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.WithSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.DeleteMultiTableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.SimpleTableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.SubqueryTableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dml.DeleteStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.handler.dml.DeleteStatementHandler;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.SQLCaseAssertContext;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.SQLSegmentAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.limit.LimitClauseAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.orderby.OrderByClauseAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.output.OutputClauseAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.table.TableAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.where.WhereClauseAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.with.WithClauseAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dml.DeleteStatementTestCase;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

/**
 * Delete statement assert.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DeleteStatementAssert {
    
    /**
     * Assert delete statement is correct with expected parser result.
     * 
     * @param assertContext assert context
     * @param actual actual delete statement
     * @param expected expected delete statement test case
     */
    public static void assertIs(final SQLCaseAssertContext assertContext, final DeleteStatement actual, final DeleteStatementTestCase expected) {
        assertWithClause(assertContext, actual, expected);
        assertTable(assertContext, actual, expected);
        assertOutput(assertContext, actual, expected);
        assertWhereClause(assertContext, actual, expected);
        assertOrderByClause(assertContext, actual, expected);
        assertLimitClause(assertContext, actual, expected);
    }
    
    private static void assertWithClause(final SQLCaseAssertContext assertContext, final DeleteStatement actual, final DeleteStatementTestCase expected) {
        Optional<WithSegment> withSegment = DeleteStatementHandler.getWithSegment(actual);
        if (null != expected.getWithClause()) {
            assertTrue(assertContext.getText("Actual with segment should exist."), withSegment.isPresent());
            WithClauseAssert.assertIs(assertContext, withSegment.get(), expected.getWithClause());
        } else {
            assertFalse(assertContext.getText("Actual with segment should not exist."), withSegment.isPresent());
        }
    }
    
    private static void assertTable(final SQLCaseAssertContext assertContext, final DeleteStatement actual, final DeleteStatementTestCase expected) {
        if (null != expected.getTables() && !expected.getTables().isEmpty()) {
            assertNotNull(assertContext.getText("Actual table segment should exist."), actual.getTableSegment());
            List<SimpleTableSegment> actualTableSegments = new LinkedList<>();
            if (actual.getTableSegment() instanceof SimpleTableSegment) {
                actualTableSegments.add((SimpleTableSegment) actual.getTableSegment());
            } else if (actual.getTableSegment() instanceof DeleteMultiTableSegment) {
                DeleteMultiTableSegment deleteMultiTableSegment = (DeleteMultiTableSegment) actual.getTableSegment();
                actualTableSegments.addAll(deleteMultiTableSegment.getActualDeleteTables());
            }
            TableAssert.assertIs(assertContext, actualTableSegments, expected.getTables());
        } else if (null != expected.getSubqueryTable()) {
            assertNotNull(assertContext.getText("Actual subquery table segment should exist."), actual.getTableSegment());
            TableAssert.assertIs(assertContext, (SubqueryTableSegment) actual.getTableSegment(), expected.getSubqueryTable());
        } else {
            assertNull(assertContext.getText("Actual table should not exist."), actual.getTableSegment());
        }
    }
    
    private static void assertOutput(final SQLCaseAssertContext assertContext, final DeleteStatement actual, final DeleteStatementTestCase expected) {
        Optional<OutputSegment> outputSegment = DeleteStatementHandler.getOutputSegment(actual);
        if (null != expected.getOutputClause()) {
            assertTrue(assertContext.getText("Actual output segment should exist."), outputSegment.isPresent());
            OutputClauseAssert.assertIs(assertContext, outputSegment.get(), expected.getOutputClause());
        } else {
            assertFalse(assertContext.getText("Actual output segment should not exist."), outputSegment.isPresent());
        }
    }
    
    private static void assertWhereClause(final SQLCaseAssertContext assertContext, final DeleteStatement actual, final DeleteStatementTestCase expected) {
        if (null != expected.getWhereClause()) {
            assertTrue(assertContext.getText("Actual where segment should exist."), actual.getWhere().isPresent());
            WhereClauseAssert.assertIs(assertContext, actual.getWhere().get(), expected.getWhereClause());
        } else {
            assertFalse(assertContext.getText("Actual where segment should not exist."), actual.getWhere().isPresent());
        }
    }
    
    private static void assertOrderByClause(final SQLCaseAssertContext assertContext, final DeleteStatement actual, final DeleteStatementTestCase expected) {
        Optional<OrderBySegment> orderBySegment = DeleteStatementHandler.getOrderBySegment(actual);
        if (null != expected.getOrderByClause()) {
            assertTrue(assertContext.getText("Actual order by segment should exist."), orderBySegment.isPresent());
            OrderByClauseAssert.assertIs(assertContext, orderBySegment.get(), expected.getOrderByClause());
        } else {
            assertFalse(assertContext.getText("Actual order by segment should not exist."), orderBySegment.isPresent());
        }
    }

    private static void assertLimitClause(final SQLCaseAssertContext assertContext, final DeleteStatement actual, final DeleteStatementTestCase expected) {
        Optional<LimitSegment> limitSegment = DeleteStatementHandler.getLimitSegment(actual);
        if (null != expected.getLimitClause()) {
            assertTrue(assertContext.getText("Actual limit segment should exist."), limitSegment.isPresent());
            LimitClauseAssert.assertRowCount(assertContext, limitSegment.get().getRowCount().orElse(null), expected.getLimitClause().getRowCount());
            SQLSegmentAssert.assertIs(assertContext, limitSegment.get(), expected.getLimitClause());
        } else {
            assertFalse(assertContext.getText("Actual limit segment should not exist."), limitSegment.isPresent());
        }
    }
}
