package org.knalpot.knalpot.addons;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/**
 * 
 */
public class ParticleGenerator {
    private int MAX_AMOUNT = 7;
    private int SIZE = 6;
    private float timer;

    private Vector2 START_POS;

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
        timer += dt;
        if (timer >= 0.08) {
            GenerateParticles();
            timer -= 0.08;
        }

        for (Particle particle : list) {
            particle.update(dt);
        }
    }

    /**
     * Draws all particles in one batch.
     * @param batch
     */
    public void draw(OrthographicCamera camera) {
        // batch.end();
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeType.Filled);
        shape.setColor(129 / 255f, 161 / 255f, 193 / 255f, 0.8f);

        for (Particle particle : list) {
            shape.rect(particle.getPosition().x, particle.getPosition().y, SIZE, SIZE);
        }

        shape.end();
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
