package plugin.npc.osrs.zulrah;

import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.content.activity.CutscenePlugin;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.CameraContext;
import org.gielinor.net.packet.out.CameraViewPacket;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles the cutscene for Zulrah.
 *
 * @author Vexia
 */
public class ZulrahCutscene extends CutscenePlugin {

    private Location startLocation;

    /**
     * Constructs a new @{Code ZulrahCutscene} object.
     *
     * @param p The player.
     */
    public ZulrahCutscene(Player p) {
        super("zulrah cutscene");
        this.player = p;
    }

    /**
     * Constructs a new @{Code ZulrahCutscene} object.
     */
    public ZulrahCutscene() {
        this(null);
    }

    @Override
    public void open() {
        super.open();
        player.getDialogueInterpreter().close();
        player.getInterfaceState().closeChatbox();
        int x = player.getLocation().getX() - 10;
        int y = player.getLocation().getY() - 4;
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.POSITION, x, y, 700, 1, 100));
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.ROTATION, x - 3, y - 3, 700, 1, 100));
        player.unlock();
        World.submit(new Pulse(6, player) {

            @Override
            public boolean pulse() {
                PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.RESET, player.getLocation().getX(), player.getLocation().getY(), 630, 1, 100));
                player.getInterfaceState().openDefaultTabs();
                //ZulrahCutscene.this.stop(false);
                return true;
            }
        });
    }

    @Override
    public boolean leave(final Entity e, boolean logout) {
        return false;
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return new ZulrahCutscene(p);
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public Location getStartLocation() {
        return startLocation;
    }

    @Override
    public boolean start(Player player, boolean login, Object... args) {
        startLocation = (Location) args[0];
        return super.start(player, login, args);
    }

    @Override
    public void configure() {
        player.getDialogueInterpreter().sendPlaneMessage(true, "The priestess rows you to Zulrah's shrine.", "then hurridlly paddles away.");
    }

    @Override
    public int getMapState() {
        return 0;
    }

}
