package plugin.activity.pestcontrol;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.zone.MapZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Pest control island map zone extension.
 *
 * @author Emperor
 */
public final class PCIslandZone extends MapZone {

    private static final Logger log = LoggerFactory.getLogger(PCIslandZone.class);

    /**
     * Constructs a new {@code PCIslandZone} {@code Object}.
     */
    public PCIslandZone() {
        super("pest control island", true);
    }

    @Override
    public boolean death(Entity e, Entity killer) {
        if (e instanceof Player) { //Ensure players can't die on the island.
            log.info("[{}] died in Pest Control, but avoided unsafe death.", e.getName());
            e.getProperties().setTeleportLocation(e.getLocation());
            return true;
        }
        return false;
    }

    @Override
    public void configure() {
        registerRegion(10537);
    }

}