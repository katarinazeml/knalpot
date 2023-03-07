package org.knalpot.knalpot.world;

import org.knalpot.knalpot.actors.PlayerProcessor;

public class WorldProcessor {
	private World world;
	private PlayerProcessor playerProcessor;

	public WorldProcessor(World world) {
		this.world = world;
		playerProcessor = new PlayerProcessor(this.world);
	}

	public void update(float dt) {
		playerProcessor.update(dt);
	}
}