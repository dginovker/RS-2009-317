package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.free.crafting.spinning.Spinnable;
import org.gielinor.game.content.skill.free.crafting.spinning.SpinningPulse;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RunScript;

public class SpinningInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(22653, this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
        Spinnable spinnable = Spinnable.forId(button);
        int amount = Spinnable.getAmount(button);
        if (spinnable == null || amount == -1) {
            player.getActionSender().sendMessage("Something went wrong, please report this on the forums.");
            return false;
        }
        if (!player.getInventory().contains(spinnable.getItem())) {
            String itemName = spinnable.name().contains("TREE_ROOT") ? "tree roots" : spinnable.getItem().getName().toLowerCase();
            itemName = itemName.equalsIgnoreCase("hair") ? "yak hair" : itemName;
            player.getActionSender().sendMessage("You need " + itemName + " to make this.");
            return true;
        }
        Item neededItem = spinnable.getItem();
        if (spinnable.name().contains("TREE_ROOT")) {
            for (Spinnable spinnable1 : Spinnable.values()) {
                if (!spinnable1.name().contains("TREE_ROOT")) {
                    continue;
                }
                if (player.getInventory().contains(spinnable1.getItem())) {
                    neededItem = spinnable1.getItem();
                    break;
                }
            }
        }
        if (amount == 10) {
            amount = player.getInventory().getCount(neededItem);
        }
        if (amount == 0) {
            final Item finalNeededItem = neededItem;
            player.getDialogueInterpreter().sendInput(false, "Enter the amount:");
            player.setAttribute("runscript", new RunScript() {

                @Override
                public boolean handle() {
                    int inputAmount = (int) value;
                    player.getPulseManager().run(new SpinningPulse(player, finalNeededItem, inputAmount, spinnable));
                    return true;
                }
            });
            return true;
        }
        player.getPulseManager().run(new SpinningPulse(player, neededItem, amount, spinnable));
        return true;
    }
}
