package org.gielinor.net.packet.in;

import java.util.List;

import org.gielinor.game.interaction.Interaction;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.ai.AIPlayer;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.PlayerContext;
import org.gielinor.net.packet.out.ClearMinimapFlag;

/**
 * Represents the object option packet.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ObjectOptionPacketHandler implements IncomingPacket {

    /**
     * Represents the first click option opcode.
     */
    private static final int OPTION_1 = 132;

    /**
     * Represents the second click option opcode.
     */
    private static final int OPTION_2 = 252;

    /**
     * Represents the third click option opcode.
     */
    private static final int OPTION_3 = 70;

    /**
     * Represents the fourth click option opcode.
     */
    private static final int OPTION_4 = 234;

    /**
     * Represents the fifth click option opcode.
     */
    private static final int OPTION_5 = 228;

    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        if (player == null) {
            return;
        }
        player = PlayerOptionPacketHandler.getPlayer(player);
        player.debug("Object Opcode: " + opcode);
        if (player.getLocks().isInteractionLocked() || !player.getInterfaceState().close()) {
            if (player.getAttribute("REMOVE_OBJECT") == null) {
                return;
            }
        }
        if (!(player instanceof AIPlayer) && player.getLocks().isMovementLocked() || !player.getInterfaceState().close() ||
            !player.getInterfaceState().closeSingleTab() || !player.getDialogueInterpreter().close()) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            player.debug("[WalkPacket] did not handle - [locked=" + player.getLocks().isMovementLocked() + "]!");
            return;
        }
        if (player.getAttribute("REMOVE_OBJECT") == null) {
            player.getInterfaceState().closeChatbox();
        }
        int optionIndex = -1;
        int objectId = -1;
        int x = -1;
        int y = -1;
        switch (packet.opcode()) {
            case OPTION_1:
                optionIndex = 0;
                x = packet.getLEShortA();
                objectId = packet.getShort() & 0xFFFF;
                y = packet.getShortA();
                break;
            case OPTION_2:
                optionIndex = 1;
                objectId = packet.getLEShortA() & 0xFFFF;
                y = packet.getLEShort();
                x = packet.getShortA();
                break;
            case OPTION_3:
                optionIndex = 2;
                x = packet.getLEShort();
                y = packet.getShort();
                objectId = packet.getLEShortA() & 0xFFFF;
                break;
            case OPTION_4:
                optionIndex = 3;
                x = packet.getLEShortA();
                objectId = packet.getShortA() & 0xFFFF;
                y = packet.getLEShortA();
                break;
            case OPTION_5:
                optionIndex = 4;
                objectId = packet.getShortA() & 0xFFFF;
                y = packet.getShortA();
                x = packet.getShort();
                break;
        }
        if (player.getAttribute("REMOVE_OBJECT") != null) {
            player.setAttribute("REMOVE_OBJECT", new int[]{ objectId, x, y });
            player.getDialogueInterpreter().open("RottenPotato");
            return;
        }
        if (optionIndex != -1) {
            handleObjectInteraction(player, optionIndex, x, y, objectId);
        }
    }

    /**
     * Handles object interaction
     *
     * @param player      The interacting player.
     * @param optionIndex The option index.
     * @param x           The x-coordinate of the object.
     * @param y           The y-coordinate of the object.
     * @param objectId    The object id.
     */
    private static void handleObjectInteraction(final Player player, int optionIndex, int x, int y, int objectId) {
        GameObject gameObject = RegionManager.getObject(player.getLocation().getZ(), x, y);
        if (gameObject == null || gameObject.getId() != objectId) {
            player.debug("GameObject(" + objectId + ") interaction was " + gameObject + " at location " + x + ", " + y + ".");
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            Interaction.handleInvalidInteraction(player, gameObject, Option.NULL);
            return;
        }
        if (!gameObject.isActive()) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            Interaction.handleInvalidInteraction(player, gameObject, Option.NULL);
            return;
        }
        gameObject.getInteraction().setDefault(); // TODO
        gameObject = gameObject.getChild(player);
        gameObject.getInteraction().setDefault();
        Option option = gameObject.getInteraction().get(optionIndex);
        if (option == null) {
            player.debug("Invalid option1" + gameObject);
            player.debug("original: " + objectId + " (h1).");
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            Interaction.handleInvalidInteraction(player, gameObject, Option.NULL);
            return;
        }
        player.debug(gameObject + ", original=" + objectId + ", option=" + option.getName() + "");
        player.debug("dir=" + gameObject.getDirection());
        if (option.getHandler() != null) {
            player.debug("Object handler: " + option.getHandler().getClass().getSimpleName());
        }
        handleAIPLegion(player, 1, optionIndex, x, y, objectId);
        gameObject.getInteraction().handle(player, option);
    }

    /**
     * Handles the AIPlayer legion.
     *
     * @param player The player.
     * @param args   The arguments.
     */
    private static void handleAIPLegion(Player player, int... args) {
        if (player.isArtificial()) {
            List<AIPlayer> legion = player.getAttribute("aip_legion");
            if (legion != null) {
                for (AIPlayer aip : legion) {
                    if (aip != player) {
                        handleObjectInteraction(aip, args[0], args[1], args[2], args[3]);
                    }
                }
            }
        }
    }
}
