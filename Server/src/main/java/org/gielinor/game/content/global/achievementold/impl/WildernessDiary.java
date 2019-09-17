package org.gielinor.game.content.global.achievementold.impl;

import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.AchievementState;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the Wilderness achievement diary.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class WildernessDiary extends AchievementDiary {

    /**
     * The name of this diary.
     */
    public static final String NAME = "Wilderness";

    /**
     * Constructs a new {@link org.gielinor.game.content.global.achievementold.impl.WildernessDiary} {@link org.gielinor.game.content.global.achievementold.AchievementDiary}.
     *
     * @param player The player.
     */
    public WildernessDiary(Player player) {
        super(player);
    }

    @Override
    public void update() {
        clear();
        sendDiary(player,
            "<col=800000>Beginner",

            "",
            "<col=800000>Easy",

            "",
            "<col=800000>Medium",
            add(AchievementTask.TAKING_THE_RISK)

        );
    }

    @Override
    public void finish() {
        player.getActionSender().sendMessage("<col=a7044e>Congratulations! You have completed all tasks in the Wilderness diary.");
        setState(AchievementState.COMPLETED);
    }

    @Override
    public int getIndex() {
        return 3;
    }

    @Override
    public String getName() {
        return NAME;
    }

}
