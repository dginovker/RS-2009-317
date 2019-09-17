package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.skill.free.magic.TeleportLocation;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.map.zone.impl.WildernessZone;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the teleports {@link org.gielinor.game.component.ComponentPlugin}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class TeleportsInterfacePlugin extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(24794, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() != 24794) {
            return false;
        }
        // Set tab?
        int tabId = 0;
        int tabButton = 24801;
        boolean selectionButton = (button == 24801 || button == 24803 || button == 24805 || button == 24807 || button == 24809 || button == 24811 || button == 24813);
        if (selectionButton) {
            for (TeleportLocation ignored : TeleportLocation.values()) {
                if (tabButton == button) {
                    open(player, tabId);
                    return true;
                }
                tabButton += 2;
                tabId++;
            }
        }
        int teleportTab = player.getAttribute("TELEPORT_TAB", -1);
        if (teleportTab == -1) {
            return true;
        }
        int teleportButton = 24824;
        TeleportLocation teleportLocation = TeleportLocation.values()[teleportTab];


        int index = 0;
        for (Location location : teleportLocation.getLocations()) {
            if (teleportButton == button) {

                if (!teleportLocation.canTeleport(player, index))
                    return true;

                final String name = teleportLocation.getNames()[index].toLowerCase();
                switch (name){
                    case "thieving":
                        open(player, TeleportLocation.THIEVING.ordinal());
                        return true;
                    case "agility":
                        open(player, TeleportLocation.AGILITY.ordinal());
                        return true;
                }
                handleTeleport(player, location,
                    teleportLocation.getWarningMessages() == null ? null
                    : teleportLocation.getWarningMessages()[index], true);

                return true;
            }
            teleportButton += 5;
            index++;
        }
        return false;
    }

    public static void handleTeleport(Player player, Location location, String warningMessage, boolean warning) {
        player.getInterfaceState().close();
        player.getDialogueInterpreter().close();
        if (warning) {
            if (WildernessZone.Companion.isInZone(location)) {
                player.getDialogueInterpreter().open("TeleportDialogue", 3, true, "<col=8A0808>Warning!<br>The location you are attempting to teleport to<br>is located in the wilderness.<br>Are you sure you wish to teleport there?", location);
                return;
            }
            if (warningMessage != null) {
                player.getDialogueInterpreter().open("TeleportDialogue", 4, true, warningMessage, location);
                return;
            }
        }
        player.getInterfaceState().close();
        player.getDialogueInterpreter().close();
        if (location.inArea(new ZoneBorders(3218, 3211, 3228, 3230))) {
            AchievementDiary.finalize(player, AchievementTask.LUMBRIDGE_TELEPORT);
        }
        player.getTeleporter().send(location.randomize());
    }

    /**
     * Opens the teleports interface.
     *
     * @param player The player.
     */
    public static void open(Player player) {
        open(player, 0);
    }

    /**
     * Opens the teleports interface.
     *
     * @param player The player.
     * @param tab    The tab to open.
     */
    public static void open(Player player, int tab) {
        // Reset buttons
        TeleportLocation teleportLocation = TeleportLocation.values()[tab];
        player.getInterfaceState().set(InterfaceConfiguration.TELEPORT_TAB, tab + 1);
        player.getInterfaceState().set(InterfaceConfiguration.TELEPORT_BUTTONS, teleportLocation.getNames().length);
        int textId = 24823;
        for (String name : teleportLocation.getNames()) {
            player.getActionSender().sendString(textId, name);
            textId += 5;
        }
        player.setAttribute("TELEPORT_TAB", tab);
        if (tab == 0 || !player.getInterfaceState().hasMainComponent(24794)) {
            player.getInterfaceState().open(new Component(24794));
        }
    }
}
