package plugin.interaction.object.dmc;

import org.gielinor.game.interaction.Interaction;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.zone.ZoneRestriction;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.task.impl.LogoutTask;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles a player's Dwarf Multi-cannon.
 *
 * @author Torchic
 * @author Emperor
 */
public final class DMCHandler {

    /**
     * The player.
     */
    private final Player player;

    /**
     * The cannon object.
     */
    private GameObject cannon;

    /**
     * The amount of cannonballs loaded.
     */
    private int cannonballs;

    /**
     * The logout plugin.
     */
    private Plugin<Player> logoutPlugin;

    /**
     * The firing pulse.
     */
    private Pulse firingPulse;

    /**
     * The current direction.
     */
    private DMCRevolution direction = DMCRevolution.NORTH;

    private final int maxCannonballs;
    private final boolean autoReload;

    /**
     * Constructs a new {@code DMCHandler} {@code Object}
     *
     * @param player The player owning the cannon.
     */
    public DMCHandler(final Player player) {
        this.player = player;
        // TODO TICKS
        maxCannonballs = player.getDonorManager().getDonorStatus().getMaxCannonballs();
        autoReload = player.getDonorManager().getDonorStatus().getAutoReloadCannon();
        this.firingPulse = new Pulse(2, player) {

            @Override
            public boolean pulse() {
                if (!cannon.isActive()) {
                    return true;
                }
                findTarget();
                return rotate();
            }
        };

        firingPulse.stop();
    }

    public DMCHandler() {
        this.player = null;
        maxCannonballs = 30;
        autoReload = false;
    }

    /**
     * Rotates the cannon.
     *
     * @return {@code True} if the cannon should stop rotating.
     */
    private boolean rotate() {
        if (cannonballs < 1) {
            if (autoReload && cannon.getLocation().withinDistance(player.getLocation())) {
                startFiring();
                player.getActionSender().sendMessage("Due to your membership status, your cannon auto-reloads!");
                return false;
            }
            player.getActionSender().sendMessage("Your cannon is out of cannonballs.");
            return true;
        }
        player.getActionSender().sendObjectAnimation(cannon, Animation.create(direction.getAnimationId()), false);
        direction = DMCRevolution.values()[(direction.ordinal() + 1) % DMCRevolution.values().length];
        return false;
    }

    /**
     * Finds a target for the cannon.
     */
    private void findTarget() {
        NPC target = targetNPC(player, new Location(cannon.getLocation().getX(), cannon.getLocation().getY(), cannon.getLocation().getZ()));
        int damage = RandomUtil.getRandom(30);
        if (target != null) {
            if (player.getProperties().isMultiZone()) {
                target.getImpactHandler().handleImpact(target, damage, CombatStyle.RANGE, true);
                target.setInteraction(new Interaction(player));
                target.attack(player);
            } else {
                target.getImpactHandler().handleImpact(target, damage, CombatStyle.RANGE, true);
                target.setInteraction(new Interaction(player));
                target.attack(player);
            }

            // Projectile.create(cannon.getCenterLocation(), target.getLocation(), 53,
            // 15 + (2 * 10), 50, 50 + (2 * 10), 37, 37,
            // 0, 96);
//            Location start, Location finish, int id,
//            int delay, int angle, int speed, int startHeight, int endHeight,
//            int lockon, int slope, int radius
            Projectile.send(Projectile.create(cannon.getCenterLocation(), target.getLocation(), 53, 52, 52, 0, 37, 0, 96));
            cannonballs--;
        }
    }

    /**
     * Represents the target NPC for the cannon.
     *
     * @param player   The player.
     * @param location The location.
     * @return The target NPC.
     */
    private NPC targetNPC(Player player, Location location) {
        for (NPC npc : RegionManager.getLocalNpcs(cannon.getLocation())) {
            if (npc == null) {
                continue;
            }
            if (!npc.getDefinition().hasAttackOption()) {
                continue;
            }
            int cannonX = cannon.getLocation().getX();
            int cannonY = cannon.getLocation().getY();
            int targetX = npc.getLocation().getX();
            int targetY = npc.getLocation().getY();
            int targetBase = npc.getLocation().getZ();
            Location npcLocation = new Location(targetX, targetY, targetBase);
            if (npcLocation.withinDistance(cannon.getLocation(), 5)) {
                if (!npc.isInvisible() && !npc.isHidden() && !npc.inCombat() && npc.getId() != 1266 && npc.getId() != 1268 && npc.getId() != 6400) {
                    switch (direction) {
                        case NORTH:
                            if (targetY > cannonY && targetX >= cannonX - 1 && targetX <= cannonX + 1) {
                                return npc;
                            }
                            break;
                        case NORTH_EAST:
                            if (targetX >= cannonX + 1 && targetY >= cannonY + 1) {
                                return npc;
                            }
                            break;
                        case EAST:
                            if (targetX > cannonX && targetY >= cannonY - 1 && targetY <= cannonY + 1) {
                                return npc;
                            }
                            break;
                        case SOUTH_EAST:
                            if (targetY <= cannonY - 1 && targetX >= cannonX + 1) {
                                return npc;
                            }
                            break;
                        case SOUTH:
                            if (targetY < cannonY && targetX >= cannonX - 1 && targetX <= cannonX + 1) {
                                return npc;
                            }
                            break;
                        case SOUTH_WEST:
                            if (targetX <= cannonX - 1 && targetY <= cannonY - 1) {
                                return npc;
                            }
                            break;
                        case WEST:
                            if (targetX < cannonX && targetY >= cannonY - 1 && targetY <= cannonY + 1) {
                                return npc;
                            }
                            break;
                        case NORTH_WEST:
                            if (targetX <= cannonX - 1 && targetY >= cannonY + 1) {
                                return npc;
                            }
                            break;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Starts rotating the cannon.
     */
    public void startFiring() {
        if (cannon == null || !cannon.isActive()) {
            player.getActionSender().sendMessage("You don't have a cannon set up.");
            return;
        }
        if (firingPulse.isRunning()) {
            if (cannonballs < maxCannonballs) {
                int amount = player.getInventory().getCount(new Item(2)) - cannonballs;
                if (amount <= 0) {
                    return;
                }
                if (amount > maxCannonballs) {
                    amount = maxCannonballs;
                }
                cannonballs += amount;
                player.getActionSender().sendMessage("You load the cannon with " + amount + " cannonball" + (amount > 1 ? "s." : "."));
                player.getInventory().remove(new Item(2, amount));
                return;
            }
            player.getActionSender().sendMessage("Your cannon is already firing.");
            return;
        }
        if (cannonballs < 1) {
            int amount = player.getInventory().getCount(new Item(2));
            if (amount < 1) {
                player.getActionSender().sendMessage("Your cannon is out of cannonballs.");
                return;
            }
            if (amount > maxCannonballs) {
                amount = maxCannonballs;
            }
            cannonballs = amount;
            player.getActionSender().sendMessage("You load the cannon with " + amount + " cannonball" + (amount > 1 ? "s." : "."));
            player.getInventory().remove(new Item(2, amount));
        }
        firingPulse.restart();
        firingPulse.start();
        World.submit(firingPulse);
    }

    /**
     * Constructs the cannon.
     */
    public static void construct(final Player player) {
        // TODO Cannons within distance
        final Location spawn = RegionManager.getSpawnLocation(player, new GameObject(6, player.getLocation()));
        if (spawn == null) {
            player.getActionSender().sendMessage("There's not enough room for your cannon.");
            return;
        }
        if (player.getZoneMonitor().isRestricted(ZoneRestriction.CANNON)) {
            player.getActionSender().sendMessage("You can't set up a cannon here.");
            return;
        }
        final DMCHandler handler = new DMCHandler(player);
        player.setAttribute("dmc", handler);
        player.lock(9);
        player.faceLocation(spawn.transform(Direction.NORTH_EAST));
        World.submit(new Pulse(2, player) {

            int count = 0;
            GameObject object = null;

            @Override
            public boolean pulse() {
                player.animate(Animation.create(827));
                if (!player.getInventory().remove(new Item(6 + (count * 2)))) {
                    for (int i = count - 1; i >= 0; i--) {
                        player.getInventory().add(new Item(6 + (i * 2)));
                    }
                    if (object != null) {
                        ObjectBuilder.remove(object);
                    }
                    return true;
                }
                player.addExtension(LogoutTask.class, new LogoutTask() {

                    int amount = count + 1;

                    @Override
                    public void run(Player player) {
                        for (int i = 0; i < amount; i++) {
                            player.getInventory().add(new Item(6 + (i * 2)));
                        }
                        if (object != null) {
                            ObjectBuilder.remove(object);
                        }
                    }
                });
                switch (count) {
                    case 0:
                        object = ObjectBuilder.add(new GameObject(7, spawn));
                        player.getActionSender().sendMessage("You place the cannon base on the ground.");
                        return count++ == 666;
                    case 3:
                        ObjectBuilder.remove(object);
                        handler.configure(ObjectBuilder.add(object = object.transform(6)));
                        return true;
                }
                ObjectBuilder.remove(object);
                ObjectBuilder.add(object = object.transform(object.getId() + 1));
                return ++count == 4;
            }
        });
    }

    /**
     * Configures the cannon.
     *
     * @param cannon The cannon.
     */
    public void configure(GameObject cannon) {
        this.cannon = cannon;
        player.removeExtension(LogoutTask.class);
//        player.getLogoutPlugins().add(logoutPlugin = new Plugin<Player>() {
//            @Override
//            public Plugin<Player> newInstance(Player arg) throws Throwable {
//                clear(false);
//                return this;
//            }
//
//            @Override
//            public Object fireEvent(String identifier, Object... args) {
//                return null;
//            }
//        });
    }

    /**
     * Clears the cannon.
     *
     * @param pickup If the cannon is getting picked up.
     */
    public void clear(boolean pickup) {
        ObjectBuilder.remove(cannon);
        player.removeAttribute("dmc");
//        player.getLogoutPlugins().remove(logoutPlugin);
//        if (!pickup) {
//            player.getSavedData().getActivityData().setLostCannon(true);
//            return;
//        }
        // TODO Return cannonballs
        for (int i = 0; i < 4; i++) {
            player.getInventory().add(new Item(12 - (i * 2)));
        }
        player.getInventory().add(new Item(2, cannonballs - 1), true);
    }

    /**
     * Gets the player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the cannon.
     *
     * @return the cannon
     */
    public GameObject getCannon() {
        return cannon;
    }

    /**
     * Sets the cannon.
     *
     * @param cannon the cannon to set.
     */
    public void setCannon(GameObject cannon) {
        this.cannon = cannon;
    }

    /**
     * Gets the cannonballs.
     *
     * @return the cannonballs
     */
    public int getCannonballs() {
        return cannonballs;
    }

    /**
     * Sets the bacannonballs.
     *
     * @param cannonballs the cannonballs to set.
     */
    public void setCannonballs(int cannonballs) {
        this.cannonballs = cannonballs;
    }

}
