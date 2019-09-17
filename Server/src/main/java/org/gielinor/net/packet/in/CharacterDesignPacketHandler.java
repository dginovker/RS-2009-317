package org.gielinor.net.packet.in;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.appearance.Appearance.AppearanceCache;
import org.gielinor.game.node.entity.player.link.appearance.Gender;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;

/**
 * Represents the {@link org.gielinor.net.packet.IncomingPacket} accepting a design for the player's character.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class CharacterDesignPacketHandler implements IncomingPacket {

    @Override
    public void decode(Player player, int opcode, PacketBuilder packet) {
        player.getAppearance().setGender(packet.get() == 0 ? Gender.MALE : Gender.FEMALE);
        int[] looks = new int[]{
            AppearanceCache.HAIR, AppearanceCache.FACIAL_HAIR, AppearanceCache.TORSO, AppearanceCache.ARMS,
            AppearanceCache.WRISTS, AppearanceCache.LEGS, AppearanceCache.FEET
        };
        int[] colors = new int[]{ 0, 2, 5, 6, 4 };
        for (int index = 0; index < 7; index++) {
            int look = packet.getShort();
            if (look < 0 && index >= 1 && index < 6) {
                look = look + 256;
            }
            player.getAppearance().getAppearanceCache()[looks[index]].setLook(look);
        }
        if (player.getAppearance().getGender() == Gender.FEMALE) {
            player.getAppearance().getAppearanceCache()[1].setLook(56);
        }
        for (int index = 0; index < 5; index++) {
            int colour = packet.get();
            player.getAppearance().getAppearanceCache()[colors[index]].setColour(colour);
        }
        player.getAppearance().sync();
        player.getInterfaceState().close();
    }
}
