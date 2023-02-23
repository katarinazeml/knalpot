package org.knalpot.knalpot;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class CollisionBlock {
	private Vector2 position;
	private Rectangle bounds;

	private int WIDTH = 16;
	private int HEIGHT = 16;

	public CollisionBlock(Vector2 position) {
		this.position = position;

		bounds = new Rectangle();
		bounds.x = this.position.x;
		bounds.y = this.position.y;
		bounds.width = WIDTH;
		bounds.height = HEIGHT;
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