package org.gielinor.game.node.entity.player.link;

import org.gielinor.parser.player.SavingModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Represents a managing class of saved data related to ingame interactions, such as questing data, npc talking data, etc.
 *
 * @author 'Vexia
 */
public class SavedData implements SavingModule {

    private static final Logger log = LoggerFactory.getLogger(SavedData.class);

    /**
     * The player's pidn.
     */
    private final int pidn;
    /**
     * Represents the activity data to save.
     */
    private final ActivityData activityData;
    /**
     * Represents the quest data to save.
     */
    private final QuestData questData = new QuestData();
    /**
     * Represents the global data to save.
     */
    private final GlobalData globalData;
    /**
     * Represents the player's {@link org.gielinor.game.node.entity.player.link.BossKillLog}.
     */
    private final BossKillLog bossKillLog = new BossKillLog();

    /**
     * Represents the player's {@link org.gielinor.game.node.entity.player.link.SlayerKillLog}.
     */
    private final SlayerKillLog slayerKillLog = new SlayerKillLog();

    /**
     * @param pidn
     */
    public SavedData(int pidn) {
        this.pidn = pidn;
        this.globalData = new GlobalData(pidn);
        this.activityData = new ActivityData(pidn);
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        byteBuffer.put((byte) 1);
        activityData.save(byteBuffer);
        byteBuffer.put((byte) 2);
        questData.save(byteBuffer);
        byteBuffer.put((byte) 3);
        globalData.save(byteBuffer);
        byteBuffer.put((byte) 4);
        bossKillLog.save(byteBuffer);
        byteBuffer.put((byte) 0);
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        int opcode;
        while ((opcode = byteBuffer.get()) != 0) {
            switch (opcode) {
                case 1:
                    activityData.parse(byteBuffer);
                    break;
                case 2:
                    questData.parse(byteBuffer);
                    break;
                case 3:
                    globalData.parse(byteBuffer);
                    break;
                case 4:
                    bossKillLog.parse(byteBuffer);
                    break;
                default:
                    log.warn("Invalid saved data opcode: {}.", opcode);
                    break;
            }
        }
    }

    /**
     * Method used to save an activity var that isn't valued at default.
     *
     * @param buffer the buffer.
     * @param var    the variable to save.
     */
    public static void save(final ByteBuffer buffer, final Object var, final int index) {
        if (var instanceof Integer ? (int) var != 0 : var instanceof Double ? (double) var != 0.0 :
            var instanceof Byte ? (byte) var != 0 : var instanceof Short ? (short)
                var != 0 : var instanceof Long ? (long) var != 0L : var instanceof Boolean
                ? (boolean) var != false : var != null) {
            buffer.put((byte) index);
            if (var instanceof Integer) {
                buffer.putInt((int) var);
            } else if (var instanceof Byte) {
                buffer.put((byte) var);
            } else if (var instanceof Short) {
                buffer.putShort((short) var);
            } else if (var instanceof Long) {
                buffer.putLong((long) var);
            } else if (var instanceof Boolean) {
                buffer.put((byte) 1);
            } else if (var instanceof Double) {
                buffer.putDouble((double) var);
            } else if (var instanceof double[]) {
                double[] doubleArray = ((double[]) var);
                for (double aDoubleArray : doubleArray) {
                    buffer.putDouble(aDoubleArray);
                }
            } else if(var instanceof int[]){
                int[] intArray = ((int[]) var);
                for (int anIntArray : intArray) {
                    buffer.putInt(anIntArray);
                }
            } else if (var instanceof boolean[]) {
                boolean[] booleanArray = ((boolean[]) var);
                for (boolean aBooleanArray : booleanArray) {
                    buffer.put((byte) (aBooleanArray ? 1 : 0));
                }
            }
        }
    }

    /**
     * Gets the boolean value.
     *
     * @param value the value.
     * @return the value.
     */
    public static boolean getBoolean(byte value) {
        return value == 1;
    }

    /**
     * Gets the activityData.
     *
     * @return The activityData.
     */
    public ActivityData getActivityData() {
        return activityData;
    }

    /**
     * Gets the questData.
     *
     * @return The questData.
     */
    public QuestData getQuestData() {
        return questData;
    }

    /**
     * Gets the globalData.
     *
     * @return The globalData.
     */
    public GlobalData getGlobalData() {
        return globalData;
    }

    /**
     * Gets the player's {@link org.gielinor.game.node.entity.player.link.BossKillLog}.
     *
     * @return The boss kill log.
     */
    public BossKillLog getBossKillLog() {
        return bossKillLog;
    }

    /**
     * Gets the player's {@link org.gielinor.game.node.entity.player.link.SlayerKillLog}.
     *
     * @return The Slayer kill log.
     */
    public SlayerKillLog getSlayerKillLog() {
        return slayerKillLog;
    }

}
