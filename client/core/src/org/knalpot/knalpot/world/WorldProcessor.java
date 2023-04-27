package org.knalpot.knalpot.world;

import org.knalpot.knalpot.actors.player.Player;
import org.knalpot.knalpot.actors.player.PlayerProcessor;
import org.knalpot.knalpot.networking.ClientProgram;

/**
 * {@code WorldProcessor} is responsible for updating every single 
 * processor in the {@code World}. It's basically the starting point
 * for updating each frame (which might be a bad idea, is it?).
 * @author Max Usmanov
 * @version 0.1
 */
public class WorldProcessor {
	//#region -- VARIABLES --
	private World world;
	private Player player;
	private PlayerProcessor playerProcessor;

	// ==== NETWORKING ==== //
	private ClientProgram clientProgram;
	//#endregion

	//#region -- FUNCTIONS --
	/**
	 * {@code WorldProcessor} constructor.
	 * @param world
	 */
	public WorldProcessor(World world) {
		this.world = world;
		this.player = (Player) this.world.getPlayer();
		playerProcessor = new PlayerProcessor(this.world);
		clientProgram = this.world.getClientProgram();
		clientProgram.create();
	}

	/**
	 * Updates every single 'processor' in the world.
	 * @param dt
	 */
	public void update(float dt) {
		playerProcessor.update(dt);
		world.getOrb().update(dt);

		clientProgram.updateNetwork();
	}
	//#endregion
}