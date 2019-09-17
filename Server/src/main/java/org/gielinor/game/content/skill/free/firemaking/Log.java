package org.gielinor.game.content.skill.free.firemaking;

/**
 * Represents an enumeration of burnable logs.
 *
 * @author 'Vexia
 */
public enum Log {
    NORMAL(21511, 1, 180, 5249, 40),
    ACHEY(22862, 1, 180, 5249, 40),
    OAK(21521, 15, 200, 5249, 60),
    WILLOW(21519, 30, 300, 5249, 90),
    TEAK(26333, 35, 450, 5249, 105),
    ARCTIC_PINE(30810, 42, 500, 5249, 125),
    MAPLE(21517, 45, 400, 5249, 135),
    MAHOGANY(26332, 50, 400, 5249, 157.5),
    EUCALYPTUS(32581, 58, 400, 5249, 193.5),
    YEW(21515, 60, 800, 5249, 202.5),
    MAGIC(21513, 75, 900, 5249, 303.8),
    CURSED_MAGIC(33567, 82, 1000, 5249, 303.8),
    PURPLE(30329, 1, 200, 20001, 50),
    WHITE(30328, 1, 200, 20000, 50),
    BLUE(27406, 1, 200, 26576, 50),
    GREEN(27405, 1, 200, 26575, 50),
    RED(27404, 1, 200, 26186, 50);
    /**
     * The log id.
     */
    private final int logId;

    /**
     * The level.
     */
    private final int level;

    /**
     * The life.
     */
    private final int life;

    /**
     * The fire id.
     */
    private final int fireId;

    /**
     * The exp gained.
     */
    private final double xp;

    /**
     * Constructs a new {@code FireMakingDefinitions.java} {@code Object}.
     *
     * @param logId  the log id.
     * @param level  the level.
     * @param life   the life.
     * @param fireId the fire id.
     * @param xp     the experience.
     */
    Log(int logId, int level, int life, int fireId, double xp) {
        this.logId = logId;
        this.level = level;
        this.life = life;
        this.fireId = fireId;
        this.xp = xp;
    }

    /**
     * Gets the logId.
     *
     * @return The logId.
     */
    public int getLogId() {
        return logId;
    }

    /**
     * Gets the level.
     *
     * @return The level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the life.
     *
     * @return The life.
     */
    public int getLife() {
        return life;
    }

    /**
     * Gets the fireId.
     *
     * @return The fireId.
     */
    public int getFireId() {
        return fireId;
    }

    /**
     * Gets the xp.
     *
     * @return The xp.
     */
    public double getXp() {
        return xp;
    }

    /**
     * Gets the log by the id.
     *
     * @param id the id.
     * @return the log.
     */
    public static Log forId(int id) {
        for (Log fire : Log.values()) {
            if (fire.getLogId() == id) {
                return fire;
            }
        }
        return null;
    }
}