package org.gielinor.game.world.update.flag.npc;

import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.net.packet.PacketBuilder;

/**
 * The NPC animation update flag.
 *
 * @author Emperor
 */
public final class NPCAnimation extends UpdateFlag<Animation> {

    /**
     * Constructs a new {@code NPCAnimation} {@code Object}.
     *
     * @param context The animation.
     */
    public NPCAnimation(Animation context) {
        super(context);
    }

    @Override
    public void write(PacketBuilder packetBuilder) {
        packetBuilder.putLEShort(context.getId());
        packetBuilder.put(context.getDelay());
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
        return 0x10;
    }

}
