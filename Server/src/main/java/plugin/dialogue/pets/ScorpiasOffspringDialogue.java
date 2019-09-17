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
public class ScorpiasOffspringDialogue extends DialoguePlugin {

    public ScorpiasOffspringDialogue() {
    }

    private ScorpiasOffspringDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new ScorpiasOffspringDialogue(player);
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
            player("At night time, if I were to hold ultraviolet", "light over you, would you glow?");
            stage = 0;

        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("Two things wrong there, human.");
                stage = 1;
                break;

            case 1:
                player("Oh?");
                stage = 2;
                break;
            case 2:
                npc("One, When has it ever been night time here?");
                stage = 3;
                break;
            case 3:
                npc("Two, When have you ever seen ultraviolet", "light around here?");
                stage = 4;
                break;
            case 4:
                player("Hm...");
                stage = 5;
                break;
            case 5:
                npc("In answer to your question though.", "Yes I, like every scorpion, would glow.");
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
            25561
        };
    }

}
