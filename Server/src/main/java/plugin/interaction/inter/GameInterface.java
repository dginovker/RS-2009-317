package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.XPDropContext;
import org.gielinor.net.packet.out.XPDrop;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the component plugin used for the game interface.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class GameInterface extends ComponentPlugin {


    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(4959, this); // TODO Change id in definition
        return this;
    }

    @Override
    public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() != 4959) {
            return false;
        }
        switch (button) {
            /**
             * Turn quick prayers on / off.
             */
            case 5000:
                player.getActionSender().sendMessage("Coming soon.");
                return true;
            /**
             * Setup quick prayers.
             */
            case 5001:
                player.getActionSender().sendMessage("Coming soon.");
                return true;
            /**
             * Lock XP drops.
             */
            case 5002:
                player.getInterfaceState().force(InterfaceConfiguration.XP_DROP_LOCK.getId(),
                    player.getInterfaceState().get(InterfaceConfiguration.XP_DROP_LOCK) == 0 ? 1 : 0, true);
                return true;
            /**
             * Reset XP drops.
             */
            case 5003:
                player.getSavedData().getGlobalData().setXpDrops(0);
                PacketRepository.send(XPDrop.class, new XPDropContext(player, -1, 0));
                return true;
            /**
             * Toggle XP drops.
             */
            case 5004:
                player.getInterfaceState().force(InterfaceConfiguration.XP_DROPS.getId(),
                    player.getInterfaceState().get(InterfaceConfiguration.XP_DROPS) == 0 ? 1 : 0, true);
                return true;
        }
        return false;
    }

    /**
     * Method used to open the report interface.
     *
     * @param player the player.
     */
    private void openReport(final Player player) {
//		player.getInterfaceState().open(new Component(553)).setCloseEvent(new CloseEvent() {
//			@Override
//			public boolean close(Player player, Component c) {
//				player.getActionSender().sendRunScript(80, "");
//				player.getActionSender().sendRunScript(137, "");
//				return true;
//			}
//		});
        player.getActionSender().sendRunScript(508, "");
        if (player.getDetails().getRights() != Rights.REGULAR_PLAYER) {
            for (int i = 0; i < 18; i++) {
                player.getActionSender().sendHideComponent(553, i, false);
            }
        }
    }

    /**
     * Gets the tab index.
     *
     * @param button The button id.
     * @return The tab index.
     */
    private static int getTabIndex(int button) {
        int tabIndex = button - 27;
        if (button == 91) {
            tabIndex = 14;
        } else if (button > 40) {
            tabIndex = button - 51;
        }
        return tabIndex;
    }
}
