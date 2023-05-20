package org.knalpot.knalpot.hud;

import org.knalpot.knalpot.addons.effects.OSM;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HUD {
    public enum HUDType {
        STATS,
        INVENTORY
    };

    private OSM osm;

    public HUD(HUDType type, OSM osm) {
        this.osm = osm;
    }

    public void update(float dt) { }

    public void render(SpriteBatch batch) {
        batch.end();
        osm.render(batch);
        batch.begin();
    }

    public void dispose() {
        osm.dispose();
    }
}
