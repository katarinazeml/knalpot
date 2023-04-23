package org.knalpot.knalpot.world;

import org.knalpot.knalpot.actors.player.PlayerProcessor;
import org.knalpot.knalpot.hud.HUDProcessor;
import org.knalpot.knalpot.networking.ClientProgram;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

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

	// ==== NETWORKING ==== //
	private ClientProgram clientProgram;

	// ==== HUD ==== //
	private HUDProcessor hud;
	private boolean isHUDActive;
	//#endregion

	//#region -- FUNCTIONS --
	/**
	 * {@code WorldProcessor} constructor.
	 * @param world
	 */
	public WorldProcessor(World world) {
		this.world = world;
		playerProcessor = new PlayerProcessor(this.world);
		clientProgram = this.world.getClientProgram();
		hud = this.world.getHUD();
		clientProgram.create();
	}

	/**
	 * Updates every single 'processor' in the world.
	 * @param dt
	 */
	public void update(float dt) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
			isHUDActive = !isHUDActive;
		}

		playerProcessor.update(dt);
		world.getOrb().update(dt);

		hud.updateHUD(dt, isHUDActive);

		clientProgram.updateNetwork();
	}
	//#endregion
}