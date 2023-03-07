<<<<<<< HEAD
<<<<<<< HEAD
package org.knalpot.knalpot.World;

import org.knalpot.knalpot.Interactive.CollisionBlock;
import org.knalpot.knalpot.Player.Player;
import org.knalpot.knalpot.Player.PlayerProcessor;
=======
package org.knalpot.knalpot.world;

import org.knalpot.knalpot.actors.*;
>>>>>>> c148d34 (Refactored a bit folders in project)
=======
package org.knalpot.knalpot.world;

import org.knalpot.knalpot.actors.*;
>>>>>>> 0a8fd2cdaa7f133b1b996dd4f2c1c35e8f593c5c

public class WorldProcessor {
	private World world;
	private PlayerProcessor playerProcessor;

	public WorldProcessor(World world) {
		this.world = world;
		playerProcessor = new PlayerProcessor(this.world);
	}

	public void update(float dt) {
		playerProcessor.update(dt);
	}
}