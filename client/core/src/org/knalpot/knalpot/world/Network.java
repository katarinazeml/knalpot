package org.knalpot.knalpot.world;

import java.io.IOException;
import java.net.InetAddress;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import org.knalpot.knalpot.actors.Player;
import org.knalpot.knalpot.networking.*;

public class Network extends Listener {
    String ip = "193.40.156.27";
    public static int port = 8084;
    private Client client = new Client();

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
        client.getKryo().register(PacketUpdateX.class);
        client.getKryo().register(PacketUpdateY.class);
        client.getKryo().register(PacketAddPlayer.class);
        client.getKryo().register(PacketRemovePlayer.class);
        client.getKryo().register(PacketUpdateDirection.class);
        client.getKryo().register(PacketUpdateState.class);
        client.getKryo().register(Player.State.class);
    }
    

    public void received(Connection c, Object o){
        if (o instanceof PacketAddPlayer) {
            PacketAddPlayer packet = (PacketAddPlayer) o;
            MPPlayer newPlayer = new MPPlayer();
            newPlayer.id = packet.id;
            ClientProgram.players.put(packet.id, newPlayer);

        }

        if(o instanceof PacketRemovePlayer){
            PacketRemovePlayer packet = (PacketRemovePlayer) o;
            ClientProgram.players.remove(packet.id);
        }

        if(o instanceof PacketUpdateX) {
            PacketUpdateX packet = (PacketUpdateX) o;
            ClientProgram.players.get(packet.id).x = packet.x;

        }

        if(o instanceof PacketUpdateY) {
            PacketUpdateY packet = (PacketUpdateY) o;
            ClientProgram.players.get(packet.id).y = packet.y;
        }

        if (o instanceof PacketUpdateDirection) {
            PacketUpdateDirection packet = (PacketUpdateDirection) o;
            ClientProgram.players.get(packet.id).direction = packet.direction;
        }

        if (o instanceof PacketUpdateState) {
            PacketUpdateState packet = (PacketUpdateState) o;
            ClientProgram.players.get(packet.id).state = packet.state;
        }
    }
}
