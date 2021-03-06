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
package net.tridentsdk.server.player;

import io.netty.util.internal.ConcurrentSet;
import net.tridentsdk.concurrent.TaskExecutor;
import net.tridentsdk.entity.living.Player;
import net.tridentsdk.factory.Factories;
import net.tridentsdk.meta.nbt.CompoundTag;
import net.tridentsdk.server.TridentServer;
import net.tridentsdk.server.data.Slot;
import net.tridentsdk.server.netty.ClientConnection;
import net.tridentsdk.server.netty.packet.Packet;
import net.tridentsdk.server.packets.play.out.*;
import net.tridentsdk.server.world.TridentChunk;
import net.tridentsdk.server.world.TridentWorld;
import net.tridentsdk.util.TridentLogger;
import net.tridentsdk.world.LevelType;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class TridentPlayer extends OfflinePlayer {
    private static final Set<TridentPlayer> players = new ConcurrentSet<>();

    private final PlayerConnection connection;
    private final TaskExecutor executor = Factories.threads().playerThread(this);
    private volatile Locale locale;

    public TridentPlayer(CompoundTag tag, TridentWorld world, ClientConnection connection) {
        super(tag, world);

        this.connection = PlayerConnection.createPlayerConnection(connection, this);
    }

    public static void sendAll(Packet packet) {
        for (TridentPlayer p : players) {
            p.connection.sendPacket(packet);
        }
    }

    public static Player spawnPlayer(ClientConnection connection, UUID id) {
        CompoundTag offlinePlayer = (OfflinePlayer.getOfflinePlayer(id) == null) ? null :
                OfflinePlayer.getOfflinePlayer(id).toNbt();

        if(offlinePlayer == null) {
            offlinePlayer = OfflinePlayer.generatePlayer(id);
        }

        final TridentPlayer p = new TridentPlayer(offlinePlayer,
                TridentServer.WORLD, connection);

        p.connection.sendPacket(new PacketPlayOutJoinGame().set("entityId", p.getId())
                .set("gamemode", p.getGameMode())
                .set("dimension", ((TridentWorld) p.getWorld()).getDimesion())
                .set("difficulty", p.getWorld().getDifficulty())
                .set("maxPlayers", (short) 10)
                .set("levelType",
                        LevelType.DEFAULT));

        p.connection.sendPacket(new PacketPlayOutSpawnPosition().set("location", p.getSpawnLocation()));
        p.connection.sendPacket(p.abilities.toPacket());
        p.connection.sendPacket(new PacketPlayOutPlayerCompleteMove().set("location", p.getLocation())
                .set("flags", (byte) 0));

        players.add(p);

        p.executor.addTask(new Runnable() {
            @Override
            public void run() {
                p.sendChunks(7);
            }
        });

        return p;
    }

    public static TridentPlayer getPlayer(UUID id) {
        for (TridentPlayer player : players) {
            if (player.getUniqueId().equals(id)) {
                return player;
            }
        }

        return null;
    }

    public static Set<TridentPlayer> getPlayers() {
        return players;
    }

    @Override
    public void tick() {
        this.executor.addTask(new Runnable() {
            @Override
            public void run() {
                TridentPlayer.super.tick();
                long keepAlive = ticksExisted.get() - connection.getKeepAliveSent();

                if (TridentPlayer.this.connection.getKeepAliveId() == -1 && keepAlive >= 300) {
                    // send Keep Alive packet if not sent already
                    PacketPlayOutKeepAlive packet = new PacketPlayOutKeepAlive();

                    connection.sendPacket(packet);
                    connection.setKeepAliveId(packet.getKeepAliveId(), ticksExisted.get());
                } else if (keepAlive >= 600L) {
                    // kick the player for not responding to the keep alive within 30 seconds/600 ticks
                    kickPlayer("Timed out!");
                }

                ticksExisted.incrementAndGet();
            }
        });
    }

    /*
     * @NotJavaDoc
     * TODO: Create Message API and utilize it
     */
    public void kickPlayer(final String reason) {
        this.executor.addTask(new Runnable() {
            @Override
            public void run() {
                TridentPlayer.this.connection.sendPacket(new PacketPlayOutDisconnect().set("reason", reason));
            }
        });
    }

    public PlayerConnection getConnection() {
        return this.connection;
    }

    public void setSlot(final short slot) {
        this.executor.addTask(new Runnable() {
            @Override
            public void run() {
                if ((int) slot > 8 || (int) slot < 0) {
                    TridentLogger.error(new IllegalArgumentException("Slot must be within the ranges of 0-8"));
                }

                TridentPlayer.super.selectedSlot = slot;
            }
        });
    }

    @Override
    public void sendRaw(final String... messages) {
        // TODO: Verify proper implementation
        this.executor.addTask(new Runnable() {
            @Override
            public void run() {
                for (String message : messages) {
                    if (message != null) {
                        TridentPlayer.this.connection.sendPacket(new PacketPlayOutChatMessage().set("jsonMessage",
                                message)
                                .set("position", PacketPlayOutChatMessage.ChatPosition.CHAT));
                    }
                }
            }
        });
    }

    private void sendChunks(int viewDistance) {
        int centX = ((int) Math.floor(loc.getX())) >> 4;
        int centZ = ((int) Math.floor(loc.getZ())) >> 4;

        for (int x = (centX - viewDistance); x <= (centX + viewDistance); x += 1) {
            for (int z = (centZ - viewDistance); z <= (centZ + viewDistance); z += 1) {
                connection.sendPacket(((TridentChunk) getWorld().getChunkAt(x, z, true)).toPacket());
            }
        }
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
