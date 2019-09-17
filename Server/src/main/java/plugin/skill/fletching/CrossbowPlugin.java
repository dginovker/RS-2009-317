package plugin.skill.fletching;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.skill.member.fletching.items.crossbow.CrossbowPulse;
import org.gielinor.game.content.skill.member.fletching.items.crossbow.Limb;
import org.gielinor.game.content.skill.member.fletching.items.crossbow.LimbPulse;
import org.gielinor.game.content.skill.member.fletching.items.crossbow.StringCross;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The plugin for making crossbows.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CrossbowPlugin extends UseWithHandler {

    /**
     * The max amount of sets that can be made.
     */
    private final int MAX_SETS = 10;

    /**
     * Constructs a new {@code CrossbowPlugin} {@code Object}.
     */
    public CrossbowPlugin() {
        super(9420, 9423, 9422, 9425, 9427, 9429, 9431, 9438);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (Limb limb : Limb.values()) {
            addHandler(limb.getStock().getId(), ITEM_TYPE, this);
        }
        for (StringCross stringCross : StringCross.values()) {
            addHandler(stringCross.getItem().getId(), ITEM_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        Player player = event.getPlayer();
        Item item = event.getBaseItem();
        Item second = event.getUsedItem();
        final StringCross stringCross = (event.getUsedItem().getName().toLowerCase().equals("string") ||
            event.getBaseItem().getName().toLowerCase().contains("string")) ? event.getUsedItem().getId() == 9438 ? StringCross.forItem(event.getBaseItem())
            : StringCross.forItem(event.getUsedItem()) : null;
        Limb limb = null;
        if (stringCross == null) {
            limb = Limb.forItems(event.getUsedItem(), event.getBaseItem());
            if (limb == null) {
                player.getDialogueInterpreter().sendPlaneMessage("That's not the correct limb to attach.");
                return true;
            }
        }
        final Limb finalLimb = limb;
        SkillDialogueHandler skillDialogueHandler = new SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.ONE_OPTION, stringCross == null ? finalLimb.getProduct() : stringCross.getProduct()) {

            @Override
            public void create(final int amount, final int index) {
                if (finalLimb == null) {
                    player.getPulseManager().run(new CrossbowPulse(player, item, stringCross, amount));
                    return;
                }
                player.getPulseManager().run(new LimbPulse(player, (item.getName().toLowerCase().contains("stock") ? second : item), finalLimb, amount));
            }

            @Override
            public int getAll(int index) {
                return player.getInventory().getCount(item);
            }

        };
        if (player.getInventory().getCount(item) == 1) {
            skillDialogueHandler.create(1, 0);
        } else {
            skillDialogueHandler.open();
        }
        return true;
    }
}
