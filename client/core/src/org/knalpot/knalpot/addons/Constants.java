<<<<<<< HEAD
package org.knalpot.knalpot.Addons;
=======
package org.knalpot.knalpot.addons;
>>>>>>> 0a8fd2cdaa7f133b1b996dd4f2c1c35e8f593c5c

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