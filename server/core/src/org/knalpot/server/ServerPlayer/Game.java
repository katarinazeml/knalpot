package org.knalpot.server.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private Map<Integer, ServerPlayer> players;

    public Game() {
        players = new HashMap<>();
    }

    public void addPlayer(int connectionID) {
        players.put(connectionID, new ServerPlayer());
    }

    public void removePlayer(int connectionID) {
        players.remove(connectionID);
    }

    public ServerPlayer getPlayer(int connectionID) {
        return players.get(connectionID);
    }
}
