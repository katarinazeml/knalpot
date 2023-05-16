package org.knalpot.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import org.knalpot.server.actors.Actor;
import org.knalpot.server.actors.Bullet;
import org.knalpot.server.actors.Enemy;
import org.knalpot.server.addons.LoadXMLData;
import org.knalpot.server.general.PacketType;
import org.knalpot.server.general.PacketUpdatePosition;
import org.knalpot.server.general.SpawnEnemyMessage;

public class Game {
    
    private Map<Integer, Actor> players = new HashMap<>();
    private Map<Integer, Enemy> enemies = new HashMap<>();
    private Map<Integer, Bullet> bullets = new HashMap<>();
    private Timer timer;

    private boolean gameStarted;

    public Game() {

    }

    public void sendFirstData() {
        // Scheduling a timer inside the game.
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
                for (Enemy en : getEnemies().values()) {
                    PacketUpdatePosition pkg = new PacketUpdatePosition();
                    pkg.id = en.id;
                    pkg.type = PacketType.ENEMY;
                    pkg.x = en.x;
                    pkg.y = en.y;
                    getPlayers().values().forEach(e -> {
                        e.c.sendUDP(pkg);
                    });
                }
            }
        }, 100, 100);
    }

    public void sendEnemyData() {
        getEnemies().values().forEach(e -> {
            SpawnEnemyMessage msg = new SpawnEnemyMessage();
            msg.id = e.id;
            msg.x = e.x;
            msg.y = e.y;
            getPlayers().values().forEach(el -> {
                el.c.sendUDP(msg);
            });
        });
    }

    public void startGame() {
        generateEnemies();
        sendEnemyData();
        System.out.println("Game is started and enemy data is sent");
        System.out.println("Players amount: " + players.size());
    }

    public void removePlayer(int connectionID) {
        players.remove(connectionID);
    }

    public Actor getPlayer(int connectionID) {
        return players.get(connectionID);
    }

    public Map<Integer, Actor> getPlayers() {
        return players;
    }

    public boolean isFull() {
        return players.size() == 2;
    }

    public Map<Integer, Enemy> getEnemies() {
        return enemies;
    }

    public void addEnemy(int id, Enemy enemy) {
        enemies.put(id, enemy);
    }

    public void removeEnemy(int id) {
        enemies.remove(id);
    }

    public Map<Integer, Bullet> getBullets() {
        return bullets;
    }

    public void addBullet(int bulletID, float x, float y) {
        if (!bullets.containsKey(bulletID)) {
            Bullet bul = new Bullet();
            bul.id = bulletID;
            bul.x = x;
            bul.y = y;
            bullets.put(bulletID, bul);
        }
    }

    public void updateBullet(int id, float x, float y) {
        if (bullets.containsKey(id)) {
            bullets.get(id).x = x;
            bullets.get(id).y = y;
        }
    }

    public void removeBullet(int id) {
        if (bullets.containsKey(id)) {
            bullets.remove(id);
        }
    }

    public void stopTimer() {
        timer.cancel();
        timer.purge();
    }

    public void generateEnemies() {
        // generate random id for the enemy
        Map<Integer, List<Integer>> values = LoadXMLData.getData("data/serverdata.xml", "enemy");

        for (List<Integer> list : values.values()) {
            int id = ThreadLocalRandom.current().nextInt(0, 10000);
            if (!enemies.containsKey(id)) {
                Enemy en = new Enemy(list.get(0), list.get(1));
                en.id = id;
                en.world = this;
                enemies.put(id, en);
            }
        }
    }

    public void update() {
        // Updating each enemy.
        enemies.values().forEach(e -> {
            e.update();
        });

        // Checking of somebody's health is lower than zero.
        Iterator<Map.Entry<Integer, Enemy>> iterator = enemies.entrySet().iterator();
        while (iterator.hasNext()) {
            Enemy value = iterator.next().getValue();
            if (value.health <= 0) {
                iterator.remove();
            }
        }
    }
}
