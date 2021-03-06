package org.gielinor.game.content.eco.grandexchange.offer;

/**
 * Represents the state of a Grand Exchange offer.
 * @author Emperor
 *
 */
public enum OfferState {

    /**
     * The player is still constructing the offer.
     */
    PENDING,

    /**
     * The player has confirmed the offer (the offer is being dispatched).
     */
    REGISTERED,

    /**
     * The offer has been aborted.
     */
    ABORTED,

    /**
     * The offer has been updated.
     */
    UPDATED,

    /**
     * The offer is completed.
     */
    COMPLETED,

    /**
     * The offer is outdated.
     */
    OUTDATED,

    /**
     * The offer has been removed.
     */
    REMOVED,

    BUYING,

    SELLING,

    ABORTED_SELL,

    ABORTED_BUY,

    COMPLETED_BUY,

    COMPLETED_SELL;
}
