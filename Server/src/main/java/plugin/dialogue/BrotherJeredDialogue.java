package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.Skillcape;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the BrotherJeredDialogue dialogue.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class BrotherJeredDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code BrotherJeredDialogue} {@code Object}.
     */
    public BrotherJeredDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code BrotherJeredDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public BrotherJeredDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BrotherJeredDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (Skillcape.isMaster(player, Skills.PRAYER)) {
            player("Can I buy a Skillcape of Prayer?");
            stage = 2;
        } else {
            player("Praise to Saradomin!");
            stage = 0;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                npc("Yes! Praise he who brings life to this world.");
                stage = 1;
                break;
            case 1:
                end();
                break;
            case 2:
                npc("Certainly! Right when you give me 99000 coins.");
                stage = 3;
                break;
            case 3:
                options("Okay, here you go.", "No");
                stage = 4;
                break;
            case 4:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Okay, here you go.");
                        stage = 5;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
            case 5:
                if (Skillcape.purchase(player, Skills.PRAYER)) {
                    npc("There you go! Enjoy.");
                }
                stage = 6;
                break;
            case 6:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 802 };
    }
}
