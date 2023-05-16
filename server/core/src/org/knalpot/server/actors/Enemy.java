package org.knalpot.server.actors;

import java.util.concurrent.ThreadLocalRandom;

public class Enemy {

    public float x, y;
    public int id;
    public int direction = 1;
    public int health;
    
    private double dt;
    private int spd = 35;

    public Player target;

    public Enemy(float x, float y) {
        this.x = x;
        this.y = y;
        health = 100;
    }
    
    public void update() {
        dt += 0.1;

        if (dt >= 10) {
            direction = ThreadLocalRandom.current().nextInt(-1, 2);
            spd = ThreadLocalRandom.current().nextInt(30, 40);
            dt -= 10;
        }

        x += spd * direction * 0.01;
    }
}
