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

package org.apache.shardingsphere.core.rewrite.masterslave;

import org.apache.shardingsphere.core.metadata.table.TableMetas;
import org.apache.shardingsphere.core.optimize.statement.SQLStatementContext;
import org.apache.shardingsphere.core.optimize.statement.impl.InsertSQLStatementContext;
import org.apache.shardingsphere.core.rewrite.parameter.builder.ParameterBuilder;
import org.apache.shardingsphere.core.rewrite.parameter.builder.impl.GroupedParameterBuilder;
import org.apache.shardingsphere.core.rewrite.parameter.builder.impl.StandardParameterBuilder;
import org.apache.shardingsphere.core.rewrite.sql.SQLBuilder;
import org.apache.shardingsphere.core.rewrite.sql.token.SQLTokenGenerators;
import org.apache.shardingsphere.core.rewrite.sql.token.builder.BaseTokenGeneratorBuilder;
import org.apache.shardingsphere.core.rewrite.sql.token.builder.EncryptTokenGenerateBuilder;
import org.apache.shardingsphere.core.rewrite.sql.token.pojo.SQLToken;
import org.apache.shardingsphere.core.route.SQLUnit;
import org.apache.shardingsphere.core.rule.BaseRule;
import org.apache.shardingsphere.core.rule.EncryptRule;
import org.apache.shardingsphere.core.rule.MasterSlaveRule;

import java.util.Collections;
import java.util.List;

/**
 * Rewrite engine for master-slave.
 * 
 * @author panjuan
 * @author zhangliang
 */
public final class MasterSlaveRewriteEngine {
    
    private final BaseRule baseRule;
    
    private final SQLStatementContext sqlStatementContext;
    
    private final List<SQLToken> sqlTokens;
    
    private final SQLBuilder sqlBuilder;
    
    private final ParameterBuilder parameterBuilder;
    
    public MasterSlaveRewriteEngine(final MasterSlaveRule masterSlaveRule, final SQLStatementContext sqlStatementContext, final String sql) {
        baseRule = masterSlaveRule;
        this.sqlStatementContext = sqlStatementContext;
        parameterBuilder = sqlStatementContext instanceof InsertSQLStatementContext
                ? new GroupedParameterBuilder(((InsertSQLStatementContext) sqlStatementContext).getGroupedParameters()) : new StandardParameterBuilder(Collections.emptyList());
        sqlTokens = createSQLTokensForEncrypt(null, Collections.emptyList(), false);
        sqlBuilder = new SQLBuilder(sql, sqlTokens);
    }
    
    private List<SQLToken> createSQLTokensForEncrypt(final TableMetas tableMetas, final List<Object> parameters, final boolean isQueryWithCipherColumn) {
        SQLTokenGenerators sqlTokenGenerators = new SQLTokenGenerators();
        sqlTokenGenerators.addAll(new BaseTokenGeneratorBuilder().getSQLTokenGenerators());
        sqlTokenGenerators.addAll(new EncryptTokenGenerateBuilder((EncryptRule) baseRule, isQueryWithCipherColumn).getSQLTokenGenerators());
        return sqlTokenGenerators.generateSQLTokens(sqlStatementContext, parameters, tableMetas, true);
    }
    
    /**
     * Generate SQL.
     * 
     * @return SQL unit
     */
    public SQLUnit generateSQL() {
        return new SQLUnit(sqlBuilder.toSQL(), parameterBuilder.getParameters());
    }
}
