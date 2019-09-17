package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.member.herblore.GenericPotion;
import org.gielinor.game.content.skill.member.herblore.HerblorePulse;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.utilities.misc.RunScript;

/**
 * Represents the dialogue used to determine the amount of a potion to make.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class HerbloreDialogue extends DialoguePlugin {

    /**
     * Represents this dialogues id.
     */
    public static final int ID = 21947748;

    /**
     * Represents the generic potion to create.
     */
    private GenericPotion potion;

    /**
     * Constructs a new {@code HerbloreDialogue} {@code Object}.
     */
    public HerbloreDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code HerbloreDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public HerbloreDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new HerbloreDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        potion = (GenericPotion) args[0];
        player.getDialogueInterpreter().sendItemSelectDialogue(potion.getProduct().getName().replace("(unf)", ""), potion.getProduct());
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                int amount = optionSelect.getAmount(optionSelect.getChildId());
                if (amount == -1) {
                    player.setAttribute("runscript", new RunScript() {

                        @Override
                        public boolean handle() {
                            int amount = (int) value;
                            player.getPulseManager().run(new HerblorePulse(player, potion.getBase(), amount, potion));
                            end();
                            return false;
                        }
                    });
                    player.getDialogueInterpreter().sendInput(false, "Enter amount:");
                    return true;
                }
                player.getPulseManager().run(new HerblorePulse(player, potion.getBase(), amount, potion));
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ ID };
    }
}
