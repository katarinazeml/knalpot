package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.addons.BBGenerator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Actor {
    //dispose of bullet when collision happens

    private Orb orb;

    public Bullet(Orb orb){
        this.orb = orb;
        scaleSize = 2;

        texture = new Texture("bullet.png");
        BBSize = BBGenerator.BBPixels(texture.getTextureData());

        WIDTH = texture.getWidth();
        HEIGHT = texture.getHeight();

        Vector2 orbPosition = this.orb.getPosition().cpy();
        position = new Vector2();
        position.x = orbPosition.x;
        position.y = orbPosition.y + this.orb.getHeight();

        bounds = new Rectangle();
        bounds.x = position.x;
        bounds.y = position.y;
        bounds.width = BBSize[0] * scaleSize;
        bounds.height = BBSize[1] * scaleSize;
    }

    public Orb getOrb() {
        return orb;
    }

    public void setOrb(Orb orb) {
        this.orb = orb;
    }

    @Override
    public void update(float dt) {
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }
}

