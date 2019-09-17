package org.gielinor.game.content.skill.member.slayer;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.utilities.misc.RandomUtil;


/**
 * Manages the players slayer task.
 *
 * @author Vexia
 */
public final class SlayerManager implements SavingModule {

    /**
     * Represents the player instance.
     */
    private final Player player;

    /**
     * Represents the current slayer master.
     */
    private Master master;

    /**
     * Represents the current task.
     */
    private Task task;

    /**
     * Represents the amount of creatures killed.
     */
    private int amount;

    /**
     * Constructs a new {@code SlayerCredentials.java} {@Code Object}.
     *
     * @param player the player.
     */
    public SlayerManager(Player player) {
        this.player = player;
    }

    @Override
    public void parse(ByteBuffer buffer) {
        int opcode;
        while ((opcode = buffer.get() & 0xFF) != 0) {
            switch (opcode) {
                case 1:
                    master = Master.forId(buffer.getInt());
                    break;
                case 2:
                    task = Tasks.values()[buffer.getInt()].getTask();
                    break;
                case 3:
                    setAmount(buffer.getInt());
                    break;
            }
        }
    }

    @Override
    public void save(ByteBuffer buffer) {
        if (master != null) {
            buffer.put((byte) 1);
            buffer.putInt(master.getNpc());
        }
        if (task != null) {
            buffer.put((byte) 2);
            buffer.putInt(Tasks.forValue(task).ordinal());
        }
        if (task != null) {
            buffer.put((byte) 3);
            buffer.putInt(getAmount());
        }
        buffer.put((byte) 0);
    }

    /**
     * Assigns a task to the manager.
     *
     * @param task   the task.
     * @param master the master.
     */
    public void assign(Task task, final Master master) {
        assign(task, master, getRandomAmount(task.getRanges(master)));
    }

    /**
     * Assigns a task to the manager.
     *
     * @param task   the task.
     * @param master the master.
     */
    public void assign(Task task, final Master master, int amount) {
        setMaster(master);
        setTask(task);
        setAmount(amount);
        if (master == Master.CHAELDAR) {
            AchievementDiary.finalize(player, AchievementTask.SLAYER_CHAELDAR);
        }
    }

    /**
     * Method used to assign a new task for a player.
     *
     * @param master the master to give the task.
     */
    public void generate(Master master) {
        final List<Task> tasks = Arrays.asList(Tasks.getTasks(master));
        Collections.shuffle(tasks, RandomUtil.RANDOM);
        for (Task task : tasks) {
            if (!task.canAssign(player, master)) {
                continue;
            }
            assign(task, master);
            break;
        }
    }

    /**
     * Clears the manager of a task.
     */
    public void clear() {
        setTask(null);
        setAmount(0);
    }

    /**
     * Gets a random amount.
     *
     * @param ranges the ranges.
     * @return the amt.
     */
    private int getRandomAmount(int[] ranges) {
        return RandomUtil.random(ranges[0], ranges[1]);
    }

    /**
     * Gets the task name.
     *
     * @return the name.
     */
    public String getTaskName() {
        if (task == null) {
            return "invalid";
        }
        if (task.getNpcs() == null) {
            return "invalid";
        }
        if (task.getNpcs().length < 1) {
            return "invalid";
        }
        return NPCDefinition.forId(task.getNpcs()[0]).getName().toLowerCase();
    }

    public Tasks getTasks() {
        if (task == null || task.getNpcs() == null || task.getNpcs().length < 1) {
            return null;
        }
        return Tasks.forValue(task);
    }

    public void informTaskProgress() {
        if (!hasTask()) {
            player.getActionSender().sendMessage("You need something new to hunt.");
            return;
        }
        player.getActionSender().sendMessage("You're assigned to kill " + NPCDefinition.forId((getTask().getNpcs()[0])).getName().toLowerCase() + "s; only " + getAmount() + " more to go.");
    }

    /**
     * Determines the slayer casket drop rate.
     *
     * @param player The player.
     * @return
     */
    public static int determineCasketDropRate(Player player) {
        if (player.getSlayer().getMaster().equals(Master.TURAEL) || player.getSlayer().getMaster().equals(Master.MAZCHNA)) {
            return 37;
        }
        if (player.getSlayer().getMaster().equals(Master.VANNAKA)) {
            return 33;
        }
        if (player.getSlayer().getMaster().equals(Master.CHAELDAR)) {
            return 30;
        }
//        if (player.getSlayer().getMaster().equals(Master.NIEVE) || player.getSlayer().getMaster().equals(Master.SUMONA)) {
//            return 25;
//        }
        if (player.getSlayer().getMaster().equals(Master.DURADEL)) {
            return 20;
        }
        return 40;
    }

    /**
     * Determines the amount of Slayer points to award the player for completing a task.
     *
     * @param player The player.
     * @return The points.
     */
    public static int determinePointAmount(Player player) {
        int points = 0;
        boolean tenthTask = player.getSavedData().getActivityData().getAccumulativeSlayerTasks() % 10 == 0;
        boolean fiftiethTask = player.getSavedData().getActivityData().getAccumulativeSlayerTasks() % 50 == 0;
        if (player.getSlayer().getMaster().equals(Master.TURAEL)) {
            points = 0;
        }
        if (player.getSlayer().getMaster().equals(Master.MAZCHNA)) {
            points = 2;
        }
        if (player.getSlayer().getMaster().equals(Master.VANNAKA)) {
            points = 4;
        }
        if (player.getSlayer().getMaster().equals(Master.CHAELDAR)) {
            points = 10;
        }
        if (player.getSlayer().getMaster().equals(Master.DURADEL)) {
            points = 15;
        }
        return fiftiethTask ? points * 15 : (tenthTask ? points * 5 : points);
    }

    /**
     * Gets the task.
     *
     * @return The task.
     */
    public Task getTask() {
        return task;
    }

    /**
     * Sets the task.
     *
     * @param task The task to set.
     */
    public void setTask(Task task) {
        this.task = task;
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
     * Gets the master.
     *
     * @return The master.
     */
    public Master getMaster() {
        return master;
    }

    /**
     * Sets the master.
     *
     * @param master The master to set.
     */
    public void setMaster(Master master) {
        this.master = master;
    }

    /**
     * Checks if a <b>Player</b> contains a task.
     *
     * @return {@code True} if so.
     */
    public boolean hasTask() {
        return task != null && getAmount() != 0;
    }

    /**
     * Method used to check if the task is completed.
     *
     * @return <code>True</code> if so.
     */
    public boolean isCompleted() {
        return amount <= 0;
    }

    /**
     * Gets the amount.
     *
     * @return The amount.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount.
     *
     * @param amount The amount to set.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Method used to decrement an amount.
     *
     * @param amount the amount.
     */
    public void decrementAmount(int amount) {
        this.amount -= amount;
    }

    /**
     * Method used to check if the player has started slayer.
     *
     * @return {@code True} if so.
     */
    public boolean hasStarted() {
        return master != null;
    }

}
