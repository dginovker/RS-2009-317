package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the dialogue plugin used for the wise old man.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class WiseOldManDialogue extends DialoguePlugin {

    /**
     * Represents the items to use.
     */
    private static final Item[] ITEMS = new Item[]{ new Item(9813), new Item(9814) };
    /**
     * Represents the achievement diary cape.
     */
    private static final Item[] ACHIEVEMENT_ITEMS = new Item[]{ new Item(Item.ACHIEVEMENT_DIARY_CAPE_T), new Item(Item.ACHIEVEMENT_DIARY_HOOD) };

    /**
     * Represents the coins item.
     */
    private static final Item COINS = new Item(Item.COINS, 99000);

    /**
     * Constructs a new {@code WiseOldManDialogue} {@code Object}.
     */
    public WiseOldManDialogue() {
    }

    /**
     * Constructs a new {@code WiseOldManDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public WiseOldManDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new WiseOldManDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Greetings, " + player.getUsername() + ",");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                if (player.getQuestRepository().hasCompletedAll() && player.getAchievementRepository().hasCompletedAll()) {
                    options("Quest Point Cape.", "Achievement diary cape.", "Something else.");
                    stage = 1000;
                    return true;
                }
                if (player.getQuestRepository().hasCompletedAll()) {
                    options("Quest Point Cape.", "Something else.");
                    stage = 500;
                    return true;
                }
                interpreter.sendOptions("What would you like to say?", "Is there anything I can do for you?", "Could you check my things for junk, please?", "I've got something I'd like you to look at.");
                stage = 1;
                break;
            case 1000:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        player("I believe you are the person to talk to if I want to buy", "a Quest Point Cape?");
                        stage = 501;
                        break;
                    case THREE_OPTION_TWO:
                        player("I believe you are the person to talk to if I want to buy", "an Achievement diary cape?");
                        stage = 1001;
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendOptions("What would you like to say?", "Is there anything I can do for you?", "Could you check my things for junk, please?", "I've got something I'd like you to look at.");
                        stage = 1;
                        break;
                }
                break;
            case 500:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("I believe you are the person to talk to if I want to buy", "a Quest Point Cape?");
                        stage = 501;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendOptions("What would you like to say?", "Is there anything I can do for you?", "Could you check my things for junk, please?", "I've got something I'd like you to look at.");
                        stage = 1;
                        break;
                }
                break;
            case 1001:
                npc("Indeed you believe rightly, " + player.getUsername() + ", and if you know that", "then you'll also know that they cost 99000 coins.");
                stage = 1002;
                break;
            case 1002:
                options("No, I hadn't heard that!", "Yes, so I was lead to believe.");
                stage = 1003;
                break;
            case 1003:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("No, I hadn't heard that!");
                        stage = 1004;
                        break;
                    case TWO_OPTION_TWO:
                        player("Yes, so I was lead to believe.");
                        stage = 1006;
                        break;
                }
                break;
            case 1004:
                npc("Well that's the cost, and it's not changing.");
                stage = 1005;
                break;
            case 1005:
                end();
                break;
            case 1006:
                if (player.getInventory().freeSlots() < 2) {
                    player("I don't seem to have enough inventory space.");
                    stage = 1007;
                    return true;
                }
                if (!player.getInventory().containsItem(COINS)) {
                    player("I don't seem to have enough coins with", "me at this time.");
                    stage = 1007;
                    return true;
                }
                if (player.getInventory().remove(COINS) && player.getInventory().add(ACHIEVEMENT_ITEMS)) {
                    npc("Have fun with it.");
                    if (player.getAttribute("achievement_cape") == null) {
                        player.saveAttribute("achievement_cape", 1);
                        if (player.getSettings().getPrivateChatSetting() != 3) { // offline?
                            for (Player onlinePlayer : Repository.getPlayers()) {
                                if (onlinePlayer == null || !onlinePlayer.isActive()) {
                                    continue;
                                }
                                if (player.getSettings().getPrivateChatSetting() == 2) { // friends only?
                                    if (!player.getCommunication().hasContact(onlinePlayer.getPidn())) {
                                        continue;
                                    }
                                }
                                if (onlinePlayer == player) {
                                    continue;
                                }
                                onlinePlayer.getActionSender().sendMessage("<col=ff8c38><shad=000>News: " + TextUtils.formatDisplayName(player.getName()) + " has just been awarded the achievement diary cape!");
                            }
                        }
                    }
                    stage = 1007;
                } else {
                    player("I don't seem to have enough coins with", "me at this time.");
                    stage = 1007;
                }
                break;
            case 1007:
                end();
                break;
            case 501:
                npc("Indeed you believe rightly, " + player.getUsername() + ", and if you know that", "then you'll also know that they cost 99000 coins.");
                stage = 502;
                break;
            case 502:
                options("No, I hadn't heard that!", "Yes, so I was lead to believe.");
                stage = 503;
                break;
            case 503:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("No, I hadn't heard that!");
                        stage = 504;
                        break;
                    case TWO_OPTION_TWO:
                        player("Yes, so I was lead to believe.");
                        stage = 506;
                        break;
                }
                break;
            case 504:
                npc("Well that's the cost, and it's not changing.");
                stage = 505;
                break;
            case 505:
                end();
                break;
            case 506:
                if (player.getInventory().freeSlots() < 2) {
                    player("I don't seem to have enough inventory space.");
                    stage = 507;
                    return true;
                }
                if (!player.getInventory().containsItem(COINS)) {
                    player("I don't seem to have enough coins with", "me at this time.");
                    stage = 507;
                    return true;
                }
                if (player.getInventory().remove(COINS) && player.getInventory().add(ITEMS)) {
                    npc("Have fun with it.");
                    if (player.getAttribute("qp_cape") == null) {
                        player.saveAttribute("qp_cape", 1);
                        if (player.getSettings().getPrivateChatSetting() != 3) { // offline?
                            for (Player onlinePlayer : Repository.getPlayers()) {
                                if (onlinePlayer == null || !onlinePlayer.isActive() || onlinePlayer == player) {
                                    continue;
                                }
                                if (player.getSettings().getPrivateChatSetting() == 2) { // friends only?
                                    if (!player.getCommunication().hasContact(onlinePlayer.getPidn())) {
                                        continue;
                                    }
                                }
                                onlinePlayer.getActionSender().sendMessage(
                                    "<col=ff8c38><shad=000>News: " + TextUtils.formatDisplayName(player.getName()) + " has just been awarded the quest point cape!");
                            }
                        }
                    }
                    stage = 507;
                } else {
                    player("I don't seem to have enough coins with", "me at this time.");
                    stage = 507;
                }
                break;
            case 507:
                end();
                break;
            case 1:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Is there anything I can do for you?");
                        stage = 10;
                        break;
                    case THREE_OPTION_TWO:
                        interpreter.sendOptions("Choose an option:", "Could you check my bank for junk, please?", "Could you check my inventory for junk, please?");
                        stage = 20;
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I've got something I'd like you to look at.");
                        stage = 30;
                        break;
                }

                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Thanks, but I don't have anything I need.");
                stage = 11;
                break;
            case 11:
                end();
                break;
            case 20:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Certainly, but I should warn you that I don't know about", "all items.");
                        stage = 100;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Certainly, but I should warn you that I don't know about", "all items.");
                        stage = 102;
                        break;
                }

                break;
            case 100:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "You seem to have no junk in your bank, sorry.");
                stage = 101;
                break;
            case 101:
                end();
                break;
            case 102:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "You seem to have no junk in your inventory, sorry.");
                stage = 101;
                break;
            case 30:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Jolly good. Give it to me, and I'll tell you anything I know", "about it.");
                stage = 31;
                break;
            case 31:
                end();
                break;
        }

        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 3820 };
    }

}
