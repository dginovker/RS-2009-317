package com.runescape.cache.config.ge;


import com.runescape.net.RSStream;

/**
 * Represents a Grand Exchange offer.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class GrandExchangeOffer {

    /**
     * The {@link com.runescape.cache.config.ge.OfferState}.
     */
    private OfferState offerState;
    /**
     * The id of the item.
     */
    private int itemId;
    /**
     * The value offered.
     */
    private int offered;
    /**
     * The amount of the item being exchanged.
     */
    private int amount;
    /**
     * The amount of the item exchange completed.
     */
    private int completed;
    /**
     * The total coins exchanged in this offer.
     */
    private int totalCoinExchange;

    /**
     * Constructs a new <code>GrandExchangeOffer</code>.
     */
    public GrandExchangeOffer() {
        /* empty */
    }

    /**
     * Constructs a new <code>GrandExchangeOffer</code>.
     *
     * @param rsStream The {@link com.runescape.net.RSStream} to read from.
     */
    public GrandExchangeOffer(RSStream rsStream) {
        this.setOfferState(OfferState.forId(rsStream.getByte()));
        this.setItemId(rsStream.getShort());
        this.setOffered(rsStream.getInt());
        this.setAmount(rsStream.getInt());
        this.setCompleted(rsStream.getInt());
        this.setTotalCoinExchange(rsStream.getInt());
    }


    /**
     * Gets the {@link OfferState}.
     *
     * @return The state.
     */
    public OfferState getOfferState() {
        return offerState;
    }

    /**
     * Sets the {@link OfferState}.
     *
     * @param offerState The state to set.
     */
    public void setOfferState(OfferState offerState) {
        this.offerState = offerState;
    }

    /**
     * Gets the id of the item.
     *
     * @return The id of the item.
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Sets the id of the item.
     *
     * @param itemId The id of the item.
     */
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets the value offered.
     *
     * @return The value offered.
     */
    public int getOffered() {
        return offered;
    }

    /**
     * Sets the valued offered.
     *
     * @param offered The valued offered.
     */
    public void setOffered(int offered) {
        this.offered = offered;
    }

    /**
     * Gets the amount of the item being exchanged.
     *
     * @return The amount.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the item being exchanged.
     *
     * @param amount The amount to set.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Gets the amount of the item exchange completed.
     *
     * @return The amount completed.
     */
    public int getCompleted() {
        return completed;
    }

    /**
     * Sets the amount of the item exchange completed.
     *
     * @param completed The amount completed to set.
     */
    public void setCompleted(int completed) {
        this.completed = completed;
    }

    /**
     * Gets the total coins exchanged in this offer.
     *
     * @return The total exchanged.
     */
    public int getTotalCoinExchange() {
        return totalCoinExchange;
    }

    /**
     * Sets the total coins exchanged in this offer.
     *
     * @param totalCoinExchange The total exchanged to set.
     */
    public void setTotalCoinExchange(int totalCoinExchange) {
        this.totalCoinExchange = totalCoinExchange;
    }
}
