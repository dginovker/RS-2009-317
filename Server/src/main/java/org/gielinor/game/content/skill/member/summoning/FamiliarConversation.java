package org.gielinor.game.content.skill.member.summoning;

import java.security.SecureRandom;

import org.gielinor.game.content.skill.member.summoning.Conversation.ConversationMessage;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;

/**
 * Represents a {@link Conversation} between a player and their {@link Familiar}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum FamiliarConversation {

    VET_ION_JR(5536, new Conversation(new ConversationMessage(true, "Who is the true lord and king of the lands?")),
        new Conversation(new ConversationMessage(false, "The mighty heir and lord of the Wilderness.")),
        new Conversation(new ConversationMessage(true, "Where is he? Why hasn't he lifted your burden?")),
        new Conversation(new ConversationMessage(false, "I have not fulfilled my purpose.")),
        new Conversation(new ConversationMessage(true, "What is your purpose?")),
        new Conversation(new ConversationMessage(false, "Not what is, what was.", "A great war tore this land apart and, for my", "failings in protecting this land, I carry", "the burden of its waste."))),
    GENERAL_GRAARDOR_JR(6644, new Conversation(new ConversationMessage(true, "Not sure this is going to be worth", "my time but... how are you?"), new ConversationMessage(false, "SFudghoigdfpDSOPGnbSOBNfdbdnopbdnopbddfn", "opdfpofhdARRRGGGGH"), new ConversationMessage(true, "Nope. Not worth it.")));

    /**
     * The id of the {@link Familiar} for this conversation.
     */
    private final int id;

    /**
     * The possible conversations.
     */
    private final Conversation[] conversations;

    /**
     * Creates a new {@link FamiliarConversation}.
     *
     * @param id            The id of the {@link Familiar} for this conversation.
     * @param conversations The possible conversations.
     */
    FamiliarConversation(int id, Conversation... conversations) {
        this.id = id;
        this.conversations = conversations;
    }

    /**
     * Gets the id of the {@link Familiar} for this conversation.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the possible conversations.
     *
     * @return The conversations.
     */
    public Conversation[] getConversations() {
        return conversations;
    }

    /**
     * Gets a random {@link Conversation} for the given {@link Familiar} id.
     *
     * @param id The id of the {@link Familiar}.
     */
    public static Conversation forId(int id) {
        for (FamiliarConversation familiarConversation : FamiliarConversation.values()) {
            if (familiarConversation.getId() == id) {
                if (familiarConversation.getConversations() == null) {
                    return null;
                }
                return familiarConversation.getConversations()[new SecureRandom().nextInt(familiarConversation.getConversations().length)];
            }
        }
        return null;
    }
}
