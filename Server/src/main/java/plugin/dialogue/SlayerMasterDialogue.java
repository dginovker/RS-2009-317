package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.Skillcape;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.slayer.Equipment;
import org.gielinor.game.content.skill.member.slayer.Master;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.item.Item;
import org.gielinor.util.extensions.CalendarExtensionsKt;

/**
 * Represents the dialogue plugin used for a slayer master.
 *
 * @author Vexia
 */
public final class SlayerMasterDialogue extends DialoguePlugin {

    /**
     * The enchanted gem item.
     */
    private static final Item GEM = new Item(Equipment.ENCHANTED_GEM.getItem().getId(), 1);

    /**
     * Represents the master talking.
     */
    private Master master;

    /**
     * Constructs a new {@code SlayerMasterDialogue} {@code Object}.
     */
    public SlayerMasterDialogue() {
    }

    /**
     * Constructs a new {@code SlayerMasterDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public SlayerMasterDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new SlayerMasterDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        if (args[0] instanceof NPC) {
            npc = (NPC) args[0];
        }
        master = Master.forId(args[0] instanceof NPC ? ((NPC) args[0]).getId() : (int) args[0]);
        if (master == Master.DURADEL) {
            if (Skillcape.isMaster(player, Skills.SLAYER)) {
                options("Ask about Skillcape", "Something else");
                stage = 900;
                return true;
            }
        }
        if (args.length > 1 && (boolean) args[1] && player.getSlayer().hasStarted()) {
            player("I need another assignment.");
            stage = 701;
            return true;
        }
        player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NORMAL, "'Ello, and what are you after, then?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                if (!player.getSlayer().hasStarted()) {
                    player.getDialogueInterpreter().sendOptions("Select an Option", "Who are you?", "Do you have anything for trade?", "Er...nothing...");
                    stage = 1;
                } else {
                    player.getDialogueInterpreter().sendOptions("Select an Option", "I need another assignment.", "Do you have anything for trade?", "Er...nothing...");
                    stage = 700;
                }
                break;
            case 1:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        player.getDialogueInterpreter().sendDialogues(player, FacialExpression.CONFUSED, "Who are you?");
                        stage = 10;
                        break;
                    case THREE_OPTION_TWO:
                        player.getDialogueInterpreter().sendDialogues(player, FacialExpression.NORMAL, "Do you have anything for trade?");
                        stage = 20;
                        break;
                    case THREE_OPTION_THREE:
                        player("Er...nothing...");
                        stage = 30;
                        break;
                }
                break;
            case 20:
                player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, "I have a wide selection of Slayer equipment; take a look!");
                stage = 21;
                break;
            case 21:
                end();
                if (npc != null) {
                    Shops.SLAYER_EQUIPMENT.open(player);
                }
                break;
            case 30:
                end();
                break;
            case 10:
                player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, "I'm one of the elite Slayer Masters.");
                stage = 11;
                break;
            case 11:
                player.getDialogueInterpreter().sendOptions("Select an Option", "What's a Slayer?", "Never heard of you...");
                stage = 12;
                break;
            case 12:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player.getDialogueInterpreter().sendDialogues(player, FacialExpression.CONFUSED, "What's a Slayer?");
                        stage = 100;
                        break;
                    case TWO_OPTION_TWO:
                        player.getDialogueInterpreter().sendDialogues(player, FacialExpression.NO_EXPRESSION, "Never heard of you...");
                        stage = 2000;
                        break;
                }

                break;
            case 100:
                player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.LOOKING_DOWN, "Oh dear, what do they teach you in school?");
                stage = 101;
                break;
            case 101:
                player.getDialogueInterpreter().sendDialogues(player, FacialExpression.LOOKING_DOWN, "Well....er...");
                stage = 102;
                break;
            case 102:
                player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, "I suppose I'll have to educate you, then. A Slayer is", "someone who is trained to fight specific creatures. They", "know those creatures' every weakenss and strength. As", "you can guess, it makes killing those creatures a lot");
                stage = 103;
                break;
            case 103:
                player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, "easier.");
                stage = 104;
                break;
            case 104:
                player.getDialogueInterpreter().sendOptions("Select an Option", "Wow, can you teach me?", "Sounds useless to me.");
                stage = 105;
                break;
            case 105:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player.getDialogueInterpreter().sendDialogues(player, FacialExpression.CONFUSED, "Wow, can you teach me?");
                        stage = 500;
                        break;
                    case TWO_OPTION_TWO:
                        player.getDialogueInterpreter().sendDialogues(player, FacialExpression.DIDNT_DO_IT, "Sounds useless to me.");
                        stage = 1000;
                        break;
                }
                break;
            case 500:
                player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.LOOKING_DOWN, "Hmmm, well, I'm not so sure...");
                stage = 501;
                break;
            case 501:
                player("Pleeeaasssse! I'll be your best friend!");
                stage = 502;
                break;
            case 502:
                if (!master.hasRequirment(player)) {
                    player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, "Sorry, but you're not strong enough to be taught by", "me.");
                    stage = 99;
                    break;
                }
                player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, "Oh, okay then; you twisted my arm. You'll have to", "train against specific groups of creatures.");
                stage = 503;
                break;
            case 503:
                player.getDialogueInterpreter().sendDialogues(player, FacialExpression.CONFUSED, "Okay then, what's first?");
                stage = 504;
                break;
            case 504:
                if (player.getInventory().freeSlots() != 0) {
                    player.getInventory().add(GEM);
                    player.getSlayer().generate(master);
                    player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, "We'll start you off hunting " + player.getSlayer().getTaskName() + "s, you'll need to", "kill " + player.getSlayer().getAmount() + " of them.");
                    stage = 510;
                } else if (player.getInventory().freeSlots() == 0) {
                    player("Sorry, I don't have enough inventory space.");
                    stage = 99;
                }
                break;
            case 99:
                if (Perk.SLAYER_COMMANDER.enabled(player) ||
                    player.getDonorManager().getDonorStatus().getCanResetTask() && CalendarExtensionsKt.compareHours(System.currentTimeMillis(), player.getSavedData().getActivityData().getLastTaskReset()) >= 24) {
                    options("Okay, thank you.", "Are you sure you don't want to assign another?");
                    stage = 600;
                    break;
                }
                end();
                break;
            case 510:
                player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, "You'll also need this enchanted gem. It allows Slayer", "Masters like myself to contact you and update you on", "your progress. Don't worry if you lose it; you can buy", "another from any Slayer Master.");
                stage = 511;
                break;
            case 511:
                player("Okay, great!");
                stage = 99;
                break;
            case 1000:
                player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, "That's what you think..");
                stage = 99;
                break;
            case 2000:
                player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, "I am one of the greatest Slayer masters!");
                stage = 99;
                break;
            case 700:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        player("I need another assignment.");
                        stage = 701;
                        break;
                    case THREE_OPTION_TWO:
                        player.getDialogueInterpreter().sendDialogues(player, FacialExpression.CONFUSED, "Do you have anything for trade?");
                        stage = 20;
                        break;
                    case THREE_OPTION_THREE:
                        player.getDialogueInterpreter().sendDialogues(player, FacialExpression.NO_EXPRESSION, "Er...nothing...");
                        stage = 30;
                        break;
                }
                break;
            case 701:
                if (!master.hasRequirment(player)) {
                    player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.LOOKING_DOWN, "Sorry, but you're not strong enough to be taught by", "me.");
                    stage = 99;
                    break;
                }
                if (!player.getSlayer().hasTask()) {
                    player.getSlayer().generate(master);
                    player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, "Excellent, you're doing great. Your new task is to kill", "" + player.getSlayer().getAmount() + " " + player.getSlayer().getTaskName() + "s.");
                    stage = 844;
                    break;
                }
                if (Master.hasSameTask(master, player.getSlayer().getMaster(), player)) {
                    player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, "You're still hunting something. Come back when you've", "finished your task.");
                    stage = 99;
                } else {
                    player.getSlayer().generate(master);
                    player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, "Excellent, you're doing great. Your new task is to kill", "" + player.getSlayer().getAmount() + " " + player.getSlayer().getTaskName() + "s.");
                    stage = 844;
                }
                break;
            case 844:
                player.getDialogueInterpreter().sendOptions("Select an Option", "Got any tips for me?", "Okay, great!");
                stage = 854;
                break;
            case 854:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, player.getSlayer().getTask().getTip());
                        stage = 860;
                        break;
                    case TWO_OPTION_TWO:
                        player("Okay, great!");
                        stage = 855;
                        break;
                }
                break;
            case 860:
                player("Great, thanks!");
                stage = 855;
                break;
            case 855:
                end();
                break;
            case 900:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player.getDialogueInterpreter().sendDialogues(player, FacialExpression.CONFUSED, "Can I buy a Skillcape of Slayer?");
                        stage = 901;
                        break;
                    case TWO_OPTION_TWO:
                        player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.CONFUSED, "'Ello, and what are you after, then?");
                        stage = 0;
                        break;
                }
                break;
            case 901:
                player.getDialogueInterpreter().sendDialogues(Master.DURADEL.getNpc(), FacialExpression.NO_EXPRESSION, "Certainly! Right when you give me 99000 coins.");
                stage = 902;
                break;
            case 902:
                options("Okay, here you go.", "No, thanks.");
                stage = 903;
                break;
            case 903:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Okay, here you go.");
                        stage = 904;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        break;
                }
                break;
            case 904:
                if (Skillcape.purchase(player, Skills.SLAYER)) {
                    player.getDialogueInterpreter().sendDialogues(Master.DURADEL.getNpc(), FacialExpression.NO_EXPRESSION, "There you go! Enjoy.");
                }
                stage = 905;
                break;
            case 905:
                end();
                break;
            case 600:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Okay, thank you.");
                        stage = END;
                        break;
                    case TWO_OPTION_TWO:
                        player("Are you sure you don't want to assign another?");
                        stage = 601;
                        break;
                }
                break;
            case 601:
                npc("Well, I... I guess I can.");
                stage = 602;
                break;
            case 602:
                player.getPerkManager().handlePerk(Perk.SLAYER_COMMANDER);
                player.getSlayer().generate(master);
                player.getSavedData().getActivityData().setLastTaskReset(System.currentTimeMillis());
                player.getDialogueInterpreter().sendDialogues(master.getNpc(), FacialExpression.NO_EXPRESSION, "Your new task is to kill", "" + player.getSlayer().getAmount() + " " + player.getSlayer().getTaskName() + "s.");
                stage = 844;
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ Master.TURAEL.getNpc(), Master.MAZCHNA.getNpc(), Master.VANNAKA.getNpc(), Master.CHAELDAR.getNpc(), Master.DURADEL.getNpc() };
    }

}
