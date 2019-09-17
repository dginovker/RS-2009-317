package org.gielinor.game.world.update;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.RenderInfo;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketHeader;
import org.gielinor.rs2.config.Constants;

/**
 * The NPC renderer.
 *
 * @author Emperor
 */
public final class NPCRenderer {

    /**
     * Handles the NPC rendering for a player.
     *
     * @param player The player.
     */
    public static void render(Player player) {
        PacketBuilder buffer = new PacketBuilder(65, PacketHeader.SHORT);
        RenderInfo info = player.getRenderInfo();
        List<NPC> localNPCs = info.getLocalNpcs();
        PacketBuilder maskBuffer = new PacketBuilder(-1, PacketHeader.NORMAL, ByteBuffer.allocate(1 << 16));
        buffer.setBitAccess();
        buffer.putBits(8, localNPCs.size());
        for (Iterator<NPC> it = localNPCs.iterator(); it.hasNext(); ) {
            NPC npc = it.next();
            boolean withinDistance = player.getLocation().withinDistance(npc.getLocation());
            if (npc.isHidden(player) || !withinDistance || npc.getProperties().isTeleporting()) {
                buffer.putBits(1, 1).putBits(2, 3);
                it.remove();
                if (!withinDistance && npc.getAggressiveHandler() != null) {
                    npc.getAggressiveHandler().removeTolerance(player.getIndex());
                }
            } else if (npc.getWalkingQueue().getRunDir() != -1) {
                buffer.putBits(1, 1)
                    .putBits(2, 2)
                    .putBits(3, npc.getWalkingQueue().getWalkDir())
                    .putBits(3, npc.getWalkingQueue().getRunDir());
                flagMaskUpdate(player, buffer, maskBuffer, npc, false);
            } else if (npc.getWalkingQueue().getWalkDir() != -1) {
                buffer.putBits(1, 1)
                    .putBits(2, 1)
                    .putBits(3, npc.getWalkingQueue().getWalkDir());
                flagMaskUpdate(player, buffer, maskBuffer, npc, false);
            } else if (npc.getUpdateMasks().isUpdateRequired()) {
                buffer.putBits(1, 1).putBits(2, 0);
                writeMaskUpdates(player, maskBuffer, npc, false);
            } else {
                buffer.putBits(1, 0);
            }
        }
        for (NPC npc : RegionManager.getLocalNpcs(player)) {
            if (localNPCs.size() >= 255) {
                break;
            }
            if (localNPCs.contains(npc) || npc.isHidden(player) || !npc.isRenderable()) {
                continue;
            }
            buffer = addNewNPC(buffer, npc, player, maskBuffer);
            if (npc.getUpdateMasks().isUpdateRequired()) {
                writeMaskUpdates(player, maskBuffer, npc, false);
            }
            localNPCs.add(npc);
        }
        ByteBuffer masks = maskBuffer.toByteBuffer();
        masks.flip();
        if (masks.hasRemaining()) {
            buffer.putBits(14, 16383);
            buffer.setByteAccess();
            buffer.put(masks);
        } else {
            buffer.setByteAccess();
        }
        player.getSession().write(buffer);
    }

    /**
     * Adds a new NPC.
     *
     * @param packet The main buffer.
     * @param npc    The npc to add.
     */
    private static PacketBuilder addNewNPC(PacketBuilder packet, NPC npc, Player player, PacketBuilder maskBuffer) {
        /*
         * Write the NPC's index.
         */
        packet.putBits(14, npc.getIndex());

        /*
         * Calculate the x and y offsets.
         */
        int yPos = npc.getLocation().getY() - player.getLocation().getY();
        int xPos = npc.getLocation().getX() - player.getLocation().getX();

        /*
         * And write them.
         */
        packet.putBits(5, yPos);
        packet.putBits(5, xPos);

        /*
         * TODO unsure, but probably discards the client-side walk queue.
         */
        packet.putBits(1, 0);

        /**
         * The facing direction.
         */
        packet.putBits(3, npc.getDirection().ordinal());

        /*
         * We now write the NPC type id.
         */
        int npcId = npc.getId();
        packet.putBits(Constants.NPC_BITS, npcId);

        /*
         * And indicate if an update is required.
         */
        packet.putBits(1, npc.getUpdateMasks().isUpdateRequired() ? 1 : 0);
        return packet;
    }

    /**
     * Update an NPC's movement.
     *
     * @param packet The main packet.
     * @param npc    The npc.
     */
    private void updateNPCMovement(PacketBuilder packet, NPC npc, Player player, PacketBuilder maskBuffer) {
        /*
         * Check if the NPC is running.
         */
        if (npc.getWalkingQueue().getRunDir() == -1) {
            /*
             * They are not, so check if they are walking.
             */
            if (npc.getWalkingQueue().getWalkDir() == -1) {
                /*
                 * They are not walking, check if the NPC needs an update.
                 */
                if (npc.getUpdateMasks().isUpdateRequired()) {
                    /*
                     * Indicate an update is required.
                     */
                    packet.putBits(1, 1);

                    /*
                     * Indicate we didn't move.
                     */
                    packet.putBits(2, 0);
                    writeMaskUpdates(player, maskBuffer, npc, false);
                } else {
                    /*
                     * Indicate no update or movement is required.
                     */
                    packet.putBits(1, 0);
                }
            } else {
                /*
                 * They are walking, so indicate an update is required.
                 */
                packet.putBits(1, 1);

                /*
                 * Indicate the NPC is walking 1 tile.
                 */
                packet.putBits(2, 1);

                /*
                 * And write the direction.
                 */
                packet.putBits(3, npc.getWalkingQueue().getWalkDir());

                /*
                 * And write the update flag.
                 */
                packet.putBits(1, npc.getUpdateMasks().isUpdateRequired() ? 1 : 0);
                if (npc.getUpdateMasks().isUpdateRequired()) {
                    writeMaskUpdates(player, maskBuffer, npc, false);
                }
            }
        } else {
            /*
             * They are running, so indicate an update is required.
             */
            packet.putBits(1, 1);

            /*
             * Indicate the NPC is running two tiles.
             */
            packet.putBits(2, 2);

            /*
             * And write the directions.
             */
            packet.putBits(3, npc.getWalkingQueue().getWalkDir());
            packet.putBits(3, npc.getWalkingQueue().getRunDir());

            /*
             * And write the update flag.
             */
            packet.putBits(1, npc.getUpdateMasks().isUpdateRequired() ? 1 : 0);
            if (npc.getUpdateMasks().isUpdateRequired()) {
                writeMaskUpdates(player, maskBuffer, npc, false);
            }
        }
    }

    /**
     * Sets the mask update flag.
     *
     * @param buffer The buffer to write on.
     * @param npc    The NPC.
     */
    private static void flagMaskUpdate(Player player, PacketBuilder buffer, PacketBuilder maskBuffer, NPC npc, boolean sync) {
        if (npc.getUpdateMasks().isUpdateRequired()) {
            buffer.putBits(1, 1);
            writeMaskUpdates(player, maskBuffer, npc, sync);
        } else {
            buffer.putBits(1, 0);
        }
    }

    /**
     * Writes the mask updates.
     *
     * @param maskBuffer The mask buffer to write on.
     * @param npc        The NPC to update.
     * @param sync       If called upon synchronization.
     */
    public static void writeMaskUpdates(Player player, PacketBuilder maskBuffer, NPC npc, boolean sync) {
        if (sync) {
            npc.getUpdateMasks().writeSynced(player, npc, maskBuffer, true, false);
        } else {
            npc.getUpdateMasks().write(player, npc, maskBuffer);
        }
    }
}
