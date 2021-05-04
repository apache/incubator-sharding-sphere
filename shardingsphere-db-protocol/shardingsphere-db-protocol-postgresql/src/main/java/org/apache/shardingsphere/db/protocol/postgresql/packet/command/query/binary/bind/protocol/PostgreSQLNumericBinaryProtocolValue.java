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

package org.apache.shardingsphere.db.protocol.postgresql.packet.command.query.binary.bind.protocol;

import org.apache.shardingsphere.db.protocol.postgresql.payload.PostgreSQLPacketPayload;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

/**
 * Binary protocol value for numeric for PostgreSQL.
 */
public final class PostgreSQLNumericBinaryProtocolValue implements PostgreSQLBinaryProtocolValue {
    
    @Override
    public int getColumnLength(final Object value) {
        return null == value ? 0 : value.toString().getBytes(StandardCharsets.UTF_8).length;
    }
    
    @Override
    public Object read(final PostgreSQLPacketPayload payload, final int parameterValueLength) {
        byte[] bytes = new byte[parameterValueLength];
        payload.getByteBuf().readBytes(bytes);
        return new BigDecimal(new String(bytes));
    }
    
    @Override
    public void write(final PostgreSQLPacketPayload payload, final Object value) {
        if (null == value) {
            return;
        }
        payload.writeBytes(value.toString().getBytes(StandardCharsets.UTF_8));
    }
}
