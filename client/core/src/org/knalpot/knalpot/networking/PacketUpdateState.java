package org.knalpot.knalpot.networking;

import org.knalpot.knalpot.actors.player.Player.State;

public class PacketUpdateState {
    public int id;
    public int room;
    public PacketType type;
    public State state;
}
