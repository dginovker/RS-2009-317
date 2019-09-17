package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.system.communication.ClanCommunication;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RunScript;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the {@link org.gielinor.game.component.ComponentPlugin} for the clan chat tab and setup interfaces.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ClanInterfacePlugin extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(32320, this);
        ComponentDefinition.put(32100, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int buttonId, int slot, int itemId) {
        switch (component.getId()) {
            /**
             * Clan chat tab.
             */
            case 32100:
                switch (buttonId) {
                    /**
                     * Join / leave.
                     */
                    case 32102:
                        if (player.getCommunication().getClan() != null) {
                            player.getCommunication().getClan().leave(player, true, true);
                            player.getCommunication().setClan(null);
                            return true;
                        }
                        player.setAttribute("runscript", new RunScript() {

                            @Override
                            public boolean handle() {
                                String name = TextUtils.formatDisplayName((String) value);
                                ClanCommunication clan = name.equalsIgnoreCase(Constants.DEFAULT_CLAN_NAME) ? ClanCommunication.DEFAULT :
                                    ClanCommunication.get(name.toLowerCase());
                                if (clan == null || clan.getName().equals("Chat disabled")) {
                                    player.getActionSender().sendMessage("The channel you tried to join does not exist.");
                                    return true;
                                }
                                if (clan.enter(player)) {
                                    player.getCommunication().setClan(clan);
                                }
                                return true;
                            }
                        });
                        player.getDialogueInterpreter().sendInput(true, "Enter the player name whose channel you wish to join:");
                        return true;
                    /**
                     * Setup.
                     */
                    case 32106:
                        if (player.getInterfaceState().getComponent(32320) != null) {
                            player.getActionSender().sendMessage("Please close the interface you have open before using 'Clan Setup'");
                            return true;
                        }
                        ClanCommunication.openSettings(player);
                        return true;
                }
                return false;

            /**
             * Clan chat setup.
             */
            case 32320:
                switch (buttonId) {
                    case 32326:

                        return true;
                }
                return false;
        }
        return true;
    }

}
