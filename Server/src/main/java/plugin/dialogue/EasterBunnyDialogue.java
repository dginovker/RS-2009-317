package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.Dye;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.LoyaltyTitle;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.config.Constants;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the Easter bunny dialogue.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 *         TODO Easter Bunny definition : Cute and friendly, with powers beyond that of magic.
 *         TODO Aggie spawn : move closer to cauldron
 *         TODO Children spawns : 2872, 2873, 2874, 2875, 2876, 2877
 *         TODO Rubber chicken examine : 4566 (Change RuneScape to Gielinor).
 */
public class EasterBunnyDialogue extends DialoguePlugin {

    public EasterBunnyDialogue() {
        /**
         * empty.
         */
    }

    public EasterBunnyDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new EasterBunnyDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (player.getSavedData().getGlobalData().getEasterEventStage() == 1) {
            npc("Have you delivered all of the easter eggs, " + TextUtils.formatDisplayName(player.getName()) + "?");
            stage = 10;
            return true;
        }
        if (player.getSavedData().getGlobalData().getEasterEventStage() == 2) {
            npc("Marvellous! There is just one other thing I need your", "help with.");
            stage = 14;
            return true;
        }
        if (player.getSavedData().getGlobalData().getEasterEventStage() == 3) {
            npc("Have you collected all of the dyes from Aggie?");
            stage = 17;
            return true;
        }
        if (player.getSavedData().getGlobalData().getEasterEventStage() == 4) {
            npc("Take these rewards as a thank you!");
            stage = 20;
            return true;
        }
        if (player.getSavedData().getGlobalData().getEasterEventStage() == 100) {
            npc("Thank you again, " + TextUtils.formatDisplayName(player.getName()) + "!");
            stage = END;
            return true;
        }
        npc("Oh dear, oh dear, oh dear, " + TextUtils.formatDisplayName(player.getName()) + "!");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                player("What's wrong?");
                stage = 1;
                break;
            case 1:
                npc("I haven't the time to deliver all of these eggs to", "children around " + Constants.SERVER_NAME + "!");
                stage = 2;
                break;
            case 2:
                player("Why not? How many could there be?");
                stage = 3;
                break;
            case 3:
                npc("The problem isn't the quantity!", "The problem is I need to gather runes to", "teleport to the main cities.");
                stage = 4;
                break;
            case 4:
                player("I could help with that!");
                stage = 5;
                break;
            case 5:
                npc("Oh, that would be great, " + TextUtils.formatDisplayName(player.getName()) + "!");
                stage = 6;
                break;
            case 6:
                if (!player.getInventory().hasRoomFor(new Item(4565))) {
                    npc("Make some room in your inventory, then come talk to me again!");
                    stage = END;
                    break;
                }
                player.getSavedData().getGlobalData().setEasterEventStage(1);
                player.getInventory().add(new Item(4565));
                interpreter.sendItemMessage(4565, "The Easter Bunny gives you a Basket of eggs!");
                stage = 7;
                break;
            case 7:
                npc("I need you to take this basket of eggs, and travel", "to the main cities, which can be found in your magic book", "under the Cities spell. You need to use the basket on");
                stage = 8;
                break;
            case 8:
                npc("the children, giving them an Easter egg.", "Once you have given them all out, return to me!");
                stage = 9;
                break;
            case 9:
                player("I won't let you down!", "I will get on it right now!");
                stage = END;
                break;
            case 10:
                if (!player.getInventory().contains(4565) && !player.getEquipment().contains(4565) &&
                    !player.getBank().contains(4565)) {
                    player("I'm afraid I've lost the basket of eggs you've given me!");
                    stage = 11;
                    break;
                }
                if (player.getSavedData().getGlobalData().getEggsDelivered().size() != 6) {
                    player("No, I'm still working on it.");
                    stage = END;
                    break;
                }
                player("Yes! I have delivered all of the Easter eggs!");
                stage = 13;
                break;
            case 11:
                npc("No problem! Your help is the only thing I need, I have", "many baskets and eggs!");
                stage = 12;
                break;
            case 12:
                if (!player.getInventory().hasRoomFor(new Item(4565))) {
                    npc("Make some room in your inventory, then come talk to me again!");
                    stage = END;
                    break;
                }
                player.getInventory().add(new Item(4565));
                interpreter.sendItemMessage(4565, "The Easter Bunny gives you a Basket of eggs!");
                stage = END;
                break;
            case 13:
                npc("Marvellous! There is just one other thing I need your", "help with.");
                player.getSavedData().getGlobalData().setEasterEventStage(2);
                stage = 14;
                break;
            case 14:
                npc("I've been working on new Easter egg colors,", "while doing so, I have run out of dye!", "If you could, talk to Aggie and ask her to make", "you one Red, Blue and Yellow dye.");
                stage = 15;
                break;
            case 15:
                npc("She can be found in Draynor, and has never", "failed to make dyes before!");
                stage = 16;
                break;
            case 16:
                player("Okay, I'll go talk to her now!");
                player.getSavedData().getGlobalData().setEasterEventStage(3);
                stage = END;
                break;
            case 17:
                if (player.getInventory().contains(Dye.RED.getItem()) &&
                    player.getInventory().contains(Dye.BLUE.getItem()) &&
                    player.getInventory().contains(Dye.YELLOW.getItem())) {
                    player("Yes, I have them all here!");
                    stage = 18;
                    break;
                }
                player("I'm afraid I don't have them all yet.", "I'll go see Aggie in Draynor.");
                stage = END;
                break;
            case 18:
                if (player.getInventory().remove(Dye.RED.getItem(), Dye.YELLOW.getItem(), Dye.BLUE.getItem())) {
                    npc("Splendid!", "Thank you for all of your hard work, " + TextUtils.formatDisplayName(player.getName()) + "!");
                    player.getSavedData().getGlobalData().setEasterEventStage(4);
                    stage = 19;
                    break;
                }
                end();
                break;
            case 19:
                npc("Take these rewards as a thank you!");
                stage = 20;
                break;
            case 20:
                if (player.getInventory().freeSlots() < 2) {
                    npc("Oh no... Make some room in your inventory,", "then come talk to me again!");
                    stage = END;
                    break;
                }
                player.getInventory().add(new Item(1961));
                player.getInventory().add(new Item(4566));
                interpreter.sendItemMessage(1961, "The Easter Bunny has given you a newly coloured", "Easter egg, and a rubber chicken!");
                player.getSavedData().getGlobalData().setEasterEventStage(100);
                player.getSavedData().getGlobalData().setEasterTitle(true);
                LoyaltyTitle.HIPPITY_HOPPITY.unlock(player);
                stage = 21;
                break;
            case 21:
                interpreter.sendPlaneMessage(false, "You have unlocked the title:", LoyaltyTitle.HIPPITY_HOPPITY.getFormattedTitle(player, false, null) + "!");
                stage = 22;
                break;
            case 22:
                interpreter.sendPlaneMessage(false, "To enable this title, go to the title manager and select the", "\"Enable\" option!");
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
        return new int[]{ 1835 };
    }
}
