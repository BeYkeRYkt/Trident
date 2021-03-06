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
package net.tridentsdk.server.netty.packet;

import io.netty.buffer.ByteBuf;
import net.tridentsdk.server.netty.ClientConnection;

/**
 * Data bearing abstraction that represents a piece of information to communicate between server and client
 *
 * @author The TridentSDK Team
 */
public interface Packet {
    /**
     * Sets the fields of the packet from the data serialized into the buffer
     *
     * @param buf the buffer storing the serialized packet data
     * @return the this instance of the packet
     */
    Packet decode(ByteBuf buf);

    /**
     * Serialized the data held by this packet into a buffer
     *
     * @param buf the buffer to toPacket to
     */
    void encode(ByteBuf buf);

    /**
     * Handles the packet after receiving it from a connection
     *
     * @param connection The connection that sent the packet
     */
    void handleReceived(ClientConnection connection);

    int getId();

    PacketType getType();
}
