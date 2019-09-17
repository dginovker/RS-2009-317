package org.gielinor.game.node.entity.player.link;

import com.google.common.primitives.Ints;
import org.gielinor.database.DatabaseVariable;
import org.gielinor.game.content.skill.member.slayer.Tasks;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.rs2.config.DatabaseDetails;
import org.gielinor.utilities.buffer.ByteBufferUtils;
import plugin.activity.gungame.GunGameStage;

import java.nio.ByteBuffer;

/**
 * Represents a managing class of activity related information.
 *
 * @author 'Vexia
 */
public final class ActivityData implements SavingModule {

    /**
     * Represents fields to be inserted into the database.
     */
    public static final String[] FIELDS = new String[]{
        "pidn", "pest_points", "warrior_guild_tokens", "bounty_hunter_rate",
        "bounty_rogue_rate", "barrows_kill_count", "random_barrows_brother",
        "barrows_chest_status", "barrows_chest_count", "barbarian_assault_points",
        "barrow_tunnel_index", "elnock_supplies", "last_bork_battle",
        "easy_clue_scrolls", "medium_clue_scrolls", "hard_clue_scrolls",
        "slayer_points", "accumulative_slayer_tasks", "completed_fight_caves",
        "last_reset_task", "last_reset_block", "last_gun_game_entry"
    };
    /**
     * Represents the pest points gained from pest control.
     */
    private int pestPoints = 0;
    /**
     * The amount of warrior's guild tokens.
     */
    private int warriorGuildTokens = 0;
    /**
     * The bounty hunter rating.
     */
    private int bountyHunterRate = 0;
    /**
     * The bounty rogue rating.
     */
    private int bountyRogueRate = 0;
    /**
     * The barrows kill count.
     */
    private int barrowsKillCount = 0;
    /**
     * The player's killed Barrows brothers.
     */
    private boolean[] barrowsKilled = new boolean[6];
    /**
     * The player's random Barrows brother ordinal.
     */
    private int randomBarrowsBrother = 0;
    /**
     * The barrows chest status (1 = opened (spawned brother), 2 = killed brother, 3 = looted).
     */
    private int barrowsChestStatus = 0;
    /**
     * The amount of Barrows chests looted.
     */
    private int barrowsChestCount;
    /**
     * The amount of Barbarian Assault points.
     */
    private int barbarianAssaultPoints;
    /**
     * The barrow tunnel crypt index.
     */
    private int barrowTunnelIndex;
    /**
     * If received the elnock supplies.
     */
    private boolean elnockSupplies;
    /**
     * The time stamp of the last battle with Bork.
     */
    private long lastBorkBattle;
    /**
     * The amount of easy clue scrolls completed.
     */
    private int easyClueScrolls = 0;
    /**
     * The amount of medium clue scrolls completed.
     */
    private int mediumClueScrolls = 0;
    /**
     * The amount of hard clue scrolls completed.
     */
    private int hardClueScrolls = 0;
    /**
     * The amount of slayer points a player has.
     */
    private int slayerPoints;
    /**
     * Represents the three different slayer learn abilities.
     */
    private boolean[] learnedSlayerOptions = new boolean[3];
    /**
     * The amount of slayer tasks a player has done in a row.
     */
    private int accumulativeSlayerTasks;
    /**
     * Represents the disabled slayer tasks.
     */
    private String[] disabledSlayerTasks = new String[]{ "nothing", "nothing", "nothing", "nothing" };
    /**
     * The player's skill level 99 milestones.
     */
    private int milestones = 0;
    /**
     * The player's gun game stage.
     */
    private GunGameStage gunGameStage;
    /**
     * If the player has previously completed the fight caves.
     */
    private boolean completedFightCaves;
    /**
     * Last reset for the task for diamond+ members
     */
    private long lastTaskReset;
    /**
     * Last block for a task for diamond+ members
     */
    private long lastTaskBlock;
    /**
     * Last time the gun game entry has been reduced 1/4th.
     */
    private long lastGunGameEntry;
    /**
     * The player's skill experience 200m milestones.
     */
    private int experienceMilestones = 0;
    private int pidn;

    public int getMilestones() {
        return milestones;
    }

    public void addMilestone(int index) {
        milestones = index;
    }

    private DatabaseVariable<Integer> fogRating = new DatabaseVariable<>(DatabaseDetails.DatabaseType.GAME, -1, "player_activity_data", "fog_rating", "pidn", pidn);

    /**
     * Constructs a new {@code ActivityInfo} {@code Object}.
     * @param pidn
     */
    public ActivityData(int pidn) {
        this.pidn = pidn;
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        if (pestPoints > 0) {
            SavedData.save(byteBuffer, pestPoints, 1);
        }
        if (warriorGuildTokens > 0) {
            SavedData.save(byteBuffer, warriorGuildTokens, 2);
        }
        if (bountyHunterRate > 0) {
            SavedData.save(byteBuffer, bountyHunterRate, 3);
        }
        if (bountyRogueRate > 0) {
            SavedData.save(byteBuffer, bountyRogueRate, 4);
        }
        if (barrowsKillCount > 0) {
            SavedData.save(byteBuffer, barrowsKillCount, 5);
        }
        SavedData.save(byteBuffer, barrowsKilled, 6);
        SavedData.save(byteBuffer, randomBarrowsBrother, 7);
        SavedData.save(byteBuffer, barrowsChestStatus, 8);
        SavedData.save(byteBuffer, barrowsChestCount, 9);
        SavedData.save(byteBuffer, barrowTunnelIndex, 10);
        if (barbarianAssaultPoints > 0) {
            SavedData.save(byteBuffer, barbarianAssaultPoints, 11);
        }
        SavedData.save(byteBuffer, elnockSupplies, 12);
        SavedData.save(byteBuffer, lastBorkBattle, 13);
        if (easyClueScrolls > 0) {
            SavedData.save(byteBuffer, easyClueScrolls, 14);
        }
        if (mediumClueScrolls > 0) {
            SavedData.save(byteBuffer, mediumClueScrolls, 15);
        }
        if (hardClueScrolls > 0) {
            SavedData.save(byteBuffer, hardClueScrolls, 16);
        }
        SavedData.save(byteBuffer, slayerPoints, 17);
        SavedData.save(byteBuffer, learnedSlayerOptions, 18);
        SavedData.save(byteBuffer, accumulativeSlayerTasks, 19);

        byteBuffer.put((byte) 20);
        for (String disabledSlayerTask : disabledSlayerTasks) {
            ByteBufferUtils.putRS2String(disabledSlayerTask, byteBuffer);
        }
        SavedData.save(byteBuffer, completedFightCaves ? 1 : 0, 21);
        SavedData.save(byteBuffer, lastTaskReset, 22);
        SavedData.save(byteBuffer, lastTaskBlock, 23);
        SavedData.save(byteBuffer, lastGunGameEntry, 24);
        byteBuffer.put((byte) 0);

    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        int opcode;
        while ((opcode = byteBuffer.get()) != 0) {
            switch (opcode) {
                case 1:
                    pestPoints = byteBuffer.getInt();
                    break;
                case 2:
                    warriorGuildTokens = byteBuffer.getInt();
                    break;
                case 3:
                    bountyHunterRate = byteBuffer.getInt();
                    break;
                case 4:
                    bountyRogueRate = byteBuffer.getInt();
                    break;
                case 5:
                    barrowsKillCount = byteBuffer.getInt();
                    break;
                case 6:
                    for (byte index = 0; index < barrowsKilled.length; index++) {
                        barrowsKilled[index] = (byteBuffer.get() == 1);
                    }
                    break;
                case 7:
                    randomBarrowsBrother = byteBuffer.getInt();
                    break;
                case 8:
                    barrowsChestStatus = byteBuffer.getInt();
                    break;
                case 9:
                    barrowsChestCount = byteBuffer.getInt();
                    break;
                case 10:
                    barrowTunnelIndex = byteBuffer.getInt();
                    break;
                case 11:
                    barbarianAssaultPoints = byteBuffer.getInt();
                    break;
                case 12:
                    elnockSupplies = byteBuffer.get() == 1;
                    break;
                case 13:
                    lastBorkBattle = byteBuffer.getLong();
                    break;
                case 14:
                    easyClueScrolls = byteBuffer.getInt();
                    break;
                case 15:
                    mediumClueScrolls = byteBuffer.getInt();
                    break;
                case 16:
                    hardClueScrolls = byteBuffer.getInt();
                    break;
                case 17:
                    slayerPoints = byteBuffer.getInt();
                    break;
                case 18:
                    for (byte index = 0; index < learnedSlayerOptions.length; index++) {
                        learnedSlayerOptions[index] = (byteBuffer.get() == 1);
                    }
                    break;
                case 19:
                    accumulativeSlayerTasks = byteBuffer.getInt();
                    break;
                case 20:
                    for (byte index = 0; index < disabledSlayerTasks.length; index++) {
                        disabledSlayerTasks[index] = ByteBufferUtils.getRS2String(byteBuffer);
                    }
                    break;
                case 21:
                    completedFightCaves = byteBuffer.getInt() == 1;
                    break;
                case 22:
                    lastTaskReset = byteBuffer.getLong();
                    break;
                case 23:
                    lastTaskBlock = byteBuffer.getLong();
                    break;
                case 24:
                    lastGunGameEntry = byteBuffer.getLong();
                    break;
            }
        }
    }

    /**
     * Increases the pest points.
     *
     * @param pestPoints the pest points to increase with.
     */
    public void increasePestPoints(int pestPoints) {
        if (pestPoints + this.pestPoints >= Integer.MAX_VALUE) {
            this.pestPoints = Integer.MAX_VALUE;
        } else {
            this.pestPoints += pestPoints;
        }
    }

    /**
     * Decreases the pest points.
     *
     * @param pestPoints the pest points to increase with.
     */
    public void decreasePestPoints(int pestPoints) {
        this.pestPoints -= pestPoints;
    }

    /**
     * Gets the pest points.
     *
     * @return the pest points.
     */
    public int getPestPoints() {
        return pestPoints;
    }

    /**
     * Sets the pest points.
     *
     * @param pestPoints the pest points.
     */
    public void setPestPoints(int pestPoints) {
        this.pestPoints = pestPoints;
    }

    /**
     * Gets the warriorGuildTokens.
     *
     * @return The warriorGuildTokens.
     */
    public int getWarriorGuildTokens() {
        return warriorGuildTokens;
    }

    /**
     * Sets the warriorGuildTokens.
     *
     * @param warriorGuildTokens The warriorGuildTokens to set.
     */
    public void setWarriorGuildTokens(int warriorGuildTokens) {
        this.warriorGuildTokens = warriorGuildTokens;
    }

    /**
     * Updates the warrior guild tokens.
     *
     * @param amount The amount to increase with.
     */
    public void updateWarriorTokens(int amount) {
        this.warriorGuildTokens += amount;
    }

    /**
     * Gets the bountyHunterRate.
     *
     * @return The bountyHunterRate.
     */
    public int getBountyHunterRate() {
        return bountyHunterRate;
    }

    /**
     * Sets the bountyHunterRate.
     *
     * @param rate The rate to set.
     */
    public void setBountyHunterRate(int rate) {
        this.bountyHunterRate = rate;
    }

    /**
     * Increments the bountyHunterRate.
     *
     * @param rate The rate to set.
     */
    public void updateBountyHunterRate(int rate) {
        this.bountyHunterRate += rate;
    }

    /**
     * Gets the bountyRogueRate.
     *
     * @return The bountyRogueRate.
     */
    public int getBountyRogueRate() {
        return bountyRogueRate;
    }

    /**
     * Sets the bountyRogueRate.
     *
     * @param rate The rate to set.
     */
    public void setBountyRogueRate(int rate) {
        this.bountyRogueRate = rate;
    }

    /**
     * Increments the bountyRogueRate.
     *
     * @param rate The rate to set.
     */
    public void updateBountyRogueRate(int rate) {
        this.bountyRogueRate += rate;
    }

    /**
     * Gets the player's barrows kill count.
     *
     * @return The kill count.
     */
    public int getBarrowsKillCount() {
        return barrowsKillCount;
    }

    /**
     * Sets the player's barrows kill count.
     *
     * @param barrowsKillCount The kill count to set.
     */
    public void setBarrowsKillCount(int barrowsKillCount) {
        this.barrowsKillCount = Ints.constrainToRange(barrowsKillCount, 0, 1000);
    }

    /**
     * Resets the player's barrows kill count.
     */
    public void resetBarrowsKillCount() {
        this.barrowsKillCount = 0;
    }

    /**
     * Gets the player's killed Barrows brothers.
     *
     * @return The Barrows brothers killed.
     */
    public boolean[] getBarrowsKilled() {
        return barrowsKilled;
    }

    /**
     * Checks if the player has killed any barrow brothers.
     *
     * @return {@code True} if so.
     */
    public boolean hasKilledBarrowBrother() {
        for (boolean barrowKilled : barrowsKilled) {
            if (barrowKilled) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a Barrows brother was killed.
     *
     * @param ordinal The ordinal of the brother.
     */
    public boolean isBarrowsBrotherKilled(int ordinal) {
        return this.barrowsKilled[ordinal];
    }


    /**
     * Sets a Barrows brother killed.
     *
     * @param ordinal The ordinal of the brother.
     */
    public void setBarrowsKilled(int ordinal) {
        this.barrowsKilled[ordinal] = true;
    }


    /**
     * Resets the Barrows brother killed.
     */
    public void resetBarrowsKilled() {
        this.barrowsKilled = new boolean[6];
    }

    /**
     * Gets the player's random Barrows brother ordinal.
     * <p>
     * The random Barrows brother ordinal.
     */
    public int getRandomBarrowsBrother() {
        return randomBarrowsBrother;
    }

    /**
     * Sets the player's random Barrows brother.
     *
     * @param randomBarrowsBrother The Random Barrows brother.
     */
    public void setRandomBarrowsBrother(int randomBarrowsBrother) {
        this.randomBarrowsBrother = randomBarrowsBrother;
    }

    /**
     * Gets the barrows chest status (0 = nothing, 1 = opened, 2 = looted).
     *
     * @return The chest status.
     */
    public int getBarrowsChestStatus() {
        return barrowsChestStatus;
    }

    /**
     * Sets the barrows chest status (0 = nothing, 1 = opened, 2 = looted).
     *
     * @param barrowsChestStatus The chest status to set.
     */
    public void setBarrowsChestStatus(int barrowsChestStatus) {
        this.barrowsChestStatus = barrowsChestStatus;
    }

    /**
     * Gets the count of Barrows chest loots.
     *
     * @return The count.
     */
    public int getBarrowsChestCount() {
        return barrowsChestCount;
    }

    /**
     * Sets the count of Barrows chest loots.
     *
     * @param barrowsChestCount The count.
     */
    public void setBarrowsChestCount(int barrowsChestCount) {
        this.barrowsChestCount = barrowsChestCount;
    }


    /**
     * Increases the count of Barrows chest loots.
     */
    public void increaseBarrowsChestCount() {
        this.barrowsChestCount += 1;
    }

    public int getBarbarianAssaultPoints() {
        return barbarianAssaultPoints;
    }

    public void setBarbarianAssaultPoints(int barbarianAssaultPoints) {
        this.barbarianAssaultPoints = barbarianAssaultPoints;
    }

    public void increaseBarbarianAssaultPoints(int barbarianAssaultPoints) {
        this.barbarianAssaultPoints += barbarianAssaultPoints;
    }

    public void decreaseBarbarianAssaultPoints(int barbarianAssaultPoints) {
        this.barbarianAssaultPoints -= barbarianAssaultPoints;
    }


    /**
     * Gets the barrowKills.
     *
     * @return The barrowKills.
     */
    public int getBarrowKills() {
        return barrowsKillCount;
    }

    public void addBarrowsKillcount(int combatLevel) {
        if (barrowsKillCount == 1000) return;
        barrowsKillCount = Ints.constrainToRange(barrowsKillCount + combatLevel, 0, 1000);
    }

    /**
     * Gets the barrowTunnelIndex.
     *
     * @return The barrowTunnelIndex.
     */
    public int getBarrowTunnelIndex() {
        return barrowTunnelIndex;
    }

    /**
     * Sets the barrowTunnelIndex.
     *
     * @param barrowTunnelIndex The barrowTunnelIndex to set.
     */
    public void setBarrowTunnelIndex(int barrowTunnelIndex) {
        this.barrowTunnelIndex = barrowTunnelIndex;
    }


    /**
     * Gets the elnockSupplies.
     *
     * @return The elnockSupplies.
     */
    public boolean isElnockSupplies() {
        return elnockSupplies;
    }

    /**
     * Sets the elnockSupplies.
     *
     * @param elnockSupplies The elnockSupplies to set.
     */
    public void setElnockSupplies(boolean elnockSupplies) {
        this.elnockSupplies = elnockSupplies;
    }

    /**
     * Gets the lastBorkBattle.
     *
     * @return the lastBorkBattle
     */
    public long getLastBorkBattle() {
        return lastBorkBattle;
    }

    /**
     * Sets the balastBorkBattle.
     *
     * @param lastBorkBattle the lastBorkBattle to set.
     */
    public void setLastBorkBattle(long lastBorkBattle) {
        this.lastBorkBattle = lastBorkBattle;
    }

    /**
     * Checks if the player has killed bork.
     *
     * @return {@code True if so.}
     */
    public boolean hasKilledBork() {
        return lastBorkBattle > 0;
    }


    /**
     * The amount of easy clue scrolls completed.
     */
    public int getEasyClueScrolls() {
        return easyClueScrolls;
    }

    /**
     * Sets the amount of easy clue scrolls completed.
     *
     * @param easyClueScrolls The amount of easy clue scrolls completed.
     */
    public void setEasyClueScrolls(int easyClueScrolls) {
        this.easyClueScrolls = easyClueScrolls;
    }

    /**
     * Increases the amount of easy clue scrolls completed.
     */
    public int increaseEasyClueScrolls() {
        easyClueScrolls += 1;
        return easyClueScrolls;
    }

    /**
     * The amount of medium clue scrolls completed.
     */
    public int getMediumClueScrolls() {
        return mediumClueScrolls;
    }

    /**
     * Sets the amount of medium clue scrolls completed.
     *
     * @param mediumClueScrolls The amount of medium clue scrolls completed.
     */
    public void setMediumClueScrolls(int mediumClueScrolls) {
        this.mediumClueScrolls = mediumClueScrolls;
    }

    /**
     * Increases the amount of medium clue scrolls completed.
     */
    public int increaseMediumClueScrolls() {
        this.mediumClueScrolls += 1;
        return mediumClueScrolls;
    }


    /**
     * The amount of hard clue scrolls completed.
     */
    public int getHardClueScrolls() {
        return hardClueScrolls;
    }

    /**
     * Sets the amount of hard clue scrolls completed.
     *
     * @param hardClueScrolls The amount of hard clue scrolls completed.
     */
    public void setHardClueScrolls(int hardClueScrolls) {
        this.hardClueScrolls = hardClueScrolls;
    }

    /**
     * Increases the amount of hard clue scrolls completed.
     */
    public int increaseHardClueScrolls() {
        this.hardClueScrolls += 1;
        return hardClueScrolls;
    }

    /**
     * Gets the Slayer points.
     *
     * @return The slayer points.
     */
    public int getSlayerPoints() {
        return slayerPoints;
    }

    /**
     * Sets the players slayer points.
     *
     * @param amount The slayer points to set.
     */
    public void setSlayerPoints(int amount) {
        this.slayerPoints = amount;
    }

    /**
     * Decreases the amount of slayer points.
     *
     * @param amount The slayer points to decrease.
     */
    public void decreaseSlayerPoints(int amount) {
        slayerPoints -= amount;
    }

    /**
     * Increases the amount of slayer points.
     *
     * @param amount The slayer points to increase.
     */
    public void increaseSlayerPoints(int amount) {
        slayerPoints += amount;
    }

    /**
     * Gets the learned options for slayer.
     *
     * @return
     */
    public void setLearnedSlayerOption(int ordinal) {
        learnedSlayerOptions[ordinal] = true;
    }

    /**
     * Gets the learned option values.
     */
    public boolean[] getLearnedSlayerOptions() {
        return learnedSlayerOptions;
    }

    /**
     * Gets the learned option values.
     *
     * @param ordinal The learned option ordinal.
     * @return {@code True} if so.
     */
    public boolean hasLearnedSlayerOption(int ordinal) {
        return learnedSlayerOptions[ordinal];
    }

    /**
     * Checks if the player has learned any slayer options.
     *
     * @return {@code True} if so.
     */
    public boolean hasLearnedSlayerOptions() {
        for (boolean learnedSlayerOption : learnedSlayerOptions) {
            if (learnedSlayerOption) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the accumulated slayer task amount.
     *
     * @return
     */
    public int getAccumulativeSlayerTasks() {
        return accumulativeSlayerTasks;
    }

    /**
     * Increases the accumulative slayer task amount.
     */
    public void increaseAccumulativeSlayerTasks() {
        this.accumulativeSlayerTasks++;
    }

    /**
     * Sets the accumulative slayer tasks.
     *
     * @param accumulativeSlayerTasks The accumulative slayer tasks.
     */
    public void setAccumulativeSlayerTasks(int accumulativeSlayerTasks) {
        this.accumulativeSlayerTasks = accumulativeSlayerTasks;
    }

    /**
     * Resets the accumulative slayer tasks to 0.
     */
    public void resetAccumulativeSlayerTasks() {
        this.accumulativeSlayerTasks = 0;
    }

    /**
     * Gets the disabled task list.
     *
     * @return The disabled tasks.
     */
    public String[] getDisabledTasks() {
        return disabledSlayerTasks;
    }

    /**
     * Disables a Slayer {@link org.gielinor.game.content.skill.member.slayer.Task}.
     *
     * @param tasks The Slayer {@link org.gielinor.game.content.skill.member.slayer.Tasks}.
     */
    public void disableSlayerTask(int index, Tasks tasks) {
        disabledSlayerTasks[index] = tasks == null ? "nothing" : tasks.name();
    }

    /**
     * Disables a Slayer {@link org.gielinor.game.content.skill.member.slayer.Task}.
     *
     * @param task The Slayer {@link org.gielinor.game.content.skill.member.slayer.Tasks}.
     */
    public void disableSlayerTask(int index, String task) {
        disabledSlayerTasks[index] = (task == null || task.isEmpty() || task.equalsIgnoreCase("nothing")) ? "nothing" : task;
    }

    /**
     * Removes a disabled Slayer {@link org.gielinor.game.content.skill.member.slayer.Task}.
     *
     * @param index The index of the task to remove.
     */
    public void enableSlayerTask(int index) {
        disabledSlayerTasks[index] = "nothing";
    }

    public GunGameStage getGunGameStage() {
        return gunGameStage;
    }

    public void setGunGameStage(GunGameStage gunGameStage) {
        this.gunGameStage = gunGameStage;
    }

    public boolean isCompletedFightCaves() {
        return completedFightCaves;
    }

    public void setCompletedFightCaves(boolean completedFightCaves) {
        this.completedFightCaves = completedFightCaves;
    }

    public long getLastTaskReset() {
        return lastTaskReset;
    }

    public void setLastTaskReset(long lastTaskReset) {
        this.lastTaskReset = lastTaskReset;
    }

    public long getLastTaskBlock() {
        return lastTaskBlock;
    }

    public void setLastTaskBlock(long lastTaskBlock) {
        this.lastTaskBlock = lastTaskBlock;
    }

    public long getLastGunGameEntry() {
        return lastGunGameEntry;
    }

    public void setLastGunGameEntry(long lastGunGameEntry) {
        this.lastGunGameEntry = lastGunGameEntry;
    }

    public int getFogRating() {
        return fogRating.getValue();
    }
}
