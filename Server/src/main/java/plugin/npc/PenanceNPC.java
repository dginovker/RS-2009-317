package plugin.npc;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.RegionZone;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.map.zone.ZoneBuilder;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents a Penance NPC in the Barbarian Assault mini-game.
 *
 * @author Logan G. <logan@Gielinor.org>
 *         TODO 317
 */
public final class PenanceNPC extends AbstractNPC {

    /**
     * The IDs of NPCs using this plugin.
     */
    private static final int[] ID = {
        5219, 5218, 5217, 5216, 5215, 5214, 5213, 5045,
        5044, 5237, 5236, 5235, 5234, 5233, 5232, 5231,
        5230, 5229
    };

    /**
     * Constructs a new {@code PenanceNPC.} {@code Object}.
     */
    public PenanceNPC() {
        super(0, null);
        ZoneBuilder.configure(BarbarianAssaultMapZone.getInstance());
        PluginManager.definePlugin(new BarbarianRewardDialogue());
    }

    @Override
    public void init() {
        super.init();
        setAggressive(true);
    }


    /**
     * Constructs a new {@code PenanceNPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     */
    private PenanceNPC(int id, Location location) {
        super(id, location, true);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new PenanceNPC(id, location);
    }

    @Override
    public int[] getIds() {
        return ID;
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            Player player = (Player) killer;
            int points = getName().contains("Ranger") ? RandomUtil.random(1, 3) : RandomUtil.random(3, 6);
            player.getSavedData().getActivityData().increaseBarbarianAssaultPoints(points);
            BarbarianAssaultMapZone.show(player);
        }
    }

    /**
     * @author
     */
    public final class BarbarianRewardDialogue extends DialoguePlugin {

        /**
         *
         */
        private BarbarianReward barbarianReward;

        public BarbarianRewardDialogue() {
        }


        public BarbarianRewardDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new BarbarianRewardDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            barbarianReward = (BarbarianReward) args[0];
            String points = barbarianReward == BarbarianReward.GRANITE_BODY ? " and 300k coins" : "";
            interpreter.sendDialogue("Buy " + barbarianReward.getItem().getName() + " for " +
                barbarianReward.cost + " points" + points + "?");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    options("Yes.", "No.");
                    stage = 1;
                    break;
                case 1:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            if (player.getSavedData().getActivityData().getBarbarianAssaultPoints() < barbarianReward.getCost()) {
                                interpreter.sendPlaneMessage("You do not have enough points to buy that.");
                                stage = 1000;
                                return true;
                            }
                            if (barbarianReward == BarbarianReward.GRANITE_BODY && !player.getInventory().contains(Item.COINS, 300000)) {
                                interpreter.sendPlaneMessage("You do not have enough coins to buy that.");
                                stage = 1000;
                                return true;
                            }
                            if (!player.getInventory().hasRoomFor(barbarianReward.getItem())) {
                                interpreter.sendPlaneMessage("You do not have enough space in your inventory for that.");
                                stage = 1000;
                                return true;
                            }
                            player.getSavedData().getActivityData().decreaseBarbarianAssaultPoints(barbarianReward.getCost());
                            if (barbarianReward == BarbarianReward.GRANITE_BODY) {
                                player.getInventory().remove(new Item(Item.COINS, 300000));
                            }
                            player.getInventory().add(barbarianReward.getItem());
                            interpreter.sendItemMessage(barbarianReward.getItem(), "You've purchased a " +
                                    barbarianReward.getItem().getName() + " for " + barbarianReward.getCost() + " points!",
                                "You have " + player.getSavedData().getActivityData().getBarbarianAssaultPoints() + " points left.");
                            stage = END;
                            return true;

                        case TWO_OPTION_TWO:
                            end();
                            openRewardScreen(player);
                            break;
                    }
                    break;
                case 1000:
                    end();
                    openRewardScreen(player);
                    break;
                case END:
                    player.removeAttribute("BARBARIAN_ASSAULT_REWARD");
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ DialogueInterpreter.getDialogueKey("BarbarianReward") };
        }
    }

    /**
     * Opens the reward screen for the player.
     *
     * @param player The player.
     */
    public static void openRewardScreen(Player player) {
        player.setAttribute("BARBARIAN_ASSAULT_REWARD", 1);
        int points = player.getSavedData().getActivityData().getBarbarianAssaultPoints();
        player.getActionSender().sendString(27259, "Barbarian Assault Rewards (" + points + " Available Points)");
        int index = 27167;
        int slot = 27197;
        for (int clearId = index; clearId < 27197; clearId++) {
            player.getActionSender().sendString(clearId, "");
        }
        for (int slotId = 0; slotId < 30; slotId++) {
            player.getActionSender().sendUpdateItem(slot + slotId, 0, null);
        }
        for (BarbarianReward barbarianReward : BarbarianReward.values()) {
            String color = points >= barbarianReward.getCost() ? "<col=1DA41D>" : "<col=C46603>";
            player.getActionSender().sendString(index, color + barbarianReward.getItem().getName() + "<br>" + color
                + barbarianReward.getText());
//            String text = barbarianReward.getText();
//            String levels = text.substring(text.indexOf("@")).replace("@", "");
//            String[] needed = levels.split(",");
//            String requiredLevels = "";
//            for (String need : needed) {
//                String[] info = need.split("_");
//                int skillId = Skills.getSkillByName(info[1]);
//                requiredLevels += ((player.getSkills().getLevel(skillId) >= Integer.parseInt(info[0])) ?
//                        "<col=1DA41D>" : "<col=C46603>") + info[0] + " " + info[1].toLowerCase() + " ";
//            }
//            text = text.replaceAll(levels, "<br>" + requiredLevels);
//            String color = points >= barbarianReward.getCost() ? "<col=1DA41D>" : "<col=C46603>";
//            player.getActionSender().sendString(index, color + barbarianReward.getItem().getName() + "<br>" + text);
            if (barbarianReward.getItem() != null) {
                player.getActionSender().sendUpdateItem(slot, 0, barbarianReward.getItem());
            }
            index++;
            slot++;
        }
        player.getInterfaceState().open(new Component(27135).setCloseEvent(new CloseEvent() {

            @Override
            public void close(Player player, Component component) {
                player.removeAttribute("BARBARIAN_ASSAULT_REWARD");
            }

            @Override
            public boolean canClose(Player player, Component component) {
                return true;
            }
        }));
    }

    /**
     * Represents a Barbarian Assault reward.
     *
     * @author <a href="http://Gielinor.org/">Gielinor</a>
     */
    public enum BarbarianReward {
        FIGHTER_TORSO("350 Points", new Item(10551), 350),
        GRANITE_BODY("200 Points and 300k coins", new Item(10564), 200),
        FIGHTER_HAT("150 Points", new Item(10548), 150),
        RANGER_HAT("150 Points", new Item(10550), 150),
        RUNNER_HAT("150 Points", new Item(10549), 150),
        HEALER_HAT("150 Points", new Item(10547), 150),
        RUNNER_BOOTS("100 Points", new Item(10552), 100),
        PENANCE_GLOVES("100 Points", new Item(10553), 100),
        PENANCE_SKIRT("100 Points", new Item(10555), 100);


        /**
         * The text to display.
         */
        private final String text;
        /**
         * The item reward.
         */
        private final Item item;
        /**
         * The cost of the reward.
         */
        private final int cost;

        BarbarianReward(String text, Item item, int cost) {
            this.text = text;
            this.item = item;
            this.cost = cost;
        }

        public String getText() {
            return text;
        }

        public Item getItem() {
            return item;
        }

        public int getCost() {
            return cost;
        }
    }

    /**
     * Represents the Barbarian Assault mini-game zone.
     *
     * @author Logan G. <logan@Gielinor.org>
     */
    public static final class BarbarianAssaultMapZone extends MapZone {

        /**
         * The Barbarian Assault map zone.
         */
        private static final BarbarianAssaultMapZone INSTANCE = new BarbarianAssaultMapZone(new ZoneBorders(1867, 5450, 1908, 5490));

        /**
         * The zone borders.
         */
        private ZoneBorders[] borders;

        /**
         * Constructs a new {@code BarbarianAssaultMapZone} {@code Object}.
         */
        public BarbarianAssaultMapZone(ZoneBorders... borders) {
            super("Barbarian Assault Map Zone", true);
            this.borders = borders;
        }

        @Override
        public void configure() {
            for (ZoneBorders border : borders) {
                register(border);
            }
        }

        @Override
        public boolean enter(Entity e) {
            if (e instanceof Player) {
                final Player player = (Player) e;
                show(player);
            }
            return true;
        }

        @Override
        public boolean leave(Entity e, boolean logout) {
            if (!logout && e instanceof Player) {
                Player p = (Player) e;
                leave(p);
            }
            return true;
        }

        /**
         * Method used to remove traces of being in the zone.
         *
         * @param player The player.
         */
        public final void leave(final Player player) {
            Component overlay = player.getInterfaceState().getOverlay();
            if (overlay != null && overlay.getId() == 255) {
                player.getInterfaceState().closeOverlay();
            }
        }

        /**
         * Method used to show being the wilderness.
         *
         * @param player The player.
         */
        public static void show(final Player player) {
            player.getInterfaceState().openOverlay(new Component(255));
            player.getActionSender().sendString(256, "Points: " + player.getSavedData().getActivityData().getBarbarianAssaultPoints());
        }

        @Override
        public void locationUpdate(Entity e, Location last) {
            if (e instanceof Player) {
                Player p = (Player) e;
                Component overlay = p.getInterfaceState().getOverlay();
                if (overlay == null || overlay.getId() != 255) {
                    show(p);
                }
            }
        }

        /**
         * Checks if the entity is inside the wilderness.
         *
         * @param e The entity.
         * @return {@code True} if so.
         */
        public static boolean isInZone(Entity e) {
            Location l = e.getLocation();
            for (RegionZone zone : e.getViewport().getRegion().getRegionZones()) {
                if (zone.getZone() == INSTANCE && zone.getBorders().insideBorder(l.getX(), l.getY())) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Gets the zone borders.
         *
         * @return The borders.
         */
        public ZoneBorders[] getBorders() {
            return borders;
        }

        /**
         * Gets the instance.
         *
         * @return The instance.
         */
        public static BarbarianAssaultMapZone getInstance() {
            return INSTANCE;
        }
    }
}
