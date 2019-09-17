package plugin.dialogue;

import java.awt.Point;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * @author 'Vexia
 */
public class StrongHoldOfSecurityDialogue extends DialoguePlugin {

    /**
     * Represents the door being interacted with.
     */
    private GameObject door;

    /**
     * Constructs a new {@code StrongHoldOfSecurityDialogue} {@code Object}.
     */
    public StrongHoldOfSecurityDialogue() {
    }

    /**
     * Constructs a new {@code StrongHoldOfSecurityDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public StrongHoldOfSecurityDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new StrongHoldOfSecurityDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        door = (GameObject) args[0];
        npc = NPC.create(getNpcId(door.getName()), null);
        if (player.getLocation().getX() == 1859 && player.getLocation().getY() == 5239 || player.getLocation().getX() == 1858 && player.getLocation().getY() == 5239) {
            interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Greetings Adventurer. This place is kept safe by the", "spirits within the doors. As you pass through you will be", "asked questions about security. Hopefully you will learn", "much from us.");
            stage = 900;
            return true;
        }
        int rand = RandomUtil.random(0, 18);
        int randd = RandomUtil.random(0, 30);
        if (randd == 10) {
            doorTeleport(player);// random let through door that happens every
            // so often.
            return true;
        }
        switch (rand) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: What do I do if a", "moderator asks me for my account details?");
                stage = 100;
                break;
            case 1:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: My friend uses this", "great add-on program he got from a website, should I?");
                stage = 1000;
                break;
            case 2:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: Who is it ok to", "share my account with?");
                stage = 200;
                break;
            case 3:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: Why do I need to", "type in recovery questions?");
                stage = 300;
                break;
            case 4:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: Who is it ok to", "share my account with?");
                stage = 400;
                break;
            case 5:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: Who can I give my", "password to?");
                stage = 500;
                break;
            case 6:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: How will " + Constants.SERVER_NAME + "", "contact me if I have been chosen to be a moderator?");
                stage = 600;
                break;
            case 7:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: How often should", "you change your recovery questions?");
                stage = 700;
                break;
            case 8:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: A website says I", "can become a player moderator by giving", "them my password what do I do?");
                stage = 800;
                break;
            case 9:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: Will " + Constants.SERVER_NAME + " block me", "from saying my PIN in game?");
                stage = 1900;
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: Can I leave my", "account logged in while I'm out of the room?");
                stage = 1100;
                break;
            case 11:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: Where should I", "enter my " + Constants.SERVER_NAME + " Password?");
                stage = 1111;
                break;
            case 12:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: What is an", "example of a good bank PIN?");
                stage = 1200;
                break;
            case 13:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: What do I do if I", "think I have a keylogger or virus?");
                stage = 1300;
                break;
            case 14:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: Recovery answers", "should be...");
                stage = 1400;
                break;
            case 15:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: What do you do", "if someone tells you that you have won the " + Constants.SERVER_NAME + "", "Lottery and asks for your password or recoveries?");
                stage = 1500;
                break;
            case 16:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: What should I", "do if I think someone knows my recoveries?");
                stage = 1600;
                break;
            case 17:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: What do you do", "if someone asks you for your password or recoveries", "to make you a player moderator?");
                stage = 1700;
                break;
            case 18:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To pass you must answer me this: Where can i", "find cheats for " + Constants.SERVER_NAME + "?");
                stage = 1800;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 900:// Greetings.
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Please pass through and begin your adventure, beware", "of the various monsters that dwell within.");
                stage = 901;
                break;
            case 901:// Greeting part 2, getting sucked in.
                doorTeleport(player);
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Oh my! I just got sucked through that door... what a", "weird feeling! Still, I guess I should expect it as these", "evidently aren't your average kind of doors.... they talk", "and look creepy!");
                stage = 902;
                break;
            case 902:
                end();
                break;
            case 100:// 4282, 4283
                interpreter.sendOptions("Select an Option", "Tell them whatever they want to know.", "Politely tell them no.", "Politely tell them no and then use the 'Report Abuse' button.");
                stage = 101;
                break;
            case 101:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! Never give your account details to anyone!", "This includes things like recovery answers, contact", "details and passwords. Never use personal details for", "recoveries or bank PINs!");
                        stage = 99;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Ok! Don't tell them the details. But reporting the", "incident to " + Constants.SERVER_NAME + " would help. Use the Report Abuse", "button. Never use personal details for recoveries or", "bank PINs!");
                        stage = 69;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! Report any attempt to gain your account", "details as it is a very serious breach of " + Constants.SERVER_NAME + "'s", "rules. Never use personal details for recoveries or bank", "PINs!");
                        stage = 69;
                        break;
                }
                break;
            case 99:
                end();
                break;
            case 1000:
                interpreter.sendOptions("Select an Option", "No, it might steal my password.", "I'll gave it a try and see if I like it.", "Sure, he's used it alot, so can I.");
                stage = 10001;
                break;
            case 10001:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! The only safe add-on for " + Constants.SERVER_NAME + " is the Window", "client available from our " + Constants.SERVER_NAME + " Website.");
                        stage = 69;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! Some programs steal your password without", "you even knowing, this only requires running the", "program once, even if you don't use it.");
                        stage = 99;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! The program may steal your password and is", "against the rules to use.");
                        stage = 99;
                        break;
                }
                break;
            case 200:

                interpreter.sendOptions("Select an Option", "My friends.", "Relatives.", "Nobody.");
                stage = 201;
                break;
            case 201:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! You account may only be used by you.");
                        stage = 99;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! Your account may only be used by you.");
                        stage = 99;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! You, can you only may use your account.");
                        stage = 69;
                        break;
                }
                break;
            case 300:
                interpreter.sendOptions("Select an Option", "To help me recover my password if I forget it or it is stolen.", "To let " + Constants.SERVER_NAME + " know more about its players.", "To see if I can type in random letters on my keyboard.");
                stage = 301;
                break;
            case 301:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! Your recovery questions will help " + Constants.SERVER_NAME + " staff protect", "and return your account if it is stolen. Never use personal", "details for recoveries or bank PINs!");
                        stage = 69;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! " + Constants.SERVER_NAME + " values players' opinions, but we use polls", "and forums to see what you think. The recoveries arenot there to gain personal information about anybody", "but to protect your account. Never use personal details");
                        stage = 99;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! Typing random letters into your recoveries", "won't help you or the " + Constants.SERVER_NAME + " staff - you'll never", "remember them anyway! Never use personal details for", "recoveries or bank PINs!");
                        stage = 99;
                        break;
                }
                break;
            case 400:
                interpreter.sendOptions("Select an Option", "My friends", "Relatives", "Nobody");
                stage = 401;
                break;
            case 401:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! You account may only be used by you.");
                        stage = 99;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! You account may only be used by you.");
                        stage = 99;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! You, and you only may use your account.");
                        stage = 69;
                        break;
                }
                break;
            case 500:
                interpreter.sendOptions("Select an Option", "My friends", "My brother", "Nobody");
                stage = 501;
                break;
            case 501:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! Your password should be kept secret from", "everyone. You should *never* give it out under any", "circumstances.");
                        stage = 99;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! Your password should be kept secret from", "everyone. You should *never* give it out under any", "circumstances.");
                        stage = 99;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! Your password should be kept secret from", "everyone. You should *never* give it out under any", "circumstances.");
                        stage = 69;
                        break;
                }
                break;
            case 600:
                interpreter.sendOptions("Select an Option", "Email.", "Website popup.", "Game Inbox on the " + Constants.SERVER_NAME + " Website.");
                stage = 601;
                break;
            case 601:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! " + Constants.SERVER_NAME + " never uses email to contact you, this is a", "scam and a fake, do not reply to it and delete it", "straight away. " + Constants.SERVER_NAME + " will only contact you through your", "Game Inbox avaibale on our website.");
                        stage = 99;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! " + Constants.SERVER_NAME + " would never use such an insecure", "method to pick you. We will contact you through your", "Game Inbox available on our website.");
                        stage = 99;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! We only contact our players via the game", "Inbox which you can acces from our " + Constants.SERVER_NAME + "", "website.");
                        stage = 69;
                        break;
                }
                break;
            case 700:
                interpreter.sendOptions("Select an Option", "Never", "Every couple of months", "Every day");
                stage = 701;
                break;
            case 701:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Never changing your recovery questions will lead to", "an insecure account due to keyloggers or friends knowing", "enough about you to guess them. Don't use personal details for your recoveries.");
                        stage = 99;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! it is the ideal option to change your questions", "but make sure you can remember the answers!", "Don't use personal details for your recoveries.");
                        stage = 69;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Normally recovery questions will take 14 days to", "become active, so there's no point in changing them", "everyday! Don't use personal details for your", "recoveries.");
                        stage = 99;
                        break;
                }
                break;
            case 800:
                interpreter.sendOptions("Select an Option", "Nothing.", "Give them my password.", "Don't tell them anything and inform staff through the game website.");
                stage = 801;
                break;
            case 801:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "This is one solution, however someone will fall for", "this scam sooner or later. Tell us about it through", "the " + Constants.SERVER_NAME + " website. Remember that  moderators are hand", "picked by " + Constants.SERVER_NAME + ".");
                        stage = 69;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! This will almost certainly lead to your accout", "being hijacked. No website can make you a moderator", "as they are hand picked by " + Constants.SERVER_NAME + ".");
                        stage = 99;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! By informing us we can have the site taken", "down so other people will not have their accounts", "hijacked by this scam.");
                        stage = 69;
                        break;
                }
                break;
            case 1900:
                interpreter.sendOptions("Select an Option", "Yes.", "No.");
                stage = 1901;
                break;
            case 1901:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! " + Constants.SERVER_NAME + " does NOT block your PIN so don't type", "it!");
                        stage = 99;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! " + Constants.SERVER_NAME + " will not block your PIN so don't type", "it! Never use personal details for reccoveries or bank", "PINs!");
                        stage = 69;
                        break;
                }
                break;
            case 1100:
                interpreter.sendOptions("Select an Option", "Yes.", "No.", "If I'm going to be quick.");
                stage = 1101;
                break;
            case 1101:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! You should logout in case you are attacked or", "receive a random event. Leaving your character logged", "in can also allow someone to steal your items or entire account!");
                        stage = 99;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! This is the safest, both in terms of security", "and keeping your items! Leaving you character logged", "in can also allow someone to steal your items or entitre", "account!");
                        stage = 69;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! You should logout in case you are attacked or", "receive a random event. Leaving your character logged", "in can also allow someone to steal your items or entire account!");
                        stage = 99;
                        break;
                }
                break;
            case 1111:
                interpreter.sendOptions("Select an Opion", "On " + Constants.SERVER_NAME + " and all fansites.", "Only on the " + Constants.SERVER_NAME + " website.", "On all websites I visit.");
                stage = 1112;
                break;
            case 1112:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! Always use a unique password purely for your " + Constants.SERVER_NAME + " account.");
                        stage = 99;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! Always make sure you are entering your", "password only on the " + Constants.SERVER_NAME + " Website as other sites", "may try to steal it.");
                        stage = 69;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! This is very insecure and will may lead to", "your account being stolen.");
                        stage = 99;
                        break;
                }
                break;
            case 1200:
                interpreter.sendOptions("Select an Option", "Your real life bank PIN.", "Your birthday.", "The birthday of a famous person or event.");
                stage = 1201;
                break;
            case 1201:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "This is a bad idea as if someone happens to find out your bank", "PIN on " + Constants.SERVER_NAME + ", they then have acces to your bank account.");
                        stage = 99;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Not a good idea because you know how many presents", "you get for your birthday. So you can imagine how", "many people know this date. Never use personal details", "for recoveries or bank PINs!");
                        stage = 99;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Well done! Unless you tell someone, they are unlikely", "to guess who or what you have chosen, and you can", "always look it up, Never use personal details for recoveries or bank PINs!");
                        stage = 69;
                        break;
                }
                break;
            case 1300:
                interpreter.sendOptions("Select an Option", "Virus scan my computer then change my password and recoveries.", "Change my password and recoveries then virus scan my computer.", "Nothing, it will go away on its own.");
                stage = 1301;
                break;
            case 1301:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! Removing the keylogger must be the priority", "otherwise anything you type can be given away.", "Remember to change your password and recovery", "questions afterwards.");
                        stage = 69;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! If you change your password and recoveries", "while you still have the keylogger, they will still be", "insecure. Remove the keylogger first. Never use", "personal details for recoveries or bank PINs!");
                        stage = 99;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! This could mean your account may be", "accessed by someone else. Remove the keylogger then", "change your password and recoveries. Never use", "personal details for recoveries or bank PINs!");
                        stage = 99;
                        break;
                }
                break;
            case 1400:
                interpreter.sendOptions("Select an Option", "Memorable", "Easy to guess", "Random gibberish");
                stage = 1401;
                break;
            case 1401:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! A good recovery answer that not many people", "will know, you won't forget, will stay the same and that", "you wouldn't accidentally give away. Remember: Don't", "use personal details for your recoveries.");
                        stage = 69;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "This is a bad idea as anyone who knows you could", "guess them. Remember: Don't use personal details for", "your recoveries.");
                        stage = 99;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "This is a bad idea because it is very difficult to", "remember and you won't be able to recover your", "account! Remember: Don't use personal details for", "your recoveries.");
                        stage = 99;
                        break;
                }
                break;
            case 1500:
                interpreter.sendOptions("Select an Option", "Give them the information they asked for.", "Don't tell them anything and ignore them.");
                stage = 1501;
                break;
            case 1501:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! There is no " + Constants.SERVER_NAME + " Lottery! Never give", "your account details to anyone. Press the 'Report Abuse'", "button and fill in the offending player's name and", "the correct category.", "Don't tell them anything and click the 'Report Abuse' button.");
                        stage = 99;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Quite good. But we should try to stop scammers.", "So please report them using the 'Report Abuse' button.");
                        stage = 69;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! Press the 'Report Abuse' button and", "fill in the offending player's name and the correct category.");
                        stage = 69;
                        break;
                }
                break;
            case 1600:
                interpreter.sendOptions("Select an Option", "Tell them never to use them.", "Use the Account Managemnt section on the " + Constants.SERVER_NAME + " website.", "'Recover a Lost Password' section on the " + Constants.SERVER_NAME + " website.");
                stage = 1601;
                break;
            case 1601:
                if (optionSelect.getButtonId() == 1) {
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! This does nothing to help the security of your", "account. You should reset your questions through", "the'Lost password' link on our website.");
                    stage = 99;
                } else if (optionSelect.getButtonId() == 2) {
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! If you use the Account Management section to", "change your recovery questions, it will take 14 days", "to come into effect, someone may have acces to your", "account this time.");
                    stage = 99;
                } else if (optionSelect.getButtonId() == 3) {
                    interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! If you provide all the correct information", "this will reset your questions within 24 hours and", "make your account secure again.");
                    stage = 69;
                }
                break;
            case 1700:
                interpreter.sendOptions("Select an Option", " Don't give them any information and send an 'Abuse report'.", "Don't tell them anything and ignore them.", "Give them the information they asked for.");
                stage = 1701;
                break;
            case 1701:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! Press the 'Report Abuse' button and fill", "in the offending player's name and the correct", "category.");
                        stage = 69;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Quite good. But we should try to stop scammers.", "So please report them using the 'Report Abuse'", "button.");
                        stage = 69;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! " + Constants.SERVER_NAME + " never ask for your account", "information especially to become a player moderator.", "Press the 'Report Abuse' button and fill in the offending player's", "name and the correct cattegory.");
                        stage = 99;
                        break;
                }
                break;
            case 1800:
                interpreter.sendOptions("Select an Option", "On the " + Constants.SERVER_NAME + " website.", "By searching the internet.", "Nowhere.");
                stage = 1801;
                break;
            case 1801:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! There are NO " + Constants.SERVER_NAME + " cheats coded", "into the game and any sites claiming to have cheats", "are fakes and may lead to your account being stolen if you", "give them your password.");
                        stage = 99;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Wrong! There are NO " + Constants.SERVER_NAME + " cheats coded", "into the game and any sites claiming to have cheats", "are fakes and may lead to your account being stolen if you", "give them your password.");
                        stage = 99;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Correct! There are NO " + Constants.SERVER_NAME + " cheats coded into", "the game. Any sites claiming to have cheats are", "fakes and may lead to your account being stolen if you give", "them your password.");
                        stage = 69;
                        break;
                }
                break;
            case 69:
                end();
                doorTeleport(player);
                break;
        }
        return true;
    }

    /**
     * Method used to teleport through the door.
     *
     * @param player the player.
     */
    public void doorTeleport(final Player player) {
        player.lock();
        player.animate(new Animation(4282));
        World.submit(new Pulse(1, player) {

            int count = 0;

            @Override
            public boolean pulse() {
                if (count == 1) {
                    Point p = DoorActionHandler.getRotationPoint(door.getRotation());
                    int[] rotation = DoorActionHandler.getRotation(door, DoorActionHandler.getSecondDoor(door), p);
                    final GameObject opened = door.transform(door.getId(), rotation[0]);
                    opened.setLocation(door.getLocation().transform((int) p.getX(), (int) p.getY(), 0));
                    Location destination = door.getLocation();
                    if (player.getLocation().equals(destination)) {
                        destination = destination.transform((int) p.getX(), (int) p.getY(), 0);
                    }
                    player.getProperties().setTeleportLocation(destination);
                    player.saveAttribute("sos:answered", true);
                }
                if (count == 2) {
                    return true;
                }
                count++;
                return false;
            }

            @Override
            public void stop() {
                player.animate(new Animation(4283));
                player.unlock();
            }
        });
    }

    /**
     * Gets the npc id from the door name.
     *
     * @param name the name.
     * @return the name.
     */
    private int getNpcId(String name) {
        switch (name) {
            case "Gate of War":
                return 4377;
            case "Rickety door":
                return 4378;
            case "Oozing barrier":
                return 4379;
            case "Portal of Death":
                return 4380;
        }
        return 0;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 12459 };
    }

}
