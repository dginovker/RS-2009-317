package plugin.zone;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.map.zone.ZoneBuilder;
import org.gielinor.game.world.map.zone.ZoneRestriction;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the {@link org.gielinor.game.world.map.zone.MapZone} for the Dorgesh-Kaan mine.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DorgeshKaanMineZone extends MapZone implements Plugin<Object> {

    /**
     * Constructs a new <code>DorgeshKaanMineZone</code>.
     */
    public DorgeshKaanMineZone() {
        super("dorgesh kaan mine", true, ZoneRestriction.CANNON);
    }

    @Override
    public void configure() {
        super.register(new ZoneBorders(3306, 9610, 3317, 9616));
    }

    @Override
    public boolean enter(Entity e) {
        if (e instanceof Player) {
            Player player = (Player) e;
            if (player.getSavedData().getGlobalData().hasWalkedToDorgeshKaan()) {
                return true;
            }
            NPC npc = Repository.getClosestNpc(2086, player.getLocation());
            if (npc == null) {
                player.getDialogueInterpreter().open(2086);
                return true;
            }
            if (npc.getInteraction().get(0) == null || npc.getInteraction().get(0).getHandler() == null) {
                player.getDialogueInterpreter().open(2086);
                return true;
            }
            player.getWalkingQueue().reset();
            player.getPulseManager().clear();
            npc.getInteraction().handle(player, npc.getInteraction().get(0));
            return true;
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