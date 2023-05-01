package org.knalpot.knalpot.actors.player;

import java.util.ArrayList;
import java.util.List;

import org.knalpot.knalpot.actors.Actor;
import org.knalpot.knalpot.addons.BBGenerator;
import org.knalpot.knalpot.hud.HUDProcessor;
import org.knalpot.knalpot.hud.HUD.HUDType;
import org.knalpot.knalpot.interactive.props.Consumable;

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

    // ==== PLAYER STATES ==== //
    public enum State {
        IDLE, 
        MOVE, 
        JUMP, 
        FALL    
    }

    // ==== INVENTORY ==== //
    private List<Consumable> inventory;
    
    // ==== HUD ==== //
	private HUDProcessor inventoryHUD;

    // temporary
    public int chestIndex = 0;
    public boolean chestIsActive = false;
    //#endregion

    //#region -- FUNCTIONS --
    /**
     * Player constructor.
     * @param position
     * @param direction 
     */
    public Player(Vector2 position) {
        this.position = position;
        health = 100;

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

        // Generating inventory list.
        inventory = new ArrayList<>(5);
    }

    public void update(float dt) {
        position.add(velocity.cpy().scl(dt));
        bounds.x = position.x;
        bounds.y = position.y;

        Left = (int) bounds.x;
        Right = (int) bounds.x + WIDTH;
        Bottom = (int) bounds.y;
        Top = (int) bounds.y + HEIGHT;
    }

    /**
     * Initializes all the parameters of HUD after the object is created.
     */
    public void initializeHUD() {
        inventoryHUD = new HUDProcessor(HUDType.INVENTORY);
		inventoryHUD.setOSMData();
        inventoryHUD.initializeInventory(inventory);
    }

    /**
     * Getter for player's inventory.
     * @return List<Consumable>
     */
    public List<Consumable> getInventory() {
        return inventory;
    }

    /**
     * Returns {@link org.knalpot.knalpot.hud.HUDProcessor HUDProcessor}
     * for external usage.
     * @return HUDProcessor
     */
    public HUDProcessor getHud() {
        return inventoryHUD;
    }

    /**
     * Adds consumable to the inventory.
     * @param consum
     */
    public void addConsumable(Consumable consum) {
        inventory.add(consum);
    }

    /**
     * Removes consumable from the inventory.
     * Currently is for testing purposes only.
     * @param consumable
     */
    public void removeConsumable(Consumable consumable) {
        if (inventory.contains(consumable))
            inventory.remove(consumable);
    }
    //#endregion
}
