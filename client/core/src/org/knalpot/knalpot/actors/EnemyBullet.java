package org.knalpot.knalpot.actors;

import org.knalpot.knalpot.addons.BBGenerator;
import org.knalpot.knalpot.world.World;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class EnemyBullet extends Actor {

    private Enemy enemy;
    private float speed = 500f;
    private float angle;
    private Player player;
    private Vector2 enemyBulletDirection;
    private Vector2 bulletPosition;

    public EnemyBullet(Enemy enemy, Player player) {
        this.enemy = enemy;
        this.player = player;
        scaleSize = 2;
    
        texture = new Texture("redSquare.png");
        BBSize = BBGenerator.BBPixels(texture.getTextureData());
    
        WIDTH = texture.getWidth();
        HEIGHT = texture.getHeight();
    
        bulletPosition = new Vector2(enemy.getPosition().x, enemy.getPosition().y + 40);
    
        // Calculate the direction towards the center of the player
        Vector2 playerCenter = new Vector2(player.getPosition().x + player.getWidth() / 2,
                                           player.getPosition().y + player.getHeight() / 2);
        enemyBulletDirection = playerCenter.sub(bulletPosition).nor();
    
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
