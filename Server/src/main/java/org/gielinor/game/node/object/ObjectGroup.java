package org.gielinor.game.node.object;

/**
 * Represents a group for a {@link GameObject}.
 *
 * @author Hadyn Richard
 */
public enum ObjectGroup {

    /**
     * Enumeration for each group type.
     */
    WALL(0),
    WALL_DECORATION(1),
    GROUP_2(2),
    GROUP_3(3);

    /**
     * The array of object group ids for object type.
     */
    public static final int[] OBJECT_GROUPS = new int[]{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 };

    /**
     * The id of the group.
     */
    private final int id;

    ObjectGroup(int id) {
        this.id = id;
    }

    public static ObjectGroup forType(int type) {
        int id = OBJECT_GROUPS[type];
        for (ObjectGroup group : values()) {
            if (group.id == id) {
                return group;
            }
        }
        throw new RuntimeException();
    }

    public int getId() {
        return id;
    }

}
