package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.model.container.impl.Equipment;

/**
 * Represents the dialogue plugin used for the del monty npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class DelMontyDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code DelMontyDialogue} {@code Object}.
     */
    public DelMontyDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code DelMontyDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public DelMontyDialogue(Player player) {
        super(player);
    }

    /**
     * Method used to check if a player has a cat speak amulet.
     *
     * @param player the player.
     * @return {@code True} so.
     */
    public static boolean hasCatAmulet(Player player) {
        Item item = player.getEquipment().get(Equipment.SLOT_AMULET);
        if (item == null) {
            return false;
        }
        return item.getId() == 4677 || item.getId() == 6544;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new DelMontyDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        player("Hey kitty!");
        stage = 1;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                end();
                break;
            case 1:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hiss!");
                stage = 0;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 5563 };
    }

}
