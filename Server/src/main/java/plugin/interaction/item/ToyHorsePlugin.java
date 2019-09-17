package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the option for the toy horse.
 *
 * @author 'Vexia
 */
public class ToyHorsePlugin extends OptionHandler {

    /**
     * The force chat's you can say.
     */
    private static String speech[] = { "Come-on Dobbin, we can win the race!", "Hi-ho Silver, and away", "Neaahhhyyy! Giddy-up horsey!" };

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.lock(2);
        player.getInventory().remove((Item) node);
        int id = node.getId();
        int anim = id == 2524 ? 920 : id == 2526 ? 921 : id == 2522 ? 919 : 918;
        player.animate(new Animation(anim));
        player.sendChat(speech[RandomUtil.random(speech.length)]);
        player.getInventory().add((Item) node);
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.setOptionHandler("play-with", this);
        return this;
    }
}
