package org.gielinor.net.packet.in;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;

/**
 * The incoming item action packet.
 *
 * @author Emperor
 */
public class ItemActionPacket implements IncomingPacket {

    @SuppressWarnings("unused")
    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        if (player.getLocks().isInteractionLocked()) {
            return;
        }
        int usedWithItemId = -1;
        int usedWithSlot = -1;
        int interfaceHash1 = -1;
        int interfaceId1 = -1;
        int childId1 = -1;
        int usedItemId = -1;
        int usedSlot = -1;
        int interfaceHash2 = -1;
        int interfaceId2 = -1;
        int childId2 = -1;
        switch (packet.opcode()) {
            case 188:
                interfaceHash1 = packet.getInt();
                usedWithItemId = packet.getShortA();
                usedWithSlot = packet.getLEShortA();
                usedItemId = packet.getLEShort();
                usedSlot = packet.getShort();
                interfaceHash2 = packet.getLEInt();
                interfaceId1 = interfaceHash1 >> 16;
                childId1 = interfaceHash1 & 0xFFFF;
                interfaceId2 = interfaceHash2 >> 16;
                childId2 = interfaceHash2 & 0xFFFF;
                break;
            default:
                return;
        }
        Item used = player.getInventory().get(usedSlot);
        Item with = player.getInventory().get(usedWithSlot);
        if (used == null || with == null || used.getId() != usedItemId || with.getId() != usedWithItemId) {
            return;
        }
        if (usedItemId < usedWithItemId) {
            Item item = used;
            used = with;
            with = item;
        }
        NodeUsageEvent event = new NodeUsageEvent(player, interfaceId1, used, with);
        UseWithHandler.run(event);
    }
}
