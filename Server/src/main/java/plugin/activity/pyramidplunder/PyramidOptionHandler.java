package plugin.activity.pyramidplunder;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.ClimbActionHandler;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.impl.ForceMovement;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the pyramid plunder options.
 *
 * @author Emperor
 */
public final class PyramidOptionHandler extends OptionHandler {

    /**
     * The experience modification.
     */
    private double mod = Constants.NON_COMBAT_EXP_MODIFIER;

    /**
     * The room locations.
     */
    public static Location[] ROOM_LOCATIONS = {
        Location.create(1927, 4477, 0), // Level 21 - index 0
        Location.create(1954, 4477, 0), // Level 31 - index 1
        Location.create(1977, 4471, 0), // Level 41 - index 2
        Location.create(1927, 4453, 0), // Level 51 - index 3
        Location.create(1965, 4444, 0), // Level 61 - index 4
        Location.create(1927, 4424, 0), // Level 71 - index 5
        Location.create(1943, 4421, 0), // Level 81 - index 6
    };

    /**
     * Represents the animation to use when passing a spear trap or unlocking a door.
     */
    private static final Animation DISABLE_ANIMATION = new Animation(2246);

    /**
     * The guardian room location.
     */
    private static final Location GUARDIAN_ROOM = Location.create(1968, 4420, 2);

    /**
     * The empty room location.
     */
    private static final Location EMPTY_ROOM = Location.create(1934, 4450, 2);

    /**
     * The current entrance.
     */
    private static int currentEntrance;

    /**
     * The last entrance switch made.
     */
    private static long lastEntranceSwitch;

    /**
     * The current tomb door.
     */
    private static int currentTombDoor;

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(16458).getConfigurations().put("option:leave tomb", this);
        ObjectDefinition.forId(16459).getConfigurations().put("option:leave tomb", this);
        ObjectDefinition.forId(16473).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(16475).getConfigurations().put("option:pick-lock", this);
        ObjectDefinition.forId(16484).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(16485).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(16487).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(16488).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(16490).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(16491).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(16493).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(16494).getConfigurations().put("option:enter", this);
        ObjectDefinition.forId(16495).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(16501).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(16502).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(16503).getConfigurations().put("option:search", this);
        ObjectDefinition.forId(16517).getConfigurations().put("option:pass", this);
        NPCDefinition.forId(4476).getConfigurations().put("option:start-minigame", this);
        return null;
    }

    // you've had your 5 minutes of plundering. now be
    // off with you!

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (node instanceof GameObject) {
            GameObject object = (GameObject) node;
            switch (option) {
                case "pick-lock":
                    int room = (int) player.getAttribute("pyramidplunder:room");
                    player.lock(3);
                    player.faceLocation(object.getLocation());
                    currentTombDoor = RandomUtil.random(3);
                    int correctId = object.getId() - 16539;
                    player.animate(DISABLE_ANIMATION);
                    World.submit(new Pulse(2, player) {

                        @Override
                        public boolean pulse() {
                            player.animate(DISABLE_ANIMATION);
                            boolean success = success(player);
                            player.unlock();
                            player.getActionSender().sendMessage("You attempt to open the door. " + (player.getInventory().containItems(1523) ? "" : "Lockpicks would make it easier..."));
                            if (success) {
                                if (correctId == currentTombDoor) {
                                    if (player.getSkills().getLevel(Skills.THIEVING) >= getLevelRequired(player)) {
                                        enterRoom(player, room + 1);
                                    } else {
                                        player.getActionSender().sendMessage("You do not have a high enough Thieving level to move on to the next room.");
                                    }
                                } else {
                                    player.getActionSender().sendMessage("The door leads to a dead end.");
                                }
                                World.submit(new Pulse(40, player) {

                                    @Override
                                    public boolean pulse() {
                                        return true;
                                    }
                                });
                            } else {
                                player.getActionSender().sendMessage("Your attempt fails.");
                            }
                            return true;
                        }
                    });
                    return true;
                case "pass": // Spear trap
                    player.lock(3);
                    player.faceLocation(object.getLocation());
                    if (player.getAttribute("pptrap:passed") != null) {
                        return true;
                    }
                    player.animate(DISABLE_ANIMATION);
                    World.submit(new Pulse(2, player) {

                        @Override
                        public boolean pulse() {
                            player.animate(DISABLE_ANIMATION);
                            boolean success = success(player);
                            player.unlock();
                            player.getActionSender().sendMessage("You carefully try to temporarily deactivate the trap mechanism.");
                            if (success) {
                                boolean x = player.getLocation().getX() == 1929 || player.getLocation().getX() == 1930;
                                boolean yAdd = player.getLocation().getY() == 4427;
                                player.getActionSender().sendMessage("You deactivate the trap!");
                                player.setAttribute("pptrap:passed", true);
                                player.getSkills().addExperience(Skills.THIEVING, 10 * mod);
                                ForceMovement.run(player, player.getLocation(), player.getLocation().transform(x ? +2 : 0, x ? 0 : (yAdd ? +2 : -2), 0), new Animation(7191), 10);
                                World.submit(new Pulse(40, player) {

                                    @Override
                                    public boolean pulse() {
                                        return true;
                                    }
                                });
                            } else {
                                player.getActionSender().sendMessage("You fail to deactivate the trap.");
                            }
                            return true;
                        }
                    });
                    return true;
                case "leave tomb":
                    switch (object.getId()) {
                        case 16458: // Leave main room
                        case 16459:
                            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location.create(3288, 2801, 0));
                            return true;
                    }
                    return true;
                // you attempt to open the door lockpicks would make it easier...
                // your attempt fails
                // the door leads to a dead end.
                case "search":
                    switch (object.getId()) {
                        case 16473: // Golden chest.
                        case 16474:
                            handleSearching(player, object);
                            return true;
                        case 16484: // Enter main room
                        case 16487:
                        case 16490:
                        case 16493:
                            Location destination = EMPTY_ROOM;
                            if (System.currentTimeMillis() - lastEntranceSwitch > 15 * 60 * 1000) {
                                currentEntrance = RandomUtil.random(4);
                                lastEntranceSwitch = System.currentTimeMillis();
                            }
                            int entrance = (object.getId() - 16483) / 3;
                            int value = 0;
                            if (entrance == currentEntrance) {
                                destination = GUARDIAN_ROOM;
                                value = 4;
                            }
                            player.getConfigManager().set(704, value);
                            player.getActionSender().sendMessage("You use your thieving skills to search the stone panel.");
                            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, destination, "You find a door! You open it.");
                            return true;
                    }
                    return true;
            }
        } else if (node instanceof NPC) {
            player.getDialogueInterpreter().open(node.getId(), node, 1);
        }
        return true;
    }

    /**
     * Handles searching an urn or chest.
     *
     * @param player The player.
     * @param object The object.
     */
    private void handleSearching(Player player, GameObject object) {
        player.lock(3);
        player.faceLocation(object.getLocation());
        if (object.getId() == 16473 || object.getId() == 16474) {
            player.animate(new Animation(4238));
        } else {
            player.animate(new Animation(4340));
        }
        World.submit(new Pulse(2, player) {

            @Override
            public boolean pulse() {
                boolean success = success(player);
                player.unlock();
                if (object.getId() == 16473 || object.getId() == 16474) {
                    if (success) {
                        player.getConfigManager().set(821, player.getAttribute("ppsarc:open") != null ? 1 : 4);
                        if (RandomUtil.random(1000) == 1) {
                            if (!player.getInventory().add(new Item(9044))) {
                                GroundItemManager.create(new Item(9044), player.getLocation(), player);
                            }
                        }
                        World.submit(new Pulse(40, player) {

                            @Override
                            public boolean pulse() {
                                return true;
                            }
                        });
                    } else {
                        if (object.getId() == 16473) {
                            NPC scarab = NPC.create(4500, player.getLocation());
                            scarab.setRespawn(false);
                            scarab.init();
                            scarab.getProperties().getCombatPulse().attack(player);
                            if (!player.isActive() || player.getLocation().getDistance(scarab.getLocation()) > 10) {
                                World.submit(new Pulse(2) {

                                    @Override
                                    public boolean pulse() {
                                        scarab.clear();
                                        return true;
                                    }
                                });
                            }
                        }
                    }
                } else {
                    if (success) {
                        player.animate(new Animation(4342));
                        player.getActionSender().sendMessage("You successfully loot the urn!");
                        World.submit(new Pulse(40, player) {

                            @Override
                            public boolean pulse() {
                                return true;
                            }
                        });
                    } else {
                        player.animate(new Animation(4341));
                        player.getActionSender().sendMessage("You've been bitten by something moving inside the urn.");
                        player.getImpactHandler().manualHit(player, 4, ImpactHandler.HitsplatType.NORMAL);
                        player.sendChat("Ow!");
                    }
                }
                return true;
            }
        });
    }

    /**
     * Gets the correct tomb door to move on.
     *
     * @param player The player.
     * @param object The object id.
     * @return
     */
    private int getTombDoor(Player player, GameObject object) {
        int roll = RandomUtil.random(3);
        int correctId = object.getId() - 16539;
        if (roll == correctId) {
            return object.getId();
        }
        return 16540;
    }

    /**
     * Enters a room.
     *
     * @param player The player.
     * @param index  The index.
     */
    public void enterRoom(Player player, int index) {
        player.lock(1);
        player.getProperties().setTeleportLocation(ROOM_LOCATIONS[index]);
        PyramidPlunderActivity.sendRoomConfiguration(player, index + 1, getLevelRequired(player) - 1);
        player.removeAttribute("ppsarc:open");
        player.removeAttribute("pptrap:passed");
        player.getConfigManager().set(821, 0);
        player.getConfigManager().set(820, 0);
    }

    /**
     * Gets the level required to enter the next room.
     *
     * @param player The player.
     * @return
     */
    private int getLevelRequired(Player player) {
        switch ((int) player.getAttribute("pyramidplunder:room") + 1) {
            case 1:
                return 31;
            case 2:
                return 41;
            case 3:
                return 51;
            case 4:
                return 61;
            case 5:
                return 71;
            case 6:
                return 81;
        }
        return 21;
    }

    /**
     * Gets the chance of succeeding for a trap / urn.
     *
     * @param player The player to check for.
     * @return
     */
    private boolean success(Player player) {
        double level = player.getSkills().getLevel(Skills.THIEVING);
        double req = getLevelRequired(player);
        double successChance = Math.ceil((level * 50 - req * 15) / req / 3 * 4);
        if (Perk.SLEIGHT_OF_HAND.enabled(player)) {
            successChance += RandomUtil.random(30, 45);
            if (RandomUtil.random(5) == 1) {
                return true;
            }
        }
        int roll = RandomUtil.random(99);
        if (RandomUtil.random(12) < 2) {
            return false;
        }
        if (successChance >= roll) {
            return true;
        }
        return false;
    }

}
