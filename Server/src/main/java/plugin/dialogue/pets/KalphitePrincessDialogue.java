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
public class KalphitePrincessDialogue extends DialoguePlugin {

    public KalphitePrincessDialogue() {
    }

    private KalphitePrincessDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new KalphitePrincessDialogue(player);
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
            player("What is it with your kind and potato cactus?");
            stage = 0;

        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("Truthfully?");
                stage = 1;
                break;

            case 1:
                player("Yeah, please.");
                stage = 2;
                break;
            case 2:
                npc("Soup. We make a fine soup with it.");
                stage = 3;
                break;
            case 3:
                player("Kalphites can cook?");
                stage = 4;
                break;
            case 4:
                npc("Nah, we just collect it and put it there because we know fools like yourself","will come down looking for it then inevitably be killed by my mother.");
                stage = 5;
                break;
            case 5:
                player("Evidently not, that's how I got you!");
                stage = 6;
                break;
            case 6:
                npc("Touché.");
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
            26637,
            26638
        };
    }

}
