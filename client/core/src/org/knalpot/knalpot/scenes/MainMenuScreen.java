package org.knalpot.knalpot.scenes;
import org.knalpot.knalpot.Knalpot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;


public class MainMenuScreen implements Screen {
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Knalpot game;
    private Stage stage;

    public MainMenuScreen(Knalpot game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void show() {
        // create a button to switch to the game screen
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // create the game button
        TextButton.TextButtonStyle playStyle = new TextButton.TextButtonStyle();
        playStyle.font = font;
        playStyle.fontColor = Color.WHITE;
        playStyle.overFontColor = Color.GRAY;
        playStyle.downFontColor = Color.LIGHT_GRAY;
        playStyle.up = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("button_up.png")), 0, 0, 0, 0));
        playStyle.over = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("button_over.png")), 0, 0, 0, 0));
        playStyle.down = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("button_down.png")), 0, 0, 0, 0));

        float buttonWidth = Gdx.graphics.getWidth() / 2f;
        float buttonHeight = Gdx.graphics.getHeight() / 4f;
        TextButton gameButton = new TextButton("", playStyle);
        gameButton.setSize(buttonWidth, buttonHeight);
        gameButton.setPosition((Gdx.graphics.getWidth() - buttonWidth) / 2f, (Gdx.graphics.getHeight() - buttonHeight) / 2f);
        gameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.audio.newSound(Gdx.files.internal("start_sound.mp3")).play(1.0f);
                game.setScreen(new GameScene());
            }
        });

        // create the new button
        TextButton.TextButtonStyle exitStyle = new TextButton.TextButtonStyle();
        exitStyle.font = font;
        exitStyle.fontColor = Color.WHITE;
        exitStyle.overFontColor = Color.GRAY;
        exitStyle.downFontColor = Color.LIGHT_GRAY;
        exitStyle.up = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("exit_up.png")), 0, 0, 0, 0));
        exitStyle.over = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("exit_over.png")), 0, 0, 0, 0));
        exitStyle.down = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("exit_down.png")), 0, 0, 0, 0));

        TextButton newButton = new TextButton("", exitStyle);
        newButton.setSize(buttonWidth, buttonHeight);
        newButton.setPosition((Gdx.graphics.getWidth() - buttonWidth) / 2f, (Gdx.graphics.getHeight() - buttonHeight) / 2f - buttonHeight - 10);
        newButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // add both buttons to the stage
        stage.addActor(gameButton);
        stage.addActor(newButton);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        //font.draw(batch, "Main Menu", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        batch.end();

        //if (Gdx.input.isTouched()) {
        //    game.setScreen(new GameScene());
        //    dispose();
        //}

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
        batch.dispose();
        font.dispose();
    }
}
