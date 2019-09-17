package com.runescape.cache.config.ge;

/**
 * Represents a GrandExchange offer state.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public enum OfferState {

    PENDING(1),
    REGISTERED(2),
    ABORTED(3),
    UPDATED(4),
    COMPLETED(5),
    OUTDATED(6),
    REMOVED(7),
    SELLING(8),
    BUYING(9),
    ABORTED_SELL(10),
    ABORTED_BUY(11),
    COMPLETED_BUY(12),
    COMPLETED_SELL(13);

    /**
     * The id of this state.
     */
    private final int id;

    /**
     * Constructs an <code>OfferState</code>.
     *
     * @param id The id of this state.
     */
    OfferState(int id) {
        this.id = id;
    }

    /**
     * Gets a state by id.
     *
     * @return The state.
     */
    public static OfferState forId(int id) {
        for (OfferState offerState : values()) {
            if (offerState.getId() == id) {
                return offerState;
            }
        }
        return null;
    }

    /**
     * Gets the id of this state.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }
}
