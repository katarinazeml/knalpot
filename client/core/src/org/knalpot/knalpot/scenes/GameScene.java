package org.knalpot.knalpot.scenes;

import org.knalpot.knalpot.addons.Renderer;
import org.knalpot.knalpot.networking.ClientProgram;
import org.knalpot.knalpot.world.World;
import org.knalpot.knalpot.world.WorldProcessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * {@code GameScene} displays all game-related objects and textures.
 * It is called when player starts either a single player or multiplayer game.
 * @author Max Usmanov
 * @version 0.1
 */
public class GameScene implements Screen {
	//#region -- VARIABLES --
	// ==== OBJECT VARIABLES ==== //
	private World world;
	private WorldProcessor processor;
	private Renderer renderer;
	//#endregion	

	//#region -- FUNCTIONS --
	@Override
	public void show() {
		world = new World();
		processor = new WorldProcessor(world);
		renderer = new Renderer(world);
	}

	@Override
	public void hide() {
	}

	@Override
	public void resize(int w, int h) {
		renderer.setCameraSize(400, 400);
	}

	@Override
	public void resume() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void render(float dt) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		processor.update(dt);
		renderer.render();
	}
	
	@Override
	public void dispose() {
		renderer.dispose();
	}
	//#endregion
}