package org.knalpot.knalpot.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.math.Vector2;
import org.knalpot.knalpot.Interactive.CollisionBlock;
import org.knalpot.knalpot.Player.Player;

import java.io.DataOutputStream;
import java.net.Socket;

public class World {
	private Player player;
	private CollisionBlock block1;

	public World() {
		initializeWorld();
	}

	public Player getPlayer() {
		return player;
	}

	public CollisionBlock getCollisionBlocks() {
		return block1;
	}

	private void initializeWorld() {
		player = new Player(new Vector2(350, 240));
		block1 = new CollisionBlock(new Vector2(450, 20));
	}

}