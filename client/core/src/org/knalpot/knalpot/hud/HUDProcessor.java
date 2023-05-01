package org.knalpot.knalpot.hud;

import java.util.List;

import org.knalpot.knalpot.addons.Constants;
import org.knalpot.knalpot.addons.effects.OSM;
import org.knalpot.knalpot.addons.effects.OSMAnimator;
import org.knalpot.knalpot.addons.effects.OSM.Shape;
import org.knalpot.knalpot.hud.HUD.HUDType;
import org.knalpot.knalpot.interactive.props.Consumable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class HUDProcessor {
    // Specific type for future processing.
    private HUDType type;

    // OSM for rendering.
    private OSM osm;
    private OSMAnimator animator;

    // Typical data.
    private Vector2 position;
    private float[] size;
    private boolean isActive;

    // temporary inventory stuff.
    // this code is probably a bad idea but i need it
    // for testing purposes.
    private SpriteBatch batch;
    private List<Consumable> inventory;
    private int currentConsum = 0;
    private float speed = 4f;

    // Fonts.
    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Pixeboy.ttf"));
    private FreeTypeFontParameter parameter = new FreeTypeFontParameter();
    private BitmapFont font;

    public HUDProcessor(HUDType type) {
        this.type = type;

        ShapeType shape;

        parameter.size = 24;
        font = generator.generateFont(parameter);
        font.getData().setScale(2);
        generator.dispose();

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

                batch = new SpriteBatch();
                break;
            default:
                shape = ShapeType.Filled;
                break;
        }

        osm = new OSM(Shape.RECT, shape, Color.LIGHT_GRAY);
        animator = new OSMAnimator(osm);
    }

    public int getCurrentConsum() {
        return currentConsum;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void decreaseCurrentConsum() {
        currentConsum = 0;
    }

    public void setOSMData() {
        osm.setPosition(position);
        animator.setSize(size);
        animator.setLineWidth(10f);
        animator.updateOSMSize();
    }

    public void initializeInventory(List<Consumable> consumables) {
        inventory = consumables;
        for (int i = 0; i < inventory.size(); i++) {
            Consumable consumable = inventory.get(i);
            consumable.setPosition(
                Constants.WINDOW_WIDTH / 2 - consumable.getWidth() / 2 + consumable.getWidth() * 3 * (i - currentConsum), 
                Constants.WINDOW_HEIGHT / 2
            );
        }
    }

    public void setTargetPosition() {
        for (int i = 0; i < inventory.size(); i++) {
            Consumable consumable = inventory.get(i);
            consumable.setTargetPos(
                Constants.WINDOW_WIDTH / 2 - consumable.getWidth() / 2 + consumable.getWidth() * 3 * (i - currentConsum), 
                Constants.WINDOW_HEIGHT / 2
            );
        }
    }
    
    public void updateConsumPosition(float dt) {
        for (Consumable consumable : inventory) {
            consumable.setScale(1f);

            if (consumable.getTargetPos() != null) {
                float dx = consumable.getTargetPos().x - consumable.getPosition().x;

                if (inventory.indexOf(consumable) == currentConsum) consumable.setScale(2 - Math.abs(dx / 100));
                consumable.changePosition(dx * speed, 0, dt);
            }
        }
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void changeActive() {
        isActive = !isActive;
    }

    public void updateInventory(List<Consumable> consumables) {
        this.inventory = consumables;
    }

    public void update(float dt) {
        if (isActive) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                currentConsum += 1;
                currentConsum %= inventory.size();
                setTargetPosition();
            }
            
            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                currentConsum -= 1;
                currentConsum = (currentConsum % inventory.size() + inventory.size()) % inventory.size();
                setTargetPosition();
            }
        }

        updateConsumPosition(dt);

        if (!animator.isConvertationDone())
            if (isActive)
                animator.expandShape(dt);

            if (!isActive)
                animator.shrinkShape(dt);
    }

    public void render() {
        // batch.end();
        if (isActive) {
            osm.drawRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, new Color(0, 0, 0, 0.4f), true);
            osm.render();

            for (Consumable consumable : inventory) {
                consumable.render(batch);
            }

            if (inventory.size() != 0) {
                batch.begin();
    
                Consumable consumable = inventory.get(currentConsum);
                font.draw(batch, consumable.getName(),
                    Constants.WINDOW_WIDTH / 2 - (consumable.getName().length() * 24) / 2, Constants.WINDOW_HEIGHT / 2 - Constants.WINDOW_HEIGHT / 6);
    
                batch.end();
            }
        }
        // batch.begin();
    }
}
