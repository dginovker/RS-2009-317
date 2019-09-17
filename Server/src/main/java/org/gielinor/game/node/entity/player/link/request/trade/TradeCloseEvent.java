package org.gielinor.game.node.entity.player.link.request.trade;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.out.ContainerPacket;

/**
 * Represents the close event invoked at the closing of a trade interface.
 *
 * @author 'Vexia
 */
public final class TradeCloseEvent implements CloseEvent {

    @Override
    public void close(Player player, Component c) {
        final TradeModule module = TradeModule.getExtension(player);
        if (module == null) {
            return;
        }
        if (module.isAccepted() && TradeModule.getExtension(module.getTarget()).isAccepted()) {
            return;
        }
        if (module.getStage() != 2) {
            retainContainer(player);
            retainContainer(module.getTarget());
        }
        closeInterfaces(player);
        closeInterfaces(module.getTarget());
        module.getTarget().getInterfaceState().closeUnchecked();
        end(player);
        end(module.getTarget());
    }

    @Override
    public boolean canClose(Player player, Component component) {
        return true;
    }

    /**
     * Method used to close the trade interface.
     *
     * @param player the player.
     */
    private void closeInterfaces(final Player player) {
        player.removeExtension(TradeModule.class);
        player.getInterfaceState().closeSingleTab();
        player.getInterfaceState().openDefaultTabs();
        // Trade interfaces below?
        PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, 2, 24, new Item[]{}, 27, false));
        PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, 2, 23, new Item[]{}, 27, false));
    }

    /**
     * Method used to end the trade session.
     *
     * @param player the player.
     */
    private void end(final Player player) {
        player.getConfigManager().set(1043, 0);
        player.getConfigManager().set(1042, 0);
    }

    /**
     * Method used to retain the trade container.
     *
     * @param player the player.
     */
    private void retainContainer(final Player player) {
        final TradeModule module = TradeModule.getExtension(player);
        player.getInventory().addAll(module.getContainer());
    }

}
