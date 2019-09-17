package plugin.skill.prayer;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the {@link org.gielinor.game.component.ComponentPlugin} for quick prayers.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class QuickPrayerPlugin extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.forId(17200).setPlugin(this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int childId, int slot, int itemId) {
        if (component.getId() != 17200) {
            return false;
        }
        /**
         * Confirm.
         */
        if (childId == 17241) {
            player.getInterfaceState().openTab(Sidebar.PRAYER_TAB.ordinal(), new Component(Sidebar.PRAYER_TAB.getInterfaceId()));
            return true;
        }
        return player.getPrayer().getQuickPrayer().toggleQuickPrayer(childId);
    }

}
