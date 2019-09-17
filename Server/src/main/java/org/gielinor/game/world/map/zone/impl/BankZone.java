package org.gielinor.game.world.map.zone.impl;

import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.ZoneBorders;

/**
 * Represents a bank zone.
 *
 * @author 'Vexia
 */
public final class BankZone extends MapZone {

    /**
     * Represents the instance.
     */
    private static final BankZone INSTANCE = new BankZone();

    /**
     * Represents the Varrock west bank.
     */
    public static final ZoneBorders VARROCK_WEST = new ZoneBorders(3179, 3432, 3194, 3446);

    /**
     * Represents the Varrock east bank.
     */
    public static final ZoneBorders VARROCK_EAST = new ZoneBorders(3250, 3416, 3257, 3423);

    /**
     * Represents the Lumbridge bank.
     */
    public static final ZoneBorders LUMBRIDGE = new ZoneBorders(3206, 3215, 3210, 3222, 2);
    /**
     * Represents the Edgeville bank.
     */
    public static final ZoneBorders[] EDGEVILLE = new ZoneBorders[]{
        new ZoneBorders(3091, 3488, 3098, 3499),
        new ZoneBorders(3090, 3493, 3090, 3497)
    };

    /**
     * Constructs a new {@code BankZone} {@code Object}.
     */
    public BankZone() {
        super("bank", true);
    }

    @Override
    public void configure() {
        register(VARROCK_WEST);
        register(VARROCK_EAST);
        register(LUMBRIDGE);
        register(EDGEVILLE);
    }

    /**
     * Gets the instance.
     *
     * @return The instance.
     */
    public static BankZone getInstance() {
        return INSTANCE;
    }

}
