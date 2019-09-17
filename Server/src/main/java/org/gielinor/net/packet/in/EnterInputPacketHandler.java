package org.gielinor.net.packet.in;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.utilities.buffer.ByteBufferUtils;
import org.gielinor.utilities.misc.RunScript;

/**
 * Represents the {@link org.gielinor.net.packet.IncomingPacket} for a closed input dialogue.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class EnterInputPacketHandler implements IncomingPacket {

    private final static int TEXT_OPCODE = 60;

    @Override
    public void decode(Player player, int opcode, PacketBuilder buffer) {

        final String inputString = opcode == TEXT_OPCODE ? ByteBufferUtils.getRS2String(buffer.toByteBuffer()) : String.valueOf(buffer.getInt());

        final RunScript script = player.getAttribute("runscript", null);

        if (script == null || player.getLocks().isInteractionLocked())
            return;

        script.setValue(opcode == TEXT_OPCODE ? inputString : Integer.parseInt(inputString));
        script.setPlayer(player);
        player.removeAttribute("runscript");
        script.handle();
    }
}
