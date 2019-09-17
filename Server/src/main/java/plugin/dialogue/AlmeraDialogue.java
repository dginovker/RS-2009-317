package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Handles Almera's dialogue.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public final class AlmeraDialogue extends DialoguePlugin {

    public AlmeraDialogue() {
    }

    public AlmeraDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new AlmeraDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npc("Hello, what can I help you with?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Ask about raft", "Nothing");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Could I use your raft to travel down the waterfall?");
                        stage = 2;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
            case 2:
                if (player.getSavedData().getGlobalData().hasPaidWaterfall()) {
                    npc("Of course you can.");
                    stage = 20;
                    break;
                }
                npc("You can for a one time payment of 50000 gold coins.");
                stage = 3;
                break;
            case 3:
                interpreter.sendOptions("Select an Option", "Pay 50000 coins", "No thank you.");
                stage = 4;
                break;
            case 4:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        if (player.getInventory().contains(new Item(Item.COINS, 50000))) {
                            player("Okay, I'll pay 50000 coins.");
                            stage = 5;
                            break;
                        }
                        player("I'm afraid I don't have that much money on me.");
                        stage = 6;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
            case 5:
                if (player.getInventory().remove(new Item(Item.COINS, 50000))) {
                    player.getSavedData().getGlobalData().setWaterfallPaid();
                    interpreter.sendItemMessage(new Item(Item.COINS, 50000), "You give 50000 coins to Almera to use the raft.");
                    stage = 7;
                    break;
                }
                end();
                break;
            case 6:
                npc("Well, come back when you have it!");
                stage = 20;
                break;
            case 7:
                npc("Thank you. You can now board the raft and travel down", "the waterfall anytime you want to!");
                stage = 20;
                break;
            case 20:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 304 };
    }
}
