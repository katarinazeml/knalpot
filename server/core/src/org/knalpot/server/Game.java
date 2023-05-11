package org.knalpot.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.knalpot.server.actors.Actor;
import org.knalpot.server.actors.Enemy;

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
        int id = ThreadLocalRandom.current().nextInt(0, 100);
        enemies.put(id, new Enemy(240, 112));
    }
}
