package org.gielinor.game.node.entity.player.info;

import org.gielinor.game.node.entity.player.info.login.Starter;
import org.gielinor.utilities.string.TextUtils;

/**
 * Created by Corey on 09/07/2017.
 */
public enum IronmanMode {
    NONE,
    IRONMAN(3, 4, 13, "545454"),
    ULTIMATE_IRONMAN(2, 5, 15, "D9D9D9"),
    HARDCORE_IRONMAN(24, 6, 14, "CE4C4C");

    private int crownId;
    private int starterPackId;
    private int memberGroupId;
    private String colour;

    IronmanMode() {
        this.crownId = -1;
        this.starterPackId = -1;
        this.memberGroupId = -1;
        this.colour = "";
    }

    IronmanMode(int crownId, int starterPackId, int memberGroupId, String colour) {
        this.crownId = crownId;
        this.starterPackId = starterPackId;
        this.memberGroupId = memberGroupId;
        this.colour = colour;
    }

    public int getCrownId() {
        return crownId;
    }

    public int getStarterPackId() {
        return starterPackId;
    }

    public int getMemberGroupId() {
        return memberGroupId;
    }

    public String getColour() {
        return colour;
    }

    public String getFormattedName() {
        return TextUtils.formatDisplayName(name().replace("_", " ")
            .replace("NONE", "REGULAR PLAYER")
            .replace("IRONMAN", "REGULAR IRONMAN")
            .replace("ULTIMATE_REGULAR_IRONMAN", "ULTIMATE_IRONMAN"));
    }

    public String getShortName() {
        return getFormattedName().replace("Ultimate", "Ult.");
    }

    public static IronmanMode forStarterId(int starterPackId) {
        for (IronmanMode mode : values()) {
            if (mode.getStarterPackId() == starterPackId) {
                return mode;
            }
        }
        return NONE;
    }

    /**
     * Gets the ironman mode one mode down.
     */
    public static IronmanMode getDegradedMode(IronmanMode mode) {
        switch (mode) {
            case ULTIMATE_IRONMAN:
            case HARDCORE_IRONMAN:
                return IRONMAN;
            default:
                return NONE;
        }
    }

    public Starter.StarterPackage getStarterPackage() {
        switch (this) {
            case ULTIMATE_IRONMAN:
                return Starter.StarterPackage.ULTIMATE_IRON_MAN_MODE;
            case HARDCORE_IRONMAN:
                return Starter.StarterPackage.HARDCORE_IRON_MAN_MODE;
            case IRONMAN:
                return Starter.StarterPackage.IRON_MAN_MODE;
            default:
                return Starter.StarterPackage.MAIN;
        }
    }
}
