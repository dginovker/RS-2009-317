package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.ClimbActionHandler;
import org.gielinor.game.content.skill.free.magic.TeleportLocation;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.zone.impl.ModeratorZone;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RunScript;

/**
 * Represents the plugin used for the moderator objects in the p-mod room.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class ModeratorObject extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(26806).getConfigurations().put("option:climb-up", this);
        ObjectDefinition.forId(26807).getConfigurations().put("option:j-mod options", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "climb-up":
                ClimbActionHandler.climb(player, new Animation(828), TeleportLocation.LUMBRIDGE.getLocation());
                break;
            case "j-mod options":
                if (!player.getDetails().getRights().isAdministrator()) {
                    return true;
                }
                player.getDialogueInterpreter().sendInput(true, "Toggle Room:(on/off)");
                player.setAttribute("runscript", new RunScript() {

                    @Override
                    public boolean handle() {
                        final boolean toggle = getValue().equals("on");
                        ModeratorZone.toggle(player, toggle);
                        return false;
                    }

                });
                break;
        }
        return true;
    }

}
