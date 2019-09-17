package org.gielinor.net.packet.in;

import org.gielinor.game.content.skill.free.firemaking.FiremakingPulse;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.rs2.pulse.impl.MovementPulse;

/**
 * Represents the {@link org.gielinor.net.packet.IncomingPacket} for using an {@link org.gielinor.game.node.item.Item} on another {@code Item}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ItemOnItemPacketHandler implements IncomingPacket {

    /**
     * Represents the item on item opcode.
     */
    private static final int ITEM_ON_ITEM = 53;

    /**
     * Represents the item on ground item opcode.
     */
    public static final int ITEM_ON_GROUND_ITEM = 25;

    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        if (player.getLocks().isInteractionLocked()) {
            return;
        }
        switch (opcode) {
            case ITEM_ON_ITEM:
                int usedWithSlot = packet.getShort();
                int usedSlot = packet.getShortA();
                int usedWithItemId = packet.getLEShortA();
                int componentId = packet.getShort();
                int usedItemId = packet.getLEShort();
                int componentId1 = packet.getShort();
                player.getActionSender().sendDebugPacket(opcode,
                    "ItemOnItem",
                    "Used slot: " + usedSlot,
                    "Used with slot: " + usedWithSlot,
                    "Used with item ID: " + usedWithItemId,
                    "Component ID: " + componentId,
                    "Used item ID: " + usedItemId,
                    "Component ID1: " + componentId1);
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
                NodeUsageEvent event = new NodeUsageEvent(player, componentId1, used, with);
                UseWithHandler.run(event);
                break;

            case ITEM_ON_GROUND_ITEM:
                componentId = packet.getLEShort();
                final int itemId = packet.getShortA() & 0xFFFF;
                final int groundItemId = packet.getShort();
                final int y = packet.getShortA() & 0xFFFF;
                final int slot = packet.getLEShortA() & 0xFFFF;
                final int x = packet.getShort();
                player.getActionSender().sendDebugPacket(packet.opcode(),
                    "ItemOnGroundItem",
                    "X: " + x,
                    "Y: " + y,
                    "Slot: " + slot,
                    "Component ID: " + componentId,
                    "Item ID: " + itemId,
                    "Ground item ID: " + groundItemId);
                used = player.getInventory().get(slot);
                GroundItem usedOnGroundItem = GroundItemManager.get(groundItemId, Location.create(x, y, player.getLocation().getZ()), player);
                if (used == null || usedOnGroundItem == null || used.getId() != itemId || usedOnGroundItem.getId() != groundItemId) {
                    break;
                }
                if (usedOnGroundItem.getLocation() == null) {
                    break;
                }
                boolean exists = false;
                for (Item groundItem : GroundItemManager.getItems()) {
                    if (groundItem.equals(usedOnGroundItem) && groundItem.isActive()) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    break;
                }
                if (usedOnGroundItem.isRemoved()) {
                    break;
                }
                // TODO Plugin
                if (used.getName().startsWith("Tinder") && usedOnGroundItem.getName().toLowerCase().contains("logs")) {
                    final Item finalUsed = used;
                    player.getPulseManager().run(new MovementPulse(player, usedOnGroundItem) {

                        @Override
                        public boolean update() {
                            boolean finished = super.update();
                            if (finished) {
                                player.getWalkingQueue().reset();
                            }
                            return finished;

                        }

                        @Override
                        public boolean pulse() {
                            if (finalUsed.getId() != itemId || usedOnGroundItem.getId() != groundItemId) {
                                return true;
                            }
                            if (usedOnGroundItem.getLocation() == null) {
                                return true;
                            }
                            boolean exists = false;
                            for (Item groundItem : GroundItemManager.getItems()) {
                                if (groundItem.equals(usedOnGroundItem) && groundItem.isActive()) {
                                    exists = true;
                                    break;
                                }
                            }
                            if (!exists) {
                                return true;
                            }
                            if (usedOnGroundItem.isRemoved()) {
                                return true;
                            }
                            player.getPulseManager().run(new FiremakingPulse(player, usedOnGroundItem, usedOnGroundItem));
                            return true;
                        }
                    }, "movement");
                }
                // UseWithHandler.run(new NodeUsageEvent(player, 0, used, usedOnGroundItem, ITEM_ON_GROUND_ITEM));
                break;
        }
    }
}
