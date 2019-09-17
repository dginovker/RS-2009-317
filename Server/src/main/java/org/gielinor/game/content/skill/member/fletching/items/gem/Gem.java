package org.gielinor.game.content.skill.member.fletching.items.gem;

import org.gielinor.game.node.item.Item;

/**
 * Represents gems to cut into boltTip tips.
 *
 * @author 'Vexia
 * @date 01/12/2013
 */
public enum Gem {
    OPAL(new Item(1609), new Item(45, 12), 11, 1.5),
    JADE(new Item(1611), new Item(9187, 12), 26, 2.4),
    PEARL(new Item(411), new Item(46, 6), 41, 3.5),
    BIGGER_PEARL(new Item(413), new Item(46, 24), 41, 3.5),
    RED_TOPAZ(new Item(1613), new Item(9188, 12), 48, 3.9),
    SAPPHIRE(new Item(1607), new Item(9189, 12), 56, 4),
    EMERALD(new Item(1605), new Item(9190, 12), 58, 5.5),
    RUBY(new Item(1603), new Item(9191, 12), 63, 6.3),
    DIAMOND(new Item(1601), new Item(9192, 12), 65, 7),
    DRAGONSTONE(new Item(1615), new Item(9193, 12), 71, 8.2),
    ONYX(new Item(6573), new Item(9194, 24), 73, 9.4);

    /**
     * Constructs a new {@code Gem.java} {@code Object}.
     *
     * @param gem        the gem.
     * @param boltTip    the boltTip.
     * @param level      the level.
     * @param experience the experience.
     */
    Gem(Item gem, Item boltTip, int level, double experience) {
        this.gem = gem;
        this.boltTip = boltTip;
        this.level = level;
        this.experience = experience;
    }

    /**
     * Represents the gem.
     */
    private final Item gem;

    /**
     * Represents the boltTip.
     */
    private final Item boltTip;

    /**
     * Represents the level required.
     */
    private final int level;

    /**
     * Represents the experience gained.
     */
    private final double experience;

    /**
     * Gets the gem.
     *
     * @return The gem.
     */
    public Item getGem() {
        return gem;
    }

    /**
     * Gets the boltTip.
     *
     * @return The boltTip.
     */
    public Item getBoltTip() {
        return boltTip;
    }

    /**
     * Gets the level.
     *
     * @return The level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the experience.
     *
     * @return The experience.
     */
    public double getExperience() {
        return experience;
    }

    /**
     * Method used to get a gem for the item.
     *
     * @param item the item.
     * @return the gem.
     */
    public static Gem forItem(final Item item) {
        for (Gem gem : values()) {
            if (gem.getGem().getId() == item.getId()) {
                return gem;
            }
        }
        return null;
    }

}
