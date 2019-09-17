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
public class VetionDialogue extends DialoguePlugin {

    public VetionDialogue() {
    }

    private VetionDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new VetionDialogue(player);
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
            player("Who is the true lord and king of the lands?");
            stage = 0;

        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("The mighty heir and lord of the Wilderness.");
                stage = 1;
                break;

            case 1:
                player("Where is he? Why hasn't he lifted your burden?");
                stage = 2;
                break;
            case 2:
                npc("I have not fulfilled my purpose.");
                stage = 3;
                break;
            case 3:
                player("What is your purpose?");
                stage = 4;
                break;
            case 4:
                npc("Not what is, what was. A great war tore this land apart and,", "for my failings in protecting this land,", "I carry the burden of its waste.");
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
            25559,
            25560
        };
    }

}
