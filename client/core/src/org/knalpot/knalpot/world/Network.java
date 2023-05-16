package org.knalpot.knalpot.world;

import java.io.IOException;
import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import org.knalpot.knalpot.actors.player.Player;
import org.knalpot.knalpot.networking.*;

public class Network extends Listener {
    String ip = "localhost";
    public static int port = 8084;
    private Client client;

    private ClientProgram clientProg;

    public Network(ClientProgram clientProg) {
        this.clientProg = clientProg;
        client = new Client();
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
        client.getKryo().register(PacketAddRoom.class);
        client.getKryo().register(PacketRemoveActor.class);
        client.getKryo().register(PacketUpdatePosition.class);
        client.getKryo().register(PacketUpdateDirection.class);
        client.getKryo().register(PacketUpdateState.class);
        client.getKryo().register(PacketType.class, 3);
        client.getKryo().register(Player.State.class);
        client.getKryo().register(SpawnEnemyMessage.class);
        client.getKryo().register(PacketUpdateHealth.class, 2);
    }
    
    public void received(Connection c, Object o){
        if (o instanceof PacketAddRoom) {
            PacketAddRoom packet = (PacketAddRoom) o;
            ClientProgram.world.roomID = packet.roomID;
            System.out.println(ClientProgram.world.roomID);
            System.out.println("Room is created.");
        }

        if (o instanceof PacketAddActor) {
            PacketAddActor packet = (PacketAddActor) o;
            if (packet.type == null || packet.type == PacketType.PLAYER) {
                MPActor temp = new MPActor();
                Player player = new Player(temp);
                ClientProgram.players.put(packet.id, player);
                
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        clientProg.addOrbToWorld(player);
                    }
                });
            } else {
                switch (packet.type) {
                    case BULLET:
                        MPActor bullet = new MPActor();
                        System.out.println("Packet ID aka hashcode");
                        System.out.println(packet.id);
                        ClientProgram.bullets.put(packet.id, bullet);
                        System.out.println("Bullets map size");
                        System.out.println(ClientProgram.bullets.size());
                        break;
                    default:
                        break;
                }
            }
        }

        if (o instanceof PacketRemoveActor){
            PacketRemoveActor packet = (PacketRemoveActor) o;
            switch (packet.type) {
                case PLAYER:
                    ClientProgram.players.remove(packet.id);
                    break;
                case BULLET:
                    ClientProgram.bullets.remove(packet.id);
                    break;
                default:
                    break;
            }
        }

        if (o instanceof PacketUpdatePosition) {
            PacketUpdatePosition packet = (PacketUpdatePosition) o;
            switch (packet.type) {
                case PLAYER:
                    System.out.println("Update player position");
                    System.out.println(packet.id);
                    System.out.println(ClientProgram.players.size());
                    ClientProgram.players.get(packet.id).getPosition().x = packet.x;
                    ClientProgram.players.get(packet.id).getPosition().y = packet.y;
                    break;
                case ENEMY:
                    System.out.println("Updating enemy position");
                    System.out.println(packet.id);
                    if (ClientProgram.enemies.containsKey(packet.id)) {
                        System.out.println("Enemy exists on the server");
                        ClientProgram.enemies.get(packet.id).getPosition().x = packet.x * 2;
                        ClientProgram.enemies.get(packet.id).getPosition().y = packet.y * 2;
                    }
                    break;
                case BULLET:
                    System.out.println("Updating bullet position.");
                    if (ClientProgram.bullets.containsKey(packet.id)) {
                        ClientProgram.bullets.get(packet.id).x = packet.x;
                        ClientProgram.bullets.get(packet.id).y = packet.y;
                    }
                    break;
                default:
                    break;
            }
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
            System.out.println("Received enemy data");
            SpawnEnemyMessage packet = (SpawnEnemyMessage) o;
            MPActor temp = new MPActor();
            temp.id = packet.id;
            temp.x = packet.x;
            temp.y = packet.y;
            
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    clientProg.addEnemyToWorld(temp);
                    System.out.println("Added enemy to the world");
                }
            });
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
