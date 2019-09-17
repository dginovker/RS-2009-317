package plugin.skill.smithing;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.free.smithing.SmithingConstants;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used for the furnace.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FurnaceOptionPlugin extends OptionHandler {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(833);

    /**
     * Represents the items used for the tutorial island (ores).
     */
    private static final Item[] ITEMS = new Item[]{ new Item(438, 1), new Item(436, 1) };

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.setOptionHandler("smelt", this);
        ObjectDefinition.forId(3044).getConfigurations().put("option:use", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        show(player);
        return true;
    }

    /**
     * Shows the items.
     *
     * @param player the player.
     */
    public static void show(final Player player) {
        int[][] smelting = {
            { 2405, 2349 }, { 2406, 2351 }, { 2407, 2355 },
            { 2409, 2353 }, { 2410, 2357 }, { 2411, 2359 },
            { 2412, 2361 }, { 2413, 2363 }
        };
        for (int[] smelt : smelting) {
            player.getActionSender().sendItemZoomOnInterface(smelt[1], 150, smelt[0]);
        }
        player.getInterfaceState().openChatbox(new Component(SmithingConstants.SMELTING_INTERFACE));
    }
}
