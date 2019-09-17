package org.gielinor.game.system.communication;

/**
 * Represents a rank of a clan member.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum ClanRank {

    OWNER("Only me", 9, 1),
    ANYONE("Anyone", 8, 9),
    ANY_FRIENDS("Any friends", 7, 8),
    NOT_IN_CLAN("Not in clan", 6, -1),
    RECRUIT("Recruit+", 5, 7),
    CORPORAL("Corporal+", 4, 6),
    SERGEANT("Sergeant+", 3, 5),
    LIEUTENANT("Lieutenant+", 2, 4),
    CAPTAIN("Captain+", 1, 3),
    GENERAL("General+", 0, 2),
    NO_ONE("No one", -1, 1),;

    /**
     * The requirement info.
     */
    private final String info;
    /**
     * The value of the rank.
     */
    private final int value;
    /**
     * The setting value.
     */
    private final int settingValue;

    /**
     * Constructs a new {@code ClanRank} {@code Object}.
     *
     * @param value The rank value.
     * @param info  The requirement info.
     */
    private ClanRank(String info, int value, int settingValue) {
        this.info = info;
        this.value = value;
        this.settingValue = settingValue;
    }

    /**
     * Gets the info.
     *
     * @return The info.
     */
    public String getInfo() {
        return info;
    }

    /**
     * Gets the value.
     *
     * @return The value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the setting value.
     *
     * @return The setting value.
     */
    public int getSettingValue() {
        return settingValue;
    }

    /**
     * Gets a {@code ClanRank} by rank id.
     *
     * @param rankId The id of the rank.
     * @return The {@code ClanRank}.
     */
    public static ClanRank forValue(int rankId) {
        for (ClanRank clanRank : values()) {
            if (clanRank.getValue() == rankId) {
                return clanRank;
            }
        }
        return NOT_IN_CLAN;
    }

    /**
     * Gets a {@code ClanRank} by the setting value.
     *
     * @param settingValue The setting value.
     * @return The {@code ClanRank}.
     */
    public static ClanRank forSettingValue(int settingValue) {
        for (ClanRank clanRank : values()) {
            if (clanRank.getSettingValue() == settingValue) {
                return clanRank;
            }
        }
        return ANY_FRIENDS;
    }
}
