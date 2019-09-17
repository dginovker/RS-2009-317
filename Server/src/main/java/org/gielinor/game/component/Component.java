package org.gielinor.game.component;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.out.Interface;
import org.gielinor.net.packet.out.SidebarInterface;

/**
 * Represents a component.
 *
 * @author Emperor
 */
public class Component {

    /**
     * The component id.
     */
    protected int id;

    /**
     * The component definitions.
     */
    protected final ComponentDefinition definition;

    /**
     * The close event.
     */
    protected CloseEvent closeEvent;

    /**
     * The component plugin.
     */
    protected ComponentPlugin plugin;

    /**
     * Constructs a new {@code Component} {@code Object}.
     *
     * @param id The component id.
     */
    public Component(int id) {
        this.id = id;
        this.definition = ComponentDefinition.forId(id) == null ? new ComponentDefinition() : ComponentDefinition.forId(id);
        this.plugin = definition.getPlugin();
    }

    /**
     * Opens the component.
     */
    public void open(Player player) {
        for (Sidebar sidebar : Sidebar.values()) {
            if (id == sidebar.getInterfaceId()) {
                return;
            }
        }
        if (definition.getSidebarContext() != null) {
            PacketRepository.send(SidebarInterface.class, definition.getSidebarContext().setPlayer(player));
        }
        if (definition.getContext() != null) {
            PacketRepository.send(Interface.class, definition.getContext().setPlayer(player));
        } else {
            //player.debug("No component definitions added - [id=" + id + "]!");
        }
    }

    /**
     * Closes the component.
     *
     * @param player The player.
     * @return <code>True</code> if the component can be closed.
     */
    public boolean close(Player player) {
        return !(closeEvent != null && !closeEvent.canClose(player, this));
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the definition.
     *
     * @return The definition.
     */
    public ComponentDefinition getDefinition() {
        return definition;
    }

    /**
     * Gets the closeEvent.
     *
     * @return The closeEvent.
     */
    public CloseEvent getCloseEvent() {
        return closeEvent;
    }

    /**
     * Sets the closeEvent.
     *
     * @param closeEvent The closeEvent to set.
     */
    public Component setCloseEvent(CloseEvent closeEvent) {
        this.closeEvent = closeEvent;
        return this;
    }

    /**
     * Sets the component unclosable.
     *
     * @param c The component.
     */
    public static void setUnclosable(Player p, Component c) {
        p.setAttribute("close_c_", false);
        c.setCloseEvent(new CloseEvent() {

            @Override
            public void close(Player player, Component c) {

            }

            @Override
            public boolean canClose(Player player, Component component) {
                return player.getAttribute("close_c_", false);
            }
        });
    }

    /**
     * Sets the plugin.
     *
     * @param plugin the plugin.
     */
    public void setPlugin(ComponentPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the component plugin.
     *
     * @return the plugin.
     */
    public ComponentPlugin getPlugin() {
        if (plugin == null) {
            ComponentPlugin p = ComponentDefinition.forId(getId()).getPlugin();
            if ((plugin = p) != null) {
                return p;
            }
        }
        return plugin;
    }


}
