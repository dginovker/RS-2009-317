package org.gielinor.game.content.skill.free.magic;

import plugin.interaction.item.withitem.OrnamentsPlugin.Ornament;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a magic staff.
 *
 * @author Emperor
 */
public enum MagicStaff {

    /**
     * Represents the fire rune staves.
     */
    FIRE_RUNE(554, 1387, 1393, 1401, 3053, 3055, 3056, Ornament.MYSTIC_STEAM_STAFF.getUseWithId(),
        Ornament.MYSTIC_STEAM_STAFF.getProduct(),
        Ornament.MYSTIC_LAVA_STAFF.getUseWithId(),
        Ornament.MYSTIC_LAVA_STAFF.getProduct(),
        Ornament.STEAM_BATTLESTAFF.getUseWithId(),
        Ornament.STEAM_BATTLESTAFF.getProduct(),
        Ornament.LAVA_BATTLESTAFF.getUseWithId(),
        Ornament.LAVA_BATTLESTAFF.getProduct(),
        31998, 32000),
    // smoke staffs

    /**
     * Represents the water rune staves.
     */
    WATER_RUNE(555, 1383, 1395, 1403, 6563, Ornament.MYSTIC_STEAM_STAFF.getUseWithId(),
        Ornament.MYSTIC_STEAM_STAFF.getProduct(),
        Ornament.STEAM_BATTLESTAFF.getUseWithId(),
        Ornament.STEAM_BATTLESTAFF.getProduct(),
        40730, 40733),
    // mist staffs

    /**
     * Represents the air rune staves.
     */
    AIR_RUNE(556, 1381, 1397, 1405, 31998, 32000, 40736, 40739, 40730, 40733),

    /**
     * Represents the earth rune staves.
     */
    EARTH_RUNE(557, 3053, 3055, 3056, 1385, 1399, 1407, 557, 6563, Ornament.MYSTIC_LAVA_STAFF.getUseWithId(),
        Ornament.MYSTIC_LAVA_STAFF.getProduct(),
        Ornament.LAVA_BATTLESTAFF.getUseWithId(),
        Ornament.LAVA_BATTLESTAFF.getProduct(),
        40736, 40739); // dust staffs

    /**
     * The magic staves mapping.
     */
    private static final Map<Integer, MagicStaff> MAGIC_STAVES = new HashMap<Integer, MagicStaff>();

    /**
     * Populate the mapping.
     */
    static {
        for (MagicStaff m : MagicStaff.values()) {
            MAGIC_STAVES.put(m.getRuneId(), m);
        }
    }

    /**
     * Gets a magic staff from the mapping.
     *
     * @param runeId The rune id.
     * @return The magic staff instance.
     */
    public static MagicStaff forId(int runeId) {
        return MAGIC_STAVES.get(runeId);
    }

    /**
     * The rune id.
     */
    private final int runeId;

    /**
     * The staves.
     */
    private final int[] staves;

    /**
     * Constructs a new {@code MagicStaff} {@code Object}.
     *
     * @param runeId The rune id.
     * @param staves The possible staff item ids.
     */
    private MagicStaff(int runeId, int... staves) {
        this.runeId = runeId;
        this.staves = staves;
    }

    /**
     * @return the runeId
     */
    public int getRuneId() {
        return runeId;
    }

    /**
     * @return the staves
     */
    public int[] getStaves() {
        return staves;
    }
}
