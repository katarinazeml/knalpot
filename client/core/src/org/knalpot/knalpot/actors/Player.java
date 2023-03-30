package org.knalpot.knalpot.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * {@code Player} class is responsible for housing key components of player's character. 
 * It is mainly used as a storage for certain parameters to be called from the outside.
 * <p>
 * Comes together with {@code PlayerProcessor} class.
 * @author Max Usmanov
 * @version 0.1
 */
public class Player extends Actor {
    //#region -- VARIABLES --

    // no comments //
    public enum State {
        IDLE, MOVE, JUMP, FALL
    }
    public State state;

    //#endregion

    //#region -- FUNCTIONS --
    /**
     * Player constructor.
     * @param position
     * @param direction 
     */
    public Player(Vector2 position) {
        this.position = position;

        WIDTH = 32;
        HEIGHT = 48;
        
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

    public void update(float dt) {
        position.add(velocity.cpy().scl(dt));
        bounds.x = (int) position.x;
        bounds.y = (int) position.y;

        Left = (int) bounds.x;
        Right = (int) bounds.x + WIDTH;
        Bottom = (int) bounds.y;
        Top = (int) bounds.y + HEIGHT;
    }
    //#endregion
}
