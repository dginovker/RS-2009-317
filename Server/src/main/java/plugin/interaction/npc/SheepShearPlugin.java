package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the plugin used to shear a sheep.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class SheepShearPlugin extends OptionHandler {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(893);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.setOptionHandler("shear", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final NPC sheep = (NPC) node;
        if (!player.getInventory().contains(1735, 1)) {
            player.getActionSender().sendMessage("You need shears to shear a sheep.");
            return true;
        }
        if (sheep.getDefinition().hasAction("attack")) {
            player.getActionSender().sendMessage("That one looks a little too violent to shear...");
            return true;
        }
        if (player.getInventory().freeSlots() == 0) {
            player.getActionSender().sendMessage("You don't have enough space in your inventory to carry any wool you would shear.");
            return true;
        }
        sheep.lock(3);
        sheep.getWalkingQueue().reset();
        player.animate(ANIMATION);
        int random = RandomUtil.random(1, 5);
        if (random != 4) {
            sheep.getLocks().lockMovement(2);
            sheep.transform(5153);
            player.getActionSender().sendMessage("You get some wool.");
            player.getInventory().add(new Item(1737, 1));// 5160
            World.submit(new Pulse(80, sheep) {

                @Override
                public boolean pulse() {
                    sheep.reTransform();
                    return true;
                }
            });
        } else {
            player.getActionSender().sendMessage("The sheep manages to get away from you!");
            sheep.moveStep();
        }
        return true;
    }

}
