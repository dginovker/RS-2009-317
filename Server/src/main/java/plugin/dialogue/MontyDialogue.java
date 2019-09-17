package plugin.dialogue;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for Monty the Collector.
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class MontyDialogue extends DialoguePlugin {

    /**
     * Constructs a new <code>MontyDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     */
    public MontyDialogue() {
    }

    /**
     * Constructs a new <code>MontyDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     *
     * @param player The player.
     */
    public MontyDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new MontyDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        player("Hello!");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                npc("Hello there, " + TextUtils.formatDisplayName(player.getUsername()) + ".", "What can I do for you?");
                stage = 1;
                break;
            case 1:
                options("Who are you?", "What are loyalty titles?", "Nothing.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        player("Who are you?");
                        stage = 3;
                        break;
                    case THREE_OPTION_TWO:
                        player("What are loyalty titles?");
                        stage = 5;
                        break;
                    case THREE_OPTION_THREE:
                        stage = END;
                        player("Nothing.");
                        break;
                }
                break;
            case 3:
                npc("My name is Monty. I control loyalty titles for players, when I", "have new titles to sell, they will be in my shop", "which you can look at by using the \"Open\" option on me.");
                stage = 4;
                break;
            case 4:
                options("Who are you?", "What are loyalty titles?", "Nothing.");
                stage = 2;
                break;
            case 5:
                npc("Loyalty titles are, primarily descriptions for players.", "They can be purchased and enabled through my \"Open\" option,", "when enabled they will show next to a player's name in", "the chat, in private messages, and the context menu.");
                stage = 6;
                break;
            case 6:
                player("How do I purchase a loyalty title?");
                stage = 7;
                break;
            case 7:
                npc("Once you have opened my shop, you will be able to see", "how much loyalty titles cost and what currency they use.", "You can earn the currencies in various ways throughout the server.");
                stage = 8;
                break;
            case 8:
                player("Can I purchase more than one loyalty title?");
                stage = 9;
                break;
            case 9:
                npc("Yes, you can purchase unlimited loyalty titles.", "Once you have purchased a loyalty title, it is yours forever!", "You can have one loyalty title enabled on your account at a time.");
                stage = 10;
                break;
            case 10:
                npc("Is there anything else I can help you with?");
                stage = 11;
                break;
            case 11:
                options("Who are you?", "Show me your loyalty title shop, please.", "No thank you.");
                stage = 12;
                break;
            case 12:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        player("Who are you?");
                        stage = 3;
                        break;
                    case THREE_OPTION_TWO:
                        player("I would like to see your loyalty title shop, please.");
                        stage = 13;
                        break;
                    case THREE_OPTION_THREE:
                        stage = END;
                        player("No thank you.");
                        break;
                }
                break;
            case 13:
                end();
                player.getInterfaceState().open(new Component(23035));
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 3092 };
    }
}
