package org.gielinor.game.world.map.zone;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.node.Node;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.Region;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Represents the borders of a zone.
 *
 * @author Emperor
 *
 */
public final class ZoneBorders {

    /**
     * The south west x-coordinate.
     */
    private final int southWestX;

    /**
     * The south west y-coordinate.
     */
    private final int southWestY;

    /**
     * The north east x-coordinate.
     */
    private final int northEastX;

    /**
     * The north east y-coordinate.
     */
    private final int northEastY;

    /**
     * The plane required to be on.
     */
    private int plane;

    /**
     * The list of exceptions.
     */
    private List<ZoneBorders> exceptions;

    /**
     * If we need to do a zero plane check.
     */
    private boolean zeroPlaneCheck;

    /**
     * Holds the region IDs of every {@link Region}
     * inside these borders.
     */
    private IntList REGION_IDS = new IntArrayList();

    /**
     * Constructs a new {@code ZoneBorders} {@code Object}.
     *
     * @param southWestX
     *            The south west x-coordinate.
     * @param southWestY
     *            The south west y-coordinate.
     * @param northEastX
     *            The north east x-coordinate.
     * @param northEastY
     *            The north east y-coordinate.
     */
    public ZoneBorders(int southWestX, int southWestY, int northEastX, int northEastY) {
        this(southWestX, southWestY, northEastX, northEastY, 0);
    }

    /**
     * Constructs a new {@code ZoneBorders} {@code Object}.
     *
     * @param southWestX
     *            The south west x-coordinate.
     * @param southWestY
     *            The south west y-coordinate.
     * @param northEastX
     *            The north east x-coordinate.
     * @param northEastY
     *            The north east y-coordinate.
     * @param plane
     *            the plane.
     */
    public ZoneBorders(int southWestX, int southWestY, int northEastX, int northEastY, int plane) {
        this.southWestX = southWestX;
        this.southWestY = southWestY;
        this.northEastX = northEastX;
        this.northEastY = northEastY;

        for (int x = southWestX >> 6; x < (northEastX >> 6) + 1; x++) {
            for (int y = southWestY >> 6; y < (northEastY >> 6) + 1; y++) {
                int id = y | x << 8;

                REGION_IDS.add(id);
            }
        }
        this.plane = plane;
    }

    /**
     * Constructs a new {@code ZoneBorders} {@code Object}.
     *
     * @param southWestX
     *            The south west x-coordinate.
     * @param southWestY
     *            The south west y-coordinate.
     * @param northEastX
     *            The north east x-coordinate.
     * @param northEastY
     *            The north east y-coordinate.
     * @param plane
     *            the plane.
     * @param zeroPlaneCheck
     *            the plane check.
     */
    public ZoneBorders(int southWestX, int southWestY, int northEastX, int northEastY, int plane, boolean zeroPlaneCheck) {
        this(southWestX, southWestY, northEastX, northEastY, plane);

        this.zeroPlaneCheck = zeroPlaneCheck;
    }

    /**
     * Creates zone borders for the given region id.
     *
     * @param regionId
     *            The region id.
     * @return The zone borders.
     */
    public static ZoneBorders forRegion(int regionId) {
        int baseX = ((regionId >> 8) & 0xFF) << 6;
        int baseY = (regionId & 0xFF) << 6;

        int size = 64 - 1;

        return new ZoneBorders(baseX, baseY, baseX + size, baseY + size);
    }

    /**
     * Checks if the location is inside the borders.
     *
     * @param location
     *            The location.
     * @return <code>True</code> if the location is inside the zone borders.
     */
    public boolean insideBorder(Location location) {
        return insideBorder(location.getX(), location.getY(), location.getZ());
    }

    /**
     * Checks if the node is inside the borders.
     *
     * @param node
     *            the node.
     * @return <code>True</code> if so.
     */
    public boolean insideBorder(Node node) {
        return insideBorder(node.getLocation());
    }

    /**
     * Checks if the player is in the zone
     *
     * @param x
     *            the x.
     * @param y
     *            the y.
     * @return the z.
     */
    public boolean insideBorder(int x, int y) {
        return insideBorder(x, y, 0);
    }

    /**
     * Checks if the coordinates are inside the borders.
     *
     * @param x
     *            The x-coordinate.
     * @param y
     *            The y-coordinate.
     * @param z
     *            The z coordinate.
     * @return <code>True</code> if the coordinates lay in the zone borders.
     */
    public boolean insideBorder(int x, int y, int z) {
        if (zeroPlaneCheck ? z != plane : (plane != 0 && z != plane)) {
            return false;
        }

        if (southWestX <= x && southWestY <= y && northEastX >= x && northEastY >= y) {
            if (exceptions != null) {
                for (ZoneBorders exception : exceptions) {
                    if (exception.insideBorder(x, y, z)) {
                        return false;
                    }
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Gets the ids of all the regions inside these borders.
     *
     * @return The region ids.
     */
    public IntList getRegionIds() {
        return REGION_IDS;
    }

    /**
     * Gets the southWestX.
     *
     * @return The southWestX.
     */
    public int getSouthWestX() {
        return southWestX;
    }

    /**
     * Gets the southWestY.
     *
     * @return The southWestY.
     */
    public int getSouthWestY() {
        return southWestY;
    }

    /**
     * Gets the northEastX.
     *
     * @return The northEastX.
     */
    public int getNorthEastX() {
        return northEastX;
    }

    /**
     * Gets the northEastY.
     *
     * @return The northEastY.
     */
    public int getNorthEastY() {
        return northEastY;
    }

    /**
     * Gets the exceptions.
     *
     * @return The exceptions.
     */
    public List<ZoneBorders> getExceptions() {
        return exceptions;
    }

    /**
     * Adds an exception.
     *
     * @param exception
     *            The exception to add.
     */
    public void addException(ZoneBorders exception) {
        if (exceptions == null) {
            this.exceptions = new ArrayList<>();
        }

        exceptions.add(exception);
    }

    @Override
    public String toString() {
        return "ZoneBorders [southWestX=" + southWestX + ", southWestY=" + southWestY + ", northEastX=" + northEastX
            + ", northEastY=" + northEastY + ", exceptions=" + exceptions + "]";
    }

    /**
     * Gets the bplane.
     *
     * @return the plane
     */
    public int getPlane() {
        return plane;
    }

    /**
     * Sets the baplane.
     *
     * @param plane
     *            the plane to set.
     */
    public void setPlane(int plane) {
        this.plane = plane;
    }

}
