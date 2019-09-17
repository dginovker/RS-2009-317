package plugin.skill.agility;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.agility.AgilityCourse;
import org.gielinor.game.content.skill.member.agility.AgilityHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.task.impl.LocationLogoutTask;
import org.gielinor.rs2.task.impl.LogoutTask;

/**
 * Handles the wilderness agility course.
 *
 * @author 'Vexia
 */
public final class WildernessCourse extends AgilityCourse {

    /**
     * The rope delay.
     */
    private static int ropeDelay;
    /**
     * The mark of grace spawn locations.
     */
    private static final Location[] GRACE_SPAWNS = new Location[]{
        Location.create(3008, 3951, 0),
        Location.create(3007, 3961, 0),
        Location.create(2991, 3961, 0),
        Location.create(2989, 3944, 0),
        Location.create(2989, 3933, 0)
    };

    /**
     * Constructs a new {@code WildernessCourse} {@code Object}.
     */
    public WildernessCourse() {
        this(null);
    }

    /**
     * Constructs a new {@code WildernessCourse} {@code Object}.
     *
     * @param player the player.
     */
    public WildernessCourse(Player player) {
        super(player, 5, 499);
    }

    /**
     * Creates a mark of grace drop.
     *
     * @param player The player.
     * @param index  The course index.
     */
    public static void dropMark(Player player, int index) {
        if (!AgilityHandler.spawnMarkOfGrace(player, "wild", 52)) {
            return;
        }
        GroundItem groundItem = new GroundItem(new Item(14729), GRACE_SPAWNS[index], 1000, player);
        groundItem.setRemainPrivate(true);
        GroundItemManager.create(groundItem);
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        getCourse(player); //Sets the extension.
        GameObject object = (GameObject) node;
        switch (object.getId()) {
            case 2309:
                handleEntrance(player, object);
                break;
            case 2307:
            case 2308:
                DoorActionHandler.handleAutowalkDoor(player, object);
                handleEntranceObstacle(player, object);
                break;
            case 23137:
                handlePipe(player, object);
                dropMark(player, 0);
                break;
            case 23132:
                handleRopeSwing(player, object);
                dropMark(player, 1);
                break;
            case 23556:
                handleSteppingStones(player, object);
                dropMark(player, 2);
                break;
            case 23542:
                handleLogBalance(player, object);
                dropMark(player, 3);
                break;
            case 23640:
                handleRockClimb(player, object);
                dropMark(player, 4);
                break;
        }
        return true;
    }

    /**
     * Handles the door entrance.
     *
     * @param player the player.
     * @param object the object.
     */
    private void handleEntrance(final Player player, final GameObject object) {
        if (player.getLocation().getY() > 3916 || player.getSkills().getLevel(Skills.AGILITY) >= 52) {
            DoorActionHandler.handleAutowalkDoor(player, object);
            if (player.getLocation().getY() <= 3916) {
                handleEntranceObstacle(player, object);
            }
        } else {
            player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of at least 52 to enter.");
        }
    }

    /**
     * Handles the entrance obstacle.
     *
     * @param player the player.
     * @param object the object.
     */
    private void handleEntranceObstacle(final Player player, final GameObject object) {
        World.submit(new Pulse(1, player) {

            int counter;
            final boolean fail = AgilityHandler.hasFailed(player, 1, 0.3);

            @Override
            public boolean pulse() {
                switch (++counter) {
                    case 2:
                        final Location end = fail ? Location.create(2998, 3924, 0) : object.getId() < 2309 ? Location.create(2998, 3917, 0) : Location.create(2998, 3930, 0);
                        final Location start = object.getId() < 2309 ? player.getLocation() : Location.create(2998, 3917, 0);
                        player.getActionSender().sendMessage("You go through the gate and try to edge over the ridge...");
                        AgilityHandler.walk(player, -1, start, end, Animation.create(762), fail ? 0.0 : 15.00, fail ? "You loose your footing and fail into the wolf pit." : "You skillfully balance across the ridge...");
                        break;
                    case 9:
                        if (fail) {
                            AgilityHandler.fail(player, 0, player.getLocation().transform(object.getId() < 2309 ? -2 : 2, 0, 0), Animation.create(object.getId() < 2309 ? 771 : 771), getHitAmount(player), null);
                        }
                        return fail;
                    case 15:
                        player.lock(3);
                        break;
                    case 16:
                        Location doorLoc = object.getId() < 2309 ? new Location(2998, 3917, 0) : new Location(2998, 3931, 0);
                        DoorActionHandler.handleAutowalkDoor(player, RegionManager.getObject(doorLoc));
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Handles the pipe obstacle.
     *
     * @param player the pipe.
     * @param object the object.
     */
    private void handlePipe(final Player player, final GameObject object) {
        if (object.getLocation().getY() == 3948) {
            player.getActionSender().sendMessage("You can't do that from here.");
            return;
        }
        if (player.getSkills().getLevel(Skills.AGILITY) < 49) {
            player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of at least 49 to do this.");
            return;
        }
        player.lock(12);
        World.submit(new Pulse(1, player) {

            @Override
            public boolean pulse() {
                int x = 3004;
                AgilityHandler.forceWalk(player, -1, Location.create(x, 3937, 0), Location.create(x, 3939, 0), Animation.create(749), 10, 0, null);
                AgilityHandler.forceWalk(player, -1, Location.create(x, 3939, 0), Location.create(x, 3948, 0), Animation.create(844), 10, 0, null, 1);
                AgilityHandler.forceWalk(player, 0, Location.create(x, 3948, 0), Location.create(x, 3950, 0), Animation.create(748), 20, 12.5, null, 14);
                player.addExtension(LogoutTask.class, new LocationLogoutTask(14, Location.create(3004, 3937, 0)));
                return true;
            }
        });
    }

    /**
     * Handles the rope swing obstacle.
     *
     * @param player the player.
     * @param object the object.
     */
    private void handleRopeSwing(final Player player, final GameObject object) {
        if (player.getLocation().getY() < 3554) {
            player.getActionSender().sendMessage("You cannot do that from here.");
            return;
        }
        if (ropeDelay > World.getTicks()) {
            player.getActionSender().sendMessage("The rope is being used.");
            return;
        }
        if (AgilityHandler.hasFailed(player, 1, 0.1)) {
            AgilityHandler.fail(player, 0, Location.create(3005, 10357, 0), null, getHitAmount(player), "You slip and fall to the pit bellow.");
            return;
        }
        ropeDelay = World.getTicks() + 2;
        player.getActionSender().sendObjectAnimation(object, Animation.create(497), true);
        AgilityHandler.forceWalk(player, 1, player.getLocation(), Location.create(3005, 3958, 0), Animation.create(751), 50, 20, "You skillfully swing across.", 1);
    }

    /**
     * Handles the stepping stone obstacle.
     *
     * @param player the player.
     * @param object the object.
     */
    private void handleSteppingStones(final Player player, final GameObject object) {
        final boolean fail = AgilityHandler.hasFailed(player, 1, 0.3);
        player.addExtension(LogoutTask.class, new LocationLogoutTask(12, player.getLocation()));
        World.submit(new Pulse(2, player) {

            int counter;

            @Override
            public boolean pulse() {
                if (counter == 3 && fail) {
                    AgilityHandler.fail(player, -1, Location.create(3001, 3963, 0), Animation.create(771), (int) (player.getSkills().getLifepoints() * 0.26), "...You lose your footing and fall into the lava.");
                    return true;
                }
                AgilityHandler.forceWalk(player, fail ? -1 : 2, player.getLocation(), player.getLocation().transform(-1, 0, 0), Animation.create(741), 10, counter == 5 ? 20 : 0, counter != 0 ? null : "You carefully start crossing the stepping stones...");
                return ++counter == 6;
            }
        });
    }

    /**
     * Handles the log balance obstacle.
     *
     * @param player the player.
     * @param object the object.
     */
    private void handleLogBalance(final Player player, final GameObject object) {
        final boolean failed = AgilityHandler.hasFailed(player, 1, 0.5);
        final Location end = failed ? Location.create(2998, 3945, 0) : Location.create(2994, 3945, 0);
        player.getActionSender().sendMessage("You walk carefully across the slippery log...", 1);
        AgilityHandler.walk(player, failed ? -1 : 1, player.getLocation(), end, Animation.create(762), failed ? 0.0 : 20, failed ? null : "You skillfully edge across the gap.");
        if (failed) {
            World.submit(new Pulse(5, player) {

                @Override
                public boolean pulse() {
                    player.faceLocation(Location.create(2998, 3944, 0));
                    AgilityHandler.fail(player, 3, Location.create(2998, 10345, 0), Animation.create(770), getHitAmount(player), "You slip and fall onto the spikes below.");
                    return true;
                }
            });
            return;
        }
    }

    /**
     * Handles the rock climbing obstacle.
     *
     * @param player the player.
     * @param object the object.
     */
    private void handleRockClimb(final Player player, final GameObject object) {
        AgilityHandler.walk(player, 4, player.getLocation(), player.getLocation().transform(0, -4, 0), Animation.create(740), 0, "You reach the top.");
        World.submit(new Pulse(4, player) {

            @Override
            public boolean pulse() {
                player.getAnimator().reset();
                return true;
            }
        });
    }

    @Override
    public Location getDestination(Node node, Node n) {
        switch (n.getId()) {
            case 23132:
                return Location.create(3005, 3953, 0);
            case 23556:
                return Location.create(3002, 3960, 0);
            case 23542:
                return Location.create(3002, 3945, 0);
            case 23640:
                return n.getLocation().transform(0, 1, 0);
        }
        return null;
    }

    @Override
    public void configure() {
        ObjectDefinition.forId(2309).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(2308).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(2307).getConfigurations().put("option:open", this);
        ObjectDefinition.forId(23137).getConfigurations().put("option:squeeze-through", this);
        ObjectDefinition.forId(23132).getConfigurations().put("option:swing-on", this);
        ObjectDefinition.forId(23556).getConfigurations().put("option:cross", this);
        ObjectDefinition.forId(23542).getConfigurations().put("option:walk-across", this);
        ObjectDefinition.forId(23640).getConfigurations().put("option:climb", this);
    }

    @Override
    public AgilityCourse createInstance(Player player) {
        return new WildernessCourse(player);
    }

}
