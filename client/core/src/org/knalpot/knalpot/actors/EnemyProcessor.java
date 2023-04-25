package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.interactive.Static;
import org.knalpot.knalpot.world.World;

import com.badlogic.gdx.math.Vector2;

public class EnemyProcessor {
    //#region -- VARIABLES --

    // ==== OBJECT VARIABLES ==== //
	private World world;
	private Actor enemy;

    private int moveInput = 1;

    // ==== COLLISION-RELATED ==== //
    private Vector2 cp;
    private Vector2 cn;
    //#endregion
    private boolean canJump;
    private float t;

    public enum EnemyState {
        CHASE, IDLE, ATTACK, JUMP
    }
    
    //#region -- FUNCTIONS --
	/**
     * Processor constructor.
	 * @param world
	 */
	public EnemyProcessor(World world) {
		this.world = world;
		enemy = this.world.getEnemy();
	}

	/**
     * Updating {@code Player}'s position each frame.
	 * @param dt
	 */
	public void update(float dt) {
        // System.out.println("-----");
        windowCollision(dt);

    	//enemy.getAcceleration().scl(dt);

        // System.out.println("scalar Y accel:");
        // System.out.println(player.getAcceleration().y);

		//enemy.getVelocity().add(enemy.getAcceleration().x, enemy.getAcceleration().y);

        // System.out.println("bounds");
        // System.out.println(player.getBounds().x);
        // System.out.println("Player X position");
        // System.out.println(player.getPosition().x);
        // System.out.println("Player Y pos");
        // System.out.println(player.getPosition().y);

        // System.out.println("colblock position");
        // System.out.println(collisionBlock.getPosition());
        // System.out.println("Important stuff for collisions");
        // System.out.println(cn);
        // System.out.println(cp);
        // System.out.println(t);
        

        for (Static obj : world.collisionBlocks) {
            if (resolveCollision(enemy, obj, dt)) {
                // System.out.println("Colliding!");
                if (enemy.getVelocity().y == 0f) canJump = true;
            }
        }

        for (Static obj : world.platforms) {
            if (resolvePlatformCollision(enemy, obj, dt)) {
                if (enemy.getVelocity().y == 0f) canJump = true;
            }
        }
        enemy.update(dt);
        // System.out.println("-----");
	}


    //#endregion

    //#region - COLLISIONS -
    /**
     * Temporary collision for window borders. Will be removed in the nearest future.
     * @param dt
     */
    private void windowCollision(float dt) {
        if (enemy.Bottom + enemy.getScalarVelocity(dt).y <= 0) {
            canJump = true;
        	enemy.getPosition().y = 0f;
            enemy.getVelocity().y -= enemy.getVelocity().y;
        }
        if (enemy.Top + enemy.getScalarVelocity(dt).y >= 480 - enemy.getHeight()) enemy.getVelocity().y -= enemy.getVelocity().y;
    }

    /**
     * Resolves AABB collision using {@code Actor}'s physics.
     * @param in
     * @param block
     * @param dt
     * @return boolean
     */
    private boolean resolveCollision(Actor in, Static block, float dt) {
        cn = new Vector2();
        cp = new Vector2();
        float contactTime = 0f;
        if (enemy.DynamicAABB(in, block, cp, cn, contactTime, dt)) {
            cn = enemy.getContactNormal();
            cp = enemy.getContactPoint();
            t = enemy.getContactTime();
            in.getVelocity().x -= cn.x * Math.abs(in.getVelocity().x) * (1 - contactTime);
            in.getVelocity().y -= cn.y * Math.abs(in.getVelocity().y) * (1 - contactTime);
            // move the enemy back along its velocity vector
            Vector2 correction = cn.scl((1 - contactTime) * in.getScalarVelocity(dt).len());
            in.getPosition().add(correction.x, correction.y);
            return true;
        }
        return false;
    }

    private boolean resolvePlatformCollision(Actor in, Static platform, float dt) {
        cn = new Vector2();
        cp = new Vector2();
        float contactTime = 0f;
        if (enemy.DynamicAABB(in, platform, cp, cn, contactTime, dt)) {
            cn = enemy.getContactNormal();
            cp = enemy.getContactPoint();
            t = enemy.getContactTime();

            if (in.getVelocity().y < 0f) {
                in.getVelocity().y -= cn.y * Math.abs(in.getVelocity().y) * (1 - contactTime);
            }
            return true;
        }
        return false;
    }
    //#endregion
}
