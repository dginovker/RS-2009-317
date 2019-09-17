package org.gielinor.game.content.global.travel.glider;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.CameraContext;
import org.gielinor.net.packet.context.CameraContext.CameraType;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.CameraViewPacket;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the pulse used for a glider.
 *
 * @author 'Vexia
 */
public final class GliderPulse extends Pulse {

    /**
     * Represents the player.
     */
    private final Player player;

    /**
     * Represents the glider.
     */
    private final Gliders glider;

    /**
     * Represents the count.
     */
    private int count;

    /**
     * Constructs a new {@code GliderPulse.java} {@Code Object}
     *
     * @param delay
     * @param player
     * @param glider
     */
    public GliderPulse(int delay, Player player, Gliders glider) {
        super(delay, player);
        this.player = player;
        this.glider = glider;
        player.lock();
    }

    @Override
    public boolean pulse() {
        final boolean crash = glider == Gliders.LEMANTO_ADRA;
        if (count == 1) {
            player.getConfigManager().set(153, glider.getConfig());
            PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
        } else if (count == 2 && crash) {
            PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.SHAKE, 4, 4, 1200, 4, 4));
            player.getActionSender().sendMessage("The glider almost gets blown from its path as it withstands heavy winds.");
        }
        if (count == 3) {
            player.getInterfaceState().openComponent(8677);
        } else if (count == 4) {
            player.unlock();
            player.getProperties().setTeleportLocation(glider.getLocation());
        } else if (count == 5) {
            if (crash) {
                player.getActionSender().sendMessage("The glider becomes uncontrollable and crashes down...");
                PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.RESET, 0, 0, 0, 0, 0));
            }
            player.getInterfaceState().close();
            PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
            player.getInterfaceState().close();
            player.getConfigManager().set(153, 0);
            return true;
        }
        count++;
        return false;
    }
}
