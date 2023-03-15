package org.knalpot.knalpot.actors;

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

    // POSITION BOUNDS //
    public int Left;
    public int Right;
    public int Top;
    public int Bottom;

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

        Left = (int) bounds.x;
        Right = (int) (bounds.x + bounds.width);
        Bottom = (int) bounds.y;
        Top = (int) (bounds.y + bounds.height);

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

    public Vector2 getScalarVelocity(float dt) {
        return velocity.cpy().scl(dt);
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
        bounds.x = (int) position.x;
        bounds.y = (int) position.y;

        Left = (int) bounds.x;
        Right = (int) bounds.x + WIDTH;
        Bottom = (int) bounds.y;
        Top = (int) bounds.y + HEIGHT;
    }
}