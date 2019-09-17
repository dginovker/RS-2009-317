package com.runescape.util;

/**
 * Represents skill constants.
 *
 * @author <a href="http://Gielinor.org/">Gielinor</a>
 */
public final class Skills {

    /**
     * Represents the amount of skills on this client.
     */
    public static final int SKILL_COUNT = 24;
    /**
     * Constants for the skill ids.
     */
    public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2,
            HITPOINTS = 3, RANGE = 4, PRAYER = 5,
            MAGIC = 6, COOKING = 7, WOODCUTTING = 8,
            FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
            CRAFTING = 12, SMITHING = 13, MINING = 14,
            HERBLORE = 15, AGILITY = 16, THIEVING = 17,
            SLAYER = 18, FARMING = 19, RUNECRAFTING = 20,
            CONSTRUCTION = 21, HUNTER = 22, SUMMONING = 23;

    /**
     * Represents currently enabled skills.
     */
    public static final boolean[] ENABLED_SKILLS = new boolean[]{
            true, true, true, true,
            true, true, true, true,
            true, true, true, true,
            true, true, true, true,
            true, true, true, true,
            true, false, true, true
    };

    /**
     * Gets the experience for a certain level.
     *
     * @param level The level.
     * @return The experience needed.
     */
    public static int getExperienceForLevel(int level) {
        int points = 0;
        int output = 0;
        for (int lvl = 1; lvl <= level; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            if (lvl >= level) {
                return output;
            }
            output = (int) Math.floor(points / 4);
        }
        return 0;
    }

}
