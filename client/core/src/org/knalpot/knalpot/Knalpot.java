package org.knalpot.knalpot;

import com.badlogic.gdx.Game;

public class Knalpot extends Game {

	private Game game;

	public Knalpot() {
		game = this;
	}

	public void create() {
		this.setScreen(new org.knalpot.knalpot.Scenes.GameScene());
	}

	public void render() {
		super.render();
	}
}
