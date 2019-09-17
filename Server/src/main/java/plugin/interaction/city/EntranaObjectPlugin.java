package plugin.interaction.city;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents a plugin used to handle entrana related objects.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class EntranaObjectPlugin extends OptionHandler {

    /**
     * Represents the location to teleport to.
     */
    private static final Location LOCATION = new Location(3234, 3769, 0);//magic door

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(2408).getConfigurations().put("option:climb-down", this);
        ObjectDefinition.forId(2407).getConfigurations().put("option:open", this);//magic door
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "climb-down":
                player.getDialogueInterpreter().open(656, Repository.findNPC(656));
                break;
            case "open":
                player.setTeleportTarget(LOCATION);
                break;
        }
        return true;
    }

}
