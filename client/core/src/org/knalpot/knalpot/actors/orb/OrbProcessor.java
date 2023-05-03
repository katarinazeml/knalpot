package org.knalpot.knalpot.actors.orb;

import java.util.ListIterator;

import org.knalpot.knalpot.actors.Bullet;
import org.knalpot.knalpot.actors.Enemy;
import org.knalpot.knalpot.actors.player.Player;
import org.knalpot.knalpot.world.World;

public class OrbProcessor {
    private World world;
    private Player player;
    private Orb orb;
    private java.util.List<Bullet> bullets;

    public OrbProcessor(World world) {
		this.world = world;
        player = this.world.getPlayer();
        orb = this.world.getOrb();
        bullets = orb.getBullets();
	}

    public void update(float dt) {
        ListIterator<Bullet> bulletIterator = bullets.listIterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            world.getEnemies().forEach(e -> {
                if (bullet.getBounds().overlaps(e.getBounds())) {
                    if (e.EnemyHealth > 0) {
                        bulletIterator.remove();
                        e.gotShot(10);
                    }
                }
            });
        }
    }
}
