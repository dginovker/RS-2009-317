package plugin.interaction.object.dmc;

/**
 * The DMC aiming directions.
 *
 * @author Emperor
 */
public enum DMCRevolution {

    NORTH(515),
    NORTH_EAST(516),
    EAST(517),
    SOUTH_EAST(518),
    SOUTH(519),
    SOUTH_WEST(520),
    WEST(521),
    NORTH_WEST(514);

    /**
     * The animation id.
     */
    private final int animationId;

    /**
     * Constructs a new {@Code DMCRevolution} {@Code Object}
     *
     * @param animationId The animation id.
     */
    private DMCRevolution(int animationId) {
        this.animationId = animationId;
    }

    /**
     * Gets the animationId.
     *
     * @return the animationId
     */
    public int getAnimationId() {
        return animationId;
    }
}