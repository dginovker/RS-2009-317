package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin for the Prayer interface.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class PrayerTabInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(671, this);
        ComponentDefinition.put(21356, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        final PrayerType type = PrayerType.forId(button);
        if (type == null) {
            return false;
        }
        if(player.getAttribute("reverse_gun_game", false)){
            player.getActionSender().sendMessage("You cannot use prayers in a gun game match.");
            return false;
        }
        player.getPrayer().toggle(type);
        return true;
    }
}
