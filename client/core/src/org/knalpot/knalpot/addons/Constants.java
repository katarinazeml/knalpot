package org.knalpot.knalpot.addons;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;

/**
 * {@code Constants} class' main purpose is to house constants that will be used 
 * multiple times throughout the codebase. It is much easier to reuse a constant
 * than search for specific valua in >1000 lines of code codebase.
 * <p>
 * Currently WIP.
 * @author Max Usmanov
 * @version 0.1
 */
public class Constants {
	// ==== WINDOW PARAMETERS ==== //
    public final static float WINDOW_WIDTH = Gdx.graphics.getWidth();
    public final static float WINDOW_HEIGHT = Gdx.graphics.getHeight();

    // ==== PHYSICS ==== //
	public final static float GRAVITY_FORCE = 700f;
	public final static float GRAVITY_ACCEL = 1.5F;

	// ==== KEYBOARD KEYS ==== //
	public final static int LEFT_KEY = Input.Keys.LEFT;
    public final static int RIGHT_KEY = Input.Keys.RIGHT;
    public final static int SPACEBAR = Input.Keys.SPACE;
}