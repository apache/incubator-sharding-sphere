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

package org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.AbstractExpectedSQLSegment;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.column.ExpectedColumn;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.complex.ExpectedCommonExpression;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.simple.ExpectedLiteralExpression;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.simple.ExpectedParameterMarkerExpression;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.expr.simple.ExpectedSubquery;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.projection.impl.aggregation.ExpectedAggregationProjection;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.projection.impl.expression.ExpectedExpressionProjection;

import javax.xml.bind.annotation.XmlElement;

/**
 * Expected expression.
 */
@Getter
@Setter
public final class ExpectedExpression extends AbstractExpectedSQLSegment {
    
    @XmlElement(name = "between-expression")
    private ExpectedBetweenExpression betweenExpression;
    
    @XmlElement(name = "binary-operation-expression")
    private ExpectedBinaryOperationExpression binaryOperationExpression;
    
    @XmlElement(name = "column")
    private ExpectedColumn column;
    
    @XmlElement(name = "common-expression")
    private ExpectedCommonExpression commonExpression;
    
    @XmlElement(name = "exists-subquery")
    private ExpectedExistsSubquery existsSubquery;
    
    @XmlElement(name = "expression-projection")
    private ExpectedExpressionProjection expressionProjection;
    
    @XmlElement(name = "in-expression")
    private ExpectedInExpression inExpression;
    
    @XmlElement(name = "list-expression")
    private ExpectedListExpression listExpression;
    
    @XmlElement(name = "literal-expression")
    private ExpectedLiteralExpression literalExpression;
    
    @XmlElement(name = "not-expression")
    private ExpectedNotExpression notExpression;
    
    @XmlElement(name = "parameter-marker-expression")
    private ExpectedParameterMarkerExpression parameterMarkerExpression;
    
    @XmlElement(name = "subquery")
    private ExpectedSubquery subquery;
    
    @XmlElement(name = "aggregation-projection")
    private ExpectedAggregationProjection aggregationProjection;
}
