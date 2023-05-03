package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.knalpot.knalpot.actors.player.Player;

import org.knalpot.knalpot.actors.Enemy.EnemyState;
import org.knalpot.knalpot.addons.Constants;
import org.knalpot.knalpot.interactive.Static;


public class EnemyProcessor {
    //#region -- VARIABLES --

    // ==== OBJECT VARIABLES ==== //
	private World world;
	private Enemy enemy;
    private Player player;

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

    private final float CHASE_RADIUS = 400f;
    private final float ATTACK_RADIUS = 300f;
    private final float STOP_RADIUS = 100f;
    private float directionChangeCooldown = 0f;
    private final float DIRECTION_CHANGE_COOLDOWN_TIME = 2f; // adjust the cooldown time

    public boolean attacking = false;
    private float attackTimer = 2f;

    private boolean verticalCollisionOccurred = false;
    // private int lastDirection;
    
    // ==== SHOOTING ==== //
    private ArrayList<EnemyBullet> bullets;
    //#endregion

    //#region -- FUNCTIONS --
	/**
     * Processor constructor.
	 * @param world
	 */
	public EnemyProcessor(World world, Enemy enemy) {
		this.world = world;
        this.enemy = enemy;
        player = this.world.getPlayer();
        bullets = enemy.getEnemyBullets();
	}

	/**
     * Updating {@code Enemy}'s position each frame.
	 * @param dt
	 */
    public void update(float dt) {
        gravity();
        attack();
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
        float playerX = player.getPosition().x;
        float enemyX = enemy.getPosition().x;
        float distanceToPlayer = Math.abs(playerX - enemyX);
        if ((distanceToPlayer <= STOP_RADIUS)) {
            // Enemy collides with player, stop moving
            enemy.getVelocity().x = 0f;
            enemy.setState(EnemyState.IDLE);
        }

        ListIterator<EnemyBullet> bulletIterator = bullets.listIterator();
        while (bulletIterator.hasNext()) {
            EnemyBullet bullet = bulletIterator.next();
            if (bullet.getBounds().overlaps(player.getBounds())) {
                if (enemy.EnemyHealth > 0) {
                bulletIterator.remove();
                player.caughtByEnemy(10); 
            }
            } else {
                for (Static obj : world.collisionBlocks) {
                    if (resolveCollision(bullet, obj, dt)) {
                        bulletIterator.remove();
                        break;
                    }
                }
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
    
    private void attack() {
        float playerCenterX = player.getPosition().x + player.getWidth() / 2;
        float playerCenterY = player.getPosition().y + player.getHeight() / 2;
    
        float playerX = player.getPosition().x;
        float enemyX = enemy.getPosition().x;
        float distanceToPlayer = Math.abs(playerX - enemyX);
        if (distanceToPlayer < ATTACK_RADIUS) {
            enemy.setState(EnemyState.ATTACK);
            enemy.attack = true;
            attackTimer -= Gdx.graphics.getDeltaTime();
    
            if (enemy.attack && enemy.timeSinceLastShot > enemy.shootingCooldown) {
                Vector2 target = new Vector2(playerCenterX, playerCenterY);
                float bulletSpeed = 300f;
                float dx = playerCenterX - enemy.getPosition().x;
                float dy = playerCenterY - enemy.getPosition().y;
                float distanceToTarget = (float) Math.sqrt(dx * dx + dy * dy);
                float timeToTarget = distanceToTarget / bulletSpeed;

                // Calculate the player's predicted position after the bullet reaches the target position
                float predictedX = playerCenterX + player.getVelocity().x * timeToTarget;
                float predictedY = playerCenterY + player.getVelocity().y * timeToTarget + 0.5f * gravityForce * timeToTarget * timeToTarget;

                // Adjust the predicted y position based on the player's vertical acceleration
                float predictedYAcceleration = player.getAcceleration().y - gravityForce;
                predictedY += 0.5f * predictedYAcceleration * timeToTarget * timeToTarget;
    
                // Adjust the predicted y position based on the player's vertical velocity
                if (player.getVelocity().y > 0) {
                    float timeToReachPeak = player.getVelocity().y / gravityForce;
                    float jumpHeight = (player.getVelocity().y * timeToReachPeak) - (0.5f * gravityForce * timeToReachPeak * timeToReachPeak);
                    predictedY += jumpHeight;
                }
    
                // If the predicted position is below the player's current position, adjust the y value to prevent shooting below the player
                if (predictedY < playerCenterY) {
                    float timeToFall = (playerCenterY - predictedY) / Constants.GRAVITY_FORCE;
                    float fallDistance = (player.getVelocity().y - Constants.GRAVITY_FORCE * timeToFall) * timeToTarget;
                    if (fallDistance >= predictedY - playerCenterY) {
                        predictedY += fallDistance;
                    } else {
                        predictedY = playerCenterY;
                    }
                }
                // Set the target position to the predicted position
                target = new Vector2(predictedX, predictedY);
                if (enemy.EnemyHealth > 0) {
                    enemy.shoot(target);
                }
                enemy.timeSinceLastShot = 0f;
            }
    
            if (attackTimer <= 0f && enemy.EnemyHealth > 0) {
                player.caughtByEnemy(10); // reduce player's health by 10
                attackTimer = 2f; // reset timer
            }
        } else {
            enemy.setState(EnemyState.IDLE);
            attackTimer = 2f; // reset timer
            enemy.attack = false;
        }
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
            enemy.setState(EnemyState.MOVE);
            directionChangeCooldown = DIRECTION_CHANGE_COOLDOWN_TIME; // Reset the cooldown when chasing the player
            //System.out.println(enemy.getVelocity().y);
            if (verticalCollisionOccurred && canJump) {
                jump();
                //System.out.println(enemy.getVelocity().y);
                System.out.println("jumping");
                verticalCollisionOccurred = false;
            }
        } else {
            // Player is outside of chase radius, wander around
            SPEED = 40;
            if (directionChangeCooldown <= 0f) {
                enemy.getVelocity().x = (MathUtils.randomBoolean()) ? -SPEED : SPEED;
                enemy.direction = (enemy.getVelocity().x > 0) ? 1 : -1;
                enemy.setState(EnemyState.MOVE);
                directionChangeCooldown = DIRECTION_CHANGE_COOLDOWN_TIME; // Reset the cooldown after changing direction
            } else {
                directionChangeCooldown -= Gdx.graphics.getDeltaTime();
            }
        }
    }
    
    /**
     * Makes {@code Enemy} jump.
     */
    private void jump() {
        enemy.getVelocity().y = JUMP_HEIGHT;
        canJump = false;
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
        }
        if (enemy.getVelocity().x == 0 && collision) {
            System.out.println("vertical collision occured");
            verticalCollisionOccurred = true;
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

    private boolean resolveBulletCollision(Actor in, Actor target, float dt) {
        cn = new Vector2();
        cp = new Vector2();
        float contactTime = 0f;
        if (enemy.DynamicAABBActor(in, target, cp, cn, contactTime, dt)) {
            cn = enemy.getContactNormal();
            cp = enemy.getContactPoint();
            t = enemy.getContactTime();
            return true;
        }
        return false;
    }
    //#endregion
}
