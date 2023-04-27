package org.knalpot.knalpot.actors;

//import org.knalpot.knalpot.actors.EnemyProcessor.EnemyState;
import org.knalpot.knalpot.actors.Player.State;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Actor {

    public enum EnemyState {
        IDLE, MOVE, JUMP, FALL
    }
    //private Vector2 wanderDirection = new Vector2(0, 0);

    //private Player enemyProcessor;

    public Enemy(Vector2 position, Player player) {
        this.position = position;
        BBSize = new int[]{60, 60};
        scaleSize = 2;
        bounds = new Rectangle(position.x, position.y, BBSize[0] * scaleSize, BBSize[1] * scaleSize);
        direction = 1;
        previousDirection = 1;
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
        //this.state = EnemyState.IDLE;
        //wanderDirection.set(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f)).nor();
    }

    @Override
    public void update(float dt) {
        // Update enemy position using the Actor class's position
        position.add(velocity.cpy().scl(dt));
        bounds.x = position.x;
        bounds.y = position.y;

        Left = (int) bounds.x;
        Right = (int) bounds.x + WIDTH;
        Bottom = (int) bounds.y;
        Top = (int) bounds.y + HEIGHT;

        // Update enemyProcessor
        //enemyProcessor.update(dt);
    }

    public void setEnemyDirection(int direction) {
        this.direction = direction;
    }

    public int getEnemyDirection() {
        return this.direction;
    }
}
        // float distanceToPlayer = position.dst(player.getPosition());
        // if (distanceToPlayer <= chaseRadius) { // Only chase the player if they are within the specified radius
        //     if (state != EnemyState.CHASE) {
        //         soundPlayed = false;
        //     }
        //     state = EnemyState.CHASE;
        //     chaseTime += dt;
        //     if (chaseTime >= chaseDuration) {
        //         chaseTime = 0f;
        //         state = EnemyState.IDLE;
        //     } else {
        //         moveTowardsTarget(dt);
        //     }
        // } else {
        //     state = EnemyState.IDLE;
        //     wanderTime += dt;
        //     if (wanderTime >= wanderDuration) {
        //         wanderTime = 0f;
        //         wanderDirection.set(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f)).nor();
        //     } else {
        //         moveInDirection(dt, wanderDirection);
        //     }
        // }
        // // Update enemy direction based on movement direction
        // if (velocity.x > 0) {
        //     enemyDirection = 1;
        // } else if (velocity.x < 0) {
        //     enemyDirection = -1;
        // }


    //public void setState(EnemyState state) {
    //    this.state = state;
    //}

    // private void moveTowardsTarget(float dt) {
    //     moveInDirection(dt, player.getPosition().cpy().sub(position).nor());
    //     float catchDistance = 5f;
    //     if (position.dst(player.getPosition()) < catchDistance && !soundPlayed) {
    //         player.caughtByEnemy();
    //         Gdx.audio.newSound(Gdx.files.internal("oof.mp3")).play(0.8f);
    //         soundPlayed = true;
    //     }
    // }

    // private void moveInDirection(float dt, Vector2 direction) {
    //     float speed = 40f;
    //     Vector2 targetVelocity = direction.cpy().scl(speed);
    //     Vector2 targetAcceleration = targetVelocity.cpy().sub(velocity).scl(1/dt);
    //     acceleration = targetAcceleration;
    //     velocity.add(acceleration.cpy().scl(dt));
    //     position.add(velocity.cpy().scl(dt));
    //     bounds.setPosition(position.x, position.y);
    // }

    //private EnemyState state;
    // private boolean soundPlayed = false;
    // private float chaseTime = 0f;
    // private float chaseDuration = 3f;
    //private float idleTime = 0f;
    //private float idleDuration = 5f;
    // private float chaseRadius = 220f;
    // private float wanderTime = 0f;
    // private float wanderDuration = 1f;

