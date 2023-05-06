package org.knalpot.server;

import java.util.HashMap;
import java.util.Map;

import org.knalpot.server.actors.Actor;

public class Game {
    private Map<Integer, Actor> players;

    public Game() {
        players = new HashMap<>();
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
}
