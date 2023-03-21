package org.knalpot.knalpot.addons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.knalpot.knalpot.actors.Actor;
import org.knalpot.knalpot.interactive.Static;
import org.knalpot.knalpot.networking.ClientProgram;
import org.knalpot.knalpot.networking.MPPlayer;
import org.knalpot.knalpot.world.Network;
import org.knalpot.knalpot.world.World;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * {@code Renderer} class renders all textures in the specified location.
 * It is crucial that {@code Renderer} class is called with either a list
 * or map of textures required to load.
 * <p>
 * Currently WIP, as it utilizes 'I say so' workflow. Must be modular.
 * @author Max Usmanov
 * @version 0.1
 */
public class Renderer {
    //#region -- VARIABLES --

    // ==== OBJECTS ==== //
    private SpriteBatch batch;
    private Texture playerTexture;
    private Texture staticTexture;

    private World world;
    private Actor player;

    private Static block;

    // ==== NETWORKING ==== //
    private ClientProgram networking;

	// ==== CAMERA ==== //
    private OrthographicCamera camera;
    private static int CAMERA_WIDTH = 400;
    private static int CAMERA_HEIGHT = 400;

    // ==== SHORTCUTS ==== //
    float WW = Constants.WINDOW_WIDTH;
   	float WH = Constants.WINDOW_HEIGHT;
    //#endregion

    //#region -- FUNCTIONS --

    /**
     * {@code Renderer} constructor.
     * @param world
     */
    public Renderer(World world) {
    	this.world = world;

        // Create and setup camera.
    	camera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT * (WH / WW));
        camera.position.set(WW / 2, 120, 0);
        camera.update();

        // Initialize spritebatch.
        batch = new SpriteBatch();
        player = this.world.getPlayer();
        block = this.world.getCollisionBlocks();
        networking = this.world.getClientProgram();

        // Load other objects' textures.
        loadTextures();
    }

    /**
     * Sets {@code OrthographicCamera} size, obviously.
     * @param w
     * @param h
     */
    public void setCameraSize(int w, int h) {
    	CAMERA_WIDTH = w;
    	CAMERA_HEIGHT = h;
    }

    /**
     * Renders.
     */
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
    	batch.setProjectionMatrix(camera.combined);
    	batch.begin();
    	drawPlayer();
        drawStatic();
    	batch.end();
    }

    /**
     * Disposes of textures when process is finished.
     */
    public void dispose() {
    	playerTexture.dispose();
        staticTexture.dispose();
        networking.dispose();
    	batch.dispose();
    }

    /**
     * Loads all required textures in this specific location.
     * <p>
     * Currently WIP.
     */
    private void loadTextures() {
    	playerTexture = new Texture("player.png");
        staticTexture = new Texture("collision.png");
    }

    // I hope Javadoc comments are not needed for functions below...
    /**
     * Draws a player.
     */
    private void drawPlayer() {
    	batch.draw(playerTexture, player.getPosition().x, player.getPosition().y, player.getWidth(), player.getHeight());
        System.out.println(networking.getPlayers().values());
        System.out.println(networking.getPlayers().size());
        for (MPPlayer mpPlayer : networking.getPlayers().values()) {
            batch.draw(playerTexture, mpPlayer.x, mpPlayer.y, player.getWidth(), player.getHeight());
        }
    }

    /**
     * Draws a {@code Static} objects.
     * <p>
     * Currently used for debugging pursoses.
     */
    private void drawStatic() {
        batch.draw(staticTexture, block.getPosition().x, block.getPosition().y, block.getWidth(), block.getHeight());
    }
    //#endregion
}