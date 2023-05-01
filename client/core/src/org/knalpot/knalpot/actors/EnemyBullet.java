package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.addons.BBGenerator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class EnemyBullet extends Actor {

    private Enemy enemy;
    private float speed = 200f;
    private Vector2 enemyBulletDirection;
    private Vector2 bulletPosition;

    public EnemyBullet(Enemy enemy, Vector2 targetPos) {
        this.enemy = enemy;
        scaleSize = 2;
    
        texture = new Texture("redSquare.png");
        BBSize = BBGenerator.BBPixels(texture.getTextureData());
    
        WIDTH = texture.getWidth() * scaleSize;
        HEIGHT = texture.getHeight() * scaleSize;
        
        bulletPosition = new Vector2(enemy.getPosition().x, enemy.getPosition().y + 40);
        
        // Bounds
        bounds = new Rectangle();
        bounds.x = bulletPosition.x;
        bounds.y = bulletPosition.y;
        bounds.width = BBSize[0] * scaleSize;
        bounds.height = BBSize[1] * scaleSize;
    
        // Calculate the direction towards the center of the player
        enemyBulletDirection = targetPos.sub(bulletPosition).nor();
    
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
        velocity.set(enemyBulletDirection.x * speed, enemyBulletDirection.y * speed);
        bulletPosition.add(velocity.x * dt, velocity.y * dt);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, bulletPosition.x, bulletPosition.y);
    }
}
