package plugin.npc;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.LightSource;
import org.gielinor.game.content.global.action.DigAction;
import org.gielinor.game.content.global.action.DigSpadeHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.zone.impl.DarkZone;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InterfaceContext;
import org.gielinor.net.packet.out.Interface;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the Giant Mole NPC.
 *
 * @author Emperor
 */
public final class GiantMoleNPC extends AbstractNPC {

    /**
     * The dig locations.
     */
    private static final Location[] DIG_LOCATIONS = new Location[]{
        Location.create(1760, 5183, 0),
        Location.create(1736, 5223, 0),
        Location.create(1777, 5235, 0),
        Location.create(1739, 5150, 0),
        Location.create(1769, 5148, 0)
    };

    /**
     * The digging animation.
     */
    private static final Animation DIG_ANIMATION = new Animation(3314, Priority.VERY_HIGH);

    /**
     * The digging graphic.
     */
    private static final Graphics DIG_GRAPHIC = new Graphics(572);

    /**
     * The dig up animation.
     */
    private static final Animation DIG_UP_ANIMATION = new Animation(3315, Priority.VERY_HIGH);

    /**
     * The digging graphic.
     */
    private static final Graphics DIG_UP_GRAPHIC = new Graphics(573);

    /**
     * The dust graphics.
     */
    private static final Graphics DUST_GRAPHIC = new Graphics(571);

    /**
     * If the NPC is digging.
     */
    private boolean digging;

    /**
     * Constructs a new {@code GiantMoleNPC} {@code Object}.
     */
    public GiantMoleNPC() {
        super(3340, null);
    }

    /**
     * Constructs a new {@code GiantMoleNPC} {@code Object}.
     *
     * @param id       The NPC id.
     * @param location The location.
     */
    public GiantMoleNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public void init() {
        super.init();
        super.setWalks(true);
        super.walkRadius = 128;
    }

    /**
     * Starts digging.
     */
    private void dig() {
        digging = true;
        lock(5);
        getProperties().getCombatPulse().stop();
        getWalkingQueue().reset();
        getLocks().lockMovement(5);
        final Direction dir = Direction.get(RandomUtil.randomize(4));
        faceLocation(getCenterLocation().transform(dir.getStepX() << 2, dir.getStepY() << 2, 0));
        setDirection(dir);
        int index = RandomUtil.randomize(DIG_LOCATIONS.length);
        Location dest = DIG_LOCATIONS[index];
        if (dest.withinDistance(getLocation())) {
            dest = DIG_LOCATIONS[(index + 1) % DIG_LOCATIONS.length];
        }
        final Location destination = dest;
        World.submit(new Pulse(1, this) {

            int count = 0;
            Location hole;

            @Override
            public boolean pulse() {
                if (count == 0) {
                    hole = visualizeDig(destination, true);
                } else if (count == 1) {
                    if (RandomUtil.RANDOM.nextBoolean()) {
                        splatterMud(hole);
                    }
                } else if (count == 3) {
                    getProperties().setTeleportLocation(destination);
                } else if (count == 4) {
                    visualizeDig(destination, false);
                    digging = false;
                    return true;
                }
                count++;
                return false;
            }
        });
    }

    /**
     * Handles the mud splattering.
     *
     * @param hole The hole location.
     */
    private void splatterMud(Location hole) {
        for (Player p : RegionManager.getLocalPlayers(getCenterLocation(), (size() >> 1) + 2)) {
            PacketRepository.send(Interface.class, new InterfaceContext(p, 548, 77, 226, true));
            LightSource s = LightSource.getActiveLightSource(p);
            if (s == null || s.isOpen()) {
                if (s != null) {
                    p.getActionSender().sendMessage("Your " + s.getName() + " seems to have been extinguished by the mud.");
                    int slot = p.getInventory().getSlot(s.getProduct());
                    if (slot > -1) {
                        p.getInventory().replace(new Item(s.getRaw().getId()), slot);
                    }
                }
                DarkZone.checkDarkArea(p);
            }
        }
        for (int i = 0; i < 1 + RandomUtil.randomize(3); i++) {
            Projectile.create(hole, hole.transform(-4 + RandomUtil.randomize(9), -4 + RandomUtil.randomize(9), 0),
                570, 0, 5, 45, 70, 5, 11).send();
        }
    }

    /**
     * Visualizes the digging.
     *
     * @param destination The destination of the mole.
     * @param underground If the mole is going underground.
     * @return The hole location.
     */
    private Location visualizeDig(Location destination, boolean underground) {
        Location offset = getCenterLocation();
        if (underground) {
            switch (getDirection()) {
                case NORTH:
                    offset = getLocation().transform(1, size() - 1, 0);
                    break;
                case EAST:
                    offset = getLocation().transform(size() - 1, 1, 0);
                    break;
                case WEST:
                    offset = getLocation().transform(0, 1, 0);
                    break;
                default:
                    offset = getLocation().transform(1, 0, 0);
                    break;
            }
        }
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                Graphics.send(DUST_GRAPHIC, offset.transform(x, y, 0));
            }
        }
        if (underground) {
            animate(DIG_ANIMATION);
            Graphics.send(DIG_GRAPHIC, offset);
        } else {
            animate(DIG_UP_ANIMATION);
            Graphics.send(DIG_UP_GRAPHIC, offset);
        }
        return offset;
    }

    @Override
    public void onImpact(final Entity entity, BattleState state) {
        if (!getLocks().isInteractionLocked()) {
            int chance = 15;
            if (getSkills().getLifepoints() < getSkills().getMaximumLifepoints() >> 1) {
                chance *= 3;
            }
            if (RandomUtil.randomize(100) < chance && inCombat()) {
                dig();
                return;
            }
        }
        super.onImpact(entity, state);
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style) {
        if (digging) {
            return false;
        }
        return super.isAttackable(entity, style);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
//		ComponentDefinition.put(568, new ComponentPlugin() {
//			@Override
//			public Plugin<Object> newInstance(Object arg) throws Throwable {
//				return this;
//			}
//
//			@Override
//			public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
//				if (button == 17) {
//					player.getProperties().setTeleportLocation(Location.create(1752, 5237, 0));
//					player.getActionSender().sendMessage("You seem to have dropped down into a network of mole tunnels.");
//				}
//				player.getInterfaceState().close();
//				return false;
//			}
//		});
        DigAction action = new DigAction() {

            @Override
            public void run(Player player) {
//				if (!LightSource.hasActiveLightSource(player)) {
//					player.getActionSender().sendMessage("It's going to be dark down there, I should bring a light source.");
//					return;
//				}
//				player.getInterfaceState().open(new Component(568));
                player.getProperties().setTeleportLocation(Location.create(1752, 5237, 0));
                player.getActionSender().sendMessage("You seem to have dropped down into a network of mole tunnels.");
            }
        };
        DigSpadeHandler.register(Location.create(3005, 3376, 0), action);
        DigSpadeHandler.register(Location.create(2999, 3375, 0), action);
        DigSpadeHandler.register(Location.create(2996, 3377, 0), action);
        DigSpadeHandler.register(Location.create(2989, 3378, 0), action);
        DigSpadeHandler.register(Location.create(2984, 3387, 0), action);
        DigSpadeHandler.register(Location.create(2987, 3387, 0), action);
        ObjectDefinition.forId(12230).getConfigurations().put("option:climb", new OptionHandler() {

            @Override
            public Plugin<Object> newInstance(Object arg) throws Throwable {
                return this;
            }

            @Override
            public boolean handle(final Player player, Node node, String option) {
                player.animate(Animation.create(828));
                player.lock(2);
                World.submit(new Pulse(1, player) {

                    @Override
                    public boolean pulse() {
                        player.getProperties().setTeleportLocation(Location.create(2985, 3316, 0));
                        return true;
                    }
                });
                return true;
            }
        });
        return super.newInstance(arg);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new GiantMoleNPC(id, location);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            killer.asPlayer().getSavedData().getBossKillLog().increaseGiantMoleKills(1);
        }
    }

    @Override
    public int[] getIds() {
        return new int[]{ 3340 };
    }

}
