package plugin.dialogue;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.Difficulty;
import org.gielinor.game.content.global.achievementold.impl.LumbridgeDraynorDiary;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.config.Constants;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for Assistant Serf.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AssistantSerfDialogue extends DialoguePlugin {

    /**
     * The {@link org.gielinor.game.content.global.achievementold.AchievementDiary}.
     */
    private AchievementDiary achievementDiary;

    public AssistantSerfDialogue() {
    }

    public AssistantSerfDialogue(Player player) {
        super(player);
        achievementDiary = player.getAchievementRepository().getDiary(LumbridgeDraynorDiary.NAME);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new AssistantSerfDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npc("Hello there, " + TextUtils.formatDisplayName(player.getName()) + "!");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        boolean antiqueLampEasy = player.getAttribute("claimed_easy_lumb", false);
        boolean antiqueLampMedium = player.getAttribute("claimed_medium_lumb", false);
        boolean antiqueLampHard = player.getAttribute("claimed_hard_lumb", false);
        boolean claimedHard = player.getInventory().contains(Item.EXPLORERS_RING_3) || player.getEquipment().contains(Item.EXPLORERS_RING_3) || player.getBank().contains(Item.EXPLORERS_RING_3);
        boolean claimedMedium = player.getInventory().contains(Item.EXPLORERS_RING_2) || player.getEquipment().contains(Item.EXPLORERS_RING_2) || player.getBank().contains(Item.EXPLORERS_RING_2);
        boolean claimedEasy = player.getInventory().contains(Item.EXPLORERS_RING_1) || player.getEquipment().contains(Item.EXPLORERS_RING_1) || player.getBank().contains(Item.EXPLORERS_RING_1);
        List<Item> rewards = new ArrayList<>();
        AchievementDiary achievementDiary = player.getAchievementRepository().getDiary(LumbridgeDraynorDiary.NAME);
        switch (stage) {
            case 0:
                options("Who are you?", "May I claim my rewards?", "Nothing.");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        player("Who are you?");
                        stage = 2;
                        break;
                    case THREE_OPTION_TWO:
                        player("May I claim my rewards?");
                        stage = 6;
                        break;
                    case THREE_OPTION_THREE:
                        stage = END;
                        player("Nothing.");
                        break;
                }
                break;
            case 2:
                npc("I'm Serf, I reward those who put effort in to", "complete diaries on "
                        + Constants.SERVER_NAME + ".", "Achievement diaries can be completed by doing various tasks",
                    "which can range from easy to elite.");
                stage = 3;
                break;
            case 3:
                npc("Rewards are given upon completing a difficulty set", "within the location of the diary. In order to claim", "a reward, the player must finish the easiest to hardest", " diaries before they receive any reward.");
                stage = 4;
                break;
            case 4:
                npc("For example, if you complete the Lumbridge & Draynor", "medium set, you cannot claim the reward for it unless", "you have completed the easy.");
                stage = 5;
                break;
            case 5:
                options("Who are you?", "Can I claim my rewards?", "Nothing");
                stage = 1;
                break;
            case 6:
                if (player.getAchievementRepository().isDifficultyComplete(achievementDiary, Difficulty.HARD)) {
                    if (!player.getAchievementRepository().isDifficultyComplete(achievementDiary, Difficulty.EASY) || !player.getAchievementRepository().isDifficultyComplete(achievementDiary, Difficulty.MEDIUM)) {
                        npc("I see you have completed all of the Hard difficulty", "achievement diaries! However, I cannot reward you with anything", "until you complete the easy and medium sets.");
                        stage = END;
                        break;
                    } else {
                        if (!claimedHard) {
                            rewards.add(new Item(Item.EXPLORERS_RING_3, 1));
                        }
                        if (!antiqueLampHard) {
                            player.saveAttribute("claimed_hard_lumb", true);
                            rewards.add(new Item(11141));
                        }
                    }
                }
                if (player.getAchievementRepository().isDifficultyComplete(achievementDiary, Difficulty.MEDIUM)) {
                    if (!player.getAchievementRepository().isDifficultyComplete(achievementDiary, Difficulty.EASY)) {
                        npc("I see you have completed all of the Medium difficulty", "achievement diaries! However, I cannot reward you with anything", "until you complete the easy and medium sets.");
                        stage = END;
                        break;
                    } else {
                        if (!claimedMedium) {
                            rewards.add(new Item(Item.EXPLORERS_RING_2, 1));
                        }
                        if (!antiqueLampMedium) {
                            player.saveAttribute("claimed_medium_lumb", true);
                            rewards.add(new Item(11139));
                        }
                    }
                }
                if (player.getAchievementRepository().isDifficultyComplete(achievementDiary, Difficulty.EASY)) {
                    if (!claimedEasy) {
                        rewards.add(new Item(Item.EXPLORERS_RING_1, 1));
                    }
                    if (!antiqueLampEasy) {
                        player.saveAttribute("claimed_easy_lumb", true);
                        rewards.add(new Item(11137));
                    }
                }
                if (rewards.size() == 0) {
                    npc("It looks like you already have all of the rewards", "from the difficulty sets you've completed!");
                    stage = END;
                    break;
                }
                npc("Sure! Here you go!");
                for (Item reward : rewards) {
                    player.getInventory().add(reward, player);
                    player.getActionSender().sendMessage("Serf rewards you with an " + reward.getName() + "!");
                }
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
        return new int[]{ 5053 };
    }
}
