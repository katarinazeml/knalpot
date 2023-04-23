package org.knalpot.knalpot.hud;

import org.knalpot.knalpot.addons.Constants;
import org.knalpot.knalpot.addons.effects.OSM;
import org.knalpot.knalpot.addons.effects.OSMAnimator;
import org.knalpot.knalpot.addons.effects.OSM.Shape;
import org.knalpot.knalpot.hud.HUD.HUDType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class HUDProcessor {
    private HUDType type;

    private OSM osm;
    private OSMAnimator animator;

    private Vector2 position;
    private float[] size;

    private boolean isActive;

    public HUDProcessor(HUDType type) {
        this.type = type;

        ShapeType shape;

        switch(this.type) {
            case STATS:
                shape = ShapeType.Filled;
                position = new Vector2(10, 10);
                size = new float[] { 50, 30 };
                break;
            case INVENTORY:
                shape = ShapeType.Line;
                position = new Vector2(
                    Constants.WINDOW_WIDTH / 2 - Constants.WINDOW_WIDTH / 4, 
                    Constants.WINDOW_HEIGHT / 2 - Constants.WINDOW_HEIGHT / 4
                );
                size = new float[] { 
                    Constants.WINDOW_WIDTH / 2, 
                    Constants.WINDOW_HEIGHT / 2
                };

                // DO NOT TOUCH WIP
                // float height = Constants.WINDOW_HEIGHT / 4;
                // size = new float[] { 
                //     position.x, position.y,
                //     position.x, position.y - height,
                //     position.x + Constants.WINDOW_WIDTH / 2, position.y - height,
                //     position.x + Constants.WINDOW_WIDTH / 2, position.y,
                //     position.x + Constants.WINDOW_WIDTH / 2, position.y + height,
                //     position.x, position.y + height,
                // };
                break;
            default:
                shape = ShapeType.Filled;
                break;
        }

        osm = new OSM(Shape.RECT, shape, Color.LIGHT_GRAY);
        animator = new OSMAnimator(osm);
    }

    public HUDProcessor(HUD hud, HUDType type, OSM osm, OSMAnimator animator) {
        this.type = type;
        this.osm = osm;
        this.animator = animator;
    }

    public void setOSMData() {
        osm.setPosition(position);
        animator.setSize(size);
        animator.setLineWidth(10f);
        animator.updateOSMSize();
    }

    public void updateHUD(float dt, boolean isActive) {
        this.isActive = isActive;
        if (!animator.isConvertationDone())
            if (this.isActive)
                animator.expandShape(dt);
            if (!this.isActive)
                animator.shrinkShape(dt);
    }

    public void renderHUD() {
        // batch.end();
        if (isActive) {
            osm.drawRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, new Color(0, 0, 0, 0.4f), true);
            osm.render();
        }
        // batch.begin();
    }
}
