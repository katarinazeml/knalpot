package org.knalpot.knalpot.addons;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.math.Vector2;

public class Particle {
    private float SPEED = 1.2f;
    private float directionX;
    private float directionY;

    private Vector2 position;
    private Vector2 velocity;

    public Particle(Vector2 position) {
        this.position = position.cpy();
        velocity = new Vector2();
        directionX = ThreadLocalRandom.current().nextInt(-1, 2);
        directionY = ThreadLocalRandom.current().nextInt(-1, 2);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void update(float dt) {
        velocity.x += directionX * SPEED;
        velocity.y += directionY * SPEED;

        position.add(velocity.cpy().scl(dt));
    }
}
