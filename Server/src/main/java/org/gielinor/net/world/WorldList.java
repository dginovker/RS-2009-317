package org.gielinor.net.world;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.world.World;
import org.gielinor.net.IoSession;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Represents the game world list.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class WorldList {

    /**
     * The USA country code flag.
     */
    public static final int COUNTRY_USA = 0;
    /**
     * The free-to-play world flag.
     */
    public static final int FLAG_NON_MEMBERS = 0;

    /**
     * The pay-to-play world flag.
     */
    public static final int FLAG_MEMBERS = 1;

    /**
     * The PVP world flag.
     */
    public static final int FLAG_PVP = 2;

    /**
     * The beta world flag.
     */
    public static final int FLAG_BETA = 4;

    /**
     * A {@link java.awt.List} of current world definitions.
     */
    private static final List<WorldDefinition> WORLD_LIST = new ArrayList<>();

    /**
     * The {@link org.gielinor.net.world.WorldService}.
     */
    private static final WorldService worldService = new WorldService();

    /**
     * The last update time stamp (in server ticks).
     */
    private static int updateStamp = 0;

    /**
     * Gets the {@link java.util.List} of current world definitions.
     *
     * @return The list.
     */
    public static List<WorldDefinition> getWorldList() {
        return WORLD_LIST;
    }

    /**
     * Gets the {@link org.gielinor.net.world.WorldService}.
     *
     * @return The world service.
     */
    public static WorldService getWorldService() {
        return worldService;
    }

    /**
     * Gets the packet to update the world list in the lobby.
     *
     * @param ioSession   The current connected {@link org.gielinor.net.IoSession}.
     * @param updateStamp The current update timestamp.
     */
    public static void sendUpdate(IoSession ioSession, int updateStamp) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put((byte) 0);
        byteBuffer.putShort((short) 0); /* The packet size */
        byteBuffer.put((byte) 1);
        PacketBuilder packetBuilder = new PacketBuilder();
        if (updateStamp != WorldList.updateStamp) { /* An update has occurred. */
            byteBuffer.put((byte) 1);
            sendWorlds(packetBuilder);
        } else {
            byteBuffer.put((byte) 0);
            putPlayerInfo(packetBuilder);
        }
        if (packetBuilder.toByteBuffer().position() > 0) {
            byteBuffer.put((ByteBuffer) packetBuilder.toByteBuffer().flip());
        }
        byteBuffer.putShort(1, (short) (byteBuffer.position() - 3));
        ioSession.queue((ByteBuffer) byteBuffer.flip());
    }

    /**
     * Adds world configurations to the packet.
     *
     * @param packetBuilder The current packet.
     */
    private static void sendWorlds(PacketBuilder packetBuilder) {
        packetBuilder.putShort(WORLD_LIST.size());
        for (WorldDefinition worldDefinition : WORLD_LIST) {
            packetBuilder.put(worldDefinition.getId());
            packetBuilder.putInt(worldDefinition.getFlag());
            packetBuilder.putJagString(worldDefinition.getActivity());
            packetBuilder.putJagString(worldDefinition.getAddress());
            packetBuilder.putInt(worldDefinition.getPort());
            packetBuilder.putShort(worldDefinition.getPlayerCount());
            packetBuilder.putSmart(worldDefinition.getCountry());
            packetBuilder.putJagString(worldDefinition.getRegion());
        }
        packetBuilder.putInt(updateStamp);
    }

    /**
     * Adds the world status on the packet.
     *
     * @param packetBuilder The current packet.
     */
    private static void putPlayerInfo(PacketBuilder packetBuilder) {
        for (WorldDefinition worldDefinition : WORLD_LIST) {
            packetBuilder.putShort(worldDefinition.getPlayerCount());
        }
    }

    /**
     * Adds a {@link org.gielinor.net.world.WorldDefinition} to the world list.
     *
     * @param worldDefinition The <code>WorldDefinition</code>.
     */
    public static void addWorld(WorldDefinition worldDefinition) {
        WORLD_LIST.add(worldDefinition);
        flagUpdate();
    }

//    static {
//        addWorld(new WorldDefinition(1, "Main - Free", COUNTRY_USA, FLAG_NON_MEMBERS, "world1.Gielinor.org", 43594, "United States"));
//        addWorld(new WorldDefinition(2, "Beta - Members", COUNTRY_USA, FLAG_MEMBERS | FLAG_BETA, "world1.Gielinor.org", 43595, "United States"));
//    }

    /**
     * Gets a {@link org.gielinor.net.world.WorldDefinition} by the id of the world.
     *
     * @param id The world id.
     * @return The world definition.
     */
    public static WorldDefinition forId(int id) {
        for (WorldDefinition worldDefinition : WORLD_LIST) {
            if (worldDefinition.getId() == id) {
                return worldDefinition;
            }
        }
        return null;
    }

    /**
     * Gets the update timestamp.
     *
     * @return The update timestamp.
     */
    public static int getUpdateStamp() {
        return updateStamp;
    }

    /**
     * Sets the update timestamp.
     */
    public static void flagUpdate() {
        WorldList.updateStamp = World.getTicks();
    }

}
