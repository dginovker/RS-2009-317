package org.gielinor.net.event;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.gielinor.game.node.entity.player.info.PlayerDetails;
import org.gielinor.game.node.entity.player.info.login.LoginParser;
import org.gielinor.game.node.entity.player.info.login.LoginType;
import org.gielinor.game.node.entity.player.info.login.Response;
import org.gielinor.net.IoReadEvent;
import org.gielinor.net.IoSession;
import org.gielinor.rs2.config.Constants;
import org.gielinor.utilities.buffer.ByteBufferUtils;
import org.gielinor.utilities.crypto.ISAACCipher;
import org.gielinor.utilities.crypto.ISAACPair;
import org.gielinor.utilities.string.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles login reading events.
 *
 * @author Emperor
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *
 */
public final class LoginReadEvent extends IoReadEvent {

    private static final Logger log = LoggerFactory.getLogger(LoginReadEvent.class);

    /**
     * The RSA exponent.
     */
    public static final BigInteger RSA_KEY = new BigInteger("63836541338254930133129004074732382929998437615009296959260995188633082779361618777286690536401412536788693103949475863420785422077437411678826531544580956203799783573564225075359462174640338384017065666188771184000361929010560260535244941334940115723150494202345239634306833859051437359114435513508472366353");

    /**
     * The RSA modulus.
     */
    public static final BigInteger MODULUS = new BigInteger("119365899446067315932975991898363325061579719991294025359328021960040125142258621067848949689980866028232491082585431814345859060363748342297790362002830405818586025541018815563000741957417375211440504983329981059065255756529758598479962175681326119784430342275130902058984323109363665114655494006708620299283");

    /**
     * Constructs a new {@code LoginReadEvent}.
     *
     * @param session
     *            The session.
     * @param buffer
     *            The buffer with data to read from.
     */
    public LoginReadEvent(IoSession session, ByteBuffer buffer) {
        super(session, buffer);
    }

    @Override
    public void read(IoSession session, ByteBuffer buffer) {
        int opcode = buffer.get() & 0xFF;
        int loginSize = buffer.get() & 0xFF;
        int loginEncryptSize = loginSize - (36 + 1 + 1 + 2);

        if (loginEncryptSize <= 0) {
            log.info("Malformed encrypted packet size: [{}] from [{}].",
                loginEncryptSize, session.getRemoteAddress());
            session.write(Response.BAD_SESSION_ID);
            return;
        }

        session.setLoginSize(loginSize);
        session.setLoginEncryptSize(loginEncryptSize);

        switch (opcode) {
            case 16: // Reconnect world login
            case 18: // World login
                decodeWorld(opcode, session, buffer);
                break;
            default:
                log.info("Unhandled login attempt from [{}] with opcode [{}].",
                    session.getRemoteAddress(), opcode);
                session.disconnect();
                break;
        }
    }

    /**
     * Decodes a world login request.
     *
     * @param session
     *            The session.
     * @param buffer
     *            The buffer to read from.
     */
    private static void decodeWorld(int opcode, IoSession session, ByteBuffer buffer) {
        @SuppressWarnings("unused")
        int size = session.getLoginSize();
        int encryptSize = session.getLoginEncryptSize();
        int magicId = buffer.get() & 0xFF;

        if (magicId != 255) {
            log.info("Incorrect magic ID [{}] from [{}].",
                magicId, session.getRemoteAddress());
            session.disconnect();
            return;
        }

        int revision = buffer.getShort() & 0xFFFF;

        if (revision != Constants.REVISION) {
            session.write(Response.UPDATED);
            log.info("Bad client version [{}] from [{}]. Expecting [{}].",
                revision, session.getRemoteAddress(), Constants.REVISION);
            return;
        }

        buffer.get(); // memory type, 1=low, 2=high

        // Cache indices.
        for (int i = 0; i < 9; i++) {
            buffer.getInt();
        }

        encryptSize--;
        session.setLoginEncryptSize(encryptSize);
        // Sizing
        int reportedSize = buffer.get() & 0xFF;

        if (reportedSize != encryptSize) {
            log.info("Packet size mismatch. Expected [{}], but received [{}] from [{}].",
                encryptSize, reportedSize, session.getRemoteAddress());
            session.disconnect();
            return;
        }

        buffer = getRSABlock(buffer, session);

        // Server key
        long clientKey = buffer.getLong();
        long serverKey = session.getServerKey();
        long reportedServerKey = buffer.getLong();

        if (reportedServerKey != serverKey) {
            log.info("Server key mismatch. Expected [{}] but received [{}] from [{}].",
                serverKey, reportedServerKey, session.getRemoteAddress());
            session.disconnect();
            return;
        }

        // UID from random.dat
        @SuppressWarnings("unused")
        int uid = buffer.getInt();

        final String name = ByteBufferUtils.getRS2String(buffer).trim();
        String username = TextUtils.formatDisplayName(name);
        String password = ByteBufferUtils.getRS2String(buffer);
        String address = session.getRemoteAddress().replaceAll("/", "").split(":")[0];
        log.info("Login request to [{}] from [{}].", username, address);

        int[] sessionKey = new int[4];
        sessionKey[0] = (int) (clientKey >> 32);
        sessionKey[1] = (int) clientKey;
        sessionKey[2] = (int) (serverKey >> 32);
        sessionKey[3] = (int) serverKey;
        ISAACCipher inCipher = new ISAACCipher(sessionKey);

        for (int i = 0; i < 4; i++) {
            sessionKey[i] += 50;
        }

        ISAACCipher outCipher = new ISAACCipher(sessionKey);
        session.setIsaacPair(new ISAACPair(inCipher, outCipher));
        final PlayerDetails details = new PlayerDetails(username, password, session);
        String compName = ByteBufferUtils.getRS2String(buffer);
        details.setCompName(compName);
        String mac = ByteBufferUtils.getRS2String(buffer);
        details.setMacAddress(mac);
        new LoginParser(details, password, LoginType.fromType(opcode)).run();
    }

    /**
     * Gets the ISAAC seed from the buffer.
     *
     * @param buffer
     *            The buffer to read from.
     * @return The ISAAC seed.
     */
    public static int[] getISAACSeed(ByteBuffer buffer) {
        int[] seed = new int[4];

        for (int i = 0; i < 4; i++) {
            seed[i] = buffer.getInt();
        }

        return seed;
    }

    /**
     * Gets the RSA block buffer.
     *
     * @param buffer
     *            The buffer to get the RSA block from.
     * @return The RSA block buffer.
     */
    public static ByteBuffer getRSABlock(ByteBuffer buffer, IoSession session) {
        byte[] rsaData = new byte[session.getLoginEncryptSize()];
        buffer.get(rsaData);
        ByteBuffer block = ByteBuffer.wrap(new BigInteger(rsaData).modPow(RSA_KEY, MODULUS).toByteArray());
        byte rsa = block.get();

        if (rsa != 10) {
            throw new IllegalArgumentException("Invalid RSA Magic Number: " + rsa);
        }

        return block;
    }

}
