package plugin.dialogue;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the fadli dialogue plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FadliDialogue extends DialoguePlugin {

    private static final Logger log = LoggerFactory.getLogger(FadliDialogue.class);

    /**
     * Constructs a new {@code FadliDialogue} {@code Object}.
     */
    public FadliDialogue() {
    }

    /**
     * Constructs a new {@code FadliDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public FadliDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new FadliDialogue(player);
    }

    @Override
    public void init() {
        super.init();
        try {
            new FadliPlugin().newInstance(null);
        } catch (Throwable t) {
            log.error("Failed to init Fadli dialogue.", t);
        }
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hi!");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "What?");
                stage = 1;
                break;
            case 1:
                interpreter.sendOptions("Select an Option", "What do you do?", "What is this place?", "I'd like to store some items please.", "Do you watch any matches?");
                stage = 2;
                break;
            case 2:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "What do you do?");
                        stage = 10;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "What is this place?");
                        stage = 20;
                        break;
                    case 3:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I'd like to store some items please.");
                        stage = 30;
                        break;
                    case 4:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Do you watch any matches?");
                        stage = 40;
                        break;
                    case 40:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "When I can.");
                        stage = 40;
                        break;
                }
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "You can store your stuff here if you want. You can", "dump anything you don't want to carry whilst your", "fighting duels and then pick it up again on the way out.");
                stage = 11;
                break;
            case 11:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "To be honest I'm wasted here.");
                stage = 12;
                break;
            case 12:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I should be winning duels in an arena! I'm the best", "warrior in Al Kharid!");
                stage = 13;
                break;
            case 13:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Easy, tiger!");
                stage = 14;
                break;
            case 14:
                end();
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Isn't it obvious?");
                stage = 21;
                break;
            case 21:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "This is the Duel Arena...duh!");
                stage = 22;
                break;
            case 22:
                end();
                break;
            case 30:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Sure.");
                stage = 31;
                break;
            case 31:
                end();
                player.getBank().open();
                break;
            case 40:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Most aren't any good so I throw rotten fruit at them!");
                stage = 41;
                break;
            case 41:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 958 };
    }

    /**
     * Represents the faldi interaction plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class FadliPlugin extends OptionHandler {

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            NPCDefinition.forId(958).getConfigurations().put("option:buy", this);
            return this;
        }

        @Override
        public boolean handle(Player player, Node node, String option) {
            Shops.SHOP_OF_DISTATE.open(player);
            return true;
        }


    }
}
