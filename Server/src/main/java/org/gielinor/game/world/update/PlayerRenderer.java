package org.gielinor.game.world.update;

import java.nio.ByteBuffer;
import java.util.Iterator;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.RenderInfo;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketHeader;

/**
 * Handles the player rendering.
 *
 * @author Emperor
 */
public final class PlayerRenderer {

    /**
     * The maximum amount of players to add per cycle.
     */
    private static final int MAX_ADD_COUNT = 10;

    /**
     * Handles the player rendering for a player.
     *
     * @param player The player.
     */
    public static void render(Player player) {
        PacketBuilder buffer = new PacketBuilder(81, PacketHeader.SHORT);
        PacketBuilder flags = new PacketBuilder(-1, PacketHeader.NORMAL);
        RenderInfo info = player.getRenderInfo();
        updateLocalPosition(player, buffer, flags);
        buffer.putBits(8, info.getLocalPlayers().size());
        for (Iterator<Player> it = info.getLocalPlayers().iterator(); it.hasNext(); ) {
            Player other = it.next();
            if (other == player && player.getSavedData().getGlobalData().getVisibility() == 2) {
                buffer.putBits(1, 1);
                buffer.putBits(2, 3);
                it.remove();
                continue;
            }
            if (other.isHidden(player) || !Repository.getPlayers().contains(other) ||
                other.getSavedData().getGlobalData().getVisibility() > 0 && !player.getRights().isAdministrator()) {
                buffer.putBits(1, 1);
                buffer.putBits(2, 3);
                it.remove();
                continue;
            }
            if (!other.isActive() || !other.getLocation().withinDistance(player.getLocation()) ||
                other.getProperties().isTeleporting()) {
                buffer.putBits(1, 1);
                buffer.putBits(2, 3);
                it.remove();
                continue;
            }
            renderLocalPlayer(player, other, buffer, flags);
        }
        int count = 0;
        for (Player otherPlayer : RegionManager.getLocalPlayers(player, 15)) {
            if (otherPlayer == player || !otherPlayer.isActive() || info.getLocalPlayers().contains(otherPlayer)) {
                continue;
            }
            if (otherPlayer.isHidden(player)) {
                continue;
            }
            if (!Repository.getPlayers().contains(otherPlayer)) {
                otherPlayer.clear();
                continue;
            }
            if (otherPlayer.getSavedData().getGlobalData().getVisibility() > 0 && !player.getRights().isAdministrator()) {
                continue;
            }
            if (info.getLocalPlayers().size() >= 255 || ++count == MAX_ADD_COUNT) {
                break;
            }
            addLocalPlayer(player, otherPlayer, info, buffer, flags);
        }
        ByteBuffer masks = flags.toByteBuffer();
        masks.flip();
        if (masks.hasRemaining()) {
            buffer.putBits(11, 2047);
            buffer.setByteAccess();
            buffer.put(masks);
        } else {
            buffer.setByteAccess();
        }
        player.getDetails().getSession().write(buffer);
    }

    /**
     * Renders a local player.
     *
     * @param player The player we're updating for.
     * @param other  The player.
     * @param buffer The buffer.
     * @param flags  The update flags buffer.
     */
    private static void renderLocalPlayer(Player player, Player other, PacketBuilder buffer, PacketBuilder flags) {
        if (other.getWalkingQueue().getRunDir() != -1) {
            buffer.putBits(1, 1); //Updating
            buffer.putBits(2, 2); //Sub opcode
            buffer.putBits(3, other.getWalkingQueue().getWalkDir());
            buffer.putBits(3, other.getWalkingQueue().getRunDir());
            flagMaskUpdate(player, other, buffer, flags, false, false, false);
        } else if (other.getWalkingQueue().getWalkDir() != -1) {
            buffer.putBits(1, 1); //Updating
            buffer.putBits(2, 1); //Sub opcode
            buffer.putBits(3, other.getWalkingQueue().getWalkDir());
            flagMaskUpdate(player, other, buffer, flags, false, false, false);
        } else if (other.getUpdateMasks().isUpdateRequired()) {
            buffer.putBits(1, 1);
            buffer.putBits(2, 0);
            writeMaskUpdates(player, other, flags, false, false, false);
        } else {
            buffer.putBits(1, 0);
        }
    }

    /**
     * Adds a local player.
     *
     * @param player The player.
     * @param other  The player to add.
     * @param info   The render info of the player.
     * @param buffer The buffer.
     * @param flags  The flag based buffer.
     */
    private static void addLocalPlayer(Player player, Player other, RenderInfo info, PacketBuilder buffer, PacketBuilder flags) {
        buffer.putBits(11, other.getIndex());
        int yPos = other.getLocation().getY() - player.getLocation().getY();
        int xPos = other.getLocation().getX() - player.getLocation().getX();
        boolean appearance = info.getAppearanceStamps()[other.getIndex() & 0x800] != other.getUpdateMasks().getAppearanceStamp();
        boolean chat = !(player == other);
        boolean update = chat || appearance || other.getUpdateMasks().isUpdateRequired() || other.getUpdateMasks().hasSynced();
        buffer.putBits(1, 1);
        buffer.putBits(1, 1);
        buffer.putBits(5, yPos);
        buffer.putBits(5, xPos);
        info.getLocalPlayers().add(other);
        if (update) {
            if (appearance) {
                info.getAppearanceStamps()[other.getIndex() & 0x800] = other.getUpdateMasks().getAppearanceStamp();
            }
            writeMaskUpdates(player, other, flags, appearance, chat, true);
        }
    }

    /**
     * Updates the local player's client position.
     *
     * @param local  The local player.
     * @param buffer The i/o buffer.
     * @param flags  The update flags buffer.
     */
    private static void updateLocalPosition(Player local, PacketBuilder buffer, PacketBuilder flags) {
        if (local.getPlayerFlags().isUpdateSceneGraph() || local.getProperties().isTeleporting()) {
            buffer.putBits(1, 1); //Updating
            buffer.putBits(2, 3); //Sub opcode
            buffer.putBits(2, local.getLocation().getZ());
            buffer.putBits(1, local.getProperties().isTeleporting() ? 1 : 0);
            flagMaskUpdate(local, local, buffer, flags, false, false, false);
            buffer.putBits(7, local.getLocation().getSceneY(local.getPlayerFlags().getLastSceneGraph()));
            buffer.putBits(7, local.getLocation().getSceneX(local.getPlayerFlags().getLastSceneGraph()));
        } else {
            renderLocalPlayer(local, local, buffer, flags);
        }
    }

    /**
     * Sets the update mask flag.
     *
     * @param local      The local player.
     * @param player     The player to update.
     * @param buffer     The packet buffer.
     * @param maskBuffer The mask buffer.
     * @param sync       If we should use the synced buffer.
     * @param appearance If appearance update mask should be used in the synced buffer.
     */
    private static void flagMaskUpdate(Player local, Player player, PacketBuilder buffer, PacketBuilder maskBuffer, boolean sync, boolean chat, boolean appearance) {
        if (player.getUpdateMasks().isUpdateRequired()) {
            buffer.putBits(1, 1);
            writeMaskUpdates(local, player, maskBuffer, appearance, sync, chat);
        } else {
            buffer.putBits(1, 0);
        }
    }

    /**
     * Updates the player flags.
     *
     * @param local      The local player.
     * @param player     The player to update.
     * @param flags      The flags buffer.
     * @param appearance If we should force appearance.
     * @param sync       If we should use the synced buffer.
     */
    private static void writeMaskUpdates(Player local, Player player, PacketBuilder flags, boolean appearance, boolean chat, boolean sync) {
        if (sync) {
            player.getUpdateMasks().writeSynced(local, player, flags, appearance, chat);
        } else if (player.getUpdateMasks().isUpdateRequired()) {
            player.getUpdateMasks().write(local, player, flags);
        }
    }
}
