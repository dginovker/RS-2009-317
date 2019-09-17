package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the ghost sailor dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class GhostSailorDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code GhostSailorDialogue} {@code Object}.
     */
    public GhostSailorDialogue() {
    }

    /**
     * Constructs a new {@code GhostSailorDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public GhostSailorDialogue(Player player) {
        super(player);
    }

    private boolean hasGhostspeakAmulet() {
        Item amulet = player.getEquipment().get(Equipment.SLOT_AMULET);
        return amulet != null && amulet.getId() == Item.GHOSTSPEAK_AMULET;
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GhostSailorDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (hasGhostspeakAmulet()) {
            interpreter.sendDialogues(npc, FacialExpression.EYEBROWS_UP, "Welcome to Port Phasmatys, " + player.getName() + ".");
            stage = 5;
        } else {
            interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Woooo wooo wooooo woooo");
            stage = 0;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendPlaneMessage("You cannot understand the ghost.");
                stage = 10;
                break;
            case 5:
                player("Is everyone around here...");
                stage = 6;
                break;
            case 6:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Dead?");
                stage = 7;
                break;
            case 7:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I am afraid so.");
                stage = 8;
                break;
            case 8:
                interpreter.sendDialogues(player, FacialExpression.DIDNT_DO_IT, "I'm sorry.");
                stage = RandomUtil.random(128) < 20 ? 9 : 10;
                break;
            case 9:
                interpreter.sendDialogues(npc, FacialExpression.ANGRY, "YOU did this to us?!");
                stage = 10;
                break;
            case 10:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 1703, 1704 };
    }

}
