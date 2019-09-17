package org.gielinor.game.content.global.achievementold;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.global.achievementold.impl.GlobalDiary;
import org.gielinor.game.content.global.achievementold.impl.LumbridgeDraynorDiary;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InterfaceColourContext;
import org.gielinor.net.packet.out.InterfaceColour;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.utilities.buffer.ByteBufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A repository of achievement diaries.
 *
 * @author Torchic
 */
public final class AchievementRepository implements SavingModule {

    private static final Logger log = LoggerFactory.getLogger(AchievementRepository.class);

    /**
     * Represents the mapping of instanced achievement diaries..
     */
    private final Map<String, AchievementDiary> diaries = new HashMap<>();

    /**
     * Represents the {@link java.util.Map} of achievements with their current amount (if any).
     */
    private Map<AchievementTask, Integer> achievements = new HashMap<>();

    /**
     * Represents the {@link java.util.List} of finished achievements.
     */
    private List<AchievementTask> finishedAchievements = new ArrayList<>();

    /**
     * The id of the first button for tasks on the list (BUTTON_ID - 1).
     */
    private final int START_ID = 29307 - 1;

    /**
     * Represents the player instance.
     */
    private final Player player;

    /**
     * Constructs a new {@Code AchievementRepository} {@Code Object}
     *
     * @param player The player.
     */
    public AchievementRepository(final Player player) {
        this.player = player;
        diaries.put(GlobalDiary.NAME, new GlobalDiary(player));
        diaries.put(LumbridgeDraynorDiary.NAME, new LumbridgeDraynorDiary(player));
        // diaries.put(WildernessDiary.NAME, new WildernessDiary(player));
    }

    /**
     * Updates the {@link #diaries} values and the information and states.
     *
     * @param player the player.
     */
    public void update(Player player) {
        for (Entry<String, AchievementDiary> diary : diaries.entrySet()) {
            AchievementDiary achievementDiary = diary.getValue();
            AchievementState achievementState = getAchievementState(diary.getKey());
            int colour = achievementState == AchievementState.COMPLETED ? 0x3366 :
                achievementState == AchievementState.STARTED ? 0x33FF66 : 0x6000;
            PacketRepository.send(InterfaceColour.class, new InterfaceColourContext(player, (START_ID + achievementDiary.getIndex()), colour));
            player.getActionSender().sendString((START_ID + achievementDiary.getIndex()), diary.getKey());
        }
    }

    /**
     * Checks to see if a diary has been started.
     *
     * @param diary The diary to check.
     * @return True if the diary has been started.
     */
    public boolean isStarted(AchievementDiary diary) {
        return isStarted(diary.getName());
    }

    /**
     * Checks to see if a diary has been started.
     *
     * @param name The name of the diary to check.
     * @return True if the diary has been started.
     */
    public boolean isStarted(String name) {
        return (diaries.get(name) == null ? Boolean.FALSE : diaries.get(name).getState() == AchievementState.STARTED);
    }

    /**
     * Checks if the diary by the given name is completed.
     *
     * @param name the name of the diary.
     * @return if the diary is completed.
     */
    public boolean isComplete(String name) {
        return isComplete(diaries.get(name));
    }

    /**
     * Checks if the diary is complete.
     *
     * @param diary the diary.
     * @return the diary.
     */
    public boolean isComplete(AchievementDiary diary) {
        return diary != null && diary.getState() == AchievementState.COMPLETED;
    }

    /**
     * Returns the <p>Diary</p> by the given name.
     *
     * @param name the name of the diary.
     * @return the diary.
     */
    public AchievementDiary getDiary(String name) {
        return diaries.get(name);
    }

    /**
     * Gets the <b>diary</b> by the index.
     *
     * @param index the index of the diary.
     * @return the diary by the button index.
     */
    public AchievementDiary getDiaryIndex(int index) {
        for (Entry<String, AchievementDiary> diary : diaries.entrySet()) {
            if (diary.getValue().getIndex() == index) {
                return diary.getValue();
            }
        }
        return null;
    }

    /**
     * Gets the {@link org.gielinor.game.content.global.achievementold.AchievementDiary} by the id of the button.
     *
     * @param buttonId The id of the button.
     * @return The diary by the button.
     */
    public AchievementDiary forId(int buttonId) {
        for (Entry<String, AchievementDiary> diary : diaries.entrySet()) {
            if ((START_ID + diary.getValue().getIndex()) == buttonId) {
                return diary.getValue();
            }
        }
        return null;
    }

    /**
     * Gets the diaries.
     *
     * @return the diary.
     */
    public Map<String, AchievementDiary> getDiaries() {
        return diaries;
    }

    public Map<AchievementTask, Integer> getAchievements() {
        return achievements;
    }

    public List<AchievementTask> getFinishedAchievements() {
        return finishedAchievements;
    }

    public void addFinishedAchievement(AchievementTask achievementTask) {
        finishedAchievements.add(achievementTask);
        player.getAchievementRepository().getAchievements().put(achievementTask, 1);
    }

    public void removeFinishedAchievement(AchievementTask achievement) {
        finishedAchievements.remove(achievement);
    }

    /**
     * Checks if an achievement is null or started at the count given.
     *
     * @param count The count.
     * @return <code>True</code> if so.
     */
    public boolean isNullOrCount(AchievementTask achievementTask, int count) {
        return player.getAchievementRepository().getAchievements().get(achievementTask) == null || player.getAchievementRepository().getAchievements().get(achievementTask) == count;
    }

    /**
     * Checks if an achievement is not null or started at the count given.
     *
     * @param count The count.
     * @return <code>True</code> if so.
     */
    public boolean isCount(AchievementTask achievementTask, int count) {
        if (player.getAchievementRepository().getAchievements().get(achievementTask) == null) {
            return false;
        }
        return player.getAchievementRepository().getAchievements().get(achievementTask) == count;
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
     * Gets the true state of a diary.
     *
     * @param name The diary name.
     * @return The state.
     */
    public AchievementState getAchievementState(String name) {
        List<AchievementTask> achievementTaskList = AchievementTask.forTaskLocation(name);
        if (achievementTaskList == null) {
            log.warn("Missing a requested achievement task: [{}].", name);
            return AchievementState.NOT_STARTED;
        }
        boolean started = false;
        boolean completed = true;
        for (AchievementTask anAchievementTaskList : achievementTaskList) {
            if (!player.getAchievementRepository().getFinishedAchievements().contains(anAchievementTaskList)) {
                completed = false;
                break;
            }
        }
        for (AchievementTask achievementTask : achievementTaskList) {
            if (player.getAchievementRepository().getAchievements().containsKey(achievementTask)) {
                started = true;
                break;
            }
        }
        return completed ? AchievementState.COMPLETED : started ? AchievementState.STARTED : AchievementState.NOT_STARTED;
    }

    /**
     * Checks if all difficulties are completed for a given {@link org.gielinor.game.content.global.achievementold.Difficulty}.
     *
     * @param achievementDiary The {@link org.gielinor.game.content.global.achievementold.AchievementDiary}.
     * @param difficulty       The {@link org.gielinor.game.content.global.achievementold.Difficulty}.
     * @return <code>True</code> if so.
     */
    public boolean isDifficultyComplete(AchievementDiary achievementDiary, Difficulty difficulty) {
        for (AchievementTask achievementTask : AchievementTask.values()) {
            if (!achievementDiary.getName().equals(achievementTask.getTaskLocation())) {
                continue;
            }
            if (achievementTask.getDifficulty() != difficulty) {
                continue;
            }
            if (!player.getAchievementRepository().getFinishedAchievements().contains(achievementTask)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if all achievement diaries are completed.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasCompletedAll() {
        for (AchievementDiary achievementDiary : diaries.values()) {
            if (achievementDiary.getState() != AchievementState.COMPLETED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        byteBuffer.put((byte) 1);
        byteBuffer.putInt(player.getAchievementRepository().getDiaries().entrySet().size());
        for (Entry<String, AchievementDiary> diary : player.getAchievementRepository().getDiaries().entrySet()) {
            ByteBufferUtils.putRS2String(diary.getKey(), byteBuffer);
            byteBuffer.putInt(diary.getValue().getState().ordinal());
        }
        byteBuffer.put((byte) 2);
        byteBuffer.putInt(player.getAchievementRepository().getAchievements().entrySet().size());
        for (Entry<AchievementTask, Integer> task : player.getAchievementRepository().getAchievements().entrySet()) {
            ByteBufferUtils.putRS2String(task.getKey().name(), byteBuffer);
            byteBuffer.putInt(task.getValue());
            byteBuffer.put((byte) (player.getAchievementRepository().getFinishedAchievements().contains(task.getKey()) ? 1 : 0));
        }
        byteBuffer.put((byte) 0);
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        int opcode;
        int length;
        while ((opcode = byteBuffer.get() & 0xFF) != 0) {
            switch (opcode) {
                case 1:
                    length = byteBuffer.getInt();
                    while (length > 0) {
                        String name = ByteBufferUtils.getRS2String(byteBuffer);
                        int state = byteBuffer.getInt();
                        AchievementDiary achievementDiary = player.getAchievementRepository().getDiary(name);
                        if (achievementDiary == null) {
                            log.warn("Invalid achievement diary loaded: [{}].", name);
                        } else {
                            player.getAchievementRepository().getDiaries().remove(name);
                            achievementDiary.setState(AchievementState.values()[state]);
                            player.getAchievementRepository().getDiaries().put(name, achievementDiary);
                        }
                        length--;
                    }
                    break;
                case 2:
                    length = byteBuffer.getInt();
                    while (length > 0) {
                        String task = ByteBufferUtils.getRS2String(byteBuffer);
                        int amount = byteBuffer.getInt();
                        boolean taskComplete = byteBuffer.get() == 1;
                        AchievementTask achievementTask = AchievementTask.forName(task);
                        if (achievementTask == null) {
                            log.warn("Invalid achievement task loaded: [{}].", task);
                        } else {
                            if (taskComplete) {
                                player.getAchievementRepository().getFinishedAchievements().add(achievementTask);
                            }
                            player.getAchievementRepository().getAchievements().put(achievementTask, amount);
                        }
                        length--;
                    }
                    break;
            }
        }
    }

}
