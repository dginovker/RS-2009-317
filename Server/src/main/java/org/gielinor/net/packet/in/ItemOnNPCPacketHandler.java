package org.gielinor.net.packet.in;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Represents the {@link org.gielinor.net.packet.IncomingPacket} for using an {@link org.gielinor.game.node.item.Item} on an {@link org.gielinor.game.node.entity.npc.NPC}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ItemOnNPCPacketHandler implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        if (player.getLocks().isInteractionLocked()) {
            return;
        }
        int itemId = packet.getShortA();
        int npcIndex = packet.getShortA();
        int slotId = packet.getLEShort();
        int interfaceId = packet.getShortA();
        player.getActionSender().sendDebugPacket(opcode, "ItemOnNPC", "Item ID: " + itemId,
            "NPC Index: " + npcIndex,
            "Slot ID: " + slotId,
            "Interface ID: " + interfaceId);
        NPC npc = Repository.getNpcs().get(npcIndex);
        Item item = player.getInventory().get(slotId);
        if (item == null || item.getId() != itemId) {
            return;
        }
        NodeUsageEvent event = new NodeUsageEvent(player, interfaceId, item, npc);
        UseWithHandler.run(event);
    }
}
