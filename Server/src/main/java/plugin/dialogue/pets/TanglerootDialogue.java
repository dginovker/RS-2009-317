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
public class TanglerootDialogue extends DialoguePlugin {

    public TanglerootDialogue() {
    }

    private TanglerootDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new TanglerootDialogue(player);
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
            player("How are you doing today?");
            stage = 0;
        } else if (roll < 90) {
            player("Hello there pretty plant.");
            stage = 5;
        } else/*(roll < 128)*/ {
            player("I am Tangleroot!");
            stage = 10;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("I am Tangleroot!");
                stage = 0xBABE;
                break;



            /* Dialouge 2 */
            case 5:
                npc("I am Tangleroot!");
                stage = 0xBABE;
                break;

            /* Dialogue 3 */
            case 10:
                npc("I am" + player.getName() + "!");
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
            27335
        };
    }

}
