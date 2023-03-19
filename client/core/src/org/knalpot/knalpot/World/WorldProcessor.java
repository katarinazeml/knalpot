package org.knalpot.knalpot.World;

import org.knalpot.knalpot.Interactive.CollisionBlock;
import org.knalpot.knalpot.Player.Player;
import org.knalpot.knalpot.Player.PlayerProcessor;

import java.net.Socket;

public class WorldProcessor {
	private World world;
	private Player player;
	private PlayerProcessor playerProcessor;

	private CollisionBlock block;

	private Socket socket;


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
