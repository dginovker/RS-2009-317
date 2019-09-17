package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the karim dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class KarimDialogue extends DialoguePlugin {

    /**
     * Represents the coins item.
     */
    private static final Item COINS = new Item(Item.COINS, 1);

    /**
     * Represents the mud kebab item.
     */
    private static final Item KEBAB = new Item(1971, 1);

    /**
     * Constructs a new {@code KarimDialogue} {@code Object}.
     */
    public KarimDialogue() {
    }

    /**
     * Constructs a new {@code KarimDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public KarimDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new KarimDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Would you like to buy a nice kebab? Only one gold.", "I also have a great supply of gems.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes please.", "I would like to see your shop.", "No thank you.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Yes please.");
                        stage = 20;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I would like to see your shop.");
                        stage = 30;
                        break;
                    case 3:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "No thank you.");
                        stage = 10;
                        break;

                }
                break;
            case 10:
                end();
                break;
            case 20:
                if (player.getInventory().freeSlots() == 0) {
                    interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I don't have enough room, sorry.");
                    stage = 21;
                } else if (!player.getInventory().contains(Item.COINS, 1)) {
                    end();
                    player.getActionSender().sendMessage("You need 1 gp to buy a kebab.");
                } else {
                    player.getInventory().remove(COINS);
                    player.getInventory().add(KEBAB);
                    end();
                }
                break;
            case 30:
                end();
                Shops.KARIM_KEBAB_SELLER.open(player);
                break;
            case 21:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 543 };
    }

}
