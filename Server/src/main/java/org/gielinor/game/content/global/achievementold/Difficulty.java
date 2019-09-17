package org.gielinor.game.content.global.achievementold;

/**
 * The difficulty levels of the achievement diary entries.
 *
 * @author Torchic
 */
public enum Difficulty {

    EASY("an easy"),
    MEDIUM("a medium"),
    HARD("a hard"),
    ELITE("an elite");

    /**
     * The type of difficulty.
     */
    String type;

    /**
     * Constructs a new {@Code Difficulty} {@Code Object}
     *
     * @param type The type.
     */
    Difficulty(String type) {
        this.type = type;
    }

    /**
     * Gets the type.
     *
     * @return
     */
    public String getType() {
        return type;
    }
}
