package org.gielinor.game.node.entity.player.link;

/**
 * Represents a sidebar interface.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum Sidebar {

    ATTACK_TAB(2423),
    STATS_TAB(3917),
    QUEST_TAB(638),
    INVENTORY_TAB(3213),
    EQUIPMENT_TAB(1644),
    PRAYER_TAB(5608),
    MAGIC_TAB(1151),
    CLAN_TAB(32100),
    FRIEND_TAB(5065),
    IGNORE_TAB(5715),
    LOGOUT_TAB(2449),
    OPTION_TAB(31000),
    EMOTE_TAB(147),
    MUSIC_TAB(962),
    SUMMONING_TAB(25904);

    /**
     * The interface id of the {@link org.gielinor.game.node.entity.player.link.Sidebar}.
     */
    private final int interfaceId;

    /**
     * Constructs a new {@link org.gielinor.game.node.entity.player.link.Sidebar}.
     *
     * @param interfaceId The interface id of the {@link org.gielinor.game.node.entity.player.link.Sidebar}.
     */
    Sidebar(int interfaceId) {
        this.interfaceId = interfaceId;
    }

    /**
     * The interface id of the {@link org.gielinor.game.node.entity.player.link.Sidebar}.
     *
     * @return The id of the interface.
     */
    public int getInterfaceId() {
        return interfaceId;
    }
}