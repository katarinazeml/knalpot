package org.knalpot.knalpot.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.knalpot.knalpot.Knalpot;

public class SettingsMenuScreen implements Screen {

    private final Knalpot game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private Stage stage;
    private final float buttonWidth = Gdx.graphics.getWidth() / 4f;
    private final float buttonHeight = Gdx.graphics.getHeight() / 6f;
    private Slider volumeSlider;
    private Label volumeLabel;

    public SettingsMenuScreen(Knalpot game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // create background
        createBackground();

        // create the back button
        createBackButton();

        // create volume slider
        createVolumeSlider();

        // create title label
        createTitleLabel();
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

    public void createBackground() {
        // create background
        Texture backgroundTexture = new Texture(Gdx.files.internal("buttons/background.png"));
        Image background = new Image(backgroundTexture);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // add background to the stage
        stage.addActor(background);
    }

    public void createBackButton() {
        // create style
        TextButton.TextButtonStyle backStyle = new TextButton.TextButtonStyle();
        backStyle.font = font;
        backStyle.fontColor = Color.WHITE;
        backStyle.overFontColor = Color.GRAY;
        backStyle.downFontColor = Color.LIGHT_GRAY;
        backStyle.up = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("buttons/back_up.png")), 0, 0, 0, 0));
        backStyle.over = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("buttons/back_over.png")), 0, 0, 0, 0));
        backStyle.down = new NinePatchDrawable(new NinePatch(new TextureRegion(new Texture("buttons/back_down.png")), 0, 0, 0, 0));

        // create back button
        TextButton backButton = new TextButton("", backStyle);
        backButton.setSize(buttonWidth, buttonHeight);
        backButton.setPosition((Gdx.graphics.getWidth() - buttonWidth) / 2f, (Gdx.graphics.getHeight() - buttonHeight) / 2f - buttonHeight + 20);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.audio.newSound(Gdx.files.internal("buttons/start_sound.mp3")).play(1.0f);
                game.setScreen(new MainMenuScreen(game));
                stage.dispose();
            }
        });

        // add exit button to the stage
        stage.addActor(backButton);
    }

    private void createTitleLabel() {
        // create title label
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(font, null);
        titleLabelStyle.fontColor = Color.WHITE;
        Label titleLabel = new Label("Settings", titleLabelStyle);
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(2f);
        titleLabel.setPosition(0, Gdx.graphics.getHeight() - 100, Align.center);
        titleLabel.setSize(1325, 100);

        // add title label to the stage
        stage.addActor(titleLabel);
    }

    public void createVolumeSlider() {
        // create slider
        volumeSlider = new Slider(0f, 1f, 0.1f, false, new Slider.SliderStyle(
                new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/slider_background.png")))),
                new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/slider_knob.png"))))
        ));
        // set slider position to center of the screen
        volumeSlider.setPosition((Gdx.graphics.getWidth() - volumeSlider.getWidth()) / 2f - 375, (Gdx.graphics.getHeight() - volumeSlider.getHeight()) / 2f);

        volumeSlider.setWidth(Gdx.graphics.getWidth() * 0.7f);
        volumeSlider.setValue(game.getVolume());

        // create label to display current volume value
        Label.LabelStyle volumeLabelStyle = new Label.LabelStyle(font, null);
        volumeLabelStyle.fontColor = Color.WHITE;
        volumeLabel = new Label("Volume: " + (int)(game.getVolume() * 100) + "%", volumeLabelStyle);
        volumeLabel.setAlignment(Align.center);
        volumeLabel.setPosition(Gdx.graphics.getWidth() / 2f - 50, volumeSlider.getY() + volumeSlider.getHeight() + 10, Align.center);
        volumeLabel.setSize(150, 50);

        // add listener to slider that updates volume label and sets volume level
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = volumeSlider.getValue();
                game.setVolume(volume);
                game.getMusic().setVolume(volume);
                volumeLabel.setText("Volume: " + (int)(volume * 100) + "%");
            }
        });

        // add slider and label to the stage
        stage.addActor(volumeSlider);
        stage.addActor(volumeLabel);
    }
}
