package org.gielinor.game.content.global.achievementold.impl;

import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.AchievementState;
import org.gielinor.game.node.entity.player.Player;

/**
 * Handles the Lumbdrige Draynor Diary.
 *
 * @author Torchic
 */
public class LumbridgeDraynorDiary extends AchievementDiary {

    /**
     * The name of this diary.
     */
    public static final String NAME = "Lumbridge & Draynor";

    /**
     * Constructs a new {@Code LumbridgeDraynorDiary} {@Code Object}
     *
     * @param player The player.
     */
    public LumbridgeDraynorDiary(Player player) {
        super(player);
    }

    @Override
    public void update() {
        clear();
        sendDiary(player,
            "<col=800000>Beginner",
            add(AchievementTask.SLAY_CAVE_BUG_SWAMP_CAVES),
            add(AchievementTask.SEDRIDOR_RUNE_ESS_TELEPORT),
            add(AchievementTask.CRAFT_WATER_RUNES),
            add(AchievementTask.PICKPOCKET_MAN_WOMAN),
            add(AchievementTask.CHOP_BURN_OAK_LOGS),
            add(AchievementTask.KILL_ZOMBIE_SEWERS),
            add(AchievementTask.CATCH_ANCHOVIES),
            add(AchievementTask.BAKE_BREAD),
            add(AchievementTask.MINE_IRON_ORE),
            add(AchievementTask.HAM),
            "",
            "<col=800000>Medium",
            add(AchievementTask.UPGRADED_DEVICE_AVA),
            add(AchievementTask.LUMBRIDGE_TELEPORT),
            add(AchievementTask.CATCH_SALMON),
            add(AchievementTask.CRAFT_COIF),
            add(AchievementTask.WILLOW_LOGS_DRAYNOR),
            add(AchievementTask.PICKPOCKET_MARTIN),
            add(AchievementTask.SLAYER_CHAELDAR),
            add(AchievementTask.LAVA_RUNES),
            "",
            "<col=800000>Hard",
            add(AchievementTask.JUTTING_WALL),
            add(AchievementTask.CRAFT_56_COSMIC),
            add(AchievementTask.CRAFT_AMULET_POWER),
            add(AchievementTask.GODS_GIVE_ME_STRENGTH)
        );

    }

    @Override
    public void finish() {
        player.getActionSender().sendMessage("<col=a7044e>Congratulations! You have completed all tasks in the Lumbridge & Draynor diary.");
        player.getActionSender().sendMessage("<col=a7044e>Talk to Serf outside of Lumbridge castle to claim your reward!");
        setState(AchievementState.COMPLETED);
    }

    @Override
    public int getIndex() {
        return 2;
    }

    @Override
    public String getName() {
        return NAME;
    }

}
