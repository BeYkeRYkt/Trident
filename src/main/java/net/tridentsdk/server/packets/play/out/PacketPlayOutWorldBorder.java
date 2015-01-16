/*
 * Trident - A Multithreaded Server Alternative
 * Copyright 2014 The TridentSDK Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tridentsdk.server.packets.play.out;

import io.netty.buffer.ByteBuf;
import net.tridentsdk.server.netty.Codec;
import net.tridentsdk.server.netty.packet.OutPacket;

public class PacketPlayOutWorldBorder extends OutPacket {

    protected int action;
    protected Object[] values;

    @Override
    public int getId() {
        return 0x44;
    }

    public int getAction() {
        return this.action;
    }

    public Object[] getValues() {
        return this.values;
    }

    @Override
    public void encode(ByteBuf buf) {
        Codec.writeVarInt32(buf, this.action);

        for (Object o : this.values) {
            switch (o.getClass().getSimpleName()) {
                case "Double":
                    buf.writeDouble((Double) o);
                    break;

                case "Integer":
                    Codec.writeVarInt32(buf, (Integer) o);
                    break;

                case "Long":
                    Codec.writeVarInt64(buf, (Long) o);
                    break;

                default:
                    // ignore bad developers
                    break;
            }
        }
    }
}
