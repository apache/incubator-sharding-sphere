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

package org.apache.shardingsphere.orchestration.internal.keygen;

import lombok.SneakyThrows;
import org.apache.curator.test.TestingServer;
import org.apache.shardingsphere.core.strategy.keygen.LeafSegmentKeyGenerator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LeafSegmentKeyGeneratorTest {
    private LeafSegmentKeyGenerator leafSegmentKeyGenerator = new LeafSegmentKeyGenerator();

    private static TestingServer server;


    @Test
    public void assertGetProperties() {
        assertThat(leafSegmentKeyGenerator.getProperties().entrySet().size(), is(0));
    }

    @Test
    public void assertSetProperties() {
        Properties properties = new Properties();
        properties.setProperty("key1", "value1");
        leafSegmentKeyGenerator.setProperties(properties);
        assertThat(leafSegmentKeyGenerator.getProperties().get("key1"), is((Object) "value1"));
    }

    @Test
    public void assertGenerateKeyWithSingleThread(){
        Properties properties = new Properties();
        properties.setProperty("serverList","127.0.0.1:2181");
        properties.setProperty("initialValue","100001");
        properties.setProperty("step","3");
        leafSegmentKeyGenerator.setProperties(properties);
        String tableName="/test_table_name1";
        List<Comparable<?>> expected = Arrays.<Comparable<?>>asList(100001L,100002L,100003L,100004L,100005L,100006L,100007L,100008L,100009L,100010L);
        List<Comparable<?>> actual = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            actual.add(leafSegmentKeyGenerator.generateKey(tableName));
        }
        assertThat(actual, is(expected));
    }

    @Test
    @SneakyThrows
    public void assertGenerateKeyWithMultipleThreads() {
        int threadNumber = Runtime.getRuntime().availableProcessors() << 1;
        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);
        int taskNumber = threadNumber << 2;
        Properties properties = new Properties();
        properties.setProperty("serverList","127.0.0.1:2181");
        properties.setProperty("initialValue","100001");
        properties.setProperty("step","3");
        leafSegmentKeyGenerator.setProperties(properties);
        final String tableName="/test_table_name2";
        Set<Comparable<?>> actual = new HashSet<>();
        for (int i = 0; i < taskNumber; i++) {
            actual.add(executor.submit(new Callable<Comparable<?>>() {

                @Override
                public Comparable<?> call() {
                    return leafSegmentKeyGenerator.generateKey(tableName);
                }
            }).get());
        }
        assertThat(actual.size(), is(taskNumber));
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertSetStepFailureWhenNegative() throws Exception{
        Properties properties = new Properties();
        properties.setProperty("serverList","127.0.0.1:2181");
        properties.setProperty("initialValue","100001");
        properties.setProperty("step", String.valueOf(-1L));
        leafSegmentKeyGenerator.setProperties(properties);
        final String tableName="/test_table_name3";
        leafSegmentKeyGenerator.generateKey(tableName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertSetInitialValueFailureWhenNegative() {
        Properties properties = new Properties();
        properties.setProperty("serverList","127.0.0.1:2181");
        properties.setProperty("step","3");
        properties.setProperty("initialValue", String.valueOf(-1L));
        leafSegmentKeyGenerator.setProperties(properties);
        final String tableName="/test_table_name4";
        leafSegmentKeyGenerator.generateKey(tableName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertSetServerListFailureWhenPortIllegal() {
        Properties properties = new Properties();
        properties.setProperty("initialValue","100001");
        properties.setProperty("step","3");
        properties.setProperty("serverList", "192.168.123:90999");
        leafSegmentKeyGenerator.setProperties(properties);
        final String tableName="/test_table_name5";
        leafSegmentKeyGenerator.generateKey(tableName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertSetServerListFailureWhenIpIllegal() {
        Properties properties = new Properties();
        properties.setProperty("initialValue","100001");
        properties.setProperty("step","3");
        properties.setProperty("serverList", "267.168.123:8088");
        leafSegmentKeyGenerator.setProperties(properties);
        final String tableName="/test_table_name6";
        leafSegmentKeyGenerator.generateKey(tableName);
    }

    @BeforeClass
    @SneakyThrows
    public static void startServer(){
        server = new TestingServer(2181,true);
    }

    @AfterClass
    @SneakyThrows
    public static void closeServer(){
        server.close();
    }
}
