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

package org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dml;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.limit.ExpectedLimitClause;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.orderby.ExpectedOrderByClause;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.output.ExpectedOutputClause;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.table.ExpectedSimpleTable;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.table.ExpectedSubqueryTable;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.where.ExpectedWhereClause;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.segment.impl.with.ExpectedWithClause;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.SQLParserTestCase;

import javax.xml.bind.annotation.XmlElement;
import java.util.LinkedList;
import java.util.List;

/**
 * Delete statement test case.
 */
@Getter
@Setter
public final class DeleteStatementTestCase extends SQLParserTestCase {
    
    @XmlElement(name = "with")
    private ExpectedWithClause withClause;
    
    @XmlElement(name = "table")
    private final List<ExpectedSimpleTable> tables = new LinkedList<>();
    
    @XmlElement(name = "output")
    private ExpectedOutputClause outputClause;
    
    @XmlElement(name = "where")
    private ExpectedWhereClause whereClause;
    
    @XmlElement(name = "order-by")
    private ExpectedOrderByClause orderByClause;
    
    @XmlElement(name = "limit")
    private ExpectedLimitClause limitClause;
    
    @XmlElement(name = "subquery-table")
    private ExpectedSubqueryTable subqueryTable;
}
