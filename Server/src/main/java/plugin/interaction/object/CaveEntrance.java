package plugin.interaction.object;


import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.global.action.ClimbActionHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin for the Mos Le'Harmless cave entrance / exit.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class CaveEntrance extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(12355).getConfigurations().put("option:enter", this);
        ObjectDefinition.forId(3650).getConfigurations().put("option:enter", this);
        ObjectDefinition.forId(6702).getConfigurations().put("option:exit", this);
        ObjectDefinition.forId(5553).getConfigurations().put("option:exit", this);
        return this;
    }


    @Override
    public boolean handle(final Player player, Node node, final String option) {
        if (node.getId() == 15767) {
            player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("CaveEntrance"));
        } else {
            player.setTeleportTarget(new Location(3748, 9373, 0));
            player.getInterfaceState().close();
        }
        return true;
    }

    @Override
    public Location getDestination(Node n, Node object) {
        return ClimbActionHandler.getDestination((GameObject) object);
    }
}