package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.travel.ship.Ships;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.rs2.model.container.Container;

/**
 * Represents the dialogue plugin used for the monk of entrana dialogue.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class MonkOfEntranaDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code MonkOfEntranaDialogue} {@code Object}.
     */
    public MonkOfEntranaDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code MonkOfEntranaDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public MonkOfEntranaDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new MonkOfEntranaDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (npc.getId() == 2730 || npc.getId() == 658 || npc.getId() == 2731) {
            interpreter.sendDialogues(npc, null, "Do you wish to leave holy entrana?");
            stage = 500;
            return true;
        }
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Do you seek passage to holy Entrana? If so, you must", "leave your weaponry and armour behind. This is", "Saradomin's will.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "No, not right now.", "Yes, okay, I'm ready to go.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, null, "No, not right now.");
                        stage = 10;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, null, "Yes, okay, I'm ready to go.");
                        stage = 20;
                        break;
                }
                break;
            case 10:
                interpreter.sendDialogues(npc, null, "Very well.");
                stage = 11;
                break;
            case 11:
                end();
                break;
            case 20:
                interpreter.sendDialogues(npc, null, "Very well. One moment please.");
                stage = 21;
                break;
            case 21:
                interpreter.sendPlaneMessage("The monk quickly searches you.");
                stage = 22;
                break;
            case 22:
                Container[] container = new Container[]{ player.getInventory(), player.getEquipment() };
                for (Container c : container) {
                    for (Item i : c.toArray()) {
                        if (i == null) {
                            continue;
                        }
                        if (hasBonuses(i)) {
                            interpreter.sendDialogues(npc, null, "NO WEAPONS OR ARMOUR are permitted on holy", "Entrana AT ALL. We will not allow you to travel there", "in breach of mighty Saradomin's edict.");
                            stage = 23;
                            return true;
                        }
                    }
                }
                interpreter.sendDialogues(npc, null, "All is satisfactory. You may board the boat now.");
                stage = 25;
                break;
            case 23:
                interpreter.sendDialogues(npc, null, "Do not try and decieve us again. Come back when you", "have liad down your Zamorakian instruments of death.");
                stage = 24;
                break;
            case 24:
                end();
                break;
            case 25:
                end();
                Ships.PORT_SARIM_TO_ENTRANA.sail(player);
                break;
            case 500:
                interpreter.sendOptions("Select an Option", "Yes, I'm ready to go.", "Not just yet.");
                stage = 501;
                break;
            case 501:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, null, "Yes, I'm ready to go.");
                        stage = 510;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, null, "Not just yet.");
                        stage = 520;
                        break;
                }
                break;
            case 510:
                interpreter.sendDialogues(npc, null, "Okay, let's board...");
                stage = 511;
                break;
            case 511:
                end();
                Ships.ENTRANA_TO_PORT_SARIM.sail(player);
                break;
            case 520:
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
        return new int[]{ 2728, 657, 2729, 2730, 2731, 658 };
    }
}
