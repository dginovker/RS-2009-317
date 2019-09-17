package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

import plugin.interaction.inter.EmoteTabInterface.Emote;


/**
 * Represents the grain of plenty dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class GrainOfPlenty extends DialoguePlugin {

    /**
     * Represents the coins item.
     */
    private static final Item COINS = new Item(Item.COINS, 3000);

    /**
     * Constructs a new {@code GrainOfPlenty} {@code Object}.
     */
    public GrainOfPlenty() {
    }

    /**
     * Constructs a new {@code GrainOfPlenty} {@code Object}.
     *
     * @param player the player.
     */
    public GrainOfPlenty(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GrainOfPlenty(player);
    }

    @Override
    public boolean open(Object... args) {
        interpreter.sendPlaneMessage("The grain shifts in the sack, sighing audible words....");
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
                player.getSavedData().getGlobalData().getStrongHoldRewards()[1] = true;
                interpreter.sendPlaneMessage("...congratualtions adventurer, you have been deemed worthy of this", "reward. You have also unlocked the Slap Head emote!");
                stage = 1;
                player.getInventory().add(COINS);
                player.getEmotes().unlock(Emote.SLAP_HEAD);
                break;
            case 1:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 56875 };
    }

}
