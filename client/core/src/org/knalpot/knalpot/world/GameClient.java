package org.knalpot.knalpot.world;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;

import org.knalpot.knalpot.actors.Player;
import org.knalpot.knalpot.networking.PlayerMovePacket;

public class GameClient {
    private Client client;
    private final String host = "localhost"; // change this to the IP address of the server
    private final int port = 8080; // change this to the port of the server
    private Player player; // instance of the Player class.

    public GameClient() {
        client = new Client();
        registerPackets();
        client.start();
        try {
            client.connect(5000, host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                // handle incoming packets from the server here
            }
        });

        // create an instance of the Player class
        player = new Player(new Vector2(0, 0));
    }

    private void registerPackets() {
        client.getKryo().register(PlayerMovePacket.class);
        // add more packet classes here if needed
    }

    private void sendPlayerMovePacket() {
        // create a new PlayerMovePacket with the player's position
        PlayerMovePacket packet = new PlayerMovePacket();
        packet.x = player.getPosition().x;
        packet.y = player.getPosition().y;
        client.sendTCP(packet);
    }

    public static void main(String[] args) {
        new GameClient();
    }
}
