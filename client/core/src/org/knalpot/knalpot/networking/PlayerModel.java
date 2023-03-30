package org.knalpot.knalpot.networking;

import com.badlogic.gdx.math.Vector2;

public class PlayerModel {

    float speed = 2f;
    Vector2 position;
    Vector2 networkPosition = new Vector2(0,0);
    boolean isFacingRight;

    public PlayerModel(Vector2 position, Boolean isFacingRight) {
        this.position = position;
        this.isFacingRight = isFacingRight;
    }
}

