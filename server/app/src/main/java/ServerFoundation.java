import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.time.LocalDateTime;

public class ServerFoundation {
    public static ServerFoundation instance;
    private Server server;
    public static void main(String[] args) {
        ServerFoundation.instance = new ServerFoundation();
    }

    public ServerFoundation() {
        this.server = new Server(1_000_000, 1_000_000);
        this.bindServer(8080, 8081);
    }

    public void bindServer(final int tcpPort, final int udpPort) {
        this.server.start();
        try {
            this.server.bind(tcpPort, udpPort);
            System.out.print("Server started | tcp port: " + tcpPort + " | udp port: " + udpPort
                    + " | time: " + LocalDateTime.now());
        } catch (IOException e) {
            System.out.println("Connection to the server failed!\n Exception: " + e);
            System.exit(0);
        }
    }
}
