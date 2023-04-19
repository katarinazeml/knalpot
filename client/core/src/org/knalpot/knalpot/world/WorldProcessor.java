package org.knalpot.knalpot.world;

import org.knalpot.knalpot.actors.EnemyProcessor;
import org.knalpot.knalpot.actors.PlayerProcessor;
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
	private PlayerProcessor playerProcessor;
	private EnemyProcessor enemyProcessor;

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
		playerProcessor = new PlayerProcessor(this.world);
		enemyProcessor = new EnemyProcessor(world);
		clientProgram = this.world.getClientProgram();
		clientProgram.create();
	}

	/**
	 * Updates every single 'processor' in the world.
	 * @param dt
	 */
	public void update(float dt) {
		playerProcessor.update(dt);
		enemyProcessor.update(dt);
		clientProgram.updateNetwork();
	}
	//#endregion
}