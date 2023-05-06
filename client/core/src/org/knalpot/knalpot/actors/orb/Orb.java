package org.knalpot.knalpot.actors.orb;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.knalpot.knalpot.actors.Actor;
import org.knalpot.knalpot.actors.Bullet;
import org.knalpot.knalpot.actors.player.Player.State;
import org.knalpot.knalpot.addons.ParticleGenerator;
import org.knalpot.knalpot.addons.effects.OSM;
import org.knalpot.knalpot.addons.effects.OSMAnimator;
import org.knalpot.knalpot.addons.effects.OSM.Shape;
import org.knalpot.knalpot.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * {@code Orb} playes a major role in game. It acts as a defender of the player
 * as well as attacking method by shooting little particles.
 * One player can have multiple orbs, therefore he is able to combine
 * his horsepower in order to defeat major waves of enemies.
 * 
 * @author Katarina Zemljanski
 * @author Maksim Usmanov
 * @version 0.2
 */
public class Orb extends Actor {
    public enum OrbState {
        ORB,
        WALL,
        SHOOTING,
        TRANSFORM_TO_ORB,
        TRANSFORM_TO_WALL
    }

    private World world;

    private OrbState state = OrbState.ORB;

    // Owner of the orb
    private Actor owner;

    private ParticleGenerator particles;

    private OSM osm;
    private OSMAnimator animator;
    private int layerAmount = 2;

    private Vector3 mousePos;

    private float speed = 4f;
    private float shootKickback = 20f;

    private float timeSinceLastShot;
    private boolean activateForceMode = false;

    // Levitation variables
    private float levitationTimer;
    private float range;
    private float rotation;

    private boolean mustFloat;

    private float previousX;

    // Bullets
    private List<Bullet> bullets;

    public List<Bullet> getBullets() {
        return bullets;
    }

    public Orb(Actor owner, World world) {
        this.owner = owner;
        initializeOrb(world);
    }

    private void initializeOrb(World world) {
        this.world = world;

        scaleSize = 2;
        
        WIDTH = 5;

        Vector2 ownerPosition = this.owner.getPosition().cpy();
        position = new Vector2();
        position.x = ownerPosition.x;
        position.y = ownerPosition.y + this.owner.getHeight();

        osm = new OSM(Shape.CIRCLE, ShapeType.Filled, Color.WHITE, this.position, new float[] { WIDTH });
        osm.enableTransparency();
        osm.enableLayers(layerAmount);
        animator = new OSMAnimator(osm);

        particles = new ParticleGenerator(this.position);
        // Initialize levitation variables
        levitationTimer = 0;
        range = 3f;

        bullets = new ArrayList<>();
    }

    public Actor getOwner() {
        return owner;
    }
    
    public float getDeltaPosition() {
        return position.x - previousX;
    }

    public void setOwner(Actor owner) {
        this.owner = owner;
    }

    public void setMousePos(Vector3 mousePos) {
        this.mousePos = mousePos;
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            state = OrbState.TRANSFORM_TO_WALL;
            // particles.end();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z))
            state = OrbState.TRANSFORM_TO_ORB;

        //Bullet stuff
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
            state = OrbState.SHOOTING;

        if (state == OrbState.ORB) {
            floating(dt);
            particles.update(dt);
        }

        previousX = position.cpy().x;

        if (state == OrbState.TRANSFORM_TO_WALL) {
            if (osm.getSize().length == 1)
                animator.shrinkShape(0.02f);
    
            if (osm.getSize()[0] == 0) {
                animator.setSize(new float[] { 20, 40 });
                animator.updateOSMSize();
                animator.switchShape(Shape.RECT);
            }

            if (osm.getSize().length == 2 && osm.getSize()[1] != 40) {
                animator.expandShape(0.01f);
            }
        }

        if (state == OrbState.TRANSFORM_TO_ORB) {
            if (osm.getSize().length == 2)
                animator.shrinkShape(0.01f);
            
            if (osm.getSize()[0] == 0) {
                animator.setSize(new float[] { WIDTH });
                animator.updateOSMSize();
                animator.switchShape(Shape.CIRCLE);
            }

            if (osm.getSize().length == 1 && osm.getSize()[0] != WIDTH)
                animator.expandShape(0.02f);
            
            if (animator.isConvertationDone()) {
                state = OrbState.ORB;
                animator.setConvertation(false);
            }
        }

        if (state == OrbState.SHOOTING) {
            shoot(dt, rotation);
            activateForceMode = true;
        }

        timeSinceLastShot += dt;

        if (timeSinceLastShot > 6f) {
            activateForceMode = false;
            timeSinceLastShot = 0f;
        }
        
        updateBullets();

        for (Bullet bullet : bullets) {
            bullet.update(dt);
        }
    }

    public void render(SpriteBatch batch) {
        batch.end();

        particles.draw(batch);
        osm.render(batch);

        if (activateForceMode) {
            activateForceMode();
            if (animator.getLineWidth() != 0) drawPolyOverOrb();
        } else {
            deactivateForceMode();
            if (animator.getLineWidth() != 0) drawPolyOverOrb();
        }

        batch.begin();
        for (Bullet bullet : bullets) {
            bullet.render(batch);
        }
    }

    private void updateBullets() {
        ListIterator<Bullet> bulletIterator = bullets.listIterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            world.getEnemies().forEach(e -> {
                if (bullet.getBounds().overlaps(e.getBounds())) {
                    if (e.EnemyHealth > 0) {
                        bulletIterator.remove();
                        e.gotShot(10);
                    }
                }
            });
        }
    }

    private void drawPolyOverOrb() {
        float length = WIDTH * (layerAmount + 1) * 0.8f;
        animator.drawPolygonOverObject(new float[] { 
            position.x + length, position.y,
            position.x + length / 2, (float) (position.y + (length / 2 * Math.sqrt(3))),
            position.x - length / 2, (float) (position.y + (length / 2 * Math.sqrt(3))),
            position.x - length, position.y,
            position.x - length / 2, (float) (position.y + -(length / 2 * Math.sqrt(3))),
            position.x + length / 2, (float) (position.y + -(length / 2 * Math.sqrt(3))),
            position.x + length, position.y,
        });
    }

    private void activateForceMode() {
        animator.increaseLineWidth(5f, 0.03f);
    }

    private void deactivateForceMode() {
        animator.decreaseLineWidth(0.03f);
    }

    private void shoot(float dt, float angle) {
        particles.setShooting(true, angle);

        float sine = (float) -Math.sin(Math.toRadians(angle));
        float cosine = (float) Math.cos(Math.toRadians(angle));
        position.add(-sine * shootKickback, -cosine * shootKickback);
        bullets.add(new Bullet(this, angle));

        timeSinceLastShot = 0f;
        state = OrbState.ORB;
    }

    private void floating(float dt) {
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
        
        position.x += dx * speed * dt;
    }
}
