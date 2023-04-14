package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.actors.Player.State;
import org.knalpot.knalpot.addons.BBGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Orb extends Actor {
    // Owner of the orb
    private Actor owner;

    private float speed = 5f;

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

        // Does not work because orb position must be calculated
        // according to viewport, not world.

        // float tillMouseX = Gdx.input.getX() - position.x;
        // float tillMouseY = (720 - Gdx.input.getY()) - position.y;
        // double distance = Math.sqrt(Math.pow(tillMouseX, 2) + Math.pow(tillMouseY, 2));
        // float rotation = (float) Math.asin(tillMouseY / distance);
        // System.out.println("Calculating rotation");
        // System.out.println(tillMouseX);
        // System.out.println(tillMouseY);
        // System.out.println(distance);
        // System.out.println(rotation);
        // System.out.println(Math.acos(Math.cos(tillMouseX / distance)));

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

        position.x += dx * speed * dt;
        bounds.setPosition(position.x, position.y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(region, position.x, position.y, getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), scaleSize, scaleSize, 0, false);
    }
}
