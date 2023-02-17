package org.knalpot.knalpot;

import java.lang.System;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class Player {
    // PLAYER-RELATED //
    private enum State {
        IDLE, MOVE, JUMP, FALL
    }

    private State state;
    private Rectangle hitbox;

    private static boolean isGrounded;

    public static final int WIDTH = 16;
    public static final int HEIGHT = 16;
    
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
        hitbox = new Rectangle();
        hitbox.x = this.position.x;
        hitbox.y = this.position.y;
        hitbox.width = WIDTH;
        hitbox.height = HEIGHT;

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

    public Rectangle getHitbox() {
        return hitbox;
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
        hitbox.x = position.x;
        hitbox.y = position.y;
    }
}