package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.world.World;

import com.badlogic.gdx.math.MathUtils;

public class EnemyProcessor {

    //#region -- VARIABLES --

    // ==== OBJECT VARIABLES ==== //
    private World world;
    private Enemy enemy;

    // ==== MOVEMENT ==== //
    private float SPEED = 15f;

    private int moveInput = 1;

    // ==== AI CHASING ==== //
    private final float CHASE_RADIUS = 80f;

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
        if (isPlayerInChaseRadius()) {
            chasePlayer();
            SPEED = 30f;
        } else {
            wanderAround();
        }
    
        enemy.getPosition().add(enemy.getVelocity().x * dt, 0);
    
        if (enemy.getVelocity().x > 0) {
            enemy.setEnemyDirection(1);
            System.out.println("facing right");
            System.out.println(enemy.getEnemyDirection());
        } else if (enemy.getVelocity().x < 0) {
            enemy.setEnemyDirection(-1);
            System.out.println("facing left");
            System.out.println(enemy.getEnemyDirection());
        }
    
        // System.out.println("Enemy positions");
        // System.out.println(enemy.getPosition().x);
        // System.out.println(enemy.getPosition().y);
        enemy.update(dt);
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
        // Randomly change the horizontal movement direction every few seconds
        if (MathUtils.randomBoolean(0.005f)) {
            moveInput = MathUtils.randomSign();
        }

        enemy.getVelocity().x = moveInput * SPEED;
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

    //#endregion
}
