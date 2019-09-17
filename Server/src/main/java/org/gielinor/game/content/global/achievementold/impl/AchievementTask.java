package org.gielinor.game.content.global.achievementold.impl;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.content.global.achievementold.AchievementState;
import org.gielinor.game.content.global.achievementold.Difficulty;
import org.gielinor.game.node.entity.player.Player;

/**
 * The Lumbridge/Draynor tasks.
 *
 * @author Torchic
 */
public enum AchievementTask {

    /**
     * Global tasks.
     */
    BUY_SHOP("Buy an item from a shop", Difficulty.EASY, GlobalDiary.NAME),
    SELL_SHOP("Sell an item to a shop", Difficulty.EASY, GlobalDiary.NAME),
    ITEM_PACK_OPEN("Open any item pack", Difficulty.EASY, GlobalDiary.NAME),
    TESTING_THE_WATER("Teleport to a Training area", Difficulty.EASY, GlobalDiary.NAME),
    TO_THE_GODS("Pray at an altar with 0 prayer points", Difficulty.EASY, GlobalDiary.NAME),
    PICKPOCKET_MEN_10("Pickpocket men 10 times", Difficulty.MEDIUM, GlobalDiary.NAME, 10),
    SHRIMP_100("Fish 100 Shrimp from any fishing spot", Difficulty.MEDIUM, GlobalDiary.NAME, 100),
    SMALL_AGILE("Complete a lap at the Gnome Stronghold course", Difficulty.MEDIUM, GlobalDiary.NAME, 7),
    NO_TIME_WASTED("Kill any NPC with a bronze scimitar", Difficulty.MEDIUM, GlobalDiary.NAME),
    MAKING_FONZIE_PROUD("Craft 28 leather bodies", Difficulty.HARD, GlobalDiary.NAME, 28),
    BONE_SHORTAGE("Bury 50 regular bones", Difficulty.HARD, GlobalDiary.NAME, 50),
    ESCAPE_ARTIST("Teleport away from a PvM situation with 1 HP", Difficulty.HARD, GlobalDiary.NAME),
    SMOKY_ROOM("Cast the Smoke Rush spell", Difficulty.HARD, GlobalDiary.NAME),

    /**
     * Lumbridge & Draynor tasks.
     */
    SLAY_CAVE_BUG_SWAMP_CAVES("Slay a cave bug in the Lumbridge Swamp Caves", Difficulty.EASY, LumbridgeDraynorDiary.NAME),
    SEDRIDOR_RUNE_ESS_TELEPORT("Have Sedridor teleport you to the Rune essence mine", Difficulty.EASY, LumbridgeDraynorDiary.NAME),
    CRAFT_WATER_RUNES("Craft some water runes", Difficulty.EASY, LumbridgeDraynorDiary.NAME),
    PICKPOCKET_MAN_WOMAN("Pickpocket a man or woman in Lumbridge", Difficulty.EASY, LumbridgeDraynorDiary.NAME),
    CHOP_BURN_OAK_LOGS("Chop and burn some oak logs in Lumbridge ", Difficulty.EASY, LumbridgeDraynorDiary.NAME, 2),
    KILL_ZOMBIE_SEWERS("Kill a zombie in the Draynor Sewers", Difficulty.EASY, LumbridgeDraynorDiary.NAME),
    CATCH_ANCHOVIES("Catch some anchovies in Al-Kharid", Difficulty.EASY, LumbridgeDraynorDiary.NAME),
    BAKE_BREAD("Bake some bread on the Lumbridge kitchen range", Difficulty.EASY, LumbridgeDraynorDiary.NAME),
    MINE_IRON_ORE("Mine some iron ore at the Al-Kharid mine", Difficulty.EASY, LumbridgeDraynorDiary.NAME),
    HAM("Enter the H.A.M. Hideout", Difficulty.EASY, LumbridgeDraynorDiary.NAME),
    //
    UPGRADED_DEVICE_AVA("Purchase an upgraded device from Ava", Difficulty.MEDIUM, LumbridgeDraynorDiary.NAME),
    LUMBRIDGE_TELEPORT("Cast the Lumbridge Teleport spell (cities)", Difficulty.MEDIUM, LumbridgeDraynorDiary.NAME),
    CATCH_SALMON("Catch some salmon in Lumbridge", Difficulty.MEDIUM, LumbridgeDraynorDiary.NAME),
    CRAFT_COIF("Craft a coif in the Lumbridge cow pen (eastern cows)", Difficulty.MEDIUM, LumbridgeDraynorDiary.NAME),
    WILLOW_LOGS_DRAYNOR("Chop some willow logs in Draynor Village", Difficulty.MEDIUM, LumbridgeDraynorDiary.NAME),
    PICKPOCKET_MARTIN("Pickpocket Martin the Master Gardener", Difficulty.MEDIUM, LumbridgeDraynorDiary.NAME),
    SLAYER_CHAELDAR("Get a slayer task from Chaeldar", Difficulty.MEDIUM, LumbridgeDraynorDiary.NAME),
    LAVA_RUNES("Craft some lava runes at the Fire altar in Al Kharid", Difficulty.MEDIUM, LumbridgeDraynorDiary.NAME),
    //
    // BONES_TO_PEACHES("Cast Bones to Peaches in Al kharid Palace", Difficulty.HARD, LumbridgeDraynorDiary.NAME),
    JUTTING_WALL("Squeeze past the jutting wall on your way to the Cosmic altar", Difficulty.HARD, LumbridgeDraynorDiary.NAME),
    CRAFT_56_COSMIC("Craft 56 cosmic runes simultaneously", Difficulty.HARD, LumbridgeDraynorDiary.NAME),
    CRAFT_AMULET_POWER("Craft, string and enchant an amulet of power in Lumbridge", Difficulty.HARD, LumbridgeDraynorDiary.NAME, 3),
    GODS_GIVE_ME_STRENGTH("Pray at Lumbridge Church altar with Mystic Might enabled", Difficulty.HARD, LumbridgeDraynorDiary.NAME),

    /**
     * Wilderness tasks.
     */
    TAKING_THE_RISK("Enter level 30 wilderness", Difficulty.HARD, WildernessDiary.NAME),;

    /**
     * The task to display for players who open the interface.
     */
    private String task;

    /**
     * The difficulty level of the task.
     */
    private Difficulty difficulty;

    /**
     * The task diary name for the location.
     */
    private String taskDiaryName;

    /**
     * The amount required.
     */
    private int amount;

    /**
     * Constructs a new {@Code LumbridgeDraynorTasks} {@Code Object}
     *
     * @param task          The task string.
     * @param difficulty    The task difficulty.
     * @param taskDiaryName The name of the diary for the location.
     */
    AchievementTask(String task, Difficulty difficulty, String taskDiaryName) {
        this.task = task;
        this.difficulty = difficulty;
        this.taskDiaryName = taskDiaryName;
        this.amount = 1;
    }

    /**
     * Constructs a new {@Code LumbridgeDraynorTasks} {@Code Object}
     *
     * @param task          The task string.
     * @param difficulty    The task difficulty.
     * @param taskDiaryName The name of the diary for the location.
     */
    AchievementTask(String task, Difficulty difficulty, String taskDiaryName, int amount) {
        this.task = task;
        this.difficulty = difficulty;
        this.taskDiaryName = taskDiaryName;
        this.amount = amount;
    }

    /**
     * Gets the task string.
     *
     * @return
     */
    public String getTask() {
        return task;
    }

    /**
     * Gets the task difficulty.
     *
     * @return
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Gets the task diary name for the location.
     *
     * @return
     */
    public String getTaskLocation() {
        return taskDiaryName;
    }

    public int getAmount() {
        return amount;
    }

    /**
     * Gets an {@link org.gielinor.game.content.global.achievementold.impl.AchievementTask} by name.
     *
     * @param name The name.
     * @return The {@code AchievementTask}.
     */
    public static AchievementTask forName(String name) {
        for (AchievementTask achievementTask : AchievementTask.values()) {
            if (achievementTask.name().equals(name)) {
                return achievementTask;
            }
        }
        return null;
    }

    /**
     * Gets a {@link java.util.List} of {@link org.gielinor.game.content.global.achievementold.impl.AchievementTask}s for a {@code name}.
     *
     * @param name The name of the task location.
     * @return The {@code List}.
     */
    public static List<AchievementTask> forTaskLocation(String name) {
        List<AchievementTask> achievementTaskList = new ArrayList<>();
        for (AchievementTask achievementTask : AchievementTask.values()) {
            if (achievementTask.getTaskLocation().equals(name)) {
                achievementTaskList.add(achievementTask);
            }
        }
        return achievementTaskList;
    }

    public void finish(Player player) {
        String taskLocation = getTaskLocation();
        if (getTaskLocation().equals(GlobalDiary.NAME)) {
            taskLocation = "global";
        }
        player.getActionSender().sendMessage("<col=a7044e>Well done! You have completed " + getDifficulty().getType() + " task in the " + taskLocation + " diary. Your");
        player.getActionSender().sendMessage("<col=a7044e>Achievement Diary has been updated.");
        player.getAchievementRepository().addFinishedAchievement(this);
        // TODO Interface
        if (player.getAchievementRepository().getAchievementState(getTaskLocation()) == AchievementState.COMPLETED) {
            player.getAchievementRepository().getDiary(getTaskLocation()).finish();
        }
    }
}
