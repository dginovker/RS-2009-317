package com.runescape.util;

import main.java.com.runescape.Game;
import com.runescape.media.Raster;
import com.runescape.media.Scrollbar;
import com.runescape.net.protocol.ProtocolConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A developer console.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 * @author <a href="http://www.rune-server.org/members/oncue/">OnCue</a>
 */
public class Console extends Game {
    /**
     * The current messages.
     */
    private final String[] messages;
    /**
     * The current message ID.
     */
    private int messageId;
    /**
     * The current console input.
     */
    private String input;
    /**
     * Whether or not the {@code Console} is open.
     */
    private boolean open;
    /**
     * Whether or not the {@code Console} has to scroll.
     */
    private boolean usingScroll;
    /**
     * The current index of the scrollbar.
     */
    private int scrollIndex;
    /**
     * The current position of the scrollbar.
     */
    private int scrollPosition;
    /**
     * The current offset of the scrollbar.
     */
    private int scrollOffset;

    /**
     * Constructs the {@link Console}.
     */
    public Console() {
        setMessageId(1);
        setInput("");
        messages = new String[500];
        setOpen(false);
        setUsingScroll(false);
    }

    /**
     * Prints a message in the console.
     *
     * @param message The message to print.
     * @param id      The id of the message.
     */
    public void printMessage(String message, int id) {
        if (getClientInstance().backDialogueId == -1) {
            Game.inputTaken = true;
        }
        System.arraycopy(getMessages(), 0, getMessages(), 1, 499);
        setMessageId(getMessageId() + 1);
        getMessages()[0] = getTimeStamp() + ": --> " + (id == 0 ? " " : "") + message;
    }

    /**
     * Prints a message in the console.
     *
     * @param message The message to print.
     */
    public void printMessage(String message) {
        printMessage(message, 0);
    }

    /**
     * Process console input.
     *
     * @param keyCode The keycode constant.
     */
    public void processConsoleInput(int keyCode) {
        // Page Up - scroll up in recently used commands
        if (keyCode == 1002) {
            if (latestCommandCaret <= latestCommandCount)
                latestCommandCaret++;
            if (latestCommandCaret > latestCommandCount) {
                setInput("");
                latestCommandCaret = 0;
            } else {
                setInput(latestCommandArray[latestCommandCaret - 1]);
            }
            inputTaken = true;
        }

        // Page Down - scroll down in recently used commands
        if (keyCode == 1003) {
            if (latestCommandCaret == 0) latestCommandCaret = latestCommandCount;
            else                         latestCommandCaret--;
            if (latestCommandCaret == 0) setInput("");
            else                         setInput(latestCommandArray[latestCommandCaret - 1]);
            inputTaken = true;
        }
        
        if (keyCode == 8 && getInput().length() > 0 && getInput().length() < 255) {
            setInput(getInput().substring(0, getInput().length() - 1));
        }

        if (Game.INSTANCE.isValidKeyCode(keyCode) && getInput().length() < 255) {
            setInput(getInput() + (char) keyCode);
        }

        if ((keyCode == 13 || keyCode == 10) && getInput().length() > 0 && getInput().length() < 255) {
            printMessage(getInput(), 0);
            String[] commands = getInput().split(" && ");
            if (commands.length != 0) {
                for (String command : commands) {
                    if (command == null || command.isEmpty()) {
                        continue;
                    }
                    processCommand(command);
                }
            }
            cacheUsedCommand(getInput());
            setInput("");
            Game.inputTaken = true;
        }
    }
    
    private void cacheUsedCommand(String command) {
        // Just repeated the most recently used command
        if (command.equals(latestCommandArray[0])) {
            latestCommandCaret = 0;
            return;
        }

        for (int i = latestCommandArray.length - 1; i > 0; i--) {
            latestCommandArray[i] = latestCommandArray[i - 1];
        }

        latestCommandArray[0] = command;
        if (latestCommandCount < latestCommandArray.length)
            latestCommandCount++;
        latestCommandCaret = 0;
    }

    /**
     * Processes an input command.
     *
     * @param command The command.
     */
    public void processCommand(String command) {
        switch (command.toLowerCase()) {
            case "cls":
                for (int line = 0; line < 500; line++) {
                    getMessages()[line] = null;
                }
                return;
            case "disconnect":
                Game.INSTANCE.sendLogout();
                return;
            case "exit":
                Game.INSTANCE.shutdown(true);
                return;
        }
        getClientInstance().processInput();
        getClientInstance().processCommand(command);
    }

    /**
     * Draws the {@code Console} on the game screen.
     */
    public void draw() {
        int w = 512;
        int h = 334;
        int l = 315;
        if (!isScreenMode(ScreenMode.FIXED)) {
            w = getFrameWidth();
            //  h = getFrameHeight() + 50;
            // l = 315 + getFrameHeight() - h;
        }
        Raster.drawAlphaFilledPixels(0, 0, w, h, 0x0093FF, 97);
        Raster.drawPixels(1, l, 0, 0xFFFFFF, w);
        getClientInstance().boldFont.drawBasicString(getInput() + (Game.loopCycle % 40 < 10 ? "|" : ""), 11, 330, 0xFFFFFF, 0);
        getClientInstance().smallFont.drawBasicString("Build: " + ProtocolConstants.PROTOCOL_REVISION,
                !isScreenMode(ScreenMode.FIXED) ? ((getFrameWidth() - 253) + 200) : 460, 313, 0xFFFFFF, 0);
        drawMessages();
    }

    /**
     * Draws messages within the {@code Console}.
     */
    public void drawMessages() {
        if (isOpen()) {
            int output_y = -3;
            int y_pos = 0;
            setScrollOffset(0);
            Raster.setDrawingArea(315, 0, 510, 21);
            for (int line = 0; line < 500; line++) {
                y_pos = (257 - output_y * 16) + getScrollPosition();
                if (getMessages()[line] != null) {
                    setScrollIndex(line - 1);
                    setUsingScroll((getScrollIndex() - 1 > 14));
                    getClientInstance().regularFont.drawBasicString(getMessages()[line], 9, y_pos, 0xFFFFFF, 0);
                    setScrollOffset(getScrollOffset() + 1);
                    output_y++;
                }
            }
            if (isUsingScroll()) {
                Scrollbar.drawConsole(Game.INSTANCE, 494, 22, 270, getScrollOffset(), 18);
            }

            Raster.setDrawingArea(512, 0, 334, 0);
        }
    }

    /**
     * Gets the {@link Game} instance.
     *
     * @return The instance.
     */
    public Game getClientInstance() {
        return Game.INSTANCE;
    }

    /**
     * Gets the current timestamp.
     *
     * @return The timestamp.
     */
    public String getTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(new Date());
    }

    /**
     * Gets whether or not the {@code Console} is open.
     *
     * @return {@code True} if so.
     */
    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * Toggles the {@code Console} open and closed.
     */
    public void toggle() {
        this.setOpen(!this.isOpen());
    }

    /**
     * The current message ID.
     */
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    /**
     * The current console input.
     */
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    /**
     * The current messages.
     */
    public String[] getMessages() {
        return messages;
    }

    /**
     * Whether or not the {@code Console} has to scroll.
     */
    public boolean isUsingScroll() {
        return usingScroll;
    }

    public void setUsingScroll(boolean usingScroll) {
        this.usingScroll = usingScroll;
    }

    /**
     * The current index of the scrollbar.
     */
    public int getScrollIndex() {
        return scrollIndex;
    }

    public void setScrollIndex(int scrollIndex) {
        this.scrollIndex = scrollIndex;
    }

    /**
     * The current position of the scrollbar.
     */
    public int getScrollPosition() {
        return scrollPosition;
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    /**
     * The current offset of the scrollbar.
     */
    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setScrollOffset(int scrollOffset) {
        this.scrollOffset = scrollOffset;
    }
}