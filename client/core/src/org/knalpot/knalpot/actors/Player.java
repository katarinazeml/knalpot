<<<<<<< HEAD
<<<<<<< HEAD:client/core/src/org/knalpot/knalpot/Player/Player.java
package org.knalpot.knalpot.player;

import java.lang.System;
=======
package org.knalpot.knalpot.actors;
>>>>>>> c148d34 (Refactored a bit folders in project):client/core/src/org/knalpot/knalpot/actors/Player.java
=======
package org.knalpot.knalpot.actors;
>>>>>>> 0a8fd2cdaa7f133b1b996dd4f2c1c35e8f593c5c

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    // PLAYER-RELATED //
    public enum State {
        IDLE, MOVE, JUMP, FALL
    }

    public State state;
    private Rectangle bounds;

    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;
    
    // VECTOR //
    private Vector2 position;
    private Vector2 acceleration;
    private Vector2 velocity;

    // PLAYER CONSTRUCTOR //
    public Player(Vector2 position) {
        this.position = position;
        
        velocity = new Vector2();
        acceleration = new Vector2();

        // Creating necessary objects.
        bounds = new Rectangle();
        bounds.x = this.position.x;
        bounds.y = this.position.y;
        bounds.width = WIDTH;
        bounds.height = HEIGHT;

        // Initializing necessary functions/setting up fundamentals.
        state = State.IDLE;
    }

    // GETTERS //
    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public State getState() {
        return state;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public void update(float dt) {
        position.add(velocity.cpy().scl(dt));
        bounds.x = position.x;
        bounds.y = position.y;
    }
}