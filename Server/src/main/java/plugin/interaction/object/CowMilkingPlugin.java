package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the plugin used to milk a cow.
 * @author 'Vexia
 * @version 1.0
 */
public final class CowMilkingPlugin extends OptionHandler {

    /**
     * Represents the animation to use;
     */
    private static final Animation ANIMATION = new Animation(2305);

    /**
     * Represents the items related to cow milking plugin.
     */
    private static final Item[] ITEMS = new Item[]{ new Item(1925, 1), new Item(3727, 1), new Item(1927, 1) };

    @Override
    public boolean handle(final Player player, Node node, String option) {
        if (option.equalsIgnoreCase("steal-cowbell")) {
            player.getDialogueInterpreter().open(70099, "You need to have started the Cold War quest to attempt this.");
            return true;
        }
        if (!player.getInventory().contains(1925, 1) && !player.getInventory().contains(3727, 1)) {
            player.getDialogueInterpreter().open(3807, true, true);
            return true;
        }
        player.animate(ANIMATION);
        player.getPulseManager().run(new Pulse(8, player) {

            @Override
            public boolean pulse() {
                if (player.getInventory().remove(ITEMS[0]) || player.getInventory().remove(ITEMS[1])) {
                    player.getInventory().add(ITEMS[2]);
                    player.getActionSender().sendMessage("You milk the cow.");
                }
                if (player.getInventory().contains(1925, 1) || player.getInventory().contains(3727, 1)) {
                    player.animate(ANIMATION);
                    return false;
                }
                return true;
            }

            @Override
            public void stop() {
                super.stop();
                player.animate(new Animation(-1));
            }
        });
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(8689).getConfigurations().put("option:milk", this);
        ObjectDefinition.forId(12111).getConfigurations().put("option:milk", this);
        ObjectDefinition.setOptionHandler("steal-cowbell", this);
        return this;
    }

}
