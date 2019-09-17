package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.donor.DonorStatus;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Erik
 */
@SuppressWarnings("unused") // Accessed by reflection
public class CraftingGuildObjects extends OptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CraftingGuildObjects.class);

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (node.getId() == 14886) {
            if (canUseChest(player)) {
                if ("use".equals(option))
                    player.getBank().open();
                else if ("collect".equals(option))
                    player.getGrandExchange().openCollectionBox();
                else {
                    player.getActionSender().sendMessage("Nothing interesting happens.");
                    log.warn("[{}] selected an unhandled option [{}] at [{} - {}].",
                        player.getName(), option, node.getName(), node.getId());
                }
            } else {
                player.getActionSender().sendMessage("A Crafting level of 99 is required to bank at this chest.");
                player.getActionSender().sendMessage("Alternatively, <col=27820d>Emerald membership</col> allows you to bypass that requirement.");
            }
            return true;
        } else if (node.getId() == 9534) {
            player.getActionSender().sendMessage("You search the crate...");
            player.animate(Animation.create(827));
            player.lock(3);
            World.submit(new Pulse(2) {

                @Override
                public boolean pulse() {
                    Item result = null;
                    int roll = RandomUtil.random(128);
                    String article = "a";
                    if (roll < 2) {
                        roll = RandomUtil.random(16);
                        if (roll < 1) result = Item.of(1275); // Rune pickaxe
                        else if (roll < 5) result = Item.of(1271); // Adamant pickaxe
                        else result = Item.of(1733); // Needle - see below!
                    } else if (roll < 7) {
                        article = "an";
                        result = Item.of(1267); // Iron pickaxe
                    } else if (roll < 24) result = Item.of(1265); // Bronze pickaxe
                    else if (roll < 27) {
                        article = "some";
                        result = Item.of(1741); // Leather
                    } else if (roll < 28) {
                        article = "some";
                        result = Item.of(1743); // Hard leather
                    } else if (roll < 32) {
                        article = "some";
                        result = Item.of(1734, RandomUtil.random(1, 5)); // Thread
                    } else if (roll < 34) result = Item.of(2357); // Gold bar
                    else if (roll < 42) {
                        article = "some";
                        result = Item.of(434); // Clay
                    }

                    if (result == null || !player.getInventory().hasRoomFor(result))
                        player.getActionSender().sendMessage("... but you find nothing interesting.");
                    else if (result.getId() == 1733) {
                        player.sendChat("Ow! That stings!");
                        player.getImpactHandler().manualHit(player, 1, ImpactHandler.HitsplatType.NORMAL);
                        player.getActionSender().sendMessage("You find a needle. What careless person put that there?");
                    } else {
                        player.getInventory().add(result);
                        player.getActionSender().sendMessage("You find " + article + " " + result.getName().toLowerCase() + ".");
                    }
                    return true;
                }
            });
            return true;
        }
        return false;
    }

    private boolean canUseChest(Player player) {
        if (player.getRights().isAdministrator() || player.getRights().isDeveloper())
            return true;
        if (player.getDonorManager().getDonorStatus().getId() >= DonorStatus.EMERALD_MEMBER.getId())
            return true;
        return player.getSkills().getStaticLevel(Skills.CRAFTING) >= 99;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(14886).getConfigurations().put("option:use", this);
        ObjectDefinition.forId(14886).getConfigurations().put("option:collect", this);
        ObjectDefinition.forId(9534).getConfigurations().put("option:search", this);
        return this;
    }

}
