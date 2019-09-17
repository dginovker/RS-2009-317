package org.gielinor.game.system.communication;

/**
 * Represents a member in a player clan.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ClanMember {

    /**
     * The pidn of the {@code ClanMember}.
     */
    private final int pidn;
    /**
     * The username of the {@code ClanMember}.
     */
    private final String username;
    /**
     * The {@link org.gielinor.game.system.communication.ClanRank} of the {@code ClanMember}.
     */
    private ClanRank clanRank;

    /**
     * Constructs a new {@code ClanMember}.
     *
     * @param pidn     The pidn of the {@code ClanMember}.
     * @param username The username of the {@code ClanMember}.
     */
    public ClanMember(int pidn, String username) {
        this(pidn, username, ClanRank.NOT_IN_CLAN);
    }

    /**
     * Constructs a new {@code ClanMember}.
     *
     * @param pidn     The pidn of the {@code ClanMember}.
     * @param username The username of the {@code ClanMember}.
     * @param clanRank The {@link org.gielinor.game.system.communication.ClanRank} of the {@link org.gielinor.game.system.communication.ClanMember}.
     */
    public ClanMember(int pidn, String username, ClanRank clanRank) {
        this.pidn = pidn;
        this.username = username;
        this.clanRank = clanRank;
    }

    /**
     * Gets the pidn of the {@code ClanMember}.
     *
     * @return The pidn.
     */
    public int getPidn() {
        return pidn;
    }

    /**
     * Gets the username of the {@code ClanMember}.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the {@link org.gielinor.game.system.communication.ClanRank} of this {@code ClanMember}.
     *
     * @return The clan rank.
     */
    public ClanRank getClanRank() {
        return clanRank;
    }

    /**
     * Sets the {@link org.gielinor.game.system.communication.ClanRank} of this {@code ClanMember}.
     *
     * @param clanRank The {@link org.gielinor.game.system.communication.ClanRank} to set.
     */
    public void setClanRank(ClanRank clanRank) {
        this.clanRank = clanRank;
    }

}