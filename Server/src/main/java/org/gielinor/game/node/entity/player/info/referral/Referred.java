package org.gielinor.game.node.entity.player.info.referral;

/**
 * Represents a referred referred.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class Referred {

    /**
     * The referred pidn.
     */
    private final int pidn;
    /**
     * The referred username.
     */
    private final String username;
    /**
     * The request time.
     */
    private final long referredRequestTime;
    /**
     * The accepted referred time.
     */
    private long referredTime;
    /**
     * Whether or not this is a request to be referred.
     */
    private final boolean requested;
    /**
     * Whether or not the player was notified of the acceptance of this referral.
     */
    private boolean notified;
    /**
     * Whether or not the player was declined of the acceptance.
     */
    private boolean declined;

    /**
     * Constructs a new <code>Referred</code>.
     *
     * @param pidn                The referred pidn.
     * @param username            The player's name.
     * @param referredRequestTime The request time.
     * @param referredTime        The accepted referred time.
     * @param requested           Whether or not this is a request to be referreded.
     */
    public Referred(int pidn, String username, long referredRequestTime, long referredTime, boolean requested) {
        this.pidn = pidn;
        this.username = username;
        this.referredRequestTime = referredRequestTime;
        this.referredTime = referredTime;
        this.requested = requested;
    }

    /**
     * Constructs a new <code>Referred</code>.
     *
     * @param pidn                The referred pidn.
     * @param username            The player's name.
     * @param referredRequestTime The request time.
     * @param requested           Whether or not this is a request to be referreded.
     */
    public Referred(int pidn, String username, long referredRequestTime, boolean requested) {
        this(pidn, username, referredRequestTime, 0, requested);
    }

    /**
     * The referred pidn.
     */
    public int getPidn() {
        return pidn;
    }

    /**
     * Gets the referred username.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * The request time.
     */
    public long getReferredRequestTime() {
        return referredRequestTime;
    }

    /**
     * Gets the accepted referred time.
     */
    public long getReferredTime() {
        return referredTime;
    }

    /**
     * Sets the accepted referred time.
     */
    public void setReferredTime(long referredTime) {
        this.referredTime = referredTime;
    }

    /**
     * Whether or not this is a request to be referred.
     */
    public boolean isRequested() {
        return requested;
    }

    /**
     *
     */
    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    /**
     *
     */
    public boolean isDeclined() {
        return declined;
    }

    public void setDeclined(boolean declined) {
        this.declined = declined;
    }

    @Override
    public String toString() {
        return Referred.class.getName() + " [pidn=" + pidn + " username=" + username + " referredRequestTime=" + referredRequestTime + " referredTime=" + referredTime + " requested=" + requested + "]";
    }
}
