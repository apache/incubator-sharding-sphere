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

package org.apache.shardingsphere.infra.rewrite.sql.impl;

import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.infra.rewrite.context.SQLRewriteContext;
import org.apache.shardingsphere.infra.rewrite.sql.SQLBuilder;
import org.apache.shardingsphere.infra.rewrite.sql.token.pojo.SQLToken;
import org.apache.shardingsphere.infra.rewrite.sql.token.pojo.Substitutable;

import java.util.Collections;

/**
 * Abstract SQL builder.
 */
@RequiredArgsConstructor
public abstract class AbstractSQLBuilder implements SQLBuilder {
    
    private final SQLRewriteContext context;
    
    @Override
    public final String toSQL() {
        if (context.getSqlTokens().isEmpty()) {
            return context.getSql();
        }
        Collections.sort(context.getSqlTokens());

        StringBuilder result = new StringBuilder();
        result.append(context.getSql(), 0, context.getSqlTokens().get(0).getStartIndex());
        int size = context.getSqlTokens().size();
        for (int index = 0; index < size; index++) {
            if (index < size - 1 && isSubstitutableNeedIgnore(context.getSqlTokens().get(index), context.getSqlTokens().get(index + 1))) {
                continue;
            }
            SQLToken each = context.getSqlTokens().get(index);
            result.append(getSQLTokenText(each));
            result.append(getConjunctionText(each));
        }

        return result.toString();
    }
    
    protected abstract String getSQLTokenText(SQLToken sqlToken);
    
    private String getConjunctionText(final SQLToken sqlToken) {
        return context.getSql().substring(getStartIndex(sqlToken), getStopIndex(sqlToken));
    }
    
    private int getStartIndex(final SQLToken sqlToken) {
        int startIndex = sqlToken instanceof Substitutable ? ((Substitutable) sqlToken).getStopIndex() + 1 : sqlToken.getStartIndex();
        return Math.min(startIndex, context.getSql().length());
    }
    
    private int getStopIndex(final SQLToken sqlToken) {
        int currentSQLTokenIndex = context.getSqlTokens().indexOf(sqlToken);
        if (context.getSqlTokens().size() - 1 == currentSQLTokenIndex) {
            return context.getSql().length();
        }
        SQLToken nextSqlToken = context.getSqlTokens().get(currentSQLTokenIndex + 1);
        if (isAllOfSubstitutable(sqlToken, nextSqlToken)) {
            int currentSQLTokenStopIndex = ((Substitutable) sqlToken).getStopIndex() + 1;
            return Math.max(currentSQLTokenStopIndex, nextSqlToken.getStartIndex());
        }
        return context.getSqlTokens().get(currentSQLTokenIndex + 1).getStartIndex();
    }

    private boolean isSubstitutableNeedIgnore(final SQLToken currentSqlToken, final SQLToken nextSqlToken) {
        if (isAllOfSubstitutable(currentSqlToken, nextSqlToken) && currentSqlToken.getStartIndex() == nextSqlToken.getStartIndex()
            && ((Substitutable) currentSqlToken).getStopIndex() != ((Substitutable) nextSqlToken).getStopIndex()) {
            return true;
        }
        return false;
    }

    private boolean isAllOfSubstitutable(final SQLToken... sqlTokens) {
        for (SQLToken sqlToken : sqlTokens) {
            if (!(sqlToken instanceof Substitutable)) {
                return false;
            }
        }
        return true;
    }
}
