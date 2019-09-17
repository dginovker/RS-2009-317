package plugin.dialogue.pets;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * @author Erik
 */
public class RockyDialogue extends DialoguePlugin {

    public RockyDialogue() {
    }

    private RockyDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new RockyDialogue(player);
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

        int roll = RandomUtil.random(128);
        if (roll < 30) {
            player("*Whistles*");
            stage = 0;
        } else if (roll < 90) {
            player("Is there much competition between you raccoons and the magpies?");
            stage = 5;
        } else/*(roll < 128)*/ {
            player("Hey Rocky, do you want to commit a bank robbery with me?");
            stage = 10;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                interpreter.sendPlaneMessage("You slip your hand into Rocky's pocket.");
                stage = 1;
                break;

            case 1:
                npc("OY!! You're going to have to do better than that! Sheesh, what an amateur.");
                stage = 0xBABE;
                break;


            /* Dialouge 2 */
            case 5:
                npc("Magpies have nothing on us! They're just interested in shinies.");
                stage = 6;
                break;
            case 6:
                npc("Us raccoons have a finer taste, we can see the value in anything, whether it shines or not.");
                stage = 0xBABE;
                break;

            /* Dialogue 3 */
            case 10:
                npc("If that is the level you are at, I do not wish to participate in criminal acts with you " + player.getName() + ".");
                stage = 11;
                break;
            case 11:
                player("Well what are you interested in stealing?");
                stage = 12;
                break;
            case 12:
                npc("The heart of a lovely raccoon called Rodney.");
                stage = 13;
            case 13:
                player("I cannot really help you there I'm afraid.");
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
            27336
        };
    }

}
