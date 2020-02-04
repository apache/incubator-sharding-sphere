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

package org.apache.shardingsphere.sql.parser;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.shardingsphere.sql.parser.api.SQLVisitor;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementBaseVisitor;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.AddColumnSpecificationContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.AddConstraintSpecificationContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.AggregationFunctionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.AlterSpecification_Context;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.AlterTableContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.AlterUserContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.AssignmentValuesContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.AutoCommitValueContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.BeginTransactionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.BitExprContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.BooleanLiteralsContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.BooleanPrimaryContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.CastFunctionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.ChangeColumnSpecificationContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.CharFunctionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.ColumnDefinitionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.ColumnNameContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.ColumnNamesContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.CommitContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.CommonDataTypeOption_Context;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.ConstraintDefinition_Context;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.ConvertFunctionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.CreateDefinitionClause_Context;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.CreateDefinition_Context;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.CreateIndexContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.CreateLikeClause_Context;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.CreateRoleContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.CreateTableContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.CreateUserContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.DataTypeName_Context;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.DropColumnSpecificationContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.DropIndexContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.DropRoleContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.DropTableContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.DropUserContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.ExprContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.ExtractFunctionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.FirstOrAfterColumnContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.ForeignKeyOption_Context;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.FromSchemaContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.FunctionCallContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.GeneratedDataType_Context;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.GroupConcatFunctionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.IdentifierContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.IndexDefinition_Context;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.IndexNameContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.InlineDataType_Context;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.IntervalExpressionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.LiteralsContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.ModifyColumnSpecificationContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.NumberLiteralsContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.OrderByClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.OrderByItemContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.OwnerContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.ParameterMarkerContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.PositionFunctionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.PredicateContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.RegularFunctionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.RenameUserContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.RollbackContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.SavepointContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.SchemaNameContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.SelectClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.SelectSpecificationContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.SetAutoCommitContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.SetDefaultRoleContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.SetPasswordContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.SetTransactionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.ShowLikeContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.ShowTableStatusContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.SimpleExprContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.SpecialFunctionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.StringLiteralsContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.SubstringFunctionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.TableNameContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.TableNamesContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.TruncateTableContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.UnreservedWord_Context;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.UseContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.WeightStringFunctionContext;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.WindowFunctionContext;
import org.apache.shardingsphere.sql.parser.core.constant.AggregationType;
import org.apache.shardingsphere.sql.parser.core.constant.LogicalOperator;
import org.apache.shardingsphere.sql.parser.core.constant.OrderDirection;
import org.apache.shardingsphere.sql.parser.sql.ASTNode;
import org.apache.shardingsphere.sql.parser.sql.segment.dal.FromSchemaSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dal.ShowLikeSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.ddl.column.ColumnDefinitionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.ddl.column.position.ColumnAfterPositionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.ddl.column.position.ColumnFirstPositionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.ddl.column.position.ColumnPositionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.ddl.index.IndexSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.assignment.InsertValuesSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.column.ColumnSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.column.InsertColumnsSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.expr.ExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.expr.complex.CommonExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.expr.complex.SubquerySegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.expr.simple.LiteralExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.expr.simple.ParameterMarkerExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.item.AggregationDistinctProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.item.AggregationProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.item.ExpressionProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.order.OrderBySegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.order.item.ColumnOrderByItemSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.order.item.ExpressionOrderByItemSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.order.item.IndexOrderByItemSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.order.item.OrderByItemSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.predicate.AndPredicate;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.predicate.OrPredicateSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.predicate.PredicateSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.predicate.value.PredicateBetweenRightValue;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.predicate.value.PredicateCompareRightValue;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.predicate.value.PredicateInRightValue;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.SchemaSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.TableSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.tcl.AutoCommitSegment;
import org.apache.shardingsphere.sql.parser.sql.statement.dal.dialect.mysql.ShowTableStatusStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dal.dialect.mysql.UseStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dcl.CreateUserStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dcl.DCLStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.ddl.AlterTableStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.ddl.CreateIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.ddl.CreateTableStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.ddl.DDLStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.ddl.DropIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.ddl.DropTableStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.ddl.TruncateStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.tcl.BeginTransactionStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.tcl.CommitStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.tcl.RollbackStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.tcl.SavepointStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.tcl.SetAutoCommitStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.tcl.SetTransactionStatement;
import org.apache.shardingsphere.sql.parser.sql.value.BooleanValue;
import org.apache.shardingsphere.sql.parser.sql.value.ListValue;
import org.apache.shardingsphere.sql.parser.sql.value.LiteralValue;
import org.apache.shardingsphere.sql.parser.sql.value.NumberValue;
import org.apache.shardingsphere.sql.parser.sql.value.ParameterMarkerValue;
import org.apache.shardingsphere.sql.parser.util.SQLUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * MySQL visitor.
 *
 * @author panjuan
 */
public class MySQLVisitor extends MySQLStatementBaseVisitor<ASTNode> implements SQLVisitor {
    
    protected int currentParameterIndex;
    
    // DALStatement.g4
    @Override
    public ASTNode visitUse(final UseContext ctx) {
        LiteralValue schema = (LiteralValue) visit(ctx.schemaName());
        UseStatement useStatement = new UseStatement();
        useStatement.setSchema(schema.getLiteral());
        return useStatement;
    }
    
    @Override
    public ASTNode visitShowTableStatus(final ShowTableStatusContext ctx) {
        ShowTableStatusStatement showTableStatusStatement = new ShowTableStatusStatement();
        FromSchemaContext fromSchemaContext = ctx.fromSchema();
        ShowLikeContext showLikeContext = ctx.showLike();
        if (null != fromSchemaContext) {
            FromSchemaSegment fromSchemaSegment = (FromSchemaSegment) visit(ctx.fromSchema());
            showTableStatusStatement.getAllSQLSegments().add(fromSchemaSegment);
        }
        if (null != showLikeContext) {
            ShowLikeSegment showLikeSegment = (ShowLikeSegment) visit(ctx.showLike());
            showTableStatusStatement.getAllSQLSegments().add(showLikeSegment);
        }
        return showTableStatusStatement;
    }
    
    @Override
    public ASTNode visitFromSchema(final FromSchemaContext ctx) {
        return new FromSchemaSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex());
    }
    
    @Override
    public ASTNode visitShowLike(final ShowLikeContext ctx) {
        LiteralValue literalValue = (LiteralValue) visit(ctx.stringLiterals());
        return new ShowLikeSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), literalValue.getLiteral());
    }
    
    // DCLStatement.g4
    @Override
    public ASTNode visitCreateUser(final CreateUserContext ctx) {
        return new CreateUserStatement();
    }
    
    @Override
    public ASTNode visitDropRole(final DropRoleContext ctx) {
        return new DCLStatement();
    }
    
    @Override
    public ASTNode visitSetDefaultRole(final SetDefaultRoleContext ctx) {
        return new DCLStatement();
    }
    
    @Override
    public ASTNode visitCreateRole(final CreateRoleContext ctx) {
        return new DCLStatement();
    }

    @Override
    public ASTNode visitDropUser(final DropUserContext ctx) {
        return new DCLStatement();
    }

    @Override
    public ASTNode visitAlterUser(final AlterUserContext ctx) {
        return new DCLStatement();
    }

    @Override
    public ASTNode visitRenameUser(final RenameUserContext ctx) {
        return new DCLStatement();
    }

    @Override
    public ASTNode visitSetPassword(final SetPasswordContext ctx) {
        return new DCLStatement();
    }

    // DDLStatement.g4
    @Override
    public ASTNode visitCreateTable(final CreateTableContext ctx) {
        CreateTableStatement result = new CreateTableStatement();
        TableSegment table = (TableSegment) visit(ctx.tableName());
        result.setTable(table);
        result.getAllSQLSegments().add(table);
        CreateDefinitionClause_Context createDefinitionClause = ctx.createDefinitionClause_();
        if (null != createDefinitionClause) {
            for (CreateDefinition_Context createDefinition : createDefinitionClause.createDefinitions_().createDefinition_()) {
                ColumnDefinitionContext columnDefinition = createDefinition.columnDefinition();
                if (null != columnDefinition) {
                    ColumnDefinitionSegment columnDefinitionSegment = createColumnDefinitionSegment(columnDefinition, result);
                    result.getColumnDefinitions().add(columnDefinitionSegment);
                    result.getAllSQLSegments().add(columnDefinitionSegment);
                }
                ConstraintDefinition_Context constraintDefinition = createDefinition.constraintDefinition_();
                ForeignKeyOption_Context foreignKeyOption = null == constraintDefinition ? null : constraintDefinition.foreignKeyOption_();
                if (null != foreignKeyOption) {
                    result.getAllSQLSegments().add((TableSegment) visit(foreignKeyOption.referenceDefinition_().tableName()));
                }
            }
        }
        CreateLikeClause_Context createLikeClause = ctx.createLikeClause_();
        if (null != createLikeClause) {
            result.getAllSQLSegments().add((TableSegment) visit(createLikeClause));
        }
        return result;
    }

    @Override
    public ASTNode visitAlterTable(final AlterTableContext ctx) {
        AlterTableStatement result = new AlterTableStatement();
        TableSegment table = (TableSegment) visit(ctx.tableName());
        result.setTable(table);
        result.getAllSQLSegments().add(table);
        if (null != ctx.alterDefinitionClause_()) {
            for (AlterSpecification_Context alterSpecification : ctx.alterDefinitionClause_().alterSpecification_()) {
                AddColumnSpecificationContext addColumnSpecification = alterSpecification.addColumnSpecification();
                if (null != addColumnSpecification) {
                    List<ColumnDefinitionContext> columnDefinitions = addColumnSpecification.columnDefinition();
                    ColumnDefinitionSegment columnDefinitionSegment = null;
                    for (ColumnDefinitionContext columnDefinition : columnDefinitions) {
                        columnDefinitionSegment = createColumnDefinitionSegment(columnDefinition, result);
                        result.getAddedColumnDefinitions().add(columnDefinitionSegment);
                        result.getAllSQLSegments().add(columnDefinitionSegment);
                    }
                    createColumnPositionSegment(addColumnSpecification.firstOrAfterColumn(), columnDefinitionSegment, result);
                }
                AddConstraintSpecificationContext addConstraintSpecification = alterSpecification.addConstraintSpecification();
                ForeignKeyOption_Context foreignKeyOption = null == addConstraintSpecification
                        ? null : addConstraintSpecification.constraintDefinition_().foreignKeyOption_();
                if (null != foreignKeyOption) {
                    result.getAllSQLSegments().add((TableSegment) visit(foreignKeyOption.referenceDefinition_().tableName()));
                }
                ChangeColumnSpecificationContext changeColumnSpecification = alterSpecification.changeColumnSpecification();
                if (null != changeColumnSpecification) {
                    createColumnPositionSegment(changeColumnSpecification.firstOrAfterColumn(),
                            createColumnDefinitionSegment(changeColumnSpecification.columnDefinition(), result), result);
                }
                DropColumnSpecificationContext dropColumnSpecification = alterSpecification.dropColumnSpecification();
                if (null != dropColumnSpecification) {
                    result.getDroppedColumnNames().add(((ColumnSegment) visit(dropColumnSpecification)).getName());
                }
                ModifyColumnSpecificationContext modifyColumnSpecification = alterSpecification.modifyColumnSpecification();
                if (null != modifyColumnSpecification) {
                    createColumnPositionSegment(modifyColumnSpecification.firstOrAfterColumn(),
                            createColumnDefinitionSegment(modifyColumnSpecification.columnDefinition(), result), result);
                }
            }
        }
        return result;
    }

    @Override
    public ASTNode visitDropTable(final DropTableContext ctx) {
        DropTableStatement result = new DropTableStatement();
        ListValue<TableSegment> tables = (ListValue<TableSegment>) visit(ctx.tableNames());
        result.getTables().addAll(tables.getValues());
        result.getAllSQLSegments().addAll(tables.getValues());
        return result;
    }
    
    @Override
    public ASTNode visitTruncateTable(final TruncateTableContext ctx) {
        TruncateStatement result = new TruncateStatement();
        TableSegment table = (TableSegment) visit(ctx.tableName());
        result.getAllSQLSegments().add(table);
        result.getTables().add(table);
        return result;
    }

    @Override
    public ASTNode visitCreateIndex(final CreateIndexContext ctx) {
        CreateIndexStatement result = new CreateIndexStatement();
        TableSegment table = (TableSegment) visit(ctx.tableName());
        result.setTable(table);
        result.getAllSQLSegments().add(table);
        return result;
    }

    @Override
    public ASTNode visitDropIndex(final DropIndexContext ctx) {
        DropIndexStatement result = new DropIndexStatement();
        TableSegment table = (TableSegment) visit(ctx.tableName());
        result.setTable(table);
        result.getAllSQLSegments().add(table);
        return result;
    }

    @Override
    public ASTNode visitIndexDefinition_(final IndexDefinition_Context ctx) {
        return visit(ctx.indexName());
    }

    @Override
    public ASTNode visitCreateLikeClause_(final CreateLikeClause_Context ctx) {
        return visit(ctx.tableName());
    }

    @Override
    public ASTNode visitDropColumnSpecification(final DropColumnSpecificationContext ctx) {
        return visit(ctx.columnName());
    }
    
    // TCLStatement.g4
    @Override
    public ASTNode visitSetTransaction(final SetTransactionContext ctx) {
        return new SetTransactionStatement();
    }
    
    @Override
    public ASTNode visitSetAutoCommit(final SetAutoCommitContext ctx) {
        SetAutoCommitStatement result = new SetAutoCommitStatement();
        AutoCommitValueContext autoCommitValueContext = ctx.autoCommitValue();
        if (null != autoCommitValueContext) {
            AutoCommitSegment autoCommitSegment = (AutoCommitSegment) visit(ctx.autoCommitValue());
            result.getAllSQLSegments().add(autoCommitSegment);
            result.setAutoCommit(autoCommitSegment.isAutoCommit());
        }
        return result;
    }
    
    @Override
    public ASTNode visitAutoCommitValue(final AutoCommitValueContext ctx) {
        boolean autoCommit = "1".equals(ctx.getText()) || "ON".equals(ctx.getText());
        return new AutoCommitSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), autoCommit);
    }
    
    @Override
    public ASTNode visitBeginTransaction(final BeginTransactionContext ctx) {
        return new BeginTransactionStatement();
    }
    
    @Override
    public ASTNode visitCommit(final CommitContext ctx) {
        return new CommitStatement();
    }
    
    @Override
    public ASTNode visitRollback(final RollbackContext ctx) {
        return new RollbackStatement();
    }
    
    @Override
    public ASTNode visitSavepoint(final SavepointContext ctx) {
        return new SavepointStatement();
    }
    
    // StoreProcedure.g4
    
    // BaseRule.g4
    @Override
    public ASTNode visitSchemaName(final SchemaNameContext ctx) {
        return visit(ctx.identifier());
    }
    
    @Override
    public ASTNode visitTableNames(final TableNamesContext ctx) {
        ListValue<TableSegment> result = new ListValue<>(new LinkedList<TableSegment>());
        for (TableNameContext each : ctx.tableName()) {
            result.getValues().add((TableSegment) visit(each));
        }
        return result;
    }

    @Override
    public ASTNode visitTableName(final TableNameContext ctx) {
        LiteralValue tableName = (LiteralValue) visit(ctx.name());
        TableSegment result = new TableSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), tableName.getLiteral());
        OwnerContext owner = ctx.owner();
        if (null != owner) {
            result.setOwner(createSchemaSegment(owner));
        }
        return result;
    }
    
    @Override
    public ASTNode visitColumnNames(final ColumnNamesContext ctx) {
        Collection<ColumnSegment> segments = new LinkedList<>();
        for (ColumnNameContext each : ctx.columnName()) {
            segments.add((ColumnSegment) visit(each));
        }
        InsertColumnsSegment result = new InsertColumnsSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex());
        result.getColumns().addAll(segments);
        return result;
    }
    
    @Override
    public ASTNode visitColumnName(final ColumnNameContext ctx) {
        LiteralValue columnName = (LiteralValue) visit(ctx.name());
        ColumnSegment result = new ColumnSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), columnName.getLiteral());
        OwnerContext owner = ctx.owner();
        if (null != owner) {
            result.setOwner(createTableSegment(owner));
        }
        return result;
    }
    
    @Override
    public ASTNode visitIndexName(final IndexNameContext ctx) {
        LiteralValue indexName = (LiteralValue) visit(ctx.identifier());
        return new IndexSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), indexName.getLiteral());
    }

    @Override
    public ASTNode visitDataTypeName_(final DataTypeName_Context ctx) {
        return visit(ctx.identifier(0));
    }

    @Override
    public ASTNode visitExpr(final ExprContext ctx) {
        BooleanPrimaryContext bool = ctx.booleanPrimary();
        if (null != bool) {
            return visit(bool);
        } else if (null != ctx.logicalOperator()) {
            return mergePredicateSegment(visit(ctx.expr(0)), visit(ctx.expr(1)), ctx.logicalOperator().getText());
        } else if (!ctx.expr().isEmpty()) {
            return visit(ctx.expr(0));
        }
        return createExpressionSegment(new LiteralValue(ctx.getText()), ctx);
    }
    
    @Override
    public ASTNode visitBooleanPrimary(final BooleanPrimaryContext ctx) {
        if (null != ctx.subquery()) {
            return new SubquerySegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.subquery().getText());
        }
        if (null != ctx.comparisonOperator()) {
            return createCompareSegment(ctx);
        }
        if (null != ctx.predicate()) {
            return visit(ctx.predicate());
        }
        return createExpressionSegment(new LiteralValue(ctx.getText()), ctx);
    }
    
    @Override
    public ASTNode visitPredicate(final PredicateContext ctx) {
        if (null != ctx.subquery()) {
            return new SubquerySegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.subquery().getText());
        }
        if (null != ctx.IN()) {
            return createInSegment(ctx);
        }
        if (null != ctx.BETWEEN()) {
            return createBetweenSegment(ctx);
        }
        BitExprContext bitExpr = ctx.bitExpr(0);
        if (null != bitExpr) {
            return createExpressionSegment(visit(bitExpr), ctx);
        }
        return createExpressionSegment(new LiteralValue(ctx.getText()), ctx);
    }
    
    @Override
    public ASTNode visitBitExpr(final BitExprContext ctx) {
        SimpleExprContext simple = ctx.simpleExpr();
        if (null != simple) {
            return visit(simple);
        }
        return new LiteralValue(ctx.getText());
    }
    
    @Override
    public ASTNode visitSimpleExpr(final SimpleExprContext ctx) {
        if (null != ctx.subquery()) {
            return new SubquerySegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.subquery().getText());
        }
        if (null != ctx.parameterMarker()) {
            return visit(ctx.parameterMarker());
        }
        if (null != ctx.literals()) {
            return visit(ctx.literals());
        }
        if (null != ctx.intervalExpression()) {
            return visit(ctx.intervalExpression());
        }
        if (null != ctx.functionCall()) {
            return visit(ctx.functionCall());
        }
        if (null != ctx.columnName()) {
            return visit(ctx.columnName());
        }
        return new CommonExpressionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitParameterMarker(final ParameterMarkerContext ctx) {
        return new ParameterMarkerValue(currentParameterIndex++);
    }
    
    @Override
    public ASTNode visitLiterals(final LiteralsContext ctx) {
        if (null != ctx.stringLiterals()) {
            return visit(ctx.stringLiterals());
        }
        if (null != ctx.numberLiterals()) {
            return visit(ctx.numberLiterals());
        }
        if (null != ctx.booleanLiterals()) {
            return visit(ctx.booleanLiterals());
        }
        if (null != ctx.nullValueLiterals()) {
            return new CommonExpressionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
        }
        return new LiteralValue(ctx.getText());
    }
    
    @Override
    public ASTNode visitStringLiterals(final StringLiteralsContext ctx) {
        String text = ctx.getText();
        return new LiteralValue(text.substring(1, text.length() - 1));
    }
    
    @Override
    public ASTNode visitNumberLiterals(final NumberLiteralsContext ctx) {
        return new NumberValue(ctx.getText());
    }
    
    @Override
    public ASTNode visitBooleanLiterals(final BooleanLiteralsContext ctx) {
        return new BooleanValue(ctx.getText());
    }
    
    @Override
    public ASTNode visitIntervalExpression(final IntervalExpressionContext ctx) {
        calculateParameterCount(Collections.singleton(ctx.expr()));
        return new ExpressionProjectionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitOrderByClause(final OrderByClauseContext ctx) {
        Collection<OrderByItemSegment> items = new LinkedList<>();
        for (OrderByItemContext each : ctx.orderByItem()) {
            items.add((OrderByItemSegment) visit(each));
        }
        return new OrderBySegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), items);
    }
    
    @Override
    public ASTNode visitOrderByItem(final OrderByItemContext ctx) {
        OrderDirection orderDirection = null != ctx.DESC() ? OrderDirection.DESC : OrderDirection.ASC;
        if (null != ctx.columnName()) {
            ColumnSegment column = (ColumnSegment) visit(ctx.columnName());
            return new ColumnOrderByItemSegment(column, orderDirection);
        }
        if (null != ctx.numberLiterals()) {
            return new IndexOrderByItemSegment(ctx.numberLiterals().getStart().getStartIndex(), ctx.numberLiterals().getStop().getStopIndex(),
                    SQLUtil.getExactlyNumber(ctx.numberLiterals().getText(), 10).intValue(), orderDirection);
        }
        return new ExpressionOrderByItemSegment(ctx.expr().getStart().getStartIndex(), ctx.expr().getStop().getStopIndex(), ctx.expr().getText(), orderDirection);
    }
    
    @Override
    public ASTNode visitFunctionCall(final FunctionCallContext ctx) {
        if (null != ctx.aggregationFunction()) {
            return visit(ctx.aggregationFunction());
        }
        if (null != ctx.regularFunction()) {
            return visit(ctx.regularFunction());
        }
        if (null != ctx.specialFunction()) {
            return visit(ctx.specialFunction());
        }
        return new ExpressionProjectionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitAggregationFunction(final AggregationFunctionContext ctx) {
        if (AggregationType.isAggregationType(ctx.aggregationFunctionName_().getText())) {
            return createAggregationSegment(ctx);
        }
        return new ExpressionProjectionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitSpecialFunction(final SpecialFunctionContext ctx) {
        if (null != ctx.groupConcatFunction()) {
            return visit(ctx.groupConcatFunction());
        }
        if (null != ctx.windowFunction()) {
            return visit(ctx.windowFunction());
        }
        if (null != ctx.castFunction()) {
            return visit(ctx.castFunction());
        }
        if (null != ctx.convertFunction()) {
            return visit(ctx.convertFunction());
        }
        if (null != ctx.positionFunction()) {
            return visit(ctx.positionFunction());
        }
        if (null != ctx.substringFunction()) {
            return visit(ctx.substringFunction());
        }
        if (null != ctx.extractFunction()) {
            return visit(ctx.extractFunction());
        }
        if (null != ctx.charFunction()) {
            return visit(ctx.charFunction());
        }
        if (null != ctx.weightStringFunction()) {
            return visit(ctx.weightStringFunction());
        }
        return new ExpressionProjectionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitGroupConcatFunction(final GroupConcatFunctionContext ctx) {
        calculateParameterCount(ctx.expr());
        return new ExpressionProjectionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitWindowFunction(final WindowFunctionContext ctx) {
        calculateParameterCount(ctx.expr());
        return new ExpressionProjectionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitCastFunction(final CastFunctionContext ctx) {
        calculateParameterCount(Collections.singleton(ctx.expr()));
        return new ExpressionProjectionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitConvertFunction(final ConvertFunctionContext ctx) {
        calculateParameterCount(Collections.singleton(ctx.expr()));
        return new ExpressionProjectionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitPositionFunction(final PositionFunctionContext ctx) {
        calculateParameterCount(ctx.expr());
        return new ExpressionProjectionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitSubstringFunction(final SubstringFunctionContext ctx) {
        calculateParameterCount(Collections.singleton(ctx.expr()));
        return new ExpressionProjectionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitExtractFunction(final ExtractFunctionContext ctx) {
        calculateParameterCount(Collections.singleton(ctx.expr()));
        return new ExpressionProjectionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitCharFunction(final CharFunctionContext ctx) {
        calculateParameterCount(ctx.expr());
        return new ExpressionProjectionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitWeightStringFunction(final WeightStringFunctionContext ctx) {
        calculateParameterCount(Collections.singleton(ctx.expr()));
        return new ExpressionProjectionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitRegularFunction(final RegularFunctionContext ctx) {
        calculateParameterCount(ctx.expr());
        return new ExpressionProjectionSegment(ctx.start.getStartIndex(), ctx.stop.getStopIndex(), ctx.getText());
    }
    
    @Override
    public ASTNode visitIdentifier(final IdentifierContext ctx) {
        UnreservedWord_Context unreservedWord = ctx.unreservedWord_();
        if (null != unreservedWord) {
            return visit(unreservedWord);
        }
        return new LiteralValue(ctx.getText());
    }
    
    @Override
    public ASTNode visitUnreservedWord_(final UnreservedWord_Context ctx) {
        return new LiteralValue(ctx.getText());
    }
    
    // Segments
    private SchemaSegment createSchemaSegment(final OwnerContext ownerContext) {
        LiteralValue literalValue = (LiteralValue) visit(ownerContext.identifier());
        return new SchemaSegment(ownerContext.getStart().getStartIndex(), ownerContext.getStop().getStopIndex(), literalValue.getLiteral());
    }
    
    private TableSegment createTableSegment(final OwnerContext ownerContext) {
        LiteralValue literalValue = (LiteralValue) visit(ownerContext.identifier());
        return new TableSegment(ownerContext.getStart().getStartIndex(), ownerContext.getStop().getStopIndex(), literalValue.getLiteral());
    }
    
    private ASTNode createExpressionSegment(final ASTNode astNode, final ParserRuleContext context) {
        if (astNode instanceof LiteralValue) {
            return new LiteralExpressionSegment(context.start.getStartIndex(), context.stop.getStopIndex(), ((LiteralValue) astNode).getLiteral());
        }
        if (astNode instanceof NumberValue) {
            return new LiteralExpressionSegment(context.start.getStartIndex(), context.stop.getStopIndex(), ((NumberValue) astNode).getNumber());
        }
        if (astNode instanceof ParameterMarkerValue) {
            return new ParameterMarkerExpressionSegment(context.start.getStartIndex(), context.stop.getStopIndex(), ((ParameterMarkerValue) astNode).getParameterIndex());
        }
        return astNode;
    }
    
    private ColumnDefinitionSegment createColumnDefinitionSegment(final ColumnDefinitionContext columnDefinition, final DDLStatement statement) {
        ColumnSegment column = (ColumnSegment) visit(columnDefinition.columnName());
        LiteralValue dataType = (LiteralValue) visit(columnDefinition.dataType().dataTypeName_());
        boolean isPrimaryKey = false;
        for (InlineDataType_Context inlineDataType : columnDefinition.inlineDataType_()) {
            CommonDataTypeOption_Context commonDataTypeOption = inlineDataType.commonDataTypeOption_();
            if (null != commonDataTypeOption) {
                if (null != commonDataTypeOption.primaryKey()) {
                    isPrimaryKey = true;
                }
                if (null != commonDataTypeOption.referenceDefinition_()) {
                    statement.getAllSQLSegments().add((TableSegment) visit(commonDataTypeOption.referenceDefinition_().tableName()));
                }
            }
        }
        for (GeneratedDataType_Context generatedDataType: columnDefinition.generatedDataType_()) {
            CommonDataTypeOption_Context commonDataTypeOption = generatedDataType.commonDataTypeOption_();
            if (null != commonDataTypeOption) {
                if (null != commonDataTypeOption.primaryKey()) {
                    isPrimaryKey = true;
                }
                if (null != commonDataTypeOption.referenceDefinition_()) {
                    statement.getAllSQLSegments().add((TableSegment) visit(commonDataTypeOption.referenceDefinition_().tableName()));
                }
            }
        }
        return new ColumnDefinitionSegment(column.getStartIndex(), column.getStopIndex(),
                column.getName(), dataType.getLiteral(), isPrimaryKey);
    }
    
    private void createColumnPositionSegment(final FirstOrAfterColumnContext firstOrAfterColumn, final ColumnDefinitionSegment columnDefinition, final AlterTableStatement statement) {
        if (null != firstOrAfterColumn) {
            ColumnPositionSegment columnPositionSegment = null;
            if (null != firstOrAfterColumn.FIRST()) {
                columnPositionSegment = new ColumnFirstPositionSegment(columnDefinition.getStartIndex(), columnDefinition.getStopIndex(),
                        columnDefinition.getColumnName());
            } else if (null != firstOrAfterColumn.AFTER()) {
                ColumnSegment afterColumn = (ColumnSegment) visit(firstOrAfterColumn.columnName());
                columnPositionSegment = new ColumnAfterPositionSegment(columnDefinition.getStartIndex(), columnDefinition.getStopIndex(),
                        columnDefinition.getColumnName(), afterColumn.getName());
            }
            statement.getChangedPositionColumns().add(columnPositionSegment);
            statement.getAllSQLSegments().add(columnPositionSegment);
        }
    }
    
    protected Collection<InsertValuesSegment> createInsertValuesSegments(final Collection<AssignmentValuesContext> assignmentValuesContexts) {
        Collection<InsertValuesSegment> result = new LinkedList<>();
        for (AssignmentValuesContext each : assignmentValuesContexts) {
            result.add((InsertValuesSegment) visit(each));
        }
        return result;
    }
    
    private ASTNode createAggregationSegment(final AggregationFunctionContext ctx) {
        AggregationType type = AggregationType.valueOf(ctx.aggregationFunctionName_().getText());
        int innerExpressionStartIndex = ((TerminalNode) ctx.getChild(1)).getSymbol().getStartIndex();
        if (null != ctx.distinct()) {
            return new AggregationDistinctProjectionSegment(ctx.getStart().getStartIndex(),
                    ctx.getStop().getStopIndex(), ctx.getText(), type, innerExpressionStartIndex, getDistinctExpression(ctx));
        }
        return new AggregationProjectionSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(),
                ctx.getText(), type, innerExpressionStartIndex);
    }
    
    private String getDistinctExpression(final AggregationFunctionContext ctx) {
        StringBuilder result = new StringBuilder();
        for (int i = 3; i < ctx.getChildCount() - 1; i++) {
            result.append(ctx.getChild(i).getText());
        }
        return result.toString();
    }
    
    private PredicateSegment createCompareSegment(final BooleanPrimaryContext ctx) {
        ASTNode leftValue = visit(ctx.booleanPrimary());
        ASTNode rightValue = visit(ctx.predicate());
        if (rightValue instanceof ColumnSegment) {
            return new PredicateSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), (ColumnSegment) leftValue, (ColumnSegment) rightValue);
        }
        return new PredicateSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(),
                (ColumnSegment) leftValue, new PredicateCompareRightValue(ctx.comparisonOperator().getText(), (ExpressionSegment) rightValue));
    }
    
    private PredicateSegment createInSegment(final PredicateContext ctx) {
        ColumnSegment column = (ColumnSegment) visit(ctx.bitExpr(0));
        Collection<ExpressionSegment> segments = Lists.transform(ctx.expr(), new Function<ExprContext, ExpressionSegment>() {
            
            @Override
            public ExpressionSegment apply(final ExprContext input) {
                return (ExpressionSegment) visit(input);
            }
        });
        return new PredicateSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), column, new PredicateInRightValue(segments));
    }
    
    private PredicateSegment createBetweenSegment(final PredicateContext ctx) {
        ColumnSegment column = (ColumnSegment) visit(ctx.bitExpr(0));
        ExpressionSegment between = (ExpressionSegment) visit(ctx.bitExpr(1));
        ExpressionSegment and = (ExpressionSegment) visit(ctx.predicate());
        return new PredicateSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), column, new PredicateBetweenRightValue(between, and));
    }
    
    private OrPredicateSegment mergePredicateSegment(final ASTNode left, final ASTNode right, final String operator) {
        Optional<LogicalOperator> logicalOperator = LogicalOperator.valueFrom(operator);
        Preconditions.checkState(logicalOperator.isPresent());
        if (LogicalOperator.OR == logicalOperator.get()) {
            return mergeOrPredicateSegment(left, right);
        }
        return mergeAndPredicateSegment(left, right);
    }
    
    private OrPredicateSegment mergeOrPredicateSegment(final ASTNode left, final ASTNode right) {
        OrPredicateSegment result = new OrPredicateSegment();
        result.getAndPredicates().addAll(getAndPredicates(left));
        result.getAndPredicates().addAll(getAndPredicates(right));
        return result;
    }
    
    private OrPredicateSegment mergeAndPredicateSegment(final ASTNode left, final ASTNode right) {
        OrPredicateSegment result = new OrPredicateSegment();
        for (AndPredicate eachLeft : getAndPredicates(left)) {
            for (AndPredicate eachRight : getAndPredicates(right)) {
                result.getAndPredicates().add(createAndPredicate(eachLeft, eachRight));
            }
        }
        return result;
    }
    
    private AndPredicate createAndPredicate(final AndPredicate left, final AndPredicate right) {
        AndPredicate result = new AndPredicate();
        result.getPredicates().addAll(left.getPredicates());
        result.getPredicates().addAll(right.getPredicates());
        return result;
    }
    
    protected Collection<AndPredicate> getAndPredicates(final ASTNode astNode) {
        if (astNode instanceof OrPredicateSegment) {
            return ((OrPredicateSegment) astNode).getAndPredicates();
        }
        if (astNode instanceof AndPredicate) {
            return Collections.singleton((AndPredicate) astNode);
        }
        AndPredicate andPredicate = new AndPredicate();
        andPredicate.getPredicates().add((PredicateSegment) astNode);
        return Collections.singleton(andPredicate);
    }
    
    protected boolean isDistinct(final SelectClauseContext ctx) {
        for (SelectSpecificationContext each : ctx.selectSpecification()) {
            boolean eachDistinct = ((BooleanValue) visit(each)).isCorrect();
            if (eachDistinct) {
                return true;
            }
        }
        return false;
    }
    
    // TODO :FIXME, sql case id: insert_with_str_to_date
    private void calculateParameterCount(final Collection<ExprContext> exprContexts) {
        for (ExprContext each : exprContexts) {
            visit(each);
        }
    }
}
