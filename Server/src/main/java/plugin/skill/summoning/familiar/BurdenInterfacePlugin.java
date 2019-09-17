package plugin.skill.summoning.familiar;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.member.summoning.familiar.BurdenBeast;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RunScript;

/**
 * Handles the beast of burden interface.
 *
 * @author Emperor
 */
public final class BurdenInterfacePlugin extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(25708, this); // Inventory
        ComponentDefinition.put(25706, this); // Burden inventory
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() != 25708 && component.getId() != 25706) {
            return false;
        }
        if (!player.getFamiliarManager().hasFamiliar() || !player.getFamiliarManager().getFamiliar().isBurdenBeast()) {
            return false;
        }
        final BurdenBeast burdenBeast = (BurdenBeast) player.getFamiliarManager().getFamiliar();
        final boolean withdraw = component.getId() == 25706;
        final Container container = withdraw ? burdenBeast.getContainer() : player.getInventory();
        if (opcode == 185 && button == 25705) {
            if (burdenBeast.getContainer().isEmpty()) {
                player.getActionSender().sendMessage("Your familiar is not carrying any items.");
                return true;
            }
            burdenBeast.withdrawAll();
            return true;
        }
        final Item item = slot >= 0 && slot < container.capacity() ? container.get(slot) : null;
        if (item == null) {
            return true;
        }
        switch (opcode) {
            case 145:
                burdenBeast.transfer(item, 1, withdraw);
                return true;
            case 117:
                burdenBeast.transfer(item, 5, withdraw);
                return true;
            case 43:
                burdenBeast.transfer(item, 10, withdraw);
                return true;
            case 129:
                burdenBeast.transfer(item, container.getCount(item), withdraw);
                return true;
            case 135:
                player.setAttribute("runscript", new RunScript() {

                    @Override
                    public boolean handle() {
                        burdenBeast.transfer(item, (int) getValue(), withdraw);
                        return true;
                    }
                });
                player.getDialogueInterpreter().sendInput(false, "Enter the amount:");
                return true;
        }
        return false;
    }

}
