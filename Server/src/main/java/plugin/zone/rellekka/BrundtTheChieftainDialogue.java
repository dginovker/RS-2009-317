package plugin.zone.rellekka;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.utilities.string.TextUtils;

/**
 * The dialogue plugin for Brundt the Chieftain.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class BrundtTheChieftainDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@link BrundtTheChieftainDialogue} {@link DialoguePlugin}.
     */
    public BrundtTheChieftainDialogue() {
    }

    /**
     * Constructs a new {@link BrundtTheChieftainDialogue} {@link DialoguePlugin}.
     *
     * @param player The player.
     */
    public BrundtTheChieftainDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BrundtTheChieftainDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        //		if (player.getQuestRepository().getQuest("Lunar Diplomacy").getStage() < 20) {
        //			interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hi there, Brundt the Chieftain.");
        //			stage = 0;
        //			return true;
        //		}
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hi there, Brundt the Chieftain.", "Could you help me out with a Seal of Passage?");
        stage = 1;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                npc("Hello again, " + TextUtils.formatDisplayName(player.getName()) + ".");
                stage = END;
                break;
            case 1:
                npc("A seal of passage?", "Whatever for?");
                stage = 2;
                break;
            case 2:
                player("Well, I've heard that there have been great troubles", "between your people and those of the Moon Clan. I was", "hoping to help, so I need a Seal of Passage to", "show my intentions.");
                stage = 3;
                break;
            case 3:
                npc("Ah, " + TextUtils.formatDisplayName(player.getName()) + ", many have tried, but the Moon Clan are by", "no means a sharing group of people. They do", "everything by magic, which is something we never use. But they", "are so secretive about it - if only they would share, we");
                stage = 5;
                break;
            case 5:
                npc("wouldn't have to kill them all the time!");
                stage = 6;
                break;
            case 6:
                player("I will see what I can do.");
                stage = 7;
                break;
            case 7:
                player("Can I have the seal then?");
                stage = 8;
                break;
            case 8:
                if (player.getInventory().contains(9083)) {
                    npc("You seem to already have one in your inventory.");
                    stage = END;
                    break;
                }
                if (player.getEquipment().contains(9083)) {
                    npc("You seem to already have one in your equipment.");
                    stage = END;
                    break;
                }
                if (player.getBank().contains(9083)) {
                    npc("You seem to already have one in your bank.");
                    stage = END;
                    break;
                }
                if (!player.getInventory().hasRoomFor(new Item(9083))) {
                    npc("You don't have any room in your inventory.");
                    stage = END;
                    break;
                }
                npc("If you are to bring peace to our two warring clans,", "then perhaps it is best that you do...");
                player.getInventory().add(new Item(9083));
                stage = END;
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 1294 };
    }
}
