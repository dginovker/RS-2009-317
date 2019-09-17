package plugin.skill.fletching;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.dialogue.SkillDialogueHandler.SkillDialogue;
import org.gielinor.game.content.skill.member.fletching.FletchItem;
import org.gielinor.game.content.skill.member.fletching.FletchType;
import org.gielinor.game.content.skill.member.fletching.FletchingPulse;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The plugin for fletching logs.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class LogFletchingPlugin extends UseWithHandler {

    /**
     * Constructs a new {@link plugin.skill.fletching.LogFletchingPlugin} {@code Object}.
     */
    public LogFletchingPlugin() {
        super(1511, 1521, 1519, 1517, 1515, 1513, 6332, 6333);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(946, ITEM_TYPE, this);
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        Item item = event.getBaseItem();
        FletchType type = FletchType.forItem(item);
        if (type == null) {
            item = event.getUsedItem();
            type = FletchType.forItem(item);
        }
        if (type == null) {
            return false;
        }
        SkillDialogue skillDialogue = null;
        if ((skillDialogue = SkillDialogue.forAmount(type.getItems().length)) == null) {
            return false;
        }
        final FletchType fletchType = type;
        Item[] items = new Item[fletchType.getItems().length];
        for (int i = 0; i < fletchType.getItems().length; i++) {
            items[i] = fletchType.getItems()[i].getProduct();
        }
        new SkillDialogueHandler(player, skillDialogue, (Object[]) items) {

            @Override
            public void create(final int amount, final int index) {
                final FletchItem fletchItem = fletchType.getItems()[index];
                fletchItem.setType(fletchType);
                player.getPulseManager().run(new FletchingPulse(player, fletchType.getLog(), amount, fletchItem));
            }

            @Override
            public int getAll(int index) {
                return player.getInventory().getCount(fletchType.getLog());
            }

        }.open();
        return true;
    }

}
