package org.knalpot.knalpot.actors;

import java.util.ArrayList;
import java.util.List;

import org.knalpot.knalpot.actors.Player.State;
import org.knalpot.knalpot.addons.BBGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Orb extends Actor {

    // Owner of the orb
    private Actor owner;

    private Vector3 mousePos;

    private float speed = 5f;

    // Levitation variables
    private float levitationTimer;
    private float range;
    private float rotation;

    private boolean mustFloat;
    private boolean isShooting;

    private List<Bullet>  bullets;

    public Orb(Actor owner) {
        this.owner = owner;

        scaleSize = 2;

        texture = new Texture("orb.png");
        BBSize = BBGenerator.BBPixels(texture.getTextureData());
        
        WIDTH = texture.getWidth();
        HEIGHT = texture.getHeight();
        
        region = new TextureRegion(texture, 0, 0, WIDTH, HEIGHT);

        Vector2 ownerPosition = this.owner.getPosition().cpy();
        position = new Vector2();
        position.x = ownerPosition.x;
        position.y = ownerPosition.y + this.owner.getHeight();

        bounds = new Rectangle();
        bounds.x = position.x;
        bounds.y = position.y;
        bounds.width = BBSize[0] * scaleSize;
        bounds.height = BBSize[1] * scaleSize;

        // Initialize levitation variables
        levitationTimer = 0;
        range = 3f;

        bullets = new ArrayList<>();
    }

    public Actor getOwner() {
        return owner;
    }

    public void setOwner(Actor owner) {
        this.owner = owner;
    }

    public void setMousePos(Vector3 mousePos) {
        this.mousePos = mousePos;
    }

    @Override
    public void update(float dt) {
        System.out.println("Mouse position:");
        System.out.println(mousePos.x);
        System.out.println(mousePos.y);

        // Calculate the target position for the orb.
        float targetX = owner.getPosition().x - owner.getWidth() * owner.direction;
        float targetY = owner.getPosition().y + owner.getHeight() / 2;
    
        // Interpolate orb's position towards the target position.
        float dx = targetX - position.x;
        float dy = targetY - position.y;
    
        // Calculate the angle between the cursor position and the orb's position
        float angle = (float) MathUtils.atan2(mousePos.y - position.y,
                                         mousePos.x - position.x);
    
        // Convert the angle to degrees and subtract 90 degrees to account for the orientation of the orb's sprite
        rotation = (angle * MathUtils.radiansToDegrees + 270) % 360;

        if (Math.abs((int) dx) == Math.abs((int) dy) && owner.state == State.IDLE) {
            mustFloat = true;
        } else {
            position.y += dy * speed * dt;
        }
        
        if ((int) dy != 0 && owner.state != State.IDLE) {
            mustFloat = false;
        }
    
        if (mustFloat == true) {
            // Update levitation timer
            levitationTimer += dt;
    
            // Calculate new y position based on levitation timer
            float levitationOffset = range * (float) Math.sin(levitationTimer * 2 * Math.PI);
            position.y = targetY + levitationOffset;
        }

        //Bullet stuff
        isShooting = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);

        if (isShooting) {
            shoot(dt, rotation);
        }
        
        for (Bullet bullet : bullets) {
            bullet.update(dt);
        }

        position.x += dx * speed * dt;
        bounds.setPosition(position.x, position.y);
    }

    public void shoot(float dt, float angle) {
        float sine = (float) -Math.sin(Math.toRadians(angle));
        float cosine = (float) Math.cos(Math.toRadians(angle));
        position.add(-sine * 10f, -cosine * 10f);
        bullets.add(new Bullet(this, angle));
        isShooting = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(region, position.x, position.y, getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), scaleSize, scaleSize, rotation, false);
        
        for (Bullet bullet : bullets) {
            bullet.render(batch);
        }
    }
}
