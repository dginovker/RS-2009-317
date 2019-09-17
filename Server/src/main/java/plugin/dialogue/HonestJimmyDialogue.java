package plugin.dialogue;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.config.Constants;
import org.gielinor.utilities.voting.VoteReward;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for Honest Jimmy.
 *
 * @author Logan G. <logan@Gielinor.org>
 */
public class HonestJimmyDialogue extends DialoguePlugin {

    /**
     * The {@link VoteReward} the player has chosen.
     */
    private VoteReward voteReward;

    /**
     * Constructs a new {@link plugin.dialogue.HonestJimmyDialogue} {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     */
    public HonestJimmyDialogue() {
    }

    /**
     * Constructs a new {@link plugin.dialogue.HonestJimmyDialogue} {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     *
     * @param player The player.
     */
    public HonestJimmyDialogue(Player player) {
        super(player);
    }

    /**
     * Opens the reward screen for the player.
     *
     * @param player The player.
     */
    public static void openRewardScreen(Player player) {
        int points = player.getSavedData().getGlobalData().getVotingPoints();
        player.getActionSender().sendString(27259, "Voting Rewards (" + points + " Available Points)");
        int index = 27167;
        int slot = 27197;
        for (int clearId = index; clearId < 27197; clearId++) {
            player.getActionSender().sendString(clearId, "");
        }
        for (int slotId = 0; slotId < 30; slotId++) {
            player.getActionSender().sendUpdateItem(slot + slotId, 0, null);
        }
        for (VoteReward voteReward : VoteReward.values()) {
            String rewardColor = (voteReward.canApplyReward(player) == null) ? "<col=1DA41D>" : "<col=C46603>";
            String color = points >= voteReward.getCost() ? "<col=1DA41D>" : "<col=C46603>";
            player.getActionSender().sendString(index, rewardColor + voteReward.getText() + "<br>" + color + voteReward.getCost() + " Points");
            if (voteReward.getItem() != null) {
                player.getActionSender().sendUpdateItem(slot, 0, voteReward.getItem());
            }
            index++;
            slot++;
        }
        player.getInterfaceState().open(new Component(27135));
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new HonestJimmyDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (args.length > 1) {
            voteReward = (VoteReward) args[1];
            npc("Are you sure you want the ", voteReward.getText().toLowerCase() + "? It will cost you " + voteReward.getCost() + " voting points.");
            stage = 30;
            return true;
        }
        player("Hello.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                npc("Why, hello there! What can I help you with?");
                stage = 1;
                break;
            case 1:
                options("What are you doing here?", "How many voting points do I have?", "May I view the voting rewards?", "Nothing.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect) {
                    case FOUR_OPTION_ONE:
                        player("What are you doing here?");
                        stage = 3;
                        break;
                    case FOUR_OPTION_TWO:
                        player("How many voting points do I have?");
                        stage = 7;
                        break;
                    case FOUR_OPTION_THREE:
                        player("May I view the voting rewards?");
                        stage = 9;
                        break;
                    case FOUR_OPTION_FOUR:
                        player("Nothing.");
                        stage = END;
                        break;
                }
                break;
            case 3:
                npc("When a player votes for " + Constants.SERVER_NAME + ", I stand by ready", "to reward them for their doing so.", "I have many rewards to choose from, and I'm", "always finding new things!");
                stage = 4;
                break;
            case 4:
                npc("You can vote by visiting http://Gielinor.org/vote/", "or by typing ::vote in your chat, which will open the voting page for you.");
                stage = 5;
                break;
            case 5:
                npc("Once you have voted, after a few minutes,", "your account will be rewarded the points.");
                stage = 6;
                break;
            case 6:
                npc("Is there anything else I can help you with?");
                stage = 1;
                break;
            case 7:
                npc("You currently have " + player.getSavedData().getGlobalData().getVotingPoints() + " voting points.", "Would you like to view the rewards?");
                stage = 8;
                break;
            case 8:
                options("Yes, please.", "No thank you.");
                stage = 10;
                break;
            case 10:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        end();
                        openRewardScreen(player);
                        break;
                    case TWO_OPTION_TWO:
                        player("No thank you.");
                        stage = 1;
                        break;
                }
                break;
            case 9:
                end();
                openRewardScreen(player);
                break;
            case 30:
                options("Yes, please.", "No thank you.");
                stage = 31;
                break;
            case 31:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Yes, please.");
                        stage = 32;
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        openRewardScreen(player);
                        break;
                }
                break;
            case 32:
                // TODO move to the VoteReward enum
                String[] applyReward = voteReward.canApplyReward(player);
                if (applyReward != null) {
                    npc(applyReward);
                    stage = END;
                    break;
                }
                if (player.getSavedData().getGlobalData().getVotingPoints() < voteReward.getCost()) {
                    npc("I'm afraid you do not have enough points for that.");
                    stage = END;
                    break;
                }
                if (voteReward.getRewards() != null && voteReward.getRewards().length > 0) {
                    if (player.getInventory().freeSlots() < voteReward.getRewards().length) {
                        npc("You don't have enough inventory space for this reward.", "You need at least " + voteReward.getRewards().length + " free spaces.");
                        stage = END;
                        break;
                    }
                    player.getSavedData().getGlobalData().decreaseVotingPoints(voteReward.getCost());
                    for (Item item : voteReward.getRewards()) {
                        player.getInventory().add(item);
                    }
                    npc("Here you go!", "You have " + player.getSavedData().getGlobalData().getVotingPoints() + " voting points left.", "Be sure to vote every 12 hours!");
                    stage = 33;
                    break;
                }
                player.getSavedData().getGlobalData().decreaseVotingPoints(voteReward.getCost());
                voteReward.applyReward(player);
                npc("There you go!", "You have " + player.getSavedData().getGlobalData().getVotingPoints() + " voting points left.", "Be sure to vote every 12 hours!");
                stage = 33;
                break;
            case 33:
                player("Thanks!");
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
        return new int[]{ 4362 };
    }
}
