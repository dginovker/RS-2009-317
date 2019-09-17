package plugin.dialogue;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Direction;

/**
 * Rerpresents the enchanted gem dialogue.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class EnchantedGemDialogue extends DialoguePlugin {

    /**
     * Represents the id associated with this plugin.
     */
    public static final int ID = 77777;

    /**
     * Constructs a new {@code EnchantedGemDialogue} {@code Object}.
     */
    public EnchantedGemDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code EnchantedGemDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public EnchantedGemDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new EnchantedGemDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        player.faceLocation(player.getLocation().transform(1, 0, 0));
        player.setDirection(Direction.EAST);
        interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "Hello there " + player.getUsername() + ", what can I help you with?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "How am I doing so far?", "Who are you?", "Where are you?", "Got any tips for me?", "Nothing really.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect) {
                    case FIVE_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "How am I doing so far?");
                        stage = 10;
                        break;
                    case FIVE_OPTION_TWO:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Who are you?");
                        stage = 20;
                        break;
                    case FIVE_OPTION_THREE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Where are you?");
                        stage = 30;
                        break;
                    case FIVE_OPTION_FOUR:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Got any tips for me?");
                        stage = 400;
                        break;
                    case FIVE_OPTION_FIVE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Nothing really.");
                        stage = 99;
                        break;

                }
                break;
            case 99:
                end();
                break;
            case 10:
                if (!player.getSlayer().hasTask()) {
                    interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "You need something new to hunt. Come and see me", "When you can and I'll give you a new task.");
                    stage = 11;
                    break;
                }
                interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "You're currently assigned to kill " + NPCDefinition.forId((player.getSlayer().getTask().getNpcs()[0])).getName().toLowerCase() + "s;", "only " + player.getSlayer().getAmount() + " more to go.");
                stage = 11;
                break;
            case 11:
                interpreter.sendOptions("Select an Option", "Who are you?", "Where are you?", "Got any tips for me?", "That's all thanks.");
                stage = 12;
                break;
            case 12:
                switch (optionSelect) {
                    case FOUR_OPTION_ONE:
                        interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "My name's " + NPCDefinition.forId(player.getSlayer().getMaster().getNpc()).getName() + ", I'm the Slayer Master best able", "to train you.");
                        stage = 21;
                        break;
                    case FOUR_OPTION_TWO:
                        interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "You'll find me in " + masterLocation() + ", I'll be here when you need a", "new task.");
                        stage = 31;
                        break;
                    case FOUR_OPTION_THREE:
                        if (!player.getSlayer().hasTask()) {
                            interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "You need something new to hunt.");
                            stage = 99;
                            break;
                        }
                        interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, player.getSlayer().getTask().getTip());
                        stage = 401;
                        break;
                    case FOUR_OPTION_FOUR:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "That's all thanks.");
                        stage = 99;
                        break;
                }
                break;
            case 20:
                interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "My name's " + NPCDefinition.forId(player.getSlayer().getMaster().getNpc()).getName() + ", I'm the Slayer Master best able", "to train you.");
                stage = 21;
                break;
            case 21:
                interpreter.sendOptions("Select an Option", "How am I doing so far?", "Where are you?", "Got any tips for me?", "That's all thanks.");
                stage = 25;
                break;
            case 25:
                switch (optionSelect) {
                    case FOUR_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "How am I doing so far?");
                        stage = 10;
                        break;
                    case FOUR_OPTION_TWO:
                        interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "You'll find me in " + masterLocation() + ", I'll be here when you need a", "new task.");
                        stage = 31;
                        break;
                    case FOUR_OPTION_THREE:
                        if (!player.getSlayer().hasTask()) {
                            interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "You need something new to hunt.");
                            stage = 99;
                            break;
                        }
                        interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, player.getSlayer().getTask().getTip());
                        stage = 401;
                        break;
                    case FOUR_OPTION_FOUR:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "That's all thanks.");
                        stage = 99;
                        break;

                }
                break;
            case 30:
                interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "You'll find me in " + masterLocation() + ", I'll be here when you need a", "new task.");
                stage = 31;
                break;
            case 31:
                interpreter.sendOptions("Select an Option", "How am I doing so far?", "Who are you?", "Got any tips for me?", "That's all thanks.");
                stage = 32;
                break;
            case 32:
                switch (optionSelect) {
                    case FOUR_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "How am I doing so far?");
                        stage = 10;
                        break;
                    case FOUR_OPTION_TWO:
                        interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "My name's " + NPCDefinition.forId(player.getSlayer().getMaster().getNpc()).getName() + ", I'm the Slayer Master best able", "to train you.");
                        stage = 21;
                        break;
                    case FOUR_OPTION_THREE:
                        if (!player.getSlayer().hasTask()) {
                            interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "You need something new to hunt.");
                            stage = 0;
                            break;
                        }
                        interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, player.getSlayer().getTask().getTip());
                        stage = 401;
                        break;
                    case FOUR_OPTION_FOUR:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "That's all thanks.");
                        stage = 99;
                        break;

                }
                break;
            case 400:
                if (!player.getSlayer().hasTask()) {
                    interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "You need something new to hunt.");
                    stage = 0;
                    break;
                }
                interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, player.getSlayer().getTask().getTip());
                stage = 401;
                break;
            case 401:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Great, thanks!");
                stage = 403;
                break;
            case 403:
                interpreter.sendOptions("Select an Option", "How am I doing so far?", "Who are you?", "Where are you?", "That's all thanks.");
                stage = 404;
                break;
            case 404:
                switch (optionSelect) {
                    case FOUR_OPTION_ONE:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "How am I doing so far?");
                        stage = 10;
                        break;
                    case FOUR_OPTION_TWO:
                        interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "My name's " + NPCDefinition.forId(player.getSlayer().getMaster().getNpc()).getName() + ", I'm the Slayer Master best able", "to train you.");
                        stage = 21;
                        break;
                    case FOUR_OPTION_THREE:
                        interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.NO_EXPRESSION, "You'll find me in " + masterLocation() + ", I'll be here when you need a", "new task.");
                        stage = 31;
                        break;
                    case FOUR_OPTION_FOUR:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "That's all thanks.");
                        stage = 99;
                        break;

                }
                break;
        }
        return true;
    }

    /**
     * Method used to get the masters location.
     *
     * @return the location.
     */
    public String masterLocation() {
        if (player.getSlayer().getMaster().getNpc() == org.gielinor.game.content.skill.member.slayer.Master.MAZCHNA.getNpc()) {
            return "Canifis";
        } else if (player.getSlayer().getMaster().getNpc() == org.gielinor.game.content.skill.member.slayer.Master.TURAEL.getNpc()) {
            return "Taverley";
        } else if (player.getSlayer().getMaster().getNpc() == org.gielinor.game.content.skill.member.slayer.Master.CHAELDAR.getNpc()) {
            return "Zanaris";
        } else if (player.getSlayer().getMaster().getNpc() == org.gielinor.game.content.skill.member.slayer.Master.VANNAKA.getNpc()) {
            return "Edgeville dungeon";
        } else if (player.getSlayer().getMaster().getNpc() == org.gielinor.game.content.skill.member.slayer.Master.DURADEL.getNpc()) {
            return "Shilo village";
        }
        return null;
    }

    @Override
    public int[] getIds() {
        return new int[]{ ID };
    }
}
