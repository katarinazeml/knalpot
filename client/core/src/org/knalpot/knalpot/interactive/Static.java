package org.knalpot.knalpot.interactive;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
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
	protected Vector2 position;
	protected Rectangle bounds;

	public float Left;
	public float Right;
	public float Bottom;
	public float Top;

	// ==== SAVING TEXTURE ==== //
	protected Texture texture;
	//#endregion

	//#region -- FUNCTIONS --
	/**
	 * {@code Static} constructor.
	 * @param position
	 */
	public Static(Vector2 position, int width, int height) {
		this.position = position;

		bounds = new Rectangle();
		bounds.x = this.position.x;
		bounds.y = this.position.y;
		bounds.width = width;
		bounds.height = height;

		Left = bounds.x;
		Right = bounds.x + bounds.width;
		Bottom = bounds.y;
		Top = bounds.y + bounds.height;
	}

	public Static(Vector2 position, int width, int height, Texture texture) {
		this.position = position;
		this.texture = texture;

		bounds = new Rectangle();
		bounds.x = this.position.x;
		bounds.y = this.position.y;
		bounds.width = width;
		bounds.height = height;

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
	 * Sets position of {@link org.knalpot.knalpot.interactive.Static Static}.
	 * Is used for moving the object around the world or HUD.
	 * @param position
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}

	/**
	 * Sets position of {@link org.knalpot.knalpot.interactive.Static Static}
	 * using X and Y coordinates.
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}

	/**
	 * Changes the position according to the given values.
	 * This helps move object independently from its current Vector2.
	 * @param x
	 * @param y
	 */
	public void changePosition(float x, float y, float dt) {
		Vector2 velocity = new Vector2(x, y);
		position.add(velocity.scl(dt));
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
		return (int) bounds.width;
	}

	/**
	 * Returns {@code Static}'s height.
	 * @return {@code int} type height.
	 */
	public int getHeight() {
		return (int) bounds.height;
	}
	//#endregion
}