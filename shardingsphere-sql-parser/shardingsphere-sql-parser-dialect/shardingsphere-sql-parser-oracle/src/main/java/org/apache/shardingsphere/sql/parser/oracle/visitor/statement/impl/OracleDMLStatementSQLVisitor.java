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

package org.apache.shardingsphere.sql.parser.oracle.visitor.statement.impl;

import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.Interval;
import org.apache.shardingsphere.sql.parser.api.visitor.ASTNode;
import org.apache.shardingsphere.sql.parser.api.visitor.operation.SQLStatementVisitor;
import org.apache.shardingsphere.sql.parser.api.visitor.type.DMLSQLVisitor;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AliasContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AssignmentContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AssignmentValueContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AssignmentValuesContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.CollectionExprContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ColumnNameContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ColumnNamesContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ConditionalInsertClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ConditionalInsertElsePartContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ConditionalInsertWhenPartContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ContainersClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.CrossOuterApplyClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.DeleteContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.DeleteSpecificationContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.DeleteWhereClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.DmlSubqueryClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.DmlTableClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.DuplicateSpecificationContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ExprContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ForUpdateClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ForUpdateClauseListContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.FromClauseListContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.FromClauseOptionContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.GroupByClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.HavingClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.InsertContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.InsertIntoClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.InsertMultiTableContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.InsertSingleTableContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.InsertValuesClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.IntoClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.JoinClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.MergeAssignmentContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.MergeAssignmentValueContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.MergeContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.MergeSetAssignmentsClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.MergeUpdateClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.MultiTableElementContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.OrderByItemContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ParenthesisSelectSubqueryContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.QueryBlockContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.QueryNameContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.QueryTableExprClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.QueryTableExprContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.SelectContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.SelectFromClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.SelectJoinOptionContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.SelectJoinSpecificationContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.SelectListContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.SelectProjectionContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.SelectProjectionExprClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.SelectSubqueryContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.SelectTableReferenceContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.SelectUnionClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.SetAssignmentsClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ShardsClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.SubqueryContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.SubqueryFactoringClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.TableCollectionExprContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.TableNameContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.UnionClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.UpdateContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.UsingClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.WhereClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.WithClauseContext;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.assignment.AssignmentSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.assignment.InsertValuesSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.assignment.SetAssignmentSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.column.ColumnSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.column.InsertColumnsSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.BinaryOperationExpression;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.ExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.complex.CommonExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.complex.CommonTableExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.simple.LiteralExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.subquery.SubqueryExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.expr.subquery.SubquerySegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.AggregationProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.ColumnProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.ExpressionProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.ProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.ProjectionsSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.ShorthandProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.SubqueryProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.order.GroupBySegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.order.OrderBySegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.order.item.OrderByItemSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.predicate.HavingSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.predicate.LockSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.predicate.WhereSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.AliasSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.InsertMultiTableElementSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.OwnerSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.WithSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.JoinTableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.SimpleTableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.SubqueryTableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.TableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.value.collection.CollectionValue;
import org.apache.shardingsphere.sql.parser.sql.common.value.identifier.IdentifierValue;
import org.apache.shardingsphere.sql.parser.sql.common.value.literal.impl.BooleanLiteralValue;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.dml.OracleDeleteStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.dml.OracleInsertStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.dml.OracleMergeStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.dml.OracleSelectStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.dml.OracleUpdateStatement;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * DML Statement SQL visitor for Oracle.
 */
@NoArgsConstructor
public final class OracleDMLStatementSQLVisitor extends OracleStatementSQLVisitor implements DMLSQLVisitor, SQLStatementVisitor {
    
    public OracleDMLStatementSQLVisitor(final Properties props) {
        super(props);
    }
    
    @Override
    public ASTNode visitInsert(final InsertContext ctx) {
        // TODO :FIXME, since there is no segment for insertValuesClause, InsertStatement is created by sub rule.
        // TODO :deal with insert select
        if (null != ctx.insertSingleTable()) {
            OracleInsertStatement result = (OracleInsertStatement) visit(ctx.insertSingleTable());
            return result;
        } else {
            OracleInsertStatement result = (OracleInsertStatement) visit(ctx.insertMultiTable());
            return result;
        }
    }
    
    @Override
    public ASTNode visitInsertSingleTable(final InsertSingleTableContext ctx) {
        OracleInsertStatement result = (OracleInsertStatement) visit(ctx.insertIntoClause());
        if (null != ctx.insertValuesClause()) {
            result.getValues().addAll(createInsertValuesSegments(ctx.insertValuesClause().assignmentValues()));
        }
        if (null != ctx.selectSubquery()) {
            OracleSelectStatement subquery = (OracleSelectStatement) visit(ctx.selectSubquery());
            SubquerySegment subquerySegment = new SubquerySegment(ctx.selectSubquery().start.getStartIndex(), ctx.selectSubquery().stop.getStopIndex(), subquery);
            result.setSelectSubquery(subquerySegment);
        }
        result.setParameterCount(getCurrentParameterIndex());
        return result;
    }
    
    private Collection<InsertValuesSegment> createInsertValuesSegments(final AssignmentValuesContext ctx) {
        Collection<InsertValuesSegment> result = new LinkedList<>();
        result.add((InsertValuesSegment) visit(ctx));
        return result;
    }
    
    @Override
    public ASTNode visitInsertMultiTable(final InsertMultiTableContext ctx) {
        OracleInsertStatement result = new OracleInsertStatement();
        if (null != ctx.conditionalInsertClause()) {
            result.setInsertMultiTableElementSegment((InsertMultiTableElementSegment) visit(ctx.conditionalInsertClause()));
        } else {
            result.setInsertMultiTableElementSegment(createInsertMultiTableElementSegment(ctx.multiTableElement()));
        }
        OracleSelectStatement subquery = (OracleSelectStatement) visit(ctx.selectSubquery());
        SubquerySegment subquerySegment = new SubquerySegment(ctx.selectSubquery().start.getStartIndex(), ctx.selectSubquery().stop.getStopIndex(), subquery);
        result.setSelectSubquery(subquerySegment);
        result.setParameterCount(getCurrentParameterIndex());
        return result;
    }
    
    private InsertMultiTableElementSegment createInsertMultiTableElementSegment(final List<MultiTableElementContext> ctx) {
        Collection<OracleInsertStatement> insertStatements = new LinkedList<>();
        for (MultiTableElementContext each: ctx) {
            insertStatements.add((OracleInsertStatement) visit(each));
        }
        InsertMultiTableElementSegment result = new InsertMultiTableElementSegment(ctx.get(0).getStart().getStartIndex(), ctx.get(ctx.size() - 1).getStop().getStopIndex());
        result.getInsertStatements().addAll(insertStatements);
        return result;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ASTNode visitInsertValuesClause(final InsertValuesClauseContext ctx) {
        OracleInsertStatement result = new OracleInsertStatement();
        result.getValues().addAll(createInsertValuesSegments(ctx.assignmentValues()));
        return result;
    }
    
    @Override
    public ASTNode visitInsertIntoClause(final InsertIntoClauseContext ctx) {
        OracleInsertStatement result = new OracleInsertStatement();
        if (null != ctx.dmlTableExprClause().dmlTableClause()) {
            result.setTable((SimpleTableSegment) visit(ctx.dmlTableExprClause().dmlTableClause()));
        } else if (null != ctx.dmlTableExprClause().dmlSubqueryClause()) {
            result.setInsertSelect((SubquerySegment) visit(ctx.dmlTableExprClause().dmlSubqueryClause()));
        } else {
            result.setInsertSelect((SubquerySegment) visit(ctx.dmlTableExprClause().tableCollectionExpr()));
        }
        if (null != ctx.columnNames()) {
            ColumnNamesContext columnNames = ctx.columnNames();
            CollectionValue<ColumnSegment> columnSegments = (CollectionValue<ColumnSegment>) visit(columnNames);
            result.setInsertColumns(new InsertColumnsSegment(columnNames.start.getStartIndex(), columnNames.stop.getStopIndex(), columnSegments.getValue()));
        } else {
            result.setInsertColumns(new InsertColumnsSegment(ctx.stop.getStopIndex() + 1, ctx.stop.getStopIndex() + 1, Collections.emptyList()));
        }
        return result;
    }
    
    @Override
    public ASTNode visitDmlTableClause(final DmlTableClauseContext ctx) {
        SimpleTableSegment result = (SimpleTableSegment) visit(ctx.tableName());
        return result;
    }
    
    @Override
    public ASTNode visitDmlSubqueryClause(final DmlSubqueryClauseContext ctx) {
        OracleSelectStatement subquery = (OracleSelectStatement) visit(ctx.selectSubquery());
        SubquerySegment result = new SubquerySegment(ctx.selectSubquery().start.getStartIndex(), ctx.selectSubquery().stop.getStopIndex(), subquery);
        return result;
    }
    
    @Override
    public ASTNode visitTableCollectionExpr(final TableCollectionExprContext ctx) {
        OracleSelectStatement subquery = (OracleSelectStatement) visit(ctx.collectionExpr().selectSubquery());
        SubquerySegment result = new SubquerySegment(ctx.collectionExpr().selectSubquery().start.getStartIndex(), ctx.collectionExpr().selectSubquery().stop.getStopIndex(), subquery);
        return result;
    }
    
    @Override
    public ASTNode visitConditionalInsertClause(final ConditionalInsertClauseContext ctx) {
        Collection<OracleInsertStatement> insertStatements = new LinkedList<>();
        for (ConditionalInsertWhenPartContext each: ctx.conditionalInsertWhenPart()) {
            insertStatements.addAll(createInsertStatementsFromConditionalInsertWhen(each));
        }
        if (null != ctx.conditionalInsertElsePart()) {
            insertStatements.addAll(createInsertStatementsFromConditionalInsertElse(ctx.conditionalInsertElsePart()));
        }
        InsertMultiTableElementSegment result = new InsertMultiTableElementSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex());
        result.getInsertStatements().addAll(insertStatements);
        return result;
    }
    
    private Collection<OracleInsertStatement> createInsertStatementsFromConditionalInsertWhen(final ConditionalInsertWhenPartContext ctx) {
        Collection<OracleInsertStatement> result = new LinkedList<>();
        for (MultiTableElementContext each: ctx.multiTableElement()) {
            result.add((OracleInsertStatement) visit(each));
        }
        return result;
    }
    
    private Collection<OracleInsertStatement> createInsertStatementsFromConditionalInsertElse(final ConditionalInsertElsePartContext ctx) {
        Collection<OracleInsertStatement> result = new LinkedList<>();
        for (MultiTableElementContext each: ctx.multiTableElement()) {
            result.add((OracleInsertStatement) visit(each));
        }
        return result;
    }
    
    @Override
    public ASTNode visitMultiTableElement(final MultiTableElementContext ctx) {
        OracleInsertStatement result = (OracleInsertStatement) visit(ctx.insertIntoClause());
        if (null != ctx.insertValuesClause()) {
            result.getValues().addAll(createInsertValuesSegments(ctx.insertValuesClause().assignmentValues()));
        }
        return result;
    }
    
    @Override
    public ASTNode visitUpdate(final UpdateContext ctx) {
        OracleUpdateStatement result = new OracleUpdateStatement();
        result.setTableSegment((TableSegment) visit(ctx.tableReferences()));
        result.setSetAssignment((SetAssignmentSegment) visit(ctx.setAssignmentsClause()));
        if (null != ctx.whereClause()) {
            result.setWhere((WhereSegment) visit(ctx.whereClause()));
        }
        result.setParameterCount(getCurrentParameterIndex());
        return result;
    }
    
    @Override
    public ASTNode visitSetAssignmentsClause(final SetAssignmentsClauseContext ctx) {
        Collection<AssignmentSegment> assignments = new LinkedList<>();
        for (AssignmentContext each : ctx.assignment()) {
            assignments.add((AssignmentSegment) visit(each));
        }
        return new SetAssignmentSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), assignments);
    }
    
    @Override
    public ASTNode visitAssignmentValues(final AssignmentValuesContext ctx) {
        List<ExpressionSegment> segments = new LinkedList<>();
        for (AssignmentValueContext each : ctx.assignmentValue()) {
            segments.add((ExpressionSegment) visit(each));
        }
        return new InsertValuesSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), segments);
    }
    
    @Override
    public ASTNode visitAssignment(final AssignmentContext ctx) {
        ColumnSegment column = (ColumnSegment) visitColumnName(ctx.columnName());
        ExpressionSegment value = (ExpressionSegment) visit(ctx.assignmentValue());
        return new AssignmentSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), column, value);
    }
    
    @Override
    public ASTNode visitAssignmentValue(final AssignmentValueContext ctx) {
        ExprContext expr = ctx.expr();
        if (null != expr) {
            return visit(expr);
        }
        return new CommonExpressionSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitDelete(final DeleteContext ctx) {
        OracleDeleteStatement result = new OracleDeleteStatement();
        result.setTableSegment((TableSegment) visit(ctx.deleteSpecification()));
        if (null != ctx.alias()) {
            result.getTableSegment().setAlias((AliasSegment) visit(ctx.alias()));
        }
        if (null != ctx.whereClause()) {
            result.setWhere((WhereSegment) visit(ctx.whereClause()));
        }
        result.setParameterCount(getCurrentParameterIndex());
        return result;
    }
    
    @Override
    public ASTNode visitDeleteSpecification(final DeleteSpecificationContext ctx) {
        TableSegment result;
        if (null != ctx.dmlTableExprClause().dmlTableClause()) {
            result = (TableSegment) visit(ctx.dmlTableExprClause().dmlTableClause());
        } else if (null != ctx.dmlTableExprClause().dmlSubqueryClause()) {
            SubquerySegment subquerySegment = (SubquerySegment) visit(ctx.dmlTableExprClause().dmlSubqueryClause());
            SubqueryTableSegment subqueryTableSegment = new SubqueryTableSegment(subquerySegment);
            result = (TableSegment) subqueryTableSegment;
        } else {
            SubquerySegment subquerySegment = (SubquerySegment) visit(ctx.dmlTableExprClause().tableCollectionExpr());
            SubqueryTableSegment subqueryTableSegment = new SubqueryTableSegment(subquerySegment);
            result = (TableSegment) subqueryTableSegment;
        }
        return result;
    }
    
    @Override
    public ASTNode visitSelect(final SelectContext ctx) {
        // TODO :Unsupported for withClause.
        OracleSelectStatement result = (OracleSelectStatement) visit(ctx.selectSubquery());
        result.setParameterCount(getCurrentParameterIndex());
        if (null != ctx.forUpdateClause()) {
            result.setLock((LockSegment) visit(ctx.forUpdateClause()));
        }
        return result;
    }
    
    @Override
    public ASTNode visitUnionClause(final UnionClauseContext ctx) {
        // TODO :Unsupported for union SQL.
        return visit(ctx.queryBlock(0));
    }
    
    @Override
    public ASTNode visitSelectSubquery(final SelectSubqueryContext ctx) {
        OracleSelectStatement result;
        if (null != ctx.queryBlock()) {
            result = (OracleSelectStatement) visit(ctx.queryBlock());
        } else if (null != ctx.selectUnionClause()) {
            result = (OracleSelectStatement) visit(ctx.selectUnionClause());
        } else {
            result = (OracleSelectStatement) visit(ctx.parenthesisSelectSubquery());
        }
        if (null != ctx.orderByClause()) {
            result.setOrderBy((OrderBySegment) visit(ctx.orderByClause()));
        }
        return result;
    }
    
    @Override
    public ASTNode visitQueryBlock(final QueryBlockContext ctx) {
        OracleSelectStatement result = new OracleSelectStatement();
        result.setProjections((ProjectionsSegment) visit(ctx.selectList()));
        if (null != ctx.withClause()) {
            result.setWithSegment((WithSegment) visit(ctx.withClause()));
        }
        if (null != ctx.duplicateSpecification()) {
            result.getProjections().setDistinctRow(isDistinct(ctx));
        }
        if (null != ctx.selectFromClause()) {
            TableSegment tableSegment = (TableSegment) visit(ctx.selectFromClause());
            result.setFrom(tableSegment);
        }
        if (null != ctx.whereClause()) {
            result.setWhere((WhereSegment) visit(ctx.whereClause()));
        }
        if (null != ctx.groupByClause()) {
            result.setGroupBy((GroupBySegment) visit(ctx.groupByClause()));
        }
        if (null != ctx.havingClause()) {
            result.setHaving((HavingSegment) visit(ctx.havingClause()));
        }
        return result;
    }
    
    @Override
    public ASTNode visitHavingClause(final HavingClauseContext ctx) {
        ExpressionSegment expr = (ExpressionSegment) visit(ctx.expr());
        return new HavingSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), expr);
    }
    
    @Override
    public ASTNode visitSelectUnionClause(final SelectUnionClauseContext ctx) {
        OracleSelectStatement result;
        if (null != ctx.queryBlock()) {
            result = (OracleSelectStatement) visit(ctx.queryBlock());
        } else {
            result = (OracleSelectStatement) visit(ctx.parenthesisSelectSubquery());
        }
        if (null != ctx.orderByClause()) {
            result.setOrderBy((OrderBySegment) visit(ctx.orderByClause()));
        }
        for (SelectSubqueryContext each : ctx.selectSubquery()) {
            visit(each);
        }
        return result;
    }
    
    @Override
    public ASTNode visitParenthesisSelectSubquery(final ParenthesisSelectSubqueryContext ctx) {
        return visit(ctx.selectSubquery());
    }
    
    @Override
    public ASTNode visitWithClause(final WithClauseContext ctx) {
        Collection<CommonTableExpressionSegment> commonTableExpressions = new LinkedList<>();
        if (null != ctx.subqueryFactoringClause()) {
            for (SubqueryFactoringClauseContext each : ctx.subqueryFactoringClause()) {
                SubquerySegment subquery = new SubquerySegment(each.selectSubquery().start.getStartIndex(), each.selectSubquery().stop.getStopIndex(), (OracleSelectStatement) visit(each));
                IdentifierValue identifier = (IdentifierValue) visit(each.queryName().name().identifier());
                CommonTableExpressionSegment commonTableExpression = new CommonTableExpressionSegment(each.start.getStartIndex(), each.stop.getStopIndex(), identifier, subquery);
                if (null != each.searchClause()) {
                    ColumnNameContext columnName = each.searchClause().orderingColumn().columnName();
                    commonTableExpression.getColumns().add((ColumnSegment) visit(columnName));
                }
                commonTableExpressions.add(commonTableExpression);
            }
        }
        return new WithSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), commonTableExpressions);
    }
    
    @Override
    public ASTNode visitSubqueryFactoringClause(final SubqueryFactoringClauseContext ctx) {
        return visit(ctx.selectSubquery());
    }
    
    private boolean isDistinct(final QueryBlockContext ctx) {
        return ((BooleanLiteralValue) visit(ctx.duplicateSpecification())).getValue();
    }
    
    @Override
    public ASTNode visitDuplicateSpecification(final DuplicateSpecificationContext ctx) {
        if (null != ctx.DISTINCT() || null != ctx.UNIQUE()) {
            return new BooleanLiteralValue(true);
        }
        return new BooleanLiteralValue(false);
    }
    
    @Override
    public ASTNode visitSelectList(final SelectListContext ctx) {
        ProjectionsSegment result = new ProjectionsSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex());
        Collection<ProjectionSegment> projections = new LinkedList<>();
        if (null != ctx.unqualifiedShorthand()) {
            projections.add(new ShorthandProjectionSegment(ctx.unqualifiedShorthand().getStart().getStartIndex(), ctx.unqualifiedShorthand().getStop().getStopIndex()));
            result.getProjections().addAll(projections);
            return result;
        }
        for (SelectProjectionContext each : ctx.selectProjection()) {
            projections.add((ProjectionSegment) visit(each));
        }
        result.getProjections().addAll(projections);
        return result;
    }
    
    @Override
    public ASTNode visitSelectProjection(final SelectProjectionContext ctx) {
        // FIXME :The stop index of project is the stop index of projection, instead of alias.
        if (null != ctx.queryName()) {
            QueryNameContext queryName = ctx.queryName();
            ShorthandProjectionSegment result = new ShorthandProjectionSegment(queryName.getStart().getStartIndex(), ctx.DOT_ASTERISK_().getSymbol().getStopIndex());
            IdentifierValue identifier = new IdentifierValue(queryName.getText());
            result.setOwner(new OwnerSegment(queryName.getStart().getStartIndex(), queryName.getStop().getStopIndex(), identifier));
            return result;
        }
        if (null != ctx.tableName()) {
            TableNameContext tableName = ctx.tableName();
            ShorthandProjectionSegment result = new ShorthandProjectionSegment(tableName.getStart().getStartIndex(), ctx.DOT_ASTERISK_().getSymbol().getStopIndex());
            IdentifierValue identifier = new IdentifierValue(tableName.getText());
            result.setOwner(new OwnerSegment(tableName.getStart().getStartIndex(), tableName.getStop().getStopIndex(), identifier));
            return result;
        }
        if (null != ctx.alias()) {
            AliasContext aliasContext = ctx.alias();
            ShorthandProjectionSegment result = new ShorthandProjectionSegment(aliasContext.getStart().getStartIndex(), ctx.DOT_ASTERISK_().getSymbol().getStopIndex());
            IdentifierValue identifier = new IdentifierValue(aliasContext.getText());
            result.setOwner(new OwnerSegment(aliasContext.getStart().getStartIndex(), aliasContext.getStop().getStopIndex(), identifier));
            return result;
        }
        return createProjection(ctx.selectProjectionExprClause());
    }
    
    @Override
    public ASTNode visitAlias(final AliasContext ctx) {
        if (null != ctx.identifier()) {
            return new AliasSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), (IdentifierValue) visit(ctx.identifier()));
        }
        return new AliasSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), new IdentifierValue(ctx.STRING_().getText()));
    }
    
    private ASTNode createProjection(final SelectProjectionExprClauseContext ctx) {
        AliasSegment alias = null == ctx.alias() ? null : (AliasSegment) visit(ctx.alias());
        ASTNode projection = visit(ctx.expr());
        if (projection instanceof AggregationProjectionSegment) {
            ((AggregationProjectionSegment) projection).setAlias(alias);
            return projection;
        }
        if (projection instanceof ExpressionProjectionSegment) {
            ((ExpressionProjectionSegment) projection).setAlias(alias);
            return projection;
        }
        if (projection instanceof CommonExpressionSegment) {
            CommonExpressionSegment segment = (CommonExpressionSegment) projection;
            ExpressionProjectionSegment result = new ExpressionProjectionSegment(segment.getStartIndex(), segment.getStopIndex(), segment.getText());
            result.setAlias(alias);
            return result;
        }
        // FIXME :For DISTINCT()
        if (projection instanceof ColumnSegment) {
            ColumnProjectionSegment result = new ColumnProjectionSegment((ColumnSegment) projection);
            result.setAlias(alias);
            return result;
        }
        if (projection instanceof SubqueryExpressionSegment) {
            SubqueryExpressionSegment subqueryExpressionSegment = (SubqueryExpressionSegment) projection;
            String text = ctx.start.getInputStream().getText(new Interval(subqueryExpressionSegment.getStartIndex(), subqueryExpressionSegment.getStopIndex()));
            SubqueryProjectionSegment result = new SubqueryProjectionSegment(((SubqueryExpressionSegment) projection).getSubquery(), text);
            result.setAlias(alias);
            return result;
        }
        if (projection instanceof BinaryOperationExpression) {
            int startIndex = ((BinaryOperationExpression) projection).getStartIndex();
            int stopIndex = null != alias ? alias.getStopIndex() : ((BinaryOperationExpression) projection).getStopIndex();
            ExpressionProjectionSegment result = new ExpressionProjectionSegment(startIndex, stopIndex, ((BinaryOperationExpression) projection).getText());
            result.setAlias(alias);
            return result;
        }
        LiteralExpressionSegment column = (LiteralExpressionSegment) projection;
        ExpressionProjectionSegment result = null == alias ? new ExpressionProjectionSegment(column.getStartIndex(), column.getStopIndex(), String.valueOf(column.getLiterals()))
                : new ExpressionProjectionSegment(column.getStartIndex(), ctx.alias().stop.getStopIndex(), String.valueOf(column.getLiterals()));
        result.setAlias(alias);
        return result;
    }
    
    @Override
    public ASTNode visitSelectFromClause(final SelectFromClauseContext ctx) {
        return visit(ctx.fromClauseList());
    }
    
    @Override
    public ASTNode visitFromClauseList(final FromClauseListContext ctx) {
        TableSegment result = (TableSegment) visit(ctx.fromClauseOption(0));
        if (ctx.fromClauseOption().size() > 1) {
            for (int i = 1; i < ctx.fromClauseOption().size(); i++) {
                result = generateJoinTableSourceFromFromClauseOption(ctx.fromClauseOption(i), result);
            }
        }
        return result;
    }
    
    private JoinTableSegment generateJoinTableSourceFromFromClauseOption(final FromClauseOptionContext ctx, final TableSegment tableSegment) {
        JoinTableSegment result = new JoinTableSegment();
        result.setStartIndex(tableSegment.getStartIndex());
        result.setStopIndex(ctx.stop.getStopIndex());
        result.setLeft(tableSegment);
        result.setRight((TableSegment) visit(ctx));
        return result;
    }
    
    @Override
    public ASTNode visitFromClauseOption(final FromClauseOptionContext ctx) {
        if (null != ctx.joinClause()) {
            return visit(ctx.joinClause());
        }
        return visit(ctx.selectTableReference());
    }
    
    @Override
    public ASTNode visitJoinClause(final JoinClauseContext ctx) {
        TableSegment result;
        TableSegment left;
        left = (TableSegment) visit(ctx.selectTableReference());
        for (SelectJoinOptionContext each : ctx.selectJoinOption()) {
            left = visitJoinedTable(each, left);
        }
        result = left;
        return result;
    }
    
    private JoinTableSegment visitJoinedTable(final SelectJoinOptionContext ctx, final TableSegment tableSegment) {
        JoinTableSegment result = new JoinTableSegment();
        result.setLeft(tableSegment);
        result.setStartIndex(tableSegment.getStartIndex());
        result.setStopIndex(ctx.stop.getStopIndex());
        if (null != ctx.innerCrossJoinClause()) {
            TableSegment right = (TableSegment) visit(ctx.innerCrossJoinClause().selectTableReference());
            result.setRight(right);
            if (null != ctx.innerCrossJoinClause().selectJoinSpecification()) {
                result = visitSelectJoinSpecification(ctx.innerCrossJoinClause().selectJoinSpecification(), result);
            }
        } else if (null != ctx.outerJoinClause()) {
            TableSegment right = (TableSegment) visit(ctx.outerJoinClause().selectTableReference());
            result.setRight(right);
            if (null != ctx.outerJoinClause().selectJoinSpecification()) {
                result = visitSelectJoinSpecification(ctx.outerJoinClause().selectJoinSpecification(), result);
            }
        } else {
            TableSegment right = (TableSegment) visit(ctx.crossOuterApplyClause());
            result.setRight(right);
        }
        return result;
    }
    
    private JoinTableSegment visitSelectJoinSpecification(final SelectJoinSpecificationContext ctx, final JoinTableSegment joinTableSource) {
        if (null != ctx.expr()) {
            ExpressionSegment condition = (ExpressionSegment) visit(ctx.expr());
            joinTableSource.setCondition(condition);
        }
        if (null != ctx.USING()) {
            List<ColumnSegment> columnSegmentList = new LinkedList<>();
            for (ColumnNameContext cname : ctx.columnNames().columnName()) {
                columnSegmentList.add((ColumnSegment) visit(cname));
            }
            joinTableSource.setUsing(columnSegmentList);
        }
        return joinTableSource;
    }
    
    @Override
    public ASTNode visitCrossOuterApplyClause(final CrossOuterApplyClauseContext ctx) {
        TableSegment result;
        if (null != ctx.selectTableReference()) {
            result = (TableSegment) visit(ctx.selectTableReference());
        } else {
            SubquerySegment subquerySegment = (SubquerySegment) visit(ctx.collectionExpr());
            result = new SubqueryTableSegment(subquerySegment);
        }
        return result;
    }
    
    @Override
    public ASTNode visitCollectionExpr(final CollectionExprContext ctx) {
        OracleSelectStatement subquery = (OracleSelectStatement) visit(ctx.selectSubquery());
        SubquerySegment result = new SubquerySegment(ctx.selectSubquery().start.getStartIndex(), ctx.selectSubquery().stop.getStopIndex(), subquery);
        return result;
    }
    
    @Override
    public ASTNode visitSelectTableReference(final SelectTableReferenceContext ctx) {
        TableSegment result;
        if (null != ctx.containersClause()) {
            result = (TableSegment) visit(ctx.containersClause());
        } else if (null != ctx.shardsClause()) {
            result = (TableSegment) visit(ctx.shardsClause());
        } else {
            result = (TableSegment) visit(ctx.queryTableExprClause());   
        }
        if (null != ctx.alias()) {
            result.setAlias((AliasSegment) visit(ctx.alias()));
        }
        return result;
    }
    
    @Override
    public ASTNode visitContainersClause(final ContainersClauseContext ctx) {
        SimpleTableSegment result = (SimpleTableSegment) visit(ctx.tableName());
        return result;
    }
    
    @Override
    public ASTNode visitShardsClause(final ShardsClauseContext ctx) {
        SimpleTableSegment result = (SimpleTableSegment) visit(ctx.tableName());
        return result;
    }
    
    @Override
    public ASTNode visitQueryTableExprClause(final QueryTableExprClauseContext ctx) {
        return visit(ctx.queryTableExpr());
    }
    
    @Override
    public ASTNode visitQueryTableExpr(final QueryTableExprContext ctx) {
        TableSegment result;
        if (null != ctx.queryTableExprSampleClause()) {
            result = (SimpleTableSegment) visit(ctx.queryTableExprSampleClause().queryTableExprTableClause().tableName());
        } else if (null != ctx.lateralClause()) {
            OracleSelectStatement subquery = (OracleSelectStatement) visit(ctx.lateralClause().selectSubquery());
            SubquerySegment subquerySegment = new SubquerySegment(ctx.lateralClause().selectSubquery().start.getStartIndex(), ctx.lateralClause().selectSubquery().stop.getStopIndex(), subquery);
            result = new SubqueryTableSegment(subquerySegment);
        } else {
            SubquerySegment subquerySegment = (SubquerySegment) visit(ctx.tableCollectionExpr());
            result = new SubqueryTableSegment(subquerySegment);
        }
        return result;
    }
    
    @Override
    public ASTNode visitWhereClause(final WhereClauseContext ctx) {
        ASTNode segment = visit(ctx.expr());
        return new WhereSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), (ExpressionSegment) segment);
    }
    
    @Override
    public ASTNode visitGroupByClause(final GroupByClauseContext ctx) {
        Collection<OrderByItemSegment> items = new LinkedList<>();
        for (OrderByItemContext each : ctx.orderByItem()) {
            items.add((OrderByItemSegment) visit(each));
        }
        return new GroupBySegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), items);
    }
    
    @Override
    public ASTNode visitSubquery(final SubqueryContext ctx) {
        return visit(ctx.selectSubquery());
    }
    
    @Override
    public ASTNode visitForUpdateClause(final ForUpdateClauseContext ctx) {
        LockSegment result = new LockSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex());
        if (null != ctx.forUpdateClauseList()) {
            result.getTables().addAll(generateTablesFromforUpdateClauseOption(ctx.forUpdateClauseList()));
            result.getColumns().addAll(generateColumnsFromforUpdateClauseOption(ctx.forUpdateClauseList()));
        }
        return result;
    }
    
    private List<SimpleTableSegment> generateTablesFromforUpdateClauseOption(final ForUpdateClauseListContext ctx) {
        List<SimpleTableSegment> result = new LinkedList<>();
        for (OracleStatementParser.ForUpdateClauseOptionContext each : ctx.forUpdateClauseOption()) {
            if (null != each.tableName()) {
                result.add((SimpleTableSegment) visit(each.tableName()));
            }
        }
        return result;
    }
    
    private List<ColumnSegment> generateColumnsFromforUpdateClauseOption(final ForUpdateClauseListContext ctx) {
        List<ColumnSegment> result = new LinkedList<>();
        for (OracleStatementParser.ForUpdateClauseOptionContext each : ctx.forUpdateClauseOption()) {
            if (null != each.columnName()) {
                result.add((ColumnSegment) visit(each.columnName()));
            }
        }
        return result;
    }
    
    @Override
    public ASTNode visitMerge(final MergeContext ctx) {
        OracleMergeStatement result = new OracleMergeStatement();
        result.setTarget((SimpleTableSegment) visit(ctx.intoClause()));
        result.setSource((TableSegment) visit(ctx.usingClause()));
        result.setExpr((ExpressionSegment) (visit(ctx.usingClause().expr())));
        if (null != ctx.mergeUpdateClause()) {
            result.getUpdate().setSetAssignment((SetAssignmentSegment) visit(ctx.mergeUpdateClause().mergeSetAssignmentsClause()));
            if (null != ctx.mergeUpdateClause().whereClause()) {
                result.getUpdate().setWhere((WhereSegment) visit(ctx.mergeUpdateClause().whereClause()));
            }
            if (null != ctx.mergeUpdateClause().deleteWhereClause()) {
                result.getDelete().setWhere((WhereSegment) visit(ctx.mergeUpdateClause().deleteWhereClause()));
            }
        }
        return result;
    }
    
    @Override
    public ASTNode visitIntoClause(final IntoClauseContext ctx) {
        if (null != ctx.tableName()) {
            SimpleTableSegment result = (SimpleTableSegment) visit(ctx.tableName());
            if (null != ctx.alias()) {
                result.setAlias((AliasSegment) visit(ctx.alias()));
            }
            return result;
        }
        SimpleTableSegment result = (SimpleTableSegment) visit(ctx.viewName());
        if (null != ctx.alias()) {
            result.setAlias((AliasSegment) visit(ctx.alias()));
        }
        return result;
    }
    
    @Override
    public ASTNode visitUsingClause(final UsingClauseContext ctx) {
        if (null != ctx.tableName()) {
            SimpleTableSegment result = (SimpleTableSegment) visit(ctx.tableName());
            if (null != ctx.alias()) {
                result.setAlias((AliasSegment) visit(ctx.alias()));
            }
            return result;
        }
        if (null != ctx.viewName()) {
            SimpleTableSegment result = (SimpleTableSegment) visit(ctx.viewName());
            if (null != ctx.alias()) {
                result.setAlias((AliasSegment) visit(ctx.alias()));
            }
            return result;
        }
        OracleSelectStatement subquery = (OracleSelectStatement) visit(ctx.subquery());
        SubquerySegment subquerySegment = new SubquerySegment(ctx.subquery().start.getStartIndex(), ctx.subquery().stop.getStopIndex(), subquery);
        SubqueryTableSegment result = new SubqueryTableSegment(subquerySegment);
        if (null != ctx.alias()) {
            result.setAlias((AliasSegment) visit(ctx.alias()));
        }
        return result;
    }
    
    @Override
    public ASTNode visitMergeUpdateClause(final MergeUpdateClauseContext ctx) {
        OracleMergeStatement result = new OracleMergeStatement();
        result.getUpdate().setSetAssignment((SetAssignmentSegment) visit(ctx.mergeSetAssignmentsClause()));
        if (null != ctx.whereClause()) {
            result.getUpdate().setWhere((WhereSegment) visit(ctx.whereClause()));
        }
        if (null != ctx.deleteWhereClause()) {
            result.getDelete().setWhere((WhereSegment) visit(ctx.deleteWhereClause()));
        }
        return result;
    }
    
    @Override
    public ASTNode visitMergeSetAssignmentsClause(final MergeSetAssignmentsClauseContext ctx) {
        Collection<AssignmentSegment> assignments = new LinkedList<>();
        for (MergeAssignmentContext each : ctx.mergeAssignment()) {
            assignments.add((AssignmentSegment) visit(each));
        }
        return new SetAssignmentSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), assignments);
    }
    
    @Override
    public ASTNode visitMergeAssignment(final MergeAssignmentContext ctx) {
        ColumnSegment column = (ColumnSegment) visitColumnName(ctx.columnName());
        ExpressionSegment value = (ExpressionSegment) visit(ctx.mergeAssignmentValue());
        return new AssignmentSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), column, value);
    }
    
    @Override
    public ASTNode visitMergeAssignmentValue(final MergeAssignmentValueContext ctx) {
        ExprContext expr = ctx.expr();
        if (null != expr) {
            return visit(expr);
        }
        return new CommonExpressionSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitDeleteWhereClause(final DeleteWhereClauseContext ctx) {
        ASTNode segment = visit(ctx.whereClause().expr());
        return new WhereSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), (ExpressionSegment) segment);
    }
}
