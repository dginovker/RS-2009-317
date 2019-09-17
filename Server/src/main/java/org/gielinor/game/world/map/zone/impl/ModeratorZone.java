package org.gielinor.game.world.map.zone.impl;

import org.gielinor.game.content.global.travel.Teleport.TeleportType;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.world.GameConstants;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.ZoneBorders;

/**
 * Represents the moderator zone.
 *
 * @author 'Vexia
 * @date 12/11/2013
 * @see <b>http://runescape.wikia.com/wiki/Player_Moderator#Surgeries_and_the_P-Mod_room</b.
 */
public class ModeratorZone extends MapZone {

    /**
     * Represents if the moderator zone is open.
     */
    public static boolean open = false;

    /**
     * Represents the center of the zone.
     */
    public static final Location center = Location.create(2846, 5213, 0);

    /**
     * Constructs a new {@code ModeratorZone} {@code Object}.
     */
    public ModeratorZone() {
        super("Moderator Zone", true);
    }

    @Override
    public boolean enter(final Entity entity) {
        if (!(entity instanceof Player)) {
            return true;
        }
        final Player player = ((Player) entity);
        if (!open && !player.getDetails().getRights().isAdministrator()) {
            home(player);
            return false;
        }
        if (player.getDetails().getRights() != Rights.GIELINOR_MODERATOR) {
            player.getInterfaceState().removeTabs(0, 1, 2, 3, 4, 5, 6, 12);
        } else {
            player.getActionSender().sendMessage(getToggleMessage());
        }
        return true;
    }

    @Override
    public boolean leave(final Entity entity, final boolean logout) {
        if (!(entity instanceof Player)) {
            return true;
        }
        final Player player = ((Player) entity);
        player.getInterfaceState().openDefaultTabs();
        return true;
    }

    @Override
    public void configure() {
        register(new ZoneBorders(2840, 5204, 2853, 5224));
    }

    /**
     * Method used to toggle the moderator zone.
     *
     * @param player
     *            the player.
     * @param on
     *            the toggle switch.
     */
    public static void toggle(final Player player, final boolean on) {
        open = on;
        player.getActionSender().sendMessage(getToggleMessage());
        if (!open) {
            for (Player p : RegionManager.getLocalPlayers(center)) {
                if (p == null || p.getDetails().getRights().isAdministrator()) {
                    continue;
                }
                home(p);
            }
        }
    }

    /**
     * Method used to get the togglemessage.
     *
     * @return the message.
     */
    public static String getToggleMessage() {
        return "The moderator room is currently " + (open ? "available" : "not available") + " to player moderators.";
    }

    /**
     * Method used to send a player home.
     *
     * @param player
     *            the player.
     */
    public static void home(final Player player) {
        player.getTeleporter().send(GameConstants.HOME_LOCATION, TeleportType.NORMAL);
    }

    /**
     * Method used to teleport a player into the zone.
     *
     * @param player
     *            the player.
     */
    public static void teleport(final Player player) {
        player.getTeleporter().send(center, TeleportType.NORMAL);
    }

    /**
     * Method used to check if the moderator zone is open.
     *
     * @return return <code>True</code> if so.
     */
    public static boolean isOpen() {
        if (!open) {
            return false;
        }
        for (Player p : RegionManager.getLocalPlayers(center, 20)) {
            if (p.getDetails().getRights().isAdministrator()) {
                return true;
            }
        }
        return false;
    }

}
