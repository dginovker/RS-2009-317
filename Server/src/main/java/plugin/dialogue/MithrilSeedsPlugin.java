package plugin.dialogue;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.player.FaceLocationFlag;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the plugin used for mithril seeds.
 *
 * @author 'Vexia
 * @version 1.2
 */
public final class MithrilSeedsPlugin extends OptionHandler {

    /**
     * Represents the item to use.
     */
    private static final Item ITEM = new Item(299, 1);

    /**
     * Represents the common flower ids.
     */
    private static final int FLOWERS[] = { 2980, 2981, 2982, 2983, 2984, 2985, 2986 };

    /**
     * Represents the rare flower ids.
     */
    private static final int RARE[] = new int[]{ 2987, 2988 };

    /**
     * Represents the animation.
     */
    private static final Animation ANIMATION = new Animation(827);

    /**
     * Gets a random flower from an array.
     *
     * @return the flower.
     */
    public static int getFlower(int[] array) {
        return array[RandomUtil.random(array.length)];
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(299).getConfigurations().put("option:plant", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        if (player.getAttribute("delay:plant", -1) > World.getTicks()) {
            return true;
        }
        if (RegionManager.getObject(player.getLocation()) != null) {
            player.getActionSender().sendMessage("You can't plant a flower here.");
            return true;
        }
        player.lock(3);
        player.animate(ANIMATION);
        player.getInventory().remove(ITEM);
        final GameObject object = ObjectBuilder.add(new GameObject(getFlower(RandomUtil.random(100) == 1 ? RARE : FLOWERS), player.getLocation()), 100);
        player.moveStep();
        player.getPulseManager().run(new Pulse(1, player) {

            @Override
            public boolean pulse() {
                player.faceLocation(FaceLocationFlag.getFaceLocation(player, object));
                player.getDialogueInterpreter().open(1 << 16 | 1, object);
                return true;
            }
        });
        player.setAttribute("delay:plant", World.getTicks() + 3);
        player.getActionSender().sendMessage("You open the small mithril case and drop a seed by your feet.");
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }


}
