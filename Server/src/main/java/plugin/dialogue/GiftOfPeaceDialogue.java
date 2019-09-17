package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

import plugin.interaction.inter.EmoteTabInterface.Emote;

/**
 * Represents the gift of peace dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class GiftOfPeaceDialogue extends DialoguePlugin {

    /**
     * Represents the coins item.
     */
    private static final Item COINS = new Item(Item.COINS, 2000);

    /**
     * Constructs a new {@code GiftOfPeaceDialogue} {@code Object}.
     */
    public GiftOfPeaceDialogue() {
    }

    /**
     * Constructs a new {@code GiftOfPeaceDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public GiftOfPeaceDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GiftOfPeaceDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        interpreter.sendPlaneMessage("The box hinges creak and appear to be forming audible words....");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                if (player.getInventory().freeSlots() == 0) {
                    player.getActionSender().sendMessage("You don't have enough inventory space.");
                    end();
                    break;
                }
                interpreter.sendPlaneMessage("...congratulations adventurer, you have been deemed worthy of this", "reward. You have also unlocked the Flap emote!");
                stage = 1;
                player.getEmotes().unlock(Emote.FLAP);
                player.getInventory().add(COINS);
                player.getSavedData().getGlobalData().getStrongHoldRewards()[0] = true;
                break;
            case 1:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 54678 };
    }

}
