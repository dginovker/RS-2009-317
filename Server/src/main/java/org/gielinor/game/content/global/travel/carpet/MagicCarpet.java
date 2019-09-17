package org.gielinor.game.content.global.travel.carpet;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles magic carpet rides.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class MagicCarpet {

    /**
     * Prepares the player for the magic carpet ride.
     *
     * @param player      The player.
     * @param destination The carpet destination.
     */
    public static void prepare(final Player player, final Destination destination) {
        final boolean running = player.getWalkingQueue().isRunning();
        player.getWalkingQueue().reset();
        player.lock(7);
        player.face(null);
        player.getWalkingQueue().addPath(3308, 3110);
        World.submit(new Pulse(4, player) {

            @Override
            public boolean pulse() {
                player.faceLocation(Location.create(3308, 3108));
                World.submit(new Pulse(2, player) {

                    @Override
                    public boolean pulse() {
                        player.setLocation(destination.getLocation());
                        return true;
                    }
                });
                return true;
            }
        });
    }

    /**
     * Represents a destination to travel to with a magic carpet.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public enum Destination {

        SHANTAY_PASS(Location.create(0, 0, 0), 200),
        NORTH_OF_POLLNIVNEACH(Location.create(3351, 2942, 0), 200),
        UZER(Location.create(3469, 3113, 0), 200),
        BEDABIN_CAMP(Location.create(3180, 3045, 0), 200);

        /**
         * The location to travel to.
         */
        private final Location location;
        /**
         * The cost.
         */
        private final int cost;

        /**
         * Creates a new carpet place.
         *
         * @param location The location to travel to.
         * @param cost     The cost.
         */
        Destination(Location location, int cost) {
            this.location = location;
            this.cost = cost;
        }

        /**
         * Gets the location to travel to.
         *
         * @return THe location.
         */
        public Location getLocation() {
            return location;
        }

        /**
         * Gets the cost.
         *
         * @return The cost.
         */
        public int getCost() {
            return cost;
        }
    }

    /**
     * Travels by magic carpet.
     *
     * @param player      The player.
     * @param destination The carpet destination.
     */
    public static void travel(final Player player, final Destination destination, boolean running) {
        //		int ticks = player.getLocation().distance(destination.getLocation()) + 1;
        //		player.lock(ticks);
        //		player.getActionQueue().clearRemovableActions();
        //		Animation standAnimation = player.getStandAnimation();
        //		Animation walkAnimation = player.getWalkAnimation();
        //		Animation runAnimation = player.getRunAnimation();
        //		player.playAnimation(Animation.create(2262));
        //		player.setStandAnimation(Animation.create(2262));
        //		player.setWalkAnimation(Animation.create(2262));
        //		player.setRunAnimation(Animation.create(2262));
        //		player.getWalkingQueue().addStep(destination.getLocation().getX(), destination.getLocation().getY());
        //		//		player.getPulseManager().run(new MovementPulse(player, destination.getLocation(), false) {
        //		//			@Override
        //		//			public boolean pulse() {
        //		//				return true;
        //		//			}
        //		//		}, "movement");
        //		World.getWorld().submit(new Tickable(ticks) {
        //
        //			@Override
        //			public void execute() {
        //				this.stop();
        //				if (player == null) {
        //					return;
        //				}
        //				System.out.println("?");
        //				player.setStandAnimation(standAnimation);
        //				player.setWalkAnimation(walkAnimation);
        //				player.setRunAnimation(runAnimation);
        //				player.getWalkingQueue().setRunningToggled(running);
        //				player.getWalkingQueue().reset();
        //				player.playAnimation(Animation.create(2263));
        //			}
        //		});
    }
}
