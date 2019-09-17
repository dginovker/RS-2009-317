package org.gielinor.net.packet.in;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.ai.AIPlayer;
import org.gielinor.game.world.map.Location;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.PlayerContext;
import org.gielinor.net.packet.out.ClearMinimapFlag;
import org.gielinor.rs2.pulse.impl.MovementPulse;

import plugin.activity.duelarena.DuelRule;
import plugin.npc.AutoSpawnNPC;

/**
 * A packet which handles walking requests.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class WalkingPacketHandler implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        player = PlayerOptionPacketHandler.getPlayer(player);
        if (!(player instanceof AIPlayer) && player.getLocks().isMovementLocked() || !player.getInterfaceState().close() ||
            !player.getInterfaceState().closeSingleTab() || !player.getDialogueInterpreter().close()) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            player.debug("[WalkPacket] did not handle - [locked=" + player.getLocks().isMovementLocked() + "]!");
            return;
        }
        if (opcode != 98 && DuelRule.NO_MOVEMENT.enforce(player, true)) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            return;
        }
        if (player.getAttribute("TELE_CS") != null) {
            player.removeAttribute("TELE_CS");
            return;
        }
        int size = packet.toByteBuffer().remaining();
        if (packet.opcode() == 248) {
            size -= 14;
        }
        player.getProperties().setSpell(null);
        player.getInterfaceState().close();
        player.getInterfaceState().closeChatbox();

        final int steps = (size - 5) / 2;
        final int[][] path = new int[steps][2];

        final int firstX = packet.getLEShortA();

        for (int i = 0; i < steps; i++) {
            path[i][0] = packet.get();
            path[i][1] = packet.get();
        }

        final int firstY = packet.getLEShort();
        final boolean runSteps = packet.getC() == 1;

        if (opcode == 98) {
            player.getWalkingQueue().setRunning(runSteps);
            return; // Action walking.
        }

        int destX = firstX, destY = firstY;
        if (steps > 0) {
            destX = path[steps - 1][0] + firstX;
            destY = path[steps - 1][1] + firstY;
        }

        player.face(null);
        player.faceLocation(null);
        player.getWalkingQueue().reset(runSteps);
        if (player.getAttribute("SPAWN_NPC") != null || (player.getAttribute("RESPAWN_NPC") != null)) {
            int npcId = player.getAttribute("SPAWN_NPC") instanceof NPC ? ((NPC) player.getAttribute("SPAWN_NPC")).getId() : player.getAttribute("SPAWN_NPC");
            if (NPCDefinition.forId(npcId) == null) {
                player.getDialogueInterpreter().sendPlaneMessage("No NPC Definition for #" + npcId);
                return;
            }
            NPC npc = new AutoSpawnNPC(npcId, Location.create(destX, destY, player.getLocation().getZ()), 0);
            npc.init();
            npc.setAggressive(false);
            npc.setWalks(false);
            npc.setWalkRadius(0);

            player.setAttribute("SPAWN_NPC", npc);
            player.getDialogueInterpreter().open("RottenPotato", true, npc);
            return;
        }
        NPC npc = NPCOptionPacketHandler.getNPC(player);
        Location walkTo = Location.create(destX, destY, player.getLocation().getZ());

        if (player.getAttribute("noclip", false)) {
            if (npc != null) {
                npc.getWalkingQueue().addPath(destX, destY);
            } else {
                player.getPulseManager().clear();
                player.getWalkingQueue().addPath(destX, destY);
            }
        } else {
            player.getPulseManager().run(new MovementPulse(npc == null ? player : npc, walkTo, runSteps) {

                @Override
                public boolean pulse() {
                    return true;
                }
            }, "movement");
        }
    }
}
