package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;

/**
 * Represents the dialogue plugin used for the duke horacio.
 *
 * @author 'Vexia
 */
public final class DukeHoracioDialogue extends DialoguePlugin {

    /**
     * Represents the air talisman item.
     */
    private static final Item TALISMAN = new Item(1438);

    /**
     * Constructs a new {@code DukeHoracioDialogue} {@code Object}.
     */
    public DukeHoracioDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code DukeHoracioDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public DukeHoracioDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 741 };
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 1:
                player("I seek a shield that will protect me from dragonbreath.");
                stage = 2;
                break;
            case 2:
                npc("A knight going on a dragon quest, hmm? What dragon", "do you intend to slay?");
                stage = 3;
                break;
            case 3:
                player("Oh, no dragon in particular. I just feel like killing a", "dragon.");
                stage = 4;
                break;
            case 4:
                if (player.hasItem(new Item(1540))) {
                    npc("But you already have one!");
                    stage = 6;
                    break;
                }
                npc("Of course.");
                stage = 5;
                break;
            case 5:
                if (!player.getInventory().add(new Item(1540))) {
                    GroundItemManager.create(new Item(1540), player);
                }
                interpreter.sendItemMessage(new Item(1540), "The Duke hands you the shield.");
                stage = 6;
                break;
            case 6:
                end();
                break;
        }
        return true;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new DukeHoracioDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Greetings. Welcome to my castle.");
        stage = 1;
        return true;
    }

}
