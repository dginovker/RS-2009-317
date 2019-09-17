package org.gielinor.net.packet.in;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.IntegerContext;
import org.gielinor.net.packet.out.InputStatePacket;

/**
 *
 */
public class GrandExchangePacket implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder packetBuilder) {
        int itemId = packetBuilder.getLEShort();
        boolean priceGuide = packetBuilder.get() == 2;
        if (priceGuide) {
            player.getPriceGuideContainer().addGrandExchangeItem(itemId);
        } else {
            player.getGrandExchange().constructBuy(itemId);
        }
        PacketRepository.send(InputStatePacket.class, new IntegerContext(player, 0));
    }

}
