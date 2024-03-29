package org.knalpot.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.knalpot.server.actors.Actor;
import org.knalpot.server.actors.State;
import org.knalpot.server.general.PacketAddActor;
import org.knalpot.server.general.PacketAddRoom;
import org.knalpot.server.general.PacketRemoveActor;
import org.knalpot.server.general.PacketType;
import org.knalpot.server.general.PacketUpdateDirection;
import org.knalpot.server.general.PacketUpdateHealth;
import org.knalpot.server.general.PacketUpdatePosition;
import org.knalpot.server.general.PacketUpdateState;
import org.knalpot.server.general.SpawnChest;
import org.knalpot.server.general.SpawnEnemyMessage;

public class ServerFoundation extends Listener {

    private static Server server;
    private static Map<Integer, Game> gameSessions = new HashMap<>();
    private static final int port = 8084;

    public static void main(String[] args) throws IOException {
        server = new Server();

        server.start();
        server.getKryo().register(PacketAddActor.class);
        server.getKryo().register(PacketAddRoom.class);
        server.getKryo().register(PacketRemoveActor.class);
        server.getKryo().register(PacketUpdatePosition.class);
        server.getKryo().register(PacketUpdateDirection.class);
        server.getKryo().register(PacketUpdateState.class);
        server.getKryo().register(PacketType.class, 3);
        server.getKryo().register(State.class);
        server.getKryo().register(PacketUpdateHealth.class, 2);
        server.getKryo().register(SpawnEnemyMessage.class);
        server.getKryo().register(SpawnChest.class, 5);
        try {
            server.bind(port, port);
            server.start();
            System.out.print("Server started on port: " + port +
                    " | start time: " + LocalDateTime.now());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.addListener(new ServerFoundation());
    }

    public void connected(Connection connection) {
        Optional<Integer> key = gameSessions.keySet().stream()
            .filter(k -> gameSessions.get(k).getPlayers().size() < 2)
            .findFirst();

        System.out.println(connection.getID());
        if (key.isEmpty()) {
            initializeGameSession(connection);
            System.out.println("Initialized new session");
        } else {
            addToSession(connection, key.get());
            System.out.println("Added to the session");
        }
    }

    private void addToSession(Connection connection, Integer key) {
        Game game = gameSessions.get(key);

        // Firstly we send a key to the recepient.
        PacketAddRoom pkt = new PacketAddRoom();
        pkt.id = connection.getID();
        pkt.roomID = key;
        connection.sendTCP(pkt);

        // Initializing player and adding it to the list
        Actor player = new Actor();
        player.c = connection;
        game.getPlayers().put(connection.getID(), player);

        for (Actor p : game.getPlayers().values()) {
            if (p.c != connection) {
                // Sending mine position to another player
                PacketAddActor mine = new PacketAddActor();
                mine.id = connection.getID();
                mine.type = PacketType.PLAYER;
                p.c.sendTCP(mine);

                // Sending other player's position to the current connection
                PacketAddActor packet2 = new PacketAddActor();
                packet2.id = p.c.getID();
                packet2.type = PacketType.PLAYER;
                connection.sendTCP(packet2);
            }
        }

        game.startGame();
    }

    private void initializeGameSession(Connection connection) {
        Integer roomID = ThreadLocalRandom.current().nextInt(100, 10000);
        if (!gameSessions.containsKey(roomID)) {
            // Firstly we send a key to the recepient.
            PacketAddRoom pkt = new PacketAddRoom();
            pkt.id = connection.getID();
            pkt.roomID = roomID;
            connection.sendTCP(pkt);

            // Creating a game session with a timer
            Game game = new Game();
            game.id = roomID;

            // Initializing player and adding it to the list
            Actor player = new Actor();
            player.c = connection;

            game.getPlayers().put(connection.getID(), player);
            
            gameSessions.put(roomID, game);
            gameSessions.get(roomID).sendFirstData();
            System.out.println("Room initialized");
            System.out.println(roomID);
        }
    }

    public void received(Connection c, Object o) {
        if (o instanceof PacketAddActor) {
            PacketAddActor packet = (PacketAddActor) o;
            Game game = gameSessions.get(packet.room);

            if (packet.type == PacketType.BULLET) {
                game.getPlayers().values().forEach(e -> {
                    if (e.c.getID() != c.getID()) {
                        e.c.sendUDP(packet);
                    }
                });
            }
        }

        if (o instanceof PacketRemoveActor) {
            PacketRemoveActor packet = (PacketRemoveActor) o;
            Game game = gameSessions.get(packet.room);

            if (packet.type == PacketType.BULLET) {
                game.getPlayers().values().forEach(e -> {
                    if (e.c.getID() != c.getID()) {
                        e.c.sendUDP(packet);
                    }
                });
            }
        }

        if (o instanceof PacketUpdatePosition) {
            PacketUpdatePosition packet = (PacketUpdatePosition) o;
            packet.id = c.getID();
            Game game = gameSessions.get(packet.room);

            if (packet.type == PacketType.BULLET) {
                game.getPlayers().values().forEach(e -> {
                    if (e.c.getID() != c.getID()) {
                        e.c.sendUDP(packet);
                    }
                });
            } else {
                game.getPlayers().values().forEach(e -> {
                    if (e.c.getID() != c.getID()) {
                        e.c.sendUDP(packet);
                    }
                });

                game.getPlayers().get(c.getID()).x = packet.x;
                game.getPlayers().get(c.getID()).y = packet.y;
            }
        } else if (o instanceof PacketUpdateDirection) {
            PacketUpdateDirection packet = (PacketUpdateDirection) o;
            packet.id = c.getID();
            Game game = gameSessions.get(packet.room);
            
            game.getPlayers().values().forEach(e -> {
                if (e.c.getID() != c.getID()) {
                    e.c.sendUDP(packet);
                }
            });

        } else if (o instanceof PacketUpdateState) {
            PacketUpdateState packet = (PacketUpdateState) o;
            packet.id = c.getID();
            Game game = gameSessions.get(packet.room);

            game.getPlayers().values().forEach(e -> {
                if (e.c.getID() != c.getID()) {
                    e.c.sendUDP(packet);
                }
            });

        } else if (o instanceof PacketUpdateHealth) {
            PacketUpdateHealth packet = (PacketUpdateHealth) o;
            Game game = gameSessions.get(packet.room);

            if (packet.type == PacketType.ENEMY) {
                if (game.getEnemies().containsKey(packet.id)) {
                    game.getEnemies().get(packet.id).health = packet.health;
                }
                game.getPlayers().values().forEach(e -> {
                    if (e.c.getID() != c.getID()) {
                        e.c.sendUDP(packet);
                    }
                });
            }
        }
    }

    public void disconnected(Connection c) {
        Game game = gameSessions.values().stream()
            .filter(e -> e.getPlayers().containsKey(c.getID()))
            .findFirst().get();

        game.removePlayer(c.getID());
        PacketRemoveActor packet = new PacketRemoveActor();
        packet.id = c.getID();
        packet.type = PacketType.PLAYER;

        game.getPlayers().values().forEach(e -> {
            if (e.c.getID() != c.getID())
                e.c.sendUDP(packet); 
        });

        if (game.getPlayers().size() <= 0) {
            game.stopTimer();
            gameSessions.remove(game.id);
        }

        System.out.println("connection dropped");
    }
}
