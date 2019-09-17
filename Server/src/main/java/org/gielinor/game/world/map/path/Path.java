package org.gielinor.game.world.map.path;

import java.util.ArrayDeque;
import java.util.Deque;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.world.map.Point;

/**
 * Represents a path.
 * @author Emperor
 *
 */
public class Path {

    /**
     * If the path was found.
     */
    private boolean successful;

    /**
     * If we have to move near the destination (as we can't reach it).
     */
    private boolean moveNear;

    /**
     * The points to walk.
     */
    private Deque<Point> points = new ArrayDeque<Point>();

    /**
     * Constructs a new {@code Path} {@code Object}.
     */
    public Path() {
        /*
         * empty.
         */
    }

    /**
     * Walks this path.
     * @param entity The entity.
     */
    public void walk(Entity entity) {
        if (entity.getLocks().isMovementLocked()) {
            return;
        }
        entity.getWalkingQueue().reset();
        for (Point step : points) {
            entity.getWalkingQueue().addPath(step.getX(), step.getY());
        }
    }

    /**
     * Gets the successful.
     * @return The successful.
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Sets the successful.
     * @param successful The successful to set.
     */
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    /**
     * Gets the points.
     * @return The points.
     */
    public Deque<Point> getPoints() {
        return points;
    }

    /**
     * Sets the points.
     * @param points The points to set.
     */
    public void setPoints(Deque<Point> points) {
        this.points = points;
    }

    /**
     * Gets the moveNear.
     * @return The moveNear.
     */
    public boolean isMoveNear() {
        return moveNear;
    }

    /**
     * Sets the moveNear.
     * @param moveNear The moveNear to set.
     */
    public void setMoveNear(boolean moveNear) {
        this.moveNear = moveNear;
    }
}
