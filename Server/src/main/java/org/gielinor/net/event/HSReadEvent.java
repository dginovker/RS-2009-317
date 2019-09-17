package org.gielinor.net.event;

import java.nio.ByteBuffer;

import org.gielinor.net.IoReadEvent;
import org.gielinor.net.IoSession;
import org.gielinor.net.world.WorldList;
import org.gielinor.rs2.config.Constants;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles handshake read events.
 *
 * @author Emperor
 */
public final class HSReadEvent extends IoReadEvent {

    /**
     * Constructs a new {@code HSReadEvent}.
     *
     * @param session The session.
     * @param buffer  The buffer.
     */
    public HSReadEvent(IoSession session, ByteBuffer buffer) {
        super(session, buffer);
    }

    @Override
    public void read(IoSession session, ByteBuffer byteBuffer) {
        int opcode = byteBuffer.get() & 0xFF;
        switch (opcode) {
            case 14:
                session.setNameHash(byteBuffer.get() & 0xFF);
                session.setServerKey(RandomUtil.RANDOM.nextLong());
                session.write(true);
                break;
            case 15:
                int revision = byteBuffer.getInt();
                if (revision != Constants.REVISION) {
                    System.err.println("Client build does not match server build!");
                    session.disconnect();
                    break;
                }
                session.write(false);
                break;
            case 18:
                int updateStamp = byteBuffer.getInt();
                WorldList.sendUpdate(session, updateStamp);
                break;

            default:
                if (opcode == 20) {
                    break;
                }
                System.err.println("Unhandled handshake opcode: " + opcode + ".");
        }
    }

}