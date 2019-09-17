package org.gielinor.game.system.monitor;

import java.nio.ByteBuffer;
import java.util.List;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.utilities.buffer.ByteBufferUtils;

/**
 * Handles the player monitoring.
 *
 * @author Emperor
 */
public final class PlayerMonitor implements SavingModule {

    /**
     * The public chat log.
     */
    public static final int PUBLIC_CHAT_LOG = 0;

    /**
     * The private chat log.
     */
    public static final int PRIVATE_CHAT_LOG = 1;

    /**
     * The clan chat log.
     */
    public static final int CLAN_CHAT_LOG = 2;

    /**
     * The IP/MAC-address log.
     */
    public static final int ADDRESS_LOG = 3;

    /**
     * The IP/MAC-address log.
     */
    public static final int COMMAND_LOG = 4;

    /**
     * The IP/MAC-address log.
     */
    public static final int ADDRESS_HISTORY_LOG = 5;

    /**
     * The macro flag for a click when the client was out of focus.
     */
    public static final int MF_NO_FOCUS_CLICK = 0x1;

    /**
     * The networth of the player.
     */
    private long networth;

    /**
     * If the client is currently focused.
     */
    private boolean clientFocus = true;

    /**
     * How likely it is this player has used a macro.
     */
    private int macroFlag;

    /**
     * The duplication log.
     */
    private DuplicationLog duplicationLog;

    /**
     * The message monitors.
     */
    private MessageLog[] logs = new MessageLog[6];

    /**
     * Constructs a new {@code PlayerMonitor} {@code Object}.
     */
    public PlayerMonitor() {
        logs[PUBLIC_CHAT_LOG] = new MessageLog(500);
        logs[PRIVATE_CHAT_LOG] = new MessageLog(500);
        logs[CLAN_CHAT_LOG] = new MessageLog(500);
        logs[ADDRESS_LOG] = new MessageLog(200);
        logs[COMMAND_LOG] = new MessageLog(200);
        logs[ADDRESS_HISTORY_LOG] = new MessageLog(50, true);
    }

    /**
     * Writes the logs.
     *
     * @param playerName The player's name.
     */
    public void writeLogs(String playerName) {
        if (true) {
            return;//
        }
        logs[PUBLIC_CHAT_LOG].write("data/logs/communication/public/" + playerName + ".log");
        logs[PRIVATE_CHAT_LOG].write("data/logs/communication/private/" + playerName + ".log");
        logs[CLAN_CHAT_LOG].write("data/logs/communication/clan/" + playerName + ".log");
        logs[ADDRESS_LOG].write("data/logs/connection/" + playerName + ".log");
        logs[COMMAND_LOG].write("data/logs/command/" + playerName + ".log");
        if (duplicationLog != null && duplicationLog.getFlag() != 0) {
            duplicationLog.write(playerName);
        }
    }

    /**
     * If the player's actions should be logged.
     *
     * @return <code>True</code> if so.
     */
    public boolean isLogActions() {
        return duplicationLog != null && duplicationLog.isLoggingFlagged();
    }

    /**
     * Logs the given string.
     *
     * @param string The string to log.
     * @param type   The log type.
     */
    public void log(String string, int type) {
        log(string, type, true);
    }

    /**
     * Logs the given string.
     *
     * @param string    The string to log.
     * @param type      The log type.
     * @param timeStamp If we should time stamp the logged string.
     */
    public void log(String string, int type, boolean timeStamp) {
        if (type < 3) {
            String check = string.toLowerCase();
            if (check.contains("dupe") || check.contains("duping")) {
                getDuplicationLog().flag(DuplicationLog.DUPE_TALK);
                String prefix = null;
                if (type == PUBLIC_CHAT_LOG) {
                    prefix = "Public";
                } else if (type == PRIVATE_CHAT_LOG) {
                    prefix = "Private";
                } else {
                    prefix = "Clan";
                }
                getDuplicationLog().log(prefix + " chat message: " + string, true);
            }
        }
        logs[type].log(string, timeStamp);
    }

    /**
     * Checks the networth difference for a player (called on logout).
     *
     * @param player     The player.
     * @param currentNet The current networth.
     */
    public void checkNetworth(Player player, long currentNet) {
        long difference = currentNet - this.networth;
        if (difference < 1) { //The player lost money.
            return;
        }
        if (difference > 150_000_000l) {
            getDuplicationLog().flag(DuplicationLog.NW_INCREASE);
            getDuplicationLog().log("Large networth increase - [incr=" + difference + ", old=" + this.networth + ", cur=" + currentNet + "].", true);
        }
    }

    /**
     * Called when the player does a mouse click.
     *
     * @param x          The x-coordinate of the cursor.
     * @param y          The y-coordinate of the cursor.
     * @param delay      The time between this click and last.
     * @param rightClick If the right mouse-button was used to click.
     */
    public void handleMouseClick(int x, int y, int delay, boolean rightClick) {
        if (!clientFocus) {
            macroFlag |= MF_NO_FOCUS_CLICK;
        }
    }

    /**
     * Adds a macro flag.
     *
     * @param flag The flag.
     */
    public void addMacroFlag(int flag) {
        macroFlag |= flag;
    }

    /**
     * Gets the macroFlag.
     *
     * @return The macroFlag.
     */
    public int getMacroFlag() {
        return macroFlag;
    }

    /**
     * Sets the macroFlag.
     *
     * @param macroFlag The macroFlag to set.
     */
    public void setMacroFlag(int macroFlag) {
        this.macroFlag = macroFlag;
    }

    /**
     * Gets the networth.
     *
     * @return The networth.
     */
    public long getNetworth() {
        return networth;
    }

    /**
     * Sets the networth.
     *
     * @param networth The networth to set.
     */
    public void setNetworth(long networth) {
        this.networth = networth;
    }

    /**
     * Increases the networth.
     *
     * @param networth The networth to increase by.
     */
    public void increaseNetworth(long networth) {
        this.networth += networth;
    }

    /**
     * Gets the clientFocus.
     *
     * @return The clientFocus.
     */
    public boolean isClientFocus() {
        return clientFocus;
    }

    /**
     * Sets the clientFocus.
     *
     * @param clientFocus The clientFocus to set.
     */
    public void setClientFocus(boolean clientFocus) {
        this.clientFocus = clientFocus;
    }

    /**
     * Gets the logs.
     *
     * @return The logs.
     */
    public MessageLog[] getLogs() {
        return logs;
    }

    /**
     * Gets the duplicationLog.
     *
     * @return The duplicationLog.
     */
    public DuplicationLog getDuplicationLog() {
        if (duplicationLog == null) {
            duplicationLog = new DuplicationLog();
        }
        return duplicationLog;
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        if (duplicationLog != null && duplicationLog.getFlag() != 0) {
            byteBuffer.put((byte) 1).put((byte) duplicationLog.getFlag());
        }
        if (macroFlag != 0) {
            byteBuffer.put((byte) 2).put((byte) macroFlag);
        }
        List<String> log = logs[ADDRESS_HISTORY_LOG].getMessages();
        if (!log.isEmpty()) {
            byteBuffer.put((byte) 3);
            byteBuffer.put((byte) log.size());
            for (String s : log) {
                ByteBufferUtils.putRS2String(s, byteBuffer);
            }
        }
        if (duplicationLog != null && duplicationLog.isLoggingFlagged()) {
            byteBuffer.put((byte) 4).putLong(duplicationLog.getLastIncreaseFlag());
        }
        byteBuffer.put((byte) 0);
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        int opcode;
        while ((opcode = byteBuffer.get() & 0xFF) != 0) {
            switch (opcode) {
                case 1:
                    getDuplicationLog().flag(byteBuffer.get() & 0xFF);
                    break;
                case 2:
                    macroFlag = byteBuffer.get() & 0xFF;
                    break;
                case 3:
                    List<String> log = logs[ADDRESS_HISTORY_LOG].getMessages();
                    int size = byteBuffer.get() & 0xFF;
                    for (int i = 0; i < size; i++) {
                        log.add(ByteBufferUtils.getRS2String(byteBuffer));
                    }
                    break;
                case 4:
                    getDuplicationLog().setLastIncreaseFlag(byteBuffer.getLong());
                    break;
            }
        }
    }
}