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
public class RiftGuardianDialogue extends DialoguePlugin {

    public RiftGuardianDialogue() {
    }

    private RiftGuardianDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new RiftGuardianDialogue(player);
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
            player("Where would you like me to take you today Rifty?");
            stage = 0;
        } else if (roll < 90) {
            player("Can you see your own rift?");
            stage = 5;
        } else/*(roll < 128)*/ {
            player("Hey! What's that!");
            stage = 10;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("Please do not call me that...", "We are a species of honour, " + player.getName() + ".");
                stage = 1;
                break;

            case 1:
                player("Sorry.");
                stage = 0xBABE;
                break;

            /* Dialouge 2 */
            case 5:
                npc("No. From time to time I feel it shift and change inside", "me though. It is an odd feeling.");
                stage = 0xBABE;
                break;

            /* Dialogue 3 */
            case 10:
                interpreter.sendPlaneMessage("You quickly poke your hand through the rift guardian's rift.");
                stage = 11;
                break;

            case 11:
                npc("Huh, what?! Where?");
                stage = 12;
                break;

            case 12:
                player("Not the best guardian it seems.");
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
            27337,
            27738,
            27339,
            27340,
            27341,
            27342,
            27343,
            27344,
            27345,
            27346,
            27347,
            27348,
            27349,
            27350,
            27354,
            27355,
            27356,
            27357,
            27358,
            27359,
            27360,
            27361,
            27362,
            27363,
            27364,
            27365,
            27366,
            27367
        };
    }

}
