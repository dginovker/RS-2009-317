package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for Otto Godblessed.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class OttoGodblessedDialogue extends DialoguePlugin {

    /**
     * The coins item.
     */
    private final Item COINS = new Item(Item.COINS, 300000);

    public OttoGodblessedDialogue() {
    }

    public OttoGodblessedDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new OttoGodblessedDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello, who are you?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                npc("I am Otto Godblessed.");
                stage = 1;
                break;
            case 1:
                if (player.getInventory().contains(11716) || player.getInventory().contains(Item.ZAMORAKIAN_HASTA)) {
                    options("Good talk!", "You could convert my Zamorakian spear into a hasta?", "You could convert my Zamorakian hasta into a spear?");
                    stage = 2;
                    return true;
                }
                player("Good talk!");
                stage = END;
                break;
            case 2:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        player("Good talk!");
                        stage = END;
                        break;
                    case THREE_OPTION_TWO:
                        player("You could convert my Zamorakian spear into a hasta?");
                        stage = 3;
                        break;
                    case THREE_OPTION_THREE:
                        player("You could convert my Zamorakian hasta into a spear?");
                        stage = 7;
                        break;
                }
                break;
            case 3:
                npc("Sure! But it will cost you 300,000 coins!");
                stage = 4;
                break;
            case 4:
                options("Pay 300,000 coins", "No thank you.");
                stage = 5;
                break;
            case 5:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Okay, here's 300,000 coins.");
                        stage = 6;
                        break;
                    case TWO_OPTION_TWO:
                        player("No thank you.");
                        stage = END;
                        break;
                }
                break;
            case 6:
                if (!player.getInventory().contains(11716)) {
                    npc("You need a Zamorakian spear for me to convert.");
                    stage = END;
                    break;
                }
                if (!player.getInventory().contains(COINS)) {
                    player("I'm afraid I don't have that much.");
                    stage = END;
                    break;
                }
                player.getInventory().remove(new Item(11716));
                player.getInventory().remove(COINS);
                player.getInventory().add(new Item(Item.ZAMORAKIAN_HASTA), true);
                interpreter.sendItemMessage(Item.ZAMORAKIAN_HASTA, "Otto Godblessed converts your zamorakian spear into a", "zamorakian hasta!");
                stage = END;
                break;
            case 7:
                npc("Sure!");
                stage = 8;
                break;
            case 8:
                if (!player.getInventory().contains(Item.ZAMORAKIAN_HASTA)) {
                    npc("You need a Zamorakian hasta for me to convert.");
                    stage = END;
                    break;
                }
                player.getInventory().remove(new Item(Item.ZAMORAKIAN_HASTA));
                player.getInventory().add(new Item(11716), true);
                interpreter.sendItemMessage(Item.ZAMORAKIAN_HASTA, "Otto Godblessed converts your zamorakian hasta into a", "zamorakian spear!");
                stage = END;
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2725 };
    }
}
