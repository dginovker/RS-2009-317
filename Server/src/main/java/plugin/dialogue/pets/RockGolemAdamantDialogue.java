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
public class RockGolemAdamantDialogue extends DialoguePlugin {

    public RockGolemAdamantDialogue() {
    }

    private RockGolemAdamantDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new RockGolemAdamantDialogue(player);
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
            npc("I may be green, but I'm not an environmentalist.");
            stage = 0;

        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            /* Dialogue 1 */
            case 0:
                player("Why not?");
                stage = 1;
                break;
            case 1:
                npc("There's no need. Whatever you may have read,", "even coal is a renewable energy source - just", "wait a minute and the rocks respawn.");
                stage = 2;
                break;
            case 2:
                npc("You can burn as much as you like, too,", "without needing to worry about it affecting", "the climate - we don't have a lot of weather here.");
                stage = 3;
                break;
            case 3:
                player("That's handy.");
                stage = 4;
                break;
            case 4:
                player("Yes, I pity anyone whose world doesn't work like this one.", "I don't know how they can possibly cope.");
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
            27449
        };
    }

}
