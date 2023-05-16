package org.knalpot.knalpot.world;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.knalpot.knalpot.actors.Actor;
import org.knalpot.knalpot.actors.Enemy;
import org.knalpot.knalpot.actors.orb.Orb;
import org.knalpot.knalpot.actors.player.Player;
import org.knalpot.knalpot.interactive.Static;
import org.knalpot.knalpot.interactive.props.Chest;
import org.knalpot.knalpot.interactive.props.Consumable;
import org.knalpot.knalpot.networking.ClientProgram;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * {@code World} is responsible for creating all required objects
 * and returning them when requested. Basically it is the heart of everything.
 * @author Max Usmanov
 * @version 0.2
 */
public class World {
	//#region -- VARIABLES --
	// ==== OBJECT VARIABLES ==== //
	private Player player;
	private Orb orb;

	// ==== NETWORKING ==== //
	private ClientProgram clientProgram;
	//#endregion

	// Tilemap temporary stuff
	private String tiledSrc = "level1/tilemap.xml";
	public TiledMap tiledMap;

	public List<Static> collisionBlocks;
	public List<Static> platforms;
	private List<Chest> chests;
	
	// Multiplayer variables
	private List<Enemy> enemies;
	private List<Orb> orbs;

	public int roomID;

	//#region -- FUNCTIONS --
	/**
	 * {@code World} constructor.
	 */
	public World() {
		tiledMap = new TmxMapLoader().load(tiledSrc);
		collisionBlocks = new ArrayList<>();
		platforms = new ArrayList<>();
		chests = new ArrayList<>();
		enemies = new ArrayList<>();
		orbs = new ArrayList<>();
		roomID = ThreadLocalRandom.current().nextInt(100, 1000);

		initializeWorld();
		initializeNetwork();

		player.initializeHUD();
		chests.forEach(e -> e.initializeChestHUD());
	}

	/**
	 * Returns {@code Player} object when requested.
	 * @return {@code Player} object.
	 */
	public Player getPlayer() {
		return player;
	}

	public List<Orb> getOrbs() {
		return orbs;
	}

	public List<Chest> getChest() {
		return chests;
	}

	/**
	 * @return ClientProgram
	 */
	public ClientProgram getClientProgram() {
		return clientProgram;
	}

	/**
	 * Returns all {@code Static} objects.
	 * @return {@code Static} object
	 */
	public List<Static> getCollisionBlocks() {
		return collisionBlocks;
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}

	public void addOrb(Actor mpPlayer) {
		orbs.add(new Orb(mpPlayer, this));
		orbs.get(orbs.size() - 1).setIsMP(true);;
	}

	public void removeMPOrb() {
		// Dumb way to remvoe orb but it must work.
		if (orbs.size() > 1) {
			orbs.remove(1);
		}
	}

	public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

	public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

	public void addChest(Chest chest) {
		// Add elements randomly to the chest
		for (int i = 0; i < 3; i++) {
			// Add consumables to the chest
			List<Consumable> consumables = new ArrayList<>();
			consumables.add(new Consumable(new Vector2(0, 0), 32, 32, new Texture("potion.png"), "Potion"));
			consumables.add(new Consumable(new Vector2(0, 0), 32, 32, new Texture("potion.png"), "Apple"));
			consumables.add(new Consumable(new Vector2(0, 0), 32, 32, new Texture("potion.png"), "Water"));
			
			consumables.get(0).setPower(10);
			consumables.get(1).setPower(5);
			consumables.get(2).setPower(-4);
			int randomIndex = ThreadLocalRandom.current().nextInt(consumables.size());
			Consumable randomConsumable = consumables.get(randomIndex);
			chest.addConsumable(randomConsumable);
			consumables.remove(randomConsumable);
		}
		chest.initializeChestHUD();
		chests.add(chest);
	}

	public void removeChest(Chest chest) {
		chests.remove(chest);
	}

	/**
	 * Initializes all object needed for this 'world'.
	 */
	private void initializeWorld() {
		player = new Player(new Vector2(100, 200));
		orb = new Orb(player, this);
		orbs.add(orb);

		for (MapObject obj : tiledMap.getLayers().get("collisions").getObjects()) {
			RectangleMapObject rectObj = (RectangleMapObject) obj;
			Rectangle rect = rectObj.getRectangle();
			collisionBlocks.add(new Static(new Vector2(rect.getX() * 2, rect.getY() * 2), (int) rect.width * 2, (int) rect.height * 2));
		}

		for (MapObject obj : tiledMap.getLayers().get("platforms").getObjects()) {
			RectangleMapObject rectObj = (RectangleMapObject) obj;
			Rectangle rect = rectObj.getRectangle();
			platforms.add(new Static(new Vector2(rect.getX() * 2, rect.getY() * 2), (int) rect.width * 2, (int) rect.height * 2));
		}
	}

	private void initializeNetwork() {
		clientProgram = new ClientProgram(this);
	}
	//#endregion
}
