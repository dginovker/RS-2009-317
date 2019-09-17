package org.gielinor.game.node.entity.combat.equipment;

/**
 * Represents an autocast spell.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum AutocastSpell {

    WIND_STRIKE(1830, 1152),
    WATER_STRIKE(1831, 1154),
    EARTH_STRIKE(1832, 1156),
    FIRE_STRIKE(1833, 1158),
    WIND_BOLT(1834, 1160),
    WATER_BOLT(1835, 1163),
    EARTH_BOLT(1836, 1166),
    FIRE_BOLT(1837, 1169),
    WIND_BLAST(1838, 1172),
    WATER_BLAST(1839, 1175),
    EARTH_BLAST(1840, 1177),
    FIRE_BLAST(1841, 1181),
    WIND_WAVE(1842, 1183),
    WATER_WAVE(1843, 1185),
    EARTH_WAVE(1844, 1188),
    FIRE_WAVE(1845, 1189),

    SMOKE_RUSH(13189, 12939, true),
    SHADOW_RUSH(13241, 12987, true),
    BLOOD_RUSH(13147, 12901, true),
    ICE_RUSH(6162, 12861, true),
    SMOKE_BURST(13215, 13011, true),
    SHADOW_BURST(13267, 12963, true),
    BLOOD_BURST(13167, 12919, true),
    ICE_BURST(13125, 12881, true),
    SMOKE_BLITZ(13202, 12951, true),
    SHADOW_BLITZ(13254, 12999, true),
    BLOOD_BLITZ(13158, 12911, true),
    ICE_BLITZ(13114, 12871, true),
    SMOKE_BARRAGE(13228, 12975, true),
    SHADOW_BARRAGE(13280, 13023, true),
    BLOOD_BARRAGE(13178, 12929, true),
    ICE_BARRAGE(13136, 12891, true),

    CRUMBLE_UNDEAD(12052, 1171, false, true),
    MAGIC_DART(12051, 12037, false, true),
    WIND_WAVE_SLAYER(12053, 1183, false, true),
    WATER_WAVE_SLAYER(12054, 1185, false, true),
    EARTH_WAVE_SLAYER(12055, 1188, false, true),
    FIRE_WAVE_SLAYER(12056, 1189, false, true),;

    /**
     * The id of the button.
     */
    private final int buttonId;
    /**
     * The id of the spell.
     */
    private final int spellId;
    /**
     * If the autocast button is ancients.
     */
    private final boolean ancients;
    /**
     * If the autocast button is Slayer.
     */
    private final boolean slayer;

    /**
     * Constructs a new {@link org.gielinor.game.node.entity.combat.equipment.AutocastSpell}.
     *
     * @param buttonId The id of the button.
     * @param spellId  The id of the spell.
     * @param ancients If the autocast button is ancients.
     * @param slayer   If the autocast button is Slayer.
     */
    AutocastSpell(int buttonId, int spellId, boolean ancients, boolean slayer) {
        this.buttonId = buttonId;
        this.spellId = spellId;
        this.ancients = ancients;
        this.slayer = slayer;
    }

    /**
     * Constructs a new {@link org.gielinor.game.node.entity.combat.equipment.AutocastSpell}.
     *
     * @param buttonId The id of the button.
     * @param spellId  The id of the spell.
     * @param ancients If the autocast button is ancients.
     */
    AutocastSpell(int buttonId, int spellId, boolean ancients) {
        this(buttonId, spellId, ancients, false);
    }

    /**
     * Constructs a new {@link org.gielinor.game.node.entity.combat.equipment.AutocastSpell}.
     *
     * @param buttonId The id of the button.
     * @param spellId  The id of the spell.
     */
    AutocastSpell(int buttonId, int spellId) {
        this(buttonId, spellId, false);
    }

    /**
     * Gets the id of the button.
     *
     * @return The id of the button.
     */
    public int getButtonId() {
        return buttonId;
    }

    /**
     * Gets te id of the spell.
     *
     * @return The id of the spell.
     */
    public int getSpellId() {
        return spellId;
    }

    /**
     * Gets if the autocast button is ancients.
     *
     * @return <code>True</code> if so.
     */
    public boolean isAncients() {
        return ancients;
    }

    /**
     * Gets if the autocast button is slayer.
     *
     * @return <code>True</code> if so.
     */
    public boolean isSlayer() {
        return slayer;
    }

    /**
     * Gets a modern <code>AutocastSpell</code> by the button id.
     *
     * @param buttonId The button id.
     * @return The modern autocast spell.
     */
    public static AutocastSpell forModern(int buttonId) {
        for (AutocastSpell autocastSpell : AutocastSpell.values()) {
            if (autocastSpell.isAncients() || autocastSpell.isSlayer()) {
                continue;
            }
            if (autocastSpell.getButtonId() == buttonId) {
                return autocastSpell;
            }
        }
        return null;
    }

    /**
     * Gets a ancient <code>AutocastSpell</code> by the button id.
     *
     * @param buttonId The button id.
     * @return The ancient autocast spell.
     */
    public static AutocastSpell forAncients(int buttonId) {
        for (AutocastSpell autocastSpell : AutocastSpell.values()) {
            if (!autocastSpell.isAncients()) {
                continue;
            }
            if (autocastSpell.getButtonId() == buttonId) {
                return autocastSpell;
            }
        }
        return null;
    }

    /**
     * Gets a slayer <code>AutocastSpell</code> by the button id.
     *
     * @param buttonId The button id.
     * @return The slayer autocast spell.
     */
    public static AutocastSpell forSlayer(int buttonId) {
        for (AutocastSpell autocastSpell : AutocastSpell.values()) {
            if (!autocastSpell.isSlayer()) {
                continue;
            }
            if (autocastSpell.getButtonId() == buttonId) {
                return autocastSpell;
            }
        }
        return null;
    }

}
