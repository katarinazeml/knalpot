package org.knalpot.knalpot;

public class WorldProcessor {
	private World world;
	private Player player;
	private PlayerProcessor playerProcessor;

	public WorldProcessor(World world) {
		this.world = world;
		player = world.getPlayer();
		playerProcessor = new PlayerProcessor(world);
	}

	public void update(float dt) {
		playerProcessor.update(dt);
	}
}