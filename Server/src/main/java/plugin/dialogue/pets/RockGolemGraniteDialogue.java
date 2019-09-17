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
public class RockGolemGraniteDialogue extends DialoguePlugin {

    public RockGolemGraniteDialogue() {
    }

    private RockGolemGraniteDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new RockGolemGraniteDialogue(player);
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
            npc("No-one appreciates granite.");
            stage = 0;

        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                player("Why do you say that?");
                stage = 1;
                break;
            case 1:
                npc(" I know how it works. No-one actually wants granite.", "They just chop us up and throw our pieces on the floor.");
                stage = 2;
                break;
            case 2:
                player("Oh, I can see that must be upsetting for you.");
                stage = 3;
                break;
            case 3:
                npc("When you've seen your relatives cut into pieces,", "with their severed limbs cast aside like junk,", "THEN you will understand how I feel.");
                stage = 4;
                break;
            case 4:
                player("I'll bear it in mind.");
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
            27448
        };
    }

}
