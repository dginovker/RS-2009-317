package org.gielinor.game.node.entity.player.link;

import java.nio.ByteBuffer;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.combat.equipment.WeaponInterface;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ChatboxInterfaceContext;
import org.gielinor.net.packet.context.InterfaceConfigContext;
import org.gielinor.net.packet.context.InterfaceContext;
import org.gielinor.net.packet.context.SidebarInterfaceContext;
import org.gielinor.net.packet.context.WindowsPaneContext;
import org.gielinor.net.packet.out.ChatboxInterface;
import org.gielinor.net.packet.out.CloseChatboxInterface;
import org.gielinor.net.packet.out.CloseInterface;
import org.gielinor.net.packet.out.Interface;
import org.gielinor.net.packet.out.InterfaceConfig;
import org.gielinor.net.packet.out.SidebarInterface;
import org.gielinor.net.packet.out.WindowsPane;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Manages a player's interfaces.
 *
 * @author Emperor
 */
public final class InterfaceState implements SavingModule {

    /**
     * The player.
     */
    private final Player player;
    /**
     * The windows pane.
     */
    private Component windowsPane;
    /**
     * The currently opened component.
     */
    private Component opened;
    /**
     * The tabs.
     */
    private Component[] tabs = new Component[15];
    /**
     * The chatbox component.
     */
    private Component chatbox;
    /**
     * The single tab.
     */
    private Component singleTab;
    /**
     * The overlay component.
     */
    private Component overlay;
    /**
     * The currently opened tab's index.
     */
    private int currentTabIndex = 3;
    /**
     * The amount of configurations.
     */
    public static final int SIZE = 2000;
    /**
     * The configurations.
     */
    private final int[] configurations = new int[SIZE];

    /**
     * The configurations.
     */
    private final int[] savedConfigurations = new int[SIZE];

    /**
     * Constructs a new {@code InterfaceState} {@code Object}.
     *
     * @param player The player.
     */
    public InterfaceState(Player player) {
        this.player = player;
    }

    /**
     * Opens the windows pane.
     *
     * @param windowsPane The windows pane.
     * @return The component instance.
     */
    public Component openWindowsPane(Component windowsPane) {
        this.windowsPane = windowsPane;
        PacketRepository.send(WindowsPane.class, new WindowsPaneContext(player, windowsPane.getId(), 0));
        windowsPane.open(player);
        return windowsPane;
    }

    /**
     * Opens a component.
     *
     * @param componentId The component id.
     * @return The opened component.
     */
    public Component openComponent(int componentId) {
        return open(new Component(componentId));
    }

    /**
     * Opens a component.
     *
     * @param component The component to open.
     * @return The opened component.
     */
    public Component open(Component component) {
        if (!close()) {
            return null;
        }
        if (component.getDefinition() == null) {
            PacketRepository.send(Interface.class, new InterfaceContext(player, component.getId(), false));
            return opened = component;
        }
        //		if (component.getDefinition().getContext() == null) {
        component.getDefinition().setContext(new InterfaceContext(null, component.getId(), false));
        //		}
        component.open(player);
        return opened = component;
    }

    /**
     * Opens a component.
     *
     * @param component The component to open.
     * @return The opened component.
     */
    public Component switchOpen(Component component) {
        if (!closeSwitch()) {
            return null;
        }
        if (component.getDefinition() == null) {
            PacketRepository.send(Interface.class, new InterfaceContext(player, component.getId(), false));
            return opened = component;
        }
        //		if (component.getDefinition().getContext() == null) {
        component.getDefinition().setContext(new InterfaceContext(null, component.getId(), false));
        //		}
        component.open(player);
        return opened = component;
    }

    /**
     * Checks if a main interface.
     *
     * @return <code>True</code> if so.
     */
    public boolean isOpened() {
        return opened != null;
    }

    /**
     * Checks if the player has a chat box interface opened (disregarding default chat box).
     *
     * @return <code>True</code> if so.
     */
    public boolean hasChatbox() {
        return chatbox != null && chatbox.getId() > 0;
    }

    /**
     * Safely closes the currently opened interface.
     */
    public boolean close() {
        if (opened != null && opened.close(player)) {
            if (opened.getDefinition() != null) {
                InterfaceContext interfaceContext = null;
                if (opened.getDefinition().getContext() == null) {
                    interfaceContext = new InterfaceContext(player, -1, false);
                } else {
                    interfaceContext = opened.getDefinition().getContext();
                    interfaceContext.setPlayer(player);
                }
                if (!interfaceContext.isWalkable()) {
                    PacketRepository.send(CloseInterface.class, interfaceContext);
                    if (opened.getCloseEvent() != null) {
                        opened.getCloseEvent().close(player, opened);
                    }
                }
            }
            opened = null;
        }
        return opened == null;
    }

    /**
     * Safely closes the currently opened interface.
     */
    public boolean closeSwitch() {
        if (opened != null && opened.close(player)) {
            if (opened.getDefinition() != null) {
                InterfaceContext interfaceContext = null;
                if (opened.getDefinition().getContext() == null) {
                    interfaceContext = new InterfaceContext(player, -1, false);
                } else {
                    interfaceContext = opened.getDefinition().getContext();
                    interfaceContext.setPlayer(player);
                }
                if (!interfaceContext.isWalkable()) {
                    if (opened.getCloseEvent() != null) {
                        opened.getCloseEvent().close(player, opened);
                    }
                }
            }
            opened = null;
        }
        return opened == null;
    }

    /**
     * Closes the currently opened interface.
     */
    public boolean closeUnchecked() {
        PacketRepository.send(CloseInterface.class, (opened == null ||
            opened.getDefinition() == null || opened.getDefinition().getContext() == null)
            ? new InterfaceContext(player, -1, false) : opened.getDefinition().getContext().setPlayer(player));
        opened = null;
        return true;
    }

    /**
     * Checks if the current interface is walkable.
     *
     * @return <code>True</code> if so.
     */
    public boolean isWalkable() {
        if (opened != null) {
            if (opened.getId() == 389) {
                return false;
            }
            if (opened.getDefinition().getContext() != null) {
                if (opened.getDefinition().getContext().isWalkable()) {
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * Safely closes the component.
     *
     * @param component The component.
     */
    public void close(Component component) {
        if (component.close(player)) {
            if (component.getDefinition().getContext() == null) {
                PacketRepository.send(CloseInterface.class, new InterfaceContext(player, -1, -1, component.getId(), true));
            } else {
                PacketRepository.send(CloseInterface.class, component.getDefinition().getContext().setPlayer(player));
            }
        }
    }

    /**
     * Closes the chatbox interface.
     */
    public void closeChatbox() {
        if (chatbox != null && chatbox.getId() > 0) {
            if (chatbox.close(player)) {
                PacketRepository.send(CloseChatboxInterface.class, new ChatboxInterfaceContext(player, chatbox.getId()));
            }
        }
    }

    /**
     * Opens a tab and removes the other tabs.
     *
     * @param component The component to open.
     * @return The component.
     */
    public Component openSingleTab(Component component) {
        if (component.getDefinition().getContext() == null || component.getDefinition().getContext().getComponentId() != 126) {
            component.getDefinition().setContext(new InterfaceContext(null, component.getId(), false));
        }
        component.open(player);
        return singleTab = component;
    }

    /**
     * Opens a tab and removes the other tabs.
     *
     * @param component The component to open.
     * @return The component.
     */
    public Component openSingleTab(Component component, int slot) {
        if (component.getDefinition().getContext() == null || component.getDefinition().getContext().getComponentId() != 126) {
            component.getDefinition().setSidebarContext(new SidebarInterfaceContext(null, component.getId(), slot));
        }
        component.open(player);
        return singleTab = component;
    }

    /**
     * Closes the current single tab opened.
     */
    public boolean closeSingleTab() {
        if (singleTab != null) {
            close(singleTab);
            singleTab = null;
        }
        return true;
    }

    /**
     * Gets the currently opened single tab.
     *
     * @return The tab opened.
     */
    public Component getSingleTab() {
        return singleTab;
    }

    /**
     * Removes the tabs.
     *
     * @param tabs The tab indexes.
     */
    public void removeTabs(int... tabs) {
        boolean changeViewedTab = false;
        for (int slot : tabs) {
            if (slot == currentTabIndex) {
                changeViewedTab = true;
            }
            Component tab = this.tabs[slot];
            if (tab != null) {
                close(tab);
                this.tabs[slot] = null;
                openTab(slot, new Component(-1));
            }
        }
        if (changeViewedTab) {
            int currentIndex = -1;
            if (this.tabs[3] == null) {
                for (int i = 0; i < this.tabs.length; i++) {
                    if (this.tabs[i] != null) {
                        currentIndex = i;
                        break;
                    }
                }
            } else {
                currentIndex = 3;
            }
            if (currentIndex > -1) {
                setViewedTab(currentIndex);
            }
        }
    }

    /**
     * Restores the tabs.
     */
    public void restoreTabs() {
        for (int i = 1; i < tabs.length; i++) {
            if (tabs[i] == null) {
                switch (i) {
                    case 0:
                        WeaponInterface inter = player.getExtension(WeaponInterface.class);
                        if (inter == null) {
                            player.addExtension(WeaponInterface.class, inter = new WeaponInterface(player));
                        }
                        openTab(0, inter);
                        break;
                    case 5:
                        openTab(5, new Component(player.getSavedData().getGlobalData().getPrayerBook())); // Prayer
                        break;
                    case 6:
                        openTab(6, new Component(player.getSpellBookManager().getSpellBook())); // Magic
                        break;
//                    case 7:
//                        if (player.getFamiliarManager().hasFamiliar()) {
//                            openTab(7, new Component(662));
//                        }
//                        break;
                    default:
                        openTab(i, new Component(Sidebar.values()[i].getInterfaceId()));
                }
            }
        }
    }

    /**
     * Opens the default tabs.
     */
    public void openDefaultTabs() {
        player.getConfigManager().set(108, 0);
        for (Sidebar sidebar : Sidebar.values()) {
            if (sidebar == Sidebar.MAGIC_TAB) {
                openTab(sidebar.ordinal(), new Component(player.getSpellBookManager().getSpellBook()));
                continue;
            }
            if (sidebar == Sidebar.PRAYER_TAB) {
                openTab(sidebar.ordinal(), new Component(player.getSavedData().getGlobalData().getPrayerBook()));
                continue;
            }
            if (sidebar == Sidebar.QUEST_TAB) {
                openTab(sidebar.ordinal(), new Component(player.getAttribute("quest_tab", Sidebar.QUEST_TAB.getInterfaceId())));
                continue;
            }
            openTab(sidebar.ordinal(), new Component(sidebar.getInterfaceId()));
        }
        WeaponInterface inter = player.getExtension(WeaponInterface.class);
        if (inter == null) {
            player.addExtension(WeaponInterface.class, inter = new WeaponInterface(player));
        }
        openTab(0, inter); // Attack
//		if (player.getProperties().getAutocastSpell() != null) {
//			inter.selectAutoSpell(inter.getAutospellId(player.getProperties().getAutocastSpell().getSpellId()), true);
//		}
    }

    /**
     * Closes the default tabs.
     */
    public void closeDefaultTabs() {
        WeaponInterface inter = player.getExtension(WeaponInterface.class);
        if (inter != null) {
            close(inter); // Attack
        }
        close(new Component(320)); // Skills
        close(new Component(274)); // Quest
        close(new Component(149)); // inventory
        close(new Component(387)); // Equipment
        close(new Component(player.getSpellBookManager().getSpellBook()));
        close(new Component(662)); // summoning.
        close(new Component(550)); // Friends
        close(new Component(551)); // Ignores
        close(new Component(589)); // Clan chat
        close(new Component(261)); // Settings
        close(new Component(464)); // Emotes
        close(new Component(187)); // Music
        close(new Component(182)); // Logout
        // close(new Component(player.getSavedData().getGlobalData().getPrayerBook()));
    }

    /**
     * Opens a tab.
     *
     * @param slot      The tab slot;
     * @param component The component.
     */
    public void openTab(int slot, Component component) {
        if (slot == Sidebar.MAGIC_TAB.ordinal()) {
            player.getProperties().setAutocastSpell(null);
        }
        if (component.getId() == WeaponInterface.WeaponInterfaces.UNARMED.getInterfaceId() && !(component instanceof WeaponInterface)) {
            throw new IllegalStateException("Attack tab can only be instanced as " + WeaponInterface.class.getCanonicalName() + "!");
        }
        if (component.getDefinition().getSidebarContext() == null) {
            PacketRepository.send(SidebarInterface.class, new SidebarInterfaceContext(player, component.getId(), slot));
        }
        //component.open(player);
        PacketRepository.send(SidebarInterface.class, new SidebarInterfaceContext(player, component.getId(), slot));
        tabs[slot] = component;
    }

    /**
     * Opens a tab.
     *
     * @param sidebar   The sidebar to open the tab in.
     * @param component The component.
     */
    public void openTab(Sidebar sidebar, Component component) {
        openTab(sidebar.ordinal(), component);
    }

    /**
     * Opens a chat box interface.
     *
     * @param componentId The component id.
     */
    public void openChatbox(int componentId) {
        openChatbox(new Component(componentId));
    }


    /**
     * Opens a chat box interface.
     *
     * @param component The component to open.
     */
    public void openChatbox(Component component) {
        PacketRepository.send(ChatboxInterface.class, new ChatboxInterfaceContext(player, component.getId()));
//		if (chatbox == null) {
//			chatbox = component;
//			chatbox.open(player);
//		}
        setChatbox(component);
    }

    /**
     * Gets the component for the given component id.
     *
     * @param componentId The component id.
     * @return The component.
     */
    public Component getComponent(int componentId) {
        if (opened != null && opened.getId() == componentId) {
            return opened;
        }
        if (chatbox != null && chatbox.getId() == componentId) {
            return chatbox;
        }
        if (singleTab != null && singleTab.getId() == componentId) {
            return singleTab;
        }
        if (overlay != null && overlay.getId() == componentId) {
            return overlay;
        }
        for (Component c : tabs) {
            if (c != null && c.getId() == componentId) {
                return c;
            }
        }
        return null;
    }

    /**
     * Sets the currently viewed tab.
     *
     * @param tabIndex The tab index.
     */
    public void setViewedTab(int tabIndex) {
        if (tabs[tabIndex] == null) {
            throw new IllegalStateException("Tab at index " + tabIndex + " is null!");
        }
        currentTabIndex = tabIndex;
        switch (tabIndex) {
            case 0:
                tabIndex = 1;
                break;
            case 1:
                tabIndex = 2;
                break;
            case 2:
                tabIndex = 3;
                break;
            case 3:
                tabIndex = 0;
                break;
        }
        if (tabIndex > 9) {
            tabIndex--;
        }
        player.getActionSender().sendSidebarTab(tabIndex);
    }

    /**
     * Checks if the main component opened matches the given component id.
     *
     * @param id The component id.
     * @return <code>True</code> if so.
     */
    public boolean hasMainComponent(int id) {
        return opened != null && opened.getId() == id;
    }

    /**
     * Opens an overlay.
     *
     * @param component The component.
     */
    public void openOverlay(Component component) {
        if (overlay != null && !overlay.close(player)) {
            return;
        }
        overlay = component;
        overlay.getDefinition().setContext(new InterfaceContext(null, component.getId(), true));
        overlay.open(player);
    }

    /**
     * Closes the current overlay.
     */
    public void closeOverlay() {
        if (overlay != null && overlay.close(player)) {
            InterfaceContext interfaceContext = new InterfaceContext(player, -1, -1, -1, true);
            PacketRepository.send(Interface.class, interfaceContext);
            PacketRepository.send(CloseInterface.class, interfaceContext);
            overlay = null;
        }
    }

    /**
     * Initializes the interface configurations.
     */
    public void initConfigurations() {
        for (int configId = 0; configId < savedConfigurations.length; configId++) {
            if (configId == InterfaceConfiguration.HD_TEXTURES.getId()) {
                continue;
            }
            int value = savedConfigurations[configId];
            if (value != 0) {
                set(configId, value, false);
            }
        }
    }

    /**
     * Sets a configuration.
     *
     * @param config The configuration.
     * @param value  The value.
     */
    public void set(InterfaceConfiguration config, boolean value) {
        set(config.getId(), value);
    }

    /**
     * Sets a configuration.
     *
     * @param id    The id of the configuration.
     * @param value The value.
     */
    public void set(int id, boolean value) {
        set(id, value ? 1 : 0);
    }

    /**
     * Sets a configuration.
     *
     * @param config The configuration.
     * @param value  The value.
     */
    public void set(InterfaceConfiguration config, int value) {
        force(config.getId(), value, false);
    }

    /**
     * Sets the configuration.
     *
     * @param config The configuration id.
     * @param value  The value.
     * @param saved  If the configuration should be saved.
     */
    public void set(InterfaceConfiguration config, int value, boolean saved) {
        force(config.getId(), value, saved);
    }

    /**
     * Sets a configuration.
     *
     * @param id    The configuration id.
     * @param value The value.
     */
    public void set(int id, int value) {
        force(id, value, false);
    }

    /**
     * Sets a configuration for a set amount of time.
     *
     * @param id    the id.
     * @param value the value.
     * @param delay the delay.
     */
    public void set(final int id, final int value, int delay) {
        set(id, value);
        World.submit(new Pulse(delay, player) {

            @Override
            public boolean pulse() {
                set(id, 0);
                return true;
            }
        });
    }

    /**
     * Sets a configuration.
     *
     * @param id    The configuration id.
     * @param value The value.
     */
    public void set(int id, int value, boolean saved) {
        force(id, value, saved);
    }

    /**
     * Forces a configuration.
     *
     * @param id    The configuration id.
     * @param value The value.
     * @param saved Whether or not to save.
     */
    public void force(int id, int value, boolean saved) {
        PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(player, id, value));
        configurations[id] = value;
        if (saved) {
            savedConfigurations[id] = value;
        }
    }

    /**
     * Forces a configuration.
     *
     * @param interfaceConfiguration The {@link org.gielinor.game.node.entity.player.link.InterfaceConfiguration}.
     * @param value                  The value.
     * @param saved                  Whether or not to save.
     */
    public void force(InterfaceConfiguration interfaceConfiguration, boolean value, boolean saved) {
        force(interfaceConfiguration, value ? 1 : 0, saved);
    }

    /**
     * Forces a configuration.
     *
     * @param interfaceConfiguration The {@link org.gielinor.game.node.entity.player.link.InterfaceConfiguration}.
     * @param value                  The value.
     * @param saved                  Whether or not to save.
     */
    public void force(InterfaceConfiguration interfaceConfiguration, int value, boolean saved) {
        PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(player, interfaceConfiguration.getId(), value));
        configurations[interfaceConfiguration.getId()] = value;
        if (saved) {
            savedConfigurations[interfaceConfiguration.getId()] = value;
        }
    }

    /**
     * Sends the configuration without caching.
     */
    public void send(int id, int value) {
        PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(player, id, value));
        configurations[id] = value;
    }

    /**
     * Gets the configuration value.
     *
     * @param id The config id.
     * @return The value.
     */
    public int get(int id) {
        return configurations[id];
    }

    /**
     * Gets the configuration value.
     *
     * @param configuration The {@link org.gielinor.net.packet.out.InterfaceConfig}.
     * @return The value.
     */
    public int get(InterfaceConfiguration configuration) {
        return configurations[configuration.getId()];
    }

    /**
     * Sends the player's interface configurations all at once.
     */
    public void sendInterfaceConfigurations() {
        for (int configId = 237; configId < 249; configId++) {
            if (configId == InterfaceConfiguration.HD_TEXTURES.getId()) {
                continue;
            }
            send(configId, savedConfigurations[configId]);
        }
    }

    /**
     * Gets the player's saved configurations.
     *
     * @return The saved configurations array.
     */
    public int[] getSavedConfigurations() {
        return savedConfigurations;
    }

    /**
     * Resets the configurations.
     */
    public void resetConfigurations() {
        for (int i = 0; i < configurations.length; i++) {
            configurations[i] = 0;
        }
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        for (int index = 0; index < savedConfigurations.length; index++) {
            int value = savedConfigurations[index];
            if (value != 0) {
                byteBuffer.putShort((short) index);
                byteBuffer.putInt(value);
            }
        }
        byteBuffer.putShort((short) -1);
    }

    @Override
    public void parse(ByteBuffer buffer) {
        int index = 0;
        while ((index = buffer.getShort()) != -1) {
            savedConfigurations[index] = buffer.getInt();
        }
    }

    /**
     * Gets the weapon tab interface.
     *
     * @return The weapon interface.
     */
    public WeaponInterface getWeaponTab() {
        return player.getExtension(WeaponInterface.class);
    }

    /**
     * Gets the opened.
     *
     * @return The opened.
     */
    public Component getOpened() {
        return opened;
    }

    /**
     * Sets the opened.
     *
     * @param opened The opened to set.
     */
    public void setOpened(Component opened) {
        this.opened = opened;
    }

    /**
     * Gets the tabs.
     *
     * @return The tabs.
     */
    public Component[] getTabs() {
        return tabs;
    }

    /**
     * Sets the tabs.
     *
     * @param tabs The tabs to set.
     */
    public void setTabs(Component[] tabs) {
        this.tabs = tabs;
    }

    /**
     * Gets the chatbox.
     *
     * @return The chatbox.
     */
    public Component getChatbox() {
        return chatbox;
    }

    /**
     * Sets the chatbox.
     *
     * @param chatbox The chatbox to set.
     */
    public void setChatbox(Component chatbox) {
        this.chatbox = chatbox;
    }

    /**
     * Gets the overlay.
     *
     * @return The overlay.
     */
    public Component getOverlay() {
        return overlay;
    }

    /**
     * Sets the overlay.
     *
     * @param overlay The overlay to set.
     */
    public void setOverlay(Component overlay) {
        this.overlay = overlay;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the currentTabIndex.
     *
     * @return The currentTabIndex.
     */
    public int getCurrentTabIndex() {
        return currentTabIndex;
    }

    /**
     * Sets the currentTabIndex.
     *
     * @param currentTabIndex The currentTabIndex to set.
     */
    public void setCurrentTabIndex(int currentTabIndex) {
        this.currentTabIndex = currentTabIndex;
    }

    /**
     * Gets the windowsPane.
     *
     * @return The windowsPane.
     */
    public Component getWindowsPane() {
        return windowsPane;
    }

}
