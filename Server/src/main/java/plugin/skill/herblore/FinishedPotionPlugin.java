package plugin.skill.herblore;

import org.gielinor.game.content.skill.member.herblore.FinishedPotion;
import org.gielinor.game.content.skill.member.herblore.GenericPotion;
import org.gielinor.game.content.skill.member.herblore.UnfinishedPotion;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the finished potion plugin creating.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FinishedPotionPlugin extends UseWithHandler {

    /**
     * Constructs a new {@code FinishedPotionPlugin} {@code Object}
     */
    public FinishedPotionPlugin() {
        super(getUnfinishedItems());
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (FinishedPotion potion : FinishedPotion.values()) {
            addHandler(potion.getIngredient().getId(), ITEM_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final FinishedPotion finished =
            FinishedPotion.getPotion(event.getUsedItem().getName().contains("(unf)") ?
                event.getUsedItem() : event.getBaseItem(), event.getUsedItem().getName().contains("(unf)") ?
                event.getBaseItem() : event.getUsedItem());
        if (finished == null) {
            return false;
        }
        final GenericPotion potion = GenericPotion.transform(finished);
        event.getPlayer().getDialogueInterpreter().open(21947748, potion);
        return true;
    }


    /**
     * Method used to gather the unfinished item bases.
     *
     * @return the ids.
     */
    public static int[] getUnfinishedItems() {
        int[] ids = new int[UnfinishedPotion.values().length];
        int counter = 0;
        for (UnfinishedPotion potion : UnfinishedPotion.values()) {
            ids[counter] = potion.getPotion().getId();
            counter++;
        }
        return ids;
    }
}
