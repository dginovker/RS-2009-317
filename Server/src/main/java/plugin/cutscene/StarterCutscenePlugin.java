package plugin.cutscene;

import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.content.activity.CutscenePlugin;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.GameConstants;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.build.DynamicRegion;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.CameraContext;
import org.gielinor.net.packet.context.CameraContext.CameraType;
import org.gielinor.net.packet.out.CameraViewPacket;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the new player starter cutscene plugin.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class StarterCutscenePlugin extends CutscenePlugin {

    /**
     * The stage we're on.
     */
    private int stage = 0;
    /**
     * The offsets for the player whilst in the cutscene.
     * Position x, position y, rotation x, rotation y, height, speed
     * // 2, -4, 0, -4, 1000 - use for iron men
     * // -2, 5, 2, 5, 1000 - use for shops
     * // -2, 3, 2, 5, 1000 - use for altars
     * // 1 0 0 -4 1000 - near banks
     */
    private final int[][] OFFSETS = new int[][]{
        { 0, 0, 0, -4, 305, 35 },
        { 1, 0, 0, -4, 1000, 120 },
        { 2, -4, 0, -4, 1000, 35 },
        { -2, 5, 2, 5, 1000, 35 },
        { -2, 3, 2, 5, 1000, 35 },
        { 3, 0, 0, -4, 305, 35 }
    };

    /**
     * Constructs a new {@code StarterCutscenePlugin}.
     */
    public StarterCutscenePlugin() {
        super("starter cutscene");
    }

    /**
     * Constructs a new {@code StarterCutscenePlugin}.
     *
     * @param player the player.
     */
    public StarterCutscenePlugin(final Player player) {
        super("starter cutscene");
        this.player = player;
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return new StarterCutscenePlugin(p);
    }

    @Override
    public void open() {
        sendCamera();
        player.getDialogueInterpreter().sendPlaneMessage(true, "Welcome to " + Constants.SERVER_NAME + ".");
        player.setAttribute("in-cutscene", this);
        player.getPulseManager().run(new Pulse(2) {

            @Override
            public boolean pulse() {
                player.getDialogueInterpreter().open("StarterGuide");
                return true;
            }
        });
    }

    /**
     * Sets the camera stage.
     */
    public void set(int stage) {
        this.stage = stage;
        sendCamera();
    }

    /**
     * Sends the next camera stage.
     */
    public void next() {
        stage++;
        sendCamera();
    }

    /**
     * Sends the camera's position and rotation.
     */
    public void sendCamera() {

        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.POSITION, player.getLocation().getX() + OFFSETS[stage][0],
            player.getLocation().getY() + OFFSETS[stage][1], OFFSETS[stage][4], 1, OFFSETS[stage][5]));
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.ROTATION, player.getLocation().getX() + OFFSETS[stage][2],
            player.getLocation().getY() + OFFSETS[stage][3], OFFSETS[stage][4], 1, OFFSETS[stage][5]));
    }

    @Override
    public void fade() {
        player.unlock();
    }

    @Override
    public void end() {
        super.end();
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.RESET, 0, 0, 0, 0, 0));
        player.getInterfaceState().openDefaultTabs();
        player.setTeleportTarget(getSpawnLocation());
        player.removeAttribute("in-cutscene");
    }

    @Override
    public Location getSpawnLocation() {
        return GameConstants.START_LOCATION;
    }

    @Override
    public Location getStartLocation() {
        return GameConstants.START_LOCATION;
    }

    @Override
    public void configure() {
        region = DynamicRegion.create(11343);
        setRegionBase();
        registerRegion(region.getId());
    }

}
