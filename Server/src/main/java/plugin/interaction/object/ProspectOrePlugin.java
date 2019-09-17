package plugin.interaction.object;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.free.gather.SkillingResource;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the prospecting of an ore.
 *
 * @author 'Vexia
 */
public class ProspectOrePlugin extends OptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ProspectOrePlugin.class);

    @Override
    public boolean handle(final Player player, Node node, String option) {
        final GameObject object = (GameObject) node;
        final SkillingResource rock = SkillingResource.forId(object.getId());
        if (rock == null) {
            player.getActionSender().sendMessage("There is no ore currently available in this rock.");
            return true;
        }
        if (!object.getDefinition().hasAction("Prospect")) {
            log.warn("[{}] attempted to prospect an undefined rock: [{}] at [{}].",
                player.getName(), node.getId(), node.getLocation());
            player.getActionSender().sendMessage("Invalid rock, please report on forums!");
            return true;
        }
        player.getActionSender().sendMessage("You examine the rock for ores...");
        player.getPulseManager().run(new Pulse(3, player, object) {

            @Override
            public boolean pulse() {
                String type = ItemDefinition.forId(rock.getReward()).getName().toLowerCase();
                if (rock.getName().startsWith("gem")) {
                    type = "gems";
                }
                player.getActionSender().sendMessage("This rock contains " + type + ".");
                return true;
            }
        });
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.setOptionHandler("prospect", this);
        return this;
    }

}
