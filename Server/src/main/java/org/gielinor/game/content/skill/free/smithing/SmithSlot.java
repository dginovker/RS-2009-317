package org.gielinor.game.content.skill.free.smithing;

import org.gielinor.game.content.skill.free.smithing.SmithingConstants.Slot;
import org.gielinor.game.node.item.Item;

/**
 * Represents a smithing slot.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SmithSlot {

    /**
     * The slot type
     */
    private Slot slot;

    /**
     * The item to smith
     */
    private final Item item;

    /**
     * The level needed for this slot
     */
    private int level;

    /**
     * The amount of bars needed for this slot
     */
    private int bars;

    /**
     * @param item
     */
    public SmithSlot(Item item) {
        this.item = item;
    }

    /**
     * @param slot
     * @param item
     * @param level
     * @param bars
     */
    public SmithSlot(Slot slot, Item item, int level, int bars) {
        this.slot = slot;
        this.item = item;
        this.level = level;
        this.bars = bars;
    }

    /**
     * @param slot
     * @param item
     * @param level
     */
    public SmithSlot(Slot slot, Item item, int level) {
        this.slot = slot;
        this.item = item;
        this.level = level;
        this.bars = 1;
    }

    /**
     * The slot type
     *
     * @return the slot
     */
    public Slot getSlot() {
        return slot;
    }

    /**
     * The item to smith
     *
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * The level needed for this slot
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * The amount of bars needed for this slot
     *
     * @return the bars
     */
    public int getBars() {
        return bars;
    }
}
