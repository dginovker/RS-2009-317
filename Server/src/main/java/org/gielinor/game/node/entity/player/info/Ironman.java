package org.gielinor.game.node.entity.player.info;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.login.Starter.StarterPackage;
import org.gielinor.game.node.entity.player.link.request.RequestType;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.utilities.string.TextUtils;

/**
 * A utility-type class for Ironman starter packages.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class Ironman {

    /**
     * The message to send upon using objects in the Party room.
     */
    public static final String PARTY_ROOM = "You are an iron man and cannot do that.";
    /**
     * The message to send upon bursting a party balloon.
     */
    public static final String PARTY_BALLOON = "You are an iron man and cannot pop balloons.";
    /**
     * The message to send upon toggling accept aid on.
     */
    public static final String ACCEPT_AID = "You are an iron man and cannot accept aid.";

    /**
     * Whether or not a player can attack or be attacked by another player.
     *
     * @param player The player.
     * @param target The target player.
     * @return {code True} if so.
     */
    public static boolean canAttack(Player player, Player target) {
        if (isIronman(player) || isIronman(target)) {
            StarterPackage starterPackage = isIronman(target) ? target.getSavedData().getGlobalData().getStarterPackage()
                : player.getSavedData().getGlobalData().getStarterPackage();
            player.getActionSender().sendMessage((isIronman(target) ?
                (TextUtils.formatDisplayName(target.getName()) + " is") : "You are")
                + " an " + starterPackage.name().replaceAll("_MODE", "")
                .toLowerCase().replaceAll("_", " ") + " and cannot " + (isIronman(target) ? "be attacked by" : "attack") + " other players.");
            return false;
        }
        return true;
    }

    /**
     * Attempts to open the bank for the player.
     *
     * @param player The player opening the bank.
     * @return <code>False</code> if the player cannot open the bank.
     */
    public static boolean openBank(Player player) {
        if (isUltimateIronman(player)) {
            player.getActionSender().sendMessage("You are an ultimate iron man and cannot bank.");
            return false;
        }
        return true;
    }

    /**
     * Checks whether or not a player can send or accept requests.
     *
     * @param player The player.
     * @param target The target.
     *               TODO Can duel?
     */
    public static boolean canRequest(Player player, Player target, RequestType requestType) {
        if (isIronman(player) || isIronman(target)) {
            StarterPackage starterPackage = isIronman(target) ? target.getSavedData().getGlobalData().getStarterPackage()
                : player.getSavedData().getGlobalData().getStarterPackage();
            player.getActionSender().sendMessage((isIronman(target) ?
                (TextUtils.formatDisplayName(target.getName()) + " is an ") : "You are an ")
                + starterPackage.name().replaceAll("_MODE", "")
                .toLowerCase().replaceAll("_", " ") + " and cannot " + requestType.getName() + ".");
            return false;
        }
        return true;
    }

    /**
     * Checks if an Iron man can pick up an item.
     *
     * @param player     The player.
     * @param groundItem The ground item.
     */
    public static boolean canPickup(Player player, GroundItem groundItem) {
        if (isIronman(player)) {
            if (groundItem.getDropper() == null || groundItem.droppedBy(player)) {
                return true;
            }
            player.getActionSender().sendMessage("You cannot pick that item up.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the player is an iron man only.
     *
     * @param player The player.
     * @return <code>True</code> if they are.
     */
    public static boolean isIronmanOnly(Player player) {
        return player.getSavedData().getGlobalData().getIronmanMode() == IronmanMode.IRONMAN;
    }

    /**
     * Checks if the player is an iron man.
     *
     * @param player The player.
     * @return <code>True</code> if they are.
     */
    public static boolean isIronman(Player player) {
        return player.getSavedData().getGlobalData().getIronmanMode() != IronmanMode.NONE;
    }

    /**
     * Checks if the player is an ultimate iron man.
     *
     * @param player The player.
     * @return <code>True</code> if they are.
     */
    public static boolean isUltimateIronman(Player player) {
        return player.getSavedData().getGlobalData().getIronmanMode() == IronmanMode.ULTIMATE_IRONMAN;
    }

    /**
     * Checks if the player is a hardcore iron man.
     *
     * @param player The player.
     * @return <code>True</code> if they are.
     */
    public static boolean isHardcoreIronman(Player player) {
        return player.getSavedData().getGlobalData().getIronmanMode() == IronmanMode.HARDCORE_IRONMAN;
    }
}
