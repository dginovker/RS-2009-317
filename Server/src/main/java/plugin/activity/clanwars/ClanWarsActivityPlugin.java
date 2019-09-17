package plugin.activity.clanwars;

import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneRestriction;

/**
 * Represents the Clan Wars {@link org.gielinor.game.content.activity.ActivityPlugin}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ClanWarsActivityPlugin extends ActivityPlugin {

    /**
     * Constructs a new <code>ClanWarsActivityPlugin</code>.
     */
    public ClanWarsActivityPlugin() {
        super("Clan wars", true, true, true, ZoneRestriction.CANNON, ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.FIRES, ZoneRestriction.FOLLOWERS);
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return null;
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public void configure() {

    }
}
