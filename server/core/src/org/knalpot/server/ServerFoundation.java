package org.knalpot.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.time.LocalDateTime;

import org.knalpot.server.actors.Actor;
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
    private static Game game;
    private static final int port = 8084;

    public static void main(String[] args) throws IOException {
        server = new Server();
        game = new Game();
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

        for (Actor p : game.getPlayers().values()) {
            PacketAddActor packet3 = new PacketAddActor();
            packet3.id = p.c.getID();
            connection.sendTCP(packet3);

            game.getEnemies().values().forEach(e -> {
                SpawnEnemyMessage msg = new SpawnEnemyMessage();
                msg.id = e.id;
                msg.x = e.x;
                msg.y = e.y;
                connection.sendTCP(msg);
            });
        }

        game.getPlayers().put(connection.getID(), player);
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
            server.sendToAllExceptUDP(c.getID(), packet);
            System.out.println("health updated");
        }
    }

    public void disconnected(Connection c) {
        game.removePlayer(c.getID());
        PacketRemoveActor packet = new PacketRemoveActor();
        packet.id = c.getID();
        server.sendToAllExceptTCP(c.getID(), packet);
        System.out.println("connection dropped");
    }
}
