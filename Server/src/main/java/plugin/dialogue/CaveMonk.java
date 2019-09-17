package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.rs2.model.container.Container;

/**
 * Represents the dialogue plugin used for a cave monk.
 *
 * @author 'Vexia
 * @author Logan G. <logan@Gielinor.org>
 */
public final class CaveMonk extends DialoguePlugin {

    /**
     * Represents the dungeon location.
     */
    private static final Location DUNGEON = Location.create(2822, 9774, 0);

    /**
     * Constructs a new {@code CaveMonk} {@code Object}.
     */
    public CaveMonk() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code CaveMonk} {@code Object}.
     *
     * @param player the player.
     */
    public CaveMonk(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new CaveMonk(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Be careful going in there! You are unarmed, and there", "is much evilness lurking down there! The evilness seems", "to block off our contact with our gods,");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {

            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "so our prayers seem to have less effect down there. Oh,", "also, you won't be able to come back this way - This", "ladder only goes one way!");
                stage = 1;
                break;
            case 1:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "The only exit from the caves below is a portal which", "leads only to the deepest wilderness!");
                stage = 2;
                break;
            case 2:
                interpreter.sendOptions("Select an Option", "I don't think I'm strong enough to enter then.", "Well that is a risk I will have to take.");
                stage = 3;
                break;
            case 3:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I don't think I'm strong enough to enter then.");
                        stage = 10;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Well that is a risk I will have to take.");
                        stage = 20;
                        break;
                }
                break;
            case 10:
                end();
                break;
            case 20:
                Container[] container = new Container[]{ player.getInventory(), player.getEquipment() };
                for (Container c : container) {
                    for (Item i : c.toArray()) {
                        if (i == null) {
                            continue;
                        }
                        if (hasBonuses(i)) {
                            player.getDialogueInterpreter().sendPlaneMessage("You cannot take any weapons or armour down there.");
                            stage = 10;
                            return true;
                        }
                    }
                }
                player.setTeleportTarget(DUNGEON);
                end();
                break;
        }
        return true;
    }

    /**
     * Method used to check if an item has bonuses.
     *
     * @param item the item.
     * @return {@code True} if so.
     */
    private boolean hasBonuses(Item item) {
        return item.getDefinition().getConfiguration(ItemConfiguration.BONUS) != null;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 656 };
    }
}
