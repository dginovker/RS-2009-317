package plugin.interaction.inter;

import org.gielinor.game.content.skill.free.crafting.armour.LeatherCrafting;
import org.gielinor.game.content.skill.free.crafting.armour.LeatherCrafting.SoftLeather;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used for crafting leather.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class LeatherCraftPlugin extends UseWithHandler {

    /**
     * Constructs a new {@code LeatherCraftPlugin} {@code Object}.
     */
    public LeatherCraftPlugin() {
        super(LeatherCrafting.LEATHER, LeatherCrafting.HARD_LEATHER, LeatherCrafting.GREEN_LEATHER,
            LeatherCrafting.BLUE_LEATHER, LeatherCrafting.RED_LEATHER, LeatherCrafting.BLACK_LEATHER, LeatherCrafting.SNAKESKIN);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(LeatherCrafting.NEEDLE, ITEM_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        if (event.getBaseItem().getId() == LeatherCrafting.LEATHER || event.getUsedItem().getId() == LeatherCrafting.LEATHER) {
            SoftLeather.open(event.getPlayer());
        } else if (event.getBaseItem().getId() == LeatherCrafting.HARD_LEATHER || event.getUsedItem().getId() == LeatherCrafting.HARD_LEATHER) {
            event.getPlayer().getDialogueInterpreter().open(48923, "hard");
        } else if (event.getBaseItem().getId() == LeatherCrafting.SNAKESKIN || event.getUsedItem().getId() == LeatherCrafting.SNAKESKIN) {
            event.getPlayer().getDialogueInterpreter().open(48923, "snakeskin", event.getUsedItem().getId());
        } else {
            event.getPlayer().getDialogueInterpreter().open(48923, "dragon", event.getUsedItem().getId());
        }
        return true;
    }
}
