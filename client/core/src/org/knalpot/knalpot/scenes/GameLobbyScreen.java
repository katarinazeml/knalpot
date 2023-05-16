package org.knalpot.knalpot.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.knalpot.knalpot.Knalpot;

public class GameLobbyScreen implements Screen {
    private final Knalpot game;
    private Stage stage;
    private final BitmapFont font;
    private final float buttonWidth = Gdx.graphics.getWidth() / 4f;
    private final float buttonHeight = Gdx.graphics.getHeight() / 6f;

    public GameLobbyScreen(Knalpot game) {
        this.game = game;
        font = new BitmapFont();
    }

    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // create background
        createBackground();

        // create title label
        createTitleLabel();

        // create the back button
        createBackButton();

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

    private void createBackButton() {
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
                stage.dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // add exit button to the stage
        stage.addActor(backButton);
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
                        codeTextField.setText(codeTextField.getText());
                    }
                    return true;
                } else if (Character.isLowerCase(character)) {
                    // to keep track of cursor position
                    int cursorPosition = codeTextField.getCursorPosition(); // store cursor position
                    String textBeforeCursor = codeTextField.getText().substring(0, cursorPosition);
                    String textAfterCursor = codeTextField.getText().substring(cursorPosition);
                    int letterPosition = textBeforeCursor.length() - textBeforeCursor.replaceAll("[a-z]", "").length();
                    String newText = textBeforeCursor.replaceAll("[a-z]", "") + Character.toUpperCase(character) + textAfterCursor;
                    codeTextField.setText(newText);
                    codeTextField.setCursorPosition(cursorPosition + 1 - letterPosition); // set new cursor position
                    return true;
                }
                return false;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    String code = codeTextField.getText();
                    System.out.println("Entered code: " + code);
                    stage.dispose();
                    game.getMusic().dispose();
                    game.setScreen(new GameScene(game));
                    return true;
                }
                return false;
            }
        });

        // add code text field to the stage
        stage.addActor(codeTextField);
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
