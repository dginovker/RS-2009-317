package org.gielinor.game.world.update.flag.chunk;

import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.UpdateFlag;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Handles the location graphic update.
 *
 * @author Emperor
 */
public final class GraphicUpdateFlag extends UpdateFlag<Graphics> {

    /**
     * The location.
     */
    private final Location location;

    /**
     * Constructs a new {@code GraphicUpdateFlag} {@code Object}.
     *
     * @param graphic  The graphic.
     * @param location The location.
     */
    public GraphicUpdateFlag(Graphics graphic, Location location) {
        super(graphic);
        this.location = location;
    }

    @Override
    public void write(PacketBuilder packetBuilder) {
        packetBuilder.put((byte) 4);
        packetBuilder.put((location.getChunkOffsetX() << 4) | (location.getChunkOffsetY() & 0x7));
        packetBuilder.putShort(context.getId());
        packetBuilder.put(context.getHeight());
        packetBuilder.putShort(context.getDelay());
    }

    @Override
    public int data() {
        return 0;
    }

    @Override
    public int ordinal() {
        return 3;
    }

}