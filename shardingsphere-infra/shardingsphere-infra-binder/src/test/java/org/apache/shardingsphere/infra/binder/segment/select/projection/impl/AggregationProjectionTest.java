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

package org.apache.shardingsphere.infra.binder.segment.select.projection.impl;

import org.apache.shardingsphere.sql.parser.sql.common.constant.AggregationType;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public final class AggregationProjectionTest {
    private final AggregationType aggregationType = AggregationType.COUNT;

    private final String innerExpression = "( A.\"DIRECTION\" )";

    private final String alias = "AVG_DERIVED_COUNT_0";

    private final AggregationProjection aggregationProjection1 = new AggregationProjection(aggregationType, innerExpression, alias);

    private final AggregationProjection aggregationProjection2 = new AggregationProjection(aggregationType, innerExpression, null);

    @Test
    public void assertGetExpression() {
        assertThat(aggregationProjection1.getExpression(), is(aggregationType + innerExpression));
    }

    @Test
    public void assertGetAlias() {
        Optional<String> actual = aggregationProjection1.getAlias();
        assertTrue(actual.isPresent());
        assertThat(actual.get(), is(alias));
    }

    @Test
    public void assertGetColumnLabel() {
        assertThat(aggregationProjection1.getColumnLabel(), is(alias));
    }

    @Test
    public void assertGetColumnLabelWithoutAlias() {
        assertThat(aggregationProjection2.getColumnLabel(), is(aggregationType + innerExpression));
    }
}
