package org.gielinor.game.content.global.travel.ship;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents a pulse used to travel a player to a location.
 * @author 'Vexia
 */
public final class ShipTravellPulse extends Pulse {

    /**
     * Represents the player instance.
     */
    private Player player;

    /**
     * Represents the ship we're using.
     */
    private Ships ship;

    /**
     * Represents the current counter.
     */
    private int counter = 0;

    /**
     * Constructs a new {@code ShipTravellPulse.java} {@code Object}.
     * @param player the <b>Player</b>.
     */
    public ShipTravellPulse(Player player, Ships ship) {
        super(1);
        this.player = player;
        this.ship = ship;
    }

    @Override
    public boolean pulse() {
        switch (counter++) {
            case 0:
                prepare();
                break;
            case 1:
                if (ship != Ships.PORT_SARIM_TO_CRANDOR) {
                    player.getProperties().setTeleportLocation(ship.getLocation());
                }
                break;
            default:
                if (counter == ship.getDelay()) {
                    arrive();
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Method used to arrive at a location.
     */
    private final void arrive() {
        player.unlock();
        player.getConfigManager().set(75, -1);
        player.getInterfaceState().close();
        PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
        if (!ship.getName().equals("Crandor")) {
            player.getDialogueInterpreter().sendPlaneMessage("The ship arrives at " + ship.getName() + ".");
            player.getInterfaceState().close();
        } else {
            player.getInterfaceState().open(new Component(317));
            PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
            player.getInterfaceState().openOverlay(new Component(544));
            player.getInterfaceState().open(new Component(317));
        }
    }

    /**
     * Method used to prepare the player.
     */
    private final void prepare() {
        player.lock(ship.getDelay() + 1);
        player.getInterfaceState().open(new Component(299));
        PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
        player.getConfigManager().set(75, ship.getConfig());
    }
}
