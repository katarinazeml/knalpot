package org.knalpot.server.general;

import org.knalpot.server.actors.State;

public class PacketUpdateState {
    public int id;
    public int room;
    public PacketType type;
    public State state;
}
