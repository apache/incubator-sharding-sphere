/**
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.dangdang.ddframe.rdb.sharding.executor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.codahale.metrics.Timer.Context;
import com.dangdang.ddframe.rdb.sharding.executor.event.DMLExecutionEvent;
import com.dangdang.ddframe.rdb.sharding.executor.event.DMLExecutionEventBus;
import com.dangdang.ddframe.rdb.sharding.executor.event.EventExecutionType;
import com.dangdang.ddframe.rdb.sharding.executor.wapper.PreparedStatementExecutorWapper;
import com.dangdang.ddframe.rdb.sharding.metrics.MetricsContext;

import lombok.RequiredArgsConstructor;

/**
 * 多线程执行预编译语句对象请求的执行器.
 * 
 * @author zhangliang
 */
@RequiredArgsConstructor
public final class PreparedStatementExecutor {
    
    private final List<PreparedStatementExecutorWapper> preparedStatementExecutorWappers;
    
    /**
     * 执行SQL查询.
     * 
     * @return 结果集列表
     * @throws SQLException SQL异常
     */
    public List<ResultSet> executeQuery() throws SQLException {
        Context context = MetricsContext.start("ShardingPreparedStatement-executeQuery");
        List<ResultSet> result;
        if (1 == preparedStatementExecutorWappers.size()) {
            result =  Arrays.asList(preparedStatementExecutorWappers.iterator().next().getPreparedStatement().executeQuery());
            MetricsContext.stop(context);
            return result;
        }
        result = ExecutorEngine.execute(preparedStatementExecutorWappers, new ExecuteUnit<PreparedStatementExecutorWapper, ResultSet>() {
            
            @Override
            public ResultSet execute(final PreparedStatementExecutorWapper input) throws Exception {
                return input.getPreparedStatement().executeQuery();
            }
        });
        MetricsContext.stop(context);
        return result;
    }
    
    /**
     * 执行SQL更新.
     * 
     * @return 更新数量
     * @throws SQLException SQL异常
     */
    public int executeUpdate() throws SQLException {
        Context context = MetricsContext.start("ShardingPreparedStatement-executeUpdate");
        postDMLExecutionEvents();
        int result;
        if (1 == preparedStatementExecutorWappers.size()) {
            PreparedStatementExecutorWapper preparedStatementExecutorWapper = preparedStatementExecutorWappers.iterator().next();
            try {
                result =  preparedStatementExecutorWapper.getPreparedStatement().executeUpdate();
            } catch (final SQLException ex) {
                postDMLExecutionEventsAfterExecution(preparedStatementExecutorWapper, EventExecutionType.EXECUTE_FAILURE);
                throw ex;
            } finally {
                MetricsContext.stop(context);
            }
            postDMLExecutionEventsAfterExecution(preparedStatementExecutorWapper, EventExecutionType.EXECUTE_SUCCESS);
            return result;
        }
        result = ExecutorEngine.execute(preparedStatementExecutorWappers, new ExecuteUnit<PreparedStatementExecutorWapper, Integer>() {
            
            @Override
            public Integer execute(final PreparedStatementExecutorWapper input) throws Exception {
                int result;
                try {
                    result = input.getPreparedStatement().executeUpdate();
                } catch (final SQLException ex) {
                    postDMLExecutionEventsAfterExecution(input, EventExecutionType.EXECUTE_FAILURE);
                    throw ex;
                }
                postDMLExecutionEventsAfterExecution(input, EventExecutionType.EXECUTE_SUCCESS);
                return result;
            }
        }, new MergeUnit<Integer, Integer>() {
            
            @Override
            public Integer merge(final List<Integer> results) {
                int result = 0;
                for (Integer each : results) {
                    result += each;
                }
                return result;
            }
        });
        MetricsContext.stop(context);
        return result;
    }
    
    /**
     * 执行SQL请求.
     * 
     * @return true表示执行DQL, false表示执行的DML
     * @throws SQLException SQL异常
     */
    public boolean execute() throws SQLException {
        Context context = MetricsContext.start("ShardingPreparedStatement-execute");
        postDMLExecutionEvents();
        if (1 == preparedStatementExecutorWappers.size()) {
            boolean result;
            PreparedStatementExecutorWapper preparedStatementExecutorWapper = preparedStatementExecutorWappers.iterator().next();
            try {
                result = preparedStatementExecutorWapper.getPreparedStatement().execute();
            } catch (final SQLException ex) {
                postDMLExecutionEventsAfterExecution(preparedStatementExecutorWapper, EventExecutionType.EXECUTE_FAILURE);
                throw ex;
            } finally {
                MetricsContext.stop(context);
            }
            postDMLExecutionEventsAfterExecution(preparedStatementExecutorWapper, EventExecutionType.EXECUTE_SUCCESS);
            return result;
        }
        List<Boolean> result = ExecutorEngine.execute(preparedStatementExecutorWappers, new ExecuteUnit<PreparedStatementExecutorWapper, Boolean>() {
            
            @Override
            public Boolean execute(final PreparedStatementExecutorWapper input) throws Exception {
                boolean result;
                try {
                    result = input.getPreparedStatement().execute();
                } catch (final SQLException ex) {
                    postDMLExecutionEventsAfterExecution(input, EventExecutionType.EXECUTE_FAILURE);
                    throw ex;
                }
                postDMLExecutionEventsAfterExecution(input, EventExecutionType.EXECUTE_SUCCESS);
                return result;
            }
        });
        MetricsContext.stop(context);
        return result.get(0);
    }
    
    private void postDMLExecutionEvents() {
        for (PreparedStatementExecutorWapper each : preparedStatementExecutorWappers) {
            if (each.getDMLExecutionEvent().isPresent()) {
                DMLExecutionEventBus.post(each.getDMLExecutionEvent().get());
            }
        }
    }
    
    private void postDMLExecutionEventsAfterExecution(final PreparedStatementExecutorWapper preparedStatementExecutorWapper, final EventExecutionType eventExecutionType) {
        if (preparedStatementExecutorWapper.getDMLExecutionEvent().isPresent()) {
            DMLExecutionEvent event = preparedStatementExecutorWapper.getDMLExecutionEvent().get();
            event.setEventExecutionType(eventExecutionType);
            DMLExecutionEventBus.post(event);
        }
    }
}
