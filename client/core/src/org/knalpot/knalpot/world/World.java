<<<<<<< HEAD
package org.knalpot.knalpot.World;
=======
package org.knalpot.knalpot.world;

import org.knalpot.knalpot.actors.Player;
import org.knalpot.knalpot.interactive.CollisionBlock;
>>>>>>> c148d34 (Refactored a bit folders in project)

import com.badlogic.gdx.math.Vector2;
import org.knalpot.knalpot.Interactive.CollisionBlock;
import org.knalpot.knalpot.Player.Player;

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
		player = new Player(new Vector2(400, 240));
		block1 = new CollisionBlock(new Vector2(450, 20));
	}
}