package org.gielinor.game.world.map.path;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.Point;
import org.gielinor.game.world.map.RegionManager;

/**
 * Used for checking if a projectile can travel from the start location to the end location.
 * @author Emperor
 *
 */
public final class ProjectilePathfinder extends Pathfinder {

    @Override
    public Path find(Location start, int size, Location end, int sizeX, int sizeY, int rotation, int type, int walkingFlag, boolean near) {
        Path path = new Path();
        int z = start.getZ();
        int x = start.getX();
        int y = start.getY();
        List<Point> points = new ArrayList<>();
        path.setSuccessful(true);
        while (x != end.getX() || y != end.getY()) {
            Direction[] directions = getDirection(x, y, end);
            boolean found = true;
            for (Direction dir : directions) {
                switch (dir) {
                    case NORTH:
                        for (int i = 0; i < size; i++) {
                            if ((RegionManager.getProjectileFlag(z, x + i, y + size) & 0x12c0120) != 0) {
                                found = false;
                            }
                        }
                        if (found) {
                            points.add(new Point(x, ++y, dir));
                        }
                        break;
                    case NORTH_EAST:
                        for (int i = 0; i < size; i++) {
                            if ((RegionManager.getProjectileFlag(z, x + size, y + i) & 0x12c0180) != 0
                                || (RegionManager.getProjectileFlag(z, x + i, y + size) & 0x12c0120) != 0) {
                                found = false;
                            }
                        }
                        if ((RegionManager.getProjectileFlag(z, x + size, y + size) & 0x12c01e0) != 0) {
                            found = false;
                        }
                        if (found) {
                            points.add(new Point(++x, ++y, dir));
                        }
                        break;
                    case EAST:
                        for (int i = 0; i < size; i++) {
                            if ((RegionManager.getProjectileFlag(z, x + size, y + i) & 0x12c0180) != 0) {
                                found = false;
                            }
                        }
                        if (found) {
                            points.add(new Point(++x, y, dir));
                        }
                        break;
                    case SOUTH_EAST:
                        for (int i = 0; i < size; i++) {
                            if ((RegionManager.getProjectileFlag(z, x + size, y + i) & 0x12c0180) != 0
                                || (RegionManager.getProjectileFlag(z, x + i, y - 1) & 0x12c0102) != 0) {
                                found = false;
                            }
                        }
                        if ((RegionManager.getProjectileFlag(z, x + size, y - 1) & 0x12c0183) != 0) {
                            found = false;
                        }
                        if (found) {
                            points.add(new Point(++x, --y, dir));
                        }
                        break;
                    case SOUTH:
                        for (int i = 0; i < size; i++) {
                            if ((RegionManager.getProjectileFlag(z, x + i, y - 1) & 0x12c0102) != 0) {
                                found = false;
                            }
                        }
                        if (found) {
                            points.add(new Point(x, --y, dir));
                        }
                        break;
                    case SOUTH_WEST:
                        for (int i = 0; i < size; i++) {
                            if ((RegionManager.getProjectileFlag(z, x - 1, y + i) & 0x12c0108) != 0
                                || (RegionManager.getProjectileFlag(z, x + i, y - 1) & 0x12c0102) != 0) {
                                found = false;
                            }
                        }
                        if ((RegionManager.getProjectileFlag(z, x - 1, y - 1) & 0x12c010e) != 0) {
                            found = false;
                        }
                        if (found) {
                            points.add(new Point(--x, --y, dir));
                        }
                        break;
                    case WEST:
                        for (int i = 0; i < size; i++) {
                            if ((RegionManager.getProjectileFlag(z, x - 1, y + i) & 0x12c0108) != 0) {
                                found = false;
                            }
                        }
                        if (found) {
                            points.add(new Point(--x, y, dir));
                        }
                        break;
                    case NORTH_WEST:
                        for (int i = 0; i < size; i++) {
                            if ((RegionManager.getProjectileFlag(z, x - 1, y + i) & 0x12c0108) != 0
                                || (RegionManager.getProjectileFlag(z, x + i, y + size) & 0x12c0120) != 0) {
                                found = false;
                            }
                        }
                        if ((RegionManager.getProjectileFlag(z, x - 1, y + size) & 0x12c0138) != 0) {
                            found = false;
                        }
                        if (found) {
                            points.add(new Point(--x, ++y, dir));
                        }
                        break;
                }
                if (found) {
                    break;
                }
            }
            if (!found) {
                path.setMoveNear(x != start.getX() || y != start.getY());
                path.setSuccessful(false);
                break;
            }
        }
        if (!points.isEmpty()) {
            Direction last = null;
            for (int i = 0; i < points.size() - 1; i++) {
                Point p = points.get(i);
                if (p.getDirection() != last) {
                    path.getPoints().add(p);
                }
            }
            path.getPoints().add(points.get(points.size() - 1));
        }
        return path;
    }

    /**
     * Gets the direction.
     * @param start The start direction.
     * @param end The end direction.
     * @return The direction.
     */
    private static Direction[] getDirection(int startX, int startY, Location end) {
        int endX = end.getX();
        int endY = end.getY();
        if (startX == endX) {
            if (startY > endY) {
                return new Direction[]{ Direction.SOUTH };
            } else if (startY < endY) {
                return new Direction[]{ Direction.NORTH };
            }
        } else if (startY == endY) {
            if (startX > endX) {
                return new Direction[]{ Direction.WEST };
            }
            return new Direction[]{ Direction.EAST };
        } else {
            if (startX < endX && startY < endY) {
                return new Direction[]{ Direction.NORTH_EAST, Direction.EAST, Direction.NORTH };
            } else if (startX < endX && startY > endY) {
                return new Direction[]{ Direction.SOUTH_EAST, Direction.EAST, Direction.SOUTH };
            } else if (startX > endX && startY < endY) {
                return new Direction[]{ Direction.NORTH_WEST, Direction.WEST, Direction.NORTH };
            } else if (startX > endX && startY > endY) {
                return new Direction[]{ Direction.SOUTH_WEST, Direction.WEST, Direction.SOUTH };
            }
        }
        return new Direction[0];
    }

}
