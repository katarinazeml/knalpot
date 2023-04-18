package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.actors.Player.State;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Actor {
    
    private Player player;
    
    public Enemy(Vector2 position, Player player) {
        this.position = position;
        BBSize = new int[]{60, 60};
        scaleSize = 2;
        bounds = new Rectangle(position.x, position.y, BBSize[0], BBSize[1]);
        direction = 1;
        previousDirection = 1;
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
        state = State.IDLE;
        previousState = State.IDLE;
        this.player = player;
    }

    @Override
    public void update(float dt) {
        moveTowardsTarget(dt);
    }
    
    private void moveTowardsTarget(float dt) {
        float speed = 100f;
        Vector2 targetVelocity = player.getPosition().cpy().sub(position).nor().scl(speed);
        Vector2 targetAcceleration = targetVelocity.cpy().sub(velocity).scl(1/dt);
        acceleration = targetAcceleration;
        velocity.add(acceleration.cpy().scl(dt));
        position.add(velocity.cpy().scl(dt));
        bounds.setPosition(position);
    }
}
