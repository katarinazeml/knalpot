package org.knalpot.knalpot;

import com.badlogic.gdx.math.Vector2;

public class World {
	private Player player;

	public World() {
		initializeWorld();
	}

	public Player getPlayer() {
		return player;
	}

	private void initializeWorld() {
		player = new Player(new Vector2(400, 240));
	}
}