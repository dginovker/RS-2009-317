package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.free.crafting.armour.LeatherCrafting.SoftLeather;
import org.gielinor.game.content.skill.free.crafting.armour.SoftCraftPulse;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the {@link org.gielinor.game.component.ComponentPlugin} for crafting leather.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class LeatherCraftInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(2311, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        final SoftLeather soft = SoftLeather.forButton(button);
        if (soft == null) {
            return false;
        }
        int amount = SoftLeather.getAmount(button);
        if (amount == -1) {
            return false;
        }
        player.getPulseManager().run(new SoftCraftPulse(player, null, soft, amount));
        return true;
    }
}
