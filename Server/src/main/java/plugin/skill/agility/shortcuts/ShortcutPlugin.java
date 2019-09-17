package plugin.skill.agility.shortcuts;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.activity.ActivityManager;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.agility.AgilityHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.impl.ForceMovement;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.pulse.impl.MovementPulse;
import org.gielinor.rs2.task.impl.LocationLogoutTask;
import org.gielinor.rs2.task.impl.LogoutTask;

/**
 * Represents the plugin used to manage shortcuts.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class ShortcutPlugin implements Plugin<Object> {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        new StileShortcut().newInstance(arg);
        new AlKharidPit().newInstance(arg);
        new FaladorCrumblingWall().newInstance(arg);
        new FaladorTunnel().newInstance(arg);
        new VarrockSouthFence().newInstance(arg);
        new SteppingStones().newInstance(arg);
        new FaladorGrapple().newInstance(arg);
        new LumbridgeRiverGrapple().newInstance(arg);
        new TaverlyFloor().newInstance(arg);
        new MonkeyBarPlugin().newInstance(arg);
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Represents the plugin used for all stile shortcuts.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class StileShortcut extends OptionHandler {

        /**
         * Represents the animation to use.
         */
        private static final Animation ANIMATION = new Animation(839);

        /**
         * Represents the ids to use for the plugin.
         */
        private static final int[] IDS = new int[]{ 993, 2618, 3730, 7527, 12982, 19222, 22302, 29460, 33842, 34776 };

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            for (int objectId : IDS) {
                ObjectDefinition.forId(objectId).getConfigurations().put("option:climb-over", this);
            }
            return this;
        }

        @Override
        public boolean handle(final Player player, final Node n, String option) {
            player.getWalkingQueue().reset();
            Location loc = getLocation(player, (GameObject) n, true);
            player.getWalkingQueue().addPath(loc.getX(), loc.getY());
            player.getPulseManager().run(new MovementPulse(player, loc) {

                @Override
                public boolean pulse() {
                    climb(player, (GameObject) n);
                    return true;
                }
            }, "movement");
            return true;
        }

        @Override
        public Location getDestination(Node node, Node n) {
            return null;
        }

        /**
         * Method used to climb the stile.
         *
         * @param player the player.
         * @param object the object.
         */
        public static void climb(final Player player, final GameObject object) {
            player.lock(1);
            int delay = 0;
            World.submit(new Pulse(delay, player) {

                @Override
                public boolean pulse() {
                    ForceMovement movement = new ForceMovement(player, getLocation(player, object, true),
                        getLocation(player, object, false), ANIMATION) {

                        @Override
                        public void stop() {
                            super.stop();
                            if (object.getId() == 19222) {//falconry.
                                handleFalconry(player, object);
                            }
                        }
                    };
                    movement.run(player, 5);
                    return true;
                }
            });
        }

        /**
         * Method used to get the end location.
         *
         * @param player the player.
         * @param object the object.
         * @param start  if getting the start loc.
         * @return the location.
         */
        public static Location getLocation(final Player player, final GameObject object, boolean start) {
            if (object.getLocation().equals(3308, 3492, 0)) {
                if (start) {
                    return (player.getLocation().getY() >= 3493) ? object.getLocation().transform(0, 1, 0) : object.getLocation();
                }
                return (player.getLocation().getY() < 3493) ? object.getLocation().transform(0, 1, 0) : object.getLocation().transform(0, -1, 0);
            }
            if ((object.getDirection() == Direction.NORTH || object.getDirection() == Direction.SOUTH)) {
                if (player.getLocation().getY() <= object.getLocation().getY()) {
                    return start ? object.getLocation() : object.getLocation().transform(0, 1, 0);
                } else {
                    return start ? object.getLocation().transform(0, 1, 0) : object.getLocation();
                }
            } else {
                if (player.getLocation().getX() <= object.getLocation().getX()) {
                    return start ? object.getLocation() : object.getLocation().transform(1, 0, 0);
                } else {
                    return start ? object.getLocation().transform(1, 0, 0) : object.getLocation();
                }
            }
        }

        /**
         * Method used to handle the falconry stile.
         *
         * @param player the player.
         * @param object the object.
         */
        private static void handleFalconry(final Player player, GameObject object) {
            if (player.getLocation().equals(Location.create(2371, 3621, 0))) {
                ActivityManager.start(player, "falconry", false);
            } else {
                ActivityManager.getActivity("falconry").leave(player, false);
            }
        }
    }

    /**
     * Represents the shortcut for the alkharid pit.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class AlKharidPit extends OptionHandler {

        /**
         * Represents the scaling down anmation.
         */
        private static final Animation ANIMATION = new Animation(1148);

        /**
         * Represents the scaling animation.
         */
        private static final Animation SCALE = new Animation(740);

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ObjectDefinition.forId(9331).getConfigurations().put("option:climb", this);
            ObjectDefinition.forId(9332).getConfigurations().put("option:climb", this);
            return this;
        }

        @Override
        public boolean handle(final Player player, Node node, String option) {
            int id = ((GameObject) node).getId();
            if (player.getSkills().getLevel(Skills.AGILITY) < 38) {
                player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of at least 38 in order to do this.");
                return true;
            }
            switch (id) {
                case 9331:
                    ForceMovement.run(player, player.getLocation(), Location.create(3303, 3315, 0), ANIMATION, ANIMATION, Direction.EAST, 13).setEndAnimation(Animation.RESET);
                    break;
                case 9332:
                    ForceMovement.run(player, player.getLocation(), Location.create(3307, 3315, 0), SCALE, SCALE, Direction.EAST, 13).setEndAnimation(Animation.RESET);
                    break;
            }
            return true;
        }

        @Override
        public Location getDestination(Node node, Node n) {
            final GameObject object = (GameObject) n;
            if (object.getId() == 9331) {
                return object.getLocation().transform(1, 0, 0);
            }
            return null;
        }

    }

    /**
     * Represents the falador crumbling will plugin to handle the shortcut.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class FaladorCrumblingWall extends OptionHandler {

        /**
         * Represents the level needed to pass this shortcut.
         */
        private static final int LEVEL = 5;

        /**
         * Represents the animation to use.
         */
        private static final Animation ANIMATION = new Animation(840);

        /**
         * Represents the locations used in this force movement.
         */
        private static final Location[] LOCATIONS = new Location[]{ new Location(2936, 3355, 0), new Location(2934, 3355, 0) };

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ObjectDefinition.forId(11844).getConfigurations().put("option:climb-over", this);
            return this;
        }

        @Override
        public boolean handle(Player player, Node node, String option) {
            if (player.getSkills().getLevel(Skills.AGILITY) < LEVEL) {
                player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of " + LEVEL + " in order to do this.");
                return true;
            }
            ForceMovement.run(player, player.getLocation().getX() >= 2936 ? LOCATIONS[0] : LOCATIONS[1], player.getLocation().getX() >= 2936 ? LOCATIONS[1] : LOCATIONS[0], ANIMATION, 10);
            return true;
        }

    }

    /**
     * Represents the plugin used to handle the tunnel shortcut in falador.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class FaladorTunnel extends OptionHandler {

        /**
         * Represents the level needed to pass.
         */
        private static final int LEVEL = 26;

        /**
         * The climbing down animation.
         */
        private static final Animation CLIMB_DOWN = Animation.create(2589);

        /**
         * The crawling through animation.
         */
        private static final Animation CRAWL_THROUGH = Animation.create(2590);

        /**
         * The climbing up animation.
         */
        private static final Animation CLIMB_UP = Animation.create(2591);

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ObjectDefinition.forId(9309).getConfigurations().put("option:climb-into", this);
            ObjectDefinition.forId(9310).getConfigurations().put("option:climb-into", this);
            return this;
        }

        @Override
        public boolean handle(final Player player, Node node, String option) {
            if (player.getSkills().getLevel(Skills.AGILITY) < LEVEL) {
                player.getActionSender().sendMessage("You need an Agility level of 26 to negotiate this tunnel.");
                return true;
            }
            player.lock(4);
            final GameObject o = (GameObject) node;
            ForceMovement.run(player, o.getId() == 9310 ? Location.create(2948, 3313, 0) : Location.create(2948, 3309, 0), o.getLocation(), CLIMB_DOWN, 8);
            World.submit(new Pulse(1, player) {

                int count;

                @Override
                public boolean pulse() {
                    switch (++count) {
                        case 2:
                            player.animate(CRAWL_THROUGH);
                            player.getProperties().setTeleportLocation(Location.create(2948, 3311, 0));
                            break;
                        case 5:
                            ForceMovement.run(player, o.getId() == 9310 ? Location.create(2948, 3311, 0) : Location.create(2948, 3311, 0), o.getId() == 9310 ? Location.create(2948, 3309, 0) : Location.create(2948, 3313, 0), CLIMB_UP, 19);
                            break;
                        case 6:
                            player.animate(ForceMovement.WALK_ANIMATION);
                            return true;
                    }
                    return false;
                }
            });
            return true;
        }

    }

    /**
     * Represents the varrock fence shortcut plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class VarrockSouthFence extends OptionHandler {

        /**
         * Represents the level needed to cross this fence.
         */
        private static final int LEVEL = 13;

        /**
         * Represents the running animation.
         */
        private static final Animation RUNNING_ANIM = new Animation(1995);

        /**
         * Represents the jumping animation.
         */
        private static final Animation JUMP_ANIM = new Animation(1603);

        /**
         * Represents the related locations used in this shortcut. format: (start, start, end, end
         */
        private static final Location[] LOCATIONS = new Location[]{ new Location(3240, 3331, 0), new Location(3240, 3338, 0), Location.create(3240, 3334, 0), Location.create(3240, 3335, 0) };

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ObjectDefinition.forId(9300).getConfigurations().put("option:jump-over", this);
            return this;
        }

        @Override
        public boolean handle(final Player player, Node node, String option) {
            if (player.getSkills().getLevel(Skills.AGILITY) < LEVEL) {
                player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of " + LEVEL + " in order to do this.");
                return true;
            }
            player.faceLocation(node.getLocation());
            World.submit(new Pulse(2, player) {

                @Override
                public boolean pulse() {
                    player.animate(JUMP_ANIM);
                    return true;
                }
            });
            ForceMovement.run(player, player.getLocation().getY() >= 3335 ? LOCATIONS[1] : LOCATIONS[0], player.getLocation().getY() >= 3335 ? LOCATIONS[2] : LOCATIONS[3], RUNNING_ANIM, 18);
            return true;
        }

        @Override
        public Location getDestination(Node node, Node n) {
            return ((Player) node).getLocation().getY() >= 3335 ? LOCATIONS[1] : LOCATIONS[0];
        }

    }

    /**
     * Represents the stepping stone shortcut plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class SteppingStones extends OptionHandler {

        /**
         * Represents the level needed to pass this shortcut.
         */
        private static final int LEVEL = 31;

        /**
         * Represents the animation to use.
         */
        private static final Animation ANIMATION = new Animation(741);

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ObjectDefinition.forId(9315).getConfigurations().put("option:jump-onto", this);
            return this;
        }

        @Override
        public boolean handle(final Player player, Node node, String option) {
            if (player.getSkills().getLevel(Skills.AGILITY) < LEVEL) {
                player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of at least " + LEVEL + " in order to do this.");
                return true;
            }
            final int offset = player.getLocation().getX() == 3149 ? 1 : -1;
            player.lock();
            World.submit(new Pulse(2, player) {

                int counter = 1;

                @Override
                public boolean pulse() {
                    if (counter == 6) {
                        player.unlock();
                        return true;
                    }
                    ForceMovement.run(player, player.getLocation(), player.getLocation().transform(offset, 0, 0), ANIMATION, 10);
                    counter++;
                    return false;
                }
            });
            return true;
        }
    }

    /**
     * Represents the plugin used to handle the grappling of the falador wall.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class FaladorGrapple extends OptionHandler {

        /**
         * Represents the requirements array.
         */
        private static final int[][] REQUIREMENTS = new int[][]{ { Skills.AGILITY, 11 }, { Skills.RANGE, 19 }, { Skills.STRENGTH, 37 } };

        /**
         * Represents the mithril c'bow item.
         */
        private static final Item MITH_CBOW = new Item(9181);

        /**
         * Represents the mithril grapple item.
         */
        private static final Item MITH_GRAPPLE = new Item(9419);

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ObjectDefinition.forId(17049).getConfigurations().put("option:grapple", this);
            ObjectDefinition.forId(17050).getConfigurations().put("option:grapple", this);
            ObjectDefinition.forId(17051).getConfigurations().put("option:jump", this);
            ObjectDefinition.forId(17052).getConfigurations().put("option:jump", this);
            return this;
        }

        @Override
        public boolean handle(final Player player, final Node node, String option) {
            switch (option) {
                case "jump":
                    ForceMovement.run(player, player.getLocation(), ((GameObject) node).getId() == 17051 ? Location.create(3033, 3390, 0) : Location.create(3032, 3388, 0), new Animation(7268), 10);
                    break;
                case "grapple":
                    for (int i = 0; i < REQUIREMENTS.length; i++) {
                        if (player.getSkills().getLevel(REQUIREMENTS[i][0]) < REQUIREMENTS[i][1]) {
                            player.getDialogueInterpreter().sendPlaneMessage("You need at least 19 Ranged, 11 Agility and 37 Strength to do that.");
                            return true;
                        }
                    }
                    if (!player.getEquipment().containsItem(MITH_CBOW) || !player.getEquipment().containsItem(MITH_GRAPPLE)) {
                        player.getDialogueInterpreter().sendPlaneMessage("You need a Mithril crossbow and a Mithril grapple in order to do this.");
                        return true;
                    }
                    player.lock();
                    World.submit(new Pulse(1, player) {

                        int counter = 1;

                        @Override
                        public boolean pulse() {
                            switch (counter++) {
                                case 2:
                                    player.faceLocation(((GameObject) node).getId() == 17049 ? Location.create(3033, 3388, 0) : Location.create(3032, 3391, 0));
                                    break;
                                case 3:
                                    player.getInterfaceState().openComponent(8677);
                                    break;
                                case 4:
                                    PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
                                    player.getInterfaceState().removeTabs(0, 1, 2, 3, 4, 5, 6, 11, 12);
                                    break;
                                case 7:
                                    player.getProperties().setTeleportLocation(((GameObject) node).getId() == 17050 ? Location.create(3033, 3389, 1) : Location.create(3033, 3389, 1));
                                    break;
                                case 8:
                                    player.getInterfaceState().close();
                                    player.getInterfaceState().openDefaultTabs();
                                    PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                                    player.unlock();
                                    return true;
                            }
                            return false;
                        }
                    });
                    break;
            }
            return true;
        }

        @Override
        public Location getDestination(final Node node, final Node n) {
            return ((GameObject) n).getId() == 17050 ? Location.create(3032, 3388, 0) : null;
        }

    }

    /**
     * Represents the plugin used to handle river grappling a broken raft from lumbridge to Al Kharid.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class LumbridgeRiverGrapple extends OptionHandler {

        /**
         * Represents the river grappling locations(start, finish, etc..)
         */
        private static final Location LOCATIONS[] = new Location[]{ new Location(3246, 3179, 0), new Location(3260, 3181, 0) };

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ObjectDefinition.forId(17068).getConfigurations().put("option:grapple", this);
            return this;
        }

        @Override
        public boolean handle(Player player, Node node, String option) {
            if (player.getSkills().getLevel(Skills.AGILITY) < 8 || player.getSkills().getLevel(Skills.RANGE) < 37 || player.getSkills().getLevel(Skills.STRENGTH) < 19) {
                player.getDialogueInterpreter().sendPlaneMessage("You need at least 37 Ranged, 8 Agility and 19 Strength to do that.");
                return true;
            }
            return true;
        }

        @Override
        public Location getDestination(final Node node, final Node n) {
            return node.getLocation().getX() < 3251 ? LOCATIONS[0] : LOCATIONS[1];
        }

    }

    /**
     * Represents the taverly floor shortcut plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class TaverlyFloor extends OptionHandler {

        /**
         * Represents the level required to use this shortcut.
         */
        private static final int LEVEL = 80;

        /**
         * Represents the running animation.
         */
        private static final Animation RUNNING_ANIM = new Animation(1995);

        /**
         * Represents the jumping animation.
         */
        private static final Animation JUMP_ANIM = new Animation(1603);

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ObjectDefinition.forId(16510).getConfigurations().put("option:jump-over", this);
            return this;
        }

        @Override
        public boolean handle(final Player player, Node node, String option) {
            if (player.getSkills().getLevel(Skills.AGILITY) < LEVEL) {
                player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of at least " + LEVEL + " in order to do this.");
                return true;
            }
            World.submit(new Pulse(1, player) {

                @Override
                public boolean pulse() {
                    player.animate(JUMP_ANIM);
                    return true;
                }
            });
            ForceMovement.run(player, player.getLocation().getX() >= 2880 ? Location.create(2881, 9813, 0) : Location.create(2877, 9813, 0), player.getLocation().getX() >= 2880 ? Location.create(2877, 9813, 0) : Location.create(2881, 9813, 0), RUNNING_ANIM, 13);
            return true;
        }

        @Override
        public Location getDestination(Node node, Node n) {
            return node.getLocation().getX() >= 2880 ? Location.create(2881, 9813, 0) : Location.create(2877, 9813, 0);
        }
    }

    /**
     * Represents the plugin used to handle monkey bars.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class MonkeyBarPlugin extends OptionHandler {

        /**
         * Represents the ids of the monkey bars.
         */
        private static final int[] IDS = new int[]{ 29375 };

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            for (int id : IDS) {
                ObjectDefinition.forId(id).getConfigurations().put("option:swing across", this);
            }
            return this;
        }

        @Override
        public boolean handle(final Player player, final Node node, final String option) {
            final GameObject object = (GameObject) node;
            player.lock(5);
            Direction direct = Direction.get((object.getDirection().toInteger() + 2) % 4);
            if (direct == Direction.SOUTH && player.getLocation().getY() < 9969) {
                direct = Direction.NORTH;
            } else if (direct == Direction.NORTH && player.getLocation().getY() >= 9969) {
                direct = Direction.SOUTH;
            }
            player.getAppearance().setAnimations(Animation.create(745));
            final Location start = player.getLocation();
            final Direction dir = direct;
            ForceMovement.run(player, start.transform(dir), start.transform(dir.getStepX() << 1, dir.getStepY() << 1, 0), Animation.create(742), Animation.create(744));
            player.addExtension(LogoutTask.class, new LocationLogoutTask(5, start));
            World.submit(new Pulse(2, player) {

                int count;

                @Override
                public boolean pulse() {
                    if (++count == 1) {
                        setDelay(4);
                        AgilityHandler.walk(player, -1, player.getLocation().transform(dir), player.getLocation().transform(dir.getStepX() * 4, dir.getStepY() * 4, 0), Animation.create(744), 0.0, null);
                        player.addExtension(LogoutTask.class, new LocationLogoutTask(9, start));
                    } else if (count == 2) {
                        AgilityHandler.forceWalk(player, -1, player.getLocation(), player.getLocation().transform(dir), Animation.create(743), 10, 14.0, null);
                        return true;
                    }
                    return false;
                }
            });
            return true;
        }

    }
}
