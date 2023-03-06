package org.knalpot.knalpot.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

public class GameScene implements Screen {
	private org.knalpot.knalpot.World.World world;
	private org.knalpot.knalpot.World.WorldProcessor processor;
	private org.knalpot.knalpot.Addons.Renderer renderer;

	@Override
	public void show() {
		world = new org.knalpot.knalpot.World.World();
		processor = new org.knalpot.knalpot.World.WorldProcessor(world);
		renderer = new org.knalpot.knalpot.Addons.Renderer(world);
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