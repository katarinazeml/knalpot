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
	private float SPEED = 60f;
    private final float JUMP_HEIGHT = 400f;

    // ==== JUMP MECHANICS ==== //
    private boolean canJump = true;

    // ==== GRAVITY ==== //
    private float gravityForce = Constants.GRAVITY_FORCE;

    // ==== COLLISION-RELATED ==== //
    private Vector2 cp;
    private Vector2 cn;
    private float t;

    private final float CHASE_RADIUS = 100f;
    private float directionChangeCooldown = 0f;
    private final float DIRECTION_CHANGE_COOLDOWN_TIME = 2f; // Change this to adjust the cooldown time
    private float jumpCooldown = 0.5f;
    private float JUMP_COOLDOWN_TIME = 10f; // Control how often the enemy jumps

    private boolean collisionOccurred = false;

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
        verticalMovement();
        horizontalMovement();
    
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
        float distanceToPlayer = Math.abs(playerX - enemyX);
    
        if (distanceToPlayer < CHASE_RADIUS) {
            // Player is within chase radius, move towards player
            enemy.getVelocity().x = (playerX < enemyX) ? -SPEED : SPEED;
            enemy.direction = (playerX < enemyX) ? -1 : 1;
            enemy.enemyState = Enemy.EnemyState.MOVE;
            directionChangeCooldown = DIRECTION_CHANGE_COOLDOWN_TIME; // Reset the cooldown when chasing the player
            if (collisionOccurred) {
                System.out.println("jumping");
                jump();
                collisionOccurred = false;
            }
        } else {
            // Player is outside of chase radius, wander around
            SPEED = 40;
            if (directionChangeCooldown <= 0f) {
                enemy.getVelocity().x = (MathUtils.randomBoolean()) ? -SPEED : SPEED;
                enemy.direction = (enemy.getVelocity().x > 0) ? 1 : -1;
                enemy.enemyState = Enemy.EnemyState.MOVE;
                directionChangeCooldown = DIRECTION_CHANGE_COOLDOWN_TIME; // Reset the cooldown after changing direction
            } else {
                directionChangeCooldown -= Gdx.graphics.getDeltaTime();
            }
            if (collisionOccurred) {
                jump();
                collisionOccurred = false;
            }
        }
    }
    
    
    /**
     * Makes {@code Enemy} jump.
     */
    private void jump() {
        enemy.getVelocity().y = JUMP_HEIGHT;
        jumpCooldown = JUMP_COOLDOWN_TIME;
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
        boolean collision = enemy.DynamicAABB(in, block, cp, cn, contactTime, dt);
        if (collision) {
            cn = enemy.getContactNormal();
            cp = enemy.getContactPoint();
            t = enemy.getContactTime();
            in.getVelocity().x -= cn.x * Math.abs(in.getVelocity().x) * (1 - contactTime);
            in.getVelocity().y -= cn.y * Math.abs(in.getVelocity().y) * (1 - contactTime);
            collisionOccurred = true;
        }
        return collision;
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
