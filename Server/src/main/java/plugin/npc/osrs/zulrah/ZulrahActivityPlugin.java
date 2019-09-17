package plugin.npc.osrs.zulrah;

import org.gielinor.game.content.activity.ActivityManager;
import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.build.DynamicRegion;

public class ZulrahActivityPlugin extends ActivityPlugin {

    /**
     * The zulrah npc.
     */
    private ZulrahNPC zulrah;

    public ZulrahActivityPlugin() {
        super("zulrah", true, true, false);
    }

    @Override
    public void configure() {
        region = DynamicRegion.create(9007, 9008);
        setMulticombat(true);
        setRegionBase();
        //registerRegion(region.getId());
        register(region.getBorders());
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return new ZulrahActivityPlugin();
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public boolean teleport(final Entity entity, int type, Node node) {
        leave(entity, false);
        return true;
    }

    @Override
    public boolean start(Player player, boolean login, Object... args) {
        boolean cutscene = (boolean) args[0];
        if (cutscene) {
            ActivityManager.start(player, "zulrah cutscene", false, base.transform(28, 60, 0));
        } else {
            player.teleport(base.transform(28, 60, 0));
        }

        zulrah = new ZulrahNPC(22043, base.transform(26, 64, 0), ZulrahPattern.getPattern());
        zulrah.setBase(base);
        zulrah.setDirection(Direction.SOUTH);
        zulrah.setWalks(false);
        zulrah.setRespawn(false);
        zulrah.setPlayer(player);
        zulrah.init();

        player.face(zulrah);
        return super.start(player, login, args);
    }

    @Override
    public boolean enter(Entity e) {
        e.setAttribute("zulrah_activity", true);
        return super.enter(e);
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        e.removeAttribute("zulrah_activity");
        if (e instanceof Player) {
            if (zulrah.isActive()) {
                zulrah.killSnakes();
                zulrah.clear();
            }
            unregisterRegion(region.getRegionId());
        }
        return super.leave(e, logout);
    }
}
