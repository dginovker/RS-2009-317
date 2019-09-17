package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.model.container.impl.Equipment;

/**
 * @author Logan G. <logan@Gielinor.org>
 */
public class GhostDiscipleDialogue extends DialoguePlugin {

    public GhostDiscipleDialogue() {
    }

    public GhostDiscipleDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GhostDiscipleDialogue(player);
    }

    private boolean hasGhostspeakAmulet() {
        Item amulet = player.getEquipment().get(Equipment.SLOT_AMULET);
        return amulet != null && amulet.getId() == Item.GHOSTSPEAK_AMULET;
    }

    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (hasGhostspeakAmulet()) {
            npc("Would you like to buy an Ectophial?", "It costs 300,000 coins, but will help you get here faster!");
            stage = 0;
        } else {
            npc("Woooo wooo wooooo woooo");
            stage = 10;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Yes, please.", "No thank you.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        if (player.getInventory().contains(4251) || player.getInventory().contains(4252) || player.getBank().contains(4251) || player.getBank().contains(4252)) {
                            npc("You already have an Ectophial!");
                            stage = 20;
                            break;
                        }
                        if (!player.getInventory().contains(new Item(Item.COINS, 300000))) {
                            player("I don't seem to have enough coins.");
                            stage = 20;
                            break;
                        }
                        player.getInventory().remove(new Item(Item.COINS, 300000));
                        player.getInventory().add(new Item(4251, 1), player);
                        interpreter.sendItemMessage(4251, "The ghost sells you an Ectophial for 300,000 coins!");
                        stage = 20;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;

            case 10:
                interpreter.sendPlaneMessage("You cannot understand the ghost.");
                stage = 20;
                break;

            case 20:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 1686 };
    }

}
