package plugin.interaction.item.withitem;

import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.global.consumable.Consumables;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.fishing.Fish;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the cutting of leaping fishes.
 *
 * references:
 *  - http://oldschoolrunescape.wikia.com/wiki/Leaping_trout
 *  - http://oldschoolrunescape.wikia.com/wiki/Leaping_salmon
 *  - http://oldschoolrunescape.wikia.com/wiki/Leaping_sturgeon
 *  - http://oldschoolrunescape.wikia.com/wiki/Fish_offcuts
 *  - http://oldschoolrunescape.wikia.com/wiki/Roe
 *
 * TODO: find correct animation
 * TODO: find the correct message
 * TODO: find the correct calculation for adding fish offcuts
 *
 * Created by Stan van der Bend on 06/02/2018.
 *
 * project: Gielinor-Server
 * package: plugin.interaction.item.withitem
 */
public class LeapingFishCuttingPlugin extends UseWithHandler {

    private static final Animation ANIMATION = new Animation(1);
    private static final Fish[] LEAPING_FISH = new Fish[]{Fish.LEAPING_TROUT, Fish.LEAPING_SALMON, Fish.LEAPING_STRUGEON};

    /**
     * Constructs a new {@code LeapingFishPlugin} {@code Object}.
     */
    public LeapingFishCuttingPlugin() {
        super(946);
    }

    @Override public Plugin<Object> newInstance(Object arg) throws Throwable {
        for(Fish fish : LEAPING_FISH)
            addHandler(fish.getItem().getId(), ITEM_TYPE, this);
        return this;
    }

    @Override public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        final Item leapingFish = event.getBaseItem();

        if (player.getInventory().remove(leapingFish)) {
            player.animate(ANIMATION);
            player.getInventory().add(Consumables.ROE.getConsumable().getItem());
            if( RandomUtil.getRandom(100 - player.getSkills().getStaticLevel(Skills.COOKING)) <= 1)
                player.getInventory().add(11334); // fish offcuts
            player.getActionSender().sendMessage("You cut the fish.");
        }
        return true;
    }

}
