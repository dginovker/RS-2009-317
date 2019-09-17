package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.perk.PerkManagement;
import org.gielinor.game.node.entity.player.info.title.LoyaltyTitleManagement;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the management component.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ManagementInterfacePlugin extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(23035, this);
        ComponentDefinition.put(23057, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (player.getAttribute("PERKS_PAGE") != null) {
            return PerkManagement.handle(player, component, button);
        }
        return LoyaltyTitleManagement.handle(player, component, button);
    }


}
