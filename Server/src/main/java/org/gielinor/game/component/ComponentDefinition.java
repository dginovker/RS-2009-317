package org.gielinor.game.component;

import java.util.Map;

import org.gielinor.net.packet.context.ChatboxInterfaceContext;
import org.gielinor.net.packet.context.InterfaceContext;
import org.gielinor.net.packet.context.SidebarInterfaceContext;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * Represents the component definitions.
 * <p>
 * TODO Finish client interfaces, dump component definitions.
 *
 * @author Emperor
 *
 */
public final class ComponentDefinition {

    /**
     * The component definitions mapping.
     */
    private static final Int2ObjectMap<ComponentDefinition> DEFINITIONS = new Int2ObjectOpenHashMap<>();

    /**
     * The id of the {@link org.gielinor.game.component.Component}.
     */
    private int id;

    /**
     * The parent id.
     */
    private int parentId;

    /**
     * The tab index.
     */
    private int tabIndex;

    /**
     * If the {@link org.gielinor.game.component.Component} is walkable.
     */
    private boolean walkable;

    /**
     * The {@link ComponentType}.
     */
    private ComponentType componentType = ComponentType.DEFAULT;

    /**
     * The actions of the {@link org.gielinor.game.component.Component}.
     */
    private String[] actions;

    /**
     * The message on the {@link org.gielinor.game.component.Component}.
     */
    private String message;

    /**
     * The tooltip for the {@link org.gielinor.game.component.Component}.
     */
    private String tooltip;

    /**
     * The interface context.
     */
    private InterfaceContext context;

    /**
     * The sidebar interface context.
     */
    private SidebarInterfaceContext sidebarContext;

    /**
     * The chatbox interface context.
     */
    private ChatboxInterfaceContext chatboxContext;

    /**
     * Represents the plugin handler.
     */
    private ComponentPlugin plugin;

    /**
     * Constructs a new {@code ComponentDefinition} {@code Object}.
     */
    public ComponentDefinition() {
        actions = new String[5];
    }

    /**
     * Gets the component definitions for the component id.
     *
     * @param componentId
     *            The component id.
     * @return The component definitions.
     */
    public static ComponentDefinition forId(int componentId) {
        if (DEFINITIONS.get(componentId) == null) {
            for (ComponentDefinition componentDefinition : DEFINITIONS.values()) {
                if (componentDefinition.getParentId() == componentId) {
                    return ComponentDefinition.getDefinitions().put(componentId, new ComponentDefinition());
                }
            }
            ComponentDefinition.getDefinitions().put(componentId, new ComponentDefinition());
        }
        return DEFINITIONS.get(componentId);
    }

    public static void parse() {

    }

    /**
     * Method used to put a plugin.
     *
     * @param id
     *            the id.
     * @param plugin
     *            the plugin.
     */
    public static void put(int id, ComponentPlugin plugin) {
        if (ComponentDefinition.forId(id) == null) {
            ComponentDefinition.getDefinitions().put(id, new ComponentDefinition());
        }
        ComponentDefinition.forId(id).setPlugin(plugin);
    }

    /**
     * Gets the context.
     *
     * @return The context.
     */
    public InterfaceContext getContext() {
        return context;
    }

    /**
     * Gets the sidebar context.
     *
     * @return The sidebar context.
     */
    public SidebarInterfaceContext getSidebarContext() {
        return sidebarContext;
    }

    /**
     * Gets the chatbox context.
     *
     * @return The chatbox context.
     */
    public ChatboxInterfaceContext getChatboxContext() {
        return chatboxContext;
    }

    /**
     * Gets the definitions mapping.
     *
     * @return The definitions mapping.
     */
    public static Map<Integer, ComponentDefinition> getDefinitions() {
        return DEFINITIONS;
    }

    /**
     * Sets the context.
     *
     * @param context
     *            The context to set.
     */
    public void setContext(InterfaceContext context) {
        this.context = context;
    }

    /**
     * Sets the sidebar context.
     *
     * @param sidebarContext
     *            The sidebar context to set.
     */
    public void setSidebarContext(SidebarInterfaceContext sidebarContext) {
        this.sidebarContext = sidebarContext;
    }

    /**
     * Gets the plugin.
     *
     * @return The plugin.
     */
    public ComponentPlugin getPlugin() {
        return plugin;
    }

    /**
     * Sets the plugin.
     *
     * @param plugin
     *            The plugin to set.
     */
    public void setPlugin(ComponentPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * The id of the {@link Component}.
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * The parent id.
     */
    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    /**
     * The tab index.
     */
    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * If the {@link Component} is walkable.
     */
    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    /**
     * The {@link org.gielinor.game.component.ComponentType}.
     */
    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    /**
     * The actions of the {@link Component}.
     */
    public String[] getActions() {
        return actions;
    }

    public void setActions(String[] actions) {
        this.actions = actions;
    }

    /**
     * The message on the {@link Component}.
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * The tooltip for the {@link Component}.
     */
    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

}
