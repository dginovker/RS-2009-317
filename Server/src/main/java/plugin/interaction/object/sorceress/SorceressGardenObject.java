package plugin.interaction.object.sorceress;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Hanldes the sorceress garden gates.
 *
 * @author 'Vexia
 *
 */
public class SorceressGardenObject extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        GardenObjectsPlugin.SeasonDefinitions def = GardenObjectsPlugin.SeasonDefinitions.forGateId(((GameObject) node).getId());
        if (def != null) {
            if (player.getSkills().getStaticLevel(Skills.THIEVING) < def.getLevel()) {
                player.getDialogueInterpreter().sendItemMessage(10692, "You need Thieving level of " + def.getLevel() + " to pick the lock of this gate.");
                return true;
            }
            DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
        }
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(21709).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(21753).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(21731).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(21687).getConfigurations().put("option:open", this);
        return this;
    }

}
