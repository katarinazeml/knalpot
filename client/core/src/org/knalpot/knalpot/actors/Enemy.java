package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.actors.EnemyProcessor.EnemyState;
import org.knalpot.knalpot.actors.Player.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Actor {

    private Player player;
    private EnemyState state;
    private boolean soundPlayed = false;
    private float chaseTime = 0f;
    private float chaseDuration = 3f;
    //private float idleTime = 0f;
    //private float idleDuration = 5f;
    private float chaseRadius = 220f; // Change this to adjust the radius at which the enemy will start chasing the player
    private float wanderTime = 0f;
    private float wanderDuration = 1f; // Change this to adjust how long the enemy will wander before changing direction
    private Vector2 wanderDirection = new Vector2(0, 0);
    
    public int enemyDirection = -1; // Default direction is right

    public Enemy(Vector2 position, Player player) {
        this.position = position;
        BBSize = new int[]{60, 60};
        scaleSize = 2;
        bounds = new Rectangle(position.x, position.y, BBSize[0] * scaleSize, BBSize[1] * scaleSize);
        direction = 1;
        previousDirection = 1;
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
        previousState = State.IDLE;
        this.player = player;
        this.state = EnemyState.IDLE;
        wanderDirection.set(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f)).nor();
    }

    @Override
    public void update(float dt) {
        float distanceToPlayer = position.dst(player.getPosition());
        if (distanceToPlayer <= chaseRadius) { // Only chase the player if they are within the specified radius
            if (state != EnemyState.CHASE) {
                soundPlayed = false;
            }
            state = EnemyState.CHASE;
            chaseTime += dt;
            if (chaseTime >= chaseDuration) {
                chaseTime = 0f;
                state = EnemyState.IDLE;
            } else {
                moveTowardsTarget(dt);
            }
        } else {
            state = EnemyState.IDLE;
            wanderTime += dt;
            if (wanderTime >= wanderDuration) {
                wanderTime = 0f;
                wanderDirection.set(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f)).nor();
            } else {
                moveInDirection(dt, wanderDirection);
            }
        }
        // Update enemy direction based on movement direction
        if (velocity.x > 0) {
            enemyDirection = 1;
        } else if (velocity.x < 0) {
            enemyDirection = -1;
        }
    }

    public void setState(EnemyState state) {
        this.state = state;
    }

    private void moveTowardsTarget(float dt) {
        moveInDirection(dt, player.getPosition().cpy().sub(position).nor());
        float catchDistance = 5f;
        if (position.dst(player.getPosition()) < catchDistance && !soundPlayed) {
            player.caughtByEnemy();
            Gdx.audio.newSound(Gdx.files.internal("oof.mp3")).play(0.8f);
            soundPlayed = true;
        }
    }

    private void moveInDirection(float dt, Vector2 direction) {
        float speed = 40f;
        Vector2 targetVelocity = direction.cpy().scl(speed);
        Vector2 targetAcceleration = targetVelocity.cpy().sub(velocity).scl(1/dt);
        acceleration = targetAcceleration;
        velocity.add(acceleration.cpy().scl(dt));
        position.add(velocity.cpy().scl(dt));
        bounds.setPosition(position.x, position.y);
    }
}
