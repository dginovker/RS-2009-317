package plugin.cutscene;

import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.content.activity.CutscenePlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.build.DynamicRegion;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.CameraContext;
import org.gielinor.net.packet.context.CameraContext.CameraType;
import org.gielinor.net.packet.out.CameraViewPacket;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the organ cutscene.
 * @author 'Vexia
 */
public final class OrganCutScene extends CutscenePlugin {

    /**
     * Constructs a new {@code OrganCutScene} {@code Object}.
     */
    public OrganCutScene() {
        super("organ cutscene");
    }

    /**
     * Constructs a new {@code OrganCutScene} {@code Object}.
     * @param player the player.
     */
    public OrganCutScene(final Player player) {
        super("organ cutscene");
        this.player = player;
    }

    @Override
    public void open() {
        final GameObject organ = RegionManager.getObject(base.transform(43, 4, 0));
        final GameObject newOrgan = new GameObject(3500, base.transform(42, 4, 0));
        ObjectBuilder.remove(organ);
        ObjectBuilder.add(newOrgan);
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 2, player.getLocation().getY() - 7, 405, 1, 100));
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() + 1, player.getLocation().getY(), 405, 1, 100));
        player.lock();
        World.submit(new Pulse(3) {

            @Override
            public boolean pulse() {
                player.getActionSender().sendObjectAnimation(RegionManager.getObject(base.transform(42, 4, 0)), new Animation(9841));
                PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + 2, player.getLocation().getY() - 3, 400, 1, 1));
                PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() + 1, player.getLocation().getY(), 400, 1, 1));
                return true;
            }
        });
        World.submit(new Pulse(30) {

            @Override
            public boolean pulse() {
                unpause();
                return true;
            }
        });
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return new OrganCutScene(p);
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public Location getStartLocation() {
        return base.transform(42, 4, 0);
    }

    @Override
    public void configure() {
        region = DynamicRegion.create(12850);
        setRegionBase();
        registerRegion(region.getId());
    }

}
