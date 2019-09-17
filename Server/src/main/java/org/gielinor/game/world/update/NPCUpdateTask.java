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


/**
 * A task which creates and sends the NPC update block.
 *
 * @author Graham Edgecombe
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class NPCUpdateTask {

    /**
     * The player.
     */
    private Player player;

    /**
     * Creates an npc update task.
     *
     * @param player The player.
     */
    public NPCUpdateTask(Player player) {
        this.player = player;
    }

    public void execute() {
        /*
         * The update block holds the update masks and data, and is written
         * after the main block.
         */
        PacketBuilder updateBlock = new PacketBuilder();

        RenderInfo info = player.getRenderInfo();
        List<NPC> localNPCs = info.getLocalNpcs();

        /*
         * The main packet holds information about adding, moving and removing
         * NPCs.
         */
        PacketBuilder packet = new PacketBuilder(65, PacketHeader.SHORT);
        packet.setBitAccess();

        /*
         * Write the current size of the npc list.
         */
        packet.putBits(8, localNPCs.size());

        /*
         * Iterate through the local npc list.
         */
        for (Iterator<NPC> it$ = localNPCs.iterator(); it$.hasNext(); ) {
            /*
             * Get the next NPC.
             */
            NPC npc = it$.next();

            /*
             * If the NPC should still be in our list.
             */
            if (!npc.isHidden(player) && !npc.getProperties().isTeleporting() && player.getLocation().withinDistance(npc.getLocation())) {
                /*
                 * Update the movement.
                 */
                updateNPCMovement(packet, npc);

                /*
                 * Check if an update is required, and if so, send the update.
                 */
                if (npc.getUpdateMasks().isUpdateRequired()) {
                    updateNPC(updateBlock, npc);
                }
            } else {
                /*
                 * Otherwise, remove the NPC from the list.
                 */
                it$.remove();

                /*
                 * Tell the client to remove the NPC from the list.
                 */
                packet.putBits(1, 1);
                packet.putBits(2, 3);
            }
        }

        /*
         * Loop through all NPCs in the world.
         */
        for (NPC npc : RegionManager.getLocalNpcs(player)) {
            /*
             * Check if there is room left in the local list.
             */
            if (localNPCs.size() >= 255) {
                /*
                 * There is no more room left in the local list. We cannot add
                 * more NPCs, so we just ignore the extra ones. They will be
                 * added as other NPCs get removed.
                 */
                break;
            }

            /*
             * If they should not be added ignore them.
             */
            if (localNPCs.contains(npc) || npc.isHidden(player) || !npc.isRenderable()) {
                continue;
            }

            /*
             * Add the npc to the local list if it is within distance.
             */
            localNPCs.add(npc);

            /*
             * Add the npc in the packet.
             */
            addNewNPC(packet, npc);

            /*
             * Check if an update is required.
             */
            if (npc.getUpdateMasks().isUpdateRequired()) {

                /*
                 * If so, update the npc.
                 */
                updateNPC(updateBlock, npc);

            }
        }

        /*
         * The update block as a ByteBuffer.
         */
        ByteBuffer masks = updateBlock.toByteBuffer();

        /*
         * Check if the update block isn't empty.
         */
        if (masks.hasRemaining()) {
            /*
             * If so, put a flag indicating that an update block follows.
             */
            packet.putBits(14, 16383);
            packet.setByteAccess();

            /*
             * And append the update block.
             */
            packet.put(masks);
        } else {
            /*
             * Terminate the packet normally.
             */
            packet.setByteAccess();
        }

        /*
         * Write the packet.
         */
        player.getSession().write(packet);
    }

    /**
     * Adds a new NPC.
     *
     * @param packet The main packet.
     * @param npc    The npc to add.
     */
    private void addNewNPC(PacketBuilder packet, NPC npc) {

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

        /*
         * We now write the NPC type id.
         */
        packet.putBits(14, npc.getDefinition().getId());

        /*
         * And indicate if an update is required.
         */
        packet.putBits(1, npc.getUpdateMasks().isUpdateRequired() ? 1 : 0);
    }

    /**
     * Update an NPC's movement.
     *
     * @param packet The main packet.
     * @param npc    The npc.
     */
    private void updateNPCMovement(PacketBuilder packet, NPC npc) {
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
        }
    }

    /**
     * Update an NPC.
     *
     * @param packet The update block.
     * @param npc    The npc.
     */
    private void updateNPC(PacketBuilder packet, NPC npc) {

//		int mask = 0;
//		final UpdateFlags flags = npc.getUpdateMasks();
//		double max = npc.getSkills().getLevelForExperience(Skills.HITPOINTS);
//		double hp = npc.getSkills().getLevel(Skills.HITPOINTS);
//		double calc = hp / max;
//		int percentage = (int) (calc * 100);
//		if (percentage > 100) {
//			percentage = 100;
//		}
//		if (flags.get(UpdateFlag.ANIMATION)) {
//			mask |= 0x10;
//		}
//		if (flags.get(UpdateFlag.HIT)) {
//			mask |= 0x8;
//		}
//		if (flags.get(UpdateFlag.GRAPHICS)) {
//			mask |= 0x80;
//		}
//		if (flags.get(UpdateFlag.FACE_ENTITY)) {
//			mask |= 0x20;
//		}
//		if (flags.get(UpdateFlag.FORCED_CHAT)) {
//			mask |= 0x1;
//		}
//		if (flags.get(UpdateFlag.HIT_2)) {
//			mask |= 0x40;
//		}
//		if (flags.get(UpdateFlag.TRANSFORM)) {
//			mask |= 0x2;
//		}
//		if (flags.get(UpdateFlag.FACE_COORDINATE)) {
//			mask |= 0x4;
//		}
//
//        /*
//         * And write the mask.
//         */
//		packet.put((byte) mask);
////        if (flags.get(UpdateFlag.APPEARANCE)) {
////            if (npc.getName() != null && npc.getName().isEmpty()) {
////                packet.putRS2String(npc.getName());
////            }
////        }
//		if (flags.get(UpdateFlag.ANIMATION)) {
//			if (npc.getCurrentAnimation() != null) {
//				packet.putLEShort(npc.getCurrentAnimation().getId());
//				packet.put((byte) npc.getCurrentAnimation().getDelay());
//			} else {
//				packet.putLEShort(Animation.IDLE.getId());
//				packet.put((byte) 0);
//			}
//		}
//		if (flags.get(UpdateFlag.HIT)) {
//			Hit hit = npc.getPrimaryHit();
//			packet.putByteA((byte) hit.getDamage());
//			packet.putByteC((byte) hit.getType().getId());
//			packet.putByteA((byte) percentage);
//			packet.put((byte) 100);
//		}
//		if (flags.get(UpdateFlag.GRAPHICS)) {
//			packet.putShort(npc.getCurrentGraphic().getId());
//			packet.putInt(npc.getCurrentGraphic().getDelay() + (65536 * npc.getCurrentGraphic().getHeight()));
//		}
//		if (flags.get(UpdateFlag.FACE_ENTITY)) {
//			Entity entity = npc.getInteractingEntity();
//			packet.putShort(entity == null ? -1 : entity.getClientIndex());
//		}
//		if (flags.get(UpdateFlag.FORCED_CHAT)) {
//			packet.putRS2String(npc.getForcedChatMessage());
//		}
//		if (flags.get(UpdateFlag.HIT_2)) {
//			Hit hit = npc.getSecondaryHit();
//			packet.putByteC((byte) hit.getDamage());
//			packet.putByteS((byte) hit.getType().getId());
//			packet.putByteS((byte) percentage);
//			packet.putByteC((byte) 100);
//		}
//		if (flags.get(UpdateFlag.TRANSFORM)) {
//			packet.putLEShortA((int) npc.getTransformId());
//		}
//		if (flags.get(UpdateFlag.FACE_COORDINATE)) {
//			Location loc = npc.getFaceLocation();
//			if (loc == null) {
//				packet.putLEShort(0);
//				packet.putLEShort(0);
//			} else {
//				packet.putLEShort(loc.getX() * 2 + 1);
//				packet.putLEShort(loc.getY() * 2 + 1);
//			}
//		}
    }

    public static int getCurrentHP(int i, int i1, int i2) {
        double x = (double) i / (double) i1;
        return (int) Math.round(x * i2);
    }
}
