package plugin.activity.clanwars;

import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.map.zone.ZoneBuilder;
import org.gielinor.game.world.map.zone.ZoneRestriction;
import org.gielinor.game.world.map.zone.impl.WildernessZone;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;


/**
 * Represents the Clan Wars challenge zone.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ClanWarsChallengeZone extends MapZone implements Plugin<Object> {

    /**
     * Constructs a new <code>ClanWarsChallengeZone</code>.
     */


    public ClanWarsChallengeZone(ZoneBorders... borders) {
        super("clan wars cz", true, ZoneRestriction.CANNON, ZoneRestriction.RANDOM_EVENTS);
    }


    @Override
    public void configure() {
        register(new ZoneBorders(3352, 3143, 3391, 3176));
        // TEMP
        //register(ZoneBorders.forRegion(11343));
        //register(new ZoneBorders(3264, 3672, 3279, 3695));
    }

    @Override
    public boolean enter(Entity e) {
        if (e instanceof Player) {
            Player p = (Player) e;
            p.getSkullManager().setWildernessDisabled(true);
            p.getSkullManager().setWilderness(false);
            p.getInterfaceState().closeOverlay();
            p.getInteraction().remove(Option._P_ASSIST);
            p.getInteraction().set(ClanWarsChallengeOption.OPTION);
        }
        return super.enter(e);
    }

    @Override
    public boolean leave(final Entity e, final boolean logout) {
        if (e instanceof Player) {
            Player p = (Player) e;
            p.getSkullManager().setWildernessDisabled(false);
            p.getInteraction().remove(ClanWarsChallengeOption.OPTION);
            p.getInteraction().set(Option._P_ASSIST);
            if (WildernessZone.Companion.isInZone(e)) {
                WildernessZone.Companion.show(p);
                p.getSkullManager().setWilderness(true);
            }
        }
        return super.leave(e, logout);
    }

    @Override
    public boolean interact(Entity e, Node target, Option option) {
        if (target instanceof GameObject) {
            GameObject object = (GameObject) target;
            Player player = (Player) e;
            if (object.getId() == 26644) {
                if (player.getCommunication().getClan() == null) {
                    player.getActionSender().sendMessage("You have to be in a clan to enter this portal.");
                } else if (player.getCommunication().getClan().getClanWar() == null) {
                    player.getActionSender().sendMessage("Your clan has to be in a war.");
                } else {
                    player.getCommunication().getClan().getClanWar().fireEvent("join", player);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ZoneBuilder.configure(this);
        PluginManager.definePlugin(new ClanWarsChallengeOption());
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

}
