package org.knalpot.knalpot;

public class WorldProcessor {
	private World world;
	private Player player;
	private PlayerProcessor playerProcessor;

	private CollisionBlock block;

	public WorldProcessor(World world) {
		this.world = world;
		player = world.getPlayer();
		block = world.getCollisionBlocks();
		playerProcessor = new PlayerProcessor(world);
	}

	public void update(float dt) {
		playerProcessor.update(dt);
	}
}