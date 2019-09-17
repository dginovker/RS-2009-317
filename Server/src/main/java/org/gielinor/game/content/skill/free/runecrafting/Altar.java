package org.gielinor.game.content.skill.free.runecrafting;

import org.gielinor.game.node.object.GameObject;

/**
 * Represents an altar an it's relative information(corresponding ruin, etc)
 *
 * @author 'Vexia
 * @date 01/11/2013
 */
public enum Altar {
    AIR(14897, 14841, MysteriousRuin.AIR, Rune.AIR),
    MIND(14898, 14842, MysteriousRuin.MIND, Rune.MIND),
    WATER(14899, 14843, MysteriousRuin.WATER, Rune.WATER),
    EARTH(14900, 14844, MysteriousRuin.EARTH, Rune.EARTH),
    FIRE(14901, 14845, MysteriousRuin.FIRE, Rune.FIRE),
    BODY(14902, 14846, MysteriousRuin.BODY, Rune.BODY),
    COSMIC(14903, 14847, MysteriousRuin.COSMIC, Rune.COSMIC),
    CHAOS(14906, 14893, MysteriousRuin.CHAOS, Rune.CHAOS),
    ASTRAL(14911, 0, null, Rune.ASTRAL),
    NATURE(14905, 14892, MysteriousRuin.NATURE, Rune.NATURE),
    LAW(14904, 14848, MysteriousRuin.LAW, Rune.LAW),
    DEATH(14907, 14894, MysteriousRuin.DEATH, Rune.DEATH),
    BLOOD(27978, 2477, MysteriousRuin.BLOOD, Rune.BLOOD),
    SOUL(2489, 0, null, Rune.SOUL),
    OURANIA(29631, 0, null, null);

    /**
     * Constructs a new {@code Altar} {@code Object}.
     *
     * @param object the object.
     * @param ruin   the ruin.
     * @param rune   the rune.
     */
    Altar(final int object, final int portal, final MysteriousRuin ruin, final Rune rune) {
        this.object = object;
        this.portal = portal;
        this.ruin = ruin;
        this.rune = rune;
    }

    /**
     * Represents the object of the altar.
     */
    private final int object;

    /**
     * Represents the portal object.
     */
    private final int portal;

    /**
     * Represents the corresponding ruin.
     */
    private final MysteriousRuin ruin;

    /**
     * Represents the rune constructed.
     */
    private final Rune rune;

    /**
     * Gets the object.
     *
     * @return The object.
     */
    public int getObject() {
        return object;
    }

    /**
     * Gets the ruin.
     *
     * @return The ruin.
     */
    public MysteriousRuin getRuin() {
        return ruin;
    }

    /**
     * Gets the rune.
     *
     * @return The rune.
     */
    public Rune getRune() {
        return rune;
    }

    /**
     * Checks if its the ourania altar.
     *
     * @return the ourania.
     */
    public boolean isOurania() {
        return this == OURANIA;
    }

    /**
     * Gets the talisman.
     *
     * @return the talisman.
     */
    public Talisman getTalisman() {
        for (Talisman talisman : Talisman.values()) {
            if (talisman.name().equals(name())) {
                return talisman;
            }
        }
        return null;
    }

    /**
     * Gets the tiara.
     *
     * @return the tiara.
     */
    public Tiara getTiara() {
        for (Tiara tiara : Tiara.values()) {
            if (tiara.name().equals(name())) {
                return tiara;
            }
        }
        return null;
    }

    /**
     * Method used to get the <code>Altar</code> by the object.
     *
     * @param object the object.
     * @return the <code>Altar</code> or <code>Null</code>.
     */
    public static Altar forObject(final GameObject object) {
        for (Altar altar : values()) {
            if (altar.getObject() == object.getId() || altar.getPortal() == object.getId()) {
                return altar;
            }
        }
        return null;
    }

    /**
     * Gets the portal.
     *
     * @return The portal.
     */
    public int getPortal() {
        return portal;
    }
}
