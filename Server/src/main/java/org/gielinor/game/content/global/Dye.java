package org.gielinor.game.content.global;

import org.gielinor.game.node.item.Item;

/**
 * Represents a dye.
 *
 * @author Vexia
 */
public enum Dye {
    RED(new Item(1763)),
    YELLOW(new Item(1765)),
    BLUE(new Item(1767)),
    ORANGE(new Item(1769)),
    GREEN(new Item(1771)),
    PURPLE(new Item(1773)),
    PINK(new Item(6955));

    /**
     * The dye item.
     */
    private final Item item;

    /**
     * Constructs a new {@code Dyes} {@code Object}.
     *
     * @param item the item.
     */
    private Dye(Item item) {
        this.item = item;
    }

    /**
     * Gets the dye for the item.
     *
     * @param item the item.
     * @return the dye.
     */
    public static Dye forItem(Item item) {
        for (Dye d : values()) {
            if (d.getItem().getId() == item.getId()) {
                return d;
            }
        }
        return null;
    }

    /**
     * Gets the item.
     *
     * @return The item.
     */
    public Item getItem() {
        return item;
    }
}
