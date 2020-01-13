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

package org.apache.shardingsphere.sql.parser.integrate.asserts;

import com.google.common.base.Optional;
import org.apache.shardingsphere.sql.parser.integrate.asserts.groupby.GroupByAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.index.IndexAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.insert.InsertNamesAndValuesAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.orderby.OrderByAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.pagination.PaginationAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.predicate.PredicateAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.selectitem.SelectItemAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.table.AlterTableAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.table.TableAssert;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.ParserResultSetRegistryFactory;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.root.ParserResult;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.item.SelectItemsSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.order.GroupBySegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.order.OrderBySegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.pagination.limit.LimitSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.predicate.WhereSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.TableSegment;
import org.apache.shardingsphere.sql.parser.sql.statement.SQLStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.ddl.AlterTableStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dml.InsertStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dml.SelectStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.tcl.SetAutoCommitStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.tcl.TCLStatement;
import org.apache.shardingsphere.test.sql.SQLCaseType;
import org.apache.shardingsphere.test.sql.loader.SQLCasesRegistry;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * SQL statement assert.
 *
 * @author zhangliang
 */
public final class SQLStatementAssert {
    
    private final SQLStatement actual;
    
    private final ParserResult expected;
    
    private final TableAssert tableAssert;
    
    private final IndexAssert indexAssert;
    
    private final GroupByAssert groupByAssert;
    
    private final OrderByAssert orderByAssert;
    
    private final PaginationAssert paginationAssert;
    
    private final AlterTableAssert alterTableAssert;

    private final SelectItemAssert selectItemAssert;

    private final PredicateAssert predicateAssert;
    
    private final InsertNamesAndValuesAssert insertNamesAndValuesAssert;
    
    private final String databaseType;
    
    public SQLStatementAssert(final SQLStatement actual, final String sqlCaseId, final SQLCaseType sqlCaseType, final String databaseType) {
        SQLStatementAssertMessage assertMessage = new SQLStatementAssertMessage(
                SQLCasesRegistry.getInstance().getSqlCasesLoader(), ParserResultSetRegistryFactory.getInstance().getRegistry(), sqlCaseId, sqlCaseType);
        this.actual = actual;
        expected = ParserResultSetRegistryFactory.getInstance().getRegistry().get(sqlCaseId);
        tableAssert = new TableAssert(assertMessage);
        indexAssert = new IndexAssert(sqlCaseType, assertMessage);
        groupByAssert = new GroupByAssert(assertMessage);
        orderByAssert = new OrderByAssert(assertMessage);
        paginationAssert = new PaginationAssert(sqlCaseType, assertMessage);
        alterTableAssert = new AlterTableAssert(assertMessage);
        selectItemAssert = new SelectItemAssert(sqlCaseType, assertMessage);
        predicateAssert = new PredicateAssert(sqlCaseType, assertMessage);
        insertNamesAndValuesAssert = new InsertNamesAndValuesAssert(assertMessage, sqlCaseType);
        this.databaseType = databaseType;
    }
    
    /**
     * Assert SQL statement.
     */
    public void assertSQLStatement() {
        tableAssert.assertTables(actual.findSQLSegments(TableSegment.class), expected.getTables());
        indexAssert.assertParametersCount(actual.getParametersCount(), expected.getParameters().size());
        if (actual instanceof SelectStatement) {
            assertSelectStatement((SelectStatement) actual);
        }
        if (actual instanceof InsertStatement) {
            assertInsertStatement((InsertStatement) actual, databaseType);
        }
        if (actual instanceof AlterTableStatement) {
            assertAlterTableStatement((AlterTableStatement) actual);
        }
        if (actual instanceof TCLStatement) {
            assertTCLStatement((TCLStatement) actual);
        }
    }
    
    private void assertSelectStatement(final SelectStatement actual) {
        Optional<SelectItemsSegment> selectItemsSegment = actual.findSQLSegment(SelectItemsSegment.class);
        if (selectItemsSegment.isPresent()) {
            selectItemAssert.assertSelectItems(selectItemsSegment.get(), expected.getSelectItems());
        }
        Optional<GroupBySegment> groupBySegment = actual.findSQLSegment(GroupBySegment.class);
        if (groupBySegment.isPresent()) {
            groupByAssert.assertGroupByItems(groupBySegment.get().getGroupByItems(), expected.getGroupByColumns());
        }
        Optional<OrderBySegment> orderBySegment = actual.findSQLSegment(OrderBySegment.class);
        if (orderBySegment.isPresent()) {
            orderByAssert.assertOrderByItems(orderBySegment.get().getOrderByItems(), expected.getOrderByColumns());
        }
        Optional<LimitSegment> limitSegment = actual.findSQLSegment(LimitSegment.class);
        if (limitSegment.isPresent()) {
            paginationAssert.assertOffset(limitSegment.get().getOffset().orNull(), expected.getOffset());
            paginationAssert.assertRowCount(limitSegment.get().getRowCount().orNull(), expected.getRowCount());
        }
        Optional<WhereSegment> whereSegment = actual.findSQLSegment(WhereSegment.class);
        if (whereSegment.isPresent() && null != expected.getWhereSegment()) {
            predicateAssert.assertPredicate(whereSegment.get(), expected.getWhereSegment());
        }
    }
    
    private void assertInsertStatement(final InsertStatement actual, final String databaseType) {
        // TODO remove it when oracle fix for column names extract
        if ("oracle".equalsIgnoreCase(databaseType)) {
            return;
        }
        insertNamesAndValuesAssert.assertInsertNamesAndValues(actual, expected.getInsertColumnsAndValues());
    }
    
    private void assertAlterTableStatement(final AlterTableStatement actual) {
        if (null != expected.getAlterTable()) {
            alterTableAssert.assertAlterTable(actual, expected.getAlterTable());
        }
    }
    
    private void assertTCLStatement(final TCLStatement actual) {
        assertThat(actual.getClass().getName(), is(expected.getTclActualStatementClassType()));
        if (actual instanceof SetAutoCommitStatement) {
            assertThat(((SetAutoCommitStatement) actual).isAutoCommit(), is(expected.isAutoCommit()));
        }
    }
}
