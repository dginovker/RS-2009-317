package org.gielinor.game.content.global.action;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Ironman;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.SystemManager;
import org.gielinor.game.world.World;
import org.gielinor.mqueue.message.impl.DropMessage;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.config.ServerVar;

/**
 * Handles the dropping of an item.
 *
 * @author Vexia
 */
public final class DropItemHandler {

    /**
     * Handles the droping of an item.
     *
     * @param player
     *            the player.
     * @param node
     *            the node.
     * @param option
     *            the option.
     * @return <code>True</code> if so.
     */
    public static boolean handle(final Player player, Node node, String option) {
        Item item = (Item) node;
        if (item.getSlot() == -1) {
            player.getActionSender().sendMessage("Invalid slot!");
            return false;
        }
        switch (option) {
            case "drop":
            case "destroy":
                if (!player.getInterfaceState().close()) {
                    return true;
                }
                if (option.equals("drop") && !Ironman.isIronman(player)
                    && player.getSavedData().getGlobalData().getTotalPlayTime() < Constants.MINIMUM_PLAY_TIME) {
                    player.getActionSender().sendMessage("You must have been playing for 30 minutes to drop items.");
                    return true;
                }
                if (option.equals("drop") && ServerVar.fetch("player_dropping_disabled", 0) == 1) {
                    player.getDialogueInterpreter().sendPlaneMessage("Dropping items has been disabled.");
                    return true;
                }
                player.getDialogueInterpreter().close();
                player.getPulseManager().clear();
                if (option.equalsIgnoreCase("destroy")) {
                    player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("DESTROY_ITEM"), item);
                    return true;
                }
                player.getAudioManager().send(new Audio(1106, 0, 0));
                if (player.getInventory().replace(null, item.getSlot()) == item) {
                    item = item.getDropItem();
                    player.getAudioManager().send(new Audio(item.getId() == Item.COINS ? 10 : 2739, 1, 0));
                    GroundItem groundItem = GroundItemManager.create(item, player.getLocation(), player);
                    if (player.getRights().isAdministrator() || Ironman.isIronman(player)) {
                        groundItem.setDecayTime(99);
                    }
                    if (SystemManager.isActive()) {
                        World.submit(new DropMessage(player, groundItem));
                    }
                }
                return true;
        }
        return false;
    }

    /**
     * Drops an item.
     *
     * @param player
     *            the player.
     * @param item
     *            the item.
     * @return
     */
    public static boolean drop(Player player, Item item) {
        return handle(player, item, item.getDefinition().hasDestroyAction() ? "destroy" : "drop");
    }

}
