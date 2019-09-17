package plugin.dialogue.pets;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * @author Erik
 * @author Harry
 */
public class KreeDialogue extends DialoguePlugin {

    public KreeDialogue() {
    }

    private KreeDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new KreeDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];

        if (npc instanceof Familiar) {
            Familiar familiar = (Familiar) npc;
            if (familiar.getOwner() != player) {
                player.getActionSender().sendMessage("It doesn't seem interested in talking to you.");
                return true;
            }
        } else {
            player.getActionSender().sendMessage("It doesn't seem interested in talking to you.");
            return true;
        }

        int roll = RandomUtil.random(30);
        if (roll < 30) {
            player("How's life in the light?");
            stage = 0;

        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("You thought what would be big news?");
                stage = 1;
                break;

            case 1:
                player("Well there seems to be an absence of a certain ornithological piece:", "a headline regarding mass awareness of a certain avian variety.");
                stage = 2;
                break;
            case 2:
                npc("What are you talking about?");
                stage = 3;
                break;
            case 3:
                player("Oh have you not heard?", "It was my understanding that everyone had heard....");
                stage = 4;
                break;
            case 4:
                npc("Heard wha...... OH NO!!!!?!?!!?!");
                stage = 5;
                break;
            case 5:
                player("OH WELL THE BIRD, BIRD, BIRD, BIRD BIRD IS THE WORD.", "OH WELL THE BIRD, BIRD, BIRD, BIRD BIRD IS THE WORD.");
                stage = 0xBABE;
                break;


            case 0xBABE:
                end();
                break;
        }

        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{
            8593
        };
    }

}
