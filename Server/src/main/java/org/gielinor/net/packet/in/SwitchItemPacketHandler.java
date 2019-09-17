package org.gielinor.net.packet.in;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.impl.BankContainer;

/**
 * Represents the {@link org.gielinor.net.packet.IncomingPacket} switching an {@link org.gielinor.game.node.item.Item} slot.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SwitchItemPacketHandler implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        final int interfaceId = packet.getLEShortA();
        byte type = (byte) packet.getC();
        final boolean insert = type == 1;
        final int slot = packet.getLEShortA();
        final int toSlot = packet.getLEShort();

        final Container container = interfaceId == BankContainer.BANK_INVENTORY_INTERFACE ? player.getBank() :
            (interfaceId == BankContainer.PLAYER_INVENTORY_INTERFACE || interfaceId == 3214) ? player.getInventory() : null;
        if (container == null) {
            return;
        }
        final Item item = container.get(slot);
        if (interfaceId == BankContainer.BANK_INVENTORY_INTERFACE && type == 2 && Constants.BANK_TABS) {
            if (player.getBank().isOpen()) {
                player.getBank().getBankData().handleBankTab(slot, toSlot, true);
            }
        } else {
            final Item second = container.get(toSlot);
            if (!insert) {
                container.replace(second, slot);
                container.replace(item, toSlot);
            } else {
                container.insert(slot, toSlot);
            }
        }
    }
}
