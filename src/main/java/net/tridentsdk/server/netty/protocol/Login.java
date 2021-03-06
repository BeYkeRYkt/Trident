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
package net.tridentsdk.server.netty.protocol;

import net.tridentsdk.docs.AccessNoDoc;
import net.tridentsdk.server.packets.login.*;

@AccessNoDoc
class Login extends PacketManager {
    Login() {
        this.inPackets.put(0x00, PacketLoginInStart.class);
        this.inPackets.put(0x01, PacketLoginInEncryptionResponse.class);

        this.outPackets.put(0x00, PacketLoginOutDisconnect.class);
        this.outPackets.put(0x01, PacketLoginOutEncryptionRequest.class);
        this.outPackets.put(0x02, PacketLoginOutSuccess.class);
        this.outPackets.put(0x03, PacketLoginOutSetCompression.class);
    }
}