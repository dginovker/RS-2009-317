package org.gielinor.net.packet.in;

import org.gielinor.game.node.entity.combat.equipment.WeaponInterface;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.XPDropContext;
import org.gielinor.net.packet.out.XPDrop;

/**
 * Handles additional action buttons.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ActionButtonHandler {

    public static boolean handle(Player player, int interfaceId, int childId) {
        switch (childId) {
            /**
             * Cancel autocast selection.
             */
            case 2004:
            case 6161:
                player.removeAttribute("autocast_select");
                player.getInterfaceState().openTab(Sidebar.ATTACK_TAB, player.getExtension(WeaponInterface.class));
                return true;
            /**
             * Turn quick prayers on / off.
             */
            case 5000:
                if (player.getSavedData().getGlobalData().getPrayerBook() != 5608) {
                    player.getActionSender().sendMessage("You cannot use quick curses yet.");
                    return true;
                }
                player.getPrayer().getQuickPrayer().toggle();
                return true;
            /**
             * Setup quick prayers.
             */
            case 5001:
                if (player.getSavedData().getGlobalData().getPrayerBook() != 5608) {
                    player.getActionSender().sendMessage("You cannot setup quick curses yet.");
                    return true;
                }
                player.getPrayer().getQuickPrayer().open();
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
}
