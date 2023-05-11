package org.knalpot.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.knalpot.server.actors.Actor;
import org.knalpot.server.actors.Enemy;
import org.knalpot.server.addons.LoadXMLData;

public class Game {
    
    private static Map<Integer, Actor> players = new HashMap<>();
    private static Map<Integer, Enemy> enemies = new HashMap<>();

    public Game() {
        generateEnemies();
    }

    public void addPlayer(int connectionID) {
        players.put(connectionID, new Actor());
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

    public Map<Integer, Enemy> getEnemies() {
        return enemies;
    }

    public void addEnemy(int id, Enemy enemy) {
        enemies.put(id, enemy);
    }

    public void removeEnemy(int id) {
        enemies.remove(id);
    }

    public void generateEnemies() {
        // generate random id for the enemy
        Map<Integer, List<Integer>> values = LoadXMLData.getData("data/serverdata.xml", "enemy");

        for (List<Integer> list : values.values()) {
            int id = ThreadLocalRandom.current().nextInt(0, 10000);
            if (!enemies.containsKey(id)) {
                Enemy en = new Enemy(list.get(0), list.get(1));
                en.id = id;
                enemies.put(id, en);
            }
        }
    }

    public void update() {
        // // Updating each enemy.
        // enemies.values().forEach(e -> {
        //     e.update();
        // });

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
