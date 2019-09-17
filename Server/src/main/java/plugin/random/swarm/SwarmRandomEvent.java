package plugin.random.swarm;

import java.nio.ByteBuffer;

import org.gielinor.game.content.anticheat.AntiMacroEvent;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.PluginManager;

/**
 * The Swarm random event.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class SwarmRandomEvent extends AntiMacroEvent {

    /**
     * Constructs a new {@code SwarmRandomEvent} {@code Object}.
     */
    public SwarmRandomEvent() {
        super("Swarm", true, false);
    }

    @Override
    public boolean start(Player player, boolean login, Object... args) {
        super.init(player);
        Location location = Location.getRandomLocation(player.getLocation(), 2, true);
        SwarmNPC npc = new SwarmNPC(411, location);
        npc.player = player;
        npc.event = this;
        npc.init();
        if (location == player.getLocation()) {
            npc.moveStep();
        }
        return true;
    }

    @Override
    public void terminate() {
        super.terminate();
    }

    @Override
    public AntiMacroEvent create(Player player) {
        SwarmRandomEvent event = new SwarmRandomEvent();
        event.player = player;
        return event;
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public void configure() {
        PluginManager.definePlugin(new SwarmNPC());
    }

    @Override
    public void save(ByteBuffer buffer) {

    }

    @Override
    public void parse(ByteBuffer buffer) {

    }

}
