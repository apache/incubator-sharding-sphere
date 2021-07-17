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

package org.apache.shardingsphere.infra.binder.segment.select.projection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.shardingsphere.infra.binder.segment.select.projection.impl.AggregationDistinctProjection;
import org.apache.shardingsphere.infra.binder.segment.select.projection.impl.AggregationProjection;
import org.apache.shardingsphere.infra.binder.segment.select.projection.impl.DerivedProjection;
import org.apache.shardingsphere.infra.binder.segment.select.projection.impl.ShorthandProjection;
import org.apache.shardingsphere.sql.parser.sql.common.util.SQLUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Projections context.
 */
@RequiredArgsConstructor
@Getter
@ToString
public final class ProjectionsContext {
    
    private final int startIndex;
    
    private final int stopIndex;
    
    private final boolean distinctRow;
    
    private final Collection<Projection> projections;
    
    /**
     * Judge is unqualified shorthand projection or not.
     * 
     * @return is unqualified shorthand projection or not
     */
    public boolean isUnqualifiedShorthandProjection() {
        if (1 != projections.size()) {
            return false;
        }
        Projection projection = projections.iterator().next();
        return projection instanceof ShorthandProjection && !((ShorthandProjection) projection).getOwner().isPresent();
    }
    
    /**
     * Find alias.
     * 
     * @param projectionName projection name
     * @return projection alias
     */
    public Optional<String> findAlias(final String projectionName) {
        for (Projection each : projections) {
            if (projectionName.equalsIgnoreCase(SQLUtil.getExactlyValue(each.getExpression()))) {
                return each.getAlias();
            }
        }
        return Optional.empty();
    }
    
    /**
     * Find projection index.
     * 
     * @param projectionName projection name
     * @return projection index
     */
    public Optional<Integer> findProjectionIndex(final String projectionName) {
        int result = 1;
        for (Projection each : projections) {
            if (projectionName.equalsIgnoreCase(SQLUtil.getExactlyValue(each.getExpression()))) {
                return Optional.of(result);
            }
            result++;
        }
        return Optional.empty();
    }
    
    /**
     * Get aggregation projections.
     * 
     * @return aggregation projections
     */
    public List<AggregationProjection> getAggregationProjections() {
        List<AggregationProjection> result = new LinkedList<>();
        for (Projection each : projections) {
            if (each instanceof AggregationProjection) {
                AggregationProjection aggregationProjection = (AggregationProjection) each;
                result.add(aggregationProjection);
                result.addAll(aggregationProjection.getDerivedAggregationProjections());
            }
        }
        return result;
    }
    
    /**
     * Get aggregation distinct projections.
     * 
     * @return aggregation distinct projections
     */
    public List<AggregationDistinctProjection> getAggregationDistinctProjections() {
        List<AggregationDistinctProjection> result = new LinkedList<>();
        for (Projection each : projections) {
            if (each instanceof AggregationDistinctProjection) {
                result.add((AggregationDistinctProjection) each);
            }
        }
        return result;
    }
    
    /**
     * Get expand projections with shorthand projections.
     * 
     * @return expand projections
     */
    public List<Projection> getExpandProjections() {
        List<Projection> result = new ArrayList<>();
        for (Projection each : projections) {
            if (each instanceof ShorthandProjection) {
                result.addAll(((ShorthandProjection) each).getActualColumns());
            } else if (!(each instanceof DerivedProjection)) {
                result.add(each);
            }
        }
        return result;
    }
}
