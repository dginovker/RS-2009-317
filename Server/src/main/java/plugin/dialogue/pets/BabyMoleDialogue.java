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
public class BabyMoleDialogue extends DialoguePlugin {

    public BabyMoleDialogue() {
    }

    private BabyMoleDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BabyMoleDialogue(player);
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
            player("Hey, Mole. How is life above ground?");
            stage = 0;

        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("Well, the last time I was above ground,", "I was having to contend with people throwing snow at some", "weird yellow duck in my park.");
                stage = 1;
                break;

            case 1:
                player("Why were they doing that?");
                stage = 2;
                break;
            case 2:
                npc("No idea, I didn't stop to ask as an angry mob", "was closing in on them pretty quickly.");
                stage = 3;
                break;
            case 3:
                player("Sounds awful.");
                stage = 4;
                break;
            case 4:
                npc("Anyway, keep Molin'!");
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
            26635
        };
    }

}
