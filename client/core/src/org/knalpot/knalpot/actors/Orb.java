package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.actors.Player.State;
import org.knalpot.knalpot.addons.BBGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Orb extends Actor {

    // Owner of the orb
    private Actor owner;

    private float speed = 5f;

    // Bullet instance
    private Bullet bullet;

    // Levitation variables
    private float levitationTimer;
    private float range;
    private float rotation;

    private boolean mustFloat;

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

        // create the Bullet instance
        bullet = new Bullet(this);
    }

    public Actor getOwner() {
        return owner;
    }

    public void setOwner(Actor owner) {
        this.owner = owner;
    }

    @Override
    public void update(float dt) {
        // Calculate the target position for the camera.
        float targetX = owner.getPosition().x - owner.getWidth() * owner.direction;
        float targetY = owner.getPosition().y + owner.getHeight() / 2;
    
        // Interpolate the camera's position towards the target position.
        float dx = targetX - position.x;
        float dy = targetY - position.y;
    
        // Calculate the angle between the cursor position and the orb's position
        float angle = (float) Math.atan2(Gdx.graphics.getHeight() - Gdx.input.getY() - position.y,
                                         Gdx.input.getX() - position.x);
    
        // Convert the angle to degrees and subtract 90 degrees to account for the orientation of the orb's sprite
        rotation = (float) Math.toDegrees(angle) - 90;
    
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
        // Check if B key is pressed
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            // Move the orb back for a short duration
            position.x -= 10f;
        }

        position.x += dx * speed * dt;
        bounds.setPosition(position.x, position.y);
    }
    

    public void render(SpriteBatch batch) {
        batch.draw(region, position.x, position.y, getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), scaleSize, scaleSize, rotation, false);
    }
}
