package org.gielinor.game.node.entity.player.link;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.chunk.AnimateObjectUpdateFlag;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.game.world.update.flag.player.AnimationFlag;
import org.gielinor.game.world.update.flag.player.GraphicFlag;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketHeader;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.AnimateInterfaceContext;
import org.gielinor.net.packet.context.AnimateObjectContext;
import org.gielinor.net.packet.context.DefaultContext;
import org.gielinor.net.packet.context.DisplayModelContext;
import org.gielinor.net.packet.context.DisplayModelContext.ModelType;
import org.gielinor.net.packet.context.GameMessageContext;
import org.gielinor.net.packet.context.HideComponentContext;
import org.gielinor.net.packet.context.InterfaceColourContext;
import org.gielinor.net.packet.context.InterfaceConfigContext;
import org.gielinor.net.packet.context.InterfaceContext;
import org.gielinor.net.packet.context.InterfaceMaxScrollContext;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.context.MusicContext;
import org.gielinor.net.packet.context.PlayerContext;
import org.gielinor.net.packet.context.PositionedGraphicContext;
import org.gielinor.net.packet.context.RunScriptContext;
import org.gielinor.net.packet.context.StringContext;
import org.gielinor.net.packet.context.StringContext498;
import org.gielinor.net.packet.context.SummoningOptionsContext;
import org.gielinor.net.packet.context.SystemUpdateContext;
import org.gielinor.net.packet.context.URLMessageContext;
import org.gielinor.net.packet.context.WindowsPaneContext;
import org.gielinor.net.packet.out.AnimateInterface;
import org.gielinor.net.packet.out.AnimateObjectPacket;
import org.gielinor.net.packet.out.AudioPacket;
import org.gielinor.net.packet.out.DisplayModel;
import org.gielinor.net.packet.out.GameMessage;
import org.gielinor.net.packet.out.HideComponent;
import org.gielinor.net.packet.out.Interface;
import org.gielinor.net.packet.out.InterfaceColour;
import org.gielinor.net.packet.out.InterfaceConfig;
import org.gielinor.net.packet.out.InterfaceMaxScrollPacket;
import org.gielinor.net.packet.out.Logout;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.net.packet.out.MusicPacket;
import org.gielinor.net.packet.out.PositionedGraphic;
import org.gielinor.net.packet.out.RunEnergy;
import org.gielinor.net.packet.out.RunScriptPacket;
import org.gielinor.net.packet.out.StringPacket;
import org.gielinor.net.packet.out.StringPacket498;
import org.gielinor.net.packet.out.SummoningOptions;
import org.gielinor.net.packet.out.SystemUpdatePacket;
import org.gielinor.net.packet.out.URLMessage;
import org.gielinor.net.packet.out.WindowsPane;
import org.gielinor.rs2.pulse.Pulse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Formatter;

/**
 * Represents the class used to dispatching packets.
 *
 * @author Emperor
 * @author 'Vexia
 */
public final class PacketDispatch {

    private static final Logger log = LoggerFactory.getLogger(PacketDispatch.class);

    /**
     * The instance of the {@code Player}.
     */
    private final Player player;

    /**
     * The player context.
     */
    private final PlayerContext context;

    /**
     * Constructs a new {@code PacketDispatch} {@code Object}.
     *
     * @param player the player.
     */
    public PacketDispatch(Player player) {
        this.player = player;
        this.context = new PlayerContext(player);
    }

    /**
     * Send a game message.
     *
     * @param message    The game message.
     * @param filterType The filter type.
     */
    public void sendMessage(String message, int filterType) {
        message = String.valueOf(message); // null => "null"
        if (message.length() > 255) {
            log.warn("{} - attempted to send very long message: {}", player.getName(), message);
            message = message.substring(0, 255);
        }
        PacketRepository.send(GameMessage.class, new GameMessageContext(player, message, filterType));
    }

    public void sendMessage(String template, Object... args) {
        sendMessage(new Formatter().format(template, args).toString());
    }

    /**
     * Send a game message.
     *
     * @param message The game message.
     */
    public void sendMessage(String message) {
        sendMessage(message, 0);
    }

    /**
     * Send a console message.
     *
     * @param message The console message.
     */
    public void sendConsoleMessage(String message) {
        message = String.valueOf(message); // null => "null"
        if (message.length() > 255) {
            log.warn("{} - attempted to send very long message: {}", player.getName(), message);
            message = message.substring(0, 255);
        }
        PacketRepository.send(GameMessage.class, new GameMessageContext(player, message + ":console:", 0));
    }

    /**
     * Send a URL message.
     *
     * @param url       The url.
     * @param message   The message.
     * @param attribute The URL attribute.
     */
    public void sendURL(String url, String message, String attribute) {
        if (message == null && attribute == null) {
            PacketRepository.send(URLMessage.class, new URLMessageContext(player, "<a href=\"" + url + "\">", true));
        } else {
            PacketRepository.send(URLMessage.class, new URLMessageContext(player, "<col=0>" + message + " <col=0000EE><a href=\"" + url + "\">" + attribute + "</a>", false));
        }
    }

    /**
     * Send a URL message, launching the URL.
     */
    public void sendURL(String url) {
        sendURL(url, null, null);
    }


    /**
     * Sends the quest interface with text.
     *
     * @param title The title of the interface.
     * @param lines The string to send.
     */
    public void sendQuestInterface(String title, String... lines) {
        sendQuestInterface(title, null, lines);
    }

    /**
     * Sends the quest interface with text.
     *
     * @param title      The title of the interface.
     * @param closeEvent The {@code CloseEvent} for the component.
     * @param lines      The string to send.
     */
    public void sendQuestInterface(String title, CloseEvent closeEvent, String... lines) {
        player.getInterfaceState().close();
        if (lines.length > 256) {
            throw new IllegalArgumentException("Lines cannot exceed 256. Attempted: " + lines.length + ".");
        }
        for (int textId = 46754; textId < 46854; textId++) {
            sendString(textId, "");
        }
        Component component = new Component(46750);
        if (closeEvent != null) {
            component.setCloseEvent(closeEvent);
        }
        PacketRepository.send(InterfaceMaxScrollPacket.class, new InterfaceMaxScrollContext(player, 46752, (20 * lines.length)));
        player.getInterfaceState().open(component);
        if (title != null) {
            sendString(46753, title);
        }
        int index = 0;
        for (int textId = 46754; textId < (46754 + lines.length); textId++) {
            sendString(textId, lines[index]);
            index++;
        }
    }

    /**
     * Sends game messages.
     *
     * @param messages the messages.
     */
    public void sendMessages(int filterType, final String... messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }


    /**
     * Sends game messages.
     *
     * @param messages the messages.
     */
    public void sendMessages(final String... messages) {
        sendMessages(0, messages);
    }

    /**
     * Method used to send a game message on a tick.
     *
     * @param message the message.
     * @param ticks   the ticks.
     */
    public void sendMessage(final String message, int filterType, int ticks) {
        World.submit(new Pulse(ticks, player) {

            @Override
            public boolean pulse() {
                sendMessage(message, filterType);
                return true;
            }
        });
    }

    /**
     * Sends a debug message.
     *
     * @param message The message to send.
     * @return The action sender instance, for chaining.
     */
    public void sendDebugMessage(String message) {
        if (!player.isDebug()) {
            return;
        }
        sendConsoleMessage("<col=ff0000>" + message);
    }

    /**
     * Sends a debug message.
     *
     * @param opcode      The opcode of the packet.
     * @param description The description of the packet.
     * @param params      The parameters of the packet.
     */
    public void sendDebugPacket(int opcode, String description, Object... params) {
        if (!player.isDebug()) {
            return;
        }
        sendDebugMessage("------------------------------------------------------------------------------------------");
        sendDebugMessage("Packet            " + opcode + "  " + description);
        sendDebugMessage("------------------------------------------------------------------------------------------");
        for (Object object : params) {
            sendDebugMessage("Param             " + object.toString());
        }
        sendDebugMessage("------------------------------------------------------------------------------------------");
    }

    /**
     * Send the interface color packet.
     *
     * @param textId
     * @param colour
     * @param hexCode
     */
    public void sendInterfaceColour(int textId, int colour, boolean hexCode) {
        PacketRepository.send(InterfaceColour.class, new InterfaceColourContext(player, textId, colour, hexCode));
    }

    /**
     * Send the interface config packet.
     *
     * @param id    The config value.
     * @param value The config value.
     */
    public void sendInterfaceConfig(int id, int value) {
        PacketRepository.send(InterfaceConfig.class, new InterfaceConfigContext(player, id, value));
    }

    /**
     * Send a access mask.
     *
     * @param id          The access mask id.
     * @param childId     The access mask child id.
     * @param interfaceId The access mask interface Id.
     * @param offset      The access mask off set.
     * @param length      The access mask length.
     */
    public void sendAccessMask(int id, int childId, int interfaceId, int offset, int length) {
        //PacketRepository.send(AccessMask.class, new SummoningOptionsContext(player, id, childId, interfaceId, offset, length));
    }


    /**
     * Sends summoning options.
     *
     * @param options The options.
     */
    public void sendSummoningOptions(String... options) {
        PacketRepository.send(SummoningOptions.class, new SummoningOptionsContext(player, options));
    }

    /**
     * Send a windowns pane.
     *
     * @param windowId The windows pane id.
     * @param type     The windowns pane type.
     */
    public void sendWindowsPane(int windowId, int type) {
        PacketRepository.send(WindowsPane.class, new WindowsPaneContext(player, windowId, type));
    }

    /**
     * sends the system update packet.
     *
     * @param time the amount of time.
     */
    public void sendSystemUpdate(int time) {
        PacketRepository.send(SystemUpdatePacket.class, new SystemUpdateContext(player, time));
    }

    /**
     * Sends music packet.
     *
     * @param musicId The music id.
     */
    public void sendMusic(int musicId) {
        PacketRepository.send(MusicPacket.class, new MusicContext(player, musicId));
    }

    /**
     * Sends the temporary music packet.
     *
     * @param musicId The music id.
     */
    public void sendTempMusic(int musicId) {
        PacketRepository.send(MusicPacket.class, new MusicContext(player, musicId, true));
    }

    /**
     * Sends the sound packet.
     *
     * @param audio the sound.
     */
    public void sendSound(Audio audio) {
        PacketRepository.send(AudioPacket.class, new DefaultContext(player, audio));
    }

    /**
     * Send a run script.
     *
     * @param id      The run script id.
     * @param string  The run script string.
     * @param objects The run scripts objects.
     */
    public void sendRunScript(int id, String string, Object... objects) {
        PacketRepository.send(RunScriptPacket.class, new RunScriptContext(player, id, string, objects));
    }

    /**
     * Sends a minimap state for the player.
     *
     * @param state The minimap state to send.
     */
    public void sendMinimapState(int state) {
        PacketRepository.send(MinimapState.class, new MinimapStateContext(player, state));
    }

    /**
     * Sends a minimap state for the player, then removes it after the given amount of ticks.
     *
     * @param state The minimap state to send.
     * @param ticks The amount of ticks to pass before removing the state.
     */
    public void sendMinimapState(int state, int ticks) {
        PacketRepository.send(MinimapState.class, new MinimapStateContext(player, state));
        World.submit(new Pulse(ticks, player) {

            @Override
            public boolean pulse() {
                PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                return true;
            }
        });
    }

    /**
     * Send the string packet.
     *
     * @param string      The string.
     * @param interfaceId The interface id.
     * @param lineId      The line id.
     */
    public void sendString498(String string, int interfaceId, int lineId) {
        // Used for parsing 400+ interface text for 317
        switch (interfaceId) {
            case 275:
                int subtract = lineId - 4;
                int textId = 8145 + subtract;
                if (textId >= 8195) {
                    textId = 12174 + subtract - 48;
                }
                if (lineId == 2) {
                    textId = 8144;
                }
                sendString(string.replaceAll("<br>", "\n"), textId);
                return;
        }
        PacketRepository.send(StringPacket498.class, new StringContext498(player, string, interfaceId, lineId));
    }

    /**
     * Send the string packet.
     *
     * @param string      The string.
     * @param interfaceId The interface id.
     */
    public void sendString(String string, int interfaceId) {
        PacketRepository.send(StringPacket.class, new StringContext(player, string, interfaceId));
    }

    /**
     * Send the string packet.
     *
     * @param interfaceId The interface id.
     * @param string      The string.
     */
    public void sendString(int interfaceId, String string) {
        sendString(string, interfaceId);
    }

    /**
     * Send the string packet.
     *
     * @param interfaceId The interface id.
     * @param lineId      The line id.
     * @param string      The string.
     */
    public void sendString498(int interfaceId, int lineId, String string) {
        sendString498(string, interfaceId, lineId);
    }

    /**
     * Sends a packet to update a single item.
     *
     * @param interfaceId The interface id.
     * @param childId
     * @param type
     * @param slot        The slot.
     * @param item        The item.
     * @return The action sender instance, for chaining.
     */
    public void sendUpdateItem(int interfaceId, int childId, int type, int slot, Item item) {
        PacketBuilder packetBuilder = new PacketBuilder(34, PacketHeader.SHORT);
        packetBuilder.putShort(interfaceId).putSmart(slot);
        if (item != null) {
            packetBuilder.putShort(item.getId() + 1);
            int count = item.getCount();
            if (count > 254) {
                packetBuilder.put((byte) 255);
                packetBuilder.putInt(count);
            } else {
                packetBuilder.put((byte) count);
            }
        } else {
            packetBuilder.putShort(0);
            packetBuilder.put((byte) 0);
        }
        player.getSession().write(packetBuilder);
    }

    /**
     * Sends a packet to update a single item.
     *
     * @param interfaceId The interface id.
     * @param slot        The slot.
     * @param item        The item.
     * @return The action sender instance, for chaining.
     */
    public void sendUpdateItem(int interfaceId, int slot, Item item) {
        PacketBuilder packetBuilder = new PacketBuilder(34, PacketHeader.SHORT);
        packetBuilder.putShort(interfaceId).putSmart(slot);
        if (item != null) {
            packetBuilder.putShort(item.getId() + 1);
            int count = item.getCount();
            if (count > 254) {
                packetBuilder.put((byte) 255);
                packetBuilder.putInt(count);
            } else {
                packetBuilder.put((byte) count);
            }
        } else {
            packetBuilder.putShort(0);
            packetBuilder.put((byte) 0);
        }
        player.getSession().write(packetBuilder);
    }

    /**
     * Sends a packet to update a single item.
     *
     * @param interfaceId The interface id.
     * @param slot        The slot.
     * @param itemId      The item id.
     * @return The action sender instance, for chaining.
     */
    public void sendUpdateItem(int interfaceId, int slot, int itemId) {
        PacketBuilder packetBuilder = new PacketBuilder(34, PacketHeader.SHORT);
        packetBuilder.putShort(interfaceId).putSmart(slot);
        if (itemId != -1) {
            packetBuilder.putShort(itemId + 1);
            packetBuilder.put((byte) 1);
        } else {
            packetBuilder.putShort(0);
            packetBuilder.put((byte) 0);
        }
        player.getSession().write(packetBuilder);
    }

    /**
     * Sends a packet to update a single item.
     *
     * @param interfaceId The interface id.
     * @param slot        The slot.
     * @param item        The item.
     * @return The action sender instance, for chaining.
     */
    public void sendUpdateItem(int interfaceId, int slot, Item item, int count) {
        PacketBuilder packetBuilder = new PacketBuilder(34, PacketHeader.SHORT);
        packetBuilder.putShort(interfaceId).putSmart(slot);
        if (item != null) {
            packetBuilder.putShort(item.getId() + 1);
            if (count > 254) {
                packetBuilder.put((byte) 255);
                packetBuilder.putInt(count);
            } else {
                packetBuilder.put((byte) count);
            }
        } else {
            packetBuilder.putShort(0);
            packetBuilder.put((byte) 0);
        }
        player.getSession().write(packetBuilder);
    }

    /**
     * Sends a packet to update a single item.
     *
     * @param item        The item.
     * @param interfaceId The interface id.
     * @param slot        The slot.
     * @return The action sender instance, for chaining.
     */
    public void sendUpdateItem(Item item, int interfaceId, int slot) {
        sendUpdateItem(interfaceId, slot, item);
    }

    /**
     * Sends a packet to update a group of items.
     *
     * @param interfaceId The interface id.
     * @param items       The items.
     */
    public void sendUpdateItems(int interfaceId, Item[] items) {
        PacketBuilder packetBuilder = new PacketBuilder(53, PacketHeader.SHORT);
        packetBuilder.putShort(interfaceId);
        packetBuilder.putShort(items.length);
        for (Item item : items) {
            if (item != null) {
                int count = item.getCount();
                if (count > 254) {
                    packetBuilder.put((byte) 255);
                    packetBuilder.putIntB(count);
                } else {
                    packetBuilder.put((byte) count);
                }
                packetBuilder.putLEShortA(item.getId() + 1);
            } else {
                packetBuilder.put((byte) 0);
                packetBuilder.putLEShortA(0);
            }
        }
        player.getSession().write(packetBuilder);
    }

    /**
     * Send a update packet for the amount of run energy.
     */
    public void sendRunEnergy() {
        PacketRepository.send(RunEnergy.class, getContext());
    }

    /**
     * Send the logout packet.
     */
    public void sendLogout() {
        PacketRepository.send(Logout.class, getContext());
    }

    /**
     * Send the interface packet without closing the current component.
     *
     * @param interfaceId The interface id.
     */
    public void sendInterface(int interfaceId) {
        PacketRepository.send(Interface.class, new InterfaceContext(player, interfaceId, false));
    }

    /**
     * Send the interface animation packet.
     *
     * @param animationId The animation id.
     * @param interfaceId The interface id.
     * @param childId     The child id.
     */
    public void sendAnimationInterface(int animationId, int interfaceId, int childId) {
        PacketRepository.send(AnimateInterface.class, new AnimateInterfaceContext(player, animationId, interfaceId, childId));
    }

    /**
     * Send the player on interface packet.
     *
     * @param interfaceId The interface id.
     * @param childId     The child id.
     */
    public void sendPlayerOnInterface(int interfaceId, int childId) {
        PacketRepository.send(DisplayModel.class, new DisplayModelContext(player, interfaceId, childId));
    }

    /**
     * Send the non-player character on interface packet.
     *
     * @param npcId       The non-player character's id.
     * @param interfaceId The interface id.
     * @param childId     The child id.
     */
    public void sendNpcOnInterface(int npcId, int interfaceId, int childId) {
        PacketRepository.send(DisplayModel.class, new DisplayModelContext(player, npcId, interfaceId, childId));
    }

    /**
     * Send the item on interface packet.
     *
     * @param itemId      The item id.
     * @param amount      The item amount.
     * @param interfaceId The interface id.
     * @param childId     The child id.
     */
    public void sendItemOnInterface(int itemId, int amount, int interfaceId, int childId) {
        PacketRepository.send(DisplayModel.class, new DisplayModelContext(player, ModelType.ITEM, itemId, amount, interfaceId, childId));
    }

    /**
     * Send the item on interface packet.
     *
     * @param itemId      The item id.
     * @param zoom        the zoom.
     * @param interfaceId The interface id.
     * @param childId     The child id.
     */
    public void sendItemZoomOnInterface(int itemId, int zoom, int interfaceId, int childId) {
        PacketRepository.send(DisplayModel.class, new DisplayModelContext(player, ModelType.ITEM, itemId, zoom, interfaceId, childId, zoom));
    }

    /**
     * Send the item on interface packet.
     *
     * @param itemId      The item id.
     * @param zoom        the zoom.
     * @param interfaceId The interface id.
     */
    public void sendItemZoomOnInterface(int itemId, int zoom, int interfaceId) {
        PacketRepository.send(DisplayModel.class, new DisplayModelContext(player, ModelType.ITEM, itemId, zoom, interfaceId, 0, zoom));
    }

    /**
     * Send the item on interface packet.
     *
     * @param itemId      The item id.
     * @param amount      The amount.
     * @param zoom        the zoom.
     * @param interfaceId The interface id.
     * @param childId     The child id.
     */
    public void sendItemZoomOnInterface(int itemId, int amount, int zoom, int interfaceId, int childId) {
        PacketRepository.send(DisplayModel.class, new DisplayModelContext(player, ModelType.ITEM, itemId, amount, interfaceId, childId, zoom));
    }

    /**
     * Send the interface config packet.
     *
     * @param interfaceId The interface id.
     * @param childId     The child id.
     * @param hide        If the component should be hidden.
     */
    public void sendHideComponent(int interfaceId, int childId, boolean hide) {
        PacketRepository.send(HideComponent.class, new HideComponentContext(player, interfaceId, childId, hide));
    }

    /**
     * Send the interface config packet.
     *
     * @param interfaceId The interface id.
     * @param hide        If the component should be hidden.
     */
    public void sendHideComponent(int interfaceId, boolean hide) {
        PacketRepository.send(HideComponent.class, new HideComponentContext(player, interfaceId, -1, hide));
    }

    /**
     * Send a animation update flag mask.
     *
     * @param id The animation id.
     */
    public void sendAnimation(int id) {
        player.getUpdateMasks().register(new AnimationFlag(new Animation(id)));
    }

    /**
     * Send a animation update flag mask.
     *
     * @param id    The animation id.
     * @param delay The animation delay.
     */
    public void sendAnimation(int id, int delay) {
        player.getUpdateMasks().register(new AnimationFlag(new Animation(id, delay)));
    }

    /**
     * Send a graphic update flag mask.
     *
     * @param id The graphic id.
     */
    public void sendGraphic(int id) {
        player.getUpdateMasks().register(new GraphicFlag(new Graphics(id)));
    }

    /**
     * Sends the positioned graphic.
     *
     * @param id       the id.
     * @param height   the height.
     * @param delay    the delay.
     * @param location the location.
     */
    public void sendPositionedGraphic(int id, int height, int delay, Location location) {
        PacketRepository.send(PositionedGraphic.class, new PositionedGraphicContext(player, new Graphics(id, height, delay), location));
    }

    /**
     * Sends the positioned graphic.
     *
     * @param graphics the graphics.
     * @param location the location.
     */
    public void sendPositionedGraphics(Graphics graphics, Location location) {
        PacketRepository.send(PositionedGraphic.class, new PositionedGraphicContext(player, graphics, location));

    }

    /**
     * Method used to send an object animation.
     *
     * @param object    the object.
     * @param animation the animation.
     */
    public void sendObjectAnimation(GameObject object, Animation animation) {
        animation = new Animation(animation.getId(), animation.getDelay(), animation.getPriority());
        animation.setObject(object);
        RegionManager.getRegionChunk(object.getLocation()).flag(new AnimateObjectUpdateFlag(animation));
    }

    /**
     * Method used to send an object animation.
     *
     * @param object    the object.
     * @param animation the animation.
     */
    public void sendObjectAnimation(GameObject object, Animation animation, boolean global) {
        if (global) {
            sendObjectAnimation(object, animation);
            return;
        }
        animation.setObject(object);
        PacketRepository.send(AnimateObjectPacket.class, new AnimateObjectContext(player, animation));
    }

    /**
     * Send a graphic update flag mask.
     *
     * @param id     The graphic id.
     * @param height The graphic height.
     */
    public void sendGraphic(int id, int height) {
        player.getUpdateMasks().register(new GraphicFlag(new Graphics(id, height)));
    }

    /**
     * Changes to the given sidebar ID.
     *
     * @param index The index of the sidebar tab.
     */
    public void sendSidebarTab(int index) {
        PacketBuilder packetBuilder = new PacketBuilder(106);
        packetBuilder.putC(index);
        player.getSession().write(packetBuilder);
    }

    /**
     * Gets the player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the context.
     *
     * @return The context.
     */
    public PlayerContext getContext() {
        return context;
    }


}
