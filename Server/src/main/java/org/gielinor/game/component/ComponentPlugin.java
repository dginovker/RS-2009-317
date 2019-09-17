package org.gielinor.game.component;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to handle a component action.
 * @author 'Vexia
 */
public abstract class ComponentPlugin implements Plugin<Object> {

    /**
     * Handles the interface interaction.
     * @param player The player.
     * @param component The component.
     * @param opcode The opcode.
     * @param slot The slot.
     * @param itemId The item id.
     * @return <code>True</code> if succesfully handled.
     */
    public abstract boolean handle(final Player player, Component component, final int opcode, final int button, int slot, int itemId);

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

}
