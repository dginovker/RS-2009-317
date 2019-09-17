package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.travel.ship.Ships;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;

/**
 * Represents the dialogue plugin used to handle the customs officer.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CustomsOfficer extends DialoguePlugin {

    /**
     * Represents the locations to check where we're.
     */
    private static final Location[] LOCATIONS = new Location[]{ Location.create(2771, 3227, 0), Location.create(2954, 3147, 0) };

    /**
     * Represents the illegal rum.
     */
    private static final Item RUM = new Item(431);

    /**
     * Represents the coins item.
     */
    private static final Item COINS = new Item(Item.COINS, 30);

    /**
     * Constructs a new {@code CustomsOfficer} {@code Object}.
     */
    public CustomsOfficer() {
    }

    /**
     * Constructs a new {@code CustomsOfficer} {@code Object}.
     *
     * @param player the player.
     */
    public CustomsOfficer(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new CustomsOfficer(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (args.length > 1) {
            if (player.getQuestRepository().isComplete("Pirate's Treasure")) {
                if (player.getInventory().containsItem(RUM)) {
                    interpreter.sendDialogues(npc, null, "Aha, trying to smuggle rum are we?");
                    stage = 900;
                    return true;
                }
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Well you've got some odd stuff, but it's all legal. Now", "you need to pay a boarding charge of 30 coins.");
                stage = 121;
                return true;
            }
        }
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Can I help you?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "Can I journey on this ship?", "Does Karamja have unusual customs then?");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can I journey on this ship?");
                        stage = 10;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Does Karamja have unusual customs then?");
                        stage = 20;
                        break;
                }
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "You need to be searched before you can board?");
                stage = 11;
                break;
            case 11:
                interpreter.sendOptions("Select an Option", "Why?", "Search way, I have nothing to hide.", "You're not putting your hands on my things!");
                stage = 12;
                break;
            case 12:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Why?");
                        stage = 110;
                        break;
                    case THREE_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Search waway, I have nothing to hide.");
                        stage = 120;
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "You're not putting your hands on my things!");
                        stage = 130;
                        break;
                }
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I'm not that sort of customs officer.");
                stage = 21;
                break;
            case 21:
                end();
                break;
            case 110:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Because Asgarnia has banned the import of intoxicating", "spirits.");
                stage = 111;
                break;
            case 111:
                end();
                break;
            case 120:
                if (player.getInventory().containsItem(RUM)) {
                    interpreter.sendDialogues(npc, null, "Aha, trying to smuggle rum are we?");
                    stage = 900;
                    return true;
                }
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Well you've got some odd stuff, but it's all legal. Now", "you need to pay a boarding charge of 30 coins.");
                stage = 121;
                break;
            case 121:
                interpreter.sendOptions("Select an Option", "Ok.", "Oh, I'll not bother then.");
                stage = 122;
                break;
            case 900:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Umm... it's for personal use?");
                stage = 901;
                break;
            case 901:
                for (int i = 0; i < player.getInventory().getCount(RUM); i++) {
                    player.getInventory().remove(RUM);
                }
                player.getActionSender().sendMessage("The customs officer confiscates your rum.");
                player.getActionSender().sendMessage("You will need to find some way to smuggle it off the island...");
                end();
                break;
            case 122:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Ok.");
                        stage = 210;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Oh, I'll not bother then.");
                        stage = 220;
                        break;
                }
                break;
            case 210://money
                if (!player.getInventory().containsItem(COINS)) {
                    interpreter.sendDialogues(player, null, "Sorry, I don't seem to have enough coins.");
                    stage = 220;
                    return true;
                }
                if (!player.getInventory().remove(COINS)) {
                    interpreter.sendDialogues(player, null, "Sorry, I don't seem to have enough coins.");
                    stage = 220;
                    return true;
                }
                end();
                Ships ship = null;
                if (player.getLocation().getDistance(LOCATIONS[0]) < 40) {
                    ship = Ships.BRIMHAVEN_TO_ARDOUGNE;
                }
                if (player.getLocation().getDistance(LOCATIONS[1]) < 40) {
                    ship = Ships.KARAMJAMA_TO_PORT_SARIM;
                }
                player.getActionSender().sendMessage("You pay 30 coins and board the ship.");
                ship.sail(player);
                break;
            case 220:
                end();
                break;
            case 130:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "You're not getting on this ship then.");
                stage = 131;
                break;
            case 131:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 380 };
    }
}
