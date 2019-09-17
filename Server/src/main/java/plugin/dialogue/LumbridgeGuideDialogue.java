package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.world.map.zone.impl.ModeratorZone;
import org.gielinor.rs2.config.Constants;

/**
 * Represents the lumbridge guide dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class LumbridgeGuideDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code LumbridgeGuideDialogue} {@code Object}.
     */
    public LumbridgeGuideDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code LumbridgeGuideDialogue} {@code Object}.
     *
     * @param player
     *            the player.
     */
    public LumbridgeGuideDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new LumbridgeGuideDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Greetings, adventurer. I am Phileas, the Lumbridge",
            "Guide. I am here to give information and directions to", "new players. Do you require any help?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                if (player.getDetails().getRights() == Rights.REGULAR_PLAYER) {
                    interpreter.sendOptions("Select an Option", "Yes please.", "No, I can find things myself thank you.");
                    stage = 1;
                } else if (player.getDetails().getRights() == Rights.PLAYER_MODERATOR && ModeratorZone.isOpen()) {
                    interpreter.sendOptions("Select an Option", "Yes please.",
                        "Yes, I would like to access the P-Mod room.", "No, I can find things myself thank you.");
                    stage = 680;
                } else if (player.getDetails().getRights() == Rights.PLAYER_MODERATOR && !ModeratorZone.isOpen()) {
                    interpreter.sendOptions("Select an Option", "Yes please.", "No, I can find things myself thank you.");
                    stage = 1;
                } else if (player.getDetails().getRights() == Rights.GIELINOR_MODERATOR) {
                    interpreter.sendOptions("Select an Option", "Yes please.",
                        "Yes, I would like to access the P-Mod room.", "No, I can find things myself thank you.");
                    stage = 680;
                }
                break;
            case 10:
                interpreter.sendOptions("Select an Option", "Tell me about the town of Lumbridge.",
                    "I'm fine for now, thanks.");
                stage = 11;
                break;
            case 1:
            case 11:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        interpreter.sendDialogues(player, null, "Yes please.");
                        stage = 100;
                        break;
                    case TWO_OPTION_TWO:
                        interpreter.sendDialogues(player, null, "No, I can find things myself thank you.");
                        stage = 200;
                        break;
                }
                break;
            case 200:
                end();
                break;
            case 100:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL, "First I must warn you to take every precaution to",
                    "keep your " + Constants.SERVER_NAME + " password and PIN secure. The",
                    "most important thing to remember is to never give your",
                    "password to, or share you account with, anyone.");
                stage = 101;
                break;
            case 101:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL,
                    "I have much more information to impart; what would", "you like to know about?");
                stage = 102;
                break;
            case 102:
                interpreter.sendOptions("Select an Option", "Where can I find a quest to go on?",
                    "What monsters should I fight?", "Where can I make money?", "I'd like to know more about security.",
                    "Where can I find a bank?");
                stage = 103;
                break;
            case 103:
                switch (optionSelect) {
                    case FIVE_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NORMAL, "Where can I find a quest to go on?");
                        stage = 1100;
                        break;
                    case FIVE_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NORMAL, "What monsters should I fight?");
                        stage = 1200;
                        break;
                    case FIVE_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NORMAL, "Where can I make money?");
                        stage = 1300;
                        break;
                    case FIVE_OPTION_FOUR:
                        interpreter.sendDialogues(player, FacialExpression.NORMAL, "I'd like to know more about security.");
                        stage = 1400;
                        break;
                    case FIVE_OPTION_FIVE:
                        interpreter.sendDialogues(player, FacialExpression.NORMAL, "Where can I find a bank?");
                        stage = 1500;
                        break;
                }
                break;
            case 1400:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL,
                    "I can tell you about password security, avoiding item",
                    "scamming and in-game moderation. I can also tell you",
                    "about a place called the Stronghold of Security, where",
                    "you can learn more about account security and have a");
                stage = 1401;
                break;
            case 1401:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL, "bit of an adventure at the same time.");
                stage = 1402;
                break;
            case 1402:
                end();
                break;
            case 1300:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL, "There are many ways to make money in the game. I",
                    "would suggest either killing monsters or doing a trade", "skill such as Smithing or Fishing.");
                stage = 1301;
                break;
            case 1301:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL,
                    "Please don't try to get money by begging off other",
                    "players. It will make you unpopular. Nobody likes a",
                    "beggar. It is very irritating to have other players asking", "for your hard-earned cash.");
                stage = 1302;
                break;
            case 1302:
                end();
                break;
            case 1200:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL, "You're strong enough to work out what monsters to",
                    "fight for yourself now, but the combat tutors might help",
                    "you with any questions you have about the skills;",
                    "they're just over there to the south of the general store.");
                stage = 1201;
                break;
            case 1201:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL,
                    "There are things to kill all over the place! At your level,",
                    "you might like to try wandering westwards to the",
                    "Wizards' Tower or north-west to the Barbarian Village.",
                    "Non-player characters usually appear as yellow dots on");
                stage = 1202;
                break;
            case 1202:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL,
                    "your mini-map, although there are some that you won't",
                    "be able to fight, such as myself. If you explore further",
                    "afield you may be able to find areas that are less",
                    "crowded, but watch out for monsters which are tougher");
                stage = 1203;
                break;
            case 1203:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL,
                    "than you. A monster's combat level is shown next to",
                    "their 'Attack' option. If that level is coloured green it",
                    "means the monster is weaker than you. If it is red, it",
                    "means that the monster is tougher than you.");
                stage = 1204;
                break;
            case 1204:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Remember, you wil do better if you have better",
                    "armour and weapons and it's always worth carrying a", "bit of food to heal yourself.");
                stage = 1205;
                break;
            case 1205:
                end();
                break;
            case 1500:
                interpreter.sendDialogues(npc, FacialExpression.NORMAL,
                    "You'll find a bank upstairs in Lumbridge Castle - go", "right to the top.");
                stage = 1501;
                break;
            case 1501:
                end();
                break;
            case 1100:
                stage = 1101;
                if (!player.getQuestRepository().isComplete("Cook's Assistant")) {
                    interpreter.sendDialogues(npc, FacialExpression.NORMAL,
                        "You can try talking to the Cook in the Lumbridge Castle",
                        "I hear he is always looking for some help.");
                    return true;
                }
                if (!player.getQuestRepository().isComplete("Sheep Shearer")) {
                    interpreter.sendDialogues(npc, FacialExpression.NORMAL,
                        "You can try talking to Fred the Farmer north-west of here",
                        "I hear he is always looking for some help.");
                    return true;
                }
                interpreter.sendDialogues(npc, null, "You are such an accomplished adventurer already; you",
                    "should be telling me some good quests to go on.");
                break;
            case 1101:
                end();
                break;
            case 104:
                end();
                break;
            case 680:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        interpreter.sendDialogues(player, null, "Yes please.");
                        stage = 100;
                        break;
                    case THREE_OPTION_TWO:
                        stage = 690;
                        interpreter.sendDialogues(npc, FacialExpression.NORMAL, "Yes, of course.");
                        break;
                    case THREE_OPTION_THREE:
                        interpreter.sendDialogues(player, null, "No, I can find things myself thank you.");
                        stage = 200;
                        break;
                }
                break;
            case 690:
                end();
                if (ModeratorZone.isOpen() && player.getDetails().getRights() == Rights.PLAYER_MODERATOR) {
                    ModeratorZone.teleport(player);
                } else if (player.getDetails().getRights() == Rights.GIELINOR_MODERATOR) {
                    ModeratorZone.teleport(player);
                }
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2244 };
    }
}
