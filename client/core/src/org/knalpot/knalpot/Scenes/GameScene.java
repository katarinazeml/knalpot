package org.knalpot.knalpot.scenes;

import org.knalpot.knalpot.addons.*;
import org.knalpot.knalpot.world.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class GameScene implements Screen {
	private World world;
	private WorldProcessor processor;
	private Renderer renderer;	

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
}