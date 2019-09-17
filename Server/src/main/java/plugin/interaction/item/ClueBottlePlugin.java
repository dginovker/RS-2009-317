package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.global.ClueBottle;
import org.gielinor.game.content.global.distraction.treasuretrail.ClueScrollPlugin;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

import java.util.Objects;

/**
 * Created by Stan van der Bend on 16/01/2018.
 *
 * todo: look into the treasure trail management (to prevent duplicate clues?).
 *
 * project: Gielinor-Server
 * package: plugin.interaction.item
 */
public class ClueBottlePlugin extends OptionHandler {
    @Override
    public boolean handle(Player player, Node node, String option) {

        if(player.getTreasureTrailManager().hasTrail()) {
            player.getActionSender().sendMessage("You already have an active treasure trail!");
            return true;
        }

        final Item clueBottleItem = node.asItem();
        final ClueBottle clueBottle = ClueBottle.forItemWith(clueBottleItem.getId());

        if(Objects.nonNull(clueBottle))
            player.getInventory().replace(ClueScrollPlugin.getClue(clueBottle.getClueLevel()), clueBottleItem.getSlot());

        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {

        for(ClueBottle clueBottle : ClueBottle.values())
            ItemDefinition.forId(clueBottle.getClueBottleID()).getConfigurations().put("option:open", this);

        return this;
    }
}
