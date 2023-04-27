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
    private float SPEED = 15f;

    private int moveInput = 1;

    // ==== GRAVITY ==== //
    private float gravityForce = Constants.GRAVITY_FORCE;

    // ==== AI ==== //
    private final float CHASE_RADIUS = 100f;
    private float timeSinceStop = 0;
    private float timeSinceDirectionChange;
    private boolean canJump = false;

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
        if (isPlayerInChaseRadius()) {
            chasePlayer();
            SPEED = 20f;
        } else {
            wanderAround();
        }
    
        enemy.getPosition().add(enemy.getVelocity().x * dt, 0);
    
        if (enemy.getVelocity().x > 0) {
            enemy.setEnemyDirection(1);
        } else if (enemy.getVelocity().x < 0) {
            enemy.setEnemyDirection(-1);
        }

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
    
        // System.out.println("Enemy positions");
        // System.out.println(enemy.getPosition().x);
        // System.out.println(enemy.getPosition().y);
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
    private void chasePlayer() {
        Actor player = world.getPlayer();
        if (enemy.getPosition().x < player.getPosition().x) {
            moveInput = 1;
        } else if (enemy.getPosition().x > player.getPosition().x) {
            moveInput = -1;
        }
    
        enemy.getVelocity().x = moveInput * SPEED;
    }
    
    /**
     * Makes {@code Enemy} wander around randomly.
     */
    private void wanderAround() {
        // Randomly stop moving for a short period of time
        if (timeSinceStop >= MathUtils.random(3f, 6f)) {
            enemy.getVelocity().x = 0;
            timeSinceStop = 0;
            System.out.println("standing still");
            return;
        }
        
        // Randomly change the horizontal movement direction every few seconds
        if (timeSinceDirectionChange >= MathUtils.random(3f, 6f)) {
            moveInput = MathUtils.randomSign();
            timeSinceDirectionChange = 0;
        }
        
        enemy.getVelocity().x = moveInput * SPEED;
        timeSinceStop += Gdx.graphics.getDeltaTime(); // Update time since last stop
        timeSinceDirectionChange += Gdx.graphics.getDeltaTime(); // Update time since last direction change
    }    
    
    /**
     * Returns {@code true} if the player is within a certain radius for the AI enemy to chase.
     * @return
     */
    private boolean isPlayerInChaseRadius() {
        Actor player = world.getPlayer();
        float distanceToPlayer = enemy.getPosition().dst(player.getPosition());
        return distanceToPlayer <= CHASE_RADIUS;
    }

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
