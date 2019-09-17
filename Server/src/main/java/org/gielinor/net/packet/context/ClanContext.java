package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.system.communication.ClanCommunication;
import org.gielinor.net.packet.Context;

/**
 * The packet context for clan-related outgoing packets.
 * @author Emperor
 *
 */
public final class ClanContext implements Context {

    /**
     * The player
     */
    private final Player player;

    /**
     * The clan instance.
     */
    private final ClanCommunication clan;

    /**
     * If the player is leaving the clan.
     */
    private final boolean leave;

    /**
     * Constructs a new {@code ClanContext} {@code Object}.
     * @param player the player.
     * @param clan the clan.
     * @param leave If the player is leaving the clan.
     */
    public ClanContext(Player player, ClanCommunication clan, boolean leave) {
        this.player = player;
        this.clan = clan;
        this.leave = leave;
    }

    /**
     * Gets the clan.
     * @return The clan.
     */
    public ClanCommunication getClan() {
        return clan;
    }

    /**
     * Gets the leave.
     * @return The leave.
     */
    public boolean isLeave() {
        return leave;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
