package org.gielinor.game.content.skill.member.summoning.familiar;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.ContainerEvent;
import org.gielinor.rs2.model.container.ContainerListener;

/**
 * The beast of burden container listener.
 *
 * @author Emperor
 */
public final class BurdenContainerListener implements ContainerListener {

    /**
     * The player.
     */
    private final Player player;

    /**
     * Constructs a new {@code BurdenContainerListener} {@code Object}.
     *
     * @param player The player.
     */
    public BurdenContainerListener(Player player) {
        this.player = player;
    }

    @Override
    public void update(Container c, ContainerEvent event) {
        if (c instanceof BurdenBeastContainer) {
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 25706, 89, 90, event.getItems(), false, event.getSlots()));
        } else {
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 25708, 0, 0, event.getItems(), false, event.getSlots()));
        }
    }

    @Override
    public void refresh(Container c) {
        if (c instanceof BurdenBeastContainer) {
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 25706, 89, 90, c.toArray(), c.capacity(), false));
        } else {
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 25708, 0, 0, player.getInventory().toArray(), 28, false));
        }
    }

}