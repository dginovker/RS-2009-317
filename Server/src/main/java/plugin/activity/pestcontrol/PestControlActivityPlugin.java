package plugin.activity.pestcontrol;

import org.gielinor.content.periodicity.daily.impl.MinigameSpotlight;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.activity.ActivityManager;
import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.impl.PulseManager;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.GameConstants;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.build.DynamicRegion;
import org.gielinor.game.world.map.zone.RegionZone;
import org.gielinor.game.world.map.zone.ZoneBuilder;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;
import org.gielinor.utilities.string.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Handles the Pest Control activity.
 *
 * @author Emperor
 */
public final class PestControlActivityPlugin extends ActivityPlugin {

    /**
     * The wait time in ticks.
     */
    private static final int WAIT_TIME = 20;
    /**
     * The minimum team size.
     */
    protected static final int MIN_TEAM_SIZE = 1;
    /**
     * The maximum team size.
     */
    protected static final int MAX_TEAM_SIZE = 20;
    /**
     * The amount of ticks passed.
     */
    private int ticks;
    /**
     * The boat type.
     */
    private final BoatType type;
    /**
     * The waiting players.
     */
    private final List<Player> waitingPlayers = new ArrayList<>();

    /**
     * The active game sessions.
     */
    private final List<PestControlSession> sessions = new ArrayList<>();

    /**
     * The game updating pulse.
     */
    private final Pulse pulse = new Pulse(1) {

        @Override
        public boolean pulse() {
            for (Iterator<PestControlSession> it = sessions.iterator(); it.hasNext(); ) {
                PestControlSession session = it.next();
                if (session != null && session.update()) {
                    it.remove();
                }
            }
            if (waitingPlayers.size() == MAX_TEAM_SIZE) {
                PestControlActivityPlugin.this.start();
            }
            if (++ticks >= WAIT_TIME) {
                if (waitingPlayers.size() >= MIN_TEAM_SIZE) {
                    PestControlActivityPlugin.this.start();
                } else {
                    ticks = (WAIT_TIME - 100);
                }
            }
            if ((ticks < ((WAIT_TIME - 100) + 50) && ticks % 100 == 0) || (ticks % 50 == 0)) {
                for (Player p : waitingPlayers) {
                    updateTime(p);
                }
            }
            return false;
        }
    };

    /**
     * Starts a new pest control session.
     */
    public void start() {
        PestControlSession session = new PestControlSession(DynamicRegion.create(10536), this);
        session.startGame(waitingPlayers);
        session.getRegion().getRegionZones().add(new RegionZone(this, session.getRegion().getBorders()));
        sessions.add(session);
        ticks = 0;
    }

    /**
     * Ends the pest control session.
     *
     * @param session The session to end.
     * @param success If the mission was successful.
     */
    public void end(PestControlSession session, boolean success) {
        if (!session.isActive()) {
            return;
        }
        for (final Player p : session.getRegion().getPlanes()[0].getPlayers()) {
            p.getProperties().setTeleportLocation(getLeaveLocation());
            if (!success) {
                p.getDialogueInterpreter().open(3781, true, 0, true);
            } else if (p.getAttribute("pc_zeal", 0) >= 50) {
                int pestPoints = type.ordinal() + 5;
                int percentage = 0 /*p.getDonorManager().isDonor() ? 25 :
                        p.getDonorManager().isSuperDonor() ? 35 :
                                p.getDonorManager().isExtremeDonor() ? 50 : 0*/;
                // TODO percentage for members?
                pestPoints += (int) p.getDonorManager().getAdditional(pestPoints, percentage);
                pestPoints *= MinigameSpotlight.Companion.getExpMultiplier(this.getName());
                int coinsCount = p.getProperties().getCurrentCombatLevel() * 20;
                coinsCount += ((int) player.getDonorManager().getAdditional(coinsCount, percentage));
                p.getSavedData().getActivityData().increasePestPoints(pestPoints);
                Item coins = new Item(Item.COINS, coinsCount);
                if (!p.getInventory().add(coins)) {
                    GroundItemManager.create(coins, p);
                }
                p.getDialogueInterpreter().open(3781, true, 1, TextUtils.numberToString(pestPoints));
            } else {
                p.getDialogueInterpreter().open(3781, true, 2, true);
            }
            p.removeAttribute("pc_zeal");
            p.removeExtension(PestControlSession.class);
            p.fullRestore();
            PulseManager.cancelDeathTask(p);
            World.submit(new Pulse(1, p) {

                @Override
                public boolean pulse() {
                    p.getSkills().restore();
                    return true;
                }
            });
        }
        session.getRegion().getRegionZones().clear();
        session.setActive(false);
    }

    /**
     * Gets the location the player should teleport to when leaving the game.
     *
     * @return {@code
     */
    public Location getLeaveLocation() {
        switch (type) {
            case NOVICE:
                return Location.create(2657, 2639, 0);
            case INTERMEDIATE:
                return Location.create(2644, 2644, 0);
            case VETERAN:
                return Location.create(2638, 2653, 0);
        }
        return GameConstants.HOME_LOCATION;
    }

    /**
     * Constructs a new {@code PestControlActivityPlugin} {@code Object}.
     */
    public PestControlActivityPlugin() {
        this(BoatType.NOVICE);
    }

    /**
     * Constructs a new {@code PestControlActivityPlugin} {@code Object}.
     *
     * @param type The boat type.
     */
    public PestControlActivityPlugin(BoatType type) {
        super("pest control " + type.name().toLowerCase(), false, true, true);
        this.type = type;
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        if (e instanceof Player) {
            Player p = (Player) e;
            if (!logout) {
                p.getInterfaceState().closeOverlay();
            } else {
                e.setLocation(getLeaveLocation());
                e.getProperties().setTeleportLocation(getLeaveLocation());
            }
            waitingPlayers.remove(p);
            updatePlayerCount();
        }
        return super.leave(e, logout);
    }

    @Override
    public void register() {
        if (type == BoatType.NOVICE) {
            PestControlActivityPlugin[] activities = new PestControlActivityPlugin[]{ this, new PestControlActivityPlugin(BoatType.INTERMEDIATE), new PestControlActivityPlugin(BoatType.VETERAN) };
            ActivityManager.register(activities[1]);
            ActivityManager.register(activities[2]);
            //Load abstract NPC plugins
            PluginManager.definePlugin(new PCPortalNPC());
            PluginManager.definePlugin(new PCSquireNPC());
            PluginManager.definePlugin(new PCTorcherNPC());
            PluginManager.definePlugin(new PCDefilerNPC());
            PluginManager.definePlugin(new PCRavagerNPC());
            PluginManager.definePlugin(new PCShifterNPC());
            PluginManager.definePlugin(new PCSplatterNPC());
            PluginManager.definePlugin(new PCSpinnerNPC());
            PluginManager.definePlugin(new PCObjectHandler());
            PluginManager.definePlugin(new PestControlSquire());
            PluginManager.definePlugin(new VoidSealPlugin());
            ZoneBuilder.configure(new PCLanderZone(activities));
            ZoneBuilder.configure(new PCIslandZone());
        }
        pulse.start();
        World.submit(pulse);
    }

    @Override
    public boolean interact(Entity e, Node target, Option option) {
        return super.interact(e, target, option);
    }

    @Override
    public boolean start(Player p, boolean login, Object... args) {
        if (p.getProperties().getCurrentCombatLevel() < type.getRequirement()) {
            p.getActionSender().sendMessage("You need a combat level of " + type.getRequirement() + " or higher to board this lander.");
            return false;
        }
        waitingPlayers.add(p);
        openLanderInterface(p);
        return true;
    }

    /**
     * Updates the lander interface.
     *
     * @param p The player.
     */
    public void openLanderInterface(Player p) {
        p.getInterfaceState().openOverlay(new Component(23680));
        updateTime(p);
        updatePlayerCount();
        p.getActionSender().sendString("Points: " + p.getSavedData().getActivityData().getPestPoints(), 23684);
        p.getActionSender().sendString("(Need " + MIN_TEAM_SIZE + " to " + MAX_TEAM_SIZE + " players)", 23683);
        p.getActionSender().sendString(TextUtils.formatDisplayName(type.name()), 23686);
        if (p.getAttribute("PEST_CONTROL_PULSE") != null) {
            return;
        }
        Pulse pulse = new Pulse(1, p) {

            @Override
            public boolean pulse() {
                if (p.getInterfaceState().getComponent(23680) == null) {
                    openLanderInterface(p);
                    return false;
                }
                return false;
            }
        };
        p.setAttribute("PEST_CONTROL_PULSE", pulse);
    }

    /**
     * Updates the current time left.
     *
     * @param p The player.
     */
    public void updateTime(Player p) {
        int ticks = WAIT_TIME - this.ticks;
        if (ticks > 99) {
            p.getActionSender().sendString("Next Departure: " + (ticks / 100) + " min", 23681);
        } else if (ticks > 50) {
            p.getActionSender().sendString("Next Departure: 1 min", 23681);
        } else {
            p.getActionSender().sendString("Next Departure: 30 seconds", 23681);
        }
    }

    /**
     * Updates the player count for all players in the lander.
     */
    public void updatePlayerCount() {
        for (Player p : waitingPlayers) {
            p.getActionSender().sendString("Players Ready: " + waitingPlayers.size(), 23682);
        }
    }

    @Override
    public boolean death(Entity e, Entity killer) {
        if (e instanceof Player && e.getViewport().getRegion().getRegionId() == 10536) {
            PestControlSession session = e.getExtension(PestControlSession.class);
            if (session != null) {
                Location l = session.getRegion().getBaseLocation();
                e.getProperties().setTeleportLocation(l.transform(32 + RandomUtil.RANDOM.nextInt(4), 49 + RandomUtil.RANDOM.nextInt(6), 0));
                return true;
            }
        }
        return super.death(e, killer);
    }

    @Override
    public ActivityPlugin newInstance(Player p) throws Throwable {
        return this;
    }

    @Override
    public Location getSpawnLocation() {
        return GameConstants.HOME_LOCATION;
    }

    @Override
    public void configure() {
        registerRegion(10536);
    }

    /**
     * Gets the list of waiting players.
     *
     * @return The list of waiting players.
     */
    public List<Player> getWaitingPlayers() {
        return waitingPlayers;
    }

    /**
     * Gets the boat type.
     *
     * @return The boat type.
     */
    public BoatType getType() {
        return type;
    }

    /**
     * The boat types.
     *
     * @author Emperor
     */
    public static enum BoatType {

        NOVICE(40),
        INTERMEDIATE(70),
        VETERAN(100);

        /**
         * The combat level requirement.
         */
        private final int requirement;

        /**
         * Constructs a new {@code BoatType} {@code Object}.
         *
         * @param requirement The combat level requirement.
         */
        private BoatType(int requirement) {
            this.requirement = requirement;
        }

        /**
         * Gets the requirement.
         *
         * @return The requirement.
         */
        public int getRequirement() {
            return requirement;
        }
    }
}
