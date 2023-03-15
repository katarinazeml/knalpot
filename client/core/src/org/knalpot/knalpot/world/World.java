package org.knalpot.knalpot.world;

import org.knalpot.knalpot.actors.Player;
import org.knalpot.knalpot.interactive.CollisionBlock;

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
		player = new Player(new Vector2(350, 200));
		block1 = new CollisionBlock(new Vector2(400, 25));
	}
}