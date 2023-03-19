package ServerPlayer;

import java.io.Serializable;

public class ServerPlayer implements Serializable {
    private float x;
    private float y;

    public ServerPlayer() {}

    public ServerPlayer(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
