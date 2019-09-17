package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the haystack plugin.
 * @author 'Vexia
 * @version 1.0
 */
public final class HaystackPlugin extends OptionHandler {

    /**
     * Represents the needle to give.
     */
    private static final Item NEEDLE = new Item(1733);

    /**
     * Represents the animation.
     */
    private static final Animation ANIMATION = new Animation(827);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(298).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(299).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(300).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(304).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(36892).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(36893).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(36894).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(36895).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(36896).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(36897).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(36898).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(36899).getConfigurations().put("option:search", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final GameObject object = ((GameObject) node);
        final int rand = RandomUtil.random(50);
        player.lock(2);
        player.animate(ANIMATION);
        player.getActionSender().sendMessage("You search the " + object.getName().toLowerCase() + "...");
        if (rand == 1 && player.getInventory().freeSlots() > 0 || player.getInventory().containsItem(NEEDLE)) {
            player.getInventory().add(NEEDLE);
            player.getDialogueInterpreter().sendDialogues(player, FacialExpression.NO_EXPRESSION, "Wow! A needle!", "Now what are the chances of finding that?");
            return true;
        }
        player.getActionSender().sendMessage("You find nothing of interest.");
        return true;
    }

}
