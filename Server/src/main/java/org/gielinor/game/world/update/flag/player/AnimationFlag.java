package org.gielinor.game.world.update.flag.player;

import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Handles the animation update flag.
 *
 * @author Emperor
 */
public final class AnimationFlag extends UpdateFlag<Animation> {

    /**
     * Constructs a new {@code AnimationFlag} {@code Object}.
     *
     * @param context The {@link org.gielinor.game.world.update.flag.context.Animation} context.
     */
    public AnimationFlag(Animation context) {
        super(context);
    }

    @Override
    public void write(PacketBuilder packetBuilder) {
        packetBuilder.putLEShort(context.getId());
        packetBuilder.putC(context.getDelay());
    }

    @Override
    public int data() {
        return maskData();
    }

    @Override
    public int ordinal() {
        return 2;
    }

    /**
     * Gets the mask data of the animation update flag.
     *
     * @return The mask data.
     */
    public static int maskData() {
        return 0x8;
    }
}