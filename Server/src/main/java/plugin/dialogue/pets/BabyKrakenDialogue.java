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
public class BabyKrakenDialogue extends DialoguePlugin {

    public BabyKrakenDialogue() {
    }

    private BabyKrakenDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BabyKrakenDialogue(player);
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
            player("What's Kraken?");
            stage = 0;

        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                npc("Not heard that one before.");
                stage = 1;
                break;

            case 1:
                player("How are you actually walking on land?");
                stage = 2;
                break;
            case 2:
                npc("We have another leg, just below the center of our body", "that we use to move across solid surfaces.");
                stage = 3;
                break;
            case 3:
                player("That's.... interesting.");
                stage = 4;
                break;
            case 4:
                npc("Please, let me go back to my duties now...");
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
            26640
        };
    }

}
