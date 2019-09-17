package org.gielinor.game.content.global.action;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles a ladder climbing action.
 *
 * @author Emperor
 */
public final class ClimbActionHandler {

    /**
     * Represents the climb up animation of ladders.
     */
    public static final Animation CLIMB_UP = new Animation(828);

    /**
     * Represents the climb down animation of ladders.
     */
    public static final Animation CLIMB_DOWN = new Animation(827);

    /**
     * The climb dialogue.
     */
    public static DialoguePlugin CLIMB_DIALOGUE = new ClimbDialogue();

    /**
     * Handles the climbing of a rope.
     *
     * @param player The player.
     * @param object The rope object.
     * @param option The option.
     */
    public static void climbRope(Player player, GameObject object, String option) {

    }

    /**
     * Handles the climbing of a trap door.
     *
     * @param player The player.
     * @param object The trap door object.
     * @param option The option.
     */
    public static void climbTrapdoor(Player player, GameObject object, String option) {

    }

    /**
     * Handles the climbing of a ladder.
     *
     * @param player The player.
     * @param object The game object.
     * @param option The option.
     */
    public static void climbLadder(Player player, GameObject object, String option) {
        GameObject newLadder = null;
        Animation animation = CLIMB_UP;
        switch (option) {
            case "climb-up":
            case "climb up":
                newLadder = getLadder(object, false);
                break;
            case "climb-down":
            case "climb down":
                if (object.getName().equals("Trapdoor")) {
                    animation = CLIMB_DOWN;
                }
                newLadder = getLadder(object, true);
                break;
            case "climb":
                GameObject upperLadder = getLadder(object, false);
                GameObject downLadder = getLadder(object, true);
                if (upperLadder == null && downLadder != null) {
                    climbLadder(player, object, "climb-down");
                    return;
                }
                if (upperLadder != null && downLadder == null) {
                    climbLadder(player, object, "climb-up");
                    return;
                }
                DialoguePlugin dial = CLIMB_DIALOGUE.newInstance(player);
                if (dial != null && dial.open(object)) {
                    player.getDialogueInterpreter().setDialogue(dial);
                }
                return;
        }
        Location destination = newLadder != null ? getDestination(newLadder) : null;
        if (newLadder == null || destination == null) {
            String s = (object.getName().toLowerCase().startsWith("stairs") || object.getName().toLowerCase().startsWith("staircase")) ? "These stairs don't" :
                object.getName().toLowerCase().startsWith("ladder") ? "This ladder doesn't" :
                    object.getName().toLowerCase().startsWith("trapdoor") ? "The trapdoor doesn't" : "This ladder doesn't";
            player.getActionSender().sendMessage(s + " seem to lead anywhere.");
            return;
        }
        if (object.getName().startsWith("Stair")) {
            animation = null;
        }
        climb(player, animation, destination);
    }

    /**
     * Gets the teleport destination.
     *
     * @param gameObject The object to teleport to.
     * @return The teleport destination.
     */
    public static Location getDestination(GameObject gameObject) {
        int sizeX = gameObject.getDefinition().sizeX;
        int sizeY = gameObject.getDefinition().sizeY;
        if (gameObject.getRotation() % 2 != 0) {
            int switcher = sizeX;
            sizeX = sizeY;
            sizeY = switcher;
        }
        Direction dir = Direction.forWalkFlag(gameObject.getDefinition().getWalkingFlag(), gameObject.getRotation());
        if (dir != null) {
            return getDestination(gameObject, sizeX, sizeY, dir, 0);
        }
        switch (gameObject.getRotation()) {
            case 0:
                return getDestination(gameObject, sizeX, sizeY, Direction.SOUTH, 0);
            case 1:
                return getDestination(gameObject, sizeX, sizeY, Direction.EAST, 0);
            case 2:
                return getDestination(gameObject, sizeX, sizeY, Direction.NORTH, 0);
            case 3:
                return getDestination(gameObject, sizeX, sizeY, Direction.WEST, 0);
        }
        return null;
    }

    /**
     * Gets the destination for the given object.
     *
     * @param gameObject The object.
     * @param dir    The preferred direction from the object.
     * @return The teleporting destination.
     */
    private static Location getDestination(GameObject gameObject, int sizeX, int sizeY, Direction dir, int count) {
        Location loc = gameObject.getLocation();
        if (dir.toInteger() % 2 != 0) {
            int x = dir.getStepX();
            if (x > 0) {
                x *= sizeX;
            }
            for (int y = 0; y < sizeY; y++) {
                Location l = loc.transform(x, y, 0);
                if (RegionManager.isTeleportPermitted(l) && dir.canMove(l)) {
                    return l;
                }
            }
        } else {
            int y = dir.getStepY();
            if (y > 0) {
                y *= sizeY;
            }
            for (int x = 0; x < sizeX; x++) {
                Location l = loc.transform(x, y, 0);
                if (RegionManager.isTeleportPermitted(l) && dir.canMove(l)) {
                    return l;
                }
            }
        }
        if (count == 3) {
            return null;
        }
        return getDestination(gameObject, sizeX, sizeY, Direction.get((dir.toInteger() + 1) % 4), count + 1);
    }

    /**
     * Executes the climbing action.
     *
     * @param player      The player.
     * @param animation   The climbing animation.
     * @param destination The destination.
     * @param messages    Messages to send after the player climbs.
     */
    public static void climb(final Player player, Animation animation, Location destination, String... messages) {
        player.getActionSender().sendDebugPacket(-1, "Ladder Destination", "Location: " +
            (destination == null ? "null" : destination.toString()));
        if (player.getLocation().withinDistance(new Location(3019, 9739, 0))) {
            destination = new Location(3017, 3339, 0);
        }
        /**
         * Lumbridge court top ladder.
         */
        if (destination != null) {
            if (destination.equals(Location.create(3229, 3225, 2)) && player.getLocation().getZ() == 2) {
                destination = destination.transform(0, 0, -1);
            }
            /**
             * Lumbridge court top ladder.
             */
            if (destination.equals(Location.create(3229, 3214, 2)) && player.getLocation().getZ() == 2) {
                destination = destination.transform(0, 0, -1);
            }
            /**
             * Lumbridge court second ladder.
             */
            if (destination.equals(Location.create(3229, 3225, 1)) && player.getLocation().getZ() == 1) {
                destination = destination.transform(0, 0, -1);
            }
            /**
             * Lumbridge court second ladder.
             */
            if (destination.equals(Location.create(3229, 3214, 1)) && player.getLocation().getZ() == 1) {
                destination = destination.transform(0, 0, -1);
            }
            /**
             * Lumbridge flour mill top ladder.
             */
            if (destination.equals(Location.create(3164, 3306, 2)) && player.getLocation().getZ() == 2) {
                destination = destination.transform(0, 0, -1);
            }
            /**
             * Lumbridge flour mill second ladder.
             */
            if (destination.equals(Location.create(3164, 3306, 1)) && player.getLocation().getZ() == 1) {
                destination = destination.transform(0, 0, -1);
            }
            /**
             * Taverley dungeon enter ladder.
             */
            if (destination.equals(Location.create(2885, 3397, 0)) && player.getLocation().getY() == 3397) {
                destination = destination.transform(-1, 6401, 0);
            }
            /**
             * Barbarian outpost course ladder.
             */
            if (destination.equals(Location.create(2547, 3550, 0)) && player.getLocation().getY() == 3550) {
                destination = destination.transform(1, 6401, 0);
            }
            /**
             * Barbarian outpost course ladder.
             */
            if (destination.equals(Location.create(2532, 3546, 1))) {
                destination = destination.transform(0, 0, -1);
            }
            /**
             * Varrock brass key shed.
             */
            if (destination.equals(Location.create(3115, 3452, 0)) && player.getLocation().getZ() == 0) {
                destination = destination.transform(0, 6400, 0);
            }
            /**
             * Heroes' Guild ladder.
             */
            if (destination.equals(Location.create(2892, 3508, 0)) && player.getLocation().getY() == 3508) {
                destination = destination.transform(0, 6400, 0);
            }
            /**
             * Falador mine stairs.
             */
            if (destination.equals(Location.create(3061, 3376, 0)) && (player.getLocation().getY() == 3376 || player.getLocation().getY() == 3377)) {
                destination = destination.transform(-3, 6400, 0);
            }
            /**
             * Barbarian Assault ladder. Down
             */
            if (player.getLocation().getX() == 2533 && player.getLocation().getY() == 3572) {
                destination = Location.create(1877, 5457, 0);
            }
            /**
             * Barbarian Assault ladder. Up
             */
            if (player.getLocation().getX() == 1877 && player.getLocation().getY() == 5457) {
                destination = Location.create(2533, 3572, 0);
            }
        }
        player.lock(2);
        if (animation != null) {
            player.playAnimation(animation);
        }
        final Location finalDestination = destination;
        World.submit(new Pulse(1) {

            @Override
            public boolean pulse() {
                player.setTeleportTarget(finalDestination);
                player.getActionSender().sendMessages(messages);
                return true;
            }
        });
    }

    /**
     * Gets the ladder the object leads to.
     *
     * @param gameObject The ladder object.
     * @param down   If the player is going down a floor.
     * @return The ladder the current ladder object leads to.
     */
    private static GameObject getLadder(GameObject gameObject, boolean down) {
        int mod = down ? -1 : 1;
        GameObject ladder = RegionManager.getObject(gameObject.getLocation().transform(0, 0, mod));
        if (ladder == null || !isLadder(ladder)) {
            if (ladder != null && ladder.getName().equals(gameObject.getName())) {
                ladder = RegionManager.getObject(ladder.getLocation().transform(0, 0, mod));
                if (ladder != null) {
                    return ladder;
                }
            }
            ladder = findLadder(gameObject.getLocation().transform(0, 0, mod));
            if (ladder == null) {
                ladder = RegionManager.getObject(gameObject.getLocation().transform(0, mod * -6400, 0));
                if (ladder == null) {
                    ladder = findLadder(gameObject.getLocation().transform(0, mod * -6400, 0));
                }
            }
        }
        return ladder;
    }

    /**
     * Finds a ladder (by searching a 10x10 area around the given location).
     *
     * @param l The location.
     * @return The ladder.
     */
    private static GameObject findLadder(Location l) {
        for (int x = -5; x < 6; x++) {
            for (int y = -5; y < 6; y++) {
                GameObject object = RegionManager.getObject(l.transform(x, y, 0));
                if (object != null && isLadder(object)) {
                    return object;
                }
            }
        }
        return null;
    }

    /**
     * Checks if the object is a ladder.
     *
     * @param object The object.
     * @return <code>True</code> if so.
     */
    private static boolean isLadder(GameObject object) {
        for (String option : object.getDefinition().getOptions()) {
            if (option != null && (option.contains("Climb"))) {
                return true;
            }
        }
        return object.getName().equals("Trapdoor");
    }

    /**
     * Represents the dialogue plugin used for climbing stairs or a ladder.
     *
     * @author 'Vexia
     * @version 1.0
     */
    static final class ClimbDialogue extends DialoguePlugin {

        /**
         * Represents the climbing dialogue id.
         */
        public static final int ID = 8 << 16;

        /**
         * Constructs a new {@code ClimbDialogue} {@code Object}.
         */
        public ClimbDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code ClimbDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public ClimbDialogue(final Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new ClimbDialogue(player);
        }

        /**
         * Represents the object to use.
         */
        private GameObject object;

        @Override
        public boolean open(Object... args) {
            object = (GameObject) args[0];
            interpreter.sendOptions("What would you like to do?", "Climb Up.", "Climb Down.");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            player.lock(1);
                            World.submit(new Pulse(1) {

                                @Override
                                public boolean pulse() {
                                    climbLadder(player, object, "climb-up");
                                    return true;
                                }
                            });
                            end();
                            break;
                        case TWO_OPTION_TWO:
                            player.lock(1);
                            World.submit(new Pulse(1) {

                                @Override
                                public boolean pulse() {
                                    climbLadder(player, object, "climb-down");
                                    return true;
                                }
                            });
                            end();
                            break;

                    }
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ ID };
        }

    }
}
