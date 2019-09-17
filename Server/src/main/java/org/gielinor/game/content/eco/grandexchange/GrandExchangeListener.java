package org.gielinor.game.content.eco.grandexchange;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.ContainerEvent;
import org.gielinor.rs2.model.container.ContainerListener;

/**
 * Listens to a Grand Exchange container.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class GrandExchangeListener implements ContainerListener {

    /**
     * The player.
     */
    private final Player player;

    /**
     * The inventory id.
     */
    private final int inventoryId;

    /**
     * Constructs a new {@code SellContainerListener}.
     *
     * @param player      The player.
     * @param inventoryId The id of the inventory.
     */
    public GrandExchangeListener(Player player, int inventoryId) {
        this.player = player;
        this.inventoryId = inventoryId;
    }

    @Override
    public void update(Container c, ContainerEvent event) {
        PacketRepository.send(ContainerPacket.class, new ContainerContext(player, inventoryId, 0, 0, event.getItems(), false, event.getSlots()));
    }

    @Override
    public void refresh(Container c) {
        PacketRepository.send(ContainerPacket.class, new ContainerContext(player, inventoryId, 0, 0, c.toArray(), 28, false));
    }

}