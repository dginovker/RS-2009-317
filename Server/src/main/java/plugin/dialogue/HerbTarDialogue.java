package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.member.herblore.HerbTarPulse;
import org.gielinor.game.content.skill.member.herblore.Tars;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.utilities.misc.RunScript;

/**
 * Represents the dialogue used to determine the amount of a herb tar to make.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class HerbTarDialogue extends DialoguePlugin {

    /**
     * Represents this dialogues id.
     */
    public static final int ID = 2827673;

    /**
     * Represents the tar to make.
     */
    private Tars tar;

    /**
     * Constructs a new {@code HerbloreDialogue} {@code Object}.
     */
    public HerbTarDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code HerbloreDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public HerbTarDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new HerbTarDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        tar = (Tars) args[0];
        player.getDialogueInterpreter().sendItemSelectDialogue(tar.getTar());
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                int amount = optionSelect.getAmount(optionSelect.getChildId());
                System.out.println("Amt: " + amount);
                // OptionSelect.ONE_ITEM_SELECT_ONE.amountForId(optionSelect.getId());
                if (amount == -1) {
                    player.setAttribute("runscript", new RunScript() {

                        @Override
                        public boolean handle() {
                            int amount = (int) value;
                            player.getPulseManager().run(new HerbTarPulse(player, null, tar, amount));
                            end();
                            return false;
                        }
                    });
                    player.getDialogueInterpreter().sendInput(false, "Enter amount:");
                    return true;
                }
                player.getPulseManager().run(new HerbTarPulse(player, null, tar, amount));
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
