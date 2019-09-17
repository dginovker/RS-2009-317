package com.runescape.media.gameframe;

/**
 * Represents a cursor.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public enum GameCursor {

    NONE(0, "cancel"),
    TAKE(1, "take"),
    USE(2, "use"),
    TALK_TO(3, "talk-to", "talk to"),
    ENTER_EXIT_CAVE(4, "enter-cave", "exit-cave", "enter cave", "exit cave"),
    FISH(5, "net", "bait", "lure", "cage", "harpoon"),
    CHOP(6, "chop", "cut down", "cut-down"),
    PRAY(7, "pray-at", "pray at", "turn quick-", "select quick-"),
    MINE(8, "mine"),
    EAT(9, "eat"),
    DRINK(10, "drink"),
    WEAR(11, "wield", "wear"),
    ATTACK(12, "attack ", "hit "),
    PLACEHOLDER(13, "cursor_placeholder"),
    ENTER(14, "enter"),
    CLIMB_DOWN(16, "climb down", "climb-down"),
    CLIMB_UP(15, "climb up", "climb-up", "climb"),
    SEARCH(17, "search"),
    STEAL(18, "pickpocket", "steal from", "steal-from"),
    SMELT(19, "smelt", "smith"),
    CLEAN(20, "clean <col=ff9040"),
    CLIMB_OVER(21, "climb over", "climb-over", "jump over", "jump-over", "tackle o", "use shortc"),
    BACK(22, "back"),
    DEPOSIT_BANK(22, "deposit <col=00ffff>bank deposit box"),
    DEPOSIT(23, "deposit", "store "),
    SETTINGS(46, "settings"),
    ACCEPT(25, "accept", "yes ", "yes,"),
    DECLINE(26, "decline", "no ", "no,"),
    CAST_ICE_BARRAGE(27, "cast <col=00ff00>ice barrage"),
    CAST_BLOOD_BARRAGE(28, "cast <col=00ff00>blood barrage"),
    CAST_SHADOW_BARRAGE(29, "cast <col=00ff00>shadow barrage"),
    CAST_SMOKE_BARRAGE(30, "cast <col=00ff00>smoke barrage"),
    CAST_ICE_BLITZ(31, "cast <col=00ff00>ice blitz"),
    CAST_BLOOD_BLITZ(32, "cast <col=00ff00>blood blitz"),
    CAST_SHADOW_BLITZ(33, "cast <col=00ff00>shadow blitz"),
    CAST_SMOKE_BLITZ(34, "cast <col=00ff00>smoke blitz"),
    CAST_ICE_BURST(35, "cast <col=00ff00>ice burst"),
    CAST_BLOOD_BURST(36, "cast <col=00ff00>blood burst"),
    CAST_SHADOW_BURST(37, "cast <col=00ff00>shadow burst"),
    CAST_SMOKE_BURST(38, "cast <col=00ff00>smoke burst"),
    CAST_ICE_RUSH(39, "cast <col=00ff00>ice rush"),
    CAST_BLOOD_RUSH(40, "cast <col=00ff00>blood rush"),
    CAST_SHADOW_RUSH(41, "cast <col=00ff00>shadow rush"),
    CAST_SMOKE_RUSH(42, "cast <col=00ff00>smoke rush"),
    INFORMATION(43, "information", "help ", "swap to information"),
    CAST_HIGH_ALCHEMY(44, "cast <col=00ff00>high level alchemy"),
    CAST_LOW_ALCHEMY(45, "cast <col=00ff00>low level alchemy"),
    TELEPORTS(48, "teleports"),
    WITHDRAW(49, "withdraw"),
    SLASH(50, "slash"),
    CAST(51, "cast <col=00ff00>"),;
    /**
     * The id of this cursor.
     */
    private final int id;
    /**
     * The text associated with this cursor.
     */
    private final String[] text;

    /**
     * Constructs a new <code>GameCursor</code>.
     *
     * @param id   The id of this cursor.
     * @param text The text associated with this cursor.
     */
    GameCursor(int id, String... text) {
        this.id = id;
        this.text = text;
    }

    /**
     * Gets a cursor by the id.
     *
     * @param id The id.
     * @return The cursor.
     */
    public static GameCursor forId(int id) {
        for (GameCursor gameCursor : values()) {
            if (gameCursor.getId() == id) {
                return gameCursor;
            }
        }
        return NONE;
    }

    /**
     * Gets a cursor for the provided text.
     *
     * @param text The text.
     * @return The cursor.
     */
    public static GameCursor forText(String text) {
        for (GameCursor gameCursor : values()) {
            if (gameCursor.getText() == null) {
                continue;
            }
            for (String s : gameCursor.getText()) {
                if (text == null) {
                    continue;
                }
                if (text.toLowerCase().startsWith(s.toLowerCase())) {
                    return gameCursor;
                }
            }
        }
        return NONE;
    }

    /**
     * Gets the id of this cursor.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the text associated with this cursor.
     *
     * @return The text.
     */
    public String[] getText() {
        return text;
    }

}
