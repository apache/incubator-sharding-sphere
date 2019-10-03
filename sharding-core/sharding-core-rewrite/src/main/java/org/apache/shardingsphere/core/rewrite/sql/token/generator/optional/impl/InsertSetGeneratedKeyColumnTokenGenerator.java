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

package org.apache.shardingsphere.core.rewrite.sql.token.generator.optional.impl;

import com.google.common.base.Preconditions;
import lombok.Setter;
import org.apache.shardingsphere.core.optimize.statement.SQLStatementContext;
import org.apache.shardingsphere.core.optimize.statement.impl.InsertSQLStatementContext;
import org.apache.shardingsphere.core.parse.sql.segment.dml.assignment.AssignmentSegment;
import org.apache.shardingsphere.core.parse.sql.segment.dml.expr.ExpressionSegment;
import org.apache.shardingsphere.core.parse.sql.statement.dml.InsertStatement;
import org.apache.shardingsphere.core.rewrite.sql.token.generator.SQLRouteResultAware;
import org.apache.shardingsphere.core.rewrite.sql.token.generator.optional.OptionalSQLTokenGenerator;
import org.apache.shardingsphere.core.rewrite.sql.token.pojo.impl.InsertSetGeneratedKeyColumnToken;
import org.apache.shardingsphere.core.route.SQLRouteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Insert set generated key token generator.
 *
 * @author panjuan
 */
@Setter
public final class InsertSetGeneratedKeyColumnTokenGenerator implements OptionalSQLTokenGenerator, SQLRouteResultAware {
    
    private SQLRouteResult sqlRouteResult;
    
    @Override
    public boolean isGenerateSQLToken(final SQLStatementContext sqlStatementContext) {
        return sqlStatementContext instanceof InsertSQLStatementContext && ((InsertStatement) sqlStatementContext.getSqlStatement()).getSetAssignment().isPresent()
                && sqlRouteResult.getGeneratedKey().isPresent() && sqlRouteResult.getGeneratedKey().get().isGenerated();
    }
    
    @Override
    public InsertSetGeneratedKeyColumnToken generateSQLToken(final SQLStatementContext sqlStatementContext) {
        Preconditions.checkState(((InsertStatement) sqlStatementContext.getSqlStatement()).getSetAssignment().isPresent());
        Preconditions.checkState(sqlRouteResult.getGeneratedKey().isPresent());
        String generatedKeyColumn = sqlRouteResult.getGeneratedKey().get().getColumnName();
        List<AssignmentSegment> assignments = new ArrayList<>(((InsertStatement) sqlStatementContext.getSqlStatement()).getSetAssignment().get().getAssignments());
        int index = ((InsertSQLStatementContext) sqlStatementContext).getColumnNames().contains(generatedKeyColumn)
                ? ((InsertSQLStatementContext) sqlStatementContext).getColumnNames().indexOf(generatedKeyColumn) : ((InsertSQLStatementContext) sqlStatementContext).getColumnNames().size();
        ExpressionSegment expressionSegment = ((InsertSQLStatementContext) sqlStatementContext).getInsertValueContexts().get(0).getValueExpressions().get(index);
        return new InsertSetGeneratedKeyColumnToken(assignments.get(assignments.size() - 1).getStopIndex() + 1, generatedKeyColumn, expressionSegment);
    }
    
}
