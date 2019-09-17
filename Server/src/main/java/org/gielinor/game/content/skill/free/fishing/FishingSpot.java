package org.gielinor.game.content.skill.free.fishing;


/**
 * Represents a fishing spot.
 *
 * @author Emperor
 */
public enum FishingSpot {

    NET_BAIT(new int[]{ 952, 316, 319, 320, 323, 325, 326, 327, 330, 332, 404, 1331, 2067, 2068, 2724, 4908, 5748, 5749, 7045 }, FishingOption.SMALL_NET, FishingOption.BAIT),
    LURE_BAIT(new int[]{ 309, 310, 311, 314, 315, 317, 318, 328, 329, 331, 403, 927, 1189, 1190, 3019 }, FishingOption.LURE, FishingOption.L_BAIT),
    CAGE_HARPOON(new int[]{ 312, 321, 324, 333, 405, 1332, 1399, 3804, 5470, 7046 }, FishingOption.CAGE, FishingOption.HARPOON),
    NET_HARPOON(new int[]{ 313, 322, 334, 406, 1191, 1333, 1405, 1406, 3574, 3575, 5471, 7044 }, FishingOption.BIG_NET, FishingOption.N_HARPOON),
    HARPOON_NET(new int[]{ 3848, 3849 }, FishingOption.HARPOON, FishingOption.H_NET);

    /**
     * Gets the FishingSpot for the given NPC id.
     *
     * @param npcId The npc id.
     * @return The fishing spot.
     */
    public static FishingSpot forId(int npcId) {
        for (FishingSpot spot : FishingSpot.values()) {
            for (int id : spot.ids) {
                if (id == npcId) {
                    return spot;
                }
            }
        }
        return null;
    }

    /**
     * Gets the FishingSpot for the given NPC id.
     *
     * @param npcId The npc id.
     * @return The fishing spot.
     */
    public static Object[] forId2(int npcId) {
        for (FishingSpot spot : FishingSpot.values()) {
            int spotIndex = 0;
            for (int id : spot.ids) {
                if (id == npcId) {
                    return new Object[]{ spot, spotIndex };
                }
                spotIndex++;
            }
        }
        return null;
    }

    /**
     * The NPC ids.
     */
    private final int[] ids;

    /**
     * The fishing options.
     */
    private FishingOption[] options;

    /**
     * Constructs a new {@code FishingSpot} {@code Object}.
     *
     * @param ids     The NPC ids.
     * @param options The fishing options.
     */
    private FishingSpot(int[] ids, FishingOption... options) {
        this.ids = ids;
        this.options = options;
    }

    /**
     * Gets the ids.
     *
     * @return The ids.
     */
    public int[] getIds() {
        return ids;
    }

    /**
     * Gets the options.
     *
     * @return The options.
     */
    public FishingOption[] getOptions() {
        return options;
    }

}
