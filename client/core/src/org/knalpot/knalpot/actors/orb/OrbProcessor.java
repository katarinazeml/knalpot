package org.knalpot.knalpot.actors.orb;

import java.util.ArrayList;
import java.util.ListIterator;

import org.knalpot.knalpot.actors.Bullet;
import org.knalpot.knalpot.actors.Enemy;
import org.knalpot.knalpot.actors.EnemyBullet;
import org.knalpot.knalpot.actors.player.Player;
import org.knalpot.knalpot.world.World;

import com.badlogic.gdx.scenes.scene2d.ui.List;

public class OrbProcessor {
    private World world;
    private Enemy enemy;
    private Player player;
    private Orb orb;
    private java.util.List<Bullet> bullets;

    public OrbProcessor(World world) {
		this.world = world;
		enemy = this.world.getEnemy();
        player = this.world.getPlayer();
        orb = this.world.getOrb();
        bullets = orb.getBullets();
	}

    public void update(float dt) {
        ListIterator<Bullet> bulletIterator = bullets.listIterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            if (bullet.getBounds().overlaps(enemy.getBounds())) {
                bulletIterator.remove();
                world.getEnemy().gotShot(10);
            }
        }
    }
}
