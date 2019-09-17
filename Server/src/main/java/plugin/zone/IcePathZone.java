package plugin.zone;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.map.zone.ZoneBuilder;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * The God Wars entrance zone monitor.
 *
 * @author Emperor
 */
public final class IcePathZone extends MapZone implements Plugin<Object> {

    /**
     * Constructs a new {@code IcePathZone} {@code Object}.
     */
    public IcePathZone() {
        super("Ice path zone", true);
    }

    @Override
    public void configure() {
        ZoneBorders border = new ZoneBorders(2821, 3712, 2940, 3839);
        border.addException(new ZoneBorders(2821, 3712, 2838, 3745));
        register(border);
    }

    @Override
    public boolean enter(Entity e) {
        if (e instanceof Player) {
            final Player player = (Player) e;
            player.getInterfaceState().openOverlay(new Component(11877)); //TODO: find the real one
            Pulse pulse = new Pulse(10, player) {

                @Override
                public boolean pulse() {
                    if (player.getLocks().isMovementLocked()) {
                        return false;
                    }
                    player.getSettings().updateRunEnergy(100);
                    player.getImpactHandler().manualHit(player, 1, HitsplatType.NORMAL);
                    int skill = RandomUtil.randomize(7);
                    if (skill == 3 || skill == 5) {
                        skill++;
                    }
                    player.getSkills().updateLevel(skill, -1, 0);
                    return false;
                }
            };
            player.setAttribute("ice_path_pulse", pulse);
            World.submit(pulse);
        }
        return true;
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        if (e instanceof Player) {
            ((Player) e).getInterfaceState().closeOverlay();
            ((Player) e).getInterfaceState().close();// TODO This isn't closing!
            Pulse pulse = e.getAttribute("ice_path_pulse");
            if (pulse != null) {
                pulse.stop();
                e.removeAttribute("ice_path_pulse");
            }
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
