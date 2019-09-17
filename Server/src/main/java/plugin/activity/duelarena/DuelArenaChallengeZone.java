package plugin.activity.duelarena;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.RegionZone;
import org.gielinor.game.world.map.zone.ZoneBorders;

/**
 * Represents the zone where players can challenge each other to a duel.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class DuelArenaChallengeZone extends MapZone {

    /**
     * The challenge zone.
     */
    private static final DuelArenaChallengeZone INSTANCE = new DuelArenaChallengeZone(new ZoneBorders(3341, 3262, 3386, 3280));

    /**
     * The zone borders.
     */
    private ZoneBorders[] borders;

    /**
     * Constructs a new {@code DuelArenaChallengeZone} {@code Object}.
     */
    public DuelArenaChallengeZone(ZoneBorders... borders) {
        super("Duel Arena Challenge", true);
        this.borders = borders;
    }

    @Override
    public void configure() {
        for (ZoneBorders border : borders) {
            register(border);
        }
    }

    @Override
    public boolean enter(Entity entity) {
        if (entity instanceof Player) {
            Player p = (Player) entity;
            show(p);
        }
        return true;
    }

    @Override
    public boolean leave(Entity entity, boolean logout) {
        if (!logout && entity instanceof Player) {
            Player player = (Player) entity;
            Component overlay = player.getInterfaceState().getOverlay();
            if (overlay != null && overlay.getId() == 201) {
                player.getInterfaceState().closeOverlay();
            }
            //   player.getInteraction().remove(DuelArenaActivityPlugin.C);
        }
        return true;
    }

    /**
     * Method used to show being the wilderness.
     *
     * @param player The player.
     */
    public static void show(final Player player) {
        player.getInterfaceState().openOverlay(new Component(201));
        //  player.getInteraction().set(DuelArenaChallengeOption.OPTION);
    }

    @Override
    public boolean teleport(Entity entity, int type, Node node) {
        return super.teleport(entity, type, node);
    }

    @Override
    public void locationUpdate(Entity entity, Location last) {
        if (entity instanceof Player) {
            Player p = (Player) entity;
            Component overlay = p.getInterfaceState().getOverlay();
            if (overlay == null || overlay.getId() != 201) {
                show(p);
            }
        }
    }

    /**
     * Checks if the entity is inside the wilderness.
     *
     * @param entity The entity.
     * @return <code>True</code> if so.
     */
    public static boolean isInZone(Entity entity) {
        Location l = entity.getLocation();
        for (RegionZone zone : entity.getViewport().getRegion().getRegionZones()) {
            if (zone.getZone() == INSTANCE && zone.getBorders().insideBorder(l.getX(), l.getY())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the zone borders.
     *
     * @return The borders.
     */
    public ZoneBorders[] getBorders() {
        return borders;
    }

    /**
     * Gets the instance.
     *
     * @return The instance.
     */
    public static DuelArenaChallengeZone getInstance() {
        return INSTANCE;
    }

}
