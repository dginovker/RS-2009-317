package plugin.interaction.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.member.slayer.Equipment;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the plugin used to handle enchanted jewellery transportation.
 *
 * @author 'Vexia
 */
public final class EnchantedJewelleryPlugin extends OptionHandler {

    /**
     * Represents the teleport animation.
     */
    private static final Animation ANIMATION = new Animation(9603);

    /**
     * Represents the graphics to use.
     */
    private static final Graphics GRAPHICS = new Graphics(1684, 100, 0);

    /**
     * Represents the charge numbers.
     */
    private static final char[] NUMBERS = new char[]{ '1', '2', '3', '4', '5', '6', '7', '8' };

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        new JewelleryDialogue().init();
        for (EnchantedJewellery jewellery : EnchantedJewellery.values()) {
            for (int id : jewellery.getIds()) {
                ItemDefinition.forId(id).getConfigurations().put("option:rub", this);
                ItemDefinition.forId(id).getConfigurations().put("option:operate", this);
            }
        }
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final EnchantedJewellery jewellery = EnchantedJewellery.forItem((Item) node);
        if (jewellery.isLast(jewellery.getItemIndex((Item) node))) {
            player.getActionSender().sendMessage("The " + jewellery.getNameType((Item) node) + " has lost its charge.");
            player.getActionSender().sendMessage("It will need to be recharged before you can use it again.");
            return true;
        }
        player.getActionSender().sendMessage("You rub the " + jewellery.getNameType((Item) node) + "...");
        player.getDialogueInterpreter().open(JewelleryDialogue.ID, node, jewellery, option.equals("operate"));
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    /**
     * Represents an enchanted jewellery.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public enum EnchantedJewellery {
        RING_OF_DUELING(new String[]{ "Al Kharid Duel Arena.", "Castle Wars Arena.", "Nowhere." }, new Location[]{ Location.create(3314, 3235, 0), Location.create(2442, 3089, 0) }, true, 2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566),
        AMULET_OF_GLORY(new String[]{ "Edgeville", "Karamja", "Draynor Village", "Al-Kharid", "Nowhere." }, new Location[]{ Location.create(3087, 3495, 0), Location.create(2919, 3175, 0), Location.create(3081, 3250, 0), Location.create(3304, 3124, 0) }, 1712, 1710, 1708, 1706, 1704),
        AMULET_OF_GLORY_T(new String[]{ "Edgeville", "Karamja", "Draynor Village", "Al-Kharid", "Nowhere." }, new Location[]{ Location.create(3087, 3495, 0), Location.create(2919, 3175, 0), Location.create(3081, 3250, 0), Location.create(3304, 3124, 0) }, 10354, 10356, 10358, 10360, 10362),
        GAMES_NECKLACE(new String[]{ "Burthorpe", "Barbarian Assault", "Clan Wars", "Bounty Hunter", "Nowhere." }, new Location[]{ Location.create(2899, 3563, 0), Location.create(2520, 3571, 0), Location.create(3266, 3686, 0), Location.create(3179, 3685, 0) }, true, 3853, 3855, 3857, 3859, 3861, 3863, 3865, 3867),
        DIGSITE_PENDANT(new String[]{}, new Location[]{ Location.create(3342, 3445, 0) }, true, 11194, 11193, 11192, 11191, 11190),
        COMBAT_BRACELET(new String[]{ "Champions' Guild", "Monastery", "Ranging Guild", "Warriors' Guild", "Nowhere." }, new Location[]{ Location.create(3191, 3365, 0), Location.create(3052, 3472, 0), Location.create(2657, 3439, 0), Location.create(2878, 3546, 0) }, 11118, 11120, 11122, 11124, 11126),
        SKILLS_NECKLACE(new String[]{ "Fishing Guild", "Mining Guild", "Crafting Guild", "Cooking Guild", "Nowhere." },
            new Location[]{ Location.create(2611, 3392, 0), Location.create(3016, 3338, 0), Location.create(2933, 3290, 0),
                Location.create(3143, 3442, 0) }, 11105, 11107, 11109, 11111, 11113),
        RING_OF_SLAYING(new String[]{ "Slayer Tower", "Fremennik Slayer Dungeon", "Dark Beasts", "Duradel" }, new Location[]{
            Location.create(3428, 3536, 0),
            Location.create(2807, 10002, 0),
            Location.create(2907, 9698, 0),
            Location.create(2870, 2965, 0)
        }, true, SlayerRingPlugin.IDS);

        /**
         * Represents the teleport options.
         */
        private final String[] options;

        /**
         * Represents the locations.
         */
        private final Location[] locations;

        /**
         * Represents the ids of the jewellery.
         */
        private final int[] ids;

        /**
         * Represents if it crumbles away into nothing.
         */
        private final boolean crumble;

        /**
         * Represents the enchanted gem item.
         */
        private final Item ENCHANTED_GEM = Equipment.ENCHANTED_GEM.getItem();

        /**
         * Constructs a new {@code EnchantedJewelleryPlugin} {@code Object}.
         *
         * @param options   the options.
         * @param locations the locations.
         * @param ids       the ids.
         * @parma crumble if it crumbles.
         */
        EnchantedJewellery(final String[] options, final Location[] locations, final boolean crumble, final int... ids) {
            this.options = options;
            this.locations = locations;
            this.ids = ids;
            this.crumble = crumble;
        }

        /**
         * Constructs a new {@code EnchantedJewelleryPlugin} {@code Object}.
         *
         * @param options   the options.
         * @param locations the locations.
         * @param ids       the ids.
         */
        EnchantedJewellery(final String[] options, final Location[] locations, final int... ids) {
            this(options, locations, false, ids);
        }

        /**
         * Method used to teleport the player to the desired location.
         *
         * @param player  the player.
         * @param item    the item.
         * @param index   the index.
         * @param operate If the player is operating.
         */
        public void use(final Player player, final Item item, final int index, boolean operate) {
            if ((index + 1) == getIds().length || item.getSlot() < 0) {
                return;
            }
            int itemIndex = getItemIndex(item);
            Item replace = item;
            if (!isLast(itemIndex)) {
                if (!(isCrumble() && itemIndex == getIds().length - 1)) {
                    replace = getReplace(getNext(itemIndex));
                }
            } else {
                if (!isCrumble()) {
                    replace = getReplace(getIds()[getIds().length - 1]);
                }
            }
            if (teleport(player, itemIndex, replace, getLocation(index))) {
                if (!isLast(itemIndex) && !(isCrumble() && itemIndex == getIds().length - 1)) {
                    if (operate) {
                        player.getEquipment().replace(replace, item.getSlot());
                    } else {
                        player.getInventory().replace(replace, item.getSlot());
                    }
                } else {
                    if (isCrumble()) {
                        if (operate) {
                            if (item.getId() == 11856) {
                                player.getInventory().add(ENCHANTED_GEM, true);
                            }
                            player.getEquipment().replace(null, item.getSlot());
                        } else {
                            player.getInventory().replace(item.getId() == 11856 ? ENCHANTED_GEM : null, item.getSlot());
                        }
                    }
                }
            }
        }

        /**
         * Method used to teleport to a location.
         *
         * @param player    the player.
         * @param itemIndex the old item index.
         * @param item      the item.
         * @param location  the location.
         */
        private boolean teleport(final Player player, final int itemIndex, final Item item, final Location location) {
            if (!player.getZoneMonitor().teleport(1, item)) {
                return false;
            }
            player.lock();
            player.visualize(ANIMATION, GRAPHICS);
            player.getImpactHandler().setDisabledTicks(4);
            World.submit(new Pulse(4, player) {

                @Override
                public boolean pulse() {
                    player.unlock();
                    player.getProperties().setTeleportLocation(location);
                    if (item.getId() == 11856) {
                        player.getActionSender().sendMessage("<col=a450b0><shad=1>You use your slayer ring's last charge.");
                        player.getAnimator().reset();
                        return true;
                    }
                    String[] charges = new String[]{ "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve" };
                    String itemName = getName(item);
                    itemName = itemName.endsWith(" ") ? itemName : (itemName + " ");
                    player.getActionSender().sendMessage(isCrumble() && itemIndex == getIds().length - 1 ||
                        isLast(getItemIndex(item)) ?
                        "<col=a450b0><shad=1>You use your " + getNameType(item) + "'s last charge." :
                        "<col=a450b0><shad=1>Your " + itemName + "has " + charges[Integer.parseInt(getCharges(item))] + " "
                            + (item.getName().toLowerCase().contains("amulet") ? "charge" : "use") +
                            (Integer.parseInt(getCharges(item)) > 1 ? "s" : "") + " left.");
                    player.getAnimator().reset();
                    return true;
                }
            });
            return true;
        }

        /**
         * Gets the charges of an item.
         *
         * @param item the item.
         * @return the charges.
         */
        public static String getCharges(Item item) {
            String[] tokens = item.getName().replace("(t", "(").replace("(", " ").replace(")", "").split(" ");
            return tokens[tokens.length - 1];
        }

        /**
         * Gets the replacement item.
         *
         * @param id the id.
         * @return the item.
         */
        public Item getReplace(int id) {
            return new Item(id);
        }

        /**
         * Gets the name.
         *
         * @param item the item.
         * @return the name.
         */
        public String getName(Item item) {
            String name = item.getName().toLowerCase().replace("(t", "(").replace("(", "").replace(")", "");
            for (char number : NUMBERS) {
                name = name.replace(number, '/');
            }
            return name.trim().replace("/", "");
        }

        /**
         * Gets the name type.
         *
         * @param item the item.
         * @return
         */
        public String getNameType(Item item) {
            return this == GAMES_NECKLACE ? "games necklace" : this == DIGSITE_PENDANT ? "necklace" : this == COMBAT_BRACELET ? "bracelet" : this == SKILLS_NECKLACE ? "necklace" : item.getName().toLowerCase().split(" ")[0];
        }

        /**
         * Checks if the index is last.
         *
         * @param index the index.
         * @return <code>True</code> if so.
         */
        public boolean isLast(int index) {
            return !isCrumble() ? index == (ids.length - 1) : index == ids.length;
        }

        /**
         * Gets the next index.
         *
         * @param index the index.
         * @return the new id
         */
        public int getNext(int index) {
            return ids[index + 1];
        }

        /**
         * Gets the location.
         *
         * @param index the index.
         * @return the location.
         */
        public Location getLocation(int index) {
            if (index > locations.length) {
                System.err.println("getLocation(" + index + ") - EnchantedJewelleryPlugin");
                index = locations.length - 1;
            }
            return locations[index];
        }

        /**
         * Gets the options.
         *
         * @return The options.
         */
        public String[] getOptions() {
            return options;
        }

        /**
         * Gets the locations.
         *
         * @return The locations.
         */
        public Location[] getLocations() {
            return locations;
        }

        /**
         * Gets the ids.
         *
         * @return The ids.
         */
        public int[] getIds() {
            return ids;
        }

        public boolean isCrumble() {
            return crumble;
        }

        /**
         * Gets the enchanted jewellery.
         *
         * @param item the item.
         * @return {@code EnchantedJewellery}.
         */
        public static EnchantedJewellery forItem(final Item item) {
            for (EnchantedJewellery jewellery : values()) {
                for (int i : jewellery.getIds()) {
                    if (i == item.getId()) {
                        return jewellery;
                    }
                }
            }
            return null;
        }

        /**
         * Gets the index.
         *
         * @param item the item.
         * @return the item index.
         */
        public int getItemIndex(Item item) {
            for (int i = 0; i < getIds().length; i++) {
                if (getIds()[i] == item.getId()) {
                    return i;
                }
            }
            return -1;
        }
    }

    /**
     * Represents the jewellery dialogue plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class JewelleryDialogue extends DialoguePlugin {

        /**
         * Represents the id to use.
         */
        public static final int ID = 329128389;

        /**
         * Represents the enchanted jewellery.
         */
        private EnchantedJewellery jewellery;

        /**
         * If the operate option is used.
         */
        private boolean operate;

        /**
         * Represents the item instance.
         */
        private Item item;

        /**
         * Constructs a new {@code EnchantedJewelleryPlugin} {@code Object}.
         */
        public JewelleryDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code EnchantedJewelleryPlugin} {@code Object}.
         *
         * @param player the player.
         */
        public JewelleryDialogue(final Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new JewelleryDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            item = (Item) args[0];
            jewellery = (EnchantedJewellery) args[1];
            operate = args.length > 2 && (Boolean) args[2];
            if (jewellery == EnchantedJewellery.DIGSITE_PENDANT) {
                jewellery.use(player, item, 0, operate);
                return true;
            }
            interpreter.sendOptions("Where would you like to go?", jewellery.getOptions());
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            end();
            jewellery.use(player, item, optionSelect.getIndex(), operate);
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ ID };
        }

    }
}
