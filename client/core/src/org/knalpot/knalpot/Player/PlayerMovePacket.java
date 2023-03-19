package org.knalpot.knalpot.Player;

public class PlayerMovePacket {

    public int connectionID;
    public float x;
    public float y;

    public PlayerMovePacket() {}

    public PlayerMovePacket(int connectionID, float x, float y) {
        this.connectionID = connectionID;
        this.x = x;
        this.y = y;
    }
}

