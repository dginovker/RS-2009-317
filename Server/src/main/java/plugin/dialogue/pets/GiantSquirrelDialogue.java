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
public class GiantSquirrelDialogue extends DialoguePlugin {

    public GiantSquirrelDialogue() {
    }

    private GiantSquirrelDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GiantSquirrelDialogue(player);
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
            player("So how come you are so agile?");
            stage = 0;
        } else if (roll < 90) {
            player("What's up with all that squirrel fur?", "I guess fleas need a home too.");
            stage = 5;
        } else/*(roll < 128)*/ {
            player("Did you ever notice how big squirrels' teeth are?");
            stage = 10;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("If you were so nutty about nuts, maybe you", "would understand the great lengths we go to!");
                stage = 0xBABE;
                break;



            /* Dialouge 2 */
            case 5:
                npc("You're pushing your luck!", "Stop it or you'll face my squirrely wrath.");
                stage = 0xBABE;
                break;

            /* Dialogue 3 */
            case 10:
                npc("No..");
                stage = 11;
                break;

            case 11:
                player("You could land a gnome glider on those things!");
                stage = 12;
                break;

            case 12:
                npc("Watch it, I'll crush your nuts!");
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
            27334
        };
    }

}
