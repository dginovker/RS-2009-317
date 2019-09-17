package org.gielinor.game.content.skill.free.prayer;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.prayer.ecto.Bonemeal;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.GlobalData;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.pulse.Pulse;

import plugin.skill.prayer.BoneBuryingOptionPlugin.Bones;

/**
 * Represents the Ectofuntus main class for Prayer offerings.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class Ectofuntus {

    /**
     * The empty pot.
     */
    public static final int EMPTY_POT = 1931;
    /**
     * The empty bucket.
     */
    public static final int EMPTY_BUCKET = 1925;
    /**
     * An ecto token.
     */
    public static final int ECTO_TOKEN = 4278;
    /**
     * A bucket of slime.
     */
    public static final int BUCKET_OF_SLIME = 4286;
    /**
     * The ectophial.
     */
    public static final int ECTOPHIAL = 4251;
    /**
     * The hopper object.
     */
    public static final int HOPPER = 16654;
    /**
     * The grinder object.
     */
    public static final int GRINDER = 16655;
    /**
     * The bin object.
     */
    public static final int BIN = 16656;
    /**
     * The laoder object.
     */
    private static final GameObject LOADER_OBJECT = RegionManager.getObject(HOPPER, 1, 3660, 3525);

    /**
     * Worships the Ectofuntus.
     *
     * @param player The player worshipping.
     */
    public static void handleWorship(Player player) {
        if (!player.getInventory().contains(BUCKET_OF_SLIME, 1)) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a bucket of slime to do that!");
            return;
        }
        player.lock(2);
        boolean hasBonemeal = false;
        for (Item item : player.getInventory().toArray()) {
            if (item == null) {
                continue;
            }
            Bonemeal bone = Bonemeal.forMealId(item.getId());
            if (bone != null) {
                hasBonemeal = true;
                break;
            }
        }
        if (!hasBonemeal) {
            player.getDialogueInterpreter().sendPlaneMessage("You do not have any bonemeal.");
            return;
        }
        for (Item item : player.getInventory().toArray()) {
            if (item == null) {
                continue;
            }
            Bonemeal bone = Bonemeal.forMealId(item.getId());
            if (bone == null) {
                continue;
            }
            Bones b = Bones.forId(bone.getBoneId());
            if (b == null) {
                continue;
            }
            player.playAnimation(new Animation(1651));
            player.getInventory().remove(new Item(bone.getBonemealId()));
            player.getInventory().remove(new Item(BUCKET_OF_SLIME));
            player.getInventory().add(new Item(EMPTY_POT));
            player.getInventory().add(new Item(EMPTY_BUCKET));
            player.getInventory().add(new Item(4278, 5), player);
            player.getSkills().addExperience(Skills.PRAYER, b.getExperience() * 4);
            return;
        }
    }

    /**
     * Handles clicking any objects for the Ectofuntus bone meal creation process.
     *
     * @param player   The player.
     * @param objectId The id of the object clicked.
     * @return Whether or not it was handled.
     */
    public static boolean handleObjects(final Player player, final int objectId) {
        GlobalData globalData = player.getSavedData().getGlobalData();
        switch (objectId) {
            /**
             * Trapdoor.
             */
            case 16113: {
                player.playAnimation(new Animation(828));
                World.submit(new Pulse(1, player) {

                    @Override
                    public boolean pulse() {
                        player.setTeleportTarget(Location.create(3669, 9888, 3));
                        return true;
                    }
                });
            }
            return true;

            /**
             * Ladder.
             */
            case 16110: {
                player.playAnimation(new Animation(828));
                World.submit(new Pulse(1, player) {

                    @Override
                    public boolean pulse() {
                        player.setTeleportTarget(Location.create(3654, 3519, 0));
                        return true;
                    }
                });
            }
            return true;

            /**
             * Weathered wall.
             */
            case 16526: {
                if (player.getSkills().getLevel(Skills.AGILITY) < 53) {
                    player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of at least 53 to do that.");
                    return true;
                }
                player.playAnimation(new Animation(828));
                World.submit(new Pulse(1, player) {

                    @Override
                    public boolean pulse() {
                        player.setTeleportTarget(Location.create(3671, 9888, 2));
                        return true;
                    }
                });
            }
            return true;

            case 16525: {
                if (player.getSkills().getLevel(Skills.AGILITY) < 53) {
                    player.getDialogueInterpreter().sendPlaneMessage("You need an Agility level of at least 53 to do that.");
                    return true;
                }
                player.playAnimation(new Animation(828));
                World.submit(new Pulse(1, player) {

                    @Override
                    public boolean pulse() {
                        player.setTeleportTarget(Location.create(3670, 9888, 3));
                        return true;
                    }
                });
            }
            return true;

            /**
             * Stairs (down).
             */
            case 5263:
                if (player.getLocation().getZ() == 3) {
                    player.setTeleportTarget(Location.create(3688, 9888, 2));
                }
                if (player.getLocation().getZ() == 2) {
                    player.setTeleportTarget(Location.create(3675, 9887, 1));
                }
                if (player.getLocation().getZ() == 1) {
                    player.setTeleportTarget(Location.create(3683, 9888, 0));
                }
                return true;

            /**
             * Stairs (up).
             */
            case 5262:
                if (player.getLocation().getZ() == 2) {
                    player.setTeleportTarget(Location.create(3692, 9888, 3));
                }
                if (player.getLocation().getZ() == 1) {
                    player.setTeleportTarget(Location.create(3671, 9888, 2));
                }
                if (player.getLocation().getZ() == 0) {
                    player.setTeleportTarget(Location.create(3687, 9888, 1));
                }
                return true;

            case 16648:
                handleWorship(player);
                return true;

            case GRINDER:
                player.lock(3);
                if (globalData.getEctofuntusBoneType() != 0 && !globalData.isEctofuntusBonesGround()) {
                    player.getActionSender().sendMessage("You turn the grinder, some crushed bones fall into the bin.", 1);
                    player.playAnimation(new Animation(1648));
                    globalData.setEctofuntusBonesGround(true);
                } else {
                    player.playAnimation(new Animation(1648));
                }
                return true;

            case BIN:
                if (globalData.getEctofuntusBoneType() == 0) {
                    player.getActionSender().sendMessage("You need to put some bones in the hopper and grind them first.");
                    return true;
                }
                if (!globalData.isEctofuntusBonesGround()) {
                    player.getActionSender().sendMessage("You need to grind the bones by turning the grinder first.");
                    return true;
                }
                if (!player.getInventory().contains(EMPTY_POT)) {
                    player.getActionSender().sendMessage("You need an empty pot to fill with the crushed bones.");
                    return true;
                }
                if (globalData.getEctofuntusBoneType() != 0 && globalData.isEctofuntusBonesGround()) {
                    Bonemeal meal = Bonemeal.forBoneId(globalData.getEctofuntusBoneType());
                    if (meal != null) {
                        player.getActionSender().sendMessage("You fill an empty pot with bones.", 1);
                        player.playAnimation(new Animation(1650));
                        player.getInventory().remove(new Item(EMPTY_POT));
                        player.getInventory().add(new Item(meal.getBonemealId()));
                        globalData.setEctofuntusBoneType(0);
                        globalData.setEctofuntusBonesGround(false);
                    } else {
                        globalData.setEctofuntusBoneType(0);
                    }
                }
                return true;
        }
        return false;
    }

    /**
     * Handles using an item on an object.
     *
     * @param player   The player.
     * @param itemId   The id of the item.
     * @param objectId The id of the object.
     * @return Whether or not it was handled.
     */
    public static boolean handleItemOnObject(final Player player, int itemId, int objectId) {
        ObjectDefinition objectDefinition = ObjectDefinition.forId(objectId);
        if (objectDefinition == null) {
            return false;
        }
        ItemDefinition itemDefinition = ItemDefinition.forId(itemId);
        if (itemDefinition == null) {
            return false;
        }

        if (itemId == EMPTY_BUCKET && objectDefinition.getName().equalsIgnoreCase("Pool of Slime")) {
            World.submit(new Pulse(1, player) {

                @Override
                public boolean pulse() {
                    if (!player.getInventory().contains(new Item(Ectofuntus.EMPTY_BUCKET))) {
                        return true;
                    }
                    player.playAnimation(new Animation(4471));
                    player.getInventory().remove(new Item(Ectofuntus.EMPTY_BUCKET));
                    player.getInventory().add(new Item(Ectofuntus.BUCKET_OF_SLIME));
                    return !player.getInventory().contains(new Item(Ectofuntus.EMPTY_BUCKET));
                }
            });
            return true;
        }

        if (itemDefinition.getName().toLowerCase().contains("bone") && objectId == HOPPER) {
            if (player.getSavedData().getGlobalData().getEctofuntusBoneType() != 0) {
                player.getActionSender().sendMessage("You already have some bones in the hopper.");
                return true;
            }
            Bonemeal meal = Bonemeal.forBoneId(itemId);
            if (meal != null) {
                player.lock(3);
                player.getSavedData().getGlobalData().setEctofuntusBoneType(meal.getBoneId());
                player.getActionSender().sendMessage("You put the bones in the hopper.", 1);
                player.playAnimation(new Animation(1649));
                player.getInventory().remove(new Item(meal.getBoneId()));
                player.getPulseManager().run(new Pulse(3) {

                    @Override
                    public boolean pulse() {
                        GlobalData globalData = player.getSavedData().getGlobalData();
                        boolean grinder = !globalData.isEctofuntusBonesGround();
                        if (globalData.getEctofuntusBoneType() == 0 && player.getInventory().contains(itemId)) {
                            Bonemeal meal = Bonemeal.forBoneId(itemId);
                            if (meal == null) {
                                return grinder;
                            }
                            UseWithHandler.run(new NodeUsageEvent(player, 0, new Item(itemId), LOADER_OBJECT));
                            return false;
                        }
                        Location location = Location.create(3659, 3525, 1);
                        GameObject gameObject = RegionManager.getObject(location);
                        gameObject.getInteraction().handle(player, gameObject.getInteraction().get(0));
                        this.setDelay(5);
                        return false;
                    }
                });
            } else {
                player.getSavedData().getGlobalData().setEctofuntusBoneType(0);
            }
        }
        return false;
    }


}
