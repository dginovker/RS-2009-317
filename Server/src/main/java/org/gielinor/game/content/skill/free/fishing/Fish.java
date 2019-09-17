package org.gielinor.game.content.skill.free.fishing;

import org.gielinor.game.node.item.Item;

/**
 * Represents the multiple types of <b>Fish</b> you can catch.
 *
 * @author 'Vexia
 */
public enum Fish {
    SHRIMP(new Item(317), 1, 10),
    SARDINE(new Item(327), 5, 20),
    KARAMBWANJI(new Item(3150), 5, 5),
    HERRING(new Item(345), 10, 30),
    ANCHOVIE(new Item(321), 15, 40),
    MACKEREL(new Item(353), 16, 20),
    TROUT(new Item(335), 20, 50),
    COD(new Item(341), 23, 45),
    PIKE(new Item(349), 25, 60),
    SLIMY_EEL(new Item(3379), 28, 65),
    SALMON(new Item(331), 30, 70),
    FROG_SPAWN(new Item(5004), 33, 75),
    TUNA(new Item(359), 35, 80),
    RAINBOW_FISH(new Item(10138), 38, 80),
    CAVE_EEL(new Item(5001), 38, 80),
    LOBSTER(new Item(377), 40, 90),
    BASS(new Item(363), 46, 100),
    SWORDFISH(new Item(371), 50, 100),
    LAVA_EEL(new Item(2148), 53, 100),
    MONKFISH(new Item(7944), 62, 120),
    KARAMBWAN(new Item(3142), 65, 105),
    SHARK(new Item(383), 76, 110),
    SEA_TURTLE(new Item(395), 79, 38),
    MANTA_RAY(new Item(389), 81, 46),
    ANGLER_FISH(new Item(33439), 82, 120),
    DARK_CRAB(new Item(31934), 85, 130),

    SEAWEED(new Item(401), 16, 1),
    CASKET(new Item(405), 16, 10),
    OYSTER(new Item(407), 16, 10),

    SACRED_EEL(new Item(33339), 87, 105),
    LEAPING_TROUT(new Item(31328), 48, 50),
    LEAPING_SALMON(new Item(31330), 58, 70),
    LEAPING_STRUGEON(new Item(31332), 70, 80),

    ;

    /**
     * Constructs a new {@code Fish} {@code Object}.
     *
     * @param item       the <code>Item</code>
     * @param level      the level.
     * @param experience the experience.
     */
    Fish(final Item item, final int level, final double experience) {
        this.item = item;
        this.level = level;
        this.experience = experience;
    }

    /**
     * Represents the {@link Item} of this <code>Fish</code>.
     */
    private final Item item;

    /**
     * Represents the required level to catch the {@link Fish}.
     */
    private final int level;

    /**
     * Represents the experience gained from this fish.
     */
    private final double experience;

    /**
     * @return the item.
     */
    public Item getItem() {
        return item;
    }

    /**
     * @return the level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return the experience.
     */
    public double getExperience() {
        return experience;
    }
}
