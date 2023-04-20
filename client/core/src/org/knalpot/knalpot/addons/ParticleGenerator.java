package org.knalpot.knalpot.addons;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/**
 * 
 */
public class ParticleGenerator {
    private int MAX_AMOUNT = 7;
    private int SIZE = 6;
    private float particlesTimer;

    private float ringWidth = 6f;
    private float ringHeight = 12f;
    private float ringTimer;
    private float ringScale = 3f;
    private float ringRotation;
    private boolean isShooting = false;

    private Vector2 START_POS;
    private Vector2 posCP;

    private ArrayList<Particle> list;
    private ShapeRenderer shape;

    /**
     * Particles constructor.
     * @param position
     */
    public ParticleGenerator(Vector2 position) {
        START_POS = position;

        list = new ArrayList<>(MAX_AMOUNT);
        shape = new ShapeRenderer();
    }

    private void GenerateParticles() { 
        list.add(new Particle(START_POS));
        if (list.size() >= MAX_AMOUNT) {
            list.remove(0);
        }
    }

    /**
     * Updates position of each particle (movement is chaotic).
     * @param dt
     */
    public void update(float dt) {
        particlesTimer += dt;
        ringTimer += dt;

        if (particlesTimer >= 0.08) {
            GenerateParticles();
            particlesTimer -= 0.08;
        }
        
        if (isShooting) {
            if (ringTimer >= 0.04) {
                ringScale -= 0.1f;
                ringTimer -= 0.04;
            }
            if (ringScale <= 0f) {
                ringScale = 3f;
                isShooting = false;
            }
        }

        for (Particle particle : list) {
            particle.update(dt);
        }
    }

    public void setShooting(boolean isShooting, float ellipseRotation) {
        this.isShooting = isShooting;
        this.ringRotation = ellipseRotation - 90;
        posCP = START_POS.cpy();
    }

    public void drawShootingRing() {
        Gdx.gl.glLineWidth(8f);
        // float sine = (float) -Math.sin(Math.toRadians(ringRotation));
        float cosine = (float) Math.cos(Math.toRadians(ringRotation));
        shape.begin(ShapeType.Line);
        shape.setColor(216 / 255f, 222 / 255f, 233 / 255f, 0.8f);
        shape.ellipse(posCP.x + (15f * Math.signum(-cosine)), posCP.y - 8f, ringWidth * ringScale, ringHeight * ringScale, ringRotation);
        shape.end();
    }

    /**
     * Draws all particles in one batch.
     * @param batch
     */
    public void draw(SpriteBatch batch) {
        // batch.end();
        shape.setProjectionMatrix(batch.getProjectionMatrix());
        shape.begin(ShapeType.Filled);
        shape.setColor(129 / 255f, 161 / 255f, 193 / 255f, 0.8f);

        for (Particle particle : list) {
            shape.rect(particle.getPosition().x, particle.getPosition().y, SIZE, SIZE);
        }
        shape.end();

        if (isShooting) {
            drawShootingRing();
        }
        // batch.begin();
    }

    /**
     * Empties the list, basically deletes all particles.
     */
    public void end() {
        list.clear();
        shape.dispose();
    }
}
