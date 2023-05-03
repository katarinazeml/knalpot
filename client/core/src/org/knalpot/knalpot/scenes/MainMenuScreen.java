package org.knalpot.knalpot.scenes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.knalpot.knalpot.Knalpot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;


public class MainMenuScreen implements Screen {
    private final float buttonWidth = Gdx.graphics.getWidth() / 4f;
    private final float buttonHeight = Gdx.graphics.getHeight() / 6f;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Knalpot game;
    private Stage stage;

    public MainMenuScreen(Knalpot game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
        game.getMusic().play();
    }

    @Override
    public void show() {
        // create a button to switch to the game screen
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // create background
        createBackground();

        // create the start button
        createStartButton();

        // create the exit button
        createExitButton();

        // create the settings button
        createSettingsButton();
    }

    @Override
    public void render(float delta) {
        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        // dispose assets
        batch.dispose();
        font.dispose();

    }

    public void createStartButton() {
        // create style
        TextButton.TextButtonStyle startStyle = new TextButton.TextButtonStyle();
        startStyle.font = font;
        startStyle.fontColor = Color.WHITE;
        startStyle.overFontColor = Color.GRAY;
        startStyle.downFontColor = Color.LIGHT_GRAY;
        startStyle.up = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("buttons/start_up.png")), 0, 0, 0, 0));
        startStyle.over = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("buttons/start_over.png")), 0, 0, 0, 0));
        startStyle.down = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("buttons/start_down.png")), 0, 0, 0, 0));

        // create start button
        TextButton startButton = new TextButton("", startStyle);
        startButton.setSize(buttonWidth, buttonHeight);
        startButton.setPosition((Gdx.graphics.getWidth() - buttonWidth) / 2f, (Gdx.graphics.getHeight() - buttonHeight) / 2f + 40);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.audio.newSound(Gdx.files.internal("buttons/start_sound.mp3")).play(1.0f);
                stage.dispose();
                game.getMusic().dispose();
                game.setScreen(new GameScene(game));
            }
        });

        // add start button to the stage
        stage.addActor(startButton);
    }

    public void createExitButton() {
        // create style
        TextButton.TextButtonStyle exitStyle = new TextButton.TextButtonStyle();
        exitStyle.font = font;
        exitStyle.fontColor = Color.WHITE;
        exitStyle.overFontColor = Color.GRAY;
        exitStyle.downFontColor = Color.LIGHT_GRAY;
        exitStyle.up = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("buttons/exit_up.png")), 0, 0, 0, 0));
        exitStyle.over = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("buttons/exit_over.png")), 0, 0, 0, 0));
        exitStyle.down = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("buttons/exit_down.png")), 0, 0, 0, 0));

        // create exit button
        TextButton exitButton = new TextButton("", exitStyle);
        exitButton.setSize(buttonWidth, buttonHeight);
        exitButton.setPosition((Gdx.graphics.getWidth() - buttonWidth) / 2f, (Gdx.graphics.getHeight() - buttonHeight) / 2f - buttonHeight + 20);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.dispose();
                Gdx.app.exit();
            }
        });

        // add exit button to the stage
        stage.addActor(exitButton);
    }

    public void createSettingsButton() {
        // create style
        TextButton.TextButtonStyle settingsStyle = new TextButton.TextButtonStyle();
        settingsStyle.font = font;
        settingsStyle.fontColor = Color.WHITE;
        settingsStyle.overFontColor = Color.GRAY;
        settingsStyle.downFontColor = Color.LIGHT_GRAY;
        settingsStyle.up = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("buttons/settings_up.png")), 0, 0, 0, 0));
        settingsStyle.over = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("buttons/settings_over.png")), 0, 0, 0, 0));
        settingsStyle.down = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("buttons/settings_down.png")), 0, 0, 0, 0));

        // create settings button
        TextButton settingsButton = new TextButton("", settingsStyle);
        float buttonSize = 118f;
        settingsButton.setSize(buttonSize, buttonSize);
        settingsButton.setPosition(Gdx.graphics.getWidth() - buttonSize - 10f, Gdx.graphics.getHeight() - buttonSize - 10f);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.audio.newSound(Gdx.files.internal("buttons/start_sound.mp3")).play(1.0f);
                stage.dispose();
                game.setScreen(new SettingsMenuScreen(game));
            }
        });

        // add settings button to the stage
        stage.addActor(settingsButton);
    }

    public void createBackground() {
        // create background
        Texture backgroundTexture = new Texture(Gdx.files.internal("buttons/background.png"));
        Image background = new Image(backgroundTexture);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // add background to the stage
        stage.addActor(background);
    }
}
