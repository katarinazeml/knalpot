<<<<<<< HEAD
<<<<<<< HEAD
package org.knalpot.knalpot.Scenes;
=======
=======
>>>>>>> 0a8fd2cdaa7f133b1b996dd4f2c1c35e8f593c5c
package org.knalpot.knalpot.scenes;

import org.knalpot.knalpot.addons.*;
import org.knalpot.knalpot.world.*;
<<<<<<< HEAD
>>>>>>> c148d34 (Refactored a bit folders in project)
=======
>>>>>>> 0a8fd2cdaa7f133b1b996dd4f2c1c35e8f593c5c

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class GameScene implements Screen {
<<<<<<< HEAD
	private org.knalpot.knalpot.World.World world;
	private org.knalpot.knalpot.World.WorldProcessor processor;
	private org.knalpot.knalpot.Addons.Renderer renderer;

	@Override
	public void show() {
		world = new org.knalpot.knalpot.World.World();
		processor = new org.knalpot.knalpot.World.WorldProcessor(world);
		renderer = new org.knalpot.knalpot.Addons.Renderer(world);
=======
	private World world;
	private WorldProcessor processor;
	private Renderer renderer;	

	@Override
	public void show() {
		world = new World();
		processor = new WorldProcessor(world);
		renderer = new Renderer(world);
>>>>>>> 0a8fd2cdaa7f133b1b996dd4f2c1c35e8f593c5c
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