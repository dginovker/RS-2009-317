package org.gielinor.game.content.global.action;

import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.SystemManager;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.mqueue.message.impl.PickupMessage;

/**
 * A class used to handle the picking up of ground items.
 *
 * @author 'Vexia
 */
public final class PickupHandler {

    /**
     * Method used to take a ground item.
     *
     * @param player the player.
     * @param item   the item.
     * @return <code>True</code> if taken.
     */
    public static boolean take(final Player player, final GroundItem item) {
        if (item.getLocation() == null) {
            player.getActionSender().sendMessage("Invalid ground item!");
            return false;
        }
        boolean exists = false;
        for (Item groundItem : GroundItemManager.getItems()) {
            if (groundItem.equals(item) && groundItem.isActive()) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            return true;
        }
        if (item.isRemoved()) {
            return true;
        }
        if (!canTake(player, item, 0)) {
            return true;
        }
        Item add = new Item(item.getId(), item.getCount(), item.getCharge());
        if (!player.getInventory().hasRoomFor(add)) {
            player.getActionSender().sendMessage("You don't have enough inventory space to hold that item.");
            return true;
        }
        if (item.isActive() && player.getInventory().add(add)) {
            if (!RegionManager.isTeleportPermitted(item.getLocation())) {
                player.animate(Animation.create(535));
            }
            if (SystemManager.isActive()) {
                World.submit(new PickupMessage(player, item));
            }
            GroundItemManager.destroy(item);
            player.getActionSender().sendSound(new Audio(2582, 10, 1));
        }
        return true;
    }

    /**
     * Checks if the player can take an item.
     *
     * @param player the player.
     * @param item   the item.
     * @param type   the type (1= ground, 2=telegrab)
     * @return <code>True</code> if so.
     */
    public static boolean canTake(Player player, GroundItem item, int type) {
        if (item.getId() == 8858 || item.getId() == 8859) {
            player.getDialogueInterpreter().sendDialogues(6074, FacialExpression.ANGRY, "Hey! You can't take that, it's guild property. Take one", "from the pile.");
            return false;
        }
//		if (GodType.forCape(item) != null) {
//			if (GodType.hasAny(player)) {
//				player.sendMessages("You may only possess one sacred cape at a time.", "The conflicting powers of the capes drive them apart.");
//				return false;
//			}
//		}
//		if (RunePouch.forItem(item) != null) {
//			if (player.hasItem(item)) {
//				player.sendMessage("A mystical force prevents you from picking up the pouch.");
//				return false;
//			}
//		}
        return !item.hasItemPlugin() || item.getPlugin().canPickUp(player, item, type);
    }
}
