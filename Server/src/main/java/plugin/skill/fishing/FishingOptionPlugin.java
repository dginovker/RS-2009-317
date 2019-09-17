package plugin.skill.fishing;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.skill.free.fishing.FishingOption;
import org.gielinor.game.content.skill.free.fishing.FishingPulse;
import org.gielinor.game.content.skill.free.fishing.FishingSpot;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used to start fishing.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FishingOptionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.setOptionHandler("net", this);
        NPCDefinition.setOptionHandler("bait", this);
        NPCDefinition.setOptionHandler("lure", this);
        NPCDefinition.setOptionHandler("cage", this);
        NPCDefinition.setOptionHandler("harpoon", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        NPC npc = (NPC) node;
        FishingSpot spot = FishingSpot.forId(npc.getId());
        if (spot == null) {
            return false;
        }
        FishingOption opt = null;
        for (FishingOption o : spot.getOptions()) {
            if (o.getName().equals(option)) {
                opt = o;
                break;
            }
        }
        if (opt == null) {
            return false;
        }
        player.getPulseManager().run(new FishingPulse(player, npc, opt));
        return true;
    }

}
