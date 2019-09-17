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
public class HellpuppyDialogue extends DialoguePlugin {

    public HellpuppyDialogue() {
    }

    private HellpuppyDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new HellpuppyDialogue(player);
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

        int roll = RandomUtil.random(128);
        if (roll < 12) {
            player("How many souls have you devoured?");
            stage = 0;
        } else if (roll < 40) {
            player("I wonder if I need to invest in a trowel", "when I take you out for a walk.");
            stage = 5;
        } else if (roll < 85) {
            player("Why were the hot dogs shivering?");
            stage = 10;
        } else if (roll < 95) {
            player("Hell yeah! Such a cute puppy!");
            stage = 15;
        } else/*(roll < 128)*/ {
            player("What a cute puppy, how nice to meet you.");
            stage = 20;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("None.");
                stage = 1;
                break;
            case 1:
                player("Awww p-");
                stage = 2;
                break;
            case 2:
                npc("Yet.");
                stage = 3;
                break;
            case 3:
                player("Oh.");
                stage = 0xBABE;
                break;
            /* Dialogue 2 */
            case 5:
                npc("More like a shovel.");
                stage = 0xBABE;
                break;

            /* Dialogue 3 */
            case 10:
                npc("Grrrrr...");
                stage = 11;
                break;

            case 11:
                player("Because they were served-");
                stage = 12;
                break;

            case 12:
                npc("GRRRRRR...");
                stage = 13;
                break;

            case 13:
                player(" -with... chilli?");
                stage = 0xBABE;
                break;

            /* Dialogue 4 */
            case 15:
                npc("Silence mortal! Or I'll eat your soul.");
                stage = 16;
                break;
            case 16:
                player("Would that go well with lemon?");
                stage = 17;
                break;
            case 17:
                npc("Grrr...");
                stage = 0xBABE;
                break;
            /* Dialogue 5 */
            case 20:
                npc("It'd be nice to meat you too...");
                stage = 21;
                break;

            case 21:
                player("Urk... nice doggy.");
                stage = 22;
                break;

            case 22:
                npc("Grrr....");
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
        return new int[]{ 20964 };
    }

}
