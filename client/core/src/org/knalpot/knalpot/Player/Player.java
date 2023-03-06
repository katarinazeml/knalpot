package org.knalpot.knalpot.Player;

import java.lang.System;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class Player {
    // PLAYER-RELATED //
    public enum State {
        IDLE, MOVE, JUMP, FALL
    }

    public State state;
    private Rectangle bounds;

    private static boolean isGrounded;

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