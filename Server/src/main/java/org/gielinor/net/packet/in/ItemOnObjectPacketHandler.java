package org.gielinor.net.packet.in;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.PlayerContext;
import org.gielinor.net.packet.out.ClearMinimapFlag;

/**
 * Represents the {@link org.gielinor.net.packet.IncomingPacket} for using an {@link org.gielinor.game.node.item.Item} on a {@link org.gielinor.game.node.object.GameObject}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ItemOnObjectPacketHandler implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        if (player.getLocks().isInteractionLocked()) {
            return;
        }
        int frameId = packet.getShortA() & 0xFFFF;
        int objectId = packet.getLEShort() & 0xFFFF;
        int y = packet.getLEShortA() & 0xFFFF;
        int slot = packet.getLEShort() & 0xFFFF;
        int x = packet.getLEShortA() & 0xFFFF;
        int itemId = packet.getShort() & 0xFFFF;
        int z = player.getLocation().getZ();
        player.getActionSender().sendDebugPacket(opcode,
            "ItemOnObject",
            "Frame ID: " + frameId,
            "Object ID: " + objectId,
            "Y: " + y,
            "X: " + x,
            "Item ID: " + itemId,
            "Z: " + z);
        GameObject object = RegionManager.getObject(z, x, y);
        if (object == null || object.getId() != objectId) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            return;
        }
        object = object.getChild(player);
        if (object == null) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            return;
        }
        final Item used = player.getInventory().get(slot);
        if (used == null || used.getId() != itemId) {
            return;
        }
        final NodeUsageEvent event = new NodeUsageEvent(player, 0, used, object);
        UseWithHandler.run(event);
    }
}
