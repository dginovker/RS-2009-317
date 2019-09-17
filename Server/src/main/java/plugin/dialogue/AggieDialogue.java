package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;

/**
 * Represents the dialogue plugin used for the aggie npc.
 *
 * @author 'Vexia
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class AggieDialogue extends DialoguePlugin {

    /**
     * Represents the animaton used to make the dye with aggie.
     */
    private static final Animation ANIMATION = new Animation(4352);

    /**
     * Represents the cauldron location
     */
    private final static Location CAULDRON_LOCATION = Location.create(3085, 3258, 0);

    /**
     * Represents the coins item.
     */
    private static final Item COINS = new Item(Item.COINS, 5);

    /**
     * Represents the woad leaves item.
     */
    private static final Item WOAD_LEAVES = new Item(1793, 2);

    /**
     * Represents the onions item.
     */
    private static final Item ONIONS = new Item(1957, 2);

    /**
     * Represents the redberries item.
     */
    private static final Item REDBERRIES = new Item(1951, 3);

    /**
     * Represents the blue dye.
     */
    private static final Item BLUE_DYE = new Item(1767);

    /**
     * Represents the yellow dye item.
     */
    private static final Item YELLOW_DYE = new Item(1765);

    /**
     * Represents the red dye item.
     */
    private static final Item RED_DYE = new Item(1763);

    /**
     * Constructs a new {@code AggieDialogue} {@code Object}.
     */
    public AggieDialogue() {
    }

    /**
     * Constructs a new {@code AggieDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public AggieDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new AggieDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendOptions("Select an Option",
            "What do you need to make a red dye?",
            "What do you need to make yellow dye?",
            "What do you need to make blue dye?"
        );
        stage = 42;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 42:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NORMAL, "What do you need to make red dye?");
                        stage = 410;
                        break;
                    case THREE_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NORMAL, "What do you need to make yellow dye?");
                        stage = 420;
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NORMAL, "What do you need to make blue dye?");
                        stage = 430;
                        break;
                }
                break;
            case 430:
                interpreter.sendDialogues(npc, null, "2 woad leaves and 5 coins to you.");
                stage = 431;
                break;
            case 431:
                interpreter.sendDialogues(player, FacialExpression.NORMAL, "Okay, make me some blue dye please.");
                stage = 432;
                break;
            case 432:
                if (player.getInventory().containsItem(COINS) && player.getInventory().containsItem(WOAD_LEAVES)) {
                    player.getInventory().remove(COINS);
                    player.getInventory().remove(WOAD_LEAVES);
                    player.getInventory().add(BLUE_DYE);
                    make(1767);
                    interpreter.sendItemMessage(1767, "You hand the woad leaves and payment to Aggie.", "Aggie produces a blue bottle and hands it to you.");
                } else {
                    interpreter.sendDialogue("You need 2 woad leaves and 5 coins.");
                }
                stage = 413;
                break;
            case 433:
                end();
                break;
            case 420:
                interpreter.sendDialogues(npc, null, "Yellow is a strange colour to get, comes from onion", "skins. I need 2 onions and 5 coins to make yellow dye.");
                stage = 421;
                break;
            case 421:
                interpreter.sendDialogues(player, FacialExpression.NORMAL, "Okay, make me some yellow dye please.");
                stage = 422;
                break;
            case 422:
                if (player.getInventory().containsItem(COINS) && player.getInventory().containsItem(ONIONS)) {
                    player.getInventory().remove(COINS);
                    player.getInventory().remove(ONIONS);
                    player.getInventory().add(YELLOW_DYE);
                    make(1765);
                    interpreter.sendItemMessage(1765, "You hand the onions and payment to Aggie.", "Aggie produces a yellow bottle and hands it to you.");
                } else {
                    interpreter.sendDialogue("You need 2 onions and 5 coins.");
                }
                stage = 423;
                break;
            case 423:
                end();
                break;
            case 410:
                interpreter.sendDialogues(npc, null, "3 lots of redberries and 5 coins to you.");
                stage = 411;
                break;
            case 411:
                interpreter.sendDialogues(player, FacialExpression.NORMAL, "Okay, make me some red dye please.");
                stage = 412;
                break;
            case 412:
                if (player.getInventory().containsItem(COINS) && player.getInventory().containsItem(REDBERRIES)) {
                    player.getInventory().remove(COINS);
                    player.getInventory().remove(REDBERRIES);
                    player.getInventory().add(RED_DYE);
                    make(1763);
                    interpreter.sendItemMessage(1763, "You hand the berries and payment to Aggie.", "Aggie produces a red bottle and hands it to you.");
                } else {
                    interpreter.sendDialogue("You need 3 lots of redberries and 5 coins.");
                }
                stage = 413;
                break;
            case 413:
                end();
                break;
        }
        return true;
    }

    /**
     * Method used to make a dye.
     *
     * @param dye the dye.
     */
    public final void make(int dye) {
        npc.animate(ANIMATION);
        npc.faceLocation(CAULDRON_LOCATION);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 922 };
    }
}
