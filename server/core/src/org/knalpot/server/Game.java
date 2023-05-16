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
import org.knalpot.server.actors.Chest;
import org.knalpot.server.actors.Enemy;
import org.knalpot.server.addons.LoadXMLData;
import org.knalpot.server.general.PacketRemoveActor;
import org.knalpot.server.general.PacketType;
import org.knalpot.server.general.PacketUpdatePosition;
import org.knalpot.server.general.SpawnChest;
import org.knalpot.server.general.SpawnEnemyMessage;

public class Game {
    
    public int id;
    private Map<Integer, Actor> players = new HashMap<>();
    private Map<Integer, Enemy> enemies = new HashMap<>();
    private Map<Integer, Bullet> bullets = new HashMap<>();
    private Map<Integer, Chest> chests = new HashMap<>();

    // Enemy data from XML file
    private Map<Integer, List<Integer>> enemyValues = LoadXMLData.getData("data/serverdata.xml", "enemy");
    private Map<Integer, List<Integer>> chestValues = LoadXMLData.getData("data/serverdata.xml", "enemy");

    // Timers
    private Timer sendEnemyDataTimer;
    private Timer spawnEnemyTimer;

    private Timer spawnChestTimer;
    private Timer clearChestsTimer;

    public Game() { }

    public void sendFirstData() {
        // Scheduling a timer inside the game.
        sendEnemyDataTimer = new Timer();
        sendEnemyDataTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
                sendEnemyData();
            }
        }, 100, 100);
    }

    public void sendEnemySpawn() {
        getEnemies().values().forEach(e -> {
            SpawnEnemyMessage msg = new SpawnEnemyMessage();
            msg.id = e.id;
            msg.x = e.x;
            msg.y = e.y;
            getPlayers().values().forEach(el -> {
                el.c.sendTCP(msg);
            });
        });
    }

    public void sendEnemyData() {
        for (Enemy en : getEnemies().values()) {
            PacketUpdatePosition pkg = new PacketUpdatePosition();
            pkg.id = en.id;
            pkg.type = PacketType.ENEMY;
            pkg.x = en.x;
            pkg.y = en.y;
            getPlayers().values().forEach(e -> {
                e.c.sendTCP(pkg);
            });
        }
    }

    public void startEnemySpawnTimer() {
        spawnEnemyTimer = new Timer();
        spawnEnemyTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int spawnPlace = ThreadLocalRandom.current().nextInt(0, enemyValues.values().size());
                int id = ThreadLocalRandom.current().nextInt(0, 10000);
                spawnEnemy(enemyValues.get(spawnPlace), id);

                SpawnEnemyMessage msg = new SpawnEnemyMessage();
                msg.id = enemies.get(id).id;
                msg.x = enemies.get(id).x;
                msg.y = enemies.get(id).y;
                getPlayers().values().forEach(el -> {
                    el.c.sendTCP(msg);
                });
                System.out.println("New enemy is spawned");
            }
        }, 5000, 5000);
    }

    public void startChestSpawnTimer() {
        spawnChestTimer = new Timer();
        spawnChestTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int spawnPlace = ThreadLocalRandom.current().nextInt(0, enemyValues.values().size());
                int id = ThreadLocalRandom.current().nextInt(0, 10000);

                spawnChest(chestValues.get(spawnPlace), id);

                SpawnChest msg = new SpawnChest();
                msg.id = id;
                msg.x = chests.get(id).x;
                msg.y = chests.get(id).y;

                getPlayers().values().forEach(el -> {
                    el.c.sendTCP(msg);
                });
                System.out.println("New chest is spawned");
            }
        }, 20000, 20000);

        clearChestsTimer = new Timer();
        clearChestsTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                chests.values().forEach(e -> {
                    PacketRemoveActor msg = new PacketRemoveActor();
                    msg.id = e.id;
                    msg.type = PacketType.CHEST;
                    msg.room = id; // just in case
                    getPlayers().values().forEach(p -> {
                        p.c.sendTCP(msg); // sending each player a message to remove chests
                    });
                });
                chests.clear();
                System.out.println("Chests deleted");
            }
        }, 30000, 30000);
    }

    public void startGame() {
        generateFirstEnemies();
        sendEnemySpawn();
        startEnemySpawnTimer();
        startChestSpawnTimer();
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

    public void spawnEnemy(List<Integer> list, int id) {
        if (!enemies.containsKey(id)) {
            Enemy en = new Enemy(list.get(0), list.get(1));
            en.id = id;
            en.world = this;
            addEnemy(id, en);
        }
    }

    public void removeEnemy(int id) {
        enemies.remove(id);
    }

    public void spawnChest(List<Integer> list, int id) {
        if (!chests.containsKey(id)) {
            Chest chest = new Chest();
            chest.id = id;
            chest.x = list.get(0);
            chest.y = list.get(1);
            chests.put(id, chest);
        }
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
        sendEnemyDataTimer.cancel();
        spawnEnemyTimer.cancel();;
        spawnChestTimer.cancel();
        clearChestsTimer.cancel();

        sendEnemyDataTimer.purge();
        spawnEnemyTimer.purge();
        spawnChestTimer.purge();
        clearChestsTimer.purge();
    }

    public void generateFirstEnemies() {
        // generate random id for the enemy
        for (List<Integer> list : enemyValues.values()) {
            int id = ThreadLocalRandom.current().nextInt(0, 10000);
            spawnEnemy(list, id);
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
