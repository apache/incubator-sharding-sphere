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

package org.apache.shardingsphere.sql.parser.sql.common.segment.dml.order.item;

import lombok.Getter;
import lombok.ToString;
import org.apache.shardingsphere.sql.parser.sql.common.constant.OrderDirection;

/**
 * Order by item segment for expression.
 */
@Getter
@ToString(callSuper = true)
public final class ExpressionOrderByItemSegment extends TextOrderByItemSegment {
    
    private final String expression;
    
    public ExpressionOrderByItemSegment(final int startIndex, final int stopIndex, final String expression, final OrderDirection orderDirection, final OrderDirection nullOrderDirection) {
        super(startIndex, stopIndex, orderDirection, nullOrderDirection);
        this.expression = expression;
    }
    
    public ExpressionOrderByItemSegment(final int startIndex, final int stopIndex, final String expression, final OrderDirection orderDirection) {
        super(startIndex, stopIndex, orderDirection, OrderDirection.ASC);
        this.expression = expression;
    }
    
    public ExpressionOrderByItemSegment(final int startIndex, final int stopIndex, final String expression) {
        super(startIndex, stopIndex, OrderDirection.ASC, OrderDirection.ASC);
        this.expression = expression;
    }
    
    @Override
    public String getText() {
        return expression;
    }
}
