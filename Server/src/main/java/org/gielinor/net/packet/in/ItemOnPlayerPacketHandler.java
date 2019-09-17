package org.gielinor.net.packet.in;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Represents the {@link org.gielinor.net.packet.IncomingPacket} for using an {@link org.gielinor.game.node.item.Item} on a {@link org.gielinor.game.node.entity.player.Player}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ItemOnPlayerPacketHandler implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        if (player.getLocks().isInteractionLocked()) {
            return;
        }
        int interfaceId = packet.getShortA();
        int playerIndex = packet.getShort();
        int itemId = packet.getShort();
        int slotId = packet.getLEShort();
        player.getActionSender().sendDebugPacket(opcode, "ItemOnPlayer", "Item ID: " + itemId,
            "Player Index: " + playerIndex,
            "Slot ID: " + slotId,
            "Interface ID: " + interfaceId);
        Player target = Repository.getPlayers().get(playerIndex);
        Item item = player.getInventory().get(slotId);
        if (target == null || item == null || item.getId() != itemId) {
            return;
        }
        NodeUsageEvent event = new NodeUsageEvent(player, interfaceId, item, target);
        UseWithHandler.run(event);
    }
}
