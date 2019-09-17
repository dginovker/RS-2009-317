package org.gielinor.game.content.global.action;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.node.entity.lock.Lock;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the equipment equipping handler plugin.
 *
 * @author Emperor
 * @author 'Vexia
 */
public class EquipHandler {

    /**
     * The sound to send.
     */
    public static final Audio SOUND = new Audio(230, 1, 0);

    /**
     * Constructs a new {@code EquipHandler} {@code Object}.
     */
    public EquipHandler() {
        super();
    }

    /**
     * Handles equipping an item from the inventory.
     *
     * @param player The player.
     * @param item   The item to equip.
     * @return
     */
    public static boolean equip(final Player player, final Item item) {
        if (item == null || player.getInventory().get(item.getSlot()) != item) {
            return true;
        }
        Plugin<Object> plugin = item.getDefinition().getConfiguration("equipment", null);
        if (plugin != null) {
            Boolean bool = (Boolean) plugin.fireEvent("equip", player, item);
            if (bool != null && !bool) {
                return true;
            }
        }
        Lock lock = player.getLocks().getEquipmentLock();
        if (lock != null && lock.isLocked()) {
            if (lock.getMessage() != null) {
                player.getActionSender().sendMessage(lock.getMessage());
            }
            return true;
        }
        if (player.getEquipment().add(item, item.getSlot(), true, true, (!item.getDefinition().isEquipment() && item.getDefinition().getWeightId() > 0) ? item.getDefinition().getWeightId() : -1)) {
            player.getDialogueInterpreter().close();
            player.getAudioManager().send(SOUND);
        }
        ItemDefinition.statsUpdate(player);
        return true;
    }
}