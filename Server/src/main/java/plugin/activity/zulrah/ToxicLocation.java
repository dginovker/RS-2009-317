package plugin.activity.zulrah;

import java.awt.Point;

/**
 * Represents a spawn location for toxic smoke objects and snakelings.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum ToxicLocation {

    SOUTH_WEST(new Point(2262, 3069), new Point(2265, 3068)),
    SOUTH_EAST(new Point(2268, 3068), new Point(2271, 3069)),
    EAST(new Point(2272, 3071), new Point(2272, 3074)),
    WEST(new Point(2262, 3071), new Point(2262, 3074));

    /**
     * The spawn points.
     */
    private Point[] points;

    /**
     * Creates a new <code>ToxicLocation</code>.
     *
     * @param points The spawn points.
     */
    private ToxicLocation(Point... points) {
        this.points = points;
    }

    /**
     * Gets the spawn points.
     *
     * @return The points.
     */
    public Point[] getPoints() {
        return points;
    }

}
