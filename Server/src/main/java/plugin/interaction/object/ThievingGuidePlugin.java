package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the thieving guide plugin.
 * @author 'Vexia
 * @date 16/11/2013
 */
public class ThievingGuidePlugin extends OptionHandler {

    /**
     * Represents the loot to gain.
     */
    private final Object[][] LOOTS = new Object[][]{ { new Item(Item.COINS, 20), 50 }, { new Item(Item.COINS, 40), 25 }, { new Item(1623), 13 }, { new Item(1621), 6 }, { new Item(1619), 5 }, { new Item(1617), 1 } };

    /**
     * Represents the stethoscope item.
     */
    @SuppressWarnings("unused")
    private final Item SETHOSCOPE = new Item(5560);

    /**
     * Represents the required level to crack a safe.
     */
    private static final int level = 50;

    /**
     * Represents the experience gained.
     */
    private static final double experience = 70;

    /**
     * Represents the animations to use.
     */
    private static final Animation[] animations = new Animation[]{ new Animation(2247), new Animation(2248), new Animation(1113), new Animation(2244) };

    /**
     * Represents the cracked safe.
     */
    private static final int CRACKED_SAFE = 7238;

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(7236).getConfigurations().put("option:crack", this);//wall safe.
        ObjectDefinition.forId(7227).getConfigurations().put("option:disarm", this);//trap
        ObjectDefinition.forId(7256).getConfigurations().put("option:open", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, final Node node, String option) {
        switch (option) {
            case "open":
                player.getDialogueInterpreter().sendDialogues(Repository.findNPC(2266), null, "And where do you think you're going? A little too eager", "I think. Come and talk to me before you go wandering", "around in there.");
                break;
            case "crack":
                if (player.getSkills().getLevel(Skills.THIEVING) < 50) {
                    player.getActionSender().sendMessage("You need to be level " + level + " thief to crack this safe.");
                    return true;
                }
                if (player.getInventory().freeSlots() == 0) {
                    player.getActionSender().sendMessage("Not enough inventory space.");
                    return true;
                }
                final boolean success = success(player, Skills.THIEVING);
                player.lock(4);
                player.getActionSender().sendMessage("You start cracking the safe.");
                player.animate(animations[success ? 1 : 0]);
                World.submit(new Pulse(3, player) {

                    @Override
                    public boolean pulse() {
                        if (success) {
                            ObjectBuilder.replace(((GameObject) node), ((GameObject) node).transform(CRACKED_SAFE), 1);
                            player.getActionSender().sendMessage("You get some loot.");
                            player.getSkills().addExperience(Skills.THIEVING, experience);
                            int index = RandomUtil.random(LOOTS.length);
                            final Item item = ((Item) LOOTS[index][0]);
                            player.getInventory().add(item);
                            return true;
                        }
                        final boolean trapped = RandomUtil.random(3) == 1;
                        if (trapped) {
                            player.getActionSender().sendMessage("You slip and trigger a trap!");
                            player.animate(animations[2]);
                            player.getImpactHandler().manualHit(player, RandomUtil.random(2, 6), HitsplatType.NORMAL);
                            World.submit(new Pulse(1) {

                                @Override
                                public boolean pulse() {
                                    player.animate(new Animation(-1, Priority.HIGH));
                                    return true;
                                }
                            });
                        }
                        return true;
                    }
                });
                break;
            case "search":
                player.animate(animations[3]);
                player.getActionSender().sendMessage("You temporarily disarm the trap!");
                break;
        }
        return true;
    }

    /**
     * Method used to determine the success of a player when thieving.
     * @param player the player.
     * @return <code>True</code> if succesfull, <code>False</code> if not.
     */
    public final boolean success(final Player player, final int skill) {
        return ((RandomUtil.getRandom(3) * player.getSkills().getLevel(skill)) / 3) > (50 / 2);
    }
}
