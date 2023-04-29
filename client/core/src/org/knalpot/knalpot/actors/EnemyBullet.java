package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.addons.BBGenerator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class EnemyBullet extends Actor {

    private Enemy enemy;
    private float speed = 500f;
    private float angle;

    public EnemyBullet(Enemy enemy, float angle) {
        this.enemy = enemy;
        this.angle = angle;
        scaleSize = 2;

        texture = new Texture("redSquare.png");
        BBSize = BBGenerator.BBPixels(texture.getTextureData());

        WIDTH = texture.getWidth();
        HEIGHT = texture.getHeight();

        // set position to the center of the enemy
        position = new Vector2(enemy.getPosition().x, 
        enemy.getPosition().y + 40);

        velocity = new Vector2();
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    @Override
    public void update(float dt) {
        float sine = (float) -Math.sin(Math.toRadians(angle));
        float cosine = (float) Math.cos(Math.toRadians(angle));
        velocity.set((sine * speed), (cosine * speed));
        position.add(velocity.x * dt, velocity.y * dt);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }
}
