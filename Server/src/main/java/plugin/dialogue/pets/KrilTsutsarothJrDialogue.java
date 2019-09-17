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
public class KrilTsutsarothJrDialogue extends DialoguePlugin {

    public KrilTsutsarothJrDialogue() {
    }

    private KrilTsutsarothJrDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new KrilTsutsarothJrDialogue(player);
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
                npc("Burns slightly.");
                stage = 1;
                break;

            case 1:
                player("You seem much nicer than your father. He's mean.");
                stage = 2;
                break;
            case 2:
                npc("If you were stuck in a very dark cave for centuries", "you'd be pretty annoyed too.");
                stage = 3;
                break;
            case 3:
                player(" I guess.");
                stage = 4;
                break;
            case 4:
                npc("He's actually quite mellow really.");
                stage = 5;
                break;
            case 5:
                player("Uh.... Yeah.");
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
            8592
        };
    }

}
