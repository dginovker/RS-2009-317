package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the bank pin interface component plugin.
 * @author Emperor
 * @author Aero
 * @version 1.0
 */
public final class BankPinInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.forId(13).setPlugin(this);
        ComponentDefinition.forId(14).setPlugin(this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() == 13 && button > 0 && button < 11) {
            player.getBankPin().update(button - 1);
            return true;
        }
        switch (button) {
            case 60:
            case 62:
                if (player.getBankPin().hasEnteredPin()) {
                    player.getBankPin().toggleConfirmInterface(true);
                } else {
                    player.getBankPin().open(new Component(13));
                }
                return true;
            case 63://Deleting pin.
                player.getBankPin().setSettingPin(false);
                player.getBankPin().setDeletingPin(true);
                player.getBankPin().open(component);
                return true;
            case 61:
            case 64:
                player.getBankPin().changeRecoveryDelay();
                return true;
            case 65:
                player.getBankPin().cancelPendingPin();
                return true;
            case 89: //Confirm
                player.getBankPin().setSettingPin(true);
                player.getBankPin().open(component);
                return true;
            case 91: //Cancel
                player.getBankPin().toggleConfirmInterface(false);
                player.getBankPin().openSettings();
                return true;
        }
        return false;
    }

}
