package org.gielinor.rs2.model.container.impl;

import org.gielinor.game.content.skill.member.summoning.SummoningPouch;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.ContainerEvent;
import org.gielinor.rs2.model.container.ContainerListener;

/**
 * Handles the inventory container listening.
 *
 * @author Emperor
 */
public final class InventoryListener implements ContainerListener {

    /**
     * The player.
     */
    private final Player player;

    /**
     * Constructs a new {@code InventoryListener} {@code Object}.
     *
     * @param player The player.
     */
    public InventoryListener(Player player) {
        this.player = player;
    }

    /**
     * Updates the required settings etc for the player when the container updates.
     *
     * @param c The container.
     */
    public void update(Container c) {
        player.getSettings().updateWeight();
        boolean hadPouch = player.getFamiliarManager().isHasPouch();
        boolean pouch = false;
        for (Item item : c.toArray()) {
            if (item != null && SummoningPouch.get(item.getId()) != null) {
                pouch = true;
                break;
            }
        }
        player.getFamiliarManager().setHasPouch(pouch);
        if (hadPouch != pouch && player.getSkullManager().isWilderness()) {
            player.getAppearance().sync();
        }
    }

    @Override
    public void refresh(Container c) {
        PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 3214, 0, 93, c, false));
        update(c);
    }

    @Override
    public void update(Container c, ContainerEvent event) {
        PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 3214, 0, 93, event.getItems(), false, event.getSlots()));
        update(c);
    }

    public void toBank() {
        player.getBank().addAll(player.getInventory());
        player.getInventory().clear();
    }

}
