package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import org.knalpot.knalpot.addons.Constants;
import org.knalpot.knalpot.interactive.Static;

public class EnemyProcessor {
    //#region -- VARIABLES --

    // ==== OBJECT VARIABLES ==== //
	private World world;
	private Enemy enemy;

    // ==== MOVEMENT ==== //
	private final float SPEED = 60f;
    private final float JUMP_HEIGHT = 600f;

    // ==== JUMP MECHANICS ==== //
    private boolean canJump = true;

    // ==== GRAVITY ==== //
    private float gravityForce = Constants.GRAVITY_FORCE;


    // ==== COLLISION-RELATED ==== //
    private Vector2 cp;
    private Vector2 cn;
    private float t;

    //#endregion
    
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
     * Updating {@code Enemy}'s position each frame.
	 * @param dt
	 */
	public void update(float dt) {
        gravity();
        horizontalMovement();
        verticalMovement();

    	enemy.getAcceleration().scl(dt);
		enemy.getVelocity().add(enemy.getAcceleration().x, enemy.getAcceleration().y);

        for (Static obj : world.collisionBlocks) {
            if (resolveCollision(enemy, obj, dt)) {
                if (enemy.getVelocity().y == 0f) canJump = true;
            }
        }

        for (Static obj : world.platforms) {
            if (resolvePlatformCollision(enemy, obj, dt)) {
                if (enemy.getVelocity().y == 0f) canJump = true;
            }
        }

        enemy.update(dt);
	}

	/**
	 * Adds constant gravity force to object.
	 */
	private void gravity() {
		if (enemy.getVelocity().y < 0) gravityForce = Constants.GRAVITY_FORCE * Constants.GRAVITY_ACCEL;
        enemy.getAcceleration().y = -gravityForce;
	}

    /**
     * Moves {@code Enemy} horizontally towards the player.
     */
    private void horizontalMovement() {
        float playerX = world.getPlayer().getPosition().x;
        float enemyX = enemy.getPosition().x;
        if (playerX < enemyX) {
            enemy.getVelocity().x = -SPEED;
            enemy.direction = -1;
        } else if (playerX > enemyX) {
            enemy.getVelocity().x = SPEED;
            enemy.direction = 1;
        } else {
            enemy.getVelocity().x = 0f;
        }

        enemy.enemyState = (enemy.getVelocity().x != 0) ? Enemy.EnemyState.MOVE : Enemy.EnemyState.IDLE;
    }


	/**
	 * Moves {@code Enemy} vertically using simple jump mechanics.
	 */
	private void verticalMovement() {
        if (canJump) {
            enemy.getVelocity().y = JUMP_HEIGHT;
            canJump = false;
        }
	}
    //#endregion

    //#region - COLLISIONS -
    private boolean resolveCollision(Actor in, Static block, float dt) {
        cn = new Vector2();
        cp = new Vector2();
        float contactTime = 0f;
        if (enemy.DynamicAABB(in, block, cp, cn, contactTime, dt)) {
            cn = enemy.getContactNormal();
            cp = enemy.getContactPoint();
            t = enemy.getContactTime();
            // System.out.println("checking velocity");
            // System.out.println(in.getVelocity().x + ":x before");
            in.getVelocity().x -= cn.x * Math.abs(in.getVelocity().x) * (1 - contactTime);
            // System.out.println(in.getVelocity().x + ":x after");
            // System.out.println(in.getVelocity().y + ":y before");
            in.getVelocity().y -= cn.y * Math.abs(in.getVelocity().y) * (1 - contactTime);
            // System.out.println(in.getVelocity().y + ":y after");
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
