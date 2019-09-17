package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

import plugin.interaction.item.withitem.KrakenTentaclePlugin;

/**
 * Represents the {@link DialoguePlugin} for the Abyssal tentacle actions.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public class AbyssalTentacleDialogue extends DialoguePlugin {

    /**
     * Represents the Abyssal tentacle item.
     */
    private static final Item ABYSSAL_TENTACLE = new Item(13272, 1);

    /**
     * The Abyssal tentacle {@link Item}.
     */
    private Item abyssalTentacle;
    /**
     * The Kraken tentacle {@link Item}.
     */
    private Item krakenTentacle;

    /**
     * Constructs a new {@link AbyssalTentacleDialogue} {@link DialoguePlugin}.
     */
    public AbyssalTentacleDialogue() {
    }

    /**
     * Constructs a new {@link AbyssalTentacleDialogue} {@link DialoguePlugin}.
     *
     * @param player The player.
     */
    public AbyssalTentacleDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new AbyssalTentacleDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        if (args.length == 1) {
            abyssalTentacle = (Item) args[0];
            interpreter.sendPlaneMessage("<col=8A0808>Warning!", "By dissolving the tentacle, it will consume your whip. You", "will only be left with the kraken tentacle.");
            stage = 0;
            return true;
        }
        // abyssalTentacle = whip in this case
        if (args.length == 2) {
            abyssalTentacle = (Item) args[0];
            krakenTentacle = (Item) args[1];
            interpreter.sendPlaneMessage("<col=8A0808>Warning!", "The tentacle will gradually consume your whip and destroy it. You", "won't be able to get the whip out again.", "The combined item is not tradeable.");
            stage = 10;
            return true;
        }
        end();
        return false;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Are you sure you wish to do this?", "Yes, dissolve the tentacle.", "No, never mind.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        end();
                        for (Item item : player.getInventory().toArray()) {
                            if (item == null) {
                                continue;
                            }
                            if (item.equals(abyssalTentacle)) {
                                if (player.getInventory().remove(item, item.getSlot(), true, false)) {
                                    player.getInventory().add(KrakenTentaclePlugin.KRAKEN_TENTACLE);
                                    return true;
                                }
                            }
                        }
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                end();
                break;
            case 10:
                interpreter.sendOptions("Are you sure you wish to do this?", "Yes, let the tentacle consume the whip.", "No, I'll keep my whip.");
                stage = 11;
                break;
            case 11:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        if (player.getInventory().remove(abyssalTentacle, krakenTentacle)) {
                            ABYSSAL_TENTACLE.setCharge(10000);
                            player.getInventory().add(ABYSSAL_TENTACLE);
                            end();
                            return true;
                        }
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                end();
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("AbyssalTentacle") };
    }
}
