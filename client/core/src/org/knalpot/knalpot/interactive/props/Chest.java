package org.knalpot.knalpot.interactive.props;

import java.util.ArrayList;
import java.util.List;

import org.knalpot.knalpot.hud.HUDProcessor;
import org.knalpot.knalpot.hud.HUD.HUDType;
import org.knalpot.knalpot.interactive.Static;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Chest extends Static {
    private int maxItemsAmount = 5;
    private List<Consumable> consumables;
    private HUDProcessor chestHud;

    public Chest(Vector2 position, int width, int height) {
        super(position, width, height);
        consumables = new ArrayList<>(maxItemsAmount);
    }

    public Chest(Vector2 position, int width, int height, Texture texture) {
        super(position, width, height, texture);
        consumables = new ArrayList<>(maxItemsAmount);
    }

    public void initializeChestHUD() {
        chestHud = new HUDProcessor(HUDType.INVENTORY);
        chestHud.setOSMData();
        chestHud.initializeInventory(consumables);
    }

    public HUDProcessor getHUD() {
        return chestHud;
    }

    public List<Consumable> getConsumables() {
        return consumables;
    }

    public void addConsumable(Consumable consumable) {
        consumables.add(consumable);
    }

    public void removeConsumable(Consumable consumable) {
        consumables.remove(consumable);
    }

    public Consumable getConsumableOfIndex(int index) {
        return consumables.get(index);
    }

    public void update(float dt) {
        chestHud.updateInventory(consumables);
        chestHud.update(dt);
    }
}
