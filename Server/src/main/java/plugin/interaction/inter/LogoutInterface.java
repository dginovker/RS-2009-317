package plugin.interaction.inter;


import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the interface used to logout of the game.
 * @author 'Vexia
 * @version 1.0
 */
public final class LogoutInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(2449, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() != 2449) {
            return false;
        }
        if (!player.getZoneMonitor().canLogout()) {
            return true;
        }
        if (player.inCombat() || (player.getAttribute("combat-time", -1L) > System.currentTimeMillis())) {
            player.getActionSender().sendMessage("You can't log out until 10 seconds after the end of combat.");
            return true;
        }
        if (player.getAttribute("logoutDelay", 0) < World.getTicks()) {
            player.getActionSender().sendLogout();
            player.setAttribute("logoutDelay", World.getTicks() + 3);
        }
        return true;
    }
}
