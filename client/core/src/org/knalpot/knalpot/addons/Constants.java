package org.knalpot.knalpot.addons;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;

public class Constants {
	// GRAPHICS RELATED //
    public final static float WINDOW_WIDTH = Gdx.graphics.getWidth();
    public final static float WINDOW_HEIGHT = Gdx.graphics.getHeight();

    // PHYSICS RELATED //
	public final static float GRAVITY_FORCE = 700f;
	public final static float GRAVITY_ACCEL = 1.5F;

	// KEYBOARD-RELATED CONSTANTS //
	public final static int LEFT_KEY = Input.Keys.LEFT;
    public final static int RIGHT_KEY = Input.Keys.RIGHT;
    public final static int SPACEBAR = Input.Keys.SPACE;

    // temporary keys/
    public final static int UP_KEY = Input.Keys.UP;
    public final static int DOWN_KEY = Input.Keys.DOWN;
}