package org.knalpot.knalpot.world;

import org.knalpot.knalpot.actors.Actor;
import org.knalpot.knalpot.actors.Player;
import org.knalpot.knalpot.interactive.Static;
import org.knalpot.knalpot.networking.ClientProgram;

import com.badlogic.gdx.math.Vector2;

/**
 * {@code World} is responsible for creating all required objects
 * and returning them when requested. Basically it is the heart of everything.
 * @author Max Usmanov
 * @version 0.1
 */
public class World {
	//#region -- VARIABLES --
	// ==== OBJECT VARIABLES ==== //
	private Player player;
	private Static block1;

	// ==== NETWORKING ==== //
	private ClientProgram clientProgram;
	//#endregion

	//#region -- FUNCTIONS --
	/**
	 * {@code World} constructor.
	 */
	public World() {
		initializeWorld();
		initializeNetwork();
	}

	/**
	 * Returns {@code Player} object when requested.
	 * @return {@code Player} object.
	 */
	public Actor getPlayer() {
		return player;
	}

	/**
	 * @return ClientProgram
	 */
	public ClientProgram getClientProgram() {
		return clientProgram;
	}

	/**
	 * Returns all {@code Static} objects.
	 * @return {@code Static} object
	 */
	public Static getCollisionBlocks() {
		return block1;
	}

	/**
	 * Initializes all object needed for this 'world'.
	 */
	private void initializeWorld() {
		player = new Player(new Vector2(350, 200));
		block1 = new Static(new Vector2(400, 0));
	}

	private void initializeNetwork() {
		clientProgram = new ClientProgram(player.getPosition());
	}
	//#endregion
}