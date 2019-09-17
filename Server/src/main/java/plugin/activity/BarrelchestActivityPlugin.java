package plugin.activity;

import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.build.DynamicRegion;
import org.gielinor.game.world.map.zone.ZoneRestriction;

import plugin.npc.BarrelchestNPC;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class BarrelchestActivityPlugin extends ActivityPlugin {

    /**
     * Constructs a new {@code BarrelchestActivityPlugin} {@code Object}.
     */
    public BarrelchestActivityPlugin() {
        this(null);
    }

    /**
     * Constructs a new {@code BarrelchestActivityPlugin} {@code Object}.
     *
     * @param player the player.
     */
    public BarrelchestActivityPlugin(final Player player) {
        super("barrelchest", true, false, false, ZoneRestriction.FIRES, ZoneRestriction.RANDOM_EVENTS);
        this.player = player;
    }

    @Override
    public ActivityPlugin newInstance(Player p) {
        return new BarrelchestActivityPlugin(p);
    }

    @Override
    public boolean enter(final Entity e) {
        e.saveAttribute("barrelchest", true);
        return super.enter(e);
    }

    @Override
    public boolean start(final Player player, boolean login, Object... args) {
        DynamicRegion dynamicRegion = DynamicRegion.create(11151);
        RegionManager.getRegionCache().put(dynamicRegion.getId(), dynamicRegion);
        Location finalLocation = dynamicRegion.getBaseLocation().transform(10, 33, 0);
        player.getProperties().setTeleportLocation(finalLocation);
        NPC npc1 = new BarrelchestNPC(5666, finalLocation.transform(4, 8, 0));
        npc1.init();
        return super.start(player, login, args);
    }

    @Override
    public boolean leave(final Entity entity, boolean logout) {
        if (!(entity instanceof Player)) {
            return super.leave(entity, logout);
        }
        if (!logout) {
            entity.removeAttribute("barrelchest");
        }
        return super.leave(entity, logout);
    }

    @Override
    public boolean teleport(final Entity entity, int type, Node node) {
        leave(entity, false);
        return true;
    }

    @Override
    public boolean interact(final Entity e, Node target, Option option) {
        return false;
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public void configure() {
        region = DynamicRegion.create(11151);
        setRegionBase();
        registerRegion(region.getId());
    }

    @Override
    public void register() {
    }

}
