package org.gielinor.net.event;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.entity.player.info.login.Response;
import org.gielinor.net.EventProducer;
import org.gielinor.net.IoSession;
import org.gielinor.net.IoWriteEvent;
import org.gielinor.net.producer.GameEventProducer;

import java.nio.ByteBuffer;

/**
 * Handles login writing events.
 *
 * @author Emperor
 */
public final class LoginWriteEvent extends IoWriteEvent {

    /**
     * The game event producer.
     */
    private static final EventProducer GAME_PRODUCER = new GameEventProducer();

    /**
     * Constructs a new {@code LoginWriteEvent}.
     *
     * @param session
     *            The session.
     * @param context
     *            The event context.
     */
    public LoginWriteEvent(IoSession session, Object context) {
        super(session, context);
    }

    @Override
    public void write(IoSession session, Object context) {
        Response response = (Response) context;
        ByteBuffer byteBuffer = ByteBuffer.allocate(500);
        byteBuffer.put((byte) response.opcode());
        switch (response) {
            case LOGIN_OK:
                byteBuffer.put(getWorldResponse(session));
                session.setProducer(GAME_PRODUCER);
                break;
            case TRANSFERRED:
                byteBuffer.put((byte) 15);
                break;
        }
        byteBuffer.flip();
        session.queue(byteBuffer);
    }

    /**
     * Gets the world response buffer.
     *
     * @param session
     *            The session.
     * @return The buffer.
     */
    private static ByteBuffer getWorldResponse(IoSession session) {
        ByteBuffer buffer = ByteBuffer.allocate(150);
        Player player = session.getPlayer();
        buffer.put((byte) player.getRights().getMemberGroupId()); // privilege
        for (int icon : Rights.Companion.getChatIcons(player)) {
            buffer.put((byte) icon); // each individual chat icon
        }
        buffer.put((byte) 0); // flagged
        buffer.flip();
        return buffer;
    }
}
