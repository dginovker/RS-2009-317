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
public class SkotosDialogue extends DialoguePlugin {

    public SkotosDialogue() {
    }

    private SkotosDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new SkotosDialogue(player);
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

        int roll = RandomUtil.random(85);
        if (roll < 12) {
            player("You look cute.");
            stage = 0;
        } else if (roll < 40) {
            player("Where did you come from?");
            stage = 5;
        } else if (roll < 85) {
            player("What can you do for me?");
            stage = 12;

        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc(": I do not thinke thou understand the depths of the", "darkness you have unleased upon the world.", "To dub it in such a scintillant manner", "is offensive to mine being.");
                stage = 1;
                break;
            case 1:
                player("So why are you following me around.");
                stage = 2;
                break;
            case 2:
                npc("Dark forces of which ye know nought", "have deemed that this is my geas.");
                stage = 3;
                break;
            case 3:
                player("Your goose?");
                stage = 4;
                break;
            case 4:
                npc("*Sighs* Nae. But thine is well", "and truly cooked.");
                stage = 0xBABE;
                break;

            /* Dialogue 2 */
            case 5:
                npc("I am spawned of darkness.", "I am filled with darkness.", "I am darkness incarnate and", "to darkness I will return.");
                stage = 6;
                break;

            case 6:
                player("Sounds pretty... dark.");
                stage = 7;
                break;

            case 7:
                npc("*Knowest thou not of the cursed place?", "Knowest thou not about the future yet", "to befall your puny race?");
                stage = 8;
                break;

            case 8:
                player("Oh yes, I've heard that before.");
                stage = 9;
                break;
            case 9:
                npc("Then it is good that ye can", "laugh in the face of the end.");
                stage = 10;
                break;
            case 10:
                player("The end has a face? Which end?");
                stage = 11;
                break;
            case 11:
                npc("*Sighs* The darkness giveth, and the darkness taketh.");
                stage = 0xBABE;
                break;
            /* Dialogue 3 */
            case 12:
                npc("Nothing.", "Ye are already tainted in my sight by the acts of light.", "However they may be some hope for you if you", "continue to aid the darkness.");
                stage = 13;
                break;
            case 13:
                player("I do have a lantern around here somewhere.");
                stage = 14;
                break;
            case 14:
                npc("Do not bring that foul and", "repellant thing near mine self.");
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
        return new int[]{ 27671 };
    }

}
