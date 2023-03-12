package org.knalpot.knalpot.interactive;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class CollisionBlock {
	private Vector2 position;
	private Rectangle bounds;

	private int WIDTH = 16;
	private int HEIGHT = 16;

	public float Left;
	public float Right;
	public float Bottom;
	public float Top;

	public CollisionBlock(Vector2 position) {
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

	public Vector2 getPosition() {
		return position;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}
}