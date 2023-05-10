package org.knalpot.knalpot.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.knalpot.knalpot.Knalpot;

public class GameOverScreen implements Screen {
    private final Knalpot game;

    private BitmapFont font;
    private Stage stage;
    private Batch batch;

    private float timeElapsed = 0f;
    private boolean showMenu = false;
    public GameOverScreen(Knalpot game) {
        this.game = game;
        font = new BitmapFont();
        font.getData().setScale(2f); // Set font size to 2 times the default size
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        batch = new SpriteBatch();
        game.getMusic().pause();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        // Update the timer
        timeElapsed += delta;
        float timeRemaining = 10f - timeElapsed;
        
        // Draw the remaining time
        String time = "Time Remaining: " + String.format("%.1f", timeRemaining);
        String gameOver = "GAME OVER";
        String message = "You will be taken to the menu";
        batch.begin();
        font.draw(batch, time, 500, 400);
        font.draw(batch, gameOver, 560, 600);
        font.draw(batch, message, 450, 500);
        batch.end();

        // Check if 10 seconds have passed
        if (!showMenu && timeElapsed >= 10f) {
            showMenu = true;
            dispose();
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(this.game));
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
    }
}

