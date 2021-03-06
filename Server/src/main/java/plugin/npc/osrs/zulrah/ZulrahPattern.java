package plugin.npc.osrs.zulrah;

import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.agg.AggressiveBehavior;
import org.gielinor.game.node.entity.npc.agg.AggressiveHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the different patterns for Zulrah.
 *
 * @author Empathy
 * @author Vexia
 */
public class ZulrahPattern {

    /**
     * The spots for each Zulrah pattern.
     */
    private final ZulrahSpot[] spots;

    /**
     * The list of current toxic cloud locations.
     */
    private final List<Location> toxicClouds = new ArrayList<>();

    /**
     * The index of the first pattern.
     */
    private int index = -1;

    /**
     * The hp of zulrah.
     */
    private int hp;
    private Player player;

    /**
     * Constructs a new {@code ZulrahPattern} object.
     *
     * @param spots The spots
     */
    public ZulrahPattern(ZulrahSpot... spots) {
        this.spots = spots;
    }

    /**
     * Gets a zulrah pattern to use.
     *
     * @return The zulrah pattern.
     */
    public static ZulrahPattern getPattern() {
        return new ZulrahPattern(RandomUtil.getRandomElement(Pattern.values()).getSpots());
    }

    /**
     * Handles the next zulrah stage.
     *
     * @param zulrah The zulrah npc.
     */
    public void next(final ZulrahNPC zulrah) {
        index++;
        if (index > spots.length - 1) {
            index = 0;
        }
        if (DeathTask.isDead(zulrah) || !zulrah.isActive()) {
            return;
        }
        hp = zulrah.getSkills().getLifepoints();
        final ZulrahSpot spot = getCurrentSpot();
        zulrah.animate(ZulrahNPC.DOWN_ANIMATION);
        World.submit(new Pulse(4, zulrah) {

            @Override
            public boolean pulse() {
                zulrah.transform(spot.getType().getNpcId());
                zulrah.setDirection(spot.getDirection());
                zulrah.teleport(spot.getLocation(zulrah.getXOffset(), zulrah.getYOffset()));
                zulrah.getSkills().setLifepoints(hp);
                zulrah.animate(ZulrahNPC.UP_ANIMATION);
                zulrah.faceLocation(zulrah.getLocation().transform(spot.getDirection()));
                return true;
            }
        });
        if (getCurrentSpot().getType() != ZulrahType.MELEE && getCurrentSpot().getToxicClouds() != null) {
            toxic(zulrah);
        }
    }

    /**
     * Handles the spawning of toxic clouds.
     *
     * @param zulrah The zulrah npc.
     */
    public void toxic(final ZulrahNPC zulrah) {
        final ZulrahSpot spot = getCurrentSpot();
        World.submit(new Pulse(5, zulrah) {

            int index = -1;

            @Override
            public boolean pulse() {
                final Location loc = ZulrahSpot.getLocation(spot.getToxicClouds()[index + 1], zulrah);
                zulrah.animate(ZulrahNPC.SHOOT_ANIMAION);
                zulrah.faceLocation(loc);
                for (int i = 0; i < 2; i++) {
                    index++;
                    Projectile pp = Projectile.create(zulrah, null, ZulrahNPC.TOXIC_PROJECTILE, 100, 20, 30, 100);
                    pp.setEndLocation(loc);
                    pp.send();
                    World.submit(new Pulse(zulrah.getLocation().getDelay(loc), zulrah) {

                        int count = 0;

                        @Override
                        public boolean pulse() {
                            if (count == 0 && getDelay() == zulrah.getLocation().getDelay(loc)) {
                                toxicClouds.add(loc);
                                ObjectBuilder.add(new GameObject(11700, loc, 10, 0) {

                                    @Override
                                    public void remove() {
                                        toxicClouds.remove(loc);
                                    }
                                }, 10);
                                setDelay(1);
                                count++;
                                return false;
                            }
                            List<Player> players = RegionManager.getLocalPlayers(loc);
                            for (Player p : players) {
                                if (p.getLocation().withinDistance(loc, 1)) {
                                    p.getImpactHandler().manualHit(p, RandomUtil.random(1, 4), ImpactHandler.HitsplatType.VENOM);
                                }
                            }
                            return count++ == 10;
                        }
                    });
                }
                if (index == spot.getToxicClouds().length - 1) {
                    if (getCurrentSpot().getSnakelingLocations() != null) {
                        snakeling(zulrah);
                    }
                    return true;
                }
                return false;
            }

        });
    }

    /**
     * Handles the spawning of snakelings.
     *
     * @param zulrah The zulrah npc.
     */
    public void snakeling(final ZulrahNPC zulrah) {
        final ZulrahSpot spot = getCurrentSpot();
        World.submit(new Pulse(5, zulrah) {

            int index = -1;

            @Override
            public boolean pulse() {
                final Location loc = ZulrahSpot.getLocation(spot.getSnakelingLocations()[index + 1], zulrah);
                zulrah.animate(ZulrahNPC.SHOOT_ANIMAION);
                zulrah.faceLocation(loc);
                index++;
                Projectile snake = Projectile.create(zulrah, null, ZulrahNPC.SNAKELING_PROJECTILE, 100, 20, 30, 100);
                snake.setEndLocation(loc);
                snake.send();
                World.submit(new Pulse(4) {

                    @Override
                    public boolean pulse() {
                        ZulrahSnake n = new ZulrahSnake(RandomUtil.random(22045, 22047), loc, zulrah);
                        n.setRespawn(false);
                        n.init();
                        n.animate(new Animation(22413));
                        n.getSkills().setLifepoints(1);
                        n.setAggressive(true);
                        if (player != null) {
                            n.attack(player);
                            n.setAggressiveHandler(new AggressiveHandler(player, AggressiveBehavior.WILDERNESS));
                        }
                        return true;
                    }

                });

                return index == spot.getSnakelingLocations().length - 1;
            }

        });
    }

    /**
     * Gets the current spot.
     *
     * @return The zulrah spot.
     */
    public ZulrahSpot getCurrentSpot() {
        return spots[index];
    }

    /**
     * @return The spots
     */
    public ZulrahSpot[] getSpots() {
        return spots;
    }

    /**
     * @return the toxicClouds
     */
    public List<Location> getToxicClouds() {
        return toxicClouds;
    }

    /**
     * Sets the player this is attacking.
     *
     * @param player The player.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Holds the possible zulrah patterns.
     *
     * @author Vexia
     * @author Empathy
     */
    public enum Pattern {
        ONE(new ZulrahSpot(ZulrahType.RANGE, Location.create(2266, 3072, 0), Direction.SOUTH, new Location[]{ Location.create(2266, 3069, 0), Location.create(2263, 3070, 0), Location.create(2269, 3069, 0), Location.create(2272, 3069, 0), Location.create(2273, 3072, 0), Location.create(2273, 3075, 0), Location.create(2263, 3073, 0), Location.create(2263, 3076, 0) }),
            new ZulrahSpot(ZulrahType.MELEE, Location.create(2266, 3072, 0), Direction.SOUTH),
            new ZulrahSpot(ZulrahType.MAGIC, Location.create(2266, 3072, 0), Direction.SOUTH),
            new ZulrahSpot(ZulrahType.RANGE, Location.create(2266, 3062, 0), Direction.NORTH, new Location[]{ Location.create(2273, 3074, 0), Location.create(2273, 3077, 0), Location.create(2273, 3071, 0), Location.create(2271, 3069, 0), Location.create(2269, 3069, 0), Location.create(2266, 3069, 0) }, new Location[]{ Location.create(2266, 3069, 0), Location.create(2263, 3070, 0), Location.create(2263, 3073, 0), Location.create(2263, 3076, 0) }),
            new ZulrahSpot(ZulrahType.MELEE, Location.create(2266, 3072, 0), Direction.SOUTH),
            new ZulrahSpot(ZulrahType.MAGIC, Location.create(2256, 3072, 0), Direction.EAST),
            new ZulrahSpot(ZulrahType.RANGE, Location.create(2266, 3062, 0), Direction.NORTH),
            new ZulrahSpot(ZulrahType.MAGIC, Location.create(2266, 3062, 0), Direction.NORTH),
            new ZulrahSpot(ZulrahType.JAD, Location.create(2256, 3072, 0), Direction.EAST),
            new ZulrahSpot(ZulrahType.MELEE, Location.create(2266, 3072, 0), Direction.SOUTH));

        /**
         * The zulrah spots for the pattern.
         */
        private final ZulrahSpot[] spots;

        /**
         * Constructs a new @{Code Pattern} object.
         *
         * @param spots The spots.
         */
        Pattern(ZulrahSpot... spots) {
            this.spots = spots;
        }

        /**
         * @return the spots
         */
        public ZulrahSpot[] getSpots() {
            return spots;
        }
    }

}
