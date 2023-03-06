package org.knalpot.knalpot.Addons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import org.knalpot.knalpot.Interactive.CollisionBlock;
import org.knalpot.knalpot.Player.Player;
import org.knalpot.knalpot.World.World;
import org.knalpot.knalpot.World.WorldProcessor;

public class Renderer {
	// CAMERA OBJECT //
    private OrthographicCamera camera;
    private static int CAMERA_WIDTH = 400;
    private static int CAMERA_HEIGHT = 400;

    float WW = Constants.WINDOW_WIDTH;
   	float WH = Constants.WINDOW_HEIGHT;

    // OTHER STUFF PLEASE DON'T JUDGE ME //
    private SpriteBatch batch;
    private Texture playerTexture;
    private Texture collisionTexture;

    private World world;
   	private WorldProcessor processor;
    private Player player;

    private CollisionBlock block;

    public Renderer(World world) {
    	this.world = world;

        // Create and setup camera.
    	camera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT * (WH / WW));
        camera.position.set(WW / 2, 120, 0);
        camera.update();

        // Initialize spritebatch.
        batch = new SpriteBatch();
        player = world.getPlayer();
        block = world.getCollisionBlocks();

        // Load other object textures.
        loadTextures();
    }

    public void setCameraSize(int w, int h) {
    	CAMERA_WIDTH = w;
    	CAMERA_HEIGHT = h;
    }

    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
    	batch.setProjectionMatrix(camera.combined);
    	batch.begin();
    	drawPlayer();
        drawCollisionBlocks();
    	batch.end();
    }

    public void dispose() {
    	playerTexture.dispose();
        collisionTexture.dispose();
    	batch.dispose();
    }

    private void loadTextures() {
    	playerTexture = new Texture("player.png");
        collisionTexture = new Texture("collision.png");
    }

    private void drawPlayer() {
    	batch.draw(playerTexture, player.getPosition().x, player.getPosition().y, player.getWidth(), player.getHeight());
    }

    private void drawCollisionBlocks() {
        batch.draw(collisionTexture, block.getPosition().x, block.getPosition().y, block.getWidth(), block.getHeight());
    }
}