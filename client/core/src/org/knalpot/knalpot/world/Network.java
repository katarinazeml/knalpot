package org.knalpot.knalpot.world;

import java.io.IOException;
import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import org.knalpot.knalpot.actors.Enemy;
import org.knalpot.knalpot.actors.player.Player;
import org.knalpot.knalpot.networking.*;

public class Network extends Listener {
    String ip = "localhost";
    public static int port = 8084;
    private Client client = new Client();

    private ClientProgram clientProg;

    public Network(ClientProgram clientProg) {
        this.clientProg = clientProg;
    }

    public Client getClient() {
        return client;
    }

    public void connect() {
        register(client);
        client.addListener(this);

        client.start();
        System.out.println("Client is started");
        try {
            System.out.println("Trying to connect to the server.");
            client.connect(5000, InetAddress.getByName(ip), port, port);
            System.out.println("Connected to the server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register(Client client) {
        client.getKryo().register(PacketAddActor.class);
        client.getKryo().register(PacketRemoveActor.class);
        client.getKryo().register(PacketUpdatePosition.class);
        client.getKryo().register(PacketUpdateDirection.class);
        client.getKryo().register(PacketUpdateState.class);
        client.getKryo().register(PacketType.class);
        client.getKryo().register(Player.State.class);
        client.getKryo().register(SpawnEnemyMessage.class);
        client.getKryo().register(PacketUpdateHealth.class);
    }
    
    public void received(Connection c, Object o){
        if (o instanceof PacketAddActor) {
            PacketAddActor packet = (PacketAddActor) o;
            MPActor temp = new MPActor();
            Player newPlayer = new Player(temp);

            ClientProgram.players.put(packet.id, newPlayer);
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    clientProg.addOrbToWorld(newPlayer);
                }
            });
        }

        if (o instanceof PacketRemoveActor){
            PacketRemoveActor packet = (PacketRemoveActor) o;
            if (packet.type == PacketType.PLAYER) {
                ClientProgram.players.remove(packet.id);
            }
        }

        if (o instanceof PacketUpdatePosition) {
            PacketUpdatePosition packet = (PacketUpdatePosition) o;
            ClientProgram.players.get(packet.id).getPosition().x = packet.x;
            ClientProgram.players.get(packet.id).getPosition().y = packet.y;
        }

        if (o instanceof PacketUpdateDirection) {
            PacketUpdateDirection packet = (PacketUpdateDirection) o;
            ClientProgram.players.get(packet.id).direction = packet.direction;
        }

        if (o instanceof PacketUpdateState) {
            PacketUpdateState packet = (PacketUpdateState) o;
            ClientProgram.players.get(packet.id).state = packet.state;
        }

        if (o instanceof SpawnEnemyMessage) {
            SpawnEnemyMessage packet = (SpawnEnemyMessage) o;
            ClientProgram.enemies.put(packet.id, new Enemy(new Vector2(packet.x, packet.y)));
            System.out.println("Added enemy to the world");
        }

        if (o instanceof PacketUpdateHealth) {
            PacketUpdateHealth packet = (PacketUpdateHealth) o;
            if (packet.type == PacketType.PLAYER) {
                ClientProgram.players.get(packet.id).health = packet.health;
            }
            if (packet.type == PacketType.ENEMY) {
                ClientProgram.enemies.get(packet.id).health = packet.health;
            }
        }

    }
}
