package org.knalpot.knalpot.Addons;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;

public class Constants {
	// GRAPHICS RELATED //
    public static float WINDOW_WIDTH = Gdx.graphics.getWidth();
    public static float WINDOW_HEIGHT = Gdx.graphics.getHeight();

    // PHYSICS RELATED //
	public static final float GRAVITY_FORCE = 700f;
	public static final float GRAVITY_ACCEL = 1.5F;

	// KEYBOARD-RELATED CONSTANTS //
	public final static int LEFT_KEY = Input.Keys.LEFT;
    public final static int RIGHT_KEY = Input.Keys.RIGHT;
    public final static int SPACEBAR = Input.Keys.SPACE;
}