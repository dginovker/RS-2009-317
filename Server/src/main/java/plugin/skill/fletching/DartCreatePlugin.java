package plugin.skill.fletching;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.skill.member.fletching.items.darts.Dart;
import org.gielinor.game.content.skill.member.fletching.items.darts.DartPulse;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The plugin for creating darts.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DartCreatePlugin extends UseWithHandler {

    /**
     * The max amount of sets that can be made.
     */
    private final int MAX_SETS = 10;

    /**
     * Constructs a new {@code DartCreatePlugin} {@code Object}.
     */
    public DartCreatePlugin() {
        super(314);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (Dart dart : Dart.values()) {
            addHandler(dart.getItem().getId(), ITEM_TYPE, this);
        }
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        Item item = event.getBaseItem();
        Item second = event.getUsedItem();
        final Dart dart = Dart.forItem(item.getName().toLowerCase().contains("feather") ? second : item);
        new SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.ONE_OPTION, dart.getProduct()) {

            @Override
            public void create(final int amount, final int index) {
                int sets = amount;
                if (sets > MAX_SETS) {
                    sets = MAX_SETS;
                }
                player.getPulseManager().run(new DartPulse(player, item, dart, sets));
            }

            @Override
            public int getAll(int index) {
                return player.getInventory().getCount(item);
            }

        }.open();
        return true;
    }
}
