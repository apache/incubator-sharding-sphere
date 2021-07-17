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

package org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.orderby;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.SQLCaseAssertContext;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.SQLSegmentAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.expression.ExpressionAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.owner.OwnerAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.value.IdentifierValueAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.orderby.ExpectedOrderByClause;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.orderby.item.ExpectedOrderByItem;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.orderby.item.impl.ExpectedColumnOrderByItem;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.orderby.item.impl.ExpectedExpressionOrderByItem;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.orderby.item.impl.ExpectedIndexOrderByItem;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.order.item.ColumnOrderByItemSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.order.item.ExpressionOrderByItemSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.order.item.IndexOrderByItemSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.order.item.OrderByItemSegment;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Order by item assert.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderByItemAssert {
    
    /**
     * Assert actual order by segment is correct with expected order by.
     *
     * @param assertContext assert context
     * @param actual actual order by segments
     * @param expected expected order by
     * @param type type of assertion, should be Order by or Group by
     */
    public static void assertIs(final SQLCaseAssertContext assertContext,
                                final Collection<OrderByItemSegment> actual, final ExpectedOrderByClause expected, final String type) {
        assertThat(assertContext.getText(String.format("%s items size assertion error: ", type)), actual.size(), is(expected.getItemSize()));
        int count = 0;
        for (OrderByItemSegment each : actual) {
            if (each instanceof ColumnOrderByItemSegment) {
                assertOrderInfo(assertContext, each, expected.getColumnItems().get(count), type);
                assertColumnOrderByItem(assertContext, (ColumnOrderByItemSegment) each, expected.getColumnItems().get(count), type);
                count++;
            }
        }
        count = 0;
        for (OrderByItemSegment each : actual) {
            if (each instanceof IndexOrderByItemSegment) {
                assertOrderInfo(assertContext, each, expected.getIndexItems().get(count), type);
                assertIndexOrderByItem(assertContext, (IndexOrderByItemSegment) each, expected.getIndexItems().get(count), type);
                count++;
            }
        }
        count = 0;
        for (OrderByItemSegment each : actual) {
            if (each instanceof ExpressionOrderByItemSegment) {
                assertOrderInfo(assertContext, each, expected.getExpressionItems().get(count), type);
                assertExpressionOrderByItem(assertContext, (ExpressionOrderByItemSegment) each, expected.getExpressionItems().get(count), type);
                if (null != ((ExpressionOrderByItemSegment) each).getExpr() && null != expected.getExpressionItems().get(count).getExpr()) {
                    ExpressionAssert.assertExpression(assertContext, ((ExpressionOrderByItemSegment) each).getExpr(), expected.getExpressionItems().get(count).getExpr());
                }
                count++;
            }
        }
    }
    
    private static void assertOrderInfo(final SQLCaseAssertContext assertContext, final OrderByItemSegment actual, final ExpectedOrderByItem expected, final String type) {
        assertThat(assertContext.getText(String.format("%s item order direction assertion error: ", type)),
                null != actual.getOrderDirection() ? actual.getOrderDirection().name() : actual.getNullOrderDirection().name(), is(expected.getOrderDirection()));
    }
    
    private static void assertColumnOrderByItem(final SQLCaseAssertContext assertContext,
                                                final ColumnOrderByItemSegment actual, final ExpectedColumnOrderByItem expected, final String type) {
        IdentifierValueAssert.assertIs(assertContext, actual.getColumn().getIdentifier(), expected, String.format("%s item", type));
        if (null != expected.getOwner()) {
            assertTrue(assertContext.getText("Actual owner should exist."), actual.getColumn().getOwner().isPresent());
            OwnerAssert.assertIs(assertContext, actual.getColumn().getOwner().get(), expected.getOwner());
        } else {
            assertFalse(assertContext.getText("Actual owner should not exist."), actual.getColumn().getOwner().isPresent());
        }
        SQLSegmentAssert.assertIs(assertContext, actual, expected);
    }
    
    private static void assertIndexOrderByItem(final SQLCaseAssertContext assertContext,
                                               final IndexOrderByItemSegment actual, final ExpectedIndexOrderByItem expected, final String type) {
        assertThat(assertContext.getText(String.format("%s item index assertion error: ", type)), actual.getColumnIndex(), is(expected.getIndex()));
        SQLSegmentAssert.assertIs(assertContext, actual, expected);
    }
    
    private static void assertExpressionOrderByItem(final SQLCaseAssertContext assertContext,
                                                    final ExpressionOrderByItemSegment actual, final ExpectedExpressionOrderByItem expected, final String type) {
        assertThat(assertContext.getText(String.format("%s item expression assertion error: ", type)), actual.getExpression(), is(expected.getExpression()));
        SQLSegmentAssert.assertIs(assertContext, actual, expected);
    }
}
