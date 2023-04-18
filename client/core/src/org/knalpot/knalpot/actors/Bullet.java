package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.addons.BBGenerator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Actor {

    private Orb orb;

    private Vector2 targetPosition;
    //private float speed = 500;

    public boolean isDisposed;

    public Texture texture;

    public Vector2 bulletPosition;

    public Vector2 bulletVelocity;

    public Bullet(Orb orb){
        super();
        this.orb = orb;
        scaleSize = 2;

        texture = new Texture("bullet.png");
        BBSize = BBGenerator.BBPixels(texture.getTextureData());

        WIDTH = texture.getWidth();
        HEIGHT = texture.getHeight();

        bulletPosition = new Vector2();
        bulletVelocity = new Vector2();

        // Initialize targetPosition to a default value
        //targetPosition = new Vector2(position.x, position.y + 100);
    }

    public Orb getOrb() {
        return orb;
    }

    public void setOrb(Orb orb) {
        this.orb = orb;
    }

    @Override
    public void update(float dt) {
        // Move the bullet towards the target position
        //Vector2 direction = targetPosition.cpy().sub(position).nor();
        //position.add(direction.scl(speed * dt));

        // Check if the bullet has reached its target
        //if (position.dst(targetPosition) < 10) {
        //    isDisposed = true; // Dispose of the bullet when it reaches its target
        //}
        bulletPosition.add(velocity.x * dt, velocity.y * dt);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, bulletPosition.x, bulletPosition.y);
    }

    public void setTargetPosition(float x, float y) {
        targetPosition = new Vector2(x, y);
    }
}
