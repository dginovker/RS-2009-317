package org.gielinor.game.node.entity.combat;

import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.impl.Animator;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.impl.WildernessZone;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.ContainerType;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.task.NodeTask;

/**
 * Handles an entity death task.
 *
 * @author Emperor
 */
public final class DeathTask extends NodeTask {

    /**
     * The death task singleton.
     */
    public static final DeathTask SINGLETON = new DeathTask();

    /**
     * Constructs a new {@code DeathTask} {@Code Object}.
     */
    private DeathTask() {
        super(1);
    }

    @Override
    public void start(Node node, Node... n) {
        Entity e = (Entity) node;
        e.getWalkingQueue().reset();
        e.setAttribute("state:death", true);
        e.setAttribute("tick:death", World.getTicks());
        e.lock(50);
        Entity killer = n.length > 0 ? (Entity) n[0] : e;
        if (e instanceof NPC) {
            killer.removeAttribute("combat-time");
        }
        e.getAnimator().forceAnimation(e.getProperties().getDeathAnimation());
        e.graphics(Animator.RESET_G);
        e.commenceDeath(killer);
        e.getImpactHandler().setDisabledTicks(50);
        e.face(null);
    }

    @Override
    public boolean run(Node node, Node... n) {
        Entity e = (Entity) node;
        int ticks = e.getProperties().getDeathAnimation().getDuration() - 1;
        if (e instanceof NPC && e.asNpc().getName().toLowerCase().contains("bird")) {
            ticks = 3;
        }
        if (ticks < 1 || ticks > 20) { //Player death animation = 601 ticks.
            ticks = 4;
        }
        if (e.getProperties().getDeathAnimation().getId() == 9055) {
            ticks += 2;
        }
        return e.getAttribute("tick:death", -1) <= World.getTicks() - ticks;
    }

    @Override
    public void stop(Node node, Node... n) {
        Entity e = (Entity) node;
        Entity killer = n.length > 0 ? (Entity) n[0] : e;
        //Entity killer = null;
        // TODO 317 Check - This is for IRONMAN MODE
//		if (n.length > 1) {
//			for (Node node1 : n) {
//				if (!(node1 instanceof Player)) {
//					continue;
//				}
//				Player k = (Player) node1;
//				if (Ironman.isIronman(k)) {
//					continue;
//				}
//				killer = k;
//			}
//		} else if (n.length == 1) {
//			killer = e;
//		}
//		if (killer == null) {
//			for (Node node1 : n) {
//				if (node1 instanceof Player) {
//					continue;
//				}
//				killer = (Entity) node1;
//				break;
//			}
//		}
        e.removeAttribute("state:death");
        e.removeAttribute("tick:death");
        Location spawn = e.getProperties().getSpawnLocation();
        e.getAnimator().forceAnimation(Animator.RESET_A);
        e.getProperties().setTeleportLocation(spawn);
        e.unlock();
        e.getImpactHandler().getImpactLog().clear();
        e.finalizeDeath(killer);
        e.getImpactHandler().setDisabledTicks(4);
    }

    @Override
    public boolean removeFor(String s, Node node, Node... n) {
        return false;
    }

    /**
     * Gets the player's death containers.
     *
     * @param player The player.
     * @return The containers, index 0 = kept items, index 1 = lost items.
     */
    public static Container[] getContainers(Player player) {
        Container[] containers = new Container[2];
        Container wornItems = new Container(42, ContainerType.ALWAYS_STACK);
        wornItems.addAll(player.getInventory());
        wornItems.addAll(player.getEquipment());
        int count = 3;
        if (player.getSkullManager().isSkulled()) {
            count -= 3;
        }
        if (player.getPrayer().get(PrayerType.PROTECT_ITEMS) || player.getPrayer().get(PrayerType.CURSE_PROTECT_ITEMS)) {
            count += 1;
        }
        if (!WildernessZone.Companion.isInZone(player)) {
            count += player.getDonorManager().getDonorStatus().getItemsKeptOnDeathPVM();
        } else {
            count += player.getDonorManager().getDonorStatus().getItemsKeptOnDeathPVP();
        }
        Container keptItems = new Container(count, ContainerType.NEVER_STACK);
        containers[0] = keptItems;
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < 42; j++) {
                Item item = wornItems.get(j);
                if (item != null) {
                    for (int x = 0; x < count; x++) {
                        Item kept = keptItems.get(x);
                        if (kept == null || kept.getDefinition().getValue(true, true) <= item.getDefinition().getValue(true, true)) {
                            keptItems.replace(new Item(item.getId(), 1, item.getCharge()), x);
                            x++;
                            while (x < count) {
                                Item newKept = keptItems.get(x);
                                keptItems.replace(kept, x++);
                                kept = newKept;
                            }
                            if (kept != null) {
                                wornItems.add(kept, false);
                            }
                            item = wornItems.get(j);
                            wornItems.replace(new Item(item.getId(), item.getCount() - 1, item.getCharge()), j);
                            break;
                        }
                    }
                }
            }
        }
        containers[1] = new Container(42, ContainerType.DEFAULT);
        containers[1].addAll(wornItems);
        return containers;
    }

    /**
     * Starts the death task for an entity.
     *
     * @param entity The entity.
     * @param killer The entity's killer.
     */
    @SuppressWarnings("deprecation")
    public static void startDeath(Entity entity, Entity killer) {
        if (!isDead(entity)) {
            entity.getPulseManager().clear();
            if (killer == null) {
                killer = entity;
            }
            if (entity instanceof Player) {
                Player player = (Player) entity;
                NodeTask deathTask = player.getZoneMonitor().deathTaskOverride(killer);
                if (deathTask == null) {
                    deathTask = SINGLETON;
                }
                Pulse pulse = deathTask.schedule(entity, killer);
                pulse.start();
                entity.getPulseManager().set(pulse);
            } else {
                Pulse pulse = SINGLETON.schedule(entity, killer);
                pulse.start();
                entity.getPulseManager().set(pulse);
            }
        }
    }

    /**
     * Checks if the entity is dead.
     *
     * @param e The entity.
     * @return <code>True</code> if so.
     */
    public static boolean isDead(Entity e) {
        return e.getAttribute("state:death", false);
    }

    /**
     * Gets the singleton.
     *
     * @return The singleton.
     */
    public static DeathTask getSingleton() {
        return SINGLETON;
    }

}
