package plugin.interaction.inter;

import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.component.QuestMenuManager;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.World;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InterfaceColourContext;
import org.gielinor.net.packet.context.InterfaceMaxScrollContext;
import org.gielinor.net.packet.context.InterfaceScrollPositionContext;
import org.gielinor.net.packet.in.CommandPacketHandler;
import org.gielinor.net.packet.out.InterfaceColour;
import org.gielinor.net.packet.out.InterfaceMaxScrollPacket;
import org.gielinor.net.packet.out.InterfaceScrollPosition;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.string.TextUtils;

/**
 * Handles the Commands component.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CommandsInterfacePlugin extends ComponentPlugin {

    /**
     * How many commands to send per page.
     */
    private static final int COMMANDS_PER_PAGE = 10;

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(26597, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() != 26597) {
            return false;
        }
        int page = player.getAttribute("COMMAND_PAGE", 0);
        switch (button) {
            case 26704:
                if (page == 0) {
                    return true;
                }
                openPage(player, page - 1, false);
                return true;
            case 26705:
                if ((page + 1) > getMaximumPages(player)) {
                    return true;
                }
                openPage(player, page + 1, false);
                return true;
        }
        return button >= 26604 && button <= (26604 + COMMANDS_PER_PAGE) && openCommandUsage(player, button);
    }

    public static boolean openCommandUsage(Player player, int buttonId) {
        Map<Integer, CommandDescription> pageView = player.getAttribute("PAGE_VIEW");
        if (pageView == null) {
            return false;
        }
        CommandDescription commandDescription = pageView.get(buttonId);
        if (commandDescription == null) {
            return false;
        }
        Command commandClass = CommandPacketHandler.getCommands().get(commandDescription.getCommand());
        boolean canUse = ((player.getRights().ordinal() >= commandDescription.getRights().ordinal() &&
            commandClass.canUse(player))) || commandClass.isBeta();
        if (!canUse) {
            return true;
        }
        String usages = commandDescription.getUsages();
        if (usages == null || usages.isEmpty()) {
            commandClass.execute(player, new String[]{ commandDescription.getCommand() });
            return true;
        }
        QuestMenuManager questMenuManager = player.getQuestMenuManager();
        questMenuManager.setTitle(TextUtils.uppercaseFirst(commandDescription.getCommand()) + " Command Usage");
        questMenuManager.setLines(commandDescription.getUsages());
        questMenuManager.setCloseEvent(new CloseEvent() {

            @Override
            public void close(Player player, Component component) {
                World.submit(new Pulse(1) {

                    @Override
                    public boolean pulse() {
                        openPage(player, player.getAttribute("COMMAND_PAGE", 0), true);
                        return true;
                    }
                });
                questMenuManager.reset();
            }

            @Override
            public boolean canClose(Player player, Component component) {
                return true;
            }
        });
        questMenuManager.send();
        return true;
    }

    public static int getMaximumPages(Player player) {
        int pages = 0;
        int index = 0;
        for (CommandDescription commandDescription : CommandDescription.values()) {
            Command commandClass = CommandPacketHandler.getCommands().get(commandDescription.getCommand());
            boolean canUse = ((player.getRights().ordinal() >= commandDescription.getRights().ordinal() &&
                commandClass.canUse(player)) || commandClass.isBeta());
            if (!canUse) {
                continue;
            }
            if (index == COMMANDS_PER_PAGE) {
                pages++;
                index = 0;
                continue;
            }
            index++;
        }
        return pages;
    }

    public static void openPage(Player player, int page, boolean reOpen) {
        if (player.getAttribute("COMMAND_PAGE", 0) != page) {
            player.setAttribute("COMMAND_PAGE", page);
        }
        int commandsShown = 0;
        int lineIndex = 26604;
        player.getInterfaceState().send(page == 0 ? 50 : 51, 26704);
        player.getInterfaceState().send((page + 1) > getMaximumPages(player) ? 50 : 51, 26705);
        for (int childId = 0; childId < COMMANDS_PER_PAGE; childId++) {
            player.getInterfaceState().send(50, lineIndex + childId);
            player.getInterfaceState().send(50, ((lineIndex + childId) + 50));
        }
        lineIndex = 26604;
        int currentIndex = 0;
        Map<Integer, CommandDescription> pageView = new HashMap<>();
        for (int index = 0; index < CommandDescription.values().size(); index++) {
            if (commandsShown == COMMANDS_PER_PAGE) {
                break;
            }
            if (index > CommandDescription.values().size()) {
                break;
            }
            CommandDescription commandDescription = CommandDescription.values().get(index);
            if (commandDescription == null) {
                continue;
            }
            Command commandClass = CommandPacketHandler.getCommands().get(commandDescription.getCommand());
            boolean canUse = ((player.getRights().ordinal() >= commandDescription.getRights().ordinal() &&
                commandClass.canUse(player)) || commandClass.isBeta());
            if (!canUse) {
                continue;
            }
            if (currentIndex >= (page * COMMANDS_PER_PAGE)) {
                int colour = commandDescription.getRights().isAdministrator() ? 0xF7FF0B :
                    commandDescription.getRights() == Rights.PLAYER_MODERATOR ? 0xA2A2A2 : 0xFF981F;
                PacketRepository.send(InterfaceColour.class, new InterfaceColourContext(player, lineIndex, colour, true));
                player.getActionSender().sendString(lineIndex, commandDescription.getCommand());
                player.getInterfaceState().send(51, lineIndex);
                player.getInterfaceState().send(51, lineIndex + 50);
                player.getActionSender().sendString(lineIndex + 50, commandDescription.getDescription());
                pageView.put(lineIndex, commandDescription);
                lineIndex++;
                commandsShown++;
            }
            currentIndex++;
        }
        player.setAttribute("PAGE_VIEW", pageView);
        player.getActionSender().sendString(Constants.SERVER_NAME + " Commands" + (page > 0 ? " ~ Page " + page : ""), 26599);
        PacketRepository.send(InterfaceMaxScrollPacket.class, new InterfaceMaxScrollContext(player, 26603, (62 * commandsShown)));
        if (reOpen) {
            Component commandsComponent = new Component(26597);
            player.getInterfaceState().open(commandsComponent);
        } else {
            PacketRepository.send(InterfaceScrollPosition.class, new InterfaceScrollPositionContext(player, 26603, 0));
        }
    }
}
