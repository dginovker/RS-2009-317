package plugin.skill.fletching;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.skill.member.fletching.items.bow.StringBow;
import org.gielinor.game.content.skill.member.fletching.items.bow.StringPulse;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The plugin for stringing bows.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class BowPlugin extends UseWithHandler {

    /**
     * Constructs a new {@link plugin.skill.fletching.BowPlugin} {@code Object}.
     */
    public BowPlugin() {
        super(1777);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (StringBow bw : StringBow.values()) {
            addHandler(bw.getItem().getId(), ITEM_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        Item item = event.getBaseItem();
        Item second = event.getUsedItem();
        final StringBow stringBow = StringBow.forItem(item.getId()) == null ? StringBow.forItem(second.getId()) : StringBow.forItem(item.getId());
        SkillDialogueHandler skillDialogueHandler = new SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.ONE_OPTION, stringBow.getProduct()) {

            @Override
            public void create(final int amount, final int index) {
                player.getPulseManager().run(new StringPulse(player, item, stringBow, amount));
            }

            @Override
            public int getAll(int index) {
                return player.getInventory().getCount(item);
            }

        };
//		if (player.getInventory().getCount(stringBow.getItem()) == 1) {
//			skillDialogueHandler.create(1, 0);
//		} else {
        skillDialogueHandler.open();
//		}
        return true;
    }
}
