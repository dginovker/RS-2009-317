package plugin.skill.construction;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.member.construction.BuildHotspot;
import org.gielinor.game.content.skill.member.construction.Decoration;
import org.gielinor.game.content.skill.member.construction.RoomBuilder;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The build option handling plugin.
 *
 * @author Emperor
 */
public final class BuildOptionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.setOptionHandler("build", this);
        ObjectDefinition.setOptionHandler("remove", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (!player.getHouseManager().isBuildingMode()) {
            player.getActionSender().sendMessage("You have to be in building mode to do this.");
            return true;
        }
        if (option.equals("remove")) {
            Decoration decoration = Decoration.getDecoration(player, node.getLocation());
            if (decoration == null) {
                return false;
            }
            player.getActionSender().sendMessage("Dec remove: " + decoration + ".");
            return true;
        }
        player.setAttribute("con:hsobject", node);
        if (node.asObject().getType() < 4) {
            player.getInterfaceState().open(new Component(402));
            return true;
        }
        BuildHotspot buildHotspot = BuildHotspot.forId(node.getId());
        if (buildHotspot == null) {
            return false;
        }
        player.setAttribute("con:hotspot", buildHotspot);
        RoomBuilder.openBuildInterface(player, buildHotspot);
        return true;
    }

}