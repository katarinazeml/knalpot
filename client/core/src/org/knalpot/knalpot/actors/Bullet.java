package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.addons.BBGenerator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Actor {

    private Orb orb;
    private float angle;

    public Bullet(Orb orb, float angle){
        super();
        this.orb = orb;
        this.angle = angle;
        scaleSize = 2;

        texture = new Texture("bullet.png");
        BBSize = BBGenerator.BBPixels(texture.getTextureData());

        WIDTH = texture.getWidth();
        HEIGHT = texture.getHeight();

        position = this.orb.getPosition().cpy();
        velocity = new Vector2();
    }

    public Orb getOrb() {
        return orb;
    }

    public void setOrb(Orb orb) {
        this.orb = orb;
    }

    @Override
    public void update(float dt) {
        // System.out.println(angle);
        velocity.set(angle * 500f, MathUtils.sin(angle) * 100f);
        position.add(velocity.x * dt, velocity.y * dt);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }
}
