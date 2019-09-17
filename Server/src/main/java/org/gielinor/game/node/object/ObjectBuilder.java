package org.gielinor.game.node.object;

import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.build.LandscapeParser;
import org.gielinor.game.world.update.flag.chunk.ObjectUpdateFlag;
import org.gielinor.rs2.pulse.Pulse;

import java.util.stream.Stream;

/**
 * An aiding class for object constructing/removing.
 *
 * @author Emperor
 */
public final class ObjectBuilder {

    /**
     * Replaces a game object.
     *
     * @param remove    The object to remove.
     * @param construct The object to add.
     * @return <code>True</code> if successful.
     */
    public static boolean replace(GameObject remove, GameObject construct) {
        return replace(remove, construct, true);
    }

    /**
     * Replaces a game object.
     *
     * @param remove    The object to remove.
     * @param construct The object to add.
     * @param clip      If clipping should be adjusted.
     * @return <code>True</code> if successful.
     */
    public static boolean replace(GameObject remove, GameObject construct, boolean clip) {
        if (!clip) {
            return replaceClientSide(remove, construct, -1);
        }
        remove = remove.getWrapper();
        GameObject current = LandscapeParser.removeGameObject(remove);
        if (current == null) {
            System.err.println("Object could not be replaced - object to remove is invalid.");
            return false;
        }
        if (current.getRestorePulse() != null) {
            current.getRestorePulse().stop();
            current.setRestorePulse(null);
        }
        if (current instanceof Constructed) {
            GameObject previous = ((Constructed) current).getReplaced();
            if (previous != null && previous.equals(construct)) {
                LandscapeParser.addGameObject(previous);
                update(current, previous);
                return true;
            }
        }
        Constructed constructed = construct.asConstructed();
        constructed.setReplaced(current);
        LandscapeParser.addGameObject(constructed);
        update(current, constructed);
        return true;
    }

    /**
     * Replaces the object client sided alone.
     *
     * @param remove       The object to remove.
     * @param construct    The object to replace with.
     * @param restoreTicks The restoration ticks.
     * @return <code>True</code> if successful.
     */
    private static boolean replaceClientSide(final GameObject remove, final GameObject construct, int restoreTicks) {
        RegionManager.getRegionChunk(remove.getLocation()).flag(new ObjectUpdateFlag(remove, true));
        RegionManager.getRegionChunk(construct.getLocation()).flag(new ObjectUpdateFlag(construct, false));
        if (restoreTicks > 0) {
            World.submit(new Pulse(restoreTicks) {

                @Override
                public boolean pulse() {
                    replaceClientSide(construct, remove, -1);
                    return true;
                }
            });
        }
        return true;
    }

    /**
     * Replaces a game object temporarily.
     *
     * @param remove       The object to remove.
     * @param construct    The object to add.
     * @param restoreTicks The amount of ticks before the object gets restored.
     * @return <code>True</code> if successful.
     */
    public static boolean replace(GameObject remove, GameObject construct, int restoreTicks) {
        return replace(remove, construct, restoreTicks, true);
    }

    /**
     * Replaces a game object temporarily.
     *
     * @param remove       The object to remove.
     * @param construct    The object to add.
     * @param restoreTicks The amount of ticks before the object gets restored.
     * @return <code>True</code> if successful.
     */
    public static boolean replace(GameObject remove, GameObject construct, int restoreTicks, final boolean clip) {
        if (!clip) {
            return replaceClientSide(remove, construct, restoreTicks);
        }
        remove = remove.getWrapper();
        GameObject current = LandscapeParser.removeGameObject(remove);
        if (current == null) {
            System.err.println("Object could not be replaced - object to remove is invalid.");
            return false;
        }
        if (current.getRestorePulse() != null) {
            current.getRestorePulse().stop();
            current.setRestorePulse(null);
        }
        if (current instanceof Constructed) {
            GameObject previous = ((Constructed) current).getReplaced();
            if (previous != null && previous.equals(construct)) {
                //Shouldn't happen.
                throw new IllegalStateException("Can't temporarily replace an already temporary object!");
            }
        }
        final Constructed constructed = construct.asConstructed();
        constructed.setReplaced(current);
        LandscapeParser.addGameObject(constructed);
        update(current, constructed);
        if (restoreTicks < 0) {
            return true;
        }
        constructed.setRestorePulse(new Pulse(restoreTicks) {

            @Override
            public boolean pulse() {
                replace(constructed, constructed.getReplaced());
                return true;
            }
        });
        World.submit(constructed.getRestorePulse());
        return true;
    }


    /**
     * Adds a game object.
     *
     * @param object The object to add.
     * @return <code>True</code> if successful.
     */
    public static Constructed add(GameObject object) {
        return add(object, -1);
    }

    /**
     * Adds an array of {@link GameObject}s.
     *
     * @param gameObjects The objects to add.
     *
     * @return a {@link Constructed} array with its length equal to the input's length.
     */
    public static Constructed[] addAll(GameObject... gameObjects) {
        return Stream.of(gameObjects).toArray(Constructed[]::new);
    }

    /**
     * Adds a game object.
     *
     * @param object The object to add.
     * @param ticks  The amount of ticks this object should last for (-1 for permanent).
     * @return <code>True</code> if successful.
     */
    public static Constructed add(GameObject object, int ticks, final GroundItem... items) {
        object = object.getWrapper();
        final Constructed constructed = object.asConstructed();
        LandscapeParser.addGameObject(constructed);
        update(constructed);
        if (ticks > -1) {
            World.submit(new Pulse(ticks, object) {

                @Override
                public boolean pulse() {
                    remove(constructed);
                    if (items != null) {
                        for (GroundItem item : items) {
                            GroundItemManager.create(item);
                        }
                    }
                    return true;
                }
            });
        }
        return constructed;
    }

    /**
     * Removes a game object.
     *
     * @param object The object to remove.
     * @return <code>True</code> if successful.
     */
    public static boolean remove(GameObject object) {
        object = object.getWrapper();
        GameObject current = LandscapeParser.removeGameObject(object);
        if (current == null) {
            return false;
        }
        update(current);
        return true;
    }

    /**
     * Updates the game object on all the player's screen.
     *
     * @param objects The game objects.
     */
    public static void update(GameObject... objects) {
        for (GameObject o : objects) {
            if (o == null) {
                continue;
            }
            if (o.isHidden()) {
                continue;
            }
            RegionManager.getRegionChunk(o.getLocation()).flag(new ObjectUpdateFlag(o, !o.isActive()));
        }
    }
}
