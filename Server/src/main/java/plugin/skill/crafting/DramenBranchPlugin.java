package plugin.skill.crafting;


import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.skill.free.crafting.DramenBranchPulse;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The plugin for cutting dramen branches into staves.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class DramenBranchPlugin extends UseWithHandler {

    /**
     * Creates the dramen branch cutting plugin.
     */
    public DramenBranchPlugin() {
        super(771);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(946, ITEM_TYPE, this);
        addHandler(5605, ITEM_TYPE, this);
        return null;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        Item node = event.getBaseItem();
        if (event.getUsedItem().getId() == 771) {
            node = event.getUsedItem();
        }
        final Item finalNode = node;
        SkillDialogueHandler handler = new SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.ONE_OPTION, new Item(772)) {

            @Override
            public void create(final int amount, int index) {
                player.getPulseManager().run(new DramenBranchPulse(player, finalNode, amount));
            }

            @Override
            public int getAll(int index) {
                return player.getInventory().getCount(finalNode);
            }

        };
        if (player.getInventory().getCount(node) == 1) {
            handler.create(0, 1);
        } else {
            handler.open();
        }
        return true;
    }

}
