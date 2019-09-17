package org.gielinor.game.world;


/**
 * Represents the game type.
 *
 * @author 'Vexia
 */
public enum GameType {

    ECONOMY,
    SPAWN;

    /**
     * Parses the game type.
     *
     * @param string the string.
     * @return {@code GameType} the type.
     */
    public static GameType parse(String string) {
        return Boolean.parseBoolean(string) ? ECONOMY : SPAWN;
    }
}