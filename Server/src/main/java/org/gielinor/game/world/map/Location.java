package org.gielinor.game.world.map;

import org.gielinor.game.interaction.DestinationFlag;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.world.map.path.Path;
import org.gielinor.game.world.map.path.Pathfinder;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.utilities.misc.RandomUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a location on the world map.
 *
 * @author Emperor
 */
public final class Location extends Node {

    /**
     * The x-coordinate.
     */
    private int x;

    /**
     * The y-coordinate.
     */
    private int y;

    /**
     * The plane.
     */
    private int z;

    /**
     * The randomizer.
     */
    private transient int randomizer = 0;

    /**
     * Constructs a new {@code Location} {@code Object}.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     */
    public Location(int x, int y, int z) {
        super(null, null);
        super.setDestinationFlag(DestinationFlag.LOCATION);
        this.x = x;
        this.y = y;
        if (z < 0) {
            z += 4;
        }
        this.z = z;
    }


    /**
     * Constructs a new {@code Location} {@code Object}.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Location(int x, int y) {
        this(x, y, 0);
    }

    /**
     * Constructs a new {@code Location} {@code Object}.
     *
     * @param x          The x-coordinate.
     * @param y          The y coordinate.
     * @param z          The z-coordinate.
     * @param randomizer The amount we should randomize the x and y coordinates with (x + random(randomizer), y + random(randomizer)).
     */
    public Location(int x, int y, int z, int randomizer) {
        this(x + RandomUtil.getRandom(randomizer), y + RandomUtil.getRandom(randomizer), z);
        this.randomizer = randomizer;
    }

    /**
     * Construct a new Location.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     * @return The constructed location.
     */
    public static Location create(int x, int y, int z) {
        return new Location(x, y, z);
    }

    /**
     * Construct a new Location.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The constructed location.
     */
    public static Location create(int x, int y) {
        return new Location(x, y, 0);
    }

    /**
     * Creates a new instance of the given location.
     *
     * @param location The given location.
     * @return The new instance.
     */
    public static Location create(Location location) {
        return create(location.getX(), location.getY(), location.getZ());
    }

    /**
     * Creates a location instance with coordinates being the difference between {@code other} & {@code location}.
     *
     * @param location The first location.
     * @param other    The other location.
     * @return The delta location.
     */
    public static Location getDelta(Location location, Location other) {
        return Location.create(other.x - location.x, other.y - location.y, other.z - location.z);
    }

    @Override
    public Location getLocation() {
        return this;
    }

    /**
     * Checks if this location is right next to the node (assuming the node is size 1x1).
     *
     * @param node The node to check.
     * @return <code>True</code> if this location is 1 tile north, west, south or east of the node location.
     */
    public boolean isNextTo(Node node) {
        Location l = node.getLocation();
        if (l.getY() == y) {
            return l.getX() - x == -1 || l.getX() - x == 1;
        }
        if (l.getX() == x) {
            return l.getY() - y == -1 || l.getY() - y == 1;
        }
        return false;
    }

    /**
     * Gets the region id.
     *
     * @return The region id.
     */
    public int getRegionId() {
        return (x >> 6) << 8 | (y >> 6);
    }

    /**
     * Gets the location incremented by the given coordinates.
     *
     * @param dir The direction to transform this location.
     * @return The location.
     */
    public Location transform(Direction dir) {
        return transform(dir, 1);
    }

    /**
     * Gets the location incremented by the given coordinates.
     *
     * @param dir   The direction to transform this location.
     * @param steps The amount of steps to move in this direction.
     * @return The location.
     */
    public Location transform(Direction dir, int steps) {
        return new Location(x + (dir.getStepX() * steps), y + (dir.getStepY() * steps), this.z);
    }

    /**
     * Gets the location incremented by the given coordinates.
     *
     * @param l The location to transform.
     * @return The location.
     */
    public Location transform(Location l) {
        return new Location(x + l.getX(), y + l.getY(), this.z + l.getZ());
    }

    /**
     * Gets the location incremented by the given coordinates.
     *
     * @param diffX The x-difference.
     * @param diffY The y-difference.
     * @param z     The height difference.
     * @return The location.
     */
    public Location transform(int diffX, int diffY, int z) {
        return new Location(x + diffX, y + diffY, this.z + z);
    }

    /**
     * Gets the delay from the distance.
     *
     * @param otherLocation the end location
     * @return the delay.
     */
    public int getDelay(Location otherLocation) {
        return (int) (2 + (getDistance(otherLocation) * 0.5));
    }

    /**
     * Checks if the other location is within viewing distance.
     *
     * @param other The other location.
     * @return If you're within the other distance.
     */
    public boolean withinDistance(Location other) {
        return withinDistance(other, MapDistance.RENDERING.getDistance());
    }

    /**
     * Returns if a player is within a specified distance.
     *
     * @param other The other location.
     * @param dist  The amount of distance.
     * @return If you're within the other distance.
     */
    public boolean withinDistance(Location other, int dist) {
        if (other.z != z) {
            return false;
        }
        int deltaX = other.x - x, deltaY = other.y - y;
        return deltaX <= dist && deltaX >= -dist && deltaY <= dist && deltaY >= -dist;
    }

    /**
     * Returns the distance between you and the other.
     *
     * @param other The other location.
     * @return The amount of distance between you and other.
     */
    public double getDistance(Location other) {
        int xdiff = this.getX() - other.getX();
        int ydiff = this.getY() - other.getY();
        return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
    }

    /**
     * Returns the distance between the first and the second specified distance.
     *
     * @param first  The first location.
     * @param second The other location.
     * @return The amount of distance between first and other.
     */
    public static double getDistance(Location first, Location second) {
        int xdiff = first.getX() - second.getX();
        int ydiff = first.getY() - second.getY();
        return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
    }

    /**
     * Checks if this location is within the area of the given location.
     *
     * @param x  The first x coordinate.
     * @param y  The first y coordinate.
     * @param x2 The second x coordinate.
     * @param y2 The second y coordinate.
     * @return Whether or not it is in the area.
     */
    public boolean inArea(int x, int y, int x2, int y2) {
        return this.x >= x && this.y >= y && this.x <= x2 && this.y <= y2;
    }

    /**
     * Checks if this location is within the zoneborders area.
     *
     * @param zoneBorders The zoneborders.
     * @return Whether or not it is in the area.
     */
    public boolean inArea(ZoneBorders zoneBorders) {
        return this.x >= zoneBorders.getSouthWestX() && this.y >= zoneBorders.getSouthWestY()
            && this.x <= zoneBorders.getNorthEastX() && this.y <= zoneBorders.getNorthEastY();
    }

    /**
     * Gets a random location within the zone borders.
     *
     * @param borders The zone borders.
     * @return The random location.
     */
    public static Location getRandomLocation(ZoneBorders borders) {
        Location finalLocation = new Location(borders.getSouthWestX() + RandomUtil.getRandom(borders.getNorthEastX() - borders.getSouthWestX()), borders.getSouthWestY() + RandomUtil.getRandom(borders.getNorthEastY() - borders.getSouthWestY()), 0);
        while (!RegionManager.isTeleportPermitted(finalLocation)) {
            finalLocation = new Location(borders.getSouthWestX() + RandomUtil.getRandom(borders.getNorthEastX() - borders.getSouthWestX()), borders.getSouthWestY() + RandomUtil.getRandom(borders.getNorthEastY() - borders.getSouthWestY()), 0);
        }
        return finalLocation;
    }

    /**
     * Gets a random location within the zone borders.
     *
     * @param borders The zone borders.
     * @param height  The height.
     * @return The random location.
     */
    public static Location getRandomLocation(ZoneBorders borders, int height) {
        Location finalLocation = new Location(borders.getSouthWestX() + RandomUtil.getRandom(borders.getNorthEastX() - borders.getSouthWestX()), borders.getSouthWestY() + RandomUtil.getRandom(borders.getNorthEastY() - borders.getSouthWestY()), height);
        while (!RegionManager.isTeleportPermitted(finalLocation)) {
            finalLocation = new Location(borders.getSouthWestX() + RandomUtil.getRandom(borders.getNorthEastX() - borders.getSouthWestX()), borders.getSouthWestY() + RandomUtil.getRandom(borders.getNorthEastY() - borders.getSouthWestY()), height);
        }
        return finalLocation;
    }

    /**
     * Gets a random location near the main location.
     *
     * @param main      The main location.
     * @param radius    The radius.
     * @param reachable If the locations should be able to reach eachother.
     * @return The location.
     */
    public static Location getRandomLocation(Location main, int radius, boolean reachable) {
        Location location = RegionManager.getTeleportLocation(main, radius);
        if (!reachable) {
            return location;
        }
        Path path = Pathfinder.find(main, location, false, Pathfinder.DUMB);
        if (!path.isSuccessful()) {
            location = main;
            if (!path.getPoints().isEmpty()) {
                Point p = path.getPoints().getLast();
                location = Location.create(p.getX(), p.getY(), main.getZ());
            }
        }
        return location;
    }

    public Location getRandomClippedLocation(int radius) {
        Location newLocation = Location.create(RandomUtil.random(x - radius, x + radius), RandomUtil.random(y - radius, y + radius), z);
        if (RegionManager.getClippingFlag(newLocation.z, newLocation.getX(), newLocation.getY()) != 0) {
            return getRandomClippedLocation(radius);
        }
        return newLocation;
    }

    /**
     * Gets the location next to this location.
     *
     * @return The location.
     */
    public Location getNextTo() {
        ArrayList<Location> locations = new ArrayList<>();
        Location closestLocation = getClosestLocation(this, locations);
        return closestLocation == null ? this.transform(-1, 1, 0) : closestLocation;
    }

    /**
     * Gets the combative location next to this one.
     *
     * @return The location.
     */
    public Location getNextToCombat() {
        Location[] locations = new Location[]{ getWest(), getEast(), getNorth(), getSouth() };
        for (Location location1 : locations) {
            if (RegionManager.isTeleportPermitted(location1)) {
                return location1;
            }
        }
        return getNextTo();
    }

    /**
     * @return a list of locations gathered by using a Flood Fill algorithm
     */
    public List<Location> floodFillArea() {
        List<Location> filledTiles = new ArrayList<>();
        List<String> checkedTiles = new ArrayList<>();
        floodFill(x, y, filledTiles, checkedTiles);
        return filledTiles;
    }

    /**
     * A recursive function to Flood Fill a given area
     * @param x x coord
     * @param y y coord
     * @param filledTiles all tiles which have been filled
     * @param checkedTiles all tiles which have been checked, regardless of being filled or not
     */
    private void floodFill(int x, int y, List<Location> filledTiles, List<String> checkedTiles) {
        Location loc = new Location(x, y);

        if (!checkedTiles.contains(loc.toString())) {
            checkedTiles.add(loc.toString());
            if (!RegionManager.isClipped(loc)) {
                filledTiles.add(loc);

                floodFill(x + 1, y, filledTiles, checkedTiles);
                floodFill(x - 1, y, filledTiles, checkedTiles);
                floodFill(x, y + 1, filledTiles, checkedTiles);
                floodFill(x, y - 1, filledTiles, checkedTiles);
            }
        }
    }

    /**
     * Gets the closest location to the entityfrom a location list.
     *
     * @param entity    The entity to get the location for.
     * @param locations The locations to check.
     * @return The closest location.
     */
    public static Location getClosestLocation(Entity entity, ArrayList<Location> locations) {
        if (locations.isEmpty()) {
            return null;
        }
        Location closest = locations.get(0);
        for (Location location : locations) {
            if (getDistance(entity.getLocation(), location) <= getDistance(entity.getLocation(), closest)) {
                closest = location;
            }
        }
        return closest;
    }

    /**
     * Gets the closest location to the given location from a location list.
     *
     * @param start     The location.
     * @param locations The locations to check.
     * @return The closest location.
     */
    public static Location getClosestLocation(Location start, ArrayList<Location> locations) {
        if (locations.isEmpty()) {
            return null;
        }
        Location closest = locations.get(0);
        for (Location location : locations) {
            if (getDistance(start, location) <= getDistance(start, closest)) {
                closest = location;
            }
        }
        return closest;
    }

    /**
     * Gets the x position on the region chunk.
     *
     * @return The x position on the region chunk.
     */
    public int getChunkOffsetX() {
        int x = getSceneX();
        return x - ((x / RegionChunk.SIZE) * RegionChunk.SIZE);
    }

    /**
     * Gets the y position on the region chunk.
     *
     * @return The y position on the region chunk.
     */
    public int getChunkOffsetY() {
        int y = getSceneY();
        return y - ((y / RegionChunk.SIZE) * RegionChunk.SIZE);
    }

    /**
     * Gets the base location for the chunk this location is in.
     *
     * @return The base location.
     */
    public Location getChunkBase() {
        return create((getRegionX() + 6) << 3, (getRegionY() + 6) << 3, z);
    }

    /**
     * Gets the region x-coordinate.
     *
     * @return The region x-coordinate.
     */
    public int getRegionX() {
        return (x >> 3) - 6;
    }

    /**
     * Gets the region y-coordinate.
     *
     * @return The region y-coordinate.
     */
    public int getRegionY() {
        return (y >> 3) - 6;
    }

    /**
     * Gets the local x coordinate relative to this region.
     *
     * @return The local x coordinate relative to this region.
     */
    public int getLocalX() {
        return getLocalX(this);
    }

    /**
     * Gets the local y coordinate relative to this region.
     *
     * @return The local y coordinate relative to this region.
     */
    public int getLocalY() {
        return getLocalY(this);
    }

    /**
     * Gets the local x coordinate relative to a specific region.
     *
     * @param l The region the coordinate will be relative to.
     * @return The local x coordinate.
     */
    public int getLocalX(Location l) {
        return x - ((x >> 6) << 6);
    }

    /**
     * Gets the local y coordinate relative to a specific region.
     *
     * @param l The region the coordinate will be relative to.
     * @return The local y coordinate.
     */
    public int getLocalY(Location l) {
        return y - ((y >> 6) << 6);
    }

    /**
     * Gets the local x-coordinate.
     *
     * @return The local x-coordinate.
     */
    public int getSceneX() {
        return x - ((getRegionX()) << 3);
    }

    /**
     * Gets the local y-coordinate.
     *
     * @return The local y-coordinate.
     */
    public int getSceneY() {
        return y - ((getRegionY()) << 3);
    }

    /**
     * Gets the local x-coordinate.
     *
     * @param loc The location containing the regional coordinates.
     * @return The local x-coordinate.
     */
    public int getSceneX(Location loc) {
        return x - ((loc.getRegionX()) << 3);
    }

    /**
     * Gets the local y-coordinate.
     *
     * @param loc The location containing the regional coordinates.
     * @return The local y-coordinate.
     */
    public int getSceneY(Location loc) {
        return y - ((loc.getRegionY()) << 3);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Location)) {
            return false;
        }
        Location loc = (Location) other;
        return loc.x == x && loc.y == y && loc.z == z;
    }

    /**
     * Checks if these coordinates equal this location.
     *
     * @param x the x.
     * @param y the y.
     * @param z the x.
     * @return <code>True</code> if so.
     */
    public boolean equals(int x, int y, int z) {
        return equals(new Location(x, y, z));
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }

    @Override
    public int hashCode() {
        return z << 30 | x << 15 | y;
    }

    /**
     * Gets the x.
     *
     * @return The x.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x.
     *
     * @param x The x to set.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y.
     *
     * @return The y.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y.
     *
     * @param y The y to set.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the z.
     *
     * @return The z.
     */
    public int getZ() {
        return z % 4;
    }

    /**
     * Gets a random location with the randomizer.
     *
     * @return The location.
     */
    public Location randomize() {
        return getRandomLocation(this, randomizer, true);
    }

    /**
     * Gets the randomizer.
     *
     * @return The randomizer.
     */
    public int getRandomizer() {
        return randomizer;
    }

    /**
     * Sets the randomizer.
     *
     * @param randomizer The randomizer.
     */
    public Location setRandomizer(int randomizer) {
        this.randomizer = randomizer;
        return this;
    }

    /**
     * Sets the z.
     *
     * @param z The z to set.
     */
    public void setZ(int z) {
        this.z = z;
    }


    public Location getNorth(int distance) {
        return transform(0, distance, 0);
    }

    public Location getSouth(int distance) {
        return transform(0, -distance, 0);
    }

    public Location getEast(int distance) {
        return transform(distance, 0, 0);
    }

    public Location getWest(int distance) {
        return transform(-distance, 0, 0);
    }

    public Location getNorth() {
        return transform(0, 1, 0);
    }

    public Location getSouth() {
        return transform(0, -1, 0);
    }

    public Location getEast() {
        return transform(1, 0, 0);
    }

    public Location getWest() {
        return transform(-1, 0, 0);
    }

    public Location getNorthEast() {
        return transform(1, 1, 0);
    }

    public Location getNorthEast(int distance) {
        return transform(distance, distance, 0);
    }

    public Location getSouthEast(int distance) {
        return transform(distance, -distance, 0);
    }

    public Location getUp() {
        return transform(0, 0, 1);
    }

    public Location getDown() {
        if (z == 0) {
            return this;
        }
        return transform(0, 0, -1);
    }

    public Location getNorthWest() {
        return transform(-1, 1, 0);
    }

    public Location getNorthWest(int distance) {
        return transform(-distance, distance, 0);
    }

    public Location getSouthWest(int distance) {
        return transform(-distance, -distance, 0);
    }

    public abstract class Area {

        /**
         * Returns whether or not all of the defined tiles (bottom left corner defined by location,
         * length and width by offset) are within this area.
         *
         * @param location The bottom left corner of the area to check
         * @param offset   The area length and width
         * @return true if the square area is entirely within the specified area.
         */
        public boolean allWithinArea(Location location, int offset, int padding) {
            for (int xOffset = 0; xOffset < offset; xOffset++) {
                for (int yOffset = 0; yOffset < offset; yOffset++) {

                    /* Check if the location at the new x and y offset is within the area */
                    if (!withinArea(location.getX() + xOffset, location.getY() + yOffset, padding)) {
                        return false;
                    }
                }
            }
            return true;
        }

        /**
         * Returns whether or not any of the defined tiles (bottom left corner defined by location,
         * length and width by offset) are within this area.
         *
         * @param location The bottom left corner of the area to check
         * @param offset   The area length and width
         * @return true if the square area is at least partially within the specified area.
         */
        public boolean anyWithinArea(Location location, int offset, int padding) {
            for (int xOffset = 0; xOffset < offset; xOffset++) {
                for (int yOffset = 0; yOffset < offset; yOffset++) {

                    /* Check if the location at the new x and y offset is within the area */
                    if (withinArea(location.getX() + xOffset, location.getY() + yOffset, padding)) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Gets if a certain coordinate is within the area.
         *
         * @param x       The x coordinate.
         * @param y       The y coordinate.
         * @param padding The padding around the area to also include in calculation
         * @return true if the coordinate is within the area
         */
        public abstract boolean withinArea(int x, int y, int padding);

        public abstract Location center();

        public abstract Location randomLocation(int height);
    }

    public class QuadArea extends Area {

        private final int minX, minY, maxX, maxY;

        public QuadArea(Location left, Location right) {
            this(left.getX(), left.getY(), right.getX(), right.getY());
        }

        public QuadArea(int minX, int minY, int maxX, int maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }

        @Override
        public boolean withinArea(int x, int y, int padding) {
            return x >= (minX - padding) && x <= (maxX + padding) && y >= (minY - padding) && y <= (maxY + padding);
        }

        @Override
        public Location center() {
            return new Location(minX + ((maxX - minX) / 2), minY + ((maxY - minY) / 2));
        }

        @Override
        public Location randomLocation(int height) {
            int x = minX + (int) (Math.random() * (maxX - minX + 1));
            int y = minY + (int) (Math.random() * (maxY - minY + 1));
            return new Location(x, y, height);
        }

        @Override
        public String toString() {
            return "QuadArea[(" + minX + ", " + minY + "), (" + maxX + ", " + maxY + ")]";
        }

    }

}
