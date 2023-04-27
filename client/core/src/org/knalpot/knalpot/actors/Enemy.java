package org.knalpot.knalpot.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Actor {

    public enum EnemyState {
        IDLE, MOVE, JUMP, ATTACK
    }

    public Enemy(Vector2 position, Player player) {
        this.position = position;
        BBSize = new int[]{60, 60};
        scaleSize = 2;
        bounds = new Rectangle(position.x, position.y, BBSize[0] * scaleSize, BBSize[1] * scaleSize);
        direction = 1;
        previousDirection = 1;
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
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
    }

    public void setEnemyDirection(int direction) {
        this.direction = direction;
    }

    public int getEnemyDirection() {
        return this.direction;
    }
}
