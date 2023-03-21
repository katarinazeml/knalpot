package org.knalpot.knalpot.networking;

import com.badlogic.gdx.math.Vector2;

public class PlayerModel {

    float speed = 2f;
    Vector2 position;
    Vector2 networkPosition = new Vector2(0,0);

    public PlayerModel(Vector2 position) {
        this.position = position;
    }
}

