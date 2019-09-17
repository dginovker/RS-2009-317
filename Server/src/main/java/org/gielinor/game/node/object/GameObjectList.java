package org.gielinor.game.node.object;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.gielinor.game.world.map.Location;

/**
 * @author Hadyn Richard
 * @author David Insley
 */
public final class GameObjectList {

    /**
     * The UID counter for all the ground objects that are created.
     */
    private static final AtomicInteger counter = new AtomicInteger(1);

    /**
     * The mapping for all the tiles in this list.
     */
    private final Map<Location, Tile> tiles = new LinkedHashMap<>();

    /**
     * The list of objects that have been updated.
     */
    private final Set<GameObject> updatedObjects = new HashSet<>();

    /**
     * The flag for if objects that were updated should be listed.
     */
    private boolean recordUpdates = true;

    /**
     * The representation of a tile in the game world that contains ground objects.
     */
    private class Tile {

        /**
         * The mapping for the objects that exist on the tile.
         */
        private Map<ObjectGroup, GameObject> objects = new LinkedHashMap<>();

        /**
         * Constructs a new {@link Tile};
         */
        public Tile() {
        }

        public boolean put(ObjectGroup group, GameObject object, boolean override) {
            /* Cannot contain multiple of the same group of object on a tile */
            if (!override && objects.containsKey(group)) {
                return false;
            }

            /* Put the object into the mapping */
            // TODO Uid for objects
            //object.setUid(counter.incrementAndGet());
            objects.put(group, object);

            ObjectBuilder.add(object);

            if (recordUpdates) {

                /* Add the object to the updated objects set */
                updatedObjects.add(object);
            }
            return true;
        }

        public GameObject get(int objectId) {
            for (Entry<ObjectGroup, GameObject> entry : objects.entrySet()) {
                GameObject object = entry.getValue();
                if (object.getId() == objectId) {
                    return object;
                }
            }
            return null;
        }

        public GameObject get(ObjectGroup group) {
            return objects.get(group);
        }

        public boolean isEmpty() {
            for (Entry<ObjectGroup, GameObject> entry : objects.entrySet()) {
                GameObject object = entry.getValue();
                if (object.getId() >= 0) {
                    return false;
                }
            }
            return true;
        }

        public boolean contains(int objectId) {
            for (Entry<ObjectGroup, GameObject> entry : objects.entrySet()) {
                GameObject object = entry.getValue();
                if (object.getId() == objectId) {
                    return true;
                }
            }
            return false;
        }

        public void remove(ObjectGroup group) {
            GameObject object = objects.remove(group);
            if (object != null) {

                ObjectBuilder.remove(object);

                if (recordUpdates) {

                    /* Remove the object from the updated objects set */
                    updatedObjects.remove(object);
                }
            }
        }

        public List<GameObject> getObjects() {
            return new LinkedList<GameObject>(objects.values());
        }
    }

    /**
     * Sets if the list will record update objects.
     *
     * @param recordUpdates The flag.
     */
    public void setRecordUpdates(boolean recordUpdates) {
        this.recordUpdates = recordUpdates;
    }


    public GameObject put(Location location, int objectId, int rotation, ObjectType type) {
        // TODO -1 =  ObjectDefinition.forId(objectId).getAnimationId()
        return put(location, objectId, -1, rotation, type);
    }

    public GameObject put(Location location, int objectId, int rotation, ObjectType type, boolean override) {
        // TODO -1 = ObjectDefinition.forId(objectId).getAnimationId()
        return put(location, objectId, -1, rotation, type, override);
    }

    public GameObject put(Location location, int objectId, int animationId, int rotation, ObjectType type) {
        return put(location, objectId, animationId, rotation, type, false);
    }

    public GameObject put(Location location, int objectId, int animationId, int rotation, ObjectType type, boolean override) {
        GameObject object = new GameObject(objectId, location, type.getId(), rotation);
        Tile tile = tiles.get(location);
        if (tile == null) {
            tile = new Tile();
            tiles.put(location, tile);
        }
        return tile.put(ObjectType.forId(object.getType()).getObjectGroup(), object, override) ? object : null;
    }

    public List<GameObject> getAll(Location location) {
        Tile tile = tiles.get(location);
        if (tile == null) {
            return null;
        }
        return tile.getObjects();
    }

    public GameObject get(int objectId, Location location) {
        Tile tile = tiles.get(location);
        if (tile == null) {
            return null;
        }
        return tile.get(objectId);
    }

    public GameObject get(ObjectGroup group, Location location) {
        Tile tile = tiles.get(location);
        if (tile == null) {
            return null;
        }
        return tile.get(group);
    }

    public boolean contains(int objectId, Location location) {
        Tile tile = tiles.get(location);
        if (tile == null) {
            return false;
        }
        return tile.contains(objectId);
    }

    public boolean isEmpty(Location location) {
        Tile tile = tiles.get(location);
        if (tile == null) {
            return true;
        }
        return tile.isEmpty();
    }

    public void remove(Location location, ObjectGroup group) {
        Tile tile = tiles.get(location);
        if (tile == null) {
            return;
        }
        tile.remove(group);
    }
}
