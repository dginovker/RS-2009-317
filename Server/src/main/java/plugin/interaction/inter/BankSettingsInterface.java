package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The {@link org.gielinor.game.component.ComponentPlugin} for the bank settings interface.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class BankSettingsInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(25682, this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
        switch (button) {
            /**
             * Dismiss menu.
             */
            case 25688:
                player.getBank().open();
                return true;
            /**
             * Tab display.
             */
            case 25693:
            case 25694:
            case 25695:
            case 25696:
            case 25697:
            case 25698:
                player.getInterfaceState().force(InterfaceConfiguration.BANK_ICON_TYPE, (button == 25693 || button == 25694) ? 1 : 0, true);
                player.getInterfaceState().force(InterfaceConfiguration.BANK_NUMERAL_TYPE, (button == 25695 || button == 25696) ? 1 : 0, true);
                player.getInterfaceState().force(InterfaceConfiguration.BANK_ROMAN_TYPE, (button == 25697 || button == 25698) ? 1 : 0, true);
                return true;
        }
        return false;
    }
}
