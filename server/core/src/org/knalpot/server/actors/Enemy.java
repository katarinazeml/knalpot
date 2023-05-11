package org.knalpot.server.actors;

public class Enemy {

    public float x, y;
    public int id;

    public Enemy(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void update() {
        // Update the enemy's position etc.
    }
}
