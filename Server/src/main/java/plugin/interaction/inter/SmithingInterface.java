package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.Craftable;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.smithing.Smithable;
import org.gielinor.game.content.skill.free.smithing.SmithingConstants;
import org.gielinor.game.content.skill.free.smithing.SmithingPulse;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * @author 'Vexia
 */
public class SmithingInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(SmithingConstants.SMITHING_INTERFACE, this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() != SmithingConstants.SMITHING_INTERFACE) {
            return false;
        }
        Smithable smithable = null;
        for (Smithable smithable1 : Smithable.values()) {
            if (smithable1.getCraftable().getItem().getId() == itemId) {
                smithable = smithable1;
                break;
            }
        }
        if (smithable == null) {
            return false;
        }
        int amount = opcode == 145 ? 1 : opcode == 117 ? 5 : opcode == 43 ? 10 : -1;
        if (amount == -1) {
            return true;
        }
        Craftable craftable = smithable.getCraftable();
        if (craftable == null) {
            return true;
        }
        if (itemId != smithable.getCraftable().getItem().getId()) {
            return true;
        }
        if (!player.getInventory().contains(Item.HAMMER, 1)) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a hammer to work the metal with.");
            return true;
        }
        if (player.getSkills().getLevel(Skills.SMITHING) < craftable.getLevel()) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a Smithing level of " + craftable.getLevel() + " to make a " + craftable.getItem().getName() + ".");
            player.playAnimation(Animation.create(-1));
            return true;
        }
        if (!player.getInventory().contains(smithable.getBar())) {
            player.getActionSender().sendMessage("You do not have enough bars to smith this item.");
            return true;
        }
        player.getPulseManager().run(new SmithingPulse(player, new Item(itemId, amount), smithable, amount));
        return true;
    }
}
