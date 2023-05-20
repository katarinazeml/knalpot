package org.knalpot.knalpot.addons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.knalpot.knalpot.actors.Enemy;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.knalpot.knalpot.actors.Actor;
import org.knalpot.knalpot.actors.orb.Orb;
import org.knalpot.knalpot.actors.player.Player;
import org.knalpot.knalpot.actors.player.Player.State;
import org.knalpot.knalpot.networking.ClientProgram;
import org.knalpot.knalpot.networking.MPActor;
import org.knalpot.knalpot.world.World;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;


/**
 * {@code Renderer} class renders all textures in the specified location.
 * It is crucial that {@code Renderer} class is called with either a list
 * or map of textures required to load.
 * <p>
 * Currently WIP, as it utilizes 'I say so' workflow. Must be modular.
 * @author Max Usmanov
 * @version 0.2
 */
public class Renderer {
    //#region -- VARIABLES --

    // Temporary
    private String tiledSrc = "level1/test.tmx";
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
    private Texture bulletTexture;

    private World world;
    private Player player;
    private List<Orb> orbs;

    // ==== MOUSE MANIPULATION ==== //
    private Vector3 mousePos;

    // ==== NETWORKING ==== //
    private ClientProgram networking;

	// ==== CAMERA ==== //
    public static OrthographicCamera camera;
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

    private BitmapFont labelFont;

    //#endregion

    //#region -- FUNCTIONS --

    /**
     * {@code Renderer} constructor.
     * @param world
     */
    public Renderer(World world) {
    	this.world = world;

        // Initializing Vector2 with mouse coordinates
        mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        // Create and setup camera.
    	camera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT * (WH / WW));
        camera.unproject(mousePos);
        camera.update();

        // Initialize spritebatch.
        batch = new SpriteBatch();
        player = this.world.getPlayer();
        orbs = this.world.getOrbs();
        networking = this.world.getClientProgram();
        ((Orb) orbs.get(0)).setMousePos(mousePos);

        // Load other objects' textures.
        loadTextures();
        loadTiledMap();
        world.addTeleports(batch);

        labelFont = new BitmapFont();
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
        // ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClearColor(0, 0, 0, 1);

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

        mousePos.x = Gdx.input.getX();
        mousePos.y =  Gdx.input.getY();

        camera.unproject(mousePos);

    	batch.setProjectionMatrix(camera.combined);
        // Draw background
    	batch.begin();
        drawBackground(targetX);
    	batch.end();

        tiledRender.setView(camera);
        tiledRender.render();
        
        // Draw teleport animation
        batch.begin();
        world.getTeleports().forEach(e -> e.render());
        world.getChest().forEach(e -> e.render(batch));
        orbs.forEach(e -> e.render(batch));
    	batch.end();

        // Draw player
        batch.begin();
    	drawPlayer();
        batch.end();

        // Draw label
        batch.begin();
        String labelText = player.getHealth() + " / 100";
        labelFont.draw(batch, labelText, player.getPosition().x - player.getWidth() / 2, player.getPosition().y + player.getHeight() + 10);
        batch.end();

        // Draw enemy
        batch.begin();
        for (Enemy enemy : world.getEnemies()) {
            drawEnemy(enemy);
        }
        batch.end();

        // Draw MPBullets
        batch.begin();
        drawBullets();
        batch.end();

        // Draw HUD
        ((Player) player).getHud().render();
        if (world.getChest().size() != 0)
            world.getChest().get(((Player) player).chestIndex).getHUD().render();
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
        world.getTeleports().forEach(e -> e.swooshSound.dispose());
        enemyTexture.dispose();
    }

    /**
     * Loads tilemap.
     */
    private void loadTiledMap() {
        tiledMap = new TmxMapLoader().load(tiledSrc);
        tiledRender = new OrthogonalTiledMapRenderer(tiledMap, 2);
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
        bulletTexture = new Texture("bullet.png");

        Texture cloudTexture = new Texture("CloudsGrassWallpaperCloud.png");
        Texture darkGrassTexture = new Texture("DarkGrass.png");
        Texture lightGrassTexture = new Texture("LightGrass.png");

        Texture spriteSheet = new Texture("playeranimation.png");
        playerRegion = new TextureRegion(spriteSheet, 0, 0, frameWidth, frameHeight);

        //Texture enemySpritesheet = new Texture("enemyanimation.png");
        //enemyRegion = new TextureRegion(enemySpriteSheet, 0, 0, frameWidth, frameHeight);

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

        if (player.state == State.MOVE) {
            batch.draw(playerTextureRun, positionX, player.getPosition().y,
                Math.signum(player.direction) * (frameWidth * player.getScale()), (frameHeight * player.getScale()), offsetX, 0, frameWidth, frameHeight, false, false);
        }
        if (player.state != State.MOVE) {
            batch.draw(playerTexture, positionX, player.getPosition().y, Math.signum(player.direction) * player.getWidth(), player.getHeight());
        }

        for (Actor mpPlayer : networking.getPlayers().values()) {
            float mpPositionX = 0;
            if (mpPlayer.direction == 1) mpPositionX = mpPlayer.getPosition().x;
            if (mpPlayer.direction == -1) mpPositionX = mpPlayer.getPosition().x + player.getWidth() / player.getScale();
            
            if (mpPlayer.state != State.IDLE) {
                batch.draw(playerTextureRun, mpPositionX, mpPlayer.getPosition().y,
                Math.signum(mpPlayer.direction) * (frameWidth * player.getScale()), (frameHeight * player.getScale()), offsetX, 0, frameWidth, frameHeight, false, false);
            } else {
                batch.draw(playerTexture, mpPositionX, mpPlayer.getPosition().y, Math.signum(mpPlayer.direction) * player.getWidth(), player.getHeight());
            }
        }
    }

    public void drawBullets() {
        System.out.println("Client bullets size");
        System.out.println(ClientProgram.bullets.size());
        for (MPActor bullet : ClientProgram.bullets.values()) {
            batch.draw(bulletTexture, bullet.x, bullet.y, bulletTexture.getWidth() * 2, bulletTexture.getHeight() * 2);
        }
    }


    private void drawEnemy(Enemy enemy) {
         float enemyPositionX = enemy.getPosition().x;
         if (enemy.getEnemyDirection() == -1) {
             batch.draw(enemyTexture, enemyPositionX, enemy.getPosition().y);
         }
         if (enemy.getEnemyDirection() == 1) {
             batch.draw(enemyTexture, enemyPositionX + enemy.getWidth(), enemy.getPosition().y, -enemyTexture.getWidth(), enemyTexture.getHeight());
         } 
    }
            // put - in front of width to reverse player.
        //     float positionX = 0;

        //     // Animation.
        //     animationTime += Gdx.graphics.getDeltaTime();
        //     int frameIndex = (int) (animationTime / frameDuration) % numFrames;
        //     int offsetX = frameIndex * frameWidth;
        //     Texture enemyTextureRun = enemyRegion.getTexture();
            
        //     // General logic.
        //     if (enemy.direction == 1) {
        //         positionX = player.getPosition().x;
        //     }
        //     if (enemy.direction == -1) {
        //         positionX = player.getPosition().x + player.getWidth() / player.getScale();
        //     }
    
        //     if (enemy.getState() != enemy.EnemyState.IDLE) {
        //         batch.draw(enemyTextureRun, positionX, enemy.getPosition().y,
        //             Math.signum(enemy.direction) * (frameWidth * enemy.getScale()), (frameHeight * enemy.getScale()), offsetX, 0, frameWidth, frameHeight, false, false);
        //     } else {
        //         batch.draw(enemyTexture, positionX, enemy.getPosition().y, Math.signum(enemy.direction) * enemy.getWidth(), enemy.getHeight());
        //     }
        // }    
    //#endregion
}
