package plugin.zone;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.ZoneBuilder;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents a snow zone for a Christmas event.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SnowZone extends MapZone implements Plugin<Object> {

    public SnowZone() {
        super("Snow zone", true);
    }

    @Override
    public void configure() {
        //register(ZoneBorders.forRegion(12850));
    }

    @Override
    public boolean enter(Entity e) {
        if (e instanceof Player) {
            final Player player = (Player) e;
            player.getInterfaceState().openOverlay(new Component(11877));
        }
        return true;
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        if (e instanceof Player) {
            ((Player) e).getInterfaceState().closeOverlay();
            ((Player) e).getInterfaceState().close();
        }
        return true;
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