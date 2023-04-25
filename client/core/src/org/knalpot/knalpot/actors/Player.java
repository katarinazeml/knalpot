package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.addons.BBGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
    
    private Enemy enemy;

    // no comments //
    public enum State {
        IDLE, MOVE, JUMP, FALL
    }

    //#endregion

    //#region -- FUNCTIONS --
    /**
     * Player constructor.
     * @param position
     * @param direction 
     */
    public Player(Vector2 position) {
        this.position = position;

        // Temporary solution
        texture = new Texture("player.png");
        BBSize = BBGenerator.BBPixels(texture.getTextureData());
        scaleSize = 2;
        //

        WIDTH = texture.getWidth() * scaleSize;
        HEIGHT = texture.getHeight() * scaleSize;
        
        velocity = new Vector2();
        acceleration = new Vector2();

        // Creating necessary objects.
        bounds = new Rectangle();
        bounds.x = this.position.x;
        bounds.y = this.position.y;
        bounds.width = BBSize[0] * scaleSize;
        bounds.height = BBSize[1] * scaleSize;

        Left = (int) bounds.x;
        Right = (int) (bounds.x + bounds.width);
        Bottom = (int) bounds.y;
        Top = (int) (bounds.y + bounds.height);

        enemy = new Enemy(new Vector2(100, 300), this);
    }

    public void update(float dt) {
        position.add(velocity.cpy().scl(dt));
        bounds.x = position.x;
        bounds.y = position.y;

        Left = (int) bounds.x;
        Right = (int) bounds.x + WIDTH;
        Bottom = (int) bounds.y;
        Top = (int) bounds.y + HEIGHT;
        enemy.update(dt);
    }
    public void caughtByEnemy(){
        
    }
    //#endregion
}
