package org.knalpot.knalpot.actors;

import java.util.ArrayList;

import org.knalpot.knalpot.addons.BBGenerator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Actor {

    public enum EnemyState {
        IDLE, MOVE, JUMP, ATTACK
    }

    public final String EnemyState = null;

    private ArrayList<EnemyBullet> bullets;

    private EnemyState enemyState;

    public boolean attack = false;

    public float shootingCooldown = 1f;

    public float timeSinceLastShot = 0f;

    public int health = 100;

    public Enemy(Vector2 position) {
        this.position = position;
    
        texture = new Texture("lavamonster.png");
        BBSize = BBGenerator.BBPixels(texture.getTextureData());
        scaleSize = 2; // Update scaleSize to 2
        direction = 1;
        previousDirection = 1;
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
        bullets = new ArrayList<>();
        
        bounds = new Rectangle(position.x, position.y, BBSize[0] * scaleSize, BBSize[1] * scaleSize);
    
        Left = (int) bounds.x;
        Right = (int) (bounds.x + bounds.width);
        Bottom = (int) bounds.y;
        Top = (int) (bounds.y + bounds.height);
    }

    @Override
    public void update(float dt) {
        
        timeSinceLastShot += dt;
        // Update enemy position using the Actor class's position
        position.add(velocity.cpy().scl(dt));
        bounds.x = position.x;
        bounds.y = position.y;

        Left = (int) bounds.x;
        Right = (int) bounds.x + WIDTH;
        Bottom = (int) bounds.y;
        Top = (int) bounds.y + HEIGHT;

        for (EnemyBullet bullet : bullets) {
            bullet.update(dt);
        }
    }

    public void shoot(Vector2 targetPos) {
        bullets.add(new EnemyBullet(this, targetPos.cpy()));
    }

    public void setEnemyDirection(int direction) {
        this.direction = direction;
    }

    public int getEnemyDirection() {
        return this.direction;
    }

    public void setState(EnemyState state) {
        this.enemyState = state;
    }
    
    public EnemyState getState() {
        return enemyState;
    }

    public ArrayList<EnemyBullet> getEnemyBullets() {
        return bullets;
    }

    public void gotShot(int damage) {
        health -= damage;
        if (health < 0) health = 0;
        //System.out.println("enemy`s health: " + health);
    }
}
