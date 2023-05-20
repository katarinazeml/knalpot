package org.knalpot.knalpot.world;

import java.util.ArrayList;
import java.util.List;

import org.knalpot.knalpot.actors.Enemy;
import org.knalpot.knalpot.actors.EnemyProcessor;
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

	private List<EnemyProcessor> enemyProcessors;

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

		enemyProcessors = new ArrayList<>();
		this.world.getEnemies().forEach(e -> {
			enemyProcessors.add(new EnemyProcessor(this.world, e));
		});

		clientProgram = this.world.getClientProgram();
		clientProgram.create();
	}

	/**
	 * Checks if the player is dead or not.
	 * If it is, then the scenery must be changed.
	 * @return boolean
	 */
	public boolean isPlayerDead() {
		return player.getHealth() <= 0;
	}

	public List<EnemyProcessor> enemyProcessors() {
		return enemyProcessors;
	}

	/**
	 * Updates every single 'processor' in the world.
	 * @param dt
	 */
	public void update(float dt) {
		playerProcessor.update(dt);

		world.getOrbs().forEach(e -> e.update(dt));
		enemyProcessors.forEach(e -> e.update(dt));

		ArrayList<Enemy> removedEnemies = new ArrayList<>(); // Create a new list to store removed enemies

		for (Enemy enemy : world.getEnemies()) {
			if (enemy.getHealth() <= 0) {
				enemy.deleteAllBullets();
				// world.removeEnemy(enemy); // Remove enemy from game world
				System.out.println("enemy removed");
				removedEnemies.add(enemy); // Add enemy to the removedEnemies list
			}
		}
		
		world.getEnemies().removeAll(removedEnemies); // Remove all removed enemies from the original list

		world.getChest().forEach(e -> e.update(dt));

		clientProgram.updateNetwork();
	}
	//#endregion
}
