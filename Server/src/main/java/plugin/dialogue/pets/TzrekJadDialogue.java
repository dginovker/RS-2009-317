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
public class TzrekJadDialogue extends DialoguePlugin {

    public TzrekJadDialogue() {
    }

    private TzrekJadDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new TzrekJadDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];

        if (npc instanceof Familiar) {
            Familiar familiar = (Familiar) npc;
            if (familiar.getOwner() != player) {
                player.getActionSender().sendMessage("He doesn't seem interested in talking to you.");
                return true;
            }
        } else {
            player.getActionSender().sendMessage("He doesn't seem interested in talking to you.");
            return true;
        }

        int roll = RandomUtil.random(40);
        if (roll < 12) {
            player("Do you miss your people?");
            stage = 0;
        } else if (roll < 40) {
            player("Are you hungry?");
            stage = 5;

        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("Mej-TzTok-Jad Kot-Kl! (TzTok-Jad will protect us!)");
                stage = 1;
                break;
            case 1:
                player("No.. I don't think so.");
                stage = 2;
                break;
            case 2:
                npc("Jal-Zek Kl? (Foreigner hurt us?)");
                stage = 3;
                break;
            case 3:
                player("No, no, I wouldn't hurt you.");
                stage = 0xBABE;
                break;
            /* Dialogue 2 */
            case 5:
                npc("Kl-Kra!");
                stage = 6;
                break;
            case 6:
                player("Ooookay...");
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
        return new int[]{ 8601 };
    }

}
