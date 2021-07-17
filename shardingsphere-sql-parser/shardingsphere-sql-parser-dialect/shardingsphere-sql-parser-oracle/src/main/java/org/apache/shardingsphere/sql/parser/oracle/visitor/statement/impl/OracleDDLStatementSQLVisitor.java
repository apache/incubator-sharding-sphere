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
import org.apache.shardingsphere.sql.parser.api.visitor.ASTNode;
import org.apache.shardingsphere.sql.parser.api.visitor.operation.SQLStatementVisitor;
import org.apache.shardingsphere.sql.parser.api.visitor.type.DDLSQLVisitor;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AddColumnSpecificationContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AddConstraintSpecificationContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AlterDatabaseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AlterDefinitionClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AlterIndexContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AlterSessionContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AlterSynonymContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AlterSystemContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AlterTableContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AnalyzeContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AssociateStatisticsContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.AuditContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ColumnDefinitionContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ColumnNameContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ColumnOrVirtualDefinitionContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.CommentContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ConstraintClausesContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.CreateDefinitionClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.CreateIndexContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.CreateTableContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.DisassociateStatisticsContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.DropColumnSpecificationContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.DropConstraintClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.DropIndexContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.DropTableContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.IndexNameContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.InlineConstraintContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ModifyColPropertiesContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ModifyColumnSpecificationContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.ModifyConstraintClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.NoAuditContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.OperateColumnClauseContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.OutOfLineConstraintContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.OutOfLineRefConstraintContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.RelationalPropertyContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.TableNameContext;
import org.apache.shardingsphere.sql.parser.autogen.OracleStatementParser.TruncateTableContext;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.AlterDefinitionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.CreateDefinitionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.column.ColumnDefinitionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.column.alter.AddColumnDefinitionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.column.alter.DropColumnDefinitionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.column.alter.ModifyColumnDefinitionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.constraint.ConstraintDefinitionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.constraint.ConstraintSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.constraint.alter.AddConstraintDefinitionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.constraint.alter.DropConstraintDefinitionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.constraint.alter.ModifyConstraintDefinitionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.index.IndexSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.column.ColumnSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.DataTypeSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.SimpleTableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.value.collection.CollectionValue;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleAlterDatabaseStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleAlterIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleAlterSessionStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleAlterSynonymStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleAlterSystemStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleAlterTableStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleAnalyzeStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleAssociateStatisticsStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleAuditStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleCommentStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleCreateIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleCreateTableStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleDisassociateStatisticsStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleDropIndexStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleDropTableStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleNoAuditStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleTruncateStatement;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Properties;

/**
 * DDL Statement SQL visitor for Oracle.
 */
@NoArgsConstructor
public final class OracleDDLStatementSQLVisitor extends OracleStatementSQLVisitor implements DDLSQLVisitor, SQLStatementVisitor {
    
    public OracleDDLStatementSQLVisitor(final Properties props) {
        super(props);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ASTNode visitCreateTable(final CreateTableContext ctx) {
        OracleCreateTableStatement result = new OracleCreateTableStatement();
        result.setTable((SimpleTableSegment) visit(ctx.tableName()));
        if (null != ctx.createDefinitionClause()) {
            CollectionValue<CreateDefinitionSegment> createDefinitions = (CollectionValue<CreateDefinitionSegment>) visit(ctx.createDefinitionClause());
            for (CreateDefinitionSegment each : createDefinitions.getValue()) {
                if (each instanceof ColumnDefinitionSegment) {
                    result.getColumnDefinitions().add((ColumnDefinitionSegment) each);
                } else if (each instanceof ConstraintDefinitionSegment) {
                    result.getConstraintDefinitions().add((ConstraintDefinitionSegment) each);
                }
            }
        }
        return result;
    }
    
    @Override
    public ASTNode visitCreateDefinitionClause(final CreateDefinitionClauseContext ctx) {
        CollectionValue<CreateDefinitionSegment> result = new CollectionValue<>();
        if (null == ctx.createRelationalTableClause()) {
            return result;
        }
        for (RelationalPropertyContext each : ctx.createRelationalTableClause().relationalProperties().relationalProperty()) {
            if (null != each.columnDefinition()) {
                result.getValue().add((ColumnDefinitionSegment) visit(each.columnDefinition()));
            }
            if (null != each.outOfLineConstraint()) {
                result.getValue().add((ConstraintDefinitionSegment) visit(each.outOfLineConstraint()));
            }
            if (null != each.outOfLineRefConstraint()) {
                result.getValue().add((ConstraintDefinitionSegment) visit(each.outOfLineRefConstraint()));
            }
        }
        return result;
    }
    
    @Override
    public ASTNode visitColumnDefinition(final ColumnDefinitionContext ctx) {
        ColumnSegment column = (ColumnSegment) visit(ctx.columnName());
        DataTypeSegment dataType = (DataTypeSegment) visit(ctx.dataType());
        boolean isPrimaryKey = isPrimaryKey(ctx);
        ColumnDefinitionSegment result = new ColumnDefinitionSegment(
                ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), column, dataType, isPrimaryKey);
        for (InlineConstraintContext each : ctx.inlineConstraint()) {
            if (null != each.referencesClause()) {
                result.getReferencedTables().add((SimpleTableSegment) visit(each.referencesClause().tableName()));
            }
        }
        if (null != ctx.inlineRefConstraint()) {
            result.getReferencedTables().add((SimpleTableSegment) visit(ctx.inlineRefConstraint().tableName()));
        }
        return result;
    }
    
    private boolean isPrimaryKey(final ColumnDefinitionContext ctx) {
        for (InlineConstraintContext each : ctx.inlineConstraint()) {
            if (null != each.primaryKey()) {
                return true;
            }
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ASTNode visitOutOfLineConstraint(final OutOfLineConstraintContext ctx) {
        ConstraintDefinitionSegment result = new ConstraintDefinitionSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex());
        if (null != ctx.constraintName()) {
            result.setConstraintName((ConstraintSegment) visit(ctx.constraintName()));
        }
        if (null != ctx.primaryKey()) {
            result.getPrimaryKeyColumns().addAll(((CollectionValue<ColumnSegment>) visit(ctx.columnNames())).getValue());
        }
        if (null != ctx.UNIQUE()) {
            result.getIndexColumns().addAll(((CollectionValue<ColumnSegment>) visit(ctx.columnNames())).getValue());
        }
        if (null != ctx.referencesClause()) {
            result.setReferencedTable((SimpleTableSegment) visit(ctx.referencesClause().tableName()));
        }
        return result;
    }
    
    @Override
    public ASTNode visitOutOfLineRefConstraint(final OutOfLineRefConstraintContext ctx) {
        ConstraintDefinitionSegment result = new ConstraintDefinitionSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex());
        if (null != ctx.constraintName()) {
            result.setConstraintName((ConstraintSegment) visit(ctx.constraintName()));
        }
        if (null != ctx.referencesClause()) {
            result.setReferencedTable((SimpleTableSegment) visit(ctx.referencesClause().tableName()));
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ASTNode visitAlterTable(final AlterTableContext ctx) {
        OracleAlterTableStatement result = new OracleAlterTableStatement();
        result.setTable((SimpleTableSegment) visit(ctx.tableName()));
        if (null != ctx.alterDefinitionClause()) {
            for (AlterDefinitionSegment each : ((CollectionValue<AlterDefinitionSegment>) visit(ctx.alterDefinitionClause())).getValue()) {
                if (each instanceof AddColumnDefinitionSegment) {
                    result.getAddColumnDefinitions().add((AddColumnDefinitionSegment) each);
                } else if (each instanceof ModifyColumnDefinitionSegment) {
                    result.getModifyColumnDefinitions().add((ModifyColumnDefinitionSegment) each);
                } else if (each instanceof DropColumnDefinitionSegment) {
                    result.getDropColumnDefinitions().add((DropColumnDefinitionSegment) each);
                } else if (each instanceof AddConstraintDefinitionSegment) {
                    result.getAddConstraintDefinitions().add((AddConstraintDefinitionSegment) each);
                } else if (each instanceof ModifyConstraintDefinitionSegment) {
                    result.getModifyConstraintDefinitions().add((ModifyConstraintDefinitionSegment) each);
                } else if (each instanceof DropConstraintDefinitionSegment) {
                    result.getDropConstraintDefinitions().add((DropConstraintDefinitionSegment) each);
                }
            }
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ASTNode visitAlterDefinitionClause(final AlterDefinitionClauseContext ctx) {
        CollectionValue<AlterDefinitionSegment> result = new CollectionValue<>();
        if (null != ctx.columnClauses()) {
            for (OperateColumnClauseContext each : ctx.columnClauses().operateColumnClause()) {
                if (null != each.addColumnSpecification()) {
                    result.getValue().addAll(((CollectionValue<AddColumnDefinitionSegment>) visit(each.addColumnSpecification())).getValue());
                }
                if (null != each.modifyColumnSpecification()) {
                    result.getValue().add((ModifyColumnDefinitionSegment) visit(each.modifyColumnSpecification()));
                }
                if (null != each.dropColumnClause()) {
                    result.getValue().add((DropColumnDefinitionSegment) visit(each.dropColumnClause()));
                }
            }
        }
        if (null != ctx.constraintClauses()) {
            // TODO Support rename constraint
            ConstraintClausesContext constraintClausesContext = ctx.constraintClauses();
            if (null != constraintClausesContext.addConstraintSpecification()) {
                result.combine((CollectionValue<AlterDefinitionSegment>) visit(constraintClausesContext.addConstraintSpecification()));
            }
            if (null != constraintClausesContext.modifyConstraintClause()) {
                result.getValue().add((AlterDefinitionSegment) visit(constraintClausesContext.modifyConstraintClause()));
            }
            for (DropConstraintClauseContext each : constraintClausesContext.dropConstraintClause()) {
                if (null != each.constraintName()) {
                    result.getValue().add((AlterDefinitionSegment) visit(each));
                }
            }
        }
        return result;
    }
    
    @Override
    public ASTNode visitAddColumnSpecification(final AddColumnSpecificationContext ctx) {
        CollectionValue<AddColumnDefinitionSegment> result = new CollectionValue<>();
        for (ColumnOrVirtualDefinitionContext each : ctx.columnOrVirtualDefinitions().columnOrVirtualDefinition()) {
            if (null != each.columnDefinition()) {
                AddColumnDefinitionSegment addColumnDefinition = new AddColumnDefinitionSegment(
                        each.columnDefinition().getStart().getStartIndex(), each.columnDefinition().getStop().getStopIndex(), 
                        Collections.singletonList((ColumnDefinitionSegment) visit(each.columnDefinition())));
                result.getValue().add(addColumnDefinition);
            }
        }
        return result;
    }
    
    @Override
    public ASTNode visitModifyColumnSpecification(final ModifyColumnSpecificationContext ctx) {
        // TODO handle no columnDefinition and multiple columnDefinitions 
        ColumnDefinitionSegment columnDefinition = null;
        for (ModifyColPropertiesContext each : ctx.modifyColProperties()) {
            columnDefinition = (ColumnDefinitionSegment) visit(each);
        }
        return new ModifyColumnDefinitionSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), columnDefinition);
    }
    
    @Override
    public ASTNode visitModifyColProperties(final ModifyColPropertiesContext ctx) {
        ColumnSegment column = (ColumnSegment) visit(ctx.columnName());
        DataTypeSegment dataType = (DataTypeSegment) visit(ctx.dataType());
        // TODO visit pk and reference table
        return new ColumnDefinitionSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), column, dataType, false);
    }
    
    @Override
    public ASTNode visitDropColumnSpecification(final DropColumnSpecificationContext ctx) {
        Collection<ColumnSegment> columns = new LinkedList<>();
        if (null != ctx.columnOrColumnList().columnName()) {
            columns.add((ColumnSegment) visit(ctx.columnOrColumnList().columnName()));
        } else {
            for (ColumnNameContext each : ctx.columnOrColumnList().columnNames().columnName()) {
                columns.add((ColumnSegment) visit(each));
            }
        }
        return new DropColumnDefinitionSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), columns);
    }
    
    @Override
    public ASTNode visitAddConstraintSpecification(final AddConstraintSpecificationContext ctx) {
        CollectionValue<AddConstraintDefinitionSegment> result = new CollectionValue<>();
        for (OutOfLineConstraintContext each : ctx.outOfLineConstraint()) {
            result.getValue().add(new AddConstraintDefinitionSegment(each.getStart().getStartIndex(), each.getStop().getStopIndex(), (ConstraintDefinitionSegment) visit(each)));
        }
        if (null != ctx.outOfLineRefConstraint()) {
            result.getValue().add(new AddConstraintDefinitionSegment(ctx.outOfLineRefConstraint().getStart().getStartIndex(), ctx.outOfLineRefConstraint().getStop().getStopIndex(),
                    (ConstraintDefinitionSegment) visit(ctx.outOfLineRefConstraint())));
        }
        return result;
    }
    
    @Override
    public ASTNode visitModifyConstraintClause(final ModifyConstraintClauseContext ctx) {
        return new ModifyConstraintDefinitionSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(),
                (ConstraintSegment) visit(ctx.constraintOption().constraintWithName().constraintName()));
    }
    
    @Override
    public ASTNode visitDropConstraintClause(final DropConstraintClauseContext ctx) {
        return new DropConstraintDefinitionSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), (ConstraintSegment) visit(ctx.constraintName()));
    }
    
    @Override
    public ASTNode visitDropTable(final DropTableContext ctx) {
        OracleDropTableStatement result = new OracleDropTableStatement();
        result.getTables().add((SimpleTableSegment) visit(ctx.tableName()));
        return result;
    }
    
    @Override
    public ASTNode visitTruncateTable(final TruncateTableContext ctx) {
        OracleTruncateStatement result = new OracleTruncateStatement();
        result.getTables().add((SimpleTableSegment) visit(ctx.tableName()));
        return result;
    }
    
    @Override
    public ASTNode visitCreateIndex(final CreateIndexContext ctx) {
        OracleCreateIndexStatement result = new OracleCreateIndexStatement();
        if (null != ctx.createIndexDefinitionClause().tableIndexClause()) {
            result.setTable((SimpleTableSegment) visit(ctx.createIndexDefinitionClause().tableIndexClause().tableName()));
        }
        result.setIndex((IndexSegment) visit(ctx.indexName()));
        return result;
    }
    
    @Override
    public ASTNode visitAlterIndex(final AlterIndexContext ctx) {
        OracleAlterIndexStatement result = new OracleAlterIndexStatement();
        result.setIndex((IndexSegment) visit(ctx.indexName()));
        return result;
    }
    
    @Override
    public ASTNode visitDropIndex(final DropIndexContext ctx) {
        OracleDropIndexStatement result = new OracleDropIndexStatement();
        result.getIndexes().add((IndexSegment) visit(ctx.indexName()));
        return result;
    }

    @Override
    public ASTNode visitAlterSynonym(final AlterSynonymContext ctx) {
        return new OracleAlterSynonymStatement();
    }
    
    @Override
    public ASTNode visitAlterSession(final AlterSessionContext ctx) {
        return new OracleAlterSessionStatement();
    }
    
    @Override
    public ASTNode visitAlterDatabase(final AlterDatabaseContext ctx) {
        return new OracleAlterDatabaseStatement();
    }
    
    @Override
    public ASTNode visitAlterSystem(final AlterSystemContext ctx) {
        return new OracleAlterSystemStatement();
    }

    @Override
    public ASTNode visitAnalyze(final AnalyzeContext ctx) {
        OracleAnalyzeStatement result = new OracleAnalyzeStatement();
        if (null != ctx.tableName()) {
            result.setTable((SimpleTableSegment) visit(ctx.tableName()));
        }
        if (null != ctx.indexName()) {
            result.setIndex((IndexSegment) visit(ctx.indexName()));
        }
        return result;
    }

    @Override
    public ASTNode visitAssociateStatistics(final AssociateStatisticsContext ctx) {
        OracleAssociateStatisticsStatement result = new OracleAssociateStatisticsStatement();
        if (null != ctx.columnAssociation()) {
            for (TableNameContext each: ctx.columnAssociation().tableName()) {
                result.getTables().add((SimpleTableSegment) visit(each));
            }
            for (ColumnNameContext each: ctx.columnAssociation().columnName()) {
                result.getColumns().add((ColumnSegment) visit(each));
            }
        }
        if (null != ctx.functionAssociation()) {
            for (IndexNameContext each: ctx.functionAssociation().indexName()) {
                result.getIndexes().add((IndexSegment) visit(each));
            }
        }
        return result;
    }

    @Override
    public ASTNode visitDisassociateStatistics(final DisassociateStatisticsContext ctx) {
        OracleDisassociateStatisticsStatement result = new OracleDisassociateStatisticsStatement();
        if (null != ctx.tableName()) {
            for (TableNameContext each : ctx.tableName()) {
                result.getTables().add((SimpleTableSegment) visit(each));
            }
            for (ColumnNameContext each: ctx.columnName()) {
                result.getColumns().add((ColumnSegment) visit(each));
            }
        }
        if (null != ctx.indexName()) {
            for (IndexNameContext each: ctx.indexName()) {
                result.getIndexes().add((IndexSegment) visit(each));
            }
        }
        return result;
    }

    @Override
    public ASTNode visitAudit(final AuditContext ctx) {
        return new OracleAuditStatement();
    }
    
    @Override
    public ASTNode visitNoAudit(final NoAuditContext ctx) {
        return new OracleNoAuditStatement();
    }

    @Override
    public ASTNode visitComment(final CommentContext ctx) {
        OracleCommentStatement result = new OracleCommentStatement();
        if (null != ctx.tableName()) {
            result.setTable((SimpleTableSegment) visit(ctx.tableName()));
        }
        if (null != ctx.columnName()) {
            result.setColumn((ColumnSegment) visit(ctx.columnName()));
        }
        return result;
    }
}
