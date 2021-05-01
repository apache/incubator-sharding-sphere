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

/**
 * Binary protocol value for int4 for PostgreSQL.
 */
public final class PostgreSQLInt4BinaryProtocolValue implements PostgreSQLBinaryProtocolValue {
    
    @Override
    public int getColumnLength(final Object value) {
        return 4;
    }
    
    @Override
    public Object read(final PostgreSQLPacketPayload payload, final int parameterValueLength) {
        switch (parameterValueLength) {
            case 1:
                return payload.readInt1();
            case 2:
                return payload.readInt2();
            case 4:
                return payload.readInt4();
            default:
                throw new UnsupportedOperationException(String.format("paramValueLength %s", parameterValueLength));
        }
    }
    
    @Override
    public void write(final PostgreSQLPacketPayload payload, final Object value) {
        payload.writeInt4((Integer) value);
    }
}
