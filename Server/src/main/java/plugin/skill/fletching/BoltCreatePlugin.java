package plugin.skill.fletching;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.skill.member.fletching.items.bolts.Bolt;
import org.gielinor.game.content.skill.member.fletching.items.bolts.BoltPulse;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The plugin for creating bolts.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class BoltCreatePlugin extends UseWithHandler {

    /**
     * The max amount of sets that can be made.
     */
    private final int MAX_SETS = 10;

    /**
     * Constructs a new {@code BoltCreatePlugin} {@code Object}.
     */
    public BoltCreatePlugin() {
        super(314);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (Bolt bolt : Bolt.values()) {
            addHandler(bolt.getItem().getId(), ITEM_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        Item item = event.getBaseItem();
        Item second = event.getUsedItem();
        final Bolt bolt = Bolt.forItem(item.getName().toLowerCase().contains("feather") ? second : item);
        SkillDialogueHandler skillDialogueHandler = new SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.ONE_OPTION, bolt.getProduct()) {

            @Override
            public void create(final int amount, final int index) {
                int sets = amount;
                if (sets > MAX_SETS) {
                    sets = MAX_SETS;
                }
                player.getPulseManager().run(new BoltPulse(player, item, bolt, sets));
            }

            @Override
            public int getAll(int index) {
                return player.getInventory().getCount(item);
            }

        };
        if (player.getInventory().getCount(bolt.getItem()) == 10) {
            skillDialogueHandler.create(1, 0);
        } else {
            skillDialogueHandler.open();
        }
        return true;
    }
}
