package org.knalpot.knalpot.World;

import org.knalpot.knalpot.Interactive.CollisionBlock;
import org.knalpot.knalpot.Player.Player;
import org.knalpot.knalpot.Player.PlayerProcessor;

import java.io.*;
import java.net.Socket;
import java.io.InputStream;


public class WorldProcessor {
	private World world;
	private Player player;
	private PlayerProcessor playerProcessor;

	private CollisionBlock block;

	private Socket socket;
	private DataOutputStream dOut;
	private DataInputStream dIn;


	public WorldProcessor(World world) {
		this.world = world;
		player = world.getPlayer();
		block = world.getCollisionBlocks();
		playerProcessor = new PlayerProcessor(world);
		try {
			socket = new Socket("localhost", 8080);
			dOut = new DataOutputStream(socket.getOutputStream());
			dIn = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void update(float dt) {
		playerProcessor.update(dt);
		try {
			dOut.writeFloat(player.getPosition().x);
			dOut.flush();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
