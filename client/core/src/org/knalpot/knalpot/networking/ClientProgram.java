package org.knalpot.knalpot.networking;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;

import org.knalpot.knalpot.world.Network;

import java.util.HashMap;
import java.util.Map;

public class ClientProgram extends ApplicationAdapter {
    static PlayerModel player;
    static Network network;
    public static Map<Integer, MPPlayer> players = new HashMap<Integer, MPPlayer>();
    private Client client;

    public ClientProgram(Vector2 position) {
        player = new PlayerModel(position);
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
        if (player.networkPosition.x != player.position.x) {
            // Send the player's X value
            PacketUpdateX packet = new PacketUpdateX();
            packet.x = player.position.x;
            client.sendUDP(packet);

            player.networkPosition.x = player.position.x;
        }
        if (player.networkPosition.y != player.position.y) {
            // Send the player's Y value
            PacketUpdateY packet = new PacketUpdateY();
            packet.y = player.position.y;
            client.sendUDP(packet);

            player.networkPosition.y = player.position.y;
        }
    }

    @Override
    public void dispose () {
        client.stop();
    }
}
