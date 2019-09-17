package com.runescape.chat;

import com.runescape.util.StringUtility;

/**
 * Represents a clan member for clan chats.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ClanChatMember {

    /**
     * The name of the member.
     */
    private final long name;
    /**
     * The rank id of the member.
     */
    private final byte rankId;
    /**
     * The world id of the member.
     */
    private final short worldId;
    /**
     * The world string of the member.
     */
    private final String worldString;
    /**
     * The privilege of the member.
     */
    private int privilege;

    /**
     * Constructs a new {@link ClanChatMember}.
     *
     * @param name        The name of the member.
     * @param rankId      The rank id of the member.
     * @param worldString The worldString of the member.
     */
    public ClanChatMember(long name, byte rankId, short worldId, String worldString, int privilege) {
        this.name = name;
        this.rankId = rankId;
        this.worldId = worldId;
        this.worldString = worldString;
        this.privilege = privilege;
    }

    /**
     * Gets the name of the member.
     *
     * @return The name.
     */
    public long getName() {
        return name;
    }

    /**
     * Gets the rank id of the member.
     *
     * @return The rank id.
     */
    public byte getRankId() {
        return rankId;
    }

    /**
     * Gets the world id of the member.
     *
     * @return The world id.
     */
    public short getWorldId() {
        return worldId;
    }

    /**
     * Gets the world string of the member.
     *
     * @return The world string.
     */
    public String getWorldString() {
        return worldString;
    }

    /**
     * Gets the privilege of the member.
     *
     * @return The privilege.
     */
    public int getPrivilege() {
        return privilege;
    }

    /**
     * Gets the clan chat rank icon.
     *
     * @return The rank icon.
     */
    public String getIcon() {
        if (getRankId() == 9) {
            return "<img=16>";
        } else if (getPrivilege() >= 2) {
            return "<img=17>";
        } else {
            if (getRankId() == -1 || getRankId() == 1) {
                return "";
            }
            if (getRankId() == 3) {
                return "<img=9>";
            }
            return "<img=" + (15 - getRankId()) + ">";
        }
    }

    @Override
    public String toString() {
        return ClanChatMember.class.getName() + " : [nameLong=" + name + ", name=" + StringUtility.decodeBase37(name) + ", rankId=" + rankId + ", worldId=" + worldId + ", worldString=" + worldString + "]";
    }
}
