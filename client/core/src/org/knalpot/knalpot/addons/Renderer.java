package org.knalpot.knalpot.addons;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.knalpot.knalpot.actors.Actor;
import org.knalpot.knalpot.actors.Enemy;
import org.knalpot.knalpot.actors.Player.State;
import org.knalpot.knalpot.networking.ClientProgram;
import org.knalpot.knalpot.networking.MPPlayer;
import org.knalpot.knalpot.world.World;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


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

    // Temporary
    private String tiledSrc = "level1/simpleLevel.tmx";
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledRender;

    // Define variables for player animation
    private TextureRegion playerRegion;
    private int frameWidth = 24; // The width of each frame in pixels
    private int frameHeight = 24; // The height of each frame in pixels
    private int numFrames = 8; // The number of frames in the sprite sheet
    private float animationTime = 0; // The time elapsed since the animation started
    private float frameDuration = 0.1f; // The duration of each frame in seconds


    // ==== OBJECTS ==== //
    private SpriteBatch batch;
    private Texture playerTexture;
    private Texture staticTexture;

    private World world;
    private Actor player;
    private Enemy enemy;

    private Teleport teleport;

    // ==== NETWORKING ==== //
    private ClientProgram networking;

	// ==== CAMERA ==== //
    private OrthographicCamera camera;
    private static int CAMERA_WIDTH = 400;
    private static int CAMERA_HEIGHT = 400;
    private static final float CAMERA_SPEED = 5.0f;


    // ==== SHORTCUTS ==== //
    float WW = Constants.WINDOW_WIDTH;
   	float WH = Constants.WINDOW_HEIGHT;

     // ==== PARALLAX ==== //
    private Texture sky;

    private ParallaxLayer cloud;
    private ParallaxLayer darkGrass;
    private ParallaxLayer lightGrass;
    private Texture enemyTexture;
       
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
        camera.update();

        // Initialize spritebatch.
        batch = new SpriteBatch();
        player = this.world.getPlayer();
        enemy = this.world.getEnemy();
        networking = this.world.getClientProgram();

        // Load other objects' textures.
        loadTextures();
        loadTiledMap();
        teleport = new Teleport(20, 48, 800, 176, batch);
    }

    /**
     * Sets {@code OrthographicCamera} size, obviously.
     * @param w
     * @param h
     */
    public void setCameraSize(int w, int h) {
    	// CAMERA_WIDTH = w;
    	// CAMERA_HEIGHT = h;
        camera.viewportWidth = w;
        camera.viewportHeight = h;
        camera.update();
    }

    /**
     * Renders.
     */
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        
        // Calculate the target position for the camera.
        float targetX = player.getPosition().x + player.getWidth() / 2;
        float targetY = player.getPosition().y + player.getHeight() / 2;

        // Interpolate the camera's position towards the target position.
        float dx = targetX - camera.position.x;
        float dy = targetY - camera.position.y;
        camera.position.x += dx * CAMERA_SPEED * Gdx.graphics.getDeltaTime();
        camera.position.y += dy * CAMERA_SPEED * Gdx.graphics.getDeltaTime();

        // Adjust the camera's position to place the lowest point of the screen at (0,0) coordinate
        float minX = camera.viewportWidth / 2;
        float minY = camera.viewportHeight / 2;
        camera.position.x = Math.max(camera.position.x, minX);
        camera.position.y = Math.max(camera.position.y, minY);

        camera.update();

    	batch.setProjectionMatrix(camera.combined);
        // Draw background
    	batch.begin();
        drawBackground(targetX);
    	batch.end();
        
        // Draw teleport animation
        batch.begin();
        teleport.render();
        batch.end();

        tiledRender.setView(camera);
        tiledRender.render();

        // Draw player
        batch.begin();
    	drawPlayer();
        batch.end();

        //Draw enemy
        batch.begin();
        drawEnemy();
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
        sky.dispose();
        teleport.swooshSound.dispose();
        enemyTexture.dispose();
    }

    /**
     * Loads tilemap.
     */
    private void loadTiledMap() {
        tiledMap = new TmxMapLoader().load(tiledSrc);
        tiledRender = new OrthogonalTiledMapRenderer(tiledMap, 2);
    }

    /**
     * Loads all required textures in this specific location.
     * <p>
     * Currently WIP.
     */
    private void loadTextures() {
    	playerTexture = new Texture("player.png");
        staticTexture = new Texture("collision.png");
        enemyTexture = new Texture("lavamonster.png");
        sky = new Texture("CloudsGrassWallpaperSky.png");

        Texture cloudTexture = new Texture("CloudsGrassWallpaperCloud.png");
        Texture darkGrassTexture = new Texture("DarkGrass.png");
        Texture lightGrassTexture = new Texture("LightGrass.png");

        Texture spriteSheet = new Texture("playeranimation.png");
        playerRegion = new TextureRegion(spriteSheet, 0, 0, frameWidth, frameHeight);

        //Texture spriteSheet1 = new Texture("teleportanimation.png");
        //teleportRegion = new TextureRegion(spriteSheet1);

        cloud = new ParallaxLayer(cloudTexture, camera, 0.5f, 0.25f);
        darkGrass = new ParallaxLayer(darkGrassTexture, camera, 0.2f, 0.4f);
        lightGrass = new ParallaxLayer(lightGrassTexture, camera, 0.2f, 0.2f);
    }

    // I hope Javadoc comments are not needed for functions below...
    /**
     * Draws a player.
     */
    private void drawPlayer() {
        // put - in front of width to reverse player.
        float positionX = 0;

        // Animation.
        animationTime += Gdx.graphics.getDeltaTime();
        int frameIndex = (int) (animationTime / frameDuration) % numFrames;
        int offsetX = frameIndex * frameWidth;
        Texture playerTextureRun = playerRegion.getTexture();
        
        // General logic.
        if (player.direction == 1) {
            positionX = player.getPosition().x;
        }
        if (player.direction == -1) {
            positionX = player.getPosition().x + player.getWidth() / player.getScale();
        }

        if (player.state != State.IDLE) {
            batch.draw(playerTextureRun, positionX, player.getPosition().y,
                Math.signum(player.direction) * (frameWidth * player.getScale()), (frameHeight * player.getScale()), offsetX, 0, frameWidth, frameHeight, false, false);
        } else {
            batch.draw(playerTexture, positionX, player.getPosition().y, Math.signum(player.direction) * player.getWidth(), player.getHeight());
        }

        for (MPPlayer mpPlayer : networking.getPlayers().values()) {
            float mpPositionX = 0;
            if (mpPlayer.direction == 1) mpPositionX = mpPlayer.x;
            if (mpPlayer.direction == -1) mpPositionX = mpPlayer.x + player.getWidth() / player.getScale();

            System.out.println("MPPlayer State:");
            System.out.println(mpPlayer.state);

            if (mpPlayer.state != State.IDLE) {
                batch.draw(playerTextureRun, mpPositionX, mpPlayer.y,
                    Math.signum(mpPlayer.direction) * (frameWidth * player.getScale()), (frameHeight * player.getScale()), offsetX, 0, frameWidth, frameHeight, false, false);
            } else {
                batch.draw(playerTexture, mpPositionX, mpPlayer.y, Math.signum(mpPlayer.direction) * player.getWidth(), player.getHeight());
            }
        }
    }
    

    private void drawBackground(float targetX) {
        // Calculate the positions of the backgrounds
        float cameraX = camera.position.x - camera.viewportWidth / 2;
        float cameraY = camera.position.y - camera.viewportHeight / 2;

        // Draw the backgrounds
        batch.draw(sky, cameraX, cameraY, camera.viewportWidth, camera.viewportHeight);
        cloud.render(batch, targetX, 0);
        darkGrass.render(batch, targetX, 0);
        lightGrass.render(batch, targetX, 0);

        //batch.draw(new Texture("level1/rocks.png"), 300, 128, 64, 64);
        //batch.draw(new Texture("level1/tree1.png"), 500, 256, 56 * 3, 77 * 3);
        //batch.draw(new Texture("level1/tree2.png"), 200, 128,26 * 3, 41 * 3);
        //batch.draw(new Texture("level1/rocks.png"), 483, 192, 64, 64);
    }

    private void drawEnemy() {
        float enemyPositionX = 0;
        if (enemy.enemyDirection == -1) {
            enemyPositionX = enemy.getPosition().x;
            batch.draw(enemyTexture, enemyPositionX, enemy.getPosition().y);
        }
        if (enemy.enemyDirection == 1) {
            enemyPositionX = enemy.getPosition().x + enemy.getWidth() / 2f;
            batch.draw(enemyTexture, enemyPositionX, enemy.getPosition().y, -enemyTexture.getWidth(), enemyTexture.getHeight());
        }
    }
    
    //#endregion
}
