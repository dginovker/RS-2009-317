package org.gielinor.game.content.global.achievementold.impl;

import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.AchievementState;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the global achievement diary.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GlobalDiary extends AchievementDiary {

    /**
     * The name of this diary.
     */
    public static final String NAME = "Global";

    /**
     * Constructs a new {@link org.gielinor.game.content.global.achievementold.impl.GlobalDiary} {@link org.gielinor.game.content.global.achievementold.AchievementDiary}.
     *
     * @param player The player.
     */
    public GlobalDiary(Player player) {
        super(player);
    }

    @Override
    public void update() {
        clear();
        sendDiary(player,
            "<col=800000>Beginner",
            add(AchievementTask.BUY_SHOP),
            add(AchievementTask.SELL_SHOP),
            add(AchievementTask.ITEM_PACK_OPEN),
            add(AchievementTask.TESTING_THE_WATER),
            add(AchievementTask.TO_THE_GODS),
            "",
            "<col=800000>Easy",
            add(AchievementTask.PICKPOCKET_MEN_10),
            add(AchievementTask.SHRIMP_100),
            add(AchievementTask.SMALL_AGILE),
            add(AchievementTask.NO_TIME_WASTED),
            "",
            "<col=800000>Medium",
            add(AchievementTask.MAKING_FONZIE_PROUD),
            add(AchievementTask.BONE_SHORTAGE),
            add(AchievementTask.ESCAPE_ARTIST),
            add(AchievementTask.SMOKY_ROOM)
        );
    }

    @Override
    public void finish() {
        player.getActionSender().sendMessage("<col=a7044e>Congratulations! You have completed all tasks in the global diary.");
        //player.getActionSender().sendMessage("<col=a7044e>Talk to Runolf at home to claim your reward!");
        setState(AchievementState.COMPLETED);
    }

    @Override
    public int getIndex() {
        return 1;
    }

    @Override
    public String getName() {
        return NAME;
    }

}
