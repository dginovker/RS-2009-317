package org.gielinor.game.content.skill.free.crafting;

import org.gielinor.game.node.item.Item;

/**
 * Represents a hide for tanning.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum Hide {

    SOFT_LEATHER("Soft leather", new Item(1739), 1, 1, new Item(1741),
        new int[]{ 14817, 1 },
        new int[]{ 14809, 5 },
        new int[]{ 14801, -1 },
        new int[]{ 14793, 28 }
    ),
    HARD_LEATHER("Hard leather", new Item(1739), 3, 28, new Item(1743),
        new int[]{ 14818, 1 },
        new int[]{ 14810, 5 },
        new int[]{ 14802, -1 },
        new int[]{ 14794, 28 }
    ),
    SNAKESKIN("Snakeskin", new Item(6287), 15, 45, new Item(6289),
        new int[]{ 14819, 1 },
        new int[]{ 14811, 5 },
        new int[]{ 14803, -1 },
        new int[]{ 14795, 28 }
    ),
    SNAKESKIN_2("Snakeskin", new Item(7801), 20, 45, new Item(6289),
        new int[]{ 14820, 1 },
        new int[]{ 14812, 5 },
        new int[]{ 14804, -1 },
        new int[]{ 14796, 28 }
    ),
    GREEN_DRAGONHIDE("Green d'hide", new Item(1753), 20, 57, new Item(1745),
        new int[]{ 14821, 1 },
        new int[]{ 14813, 5 },
        new int[]{ 14805, -1 },
        new int[]{ 14797, 28 }
    ),
    BLUE_DRAGONHIDE("Blue d'hide", new Item(1751), 20, 66, new Item(2505),
        new int[]{ 14822, 1 },
        new int[]{ 14814, 5 },
        new int[]{ 14806, -1 },
        new int[]{ 14798, 28 }
    ),
    RED_DRAGONHIDE("Red d'hide", new Item(1749), 20, 73, new Item(2507),
        new int[]{ 14823, 1 },
        new int[]{ 14815, 5 },
        new int[]{ 14807, -1 },
        new int[]{ 14799, 28 }
    ),
    BLACK_DRAGONHIDE("Black d'hide", new Item(1747), 20, 79, new Item(2509),
        new int[]{ 14824, 1 },
        new int[]{ 14816, 5 },
        new int[]{ 14808, -1 },
        new int[]{ 14800, 28 }
    );

    /**
     * The name of the leather
     */
    private final String name;
    /**
     * The raw item needed
     */
    private final Item raw;
    /**
     * The hide tanning cost
     */
    private final int cost;
    /**
     * The level required to tan the hide
     */
    private final int level;
    /**
     * The product (tanned hide)
     */
    private final Item product;
    /**
     * The button data for tanning
     */
    private final int[][] buttons;

    /**
     * Represents a tannable hide
     *
     * @param name
     * @param raw
     * @param cost
     * @param level
     * @param product
     * @param buttons
     */
    Hide(String name, Item raw, int cost, int level, Item product, int[]
        ... buttons) {
        this.name = name;
        this.raw = raw;
        this.cost = cost;
        this.level = level;
        this.product = product;
        this.buttons = buttons;
    }

    /**
     * The name of the hide
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * The raw item needed
     *
     * @return the raw item
     */
    public Item getRaw() {
        return raw;
    }

    /**
     * The hide tanning cost
     *
     * @return the cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * The level required to tan the hide
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * The product (tanned hide)
     *
     * @return the product
     */
    public Item getProduct() {
        return product;
    }

    /**
     * The buttons data
     *
     * @return the buttons data
     */
    public int[][] getButtons() {
        return buttons;
    }

    /**
     * Gets the hide by a button id
     *
     * @param id the id of the button
     * @return the hide
     */
    public static Hide forId(int id) {
        for (Hide hide : Hide.values()) {
            for (int[] ids : hide.getButtons()) {
                if (id == ids[0]) {
                    return hide;
                }
            }
        }
        return null;
    }

    /**
     * Gets the tan amount by a button id
     *
     * @param id the id of the button
     * @return the tan amount
     */
    public static int getAmount(int id) {
        for (Hide hide : Hide.values()) {
            for (int[] ids : hide.getButtons()) {
                if (id == ids[0]) {
                    return ids[1];
                }
            }
        }
        return 0;
    }
}
