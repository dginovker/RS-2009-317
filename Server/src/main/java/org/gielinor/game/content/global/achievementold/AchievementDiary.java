package org.gielinor.game.content.global.achievementold;

import java.util.Map;

import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents an achievement diary.
 *
 * @author Torchic
 *         <p>
 *         TODO Rewards
 */
public abstract class AchievementDiary {

    /**
     * Represents the red color string.
     */
    protected static final String RED = "<col=8A0808>";

    /**
     * Represents the yellow color string.
     */
    protected static final String YELLOW = "<col=ffd700>";

    /**
     * Represents the state of the quest.
     */
    protected AchievementState state = AchievementState.NOT_STARTED;

    /**
     * The player instance.
     */
    protected Player player;

    /**
     * The index represents the button id index.
     */
    protected int index = 0;

    /**
     * Constructs a new {@code Quest} {@code Object}.
     *
     * @param player the player.
     */
    public AchievementDiary(Player player) {
        this.player = player;
    }

    /**
     * Method will update your quest stage on the interface.
     */
    public abstract void update();

    /**
     * Method will finish the quest.
     */
    public abstract void finish();

    /**
     * Gets the index id of the achievement diary.
     *
     * @return
     */
    public abstract int getIndex();

    /**
     * Gets the quest name.
     *
     * @return the name of the quest.
     */
    public abstract String getName();

    /**
     * Clears the screen.
     */
    public void clear() {
        player.getActionSender().sendString("<col=8A0808>Achievement Diary - " + getName() + "</col>", 46753);
        // player.getActionSender().sendString(getStateString() + getName() + " Area Tasks</col>", JOURNAL_COMPONENT, 11);
    }

    /**
     * Sends the achievement diary information.
     *
     * @param player      The player.
     * @param information The information.
     */
    public static void sendDiary(Player player, String... information) {
        sendComponents(player, information);
    }

    /**
     * Sends the component strings for the achievement diary information interface.
     *
     * @param player The player.
     * @param lines  The lines to display.
     */
    private static void sendComponents(Player player, String[] lines) {
        player.getActionSender().sendQuestInterface(null, lines);
    }

    /**
     * Decreases the amount.
     */
    public static void decrease(Player player, AchievementTask achievementTask, int amount) {
        if (player.getAchievementRepository().getFinishedAchievements().contains(achievementTask)) {
            return;
        }

        Map<AchievementTask, Integer> achievements = player.getAchievementRepository().getAchievements();
        if (!achievements.containsKey(achievementTask)) {
            achievements.put(achievementTask, 0);
        }
        int currentAmount = achievements.get(achievementTask) + amount;
        player.getAchievementRepository().getAchievements().replace(achievementTask, currentAmount);

        int amountLeft = achievementTask.getAmount() - currentAmount;
        if (amountLeft <= 0) {
            finalize(player, achievementTask);
            return;
        }
        // TODO Colour
        if (achievementTask.getAmount() <= 10) {
            player.getActionSender().sendMessage("<col=a7044e>Achievement progress: " + currentAmount + "/" + achievementTask.getAmount() + ".");
        } else if (achievementTask.getAmount() <= 100000 && amountLeft % 1000 == 0) {
            player.getActionSender().sendMessage("<col=a7044e>Achievement progress: " + TextUtils.getFormattedNumber(currentAmount) + "/" + TextUtils.getFormattedNumber(achievementTask.getAmount()) + ".");
        } else if (amountLeft % 10 == 0) {
            player.getActionSender().sendMessage("<col=a7044e>Achievement progress: " + currentAmount + "/" + achievementTask.getAmount() + ".");
        }
        player.getAchievementRepository().update(player);
        if (amountLeft <= 0) {
            finalize(player, achievementTask);
        }
    }

    /**
     * Finalizes the completion of an achievement diary section.
     */
    public static void finalize(Player player, AchievementTask achievementTask) {
        if (player.getAchievementRepository().getFinishedAchievements().contains(achievementTask)) {
            return;
        }
        achievementTask.finish(player);
        player.getAchievementRepository().update(player);
    }

    /**
     * Method used to start an achievement diary.
     */
    public void start() {

    }

    /**
     * Checks to see if the player still has a specific item.
     *
     * @param item The item to check.
     * @return True if the player still has the item.
     */
    public final boolean hasItem(final Item item) {
        return (player.getInventory().containsItem(item) || player.getBank().containsItem(item));
    }

    /**
     * Gets the state.
     *
     * @return The state.
     */
    public AchievementState getState() {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state The state to set.
     */
    public void setState(AchievementState state) {
        this.state = state;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player.
     *
     * @param player The player to set.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Sets the index.
     *
     * @param index The index to set.
     */
    public void setIndex(int index) {
        this.index = index;
    }


    /**
     * Adds an {@link org.gielinor.game.content.global.achievementold.impl.AchievementTask}.
     *
     * @param achievementTask The {@link org.gielinor.game.content.global.achievementold.impl.AchievementTask}.
     * @return The string.
     */
    public String add(AchievementTask achievementTask) {
        return (player.getAchievementRepository().getFinishedAchievements().contains(achievementTask) ? "<str>" : "") +
            achievementTask.getTask();
    }
}
