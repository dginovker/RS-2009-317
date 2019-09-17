package org.gielinor.game.node.entity.combat;

import org.gielinor.game.node.entity.combat.handlers.MagicSwingHandler;
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.gielinor.game.node.entity.combat.handlers.RangeSwingHandler;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;

/**
 * Represents the different styles of combat.
 *
 * @author Emperor
 */
public enum CombatStyle {

    /**
     * The melee combat style.
     */
    MELEE(new MeleeSwingHandler(), PrayerType.PROTECT_FROM_MELEE, PrayerType.DEFLECT_MELEE),

    /**
     * The range combat style.
     */
    RANGE(new RangeSwingHandler(), PrayerType.PROTECT_FROM_MISSILES, PrayerType.DEFLECT_MISSILES),

    /**
     * The magic combat style.
     */
    MAGIC(new MagicSwingHandler(), PrayerType.PROTECT_FROM_MAGIC, PrayerType.DEFLECT_MAGIC),;
    /**
     * The combat swing handler used by the combat style.
     */
    private final CombatSwingHandler swingHandler;
    /**
     * The protection prayer for this combat type.
     */
    private final PrayerType protectionPrayer;
    /**
     * The deflection prayer for this combat type.
     */
    private final PrayerType deflectionPrayer;

    /**
     * Constructs a new {@code CombatStyle} {@code Object}.
     *
     * @param swingHandler     The combat swing handler.
     * @param protectionPrayer The protection prayer.
     */
    CombatStyle(CombatSwingHandler swingHandler, PrayerType protectionPrayer, PrayerType deflectionPrayer) {
        this.swingHandler = swingHandler;
        this.protectionPrayer = protectionPrayer;
        this.deflectionPrayer = deflectionPrayer;
        swingHandler.setType(this);
    }

    /**
     * Gets the swingHandler.
     *
     * @return The swingHandler.
     */
    public CombatSwingHandler getSwingHandler() {
        return swingHandler;
    }

    /**
     * Gets the protectionPrayer.
     *
     * @return The protectionPrayer.
     */
    public PrayerType getProtectionPrayer() {
        return protectionPrayer;
    }

    /**
     * Gets the deflection prayer.
     *
     * @return The defletionPrayer.
     */
    public PrayerType getDeflectionPrayer() {
        return deflectionPrayer;
    }
}
