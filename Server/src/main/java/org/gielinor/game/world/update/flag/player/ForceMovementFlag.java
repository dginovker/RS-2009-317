package org.gielinor.game.world.update.flag.player;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.impl.ForceMovement;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Handles the force movement player update flag.
 *
 * @author Emperor
 */
public final class ForceMovementFlag extends UpdateFlag<ForceMovement> {

    /**
     * Constructs a new {@code ForceMovementFlag} {@code Object}.
     *
     * @param forceMovement The force movement data.
     */
    public ForceMovementFlag(ForceMovement forceMovement) {
        super(forceMovement);
    }

    @Override
    public void write(PacketBuilder buffer) {
    }

    @Override
    public void writeDynamic(PacketBuilder packet, Entity e) {
        Location l = ((Player) e).getPlayerFlags().getLastSceneGraph();

        packet.putS(context.getStart().getSceneX(l)); //Start location
        packet.putS(context.getStart().getSceneY(l));
        packet.putS(context.getDestination().getSceneX(l)); //Destination location
        packet.putS(context.getDestination().getSceneY(l));
        packet.putLEShortA(context.getCommenceSpeed() * 30);//Commencing speed
        packet.putShortA((context.getCommenceSpeed() * 30) + (context.getPathSpeed() * 30 + 1)); //Path speed
        packet.putS(context.getDirection().toInteger());
    }

    @Override
    public int data() {
        return maskData();
    }

    @Override
    public int ordinal() {
        return 0;
    }

    /**
     * Gets the mask data.
     *
     * @return The mask data.
     */
    public static int maskData() {
        return 0x400;
    }

}
