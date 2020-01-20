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

package org.apache.shardingsphere.sql.parser.core.parser;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.shardingsphere.sql.parser.api.SQLParser;
import org.apache.shardingsphere.sql.parser.core.extractor.util.ExtractorUtils;
import org.apache.shardingsphere.sql.parser.core.extractor.util.RuleName;
import org.apache.shardingsphere.sql.parser.core.rule.registry.ParseRuleRegistry;
import org.apache.shardingsphere.sql.parser.core.rule.registry.statement.SQLStatementRule;
import org.apache.shardingsphere.sql.parser.exception.SQLParsingException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * SQL parser engine.
 *
 * @author zhangliang
 */
@RequiredArgsConstructor
public final class SQLParserEngine {
    
    private final ParseRuleRegistry parseRuleRegistry;
    
    private final String databaseTypeName;
    
    private final String sql;
    
    /**
     * Parse SQL to abstract syntax tree.
     *
     * @return abstract syntax tree of SQL
     */
    public SQLAST parse() {
        SQLParser sqlParser = SQLParserFactory.newInstance(databaseTypeName, sql);
        ParseTree parseTree;
        try {
            ((Parser) sqlParser).setErrorHandler(new BailErrorStrategy());
            ((Parser) sqlParser).getInterpreter().setPredictionMode(PredictionMode.SLL);
            parseTree = sqlParser.execute().getChild(0);
        } catch (ParseCancellationException ex) {
            ((Parser) sqlParser).reset();
            ((Parser) sqlParser).setErrorHandler(new DefaultErrorStrategy());
            ((Parser) sqlParser).getInterpreter().setPredictionMode(PredictionMode.LL);
            parseTree = sqlParser.execute().getChild(0);
        }
        if (parseTree instanceof ErrorNode) {
            throw new SQLParsingException(String.format("Unsupported SQL of `%s`", sql));
        }
        SQLStatementRule rule = parseRuleRegistry.getSQLStatementRule(databaseTypeName, parseTree.getClass().getSimpleName());
        if (null == rule) {
            throw new SQLParsingException(String.format("Unsupported SQL of `%s`", sql));
        }
        return new SQLAST((ParserRuleContext) parseTree, getParameterMarkerIndexes((ParserRuleContext) parseTree), rule);
    }
    
    private Map<ParserRuleContext, Integer> getParameterMarkerIndexes(final ParserRuleContext rootNode) {
        Collection<ParserRuleContext> placeholderNodes = ExtractorUtils.getAllDescendantNodes(rootNode, RuleName.PARAMETER_MARKER);
        Map<ParserRuleContext, Integer> result = new HashMap<>(placeholderNodes.size(), 1);
        int index = 0;
        for (ParserRuleContext each : placeholderNodes) {
            result.put(each, index++);
        }
        return result;
    }
}
