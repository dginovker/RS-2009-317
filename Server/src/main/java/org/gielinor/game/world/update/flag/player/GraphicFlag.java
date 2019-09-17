package org.gielinor.game.world.update.flag.player;


import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Handles the graphic update flag.
 *
 * @author Emperor
 */
public final class GraphicFlag extends UpdateFlag<Graphics> {

    /**
     * Constructs a new {@code GraphicFlag} {@code Object}.
     *
     * @param context The context.
     */
    public GraphicFlag(Graphics context) {
        super(context);
    }

    @Override
    public void write(PacketBuilder packet) {
        packet.putLEShort(context.getId());
        packet.putInt(context.getDelay() | (context.getHeight() << 16));
    }

    @Override
    public int data() {
        return maskData();
    }

    @Override
    public int ordinal() {
        return 1;
    }

    /**
     * Gets the mask data of the graphic update flag.
     *
     * @return The mask data.
     */
    public static int maskData() {
        return 0x100;
    }
}
