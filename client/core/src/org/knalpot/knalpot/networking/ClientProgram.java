package org.knalpot.knalpot.networking;

import com.badlogic.gdx.ApplicationAdapter;
import com.esotericsoftware.kryonet.Client;

import org.knalpot.knalpot.actors.Player;
import org.knalpot.knalpot.world.Network;


import java.util.HashMap;
import java.util.Map;

public class ClientProgram extends ApplicationAdapter {
    static Network network;
    public static Map<Integer, MPPlayer> players = new HashMap<Integer, MPPlayer>();
    private Client client;
    private Player player;

    public ClientProgram(Player player) {
        this.player = player;
        network = new Network();
        client = network.getClient();
    }

    public int getID() {
        return client.getID();
    }

    public Map<Integer, MPPlayer> getPlayers() {
        return players;
    }

    @Override
    public void create () {
        System.out.println("Connecting");
        try {
            network.connect();
            System.out.println("Connected");
        } catch (Exception e) {
            System.out.println("Catched exception: " + e);
        }
    }

    public void updateNetwork() {
        // Update position
        if (player.getVelocity().x != 0) {
            // Send the player's X value
            PacketUpdateX packet = new PacketUpdateX();
            packet.x = player.getPosition().x;
            client.sendUDP(packet);
        }
        if (player.getVelocity().y != 0) {
            // Send the player's Y value
            PacketUpdateY packet = new PacketUpdateY();
            packet.y = player.getPosition().y;
            client.sendUDP(packet);
        }
        if (player.direction != player.previousDirection) {
            // Send the player's direction
            PacketUpdateDirection packet = new PacketUpdateDirection();
            packet.direction = player.direction;
            client.sendUDP(packet);
        }
        if (player.state != player.previousState) {
            System.out.println("Current and previous player state");
            System.out.println(player.state);
            System.out.println(player.previousState);
            // Send the player's state
            PacketUpdateState packet = new PacketUpdateState();
            packet.state = player.state;
            client.sendUDP(packet);
            System.out.println("sent state");
        }
    }

    @Override
    public void dispose () {
        client.stop();
    }
}
