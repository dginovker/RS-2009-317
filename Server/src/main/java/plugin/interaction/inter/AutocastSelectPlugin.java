package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.combat.equipment.WeaponInterface;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the auto casting component plugin.
 *
 * @author Emperor
 */
public final class AutocastSelectPlugin extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.forId(1829).setPlugin(this);
        ComponentDefinition.forId(1689).setPlugin(this);
        ComponentDefinition.forId(12050).setPlugin(this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        if(player.getAttribute("reverse_gun_game", false)){
            player.getActionSender().sendMessage("You cannot disable auto-casting during a gun game.");
            return true;
        }
        if (!player.getAttribute("autocast_select", false)) {
            return true;
        }
        player.removeAttribute("autocast_select");
        final WeaponInterface weaponInterface = player.getExtension(WeaponInterface.class);
        if (weaponInterface != null) {
            weaponInterface.selectAutoSpell(button, true);
            player.getInterfaceState().openTab(Sidebar.ATTACK_TAB, weaponInterface);
        }
        return true;
    }

}
