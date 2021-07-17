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

package org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.expression;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.column.ColumnSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.BetweenExpression;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.BinaryOperationExpression;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.ExistsSubqueryExpression;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.ExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.InExpression;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.ListExpression;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.NotExpression;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.complex.CommonExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.complex.ComplexExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.simple.LiteralExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.simple.ParameterMarkerExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.subquery.SubqueryExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.subquery.SubquerySegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.AggregationProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.ExpressionProjectionSegment;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.SQLCaseAssertContext;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.SQLSegmentAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.column.ColumnAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.segment.projection.ProjectionAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.asserts.statement.dml.impl.SelectStatementAssert;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.ExpectedBetweenExpression;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.ExpectedBinaryOperationExpression;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.ExpectedExistsSubquery;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.ExpectedExpression;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.ExpectedInExpression;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.ExpectedListExpression;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.ExpectedNotExpression;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.complex.ExpectedCommonExpression;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.simple.ExpectedLiteralExpression;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.simple.ExpectedParameterMarkerExpression;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.simple.ExpectedSubquery;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.sql.SQLCaseType;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 *  Expression assert.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExpressionAssert {
    
    /**
     * Assert parameter marker expression.
     *
     * @param assertContext assert context
     * @param actual actual parameter marker expression segment
     * @param expected expected parameter marker expression
     */
    public static void assertParameterMarkerExpression(final SQLCaseAssertContext assertContext,
                                                       final ParameterMarkerExpressionSegment actual, final ExpectedParameterMarkerExpression expected) {
        if (null == expected) {
            assertNull(assertContext.getText("Actual parameter marker expression should not exist."), actual);
        } else {
            assertNotNull(assertContext.getText("Actual parameter marker expression should exist."), actual);
            assertThat(assertContext.getText("Parameter marker index assertion error: "), actual.getParameterMarkerIndex(), is(expected.getValue()));
            SQLSegmentAssert.assertIs(assertContext, actual, expected);
        }
    }
    
    /**
     * Assert literal expression.
     *
     * @param assertContext assert context
     * @param actual actual literal expression segment
     * @param expected expected literal expression
     */
    public static void assertLiteralExpression(final SQLCaseAssertContext assertContext,
                                               final LiteralExpressionSegment actual, final ExpectedLiteralExpression expected) {
        if (null == expected) {
            assertNull(assertContext.getText("Actual literal expression should not exist."), actual);
        } else {
            assertNotNull(assertContext.getText("Actual literal expression should exist."), actual);
            assertThat(assertContext.getText("Literal assertion error: "), actual.getLiterals().toString(), is(expected.getValue()));
            SQLSegmentAssert.assertIs(assertContext, actual, expected);
        }
    }
    
    /**
     * Assert common expression.
     *
     * @param assertContext assert context
     * @param actual actual common expression segment
     * @param expected expected common expression
     */
    public static void assertCommonExpression(final SQLCaseAssertContext assertContext,
                                              final ComplexExpressionSegment actual, final ExpectedCommonExpression expected) {
        if (null == expected) {
            assertNull(assertContext.getText("Actual common expression should not exist."), actual);
        } else {
            assertNotNull(assertContext.getText("Actual common expression should exist."), actual);
            String expectedText = SQLCaseType.Literal == assertContext.getSqlCaseType() && null != expected.getLiteralText() ? expected.getLiteralText() : expected.getText();
            assertThat(assertContext.getText("Common expression text assertion error: "), actual.getText(), is(expectedText));
            SQLSegmentAssert.assertIs(assertContext, actual, expected);
        }
    }
    
    /**
     * Assert subquery expression.
     *
     * @param assertContext assert context
     * @param actual actual subquery expression segment
     * @param expected expected subquery expression
     */
    public static void assertSubqueryExpression(final SQLCaseAssertContext assertContext,
                                                final SubqueryExpressionSegment actual, final ExpectedSubquery expected) {
        if (null == expected) {
            assertNull(assertContext.getText("Actual subquery expression should not exist."), actual);
        } else {
            assertNotNull(assertContext.getText("Actual subquery expression should exist."), actual);
            assertSubquery(assertContext, actual.getSubquery(), expected);
        }
    }

    /**
     * Assert subquery.
     *
     * @param assertContext assert context
     * @param actual actual subquery segment
     * @param expected expected subquery expression
     */
    public static void assertSubquery(final SQLCaseAssertContext assertContext,
                                      final SubquerySegment actual, final ExpectedSubquery expected) {
        if (null == expected) {
            assertNull(assertContext.getText("Actual subquery should not exist."), actual);
        } else {
            assertNotNull(assertContext.getText("Actual subquery should exist."), actual);
            SelectStatementAssert.assertIs(assertContext, actual.getSelect(), expected.getSelectTestCases());
            SQLSegmentAssert.assertIs(assertContext, actual, expected);
        }
    }

    /**
     * Assert exists subquery expression.
     *
     * @param assertContext assert context
     * @param actual actual exists subquery expression
     * @param expected expected exists subquery expression
     */
    public static void assertExistsSubqueryExpression(final SQLCaseAssertContext assertContext,
                                                      final ExistsSubqueryExpression actual, final ExpectedExistsSubquery expected) {
        if (null == expected) {
            assertNull(assertContext.getText("Actual exists subquery should not exist."), actual);
        } else {
            assertNotNull(assertContext.getText("Actual exists subquery should exist."), actual);
            assertSubquery(assertContext, actual.getSubquery(), expected.getSubquery());
            assertThat(assertContext.getText("Exists subquery expression not value assert error."),
                    actual.isNot(), is(expected.isNot()));
            SQLSegmentAssert.assertIs(assertContext, actual, expected);
        }
    }

    /**
     * Assert binary operation expression.
     * @param assertContext assert context
     * @param actual actual binary operation expression
     * @param expected expected binary operation expression
     */
    public static void assertBinaryOperationExpression(final SQLCaseAssertContext assertContext,
                                                       final BinaryOperationExpression actual, final ExpectedBinaryOperationExpression expected) {
        if (null == expected) {
            assertNull(assertContext.getText("Actual binary operation expression should not exist."), actual);
        } else {
            assertNotNull(assertContext.getText("Actual binary operation expression should exist."), actual);
            assertExpression(assertContext, actual.getLeft(), expected.getLeft());
            assertThat(assertContext.getText("Binary operation expression operator assert error."),
                    actual.getOperator(), is(expected.getOperator()));
            assertExpression(assertContext, actual.getRight(), expected.getRight());
            SQLSegmentAssert.assertIs(assertContext, actual, expected);
        }
    }

    /**
     * Assert in operation expression.
     * @param assertContext assert context
     * @param actual actual in operation expression
     * @param expected expected in operation expression
     */
    public static void assertInExpression(final SQLCaseAssertContext assertContext,
                                          final InExpression actual, final ExpectedInExpression expected) {
        if (null == expected) {
            assertNull(assertContext.getText("Actual in expression should not exist."), actual);
        } else {
            assertNotNull(assertContext.getText("Actual in expression should exist."), actual);
            assertExpression(assertContext, actual.getLeft(), expected.getLeft());
            assertThat(assertContext.getText("In expression not value assert error."),
                    actual.isNot(), is(expected.isNot()));
            assertExpression(assertContext, actual.getRight(), expected.getRight());
            SQLSegmentAssert.assertIs(assertContext, actual, expected);
        }
    }

    /**
     * Assert not operation expression.
     * @param assertContext assert context
     * @param actual actual not operation expression
     * @param expected expected not operation expression
     */
    public static void assertNotExpression(final SQLCaseAssertContext assertContext,
                                           final NotExpression actual, final ExpectedNotExpression expected) {
        if (null == expected) {
            assertNull(assertContext.getText("Actual not expression should not exist."), actual);
        } else {
            assertNotNull(assertContext.getText("Actual not expression should exist."), actual);
            assertExpression(assertContext, actual.getExpression(), expected.getExpr());
            SQLSegmentAssert.assertIs(assertContext, actual, expected);
        }
    }

    /**
     * Assert list operation expression.
     * @param assertContext assert context
     * @param actual actual list operation expression
     * @param expected expected list operation expression
     */
    public static void assertListExpression(final SQLCaseAssertContext assertContext,
                                            final ListExpression actual, final ExpectedListExpression expected) {
        if (null == expected) {
            assertNull(assertContext.getText("Actual list expression should not exist."), actual);
        } else {
            assertNotNull(assertContext.getText("Actual list expression should exist."), actual);
            assertThat(assertContext.getText("List expression item size assert error."),
                    actual.getItems().size(), is(expected.getItems().size()));
            Iterator<ExpressionSegment> actualItems = actual.getItems().iterator();
            Iterator<ExpectedExpression> expectedItems = expected.getItems().iterator();
            while (actualItems.hasNext()) {
                assertExpression(assertContext, actualItems.next(), expectedItems.next());
            }
            //TODO PostgreSQL list expression start index was incorrect.
//            SQLSegmentAssert.assertIs(assertContext, actual, expected);
        }
    }

    /**
     * Assert between operation expression.
     * @param assertContext assert context
     * @param actual actual between operation expression
     * @param expected expected between operation expression
     */
    public static void assertBetweenExpression(final SQLCaseAssertContext assertContext,
                                               final BetweenExpression actual, final ExpectedBetweenExpression expected) {
        if (null == expected) {
            assertNull(assertContext.getText("Actual between expression should not exist."), actual);
        } else {
            assertNotNull(assertContext.getText("Actual between expression should exist."), actual);
            assertExpression(assertContext, actual.getLeft(), expected.getLeft());
            assertExpression(assertContext, actual.getBetweenExpr(), expected.getBetweenExpr());
            assertExpression(assertContext, actual.getAndExpr(), expected.getAndExpr());
            assertThat(assertContext.getText("Between expression not value assert error."),
                    actual.isNot(), is(expected.isNot()));
            SQLSegmentAssert.assertIs(assertContext, actual, expected);
        }
    }

    /**
     * Assert expression by actual expression segment class type.
     * @param assertContext assert context
     * @param actual actual expression segment
     * @param expected expected expression
     *
     * @throws UnsupportedOperationException When expression segment class type is not supported.
     */
    public static void assertExpression(final SQLCaseAssertContext assertContext,
                                        final ExpressionSegment actual, final ExpectedExpression expected) {
        if (null == expected) {
            assertNull(assertContext.getText("Actual expression should not exist."), actual);
            return;
        }
        assertNotNull(assertContext.getText("Actual expression should exist."), actual);
        if (actual instanceof BinaryOperationExpression) {
            assertBinaryOperationExpression(assertContext,
                    (BinaryOperationExpression) actual, expected.getBinaryOperationExpression());
        } else if (actual instanceof SubqueryExpressionSegment) {
            assertSubqueryExpression(assertContext,
                    (SubqueryExpressionSegment) actual, expected.getSubquery());
        } else if (actual instanceof ColumnSegment) {
            ColumnAssert.assertIs(assertContext,
                    (ColumnSegment) actual, expected.getColumn());
        } else if (actual instanceof LiteralExpressionSegment) {
            assertLiteralExpression(assertContext,
                    (LiteralExpressionSegment) actual, expected.getLiteralExpression());
        } else if (actual instanceof ParameterMarkerExpressionSegment) {
            assertParameterMarkerExpression(assertContext,
                    (ParameterMarkerExpressionSegment) actual, expected.getParameterMarkerExpression());
        } else if (actual instanceof ExistsSubqueryExpression) {
            assertExistsSubqueryExpression(assertContext,
                    (ExistsSubqueryExpression) actual, expected.getExistsSubquery());
        } else if (actual instanceof CommonExpressionSegment) {
            assertCommonExpression(assertContext,
                    (ComplexExpressionSegment) actual, expected.getCommonExpression());
        } else if (actual instanceof InExpression) {
            assertInExpression(assertContext,
                    (InExpression) actual, expected.getInExpression());
        } else if (actual instanceof NotExpression) {
            assertNotExpression(assertContext,
                    (NotExpression) actual, expected.getNotExpression());
        } else if (actual instanceof ListExpression) {
            assertListExpression(assertContext,
                    (ListExpression) actual, expected.getListExpression());
        } else if (actual instanceof BetweenExpression) {
            assertBetweenExpression(assertContext,
                    (BetweenExpression) actual, expected.getBetweenExpression());
        } else if (actual instanceof ExpressionProjectionSegment) {
            ProjectionAssert.assertProjection(assertContext,
                    (ExpressionProjectionSegment) actual, expected.getExpressionProjection());
        } else if (actual instanceof AggregationProjectionSegment) {
            ProjectionAssert.assertProjection(assertContext,
                    (AggregationProjectionSegment) actual, expected.getAggregationProjection());
        } else {
            throw new UnsupportedOperationException(
                    String.format("Unsupported expression  : %s.", actual.getClass().getName()));
        }
    }
}
