package org.knalpot.knalpot.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.knalpot.knalpot.Knalpot;

public class GameLobbyScreen implements Screen {
    private final Knalpot game;
    private Stage stage;
    private final BitmapFont font;

    public GameLobbyScreen(Knalpot game) {
        this.game = game;
        font = new BitmapFont();
    }

    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // create title label
        createTitleLabel();

        // create message label
        createMessageLabel();

        // create code text field
        createCodeTextField();
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void pause() {}
    public void resume() {}
    public void hide() {}
    public void dispose() {
        font.dispose();
    }

    private void createTitleLabel() {
        // create title label
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(font, null);
        titleLabelStyle.fontColor = Color.WHITE;
        Label titleLabel = new Label("LOBBY", titleLabelStyle);
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(2f);
        titleLabel.setPosition(0, Gdx.graphics.getHeight() - 100, Align.center);
        titleLabel.setSize(1325, 100);

        // add title label to the stage
        stage.addActor(titleLabel);
    }

    private void createMessageLabel() {
        // create title label
        Label.LabelStyle messageLabelStyle = new Label.LabelStyle(font, null);
        messageLabelStyle.fontColor = Color.WHITE;
        Label messageLabel = new Label("Enter the code to play with friend!", messageLabelStyle);
        messageLabel.setAlignment(Align.center);
        messageLabel.setFontScale(2f);
        messageLabel.setPosition(80, Gdx.graphics.getHeight() - 150, Align.center);
        messageLabel.setSize(1325, 0);

        // add title label to the stage
        stage.addActor(messageLabel);
    }

    private void createCodeTextField() {
        // create text field style
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.BLACK;

        // set background property of the text field style to the white drawable
        textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/white.png"))));

        // set the font size of the text field style
        textFieldStyle.font.getData().setScale(2.0f);

        // create code text field
        TextField codeTextField = new TextField("", textFieldStyle);
        codeTextField.setMessageText("Enter code!");
        codeTextField.setAlignment(Align.center);
        codeTextField.setSize(200, 50);
        codeTextField.setPosition(Gdx.graphics.getWidth() / 2f - 97, Gdx.graphics.getHeight() / 2f);

        // set maximum length of the text field to 6 characters
        codeTextField.setMaxLength(6);

        codeTextField.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (character == '\b' || character == '\r' || character == '\n') { // delete, enter or return keys
                    if (codeTextField.getText().length() > 0) {
                        codeTextField.setText(codeTextField.getText().substring(0, codeTextField.getText().length() - 1));
                    }
                    return true;
                } else if (Character.isLowerCase(character)) {
                    codeTextField.setText(codeTextField.getText().replaceAll("[a-z]", "") + Character.toUpperCase(character));
                    return true;
                }
                return false;
            }
        });


        // add code text field to the stage
        stage.addActor(codeTextField);
    }
}
