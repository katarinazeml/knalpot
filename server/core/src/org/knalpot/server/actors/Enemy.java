package org.knalpot.server.actors;

import java.util.concurrent.ThreadLocalRandom;

public class Enemy {

    public float x, y;
    public int id;
    public int direction;
    public int health;
    
    private double dt;
    private double spd;

    public Enemy(float x, float y) {
        this.x = x;
        this.y = y;
        health = 100;
    }
    
    public void update() {
        dt += 0.1;
        if (dt >= 100) {
            direction = ThreadLocalRandom.current().nextInt(-1, 2);
            spd = ThreadLocalRandom.current().nextDouble(0, 1);
            dt -= 100;
        }

        x += spd * direction * 0.005;
    }
}
