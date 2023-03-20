package org.knalpot.knalpot.interactive;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

/**
 * {@code Static} class is responsible for housing all parameters for
 * static-type objects like collisions. It's mainly used as {@code getter}.
 * @author Max Usmanov
 * @version 0.1
 */
public class Static {
	//#region -- VARIABLES --

	// ==== SIZE AND POSITION ==== //
	private Vector2 position;
	private Rectangle bounds;

	private int WIDTH = 16;
	private int HEIGHT = 16;

	public float Left;
	public float Right;
	public float Bottom;
	public float Top;
	//#endregion

	//#region -- FUNCTIONS --
	/**
	 * {@code Static} constructor.
	 * @param position
	 */
	public Static(Vector2 position) {
		this.position = position;

		bounds = new Rectangle();
		bounds.x = this.position.x;
		bounds.y = this.position.y;
		bounds.width = WIDTH;
		bounds.height = HEIGHT;

		Left = bounds.x;
		Right = bounds.x + bounds.width;
		Bottom = bounds.y;
		Top = bounds.y + bounds.height;
	}

	/**
	 * Returns {@code Static}'s position
	 * @return {@code Vector2} type position
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * Returns {@code Static}'s bounds
	 * @return {@code Rectangle} type bounds.
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * Returns {@code Static}'s width.
	 * @return {@code int} type width.
	 */
	public int getWidth() {
		return WIDTH;
	}

	/**
	 * Returns {@code Static}'s height.
	 * @return {@code int} type height.
	 */
	public int getHeight() {
		return HEIGHT;
	}
	//#endregion
}