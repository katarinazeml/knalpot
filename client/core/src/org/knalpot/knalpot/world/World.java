package org.knalpot.knalpot.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.math.Vector2;
import org.knalpot.knalpot.interactive.Static;
import org.knalpot.knalpot.networking.ClientProgram;
import org.knalpot.knalpot.actors.Player;


public class World {
	private Player player;
	private Static block1;
	private ClientProgram clientProgram;

	public World() {
		initializeWorld();
		initializeNetwork();
	}

	public Player getPlayer() {
		return player;
	}

	public ClientProgram getClientProgram() {
		return clientProgram;
	}

	public Static getCollisionBlocks() {
		return block1;
	}

	private void initializeWorld() {
		player = new Player(new Vector2(350, 240));
		block1 = new Static(new Vector2(450, 20));
	}

	private void initializeNetwork() {
		clientProgram = new ClientProgram(player.getPosition());
	}

}