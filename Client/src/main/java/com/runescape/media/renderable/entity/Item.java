package com.runescape.media.renderable.entity;

import com.runescape.cache.def.item.ItemDefinition;
import com.runescape.media.renderable.Model;
import com.runescape.media.renderable.Renderable;

/**
 * Represents an Item.
 *
 * @author Jagex
 */
public final class Item extends Renderable {

    public final Model getRotatedModel() {
        ItemDefinition itemDef = ItemDefinition.forId(ID);
        return itemDef.getModel(anInt1559);
    }

    public int ID;
    public int x;
    public int y;
    public int anInt1559;
}
