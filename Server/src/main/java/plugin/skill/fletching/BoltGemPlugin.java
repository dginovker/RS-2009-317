package plugin.skill.fletching;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.skill.member.fletching.items.gem.GemBolt;
import org.gielinor.game.content.skill.member.fletching.items.gem.GemBoltPulse;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The plugin for creating gem bolts.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class BoltGemPlugin extends UseWithHandler {

    /**
     * The max amount of sets that can be made.
     */
    private final int MAX_SETS = 10;

    /**
     * Constructs a new {@code BoltGemPlugin} {@code Object}.
     */
    public BoltGemPlugin() {
        super(45, 46, 9187, 9188, 9189, 9190, 9191, 9192, 9193, 9194);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (GemBolt gemBolt : GemBolt.values()) {
            addHandler(gemBolt.getBase().getId(), ITEM_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        Item base = event.getBaseItem();
        Item tip = event.getUsedItem();
        final GemBolt gemBolt = GemBolt.forItems(base.getName().contains("tip") ? tip : base, base.getName().contains("tip") ? base : tip);
        SkillDialogueHandler skillDialogueHandler = new SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.ONE_OPTION,
            gemBolt.getProduct()) {

            @Override
            public void create(final int amount, final int index) {
                int sets = amount;
                if (sets > MAX_SETS) {
                    sets = MAX_SETS;
                }
                player.getPulseManager().run(new GemBoltPulse(player, base, gemBolt, sets));
            }

            @Override
            public int getAll(int index) {
                return player.getInventory().getCount(base);
            }

        };
//		if (player.getInventory().getCount(gemBolt.getBase()) == 1) {
//			skillDialogueHandler.create(0, 1);
//		} else {
        skillDialogueHandler.open();
//		}
        return true;
    }
}
