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
public class CallistoCubDialogue extends DialoguePlugin {

    public CallistoCubDialogue() {
    }

    private CallistoCubDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new CallistoCubDialogue(player);
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
            player("Why the grizzly face?");
            stage = 0;

        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("You're not funny...");
                stage = 1;
                break;

            case 1:
                player("You should get in the.... sun more.");
                stage = 2;
                break;
            case 2:
                npc("You're really not funny...");
                stage = 3;
                break;
            case 3:
                player("One second, let me take a picture of you with my....", "kodiak camera.");
                stage = 4;
                break;
            case 4:
                npc(".....");
                stage = 5;
                break;
            case 5:
                player("Feeling.... blue?");
                stage = 6;
                break;
            case 6:
                npc("If you don't stop, I'm going to leave some...", "brown... at your feet, human.");
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
            20497
        };
    }

}
