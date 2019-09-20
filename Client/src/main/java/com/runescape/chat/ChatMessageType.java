package com.runescape.chat;

import main.java.com.runescape.Game;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.media.font.GameFont;
import com.runescape.util.StringUtility;

import java.util.Arrays;

/**
 * Represents a type of {@link com.runescape.chat.ChatMessage}.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public enum ChatMessageType {
    GAME_MESSAGE(0) {
        @Override
        public boolean canDraw(ChatMessage chatMessage, Game game) {
            if (chatMessage.getFilterType() == 1 && Game.INSTANCE.gameFilterMode == 1) {
                return false;
            }
            return game.getChatTypeView() == 0 || game.getChatTypeView() == 5;
        }

        @Override
        public void draw(ChatMessage chatMessage, Game game, int yOffset) {
            getRSFont().drawBasicString(chatMessage.getText(), 10, yOffset + getOffsetY(), isChatTransparent() ? 0xFFFFFF : 0, getShadowColour());
        }
    },
    PUBLIC_CHAT(1, 2) {
        @Override
        public boolean canDraw(ChatMessage chatMessage, Game game) {
            return (chatMessage.getType() == 1 || game.getPublicChatMode() == 0 || game.getPublicChatMode() == 1 &&
                    game.isFriendOrSelf(chatMessage.getNameClean())) &&
                    (game.getChatTypeView() == 0 || game.getChatTypeView() == 1);
        }

        @Override
        public void draw(ChatMessage chatMessage, Game game, int yOffset) {
            int xPos = 10;
            if (chatMessage.getChatIcons().length() > 0) {
                for (int imageId : chatMessage.getImageIds()) {
                    if (imageId >= 0) {
                        getRSFont().drawImage(imageId, xPos + 1, (yOffset - 12) + getOffsetY());
                        xPos += 16;
                    }
                }
            }

            getRSFont().drawBasicString(chatMessage.getNameCleanTitle() + ":", xPos, yOffset + getOffsetY(), isChatTransparent() ? 0xFFFFFF : 0, getShadowColour());
            xPos += getRSFont().getTextWidth(chatMessage.getNameCleanTitle()) + 6;
            getRSFont().drawBasicString(chatMessage.getText(), xPos, yOffset + getOffsetY(), isChatTransparent() ? 0x9090FF : 255, getShadowColour(), false);

        }
    },
    PRIVATE_CHAT(3, 7) {
        @Override
        public boolean canDraw(ChatMessage chatMessage, Game game) {
            return ((game.getSplitPrivateChat() == 0 || game.getChatTypeView() == 2) && (chatMessage.getType() == 7 ||
                    game.getPrivateChatMode() == 0 || game.getPrivateChatMode() == 1 && game.isFriendOrSelf(chatMessage.getNameClean()))) &&
                    (game.getChatTypeView() == 0 || game.getChatTypeView() == 2);
        }

        @Override
        public void draw(ChatMessage chatMessage, Game game, int yOffset) {
            int xPos = 10;
            getRSFont().drawBasicString("From", xPos, yOffset + getOffsetY(), isChatTransparent() ? 0xFFFFFF : 0, getShadowColour());
            xPos += getRSFont().getTextWidth("From ");
            if (chatMessage.getChatIcons().length() > 0) {
                for (int imageId : chatMessage.getImageIds()) {
                    if (imageId >= 0) {
                        getRSFont().drawImage(imageId, xPos, (yOffset - 13) + getOffsetY());
                        xPos += 14;
                    }
                }
            }
            getRSFont().drawBasicString(chatMessage.getNameCleanTitle() + ":", xPos, yOffset + getOffsetY(), isChatTransparent() ? 0xFFFFFF : 0, getShadowColour());
            xPos += getRSFont().getTextWidth(chatMessage.getNameCleanTitle()) + 8;
            getRSFont().drawBasicString(chatMessage.getText(), xPos, yOffset + getOffsetY(), isChatTransparent() ? 0xBF2020 : 0x7F0000, getShadowColour());
        }

        @Override
        public boolean canDrawSplit(ChatMessage chatMessage, Game game) {
            return (chatMessage.getType() == 7 || game.getPrivateChatMode() == 0 || game.getPrivateChatMode() == 1 &&
                    game.isFriendOrSelf(chatMessage.getNameClean()));
        }

        @Override
        public void drawSplit(ChatMessage chatMessage, Game game, int yOffset) {
            int xPos = 4;
            if (chatMessage.getChatIcons().length() > 0) {
                for (int imageId : chatMessage.getImageIds()) {
                    if (imageId >= 0) {
                        getRSFont().drawImage(imageId, xPos, yOffset - 13);
                        xPos += 14;
                    }
                }
            }
            // System.out.println(chatMessage.getChatIcons());
            getRSFont().drawBasicString("From " + chatMessage.getNameCleanTitle() + ": " + chatMessage.getText(), xPos, yOffset, 0x00FFFF, 0);
        }
    },
    PRIVATE_CHAT_TO(6) {
        @Override
        public boolean canDraw(ChatMessage chatMessage, Game game) {
            return ((game.getSplitPrivateChat() == 0 || game.getChatTypeView() == 2) && game.getPrivateChatMode() < 2) &&
                    (game.getChatTypeView() == 0 || game.getChatTypeView() == 2);
        }

        @Override
        public void draw(ChatMessage chatMessage, Game game, int yOffset) {
            getRSFont().drawBasicString("To " + chatMessage.getNameClean() + ":", 10, getOffsetY() + yOffset, isChatTransparent() ? 0xFFFFFF : 0, getShadowColour());
            getRSFont().drawBasicString(chatMessage.getText(), 15 + getRSFont().getTextWidth("To :" + chatMessage.getNameClean()), getOffsetY() + yOffset, isChatTransparent() ? 0xBF2020 : 0x7F0000, getShadowColour());
        }

        @Override
        public boolean canDrawSplit(ChatMessage chatMessage, Game game) {
            return game.getPrivateChatMode() < 2;
        }

        @Override
        public void drawSplit(ChatMessage chatMessage, Game game, int yOffset) {
            getRSFont().drawBasicString("To " + chatMessage.getNameClean() + ": " + chatMessage.getText(), 4, yOffset, 0x00FFFF, 0);
        }
    },
    TRADE(4) {
        @Override
        public boolean canDraw(ChatMessage chatMessage, Game game) {
            return (game.getTradeMode() == 0 || game.getTradeMode() == 1 && game.isFriendOrSelf(chatMessage.getNameClean()))
                    && (game.getChatTypeView() == 0 || game.getChatTypeView() == 3);
        }

        @Override
        public void draw(ChatMessage chatMessage, Game game, int yOffset) {
            getRSFont().drawBasicString(chatMessage.getNameClean() + " " + chatMessage.getText(), 10, yOffset + getOffsetY(), isChatTransparent() ? 0xDF20FF : 0x7F007F, getShadowColour());
        }
    },
    PRIVATE_MESSAGE_NOTIFICATION(5) {
        @Override
        public boolean canDraw(ChatMessage chatMessage, Game game) {
            return chatMessage.getClearDelay() != 0 && (game.getSplitPrivateChat() == 0 && game.getPrivateChatMode() < 2) && (game.getChatTypeView() == 0 || game.getChatTypeView() == 2);
        }

        @Override
        public void draw(ChatMessage chatMessage, Game game, int yOffset) {
            getRSFont().drawBasicString(chatMessage.getText(), 10, yOffset + getOffsetY(), 0x800000, getShadowColour());
            if (chatMessage.getClearDelay() > 0) {
                chatMessage.decreaseClearDelay();
            }
        }

        @Override
        public boolean canDrawSplit(ChatMessage chatMessage, Game game) {
            return chatMessage.getClearDelay() != 0 && game.getPrivateChatMode() < 2;
        }

        @Override
        public void drawSplit(ChatMessage chatMessage, Game game, int yOffset) {
            getRSFont().drawBasicString(chatMessage.getText(), 4, yOffset, 0x01FFFFF, 0);
            if (chatMessage.getClearDelay() > 0) {
                chatMessage.decreaseClearDelay();
            }
        }
    },
    DUEL(8) {
        @Override
        public boolean canDraw(ChatMessage chatMessage, Game game) {
            return (game.getTradeMode() == 0 || game.getTradeMode() == 1 && game.isFriendOrSelf(chatMessage.getNameClean()))
                    && (game.getChatTypeView() == 0 || game.getChatTypeView() == 3);
        }

        @Override
        public void draw(ChatMessage chatMessage, Game game, int yOffset) {
            getRSFont().drawBasicString(chatMessage.getNameClean() + " " + chatMessage.getText(), 10, yOffset + getOffsetY(),
                    isChatTransparent() ? 0xFF20DF : 0x7E3200, getShadowColour());
        }
    },
    URL_MESSAGE(12) {
        @Override
        public boolean canDraw(ChatMessage chatMessage, Game game) {
            if (chatMessage.getFilterType() == 1 && Game.INSTANCE.gameFilterMode == 1) {
                return false;
            }
            return game.getChatTypeView() == 0 || game.getChatTypeView() == 5;
        }

        @Override
        public void draw(ChatMessage chatMessage, Game game, int yOffset) {
            getRSFont().drawBasicString(chatMessage.getNameClean(), 10, yOffset + getOffsetY(), isChatTransparent() ? 0xFFFFFF : 0, getShadowColour());
        }
    },
    CLAN_CHAT(16) {
        @Override
        public boolean canDraw(ChatMessage chatMessage, Game game) {
            return chatMessage.getClanName() != null && (game.clanChatMode == 0 || game.clanChatMode == 1 &&
                    (game.isFriendOrSelf(chatMessage.getNameClean()) || Arrays.asList(chatMessage.getImageIds()).contains(0)
                            || Arrays.asList(chatMessage.getImageIds()).contains(1))) &&
                    (game.getChatTypeView() == 11 || game.getChatTypeView() == 0);
        }

        @Override
        public void draw(ChatMessage chatMessage, Game game, int yOffset) {
            // TODO May have to draw via gameframe!
            int clanNameWidth = getRSFont().getTextWidth(StringUtility.formatUsername(chatMessage.getClanName()));
            int xPos = 14;
            if (chatMessage.getChatIcons().length() > 0) {
                for (int imageId : chatMessage.getImageIds()) {
                    if (imageId >= 0) {
                        getRSFont().drawImage(imageId, xPos + clanNameWidth + 9, yOffset + getOffsetY() - 13);
                        xPos += 14;
                    }
                }
            }
            getRSFont().drawBasicString("[", 10, yOffset + getOffsetY(), isChatTransparent() ? 0xFFFFFF : 0, getShadowColour());
            getRSFont().drawBasicString(StringUtility.formatUsername(chatMessage.getClanName()), 15, yOffset + getOffsetY(), isChatTransparent() ? 0x9070FF : 255, getShadowColour());
            getRSFont().drawBasicString("]", clanNameWidth + 15, yOffset + getOffsetY(), isChatTransparent() ? 0xFFFFFF : 0, getShadowColour());
            xPos += clanNameWidth + 8;
            getRSFont().drawBasicString(StringUtility.formatUsername(chatMessage.getNameClean()) + ":", xPos, yOffset + getOffsetY(), isChatTransparent() ? 0xFFFFFF : 0, getShadowColour());
            xPos += getRSFont().getTextWidth(chatMessage.getNameClean()) + 6;
            getRSFont().drawBasicString(chatMessage.getText(), xPos, yOffset + getOffsetY(), isChatTransparent() ? 0xEF5050 : 0x800000, getShadowColour(), false);
        }
    },
    CLAN(17) {
        @Override
        public boolean canDraw(ChatMessage chatMessage, Game game) {
            // TODO CLAN VIEW
            return (game.getChatTypeView() == 0 || game.getChatTypeView() == 4);
        }

        @Override
        public void draw(ChatMessage chatMessage, Game game, int yOffset) {
            getRSFont().drawBasicString(chatMessage.getNameClean() + " " + chatMessage.getText(), 10, yOffset + getOffsetY(),
                    isChatTransparent() ? 0xFF20DF : 0x7E3200, getShadowColour());
        }
    },;

    /**
     * The type ids.
     */
    private final int[] ids;

    /**
     * Constructs a <code>ChatMessageType</code>.
     *
     * @param ids The ids of the types.
     */
    ChatMessageType(int... ids) {
        this.ids = ids;
    }

    /**
     * Gets a {@link com.runescape.chat.ChatMessageType} by the type id.
     *
     * @return The chat message type.
     */
    public static ChatMessageType forId(int id) {
        for (ChatMessageType chatMessageType : ChatMessageType.values()) {
            for (int type : chatMessageType.getIds()) {
                if (type == id) {
                    return chatMessageType;
                }
            }
        }
        return null;// throw new IndexOutOfBoundsException("No ChatMessageType class found for ID : " + id + "!");
    }

    /**
     * Gets the type ids.
     *
     * @return The ids.
     */
    public int[] getIds() {
        return ids;
    }

    /**
     * Checks if the {@link com.runescape.chat.ChatMessage} can be drawn.
     *
     * @param chatMessage The {@link com.runescape.chat.ChatMessage} to draw.
     * @param game        The game instance.
     * @return <code>True</code> if so.
     */
    public boolean canDraw(ChatMessage chatMessage, Game game) {
        return true;
    }

    /**
     * Draws the {@link com.runescape.chat.ChatMessage}.
     *
     * @param chatMessage The {@link com.runescape.chat.ChatMessage} to draw.
     * @param game        The game instance.
     * @param yOffset     The current y offset.
     */
    public void draw(ChatMessage chatMessage, Game game, int yOffset) {

    }

    /**
     * Checks if the split {@link com.runescape.chat.ChatMessage} can be drawn.
     *
     * @param chatMessage The {@link com.runescape.chat.ChatMessage} to draw.
     * @param game        The game instance.
     * @return <code>True</code> if so.
     */
    public boolean canDrawSplit(ChatMessage chatMessage, Game game) {
        return false;
    }

    /**
     * Draws the split {@link com.runescape.chat.ChatMessage}.
     *
     * @param chatMessage The {@link com.runescape.chat.ChatMessage} to draw.
     * @param game        The game instance.
     * @param yOffset     The current y offset.
     */
    public void drawSplit(ChatMessage chatMessage, Game game, int yOffset) {

    }

    /**
     * Gets the y offset for drawing messages.
     *
     * @return The offset.
     */
    public int getOffsetY() {
        return (Game.getScreenMode() == Game.ScreenMode.FIXED ? 0 : Game.getFrameHeight() - 165);
    }

    /**
     * Gets the {@link com.runescape.media.font.GameFont} to use for drawing the messages.
     *
     * @return The font.
     */
    public GameFont getRSFont() {
        return Game.INSTANCE.regularFont;
    }

    /**
     * Gets the bold {@link com.runescape.media.font.GameFont} to use for drawing the messages.
     *
     * @return The bold font.
     */
    public GameFont getBoldFont() {
        return Game.INSTANCE.boldFont;
    }

    /**
     * Gets the shadow colour.
     *
     * @return The shadow colour.
     */
    public int getShadowColour() {
        return (Game.INSTANCE.getInterfaceConfig(InterfaceConfiguration.TRANSPARENT_CHAT) == 1 && Game.getScreenMode() != Game.ScreenMode.FIXED) ? 0 : -1;
    }

    /**
     * If the chat is drawing transparent.
     *
     * @return <code>True</code> if so.
     */
    public boolean isChatTransparent() {
        return (Game.INSTANCE.getInterfaceConfig(InterfaceConfiguration.TRANSPARENT_CHAT) == 1 && Game.getScreenMode() != Game.ScreenMode.FIXED);
    }
}
