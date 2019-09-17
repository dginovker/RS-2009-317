package org.gielinor.game.content.skill.member.summoning;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;

/**
 * Represents a {@link Conversation} to have with a {@link Familiar}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class Conversation {

    /**
     * The {@link ConversationMessage}.
     */
    private final ConversationMessage[] conversationMessages;

    /**
     * Creates the {@link Conversation} to have with a {@link Familiar}.
     *
     * @param conversationMessages The {@link ConversationMessage}.
     */
    public Conversation(ConversationMessage... conversationMessages) {
        this.conversationMessages = conversationMessages;
    }

    /**
     * The {@link ConversationMessage}.
     *
     * @return The messages.
     */
    public ConversationMessage[] getConversationMessages() {
        return conversationMessages;
    }

    /**
     * Represents a {@link ConversationMessage} message.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public static class ConversationMessage {

        /**
         * Whether or not this is a player chat message.
         */
        private final boolean player;
        /**
         * The text of the message.
         */
        private final String[] text;

        /**
         * Creates a new {@link ConversationMessage}.
         */
        public ConversationMessage(boolean player, String... text) {
            this.player = player;
            this.text = text;
        }

        /**
         * Gets whether or not this is a player chat message.
         *
         * @return <code>True</code> if so.
         */
        public boolean isPlayer() {
            return player;
        }

        /**
         * Gets the {@code text} of the message.
         *
         * @return The {@code text}.
         */
        public String[] getText() {
            return text;
        }
    }
}
