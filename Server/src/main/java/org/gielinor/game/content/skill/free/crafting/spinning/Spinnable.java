package org.gielinor.game.content.skill.free.crafting.spinning;

import org.gielinor.game.node.item.Item;

/**
 * Represents a spinnable to be spun on a Spinning Wheel.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum Spinnable {

    WOOL(new Item(1737), new Item(1759), 1, 2.5, new int[][]{
        { 22662, 1 },
        { 22661, 5 },
        { 22660, 10 },
        { 22659, 0 },
    }, 22663),
    FLAX(new Item(1779), new Item(1777), 10, 15, new int[][]{
        { 22667, 1 },
        { 22666, 5 },
        { 22665, 10 },
        { 22664, 0 },
    }, 22668),
    MAGIC_ROOTS(new Item(6051), new Item(6038), 19, 30, new int[][]{
        { 22672, 1 },
        { 22671, 5 },
        { 22670, 10 },
        { 22669, 0 },
    }, 22673, 22674, 22675),
    TREE_ROOTS(new Item(6043), new Item(9438), 10, 15, new int[][]{
        { 22679, 1 },
        { 22678, 5 },
        { 22677, 10 },
        { 22676, 0 },
    }, 22680, 22681),
    TREE_ROOTS1(new Item(6045), new Item(9438), 10, 15, new int[][]{
        { 22679, 1 },
        { 22678, 5 },
        { 22677, 10 },
        { 22676, 0 },
    }),
    TREE_ROOTS2(new Item(6047), new Item(9438), 10, 15, new int[][]{
        { 22679, 1 },
        { 22678, 5 },
        { 22677, 10 },
        { 22676, 0 },
    }),
    TREE_ROOTS3(new Item(6049), new Item(9438), 10, 15, new int[][]{
        { 22679, 1 },
        { 22678, 5 },
        { 22677, 10 },
        { 22676, 0 },
    }),
    TREE_ROOTS4(new Item(6051), new Item(9438), 10, 15, new int[][]{
        { 22679, 1 },
        { 22678, 5 },
        { 22677, 10 },
        { 22676, 0 },
    }),
    TREE_ROOTS5(new Item(6053), new Item(9438), 10, 15, new int[][]{
        { 22679, 1 },
        { 22678, 5 },
        { 22677, 10 },
        { 22676, 0 },
    }),
    SINEW(new Item(9436), new Item(9438), 10, 15, new int[][]{
        { 22685, 1 },
        { 22684, 5 },
        { 22683, 10 },
        { 22682, 0 },
    }, 22686, 22687),
    YAK_HAIR(new Item(10814), new Item(954), 30, 25, new int[][]{
        { 22691, 1 },
        { 22690, 5 },
        { 22689, 10 },
        { 22688, 0 },
    }, 22692, 22693);

    /**
     * The item required.
     */
    private final Item item;
    /**
     * The product.
     */
    private final Item product;
    /**
     * The level required.
     */
    private final int level;
    /**
     * The experience given.
     */
    private final double experience;
    /**
     * The button data.
     */
    private final int[][] buttonData;
    /**
     * The ids of the text for level.
     */
    private final int[] textIds;

    /**
     * Constructs a new <code>Spinnable</code>.
     *
     * @param item       The item required.
     * @param product    The product.
     * @param level      The level required.
     * @param experience The experience given.
     * @param buttonData The button data.
     * @param textIds    The ids of the text for level.
     */
    Spinnable(Item item, Item product, int level, double experience, int[][] buttonData, int... textIds) {
        this.item = item;
        this.product = product;
        this.level = level;
        this.experience = experience;
        this.buttonData = buttonData;
        this.textIds = textIds;
    }

    /**
     * Gets a <code>Spinnable</code> by the id of the button.
     *
     * @param buttonId The id of the button.
     * @return The spinnable.
     */
    public static Spinnable forId(int buttonId) {
        for (Spinnable spinnable : Spinnable.values()) {
            for (int button[] : spinnable.getButtonData()) {
                if (button[0] == buttonId) {
                    return spinnable;
                }
            }
        }
        return null;
    }

    /**
     * Gets the amount by the id of the button.
     *
     * @param buttonId The id of the button.
     * @return The amount.
     */
    public static int getAmount(int buttonId) {
        for (Spinnable spinnable : Spinnable.values()) {
            for (int button[] : spinnable.getButtonData()) {
                if (button[0] == buttonId) {
                    return button[1];
                }
            }
        }
        return -1;
    }

    /**
     * Gets the item required.
     */
    public Item getItem() {
        return item;
    }

    /**
     * Gets the product.
     */
    public Item getProduct() {
        return product;
    }

    /**
     * Gets the level required.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the experience given.
     */
    public double getExperience() {
        return experience;
    }

    /**
     * Gets the button data.
     */
    public int[][] getButtonData() {
        return buttonData;
    }

    /**
     * Gets the ids of the text for level.
     */
    public int[] getTextIds() {
        return textIds;
    }
}
