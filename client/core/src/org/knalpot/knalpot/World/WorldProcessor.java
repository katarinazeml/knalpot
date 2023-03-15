package org.knalpot.knalpot.World;

import org.knalpot.knalpot.Interactive.CollisionBlock;
import org.knalpot.knalpot.Player.Player;
import org.knalpot.knalpot.Player.PlayerProcessor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class WorldProcessor {
	private World world;
	private Player player;
	private PlayerProcessor playerProcessor;

	private CollisionBlock block;

	private Socket socket;
	private DataOutputStream dOut;

	public WorldProcessor(World world) {
		this.world = world;
		player = world.getPlayer();
		block = world.getCollisionBlocks();
		playerProcessor = new PlayerProcessor(world);
		try {
			socket = new Socket("localhost", 8080);
			dOut = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println(e);
		}
	}

//	public void startServer() throws Exception {
//		try {
//			client = new NettyClient(player);
//			System.out.println("server object is created");
//			serverIsStarted = true;
//			client.start();
//			System.out.println("server start");
//		} catch (Exception e) {
//			serverIsStarted = false;
//			System.out.println("there is no server");
//		}
//	}

	public void update(float dt) {
		playerProcessor.update(dt);
//		if (!serverIsStarted) {
//			try {
//				startServer();
//			} catch (Exception e) {
//				System.out.println("cant start server");
//			}
//		}
		try {
			dOut.writeFloat(player.getPosition().x);
			dOut.flush();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
