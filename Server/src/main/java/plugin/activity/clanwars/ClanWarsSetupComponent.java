package plugin.activity.clanwars;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the {@link org.gielinor.game.component.ComponentPlugin} for the Clan Wars setup interface.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ClanWarsSetupComponent extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(24126, this);
        ComponentDefinition.put(24242, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (button == 24239) {
            WarSession warSession = player.getExtension(WarSession.class);
            if (warSession != null) {
                warSession.acceptSetup();
            }
            return true;
        }
        if (button == 24250) {
            WarSession warSession = player.getExtension(WarSession.class);
            if (warSession != null) {
                warSession.confirmSetup();
            }
            return true;
        }
        ClanWarsRule clanWarsRule = ClanWarsRule.forId(button);
        if (clanWarsRule == null) {
            return false;
        }
        clanWarsRule.toggle(player, true);
        return true;
    }

}