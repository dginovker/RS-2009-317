package plugin.skill.fletching;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.skill.member.fletching.items.arrow.ArrowHead;
import org.gielinor.game.content.skill.member.fletching.items.arrow.ArrowHeadPulse;
import org.gielinor.game.content.skill.member.fletching.items.arrow.HeadlessArrowPulse;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The plugin for creating arrows.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ArrowCreatePlugin extends UseWithHandler {

    /**
     * The max amount of sets that can be made.
     */
    private final int MAX_SETS = 10;

    /**
     * Constructs a new {@code ArrowCreatePlugin} {@code Object}.
     */
    public ArrowCreatePlugin() {
        super(314, 53);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(52, ITEM_TYPE, this);
        for (ArrowHead head : ArrowHead.values()) {
            addHandler(head.getTips().getId(), ITEM_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        ArrowHead arrow = null;
        Item item = ((event.getUsedItem().getId() == 314 && event.getBaseItem().getId() == 52) ||
            (event.getUsedItem().getId() == 52 && event.getBaseItem().getId() == 314)) ? event.getUsedItem() : null;
        if (item == null) {
            arrow = ArrowHead.forItem(event.getUsedItem().getName().toLowerCase().contains("tip") ? event.getUsedItem() : event.getBaseItem());
            item = arrow.getProduct();
        }
        final Item item1 = item;
        final ArrowHead arrowHead = arrow;
        SkillDialogueHandler skillDialogueHandler = new SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.ONE_OPTION, arrowHead == null ? new Item(53) : item) {

            @Override
            public void create(final int amount, final int index) {
                int sets = amount;
                if (player.getInventory().getCount((arrowHead != null ? arrowHead.getTips() : new Item(314))) >= 15) {
                    if (sets > MAX_SETS) {
                        sets = MAX_SETS;
                    }
                }
                if (arrowHead != null) {
                    player.getPulseManager().run(new ArrowHeadPulse(player, item1, arrowHead, sets));
                    return;
                }
                player.getPulseManager().run(new HeadlessArrowPulse(player, item1, sets));
            }

            @Override
            public int getAll(int index) {
                return player.getInventory().getCount(item1);
            }

        };
        if (player.getInventory().getCount((arrowHead != null ? arrowHead.getTips() : new Item(314))) == 15) {
            skillDialogueHandler.create(1, 0);
        } else {
            skillDialogueHandler.open();
        }
        return true;
    }

}
