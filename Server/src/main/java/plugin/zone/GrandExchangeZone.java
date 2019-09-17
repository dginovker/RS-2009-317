package plugin.zone;

import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.map.zone.ZoneBuilder;
import org.gielinor.game.world.map.zone.ZoneRestriction;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The grand exchange map zone.
 *
 * @author Emperor
 */
public final class GrandExchangeZone extends MapZone implements Plugin<Object> {

    /**
     * Constructs a new {@code GrandExchangeZone} {@code Object}.
     */
    public GrandExchangeZone() {
        super("grand exchange", true, ZoneRestriction.FOLLOWERS);
    }

    @Override
    public void configure() {
        super.register(new ZoneBorders(1, 2, 3, 4));
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ZoneBuilder.configure(this);
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

}