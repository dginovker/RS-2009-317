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
public class BloodhoundDialogue extends DialoguePlugin {

    public BloodhoundDialogue() {
    }

    private BloodhoundDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BloodhoundDialogue(player);
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
            player("Walkies!");
            stage = 0;
        } else if (roll < 40) {
            player("How come I can talk to you without an amulet?");
            stage = 5;
        } else if (roll < 85) {
            player("Can you help me with this clue?");
            stage = 10;
        } else if (roll < 95) {
            player("I wonder if I could sell you to a vampire to track down dinner.");
            stage = 15;
        } else/*(roll < 128)*/ {
            player("Hey boy, what's up?");
            stage = 20;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("...");
                stage = 0xBABE;
                break;

            /* Dialogue 2 */
            case 5:
                npc("*Woof woof bark!*", "Elementary, it's due to the influence of the SQUIRREL!");
                stage = 0xBABE;
                break;

            /* Dialogue 3 */
            case 10:
                npc("*Woof! Bark yip woof!*", "Sure! Eliminate the impossible first.");
                stage = 11;
                break;

            case 11:
                player("And then?");
                stage = 12;
                break;

            case 12:
                npc("*Bark! Woof bark bark.*", "Whatever is left, however improbable, must be the answer.");
                stage = 13;
                break;

            case 13:
                player("So helpful.");
                stage = 0xBABE;
                break;

            /* Dialogue 4 */
            case 15:
                npc("*Woof bark bark woof*", "I have teeth too you know, that joke was not funny.");
                stage = 0xBABE;
                break;

            /* Dialogue 5 */
            case 20:
                npc("*Wooof! Bark bark woof!*", "You smell funny.");
                stage = 21;
                break;

            case 21:
                player("Err... funny strange or funny ha ha?");
                stage = 22;
                break;

            case 22:
                npc("*Bark bark woof!*", "You aren't funny.");
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
        return new int[]{ 26296 };
    }

}
