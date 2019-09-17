package org.gielinor.game.node.entity.player.info.portal;

import org.gielinor.game.node.entity.player.info.portal.punishment.Punishment;
import org.gielinor.game.node.entity.player.info.portal.punishment.type.Ban;
import org.gielinor.game.node.entity.player.info.portal.punishment.type.Mute;

/**
 * Represents the player portal of a player.
 *
 * @author 'Vexia
 * @date 24/11/2013
 */
public final class PlayerPortal {

    /**
     * The players unique identification number.
     */
    private int pidn;

    /**
     * Represents the punishment of being muted from collaborating/interacting by chat.
     */
    private Punishment mute;

    /**
     * Represents the punishment of being banned from in-game interaction.
     */
    private Punishment ban;

    /**
     * Represents if the player is a member.
     */
    private boolean member;

    /**
     * Represents the messages of this portal.
     */
    private int messages;

    /**
     * Constructs a new {@code PlayerPortal} {@code Object}.
     */
    public PlayerPortal() {
        /**
         * empty.
         */
    }

    public void setPidn(int pidn) {
        this.pidn = pidn;
    }

    /**
     * Method used to get the mute information of this related portal.
     *
     * @return the mute punishment.
     */
    public final Mute getMute() {
        if (mute == null) {
            mute = new Mute(pidn);
        }
        return (Mute) mute;
    }

    /**
     * Method used to get the ban information of this related portal.
     *
     * @return the ban.
     */
    public final Ban getBan() {
        if (ban == null) {
            ban = new Ban(pidn);
        }
        return (Ban) ban;
    }

    /**
     * Checks if the player is a member.
     *
     * @return <code>True</code> if so.
     */
    public boolean isMember() {
        return member;
    }

    /**
     * Sets there member value.
     *
     * @param member if a memebr.
     */
    public void setMember(boolean member) {
        this.member = member;
    }

    /**
     * Gets the messages.
     *
     * @return The messages.
     */
    public int getMessages() {
        return messages;
    }

    /**
     * Sets the messages.
     *
     * @param messages The messages to set.
     */
    public void setMessages(int messages) {
        this.messages = messages;
    }

}
