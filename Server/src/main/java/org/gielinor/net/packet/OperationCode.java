package org.gielinor.net.packet;

/**
 * Represents an operation code constant for incoming and outgoing packets.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class OperationCode {

    /**
     * The opcode for offering one to a container.
     */
    public static final int OPTION_OFFER_ONE = 145;

    /**
     * The opcode for withdrawing / storing five items.
     */
    public static final int OPTION_OFFER_FIVE = 117;

    /**
     * The opcode for withdrawing / storing ten items.
     */
    public static final int OPTION_OFFER_TEN = 43;

    /**
     * The opcode for withdrawing / storing all items.
     */
    public static final int OPTION_OFFER_ALL = 129;

    /**
     * The opcode for withdrawing / storing x items.
     */
    public static final int OPTION_OFFER_X = 135;
}
