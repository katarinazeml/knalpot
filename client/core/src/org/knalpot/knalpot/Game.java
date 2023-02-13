package org.knalpot.knalpot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private Player player;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		player = new Player("player.png");
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0.2f, 0, 1);
		batch.begin();
		player.renderPlayer();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		player.disposePlayer();
	}
}
