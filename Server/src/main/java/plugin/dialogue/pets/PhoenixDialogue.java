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
public class PhoenixDialogue extends DialoguePlugin {

    public PhoenixDialogue() {
    }

    private PhoenixDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new PhoenixDialogue(player);
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

        int roll = RandomUtil.random(95);
        if (roll < 12) {
            player("So... The Pyromancers, they're cool, right?");
            stage = 0;
        } else if (roll < 40) {
            npc("...");
            stage = 5;
        } else if (roll < 85) {
            player("Who's a pretty birdy?");
            stage = 11;
        } else if (roll < 95) {
            player("One day I will burn so hot I'll become Sacred Ash");
            stage = 15;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("We share a common goal..");
                stage = 1;
                break;
            case 1:
                player("Which is?");
                stage = 2;
                break;
            case 2:
                npc("Keeping the cinders burning and preventing", "the long night from swallowing us all.");
                stage = 3;
                break;
            case 3:
                player("That sounds scary.");
                stage = 4;
                break;
            case 4:
                npc("As long as we remain vigilant and praise the Sun,", "all will be well.");
                stage = 0xBABE;
                break;



            /* Dialogue 2 */
            case 5:
                player("What are you staring at?");
                stage = 6;
                break;
            case 6:
                npc("The great Sol Supra.");
                stage = 7;
                break;
            case 7:
                player("Is that me?");
                stage = 8;
                break;
            case 8:
                npc("No mortal. The Sun, as you would say.");
                stage = 9;
                break;
            case 9:
                player("Do you worship it?");
                stage = 10;
                break;
            case 10:
                npc("It is wonderous...", "If only I could be so grossly incandescent.");
                stage = 0xBABE;
                break;

            /* Dialogue 3 */
            case 11:
                interpreter.sendPlaneMessage("The Phoenix Gives you a smouldering look.");
                stage = 0xBABE;
                break;

            /* Dialogue 4 */
            case 15:
                player("Aww, but you're so rare, where would I find another?");
                stage = 16;
                break;
            case 16:
                npc("Do not fret mortal,", "I will rise from the Sacred Ash greater than ever before.");
                stage = 17;
                break;
            case 17:
                player("So you're immortal?");
                stage = 18;
                break;
            case 18:
                npc("As long as the Sun in the sky gives me strength.");
                stage = 20;
                break;
            case 20:
                player("...Sky?");
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
        return new int[]{ 27368 };
    }

}
