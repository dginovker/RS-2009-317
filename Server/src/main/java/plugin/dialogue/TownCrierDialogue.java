package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the town crier dialogue plugin.
 *
 * @author 'Vexia
 */
public final class TownCrierDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code TownCrierDialogue} {@code Object}.
     */
    public TownCrierDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code TownCrierDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public TownCrierDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new TownCrierDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hear ye! Hear ye! Player Moderators massive help to", "" + Constants.SERVER_NAME + "");
        stage = 1;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {

        switch (stage) {
            case 1:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Oh, hello citizen. Are you here to find out about Player", "Moderators? Or perhaps would you like to know about the", "laws of the land?");
                stage = 2;
                break;
            case 2:
                interpreter.sendOptions("Select an Option", "Tell me about Player Moderators.", "Tell me about the Rules of " + Constants.SERVER_NAME + ".", "Can you give me a handy tip please?", "Bye!");
                stage = 3;
                break;
            case 3:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Tell me about Player Moderators.");
                        stage = 50;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Tell me about the Rules of " + Constants.SERVER_NAME + ".");
                        stage = 70;
                        break;
                    case 3:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you give me a handy tip please?");
                        stage = 100;
                        break;
                    case 4:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Bye!");
                        stage = 400;
                        break;
                }
                break;
            case 400:
                interpreter.sendDialogues(npc, null, "Nice meeting you.");
                stage = 401;
                break;
            case 401:
                end();
                break;
            case 50:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Of course. What would you like to know?");
                stage = 51;
                break;
            case 51:
                interpreter.sendOptions("Select an Option", "What is a Player Moderator?", "What can Player Moderators do?", "How do I become a Player Moderator?", "What can Player Moderators not do?");
                stage = 52;
                break;
            case 52:

                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, " What is a Player Moderator?");
                        stage = 150;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "What can Player Moderators do?");
                        stage = 160;
                        break;
                    case 3:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "How do I become a Player Moderator?");
                        stage = 170;
                        break;
                    case 4:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "What can Player Moderators not do?");
                        stage = 180;
                        break;
                }
                break;
            case 70:
                player.lock(4);
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "At once. Take a look at my book here.");
                npc.animate(new Animation(6866));
                World.submit(new Pulse(4) {

                    @Override
                    public boolean pulse() {
                        player.getDialogueInterpreter().open(496107759);
                        return true;
                    }
                });
                stage = 71;
                break;
            case 71:
                end();
                break;

            case 100:
                int rand = RandomUtil.random(1, 5);
                switch (rand) {
                    case 1:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "If the chat window is moving too quickly to report a", "player accurately, run to a quiet spot and review the chat", "at your leisure!");
                        stage = 2;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "If you're lost and have no idea where to go, use the Home", "Teleporter spell for free!");
                        stage = 2;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Make your recovery questions and answers hard to guess", "but easy to remember.");
                        stage = 2;
                        break;
                    case 4:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Beware of players trying to lue you into the wilderness.", "Your items cannot be returned if you lose them!");
                        stage = 2;
                        break;
                    case 5:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "" + Constants.SERVER_NAME + " will never email you asking for your log-in details.");
                        stage = 2;
                        break;
                }
                stage = 2;
                break;
            case 150:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Player Moderators are normal players of the game, just", "like you. However, since they have shown themselves to be", "trustworthy and active reporters, they have been invited", "by Jagex to monitor the game and take appropriate");
                stage = 151;
                break;
            case 151:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "action when they see rule breaking. You can spot a Player", "Moderator in game by looking at the chat screen - when a", "Player Moderator speaks, a silver crown appears to the", "left of their name. Remember, if there's no silver crown");
                stage = 152;
                break;
            case 152:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "there, they are not a Player Moderator! You can check", "out the website if you'd like more information.");
                stage = 153;
                break;
            case 153:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Thanks!");
                stage = 154;
                break;
            case 154:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Is there anything else you'd like to know?");
                stage = 51;
                break;
            case 160:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Player Moderators, or 'P-mods', have the ability to mute", "rule breakers and " + Constants.SERVER_NAME + " view their reports as a priority so", "that action is taken as quickly as possible. P-Mods also", "have acces to the Player Moderator Centre. Within the");
                stage = 161;
                break;
            case 161:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Centre are tools to help them Moderate " + Constants.SERVER_NAME + ".", "These tools include dedicated forums, the Player", "Moderator Guidelines and the Player Moderator Code of", "Conduct.");
                stage = 153;
                break;
            case 170:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, Constants.SERVER_NAME + " picks players who spend their time and effort to", "help better the " + Constants.SERVER_NAME + " community. To increase your", "chances of becoming a Player Moderator:");
                stage = 171;
                break;
            case 171:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Keep your account secure! This is very important, as a", "player with poor security will never be a P-Mod. Read our", "Security Tips for more information.");
                stage = 173;
                break;
            case 173:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Play by the rules! The rules of " + Constants.SERVER_NAME + " are enforced", "for a reason, to make the game a fair and enjoyable", "environment for all.");
                stage = 174;
                break;
            case 174:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Report accuratley! When " + Constants.SERVER_NAME + " consider an account for", "review they look for quality, not quantity. Ensure your", "reports are of a high quality by following the report", "guidelines.");
                stage = 175;
                break;
            case 175:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Be nice to each other! Treat others as you would", "want to be treated yourself. Respect your fellow player.", "More information can be found on the website.");
                stage = 153;
                break;
            case 180:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "P-Mods cannot ban your account - they can only report", "offences. " + Constants.SERVER_NAME + " then take action based on the evidence", "received. If you lose your password or get scamme dby", "another player, P_Mods cannot help you get your account");
                stage = 181;
                break;
            case 181:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "back. All they can do is recommend you to go to Player", "Support. They cannot retrieve any items you may have", "lost and they certainly do not recieve any free items", "from " + Constants.SERVER_NAME + " for moderating the game. They are players");
                stage = 182;
                break;
            case 182:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "who give their all to help the community, out of the", "goodness of their hearts! P-mods do not work for " + Constants.SERVER_NAME + "", "and so cannot make you a Moderator, or recommend", "other accounts to become Moderators. If you wish yo");
                stage = 183;
                break;
            case 183:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "become a Moderator, feel free to ask me!");
                stage = 153;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6135, 6136, 6137, 6138, 6139 };
    }
}
