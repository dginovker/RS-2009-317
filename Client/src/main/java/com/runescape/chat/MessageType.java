package com.runescape.chat;

import main.java.com.runescape.Game;
import com.runescape.GameShell;
import com.runescape.util.StringUtility;

/**
 * Represents a type of message to show the player.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public enum MessageType {

    GAME_MESSAGE(0),
    CONSOLE(0) {
        @Override
        public void sendMessage(Game game, String gameMessage, int filterType) {
            GameShell.getConsole().printMessage(gameMessage.replaceAll(":console:", ""), 0);
        }
    },
    TRADE_REQ(4) {
        @Override
        public void sendMessage(Game game, String gameMessage, int filterType) {
            String playerName = gameMessage.substring(0, gameMessage.indexOf(":"));
            long playerNameLong = StringUtility.encodeBase37(playerName);
            boolean canSendTrade = false;
            for (int index = 0; index < game.getIgnoreCount(); index++) {
                if (game.getIgnores()[index] != playerNameLong) {
                    continue;
                }
                canSendTrade = true;

            }
            if (!canSendTrade) {
                game.pushMessage("wishes to trade with you.", getType(), playerName);
            }
        }
    },
    DUEL_REQ(8) {
        @Override
        public void sendMessage(Game game, String gameMessage, int filterType) {
            String playerName = gameMessage.substring(0, gameMessage.indexOf(":"));
            long l18 = StringUtility.encodeBase37(playerName);
            boolean canSendDuel = false;
            for (int k27 = 0; k27 < game.getIgnoreCount(); k27++) {
                if (game.getIgnores()[k27] != l18) {
                    continue;
                }
                canSendDuel = true;

            }
            if (!canSendDuel) {
                game.pushMessage("wishes to duel with you.", getType(), playerName);
            }
        }
    },
//    CLAN_REQ(17) {
//        @Override
//        public void sendMessage(Game game, String gameMessage, int filterType) {
//            String playerName = gameMessage.substring(0, gameMessage.indexOf(":"));
//            long l18 = StringUtility.encodeBase37(playerName);
//            boolean canSendChallenge = false;
//            for (int k27 = 0; k27 < game.getIgnoreCount(); k27++) {
//                if (game.getIgnores()[k27] != l18) {
//                    continue;
//                }
//                canSendChallenge = true;
//
//            }
//            if (!canSendChallenge) {
//                game.pushMessage("wishes to challenge your clan to a Clan War.", getType(), playerName);
//            }
//        }
//    },
    ;

    /**
     * The type of message to send.
     */
    private final int type;

    /**
     * Constructs a new message type.
     *
     * @param type The type of message to send.
     */
    MessageType(int type) {
        this.type = type;
    }

    /**
     * Gets the type of message to send.
     *
     * @return The type.
     */
    public int getType() {
        return type;
    }

    /**
     * The action to preform with the message.
     *
     * @param client      The client.
     * @param gameMessage The message.
     */
    public void sendMessage(Game client, String gameMessage, int filterType) {
        client.pushMessage(gameMessage.replaceAll(":" + getName() + ":", ""), type, -1, filterType);
    }

    /**
     * Gets the name to search for.
     *
     * @return The name.
     */
    public String getName() {
        return name().replaceAll("_", "").toLowerCase();
    }

}