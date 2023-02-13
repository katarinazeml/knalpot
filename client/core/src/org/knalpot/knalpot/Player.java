package org.knalpot.knalpot;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class Player {

    private OrthographicCamera camera;
    private static final int CAMERA_WIDTH = 200;
    private static final int CAMERA_HEIGHT = 120;

    // SPRITE-RELATED //
    private SpriteBatch playerBatch;
    private Texture playerTexture;
    private Rectangle player;

    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;

    // KEYBOARD INPUT //
    private final static int LEFT_KEY = Input.Keys.LEFT;
    private final static int RIGHT_KEY = Input.Keys.RIGHT;

    // INITIALIZE VARIABLES WHEN CLASS IS CREATED //
    public Player(String spriteName) {
        camera = new OrthographicCamera();
        playerBatch = new SpriteBatch();
        playerTexture = new Texture(spriteName);
        player = new Rectangle();

        camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);

        player.width = WIDTH;
        player.height = HEIGHT;

        player.x = CAMERA_WIDTH / 2 - WIDTH / 2;
        player.y = CAMERA_HEIGHT / 2 - HEIGHT / 2;
    }

    private void update() {
        renderPlayer();
        movePlayer();
    }

    private void renderPlayer() {
        camera.update();
        playerBatch.setProjectionMatrix(camera.combined);
        playerBatch.begin();
        playerBatch.draw(playerTexture, player.x, player.y);
        playerBatch.end();
    }

    public void disposePlayer() {
        playerTexture.dispose();
    }

    private void movePlayer() {
        if (Gdx.input.isKeyPressed(LEFT_KEY)) player.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(RIGHT_KEY)) player.x += 200 * Gdx.graphics.getDeltaTime();
    }
}