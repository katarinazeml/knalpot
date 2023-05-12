package org.knalpot.knalpot.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.knalpot.knalpot.actors.Actor;
import org.knalpot.knalpot.actors.Bullet;
import org.knalpot.knalpot.actors.player.Player;
import org.knalpot.knalpot.actors.Enemy;
import org.knalpot.knalpot.world.Network;
import org.knalpot.knalpot.world.World;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;

public class ClientProgram extends ApplicationAdapter {
    static Network network;

    public static Map<Integer, Actor> players = new HashMap<>();
    public static Map<Integer, Enemy> enemies = new HashMap<>();
    public static Map<Integer, MPActor> bullets = new HashMap<>();
    public static Set<Integer> bulletHash = new HashSet<Integer>();

    private Client client;
    public static World world;
    private Player player;

    public ClientProgram(World world) {
        ClientProgram.world = world;
        this.player = world.getPlayer();

        network = new Network(this);
        client = network.getClient();
    }

    public int getID() {
        return client.getID();
    }

    public Map<Integer, Actor> getPlayers() {
        return players;
    }

    public Map<Integer, Enemy> getEnemies() {
        return enemies;
    }

    public void addOrbToWorld(Actor actor) {
        world.addOrb(actor);
    }

    public void addEnemyToWorld(MPActor data) {
        Enemy enemy = new Enemy(new Vector2(data.x * 2, data.y * 2));
        ClientProgram.enemies.put(data.id, enemy);
        world.addEnemy(enemy);
    }

    @Override
    public void create() {
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
        if (player.getVelocity().x != 0 || player.getVelocity().y != 0) {
            // Send the player's Y value
            PacketUpdatePosition packet = new PacketUpdatePosition();
            packet.type = PacketType.PLAYER;
            packet.x = player.getPosition().x;
            packet.y = player.getPosition().y;
            client.sendUDP(packet);
        }

        if (player.direction != player.previousDirection) {
            // Send the player's direction
            PacketUpdateDirection packet = new PacketUpdateDirection();
            packet.type = PacketType.PLAYER;
            packet.direction = player.direction;
            client.sendUDP(packet);
        }

        if (player.state != player.previousState) {
            // Send the player's state
            PacketUpdateState packet = new PacketUpdateState();
            packet.type = PacketType.PLAYER;
            packet.state = player.state;
            client.sendUDP(packet);
            System.out.println("sent state");
        }

        // Sending position of each bullet in orb's list.
        // Issue of throttling must be resolved by adding
        // TTL (time-to-live) to each bullet, so it's removed as fast as possible.
        ClientProgram.world.getOrbs().forEach(e -> {
            if (!e.getBullets().isEmpty()) {
                if (e.getTimeSinceLastShot() == 0f) {
                    PacketAddActor packet = new PacketAddActor();
                    Bullet bullet = e.getBullets().get(e.getBullets().size() - 1);
                    // Adding hash to the list
                    bulletHash.add(bullet.hashCode());
                    packet.id = bullet.hashCode();
                    packet.type = PacketType.BULLET;
                    client.sendUDP(packet);
                    System.out.println("Sent bullet spawn");
                }

                // Checking whether to remove bullets of not.
                Set<Integer> dummy = new HashSet<Integer>(bulletHash);
                dummy.removeAll(e.getBulletsHash());
                if (!dummy.isEmpty()) {
                    dummy.stream().forEach(el -> {
                        PacketRemoveActor pkg = new PacketRemoveActor();
                        pkg.id = el;
                        pkg.type = PacketType.BULLET;
                        client.sendUDP(pkg);
                        System.out.println("Sent bullet removal packet.");
                    });
                }
                // Removing data from the original hash values list.
                dummy.stream().forEach(hash -> bulletHash.remove(hash));

                e.getBullets().forEach(el -> {
                    PacketUpdatePosition packet = new PacketUpdatePosition();
                    packet.id = el.hashCode();
                    packet.type = PacketType.BULLET;
                    packet.x = el.getPosition().x;
                    packet.y = el.getPosition().y;
                    client.sendUDP(packet);
                    System.out.println("Sent bullet position");
                });
            }
        });

        enemies.values().forEach(enemy -> {
            if (enemy.health != enemy.previousHealth) {
                // Send the enemy's health
                PacketUpdateHealth packet = new PacketUpdateHealth();
                
                // Searching for a key through the whole map.
                // Must be simplified.
                packet.id = enemies.entrySet().stream()
                    .filter(e -> e.getValue().equals(enemy))
                    .findFirst().get()
                    .getKey();

                packet.type = PacketType.ENEMY;
                packet.health = enemy.health;

                // Updating enemy's previous health so this event
                // is not triggered constantly (it seemed like the most
                // rational choice to include this piece of code here)
                enemy.previousHealth = enemy.health;
                client.sendUDP(packet);
                System.out.println("sent enemy`s health");
            }
        });
    }

    @Override
    public void dispose () {
        client.stop();
    }
}
