package org.knalpot.knalpot;

import com.badlogic.gdx.math.Vector2;

public class World {
	private Player player;
	private CollisionBlock block1;

	public World() {
		initializeWorld();
	}

	public Player getPlayer() {
		return player;
	}

	public CollisionBlock getCollisionBlocks() {
		return block1;
	}

	private void initializeWorld() {
		player = new Player(new Vector2(400, 240));
		block1 = new CollisionBlock(new Vector2(450, 20));
	}
}