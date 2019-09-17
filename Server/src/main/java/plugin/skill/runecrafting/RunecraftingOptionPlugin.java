package plugin.skill.runecrafting;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.global.action.ClimbActionHandler;
import org.gielinor.game.content.skill.free.firemaking.FiremakingPulse;
import org.gielinor.game.content.skill.free.gather.SkillingTool;
import org.gielinor.game.content.skill.free.runecrafting.*;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the option plugin used to handle node interactions related to the runecrafting skill.
 *
 * @author 'Vexia
 * @date 01/11/2013
 */
public class RunecraftingOptionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (Altar altar : Altar.values()) {
            ObjectDefinition.forId(altar.getObject()).getConfigurations().put("option:craft-rune", this);
            ObjectDefinition.forId(altar.getObject()).getConfigurations().put("option:bind", this);
            ObjectDefinition.forId(altar.getPortal()).getConfigurations().put("option:use", this);
        }
        for (MysteriousRuin ruin : MysteriousRuin.values()) {
            for (int i : ruin.getObject()) {
                ObjectDefinition.forId(i).getConfigurations().put("option:enter", this);
            }
        }
        for (Talisman talisman : Talisman.values()) {
            ItemDefinition.forId(talisman.getTalisman().getId()).getConfigurations().put("option:locate", this);
        }
        for (Pouch pouch : Pouch.values()) {
            ItemDefinition.forId(pouch.getPouch().getId()).getConfigurations().put("option:fill", this);
            ItemDefinition.forId(pouch.getPouch().getId()).getConfigurations().put("option:empty", this);
            ItemDefinition.forId(pouch.getPouch().getId()).getConfigurations().put("option:check", this);
        }
        for (Rift rift : Rift.values()) {
            ObjectDefinition.forId(rift.getObject()).getConfigurations().put("option:exit-through", this);
        }
        for (AbbysalObstacle obstacle : AbbysalObstacle.values()) {
            for (int i : obstacle.getObjects()) {
                ObjectDefinition.forId(i).getConfigurations().put("option:" + obstacle.getOption(), this);
            }
        }
        NPCDefinition.forId(2259).getConfigurations().put("option:teleport", this);
        NPCDefinition.forId(2262).getConfigurations().put("option:repair-pouches", this);
        ObjectDefinition.forId(26849).getConfigurations().put("option:climb", this);
        ObjectDefinition.forId(26850).getConfigurations().put("option:climb", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        Pouch pouch = node.getName().contains("pouch") ? Pouch.forItem(((Item) node)) : null;
        switch (option) {
            case "climb":
                int id = node.getId();
                switch (id) {
                    case 26849:
                        ClimbActionHandler.climb(player, null, new Location(3271, 4861, 0));
                        break;
                    case 26850:
                        ClimbActionHandler.climb(player, null, new Location(2452, 3230, 0));
                        break;
                }
                break;
            case "fill":
            case "empty":
            case "check":
                if (pouch == null) {
                    break;
                }
                pouch.action(player, option);
                //http://www.youtube.com/watch?v=8ln_nczz9Ls
                break;
            case "use":
                final Altar altar = Altar.forObject(((GameObject) node));
                player.getProperties().setTeleportLocation(altar.getRuin().getBase());
                break;
            case "craft-rune":
                if (node.getId() == 17010 && (node.getLocation().getX() != 2157 && node.getLocation().getY() != 3863)) {
                    player.getActionSender().sendMessage("You can only craft these runes at the altar on Lunar Isle.");
                    break;
                }
                player.getPulseManager().run(new RuneCraftPulse(player, null, Altar.forObject(((GameObject) node)), false, null));
                break;
            case "bind":
                if (node.getId() == 17010 && (node.getLocation().getX() != 2157 && node.getLocation().getY() != 3863)) {
                    player.getActionSender().sendMessage("You can only craft these runes at the altar on Lunar Isle.");
                    break;
                }
                player.getPulseManager().run(new RuneCraftPulse(player, null, Altar.forObject(((GameObject) node)), false, null));
                break;
            case "locate":
                final Talisman talisman = Talisman.forItem(((Item) node));
                talisman.locate(player);
                break;
            case "enter":
                final MysteriousRuin ruin = MysteriousRuin.forObject(((GameObject) node));
                if (ruin == null) {
                    return true;
                }
                if (ruin.getTiara().getTiara().getId() != player.getEquipment().get(Equipment.SLOT_HAT).getId()) {
                    return true;
                }
                player.getProperties().setTeleportLocation(ruin.getEnd());
                player.getActionSender().sendMessage("You feel a powerful force take hold of you...", 1);
                break;
            case "exit-through"://abbys
                final Rift rift = Rift.forObject(((GameObject) node));
                if (rift.getAltar().getRuin() == null) {
                    player.getActionSender().sendMessage("A strange power blocks your exit.", 1);
                    return true;
                }
                player.getProperties().setTeleportLocation(rift.getAltar().getRuin().getEnd());
                break;
            case "repair-pouches":
                break;
            case "teleport":
                teleport(player, ((NPC) node));
                break;
            case "mine":
            case "chop":
            case "squeeze-through":
            case "distract":
            case "go-through":
            case "burn-down":
                final AbbysalObstacle obstacle = AbbysalObstacle.forObject(((GameObject) node));
                obstacle.handle(player, ((GameObject) node));
                break;

        }
        return true;
    }

    /**
     * Handles the teleporting to the abyss.
     *
     * @param player the player.
     */
    public static void teleport(final Player player, NPC npc) {
        player.lock(3);
        npc.animate(new Animation(1979));
        npc.graphics(Graphics.create(4));
        npc.sendChat("Veniens! Sallakar! Rinnesset!");
        player.getSkills().decrementPrayerPoints(100);
        player.getSkullManager().checkSkull(player);
        World.submit(new Pulse(2, player) {

            @Override
            public boolean pulse() {
                player.getProperties().setTeleportLocation(Location.create(3021, 4847, 0));
                return true;
            }
        });
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    @Override
    public boolean isWalk(final Player player, final Node node) {
        return !(node instanceof Item);
    }

    /**
     * Represents an abyssal rift.
     *
     * @author 'Vexia
     * @date 02/11/2013
     */
    public enum Rift {
        AIR(25378, Altar.AIR),
        MIND(25379, Altar.MIND),
        WATER(25376, Altar.WATER),
        EARTH(24972, Altar.EARTH),
        FIRE(24971, Altar.FIRE),
        BODY(24973, Altar.BODY),
        COSMIC(24974, Altar.COSMIC),
        CHAOS(24976, Altar.CHAOS),
        ASTRAL(0, Altar.ASTRAL),
        NATURE(24975, Altar.NATURE),
        LAW(25034, Altar.LAW),
        DEATH(25035, Altar.DEATH),
        BLOOD(25380, Altar.BLOOD),
        SOUL(25377, Altar.SOUL);

        /**
         * Constructs a new {@code RunecraftingOptionPlugin} {@code Object}.
         *
         * @param object the object.
         * @param altar  the altar.
         */
        Rift(int object, Altar altar) {
            this.object = object;
            this.altar = altar;
        }

        /**
         * Represents the object.
         */
        private final int object;

        /**
         * Represents the altar to teleport to.
         */
        private final Altar altar;

        /**
         * Gets the object.
         *
         * @return The object.
         */
        public int getObject() {
            return object;
        }

        /**
         * Gets the altar.
         *
         * @return The altar.
         */
        public Altar getAltar() {
            return altar;
        }

        /**
         * Method used to get the object rift.
         *
         * @param object the object.
         * @return the <code>Rift</code> or <code>Null</code>.
         */
        public static Rift forObject(final GameObject object) {
            for (Rift rift : values()) {
                if (rift.getObject() == object.getId()) {
                    return rift;
                }
            }
            return null;
        }
    }

    /**
     * Represents an obstacle in an abbsyal.
     *
     * @author 'Vexia
     * @date 02/11/2013
     */
    public enum AbbysalObstacle {
        BOIL("burn-down", new Location[]{ Location.create(3024, 4833, 0), Location.create(3053, 4830, 0) }, 26190, 26252) {
            @Override
            public void handle(final Player player, final GameObject object) {
                if (!player.getInventory().contains(FiremakingPulse.TINDERBOX)) {
                    player.getActionSender().sendMessage("You don't have a tinderbox to burn it.");
                    return;
                }
                player.animate(new Animation(733));
                player.lock(3);
                World.submit(new Pulse(1, player) {

                    int count = 0;

                    @Override
                    public boolean pulse() {
                        switch (count) {
                            case 1:
                                player.getActionSender().sendMessage("You attempt to burn your way through..");
                                break;
                            case 4:
                                if (RandomUtil.random(3) != 1) {
                                    player.getActionSender().sendMessage("...and manage to burn it down and get past.");
                                    player.getProperties().setTeleportLocation(getLocations()[getIndex(object)]);
                                    return true;
                                } else {
                                    player.getActionSender().sendMessage("You fail to set it on fire.");
                                    return true;
                                }
                        }
                        count++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        player.animate(new Animation(-1, Priority.HIGH));
                    }
                });
            }
        },
        MINE("mine", new Location[]{ Location.create(3030, 4821, 0), Location.create(3048, 4822, 0) }, 26188, 26574) {
            @Override
            public void handle(final Player player, final GameObject object) {
                final SkillingTool tool = setTool(true, player);
                if (tool == null) {
                    player.getActionSender().sendMessage("You need a pickaxe in order to do that.");
                    return;
                }
                player.animate(tool.getAnimation());
                player.lock(3);
                World.submit(new Pulse(1, player) {

                    int count = 0;

                    @Override
                    public boolean pulse() {
                        switch (count) {
                            case 1:
                                player.getActionSender().sendMessage("You attempt to mine your way through..");
                                break;
                            case 4:
                                if (RandomUtil.random(3) != 1) {
                                    player.getActionSender().sendMessage("...and manage to break through the rock.");
                                    player.getProperties().setTeleportLocation(getLocations()[getIndex(object)]);
                                    return true;
                                } else {
                                    player.getActionSender().sendMessage("...but fail to break-up the rock.");
                                    return true;
                                }
                        }
                        count++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        player.animate(new Animation(-1, Priority.HIGH));
                    }
                });
            }
        },
        CHOP("chop", new Location[]{ Location.create(3050, 4824, 0), Location.create(3028, 4824, 0) }, 26253, 26189) {
            @Override
            public void handle(final Player player, final GameObject object) {
                final SkillingTool tool = setTool(false, player);
                if (tool == null) {
                    player.getActionSender().sendMessage("You need an axe in order to do that.");
                    return;
                }
                player.animate(tool.getAnimation());
                player.lock(3);
                World.submit(new Pulse(1, player) {

                    int count = 0;

                    @Override
                    public boolean pulse() {
                        switch (count) {
                            case 1:
                                player.getActionSender().sendMessage("You attempt to chop your way through...");
                                break;
                            case 4:
                                if (RandomUtil.random(3) != 1) {
                                    player.getActionSender().sendMessage("...and manage to chop down the tendrils.");
                                    player.getProperties().setTeleportLocation(getLocations()[getIndex(object)]);
                                    return true;
                                } else {
                                    player.getActionSender().sendMessage("You fail to cut through the tendrils.");
                                    return true;
                                }
                        }
                        count++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        player.animate(new Animation(-1, Priority.HIGH));
                    }
                });
            }
        },
        SQUEEZE("squeeze-through", new Location[]{ Location.create(3048, 4842, 0), Location.create(3031, 4842, 0) }, 26192, 26208, 26250) {
            @Override
            public void handle(final Player player, final GameObject object) {
                player.animate(new Animation(1331));
                player.lock(3);
                player.lock(3);
                World.submit(new Pulse(1, player) {

                    int count = 0;

                    @Override
                    public boolean pulse() {
                        switch (count) {
                            case 1:
                                player.getActionSender().sendMessage("You attempt to squeeze through the narrow gap...");
                                break;
                            case 2:
                                player.getActionSender().sendMessage("...and you manage to crawl through.");
                                player.getProperties().setTeleportLocation(getLocations()[getIndex(object)]);
                                return true;
                        }
                        count++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        player.animate(new Animation(-1, Priority.HIGH));
                    }
                });
            }
        },
        DISTRACT("distract", new Location[]{ Location.create(3029, 4841, 0), Location.create(3051, 4838, 0) }, 26191, 26251) {
            @Override
            public void handle(final Player player, final GameObject object) {
                int[] emotes = { 855, 856, 857, 858, 859, 860, 861, 862, 863, 864, 865, 866, 2113, 2109, 2111, 2106, 2107, 2108, 0x558, 2105, 2110, 2112, 0x84F, 0x850, 1131, 1130, 1129, 1128, 1745, 3544, 3543, 2836 };
                int index = RandomUtil.random(emotes.length);
                player.animate(new Animation(emotes[index]));
                player.lock(3);
                World.submit(new Pulse(1, player) {

                    int count = 0;

                    @Override
                    public boolean pulse() {
                        switch (count) {
                            case 1:
                                player.getActionSender().sendMessage("You use your thieving skills to misdirect the eyes...");
                                break;
                            case 4:
                                if (RandomUtil.random(3) != 1) {
                                    player.getActionSender().sendMessage("...and sneak past while they're not looking.");
                                    player.getProperties().setTeleportLocation(getLocations()[getIndex(object)]);
                                } else {
                                    player.getActionSender().sendMessage("You fail to distract the eyes.");
                                    return true;
                                }
                                return true;
                        }
                        count++;
                        return false;
                    }

                    @Override
                    public void stop() {
                        super.stop();
                        player.animate(new Animation(-1, Priority.HIGH));
                    }
                });
            }
        },
        PASSAGE("go-through", new Location[]{ Location.create(3040, 4844, 0) }, 7154) {
            @Override
            public void handle(final Player player, final GameObject object) {
                player.getProperties().setTeleportLocation(getLocations()[0]);
            }
        };

        /**
         * Constructs a new {@code RunecraftingOptionPlugin} {@code Object}.
         *
         * @param locations the locations.
         * @param objects   the objects.
         */
        AbbysalObstacle(final String option, Location[] locations, int... objects) {
            this.option = option;
            this.objects = objects;
            this.locations = locations;
            this.option = option;
        }

        /**
         * Represents the option.
         */
        private String option;

        /**
         * Represents the corssing location.
         */
        private final Location[] locations;

        /**
         * Represents the object id.
         */
        private final int[] objects;

        /**
         * Gets the locations.
         *
         * @return The locations.
         */
        public Location[] getLocations() {
            return locations;
        }

        /**
         * Gets the objects.
         *
         * @return The objects.
         */
        public int[] getObjects() {
            return objects;
        }

        /**
         * Method used to get the abbysal obstacle.
         *
         * @param object the object.
         * @return the <code>AbbysalObstacle</code> or <code>Null</code>.
         */
        public static AbbysalObstacle forObject(final GameObject object) {
            for (AbbysalObstacle obstacle : values()) {
                for (int i : obstacle.getObjects()) {
                    if (i == object.getId()) {
                        return obstacle;
                    }
                }
            }
            return null;
        }

        /**
         * Method used to get the index.
         *
         * @param object the object.
         * @return the index.
         */
        public int getIndex(final GameObject object) {
            for (int i = 0; i < objects.length; i++) {
                if (getObjects()[i] == object.getId()) {
                    return i;
                }
            }
            return 0;
        }

        /**
         * Methhod used to handle the obstacle.
         *
         * @param player the player.
         * @param object the object.
         */
        public void handle(final Player player, final GameObject object) {

        }

        /**
         * Gets the option.
         *
         * @return The option.
         */
        public String getOption() {
            return option;
        }

        /**
         * Sets the tool used.
         */
        private static SkillingTool setTool(final boolean mining, final Player player) {
            SkillingTool tool = null;
            if (!mining) {
                if (checkTool(player, SkillingTool.DRAGON_AXE)) {
                    tool = SkillingTool.DRAGON_AXE;
                } else if (checkTool(player, SkillingTool.RUNE_AXE)) {
                    tool = SkillingTool.RUNE_AXE;
                } else if (checkTool(player, SkillingTool.ADAMANT_AXE)) {
                    tool = SkillingTool.ADAMANT_AXE;
                } else if (checkTool(player, SkillingTool.MITHRIL_AXE)) {
                    tool = SkillingTool.MITHRIL_AXE;
                } else if (checkTool(player, SkillingTool.BLACK_AXE)) {
                    tool = SkillingTool.BLACK_AXE;
                } else if (checkTool(player, SkillingTool.STEEL_AXE)) {
                    tool = SkillingTool.STEEL_AXE;
                } else if (checkTool(player, SkillingTool.IRON_AXE)) {
                    tool = SkillingTool.IRON_AXE;
                } else if (checkTool(player, SkillingTool.BRONZE_AXE)) {
                    tool = SkillingTool.BRONZE_AXE;
                }
            } else {
                if (checkTool(player, SkillingTool.RUNE_PICKAXE)) {
                    tool = SkillingTool.RUNE_PICKAXE;
                } else if (checkTool(player, SkillingTool.ADAMANT_PICKAXE)) {
                    tool = SkillingTool.ADAMANT_PICKAXE;
                } else if (checkTool(player, SkillingTool.MITHRIL_PICKAXE)) {
                    tool = SkillingTool.MITHRIL_PICKAXE;
                } else if (checkTool(player, SkillingTool.STEEL_PICKAXE)) {
                    tool = SkillingTool.STEEL_PICKAXE;
                } else if (checkTool(player, SkillingTool.IRON_PICKAXE)) {
                    tool = SkillingTool.IRON_PICKAXE;
                } else if (checkTool(player, SkillingTool.BRONZE_PICKAXE)) {
                    tool = SkillingTool.BRONZE_PICKAXE;
                }
            }
            return tool;
        }

        /**
         * Checks if the player has a tool and if he can use it.
         *
         * @param tool The tool.
         * @return {@code True} if the tool is usable.
         */
        private static boolean checkTool(final Player player, SkillingTool tool) {
            if (player.getEquipment().contains(tool.getId(), 1)) {
                return true;
            }
            return player.getInventory().contains(tool.getId(), 1);
        }
    }
}
