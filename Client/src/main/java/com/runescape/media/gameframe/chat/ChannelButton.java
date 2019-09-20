package com.runescape.media.gameframe.chat;

import main.java.com.runescape.Game;
import com.runescape.cache.media.Sprite;
import com.runescape.media.font.GameFont;
import com.runescape.util.TextConstants;

/**
 * Represents a channel button.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public enum ChannelButton {
    ALL(5, -1, -1, 26, 157, null),
    GAME(62, 88, 163, 76, 152, new String[]{"On", "Filtered"}, 65280, 0xFFFF00) {
        @Override
        public int getStatusMode(Game game) {
            return game.gameFilterMode;
        }
    },
    PUBLIC(119, 146, 163, 132, 152, new String[]{"On", "Friends", "Off", "Hide"}, 65280, 0xffff00, 0xff0000, 65535) {
        @Override
        public int getStatusMode(Game game) {
            return game.getPublicChatMode();
        }
    },
    PRIVATE(176, 203, 163, 184, 152, new String[]{"On", "Friends", "Off", "Hide"}, 65280, 0xffff00, 0xff0000, 65535) {
        @Override
        public int getStatusMode(Game game) {
            return game.getPrivateChatMode();
        }
    },
    CLAN(233, 260, 163, 249, 152, new String[]{"On", "Friends", "Off", "Hide"}, 65280, 0xffff00, 0xff0000, 65535) {
        @Override
        public int getStatusMode(Game game) {
            return game.clanChatMode;
        }
    },
    TRADE(290, 317, 163, 304, 152, new String[]{"On", "Friends", "Off", "Hide"}, 65280, 0xffff00, 0xff0000, 65535) {
        @Override
        public int getStatusMode(Game game) {
            return game.getTradeMode();
        }
    },
    ASSIST(347, 374, 163, 359, 152, new String[]{"On", "Friends", "Off", "Hide"}, 65280, 0xffff00, 0xff0000, 65535) {
        @Override
        public int getStatusMode(Game game) {
            return game.assistMode;
        }
    },
    REPORT(404, -1, -1, 442, 157, null) {
        @Override
        public void draw(Game game, int xOffset, int yOffset) {
            GameFont smallFont = game.smallFont;
            Sprite button = null;
            if (game.currentHoveredButton == this) {
                button = game.getReportButton();
            }
            if (button != null) {
                button.drawSprite(getButtonX(), 142 + yOffset);
            }
            String modeName = TextConstants.uppercaseFirst(name().toLowerCase()).replaceAll("_", " ");
            smallFont.drawBasicString(modeName, getNameX() + xOffset, getNameY() + yOffset, 0xFFFFFF, 0);
        }
    }, ;

    /**
     * The x coordinate to draw the button.
     */
    private final int buttonX;
    /**
     * The mode status x coordinate.
     */
    private final int statusX;
    /**
     * The mode status y coordinate.
     */
    private final int statusY;
    /**
     * The x coordinate to draw the name.
     */
    private final int nameX;
    /**
     * The y coordinate to draw the name.
     */
    private final int nameY;
    /**
     * The status text colors.
     */
    private final int[] statusColors;
    /**
     * The mode names.
     */
    private String[] modeNames;

    ChannelButton(int buttonX, int statusX, int statusY, int nameX, int nameY, int... statusColors) {
        this.buttonX = buttonX;
        this.statusX = statusX;
        this.statusY = statusY;
        this.nameX = nameX;
        this.nameY = nameY;
        this.statusColors = statusColors;
    }

    ChannelButton(int buttonX, int statusX, int statusY, int nameX, int nameY, String[] modeNames, int... statusColors) {
        this.buttonX = buttonX;
        this.statusX = statusX;
        this.statusY = statusY;
        this.nameX = nameX;
        this.nameY = nameY;
        this.modeNames = modeNames;
        this.statusColors = statusColors;
    }

    /**
     * Draws the channel button.
     */
    public void draw(Game game, int xOffset, int yOffset) {
        GameFont smallFont = game.smallFont;
        Sprite button = null;
        if (game.currentActiveButton == this) {
            button = game.getChatTabOn();
        }
        if (game.currentHoveredButton == this) {
            button = (game.currentHoveredButton == game.currentActiveButton) ? game.getChatTabHover() : game.getChatTab();
        }
        if (button != null) {
            button.drawSprite(buttonX, 142 + yOffset);
        }

        String modeName = TextConstants.uppercaseFirst(name().toLowerCase()).replaceAll("_", " ");
        smallFont.drawBasicString(modeName, nameX + xOffset, nameY + yOffset, 0xFFFFFF, 0);
        int status = getStatusMode(game);
        if (modeNames != null && statusColors != null && status < modeNames.length) {
            smallFont.drawCenteredString(modeNames[status], statusX, statusY + yOffset, statusColors[status], 0);
        }
    }

    /**
     * Gets the current status mode.
     *
     * @return The status mode.
     */
    public int getStatusMode(Game game) {
        return 0;
    }

    /**
     * The x coordinate to draw the button.
     */
    public int getButtonX() {
        return buttonX;
    }

    /**
     * The mode status x coordinate.
     */
    public int getStatusX() {
        return statusX;
    }

    /**
     * The mode status y coordinate.
     */
    public int getStatusY() {
        return statusY;
    }

    /**
     * The x coordinate to draw the name.
     */
    public int getNameX() {
        return nameX;
    }

    /**
     * The y coordinate to draw the name.
     */
    public int getNameY() {
        return nameY;
    }
}
