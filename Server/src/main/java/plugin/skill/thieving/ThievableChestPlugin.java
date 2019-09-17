package plugin.skill.thieving;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.ChanceItem;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;
import org.gielinor.utilities.string.TextUtils;

/**
 * Handles the thieving of chests.
 *
 * @author Vexia
 *         TODO Clean up
 */
public final class ThievableChestPlugin extends OptionHandler {

    /**
     * The doors to pick.
     */
    private static final int[] DOORS = new int[]{ 2550, 2551, 2554, 2555, 2556, 2557, 2558, 2559, 5501, 7246, 9565, 13314, 13317, 13320, 13323, 13326, 13344, 13345, 13346, 13347, 13348, 13349, 15759, 34005, 34805, 34806, 34812 };

    /**
     * The list of pickable doors.
     */
    private static final List<PickableDoor> PICKABLE_DOORS = new ArrayList<>();

    /**
     * The lock pick item.
     */
    private static final Item LOCK_PICK = new Item(1523);

    /**
     * The last chat of Rogues.
     */
    private static int lastChat;

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int i : DOORS) {
            ObjectDefinition.forId(i).getConfigurations().put("option:pick-lock", this);
            ObjectDefinition.forId(i).getConfigurations().put("option:open", this);
        }
        for (Chest chest : Chest.values()) {
            for (int id : chest.getObjectIds()) {
                ObjectDefinition def = ObjectDefinition.forId(id);
                def.getConfigurations().put("option:open", this);
                def.getConfigurations().put("option:search for traps", this);
            }
        }
        PICKABLE_DOORS.add(new PickableDoor(new Location[]{ Location.create(2674, 3305, 0) }, 1, 3.8));
        PICKABLE_DOORS.add(new PickableDoor(new Location[]{ Location.create(2674, 3304, 0) }, 14, 15));
        PICKABLE_DOORS.add(new PickableDoor(new Location[]{ Location.create(2610, 3316, 0) }, 15, 15));
        PICKABLE_DOORS.add(new PickableDoor(new Location[]{ Location.create(3190, 3957, 0) }, 32, 25, true));
        PICKABLE_DOORS.add(new PickableDoor(new Location[]{ Location.create(2565, 3356, 0) }, 46, 37.5));
        PICKABLE_DOORS.add(new PickableDoor(new Location[]{ Location.create(2579, 3286, 1) }, 61, 50));
        PICKABLE_DOORS.add(new PickableDoor(new Location[]{ Location.create(3018, 3187, 0) }, 1, 0.0));
        PICKABLE_DOORS.add(new PickableDoor(new Location[]{ Location.create(2601, 9482, 0) }, 82, 0.0, true));
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final Chest chest = Chest.forId(node.getId());
        PickableDoor door = null;
        if (chest == null) {
            door = forDoor(node.getLocation());
            if (door == null) {
                player.getActionSender().sendMessage("The door is locked.");
                return true;
            }
        }
        switch (option) {
            case "open":
                if (chest != null) {
                    chest.open(player, (GameObject) node);
                    return true;
                }
                door.open(player, (GameObject) node);
                return true;
            case "pick-lock":
                assert door != null;
                door.pickLock(player, (GameObject) node);
                return true;
            case "search for traps":
                assert chest != null;
                chest.searchTraps(player, (GameObject) node);
                return true;
        }
        return true;
    }

    /**
     * Gets a pickable door.
     *
     * @param location the location.
     * @return the door.
     */
    private PickableDoor forDoor(Location location) {
        for (PickableDoor door : PICKABLE_DOORS) {
            for (Location l : door.getLocations()) {
                if (l.equals(location)) {
                    return door;
                }
            }
        }
        return null;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n instanceof GameObject) {
            GameObject object = (GameObject) n;
            if (object.getDefinition().hasAction("pick-lock")) {
                return DoorActionHandler.getDestination((Entity) node, object);
            }
        }
        return null;
    }

    /**
     * Represents a pickable door.
     *
     * @author Vexia
     */
    public class PickableDoor {

        /**
         * The locations of the door.
         */
        private final Location[] locations;

        /**
         * The level.
         */
        private final int level;

        /**
         * The experience required.
         */
        private final double experience;

        /**
         * If it requires a lockpick.
         */
        private final boolean lockpick;

        /**
         * Constructs a new {@code PickableDoor} {@code Object}.
         *
         * @param locations  the locations.
         * @param level      the level.
         * @param experience the experience.
         * @param lockpick   the lock pick.
         */
        public PickableDoor(final Location[] locations, int level, double experience, boolean lockpick) {
            this.locations = locations;
            this.level = level;
            this.experience = experience;
            this.lockpick = lockpick;
        }

        /**
         * Constructs a new {@code PickableDoor} {@code Object}.
         *
         * @param locations  The locations.
         * @param level      The level.
         * @param experience The experience.
         */
        public PickableDoor(Location[] locations, int level, double experience) {
            this(locations, level, experience, false);
        }

        /**
         * Gets the location.
         *
         * @return The location.
         */
        public Location[] getLocations() {
            return locations;
        }

        /**
         * Opens a pickable door.
         *
         * @param player the player.
         * @param object the object.
         */
        public void open(Player player, GameObject object) {
            if (isInside(player, object)) {
                DoorActionHandler.handleAutowalkDoor(player, object);
                player.getActionSender().sendMessage("You go through the door.");
            } else {
                player.getActionSender().sendMessage("The door is locked.");
            }
        }

        /**
         * Picks a lock on a door.
         *
         * @param player the player.
         * @param object the object.
         */
        public void pickLock(Player player, GameObject object) {
            boolean success = RandomUtil.random(12) >= 4;
            if (isInside(player, object)) {
                player.getActionSender().sendMessage("The door is already unlocked.");
                return;
            }
            if (player.getSkills().getLevel(Skills.THIEVING) < level) {
                player.getActionSender().sendMessage("You attempt to pick the lock.");
                boolean hit = RandomUtil.random(10) < 5;
                player.getImpactHandler().manualHit(player, RandomUtil.random(1, 3), HitsplatType.NORMAL);
                player.getActionSender().sendMessage(hit ? "You have activated a trap on the lock." : "You fail to pick the lock.");
                return;
            }
            if (lockpick && !player.getInventory().containsItem(LOCK_PICK)) {
                player.getActionSender().sendMessage("You need a lockpick in order to pick this lock.");
                return;
            }
            if (success) {
                player.getSkills().addExperience(Skills.THIEVING, experience);
                DoorActionHandler.handleAutowalkDoor(player, object);
            }
            player.getActionSender().sendMessage("You attempt to pick the lock.");
            player.getActionSender().sendMessage("You " + (success ? "manage" : "fail") + " to pick the lock.");
        }

        /**
         * Checks if we're behind the door/inside the building.
         *
         * @param player the player.
         * @param object the object.
         * @return {@code True} if so.
         */
        private boolean isInside(Player player, GameObject object) {
            boolean inside = false;
            Direction dir = Direction.getLogicalDirection(player.getLocation(), object.getLocation());
            Direction direction = object.getDirection();
            if (direction == Direction.SOUTH && dir == Direction.WEST) {
                inside = true;
            } else if (direction == Direction.EAST && dir == Direction.SOUTH) {
                inside = true;
            } else if (direction == Direction.NORTH && dir == Direction.EAST) {
                inside = true;
            }
            return inside;
        }

        /**
         * Gets the level.
         *
         * @return The level.
         */
        public int getLevel() {
            return level;
        }

        /**
         * Gets the experience.
         *
         * @return The experience.
         */
        public double getExperience() {
            return experience;
        }

        /**
         * Gets the lockpick.
         *
         * @return The lockpick.
         */
        public boolean isLockpick() {
            return lockpick;
        }

    }

    /**
     * Represents a thievable chest.
     *
     * @author Vexia
     */
    public static enum Chest {

        TEN_COIN(2566, 13, 7.8, new Item[]{ new Item(Item.COINS, 10) }, 7),
        NATURE_RUNE(2567, 28, 25, new Item[]{ new Item(Item.COINS, 3), new Item(561, 1) }, 8),
        FIFTY_COIN(2568, 43, 125, new Item[]{ new Item(Item.COINS, 50) }, 55),
        STEEL_ARROWHEADS(2573, 47, 150, new Item[]{ new Item(41, 5) }, 210),
        BLOOD_RUNES(2569, 59, 250, new Item[]{ new Item(Item.COINS, 500), new Item(565, 2) }, 135),
        PALADIN(2570, 72, 500, new Item[]{ new Item(Item.COINS, 1000), new Item(383, 1), new Item(449, 1), new Item(1623, 1) }, 120),
        ROGUES_CASTLE(75, 84, 100, new ChanceItem[]{
            new ChanceItem(Item.COINS, 1000, 30),
            new ChanceItem(591, 3, 30), // Tinderbox
            new ChanceItem(560, 15, 30), // Raw Tuna
            new ChanceItem(1622, 5, 30), // Uncut Emerald
            new ChanceItem(1624, 5, 30), // Uncut Sapphire
            new ChanceItem(593, 25, 30), // Ashes
            new ChanceItem(558, 30, 30), // Mind Rune
            new ChanceItem(554, 30, 70), // Fire Rune
            new ChanceItem(560, 30, 70), // Death Rune
            new ChanceItem(562, 30, 70), // Chaos Rune
            new ChanceItem(1602, 3, 70), // Diamond
            new ChanceItem(441, 10, 70), // Iron Ore
            new ChanceItem(454, 13, 70), // Coal
            new ChanceItem(352, 10, 70), // Pike
            new ChanceItem(533, 10, 70), // Big Bones
            new ChanceItem(985, 1, 97), // Loop half
            new ChanceItem(987, 1, 97), // Key half
            new ChanceItem(386, 10, 97), // Shark
            new ChanceItem(1616, 2, 97), // Dragonstone
        }, 60);

        /**
         * The object id.
         */
        private final int[] objectIds;

        /**
         * The level required.
         */
        private final int level;

        /**
         * The experience gained.
         */
        private final double experience;

        /**
         * The rewards.
         */
        private final Item[] rewards;

        /**
         * The respawn time.
         */
        private final int respawn;

        /**
         * The current respawn time.
         */
        private int currentRespawn;

        /**
         * Constructs a new {@code Chest} {@code Object}.
         *
         * @param objectIds  the object ids.
         * @param level      the level.
         * @param experience the experience.
         * @param rewards    the rewards.
         * @param respawn    the respawn time.
         */
        private Chest(int[] objectIds, int level, double experience, Item[] rewards, int respawn) {
            this.objectIds = objectIds;
            this.level = level;
            this.experience = experience;
            this.rewards = rewards;
            this.respawn = respawn;
        }

        /**
         * Constructs a new {@code Chest} {@code Object}.
         *
         * @param objectId   the object id.
         * @param level      the level.
         * @param experience the experience.
         * @param rewards    the rewards.
         * @param respawn    the respawn time.
         */
        private Chest(int objectId, int level, double experience, Item[] rewards, int respawn) {
            this(new int[]{ objectId }, level, experience, rewards, respawn);
        }

        /**
         * Opens the chest for a reward.
         *
         * @param player the player.
         * @param object the object.
         */
        private void open(final Player player, final GameObject object) {
            if (isRespawning()) {
                player.getActionSender().sendMessage("It looks like this chest has already been looted.");
                if (object.getId() == 75) {
                    sendGuardAttack(player);
                }
                return;
            }
            player.lock(2);
            if (object.getId() == 75) {
                searchTraps(player, object);
                return;
            }
            player.getActionSender().sendMessage("You have activated a trap on the chest.");
            player.getImpactHandler().manualHit(player, getHitAmount(player), HitsplatType.NORMAL);
        }

        /**
         * Searches for traps on a chest.
         *
         * @param player the player.
         * @param object the object.
         */
        private void searchTraps(final Player player, final GameObject object) {
            player.faceLocation(object.getLocation());
            if (isRespawning()) {
                if (object.getId() == 76) {
                    sendGuardAttack(player);
                }
                player.getActionSender().sendMessage("It looks like this chest has already been looted.");
                return;
            }
            player.lock();
            player.animate(Animation.create(536));
            if (object.getId() == 75) {
                sendGuardAttack(player);
            }
            if (player.getSkills().getLevel(Skills.THIEVING) < level) {
                player.lock(2);
                player.getActionSender().sendMessage("You search the chest for traps.", 1);
                player.getActionSender().sendMessage("You find nothing.", 1);
                return;
            }
            if (player.getInventory().freeSlots() == 0) {
                player.getActionSender().sendMessage("Not enough inventory space.");
                return;
            }
            player.getActionSender().sendMessage("You find a trap on the chest...", 1);
            player.getImpactHandler().setDisabledTicks(6);
            World.submit(new Pulse(1, player) {

                int counter;

                @Override
                public boolean pulse() {
                    switch (++counter) {
                        case 2:
                            player.getActionSender().sendMessage("You disable the trap.", 1);
                            break;
                        case 4:
                            player.animate(Animation.create(536));
                            player.getActionSender().sendMessage("You open the chest.", 1);
                            break;
                        case 6:
                            player.unlock();
                            if (object.getId() == 75) {
                                ChanceItem loot = getChanceItem((ChanceItem[]) rewards);
                                player.getInventory().add(loot.getCount() == 1 ? loot : new Item(loot.getId(), RandomUtil.random(loot.getMinimumAmount(), loot.getMaximumAmount())));
                                String lootAmount = loot.getCount() > 1 ? "some" : (TextUtils.isPlusN(loot.getName()) ? "an" : "a");
                                String lootSuffix = loot.getName().equalsIgnoreCase("ashes") ? "" : loot.getName().equalsIgnoreCase("coins") ? "" :
                                    loot.getName().toLowerCase().contains("bones") ? "" : loot.getName().equalsIgnoreCase("tinderbox") ? "es" : "s";
                                player.getActionSender().sendMessage("You find " + lootAmount + " " + loot.getName().toLowerCase() + lootSuffix + " inside.");
                            } else {
                                for (Item i : rewards) {
                                    player.getInventory().add(i, player);
                                }
                                player.getActionSender().sendMessage("You find treasure inside!");
                            }
                            player.getSkills().addExperience(Skills.THIEVING, experience);
                            if (object.isActive()) {
                                ObjectBuilder.replace(object, object.getId() == 75 ? object.transform(76) : object.transform(2574), 3);
                            }
                            setRespawn(player, object);
                            return true;
                    }
                    return false;
                }
            });
        }

        /**
         * Gets the chance item from the array.
         *
         * @param items the items.
         * @return the chance item.
         */
        private ChanceItem getChanceItem(ChanceItem[] items) {
            final int chance = RandomUtil.random(100);
            final List<ChanceItem> chances = new ArrayList<>();
            for (ChanceItem c : items) {
                if (chance > c.getChanceRate()) {
                    chances.add(c);
                }
            }
            return chances.size() == 0 ? items[0] : chances.get(RandomUtil.random(chances.size()));
        }

        /**
         * Sets the respawn delay.
         *
         * @param player The player.
         * @param object The object.
         */
        public void setRespawn(final Player player, final GameObject object) {
            Location objectLocation = object.getLocation();
            currentRespawn = World.getTicks() + (int) (respawn / 0.6);
        }

        /**
         * Checks if the chest is respawning.
         *
         * @return {@code True} if so.
         */
        public boolean isRespawning() {
            return currentRespawn > World.getTicks();
        }

        /**
         * Gets the amount of damage to deal.
         *
         * @param player The player.
         * @return The amount of damage.
         */
        protected static int getHitAmount(Player player) {
            int hit = player.getSkills().getLifepoints() / 12;
            if (hit < 2) {
                hit = 2;
            }
            return hit;
        }

        /**
         * Sends the rogue gaurd attack to the player thieving.
         *
         * @param player The player.
         */
        private void sendGuardAttack(Player player) {
            if (World.getTicks() > lastChat) {
                RegionManager.getLocalNpcs(player.getLocation(), 8).stream().filter(npc ->
                    !npc.getProperties().getCombatPulse().isAttacking()
                        && npc.getId() == 187).forEach(npc -> {
                    npc.sendChat("Someone's stealing from us, get them!");
                });
                lastChat = World.getTicks() + 25;
            }
            RegionManager.getLocalNpcs(player.getLocation(), 8).stream().filter(npc ->
                !npc.getProperties().getCombatPulse().isAttacking()
                    && npc.getId() == 187).forEach(npc -> {
                npc.getProperties().getCombatPulse().attack(player);
            });
        }

        /**
         * Gets a chest by the id.
         *
         * @param id the id.
         * @return the chest.
         */
        public static Chest forId(int id) {
            for (Chest chest : values()) {
                for (int i : chest.getObjectIds()) {
                    if (i == id) {
                        return chest;
                    }
                }
            }
            return null;
        }

        /**
         * Gets the objectId.
         *
         * @return The objectId.
         */
        public int[] getObjectIds() {
            return objectIds;
        }

        /**
         * Gets the level.
         *
         * @return The level.
         */
        public int getLevel() {
            return level;
        }

        /**
         * Gets the experience.
         *
         * @return The experience.
         */
        public double getExperience() {
            return experience;
        }

        /**
         * Gets the rewards.
         *
         * @return The rewards.
         */
        public Item[] getRewards() {
            return rewards;
        }

        /**
         * Gets the respawn.
         *
         * @return The respawn.
         */
        public int getRespawn() {
            return respawn;
        }

    }
}
