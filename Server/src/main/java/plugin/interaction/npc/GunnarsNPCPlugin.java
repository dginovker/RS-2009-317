package plugin.interaction.npc;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the {@link org.gielinor.game.interaction.OptionHandler} for the Gunnars NPCs.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GunnarsNPCPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.forId(5481).getConfigurations().put("option:ferry-jatizso", this);
        NPCDefinition.forId(5482).getConfigurations().put("option:ferry-rellekka", this);
        NPCDefinition.forId(5508).getConfigurations().put("option:ferry-neitiznot", this);
        NPCDefinition.forId(5507).getConfigurations().put("option:ferry-rellekka", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        switch (node.getId()) {
            case 5507:
            case 5482:
                travel(player, new Location(2650, 3709, 0), "You board the ship to travel back to Rellekka...", "The ship arrives in Rellekka.");
                break;
            case 5481:
                travel(player, new Location(2421, 3781, 0), "You board the ship to travel to Jatizso...", "The ship arrives in Jatizso.");
                break;
            case 5508:
                travel(player, new Location(2311, 3785, 0), "You board the ship to travel to Neitiznot...", "The ship arrives in Neitiznot.");
                break;
        }
        return true;
    }


    public void travel(Player player, Location location, String startMessage, String message) {
        player.lock(5);
        player.getActionSender().sendMessage(startMessage);
        World.submit(new Pulse(1) {

            int count = 0;

            @Override
            public boolean pulse() {
                switch (count++) {
                    case 0:
                        player.getInterfaceState().openComponent(8677);
                        break;
                    case 2:
                        player.getActionSender().sendMinimapState(2);
                        break;
                    case 3:
                        player.setTeleportTarget(location);
                        break;
                    case 4:
                        player.getInterfaceState().close();
                        player.getInterfaceState().openDefaultTabs();
                        player.getActionSender().sendMinimapState(0);
                        player.getActionSender().sendMessage(message);
                        return true;
                }
                return false;
            }

        });
    }
}
