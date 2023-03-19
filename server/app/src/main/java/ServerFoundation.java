import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.time.LocalDateTime;
import ServerPlayer.Game;
import ServerPlayer.ServerPlayer;

public class ServerFoundation {
    private Server server;
    private final int port = 8080;
    private Game game;

    public ServerFoundation() {
        server = new Server();
        game = new Game();
        registerPackets();
        server.start();
        try {
            server.bind(port);
            System.out.print("Server started on port: " + port +
                    " | start time: " + LocalDateTime.now());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                System.out.println("New connection! Connected ID: "
                        + connection.getID() + " date: " + LocalDateTime.now());
                if (object instanceof ServerPlayer) {
                    ServerPlayer packet = (ServerPlayer) object;
                    // update player coordinates
                    updatePlayerCoordinates(connection.getID(), packet.getX(), packet.getY());
                    System.out.println("X coordinate: " + packet.getX() + "Y coordinate: " + packet.getY());
                    broadcastPacket(packet);
                }
            }
        });
    }

    private void registerPackets() {
        server.getKryo().register(ServerPlayer.class);
        // add more packet classes here if needed
    }

    private void broadcastPacket(Object object) {
        for (Connection connection : server.getConnections()) {
            connection.sendTCP(object);
        }
    }
    private void updatePlayerCoordinates(int connectionID, float x, float y) {
        // find the player with the given connection ID and update their coordinates
        ServerPlayer player = game.getPlayer(connectionID);
        player.setX(x);
        player.setY(y);
    }

    public static void main(String[] args) {
        new ServerFoundation();
    }
}
