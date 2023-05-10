package org.knalpot.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.knalpot.server.actors.Actor;
import org.knalpot.server.actors.Enemy;
import org.knalpot.server.actors.State;
import org.knalpot.server.general.PacketAddActor;
import org.knalpot.server.general.PacketRemoveActor;
import org.knalpot.server.general.PacketType;
import org.knalpot.server.general.PacketUpdateDirection;
import org.knalpot.server.general.PacketUpdateHealth;
import org.knalpot.server.general.PacketUpdatePosition;
import org.knalpot.server.general.PacketUpdateState;
import org.knalpot.server.general.SpawnEnemyMessage;

public class ServerFoundation extends Listener {

    private static Server server;
    private static final int port = 8084;

    private Map<Integer, Actor> players = new HashMap<>();
    private Map<Integer, Enemy> enemies = new HashMap<>();

    public static void main(String[] args) throws IOException {
        server = new Server();
        server.start();
        server.getKryo().register(PacketAddActor.class);
        server.getKryo().register(PacketRemoveActor.class);
        server.getKryo().register(PacketUpdatePosition.class);
        server.getKryo().register(PacketUpdateDirection.class);
        server.getKryo().register(PacketUpdateState.class);
        server.getKryo().register(PacketType.class);
        server.getKryo().register(State.class);
        server.getKryo().register(PacketUpdateHealth.class);
        server.getKryo().register(SpawnEnemyMessage.class);
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
        Actor player = new Actor();
        player.c = connection;

        PacketAddActor packet = new PacketAddActor();
        packet.id = connection.getID();
        server.sendToAllExceptTCP(connection.getID(), packet);

        for (Actor p : players.values()) {
            PacketAddActor packet3 = new PacketAddActor();
            packet3.id = p.c.getID();
            connection.sendTCP(packet3);
        }

        players.put(connection.getID(), player);
        System.out.println("connection received");
    }

    public void received(Connection c, Object o) {
        if (o instanceof PacketUpdatePosition) {

            PacketUpdatePosition packet = (PacketUpdatePosition) o; 
            packet.id = c.getID();
            server.sendToAllExceptUDP(c.getID(), packet);
            System.out.println("position updated");

        } else if (o instanceof PacketUpdateDirection) {

            PacketUpdateDirection packet = (PacketUpdateDirection) o;
            packet.id = c.getID();
            server.sendToAllExceptUDP(c.getID(), packet);
            System.out.println("direction updated");

        } else if (o instanceof PacketUpdateState) {

            PacketUpdateState packet = (PacketUpdateState) o;
            packet.id = c.getID();
            server.sendToAllExceptUDP(c.getID(), packet);
            System.out.println("state updated");

        } else if (o instanceof PacketUpdateHealth) {

            PacketUpdateHealth packet = (PacketUpdateHealth) o;
            packet.id = c.getID();
            server.sendToAllExceptUDP(c.getID(), packet);
            System.out.println("health updated");

        } else if (o instanceof SpawnEnemyMessage) {

            SpawnEnemyMessage packet = (SpawnEnemyMessage) o;
            Random random = new Random();
            // generate random id for the enemy
            int n = 50 - 1 + 1; // maximum - minimum + 1
            int i = random.nextInt() % n;
            int id =  1 + i; // minimum + i
            packet.id = id;
            enemies.put(id, new Enemy(packet.x, packet.y));
            server.sendToAllExceptUDP(c.getID(), packet);
            
        }
    }

    public void disconnected(Connection c) {
        players.remove(c.getID());
        PacketRemoveActor packet = new PacketRemoveActor();
        packet.id = c.getID();
        server.sendToAllExceptTCP(c.getID(), packet);
        System.out.println("connection dropped");
    }
}
