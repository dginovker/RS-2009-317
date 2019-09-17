package plugin.interaction.object.sorceress;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the plugin used for the garden object plugin.
 *
 * @author SonicForce41
 * @author 'Vexia
 * @version 1.0
 */
public final class GardenObjectsPlugin extends OptionHandler {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(827);

    /**
     * Represents the herbs items used for the elemental garden picking.
     */
    private static final int[] HERBS = { 199, 201, 203, 205, 207, 209, 211, 213, 215, 217, 219, 2485, 3049, 3051, 199, 201, 203, 205 };

    /**
     * Represents the drinking animation.
     */
    private static final Animation DRINK_ANIM = new Animation(5796);

    /**
     * Represents the teleport anim.
     */
    private static final Animation TELE = new Animation(9603);

    /**
     * Represents the graphics to use.
     */
    private static final Graphics GRAPHICS = new Graphics(1684, 100, 0);

    /**
     * Represents the picking fruit anim.
     */
    private static final Animation PICK_FRUIT = new Animation(2280);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.setOptionHandler("pick-fruit", this);
        ObjectDefinition.setOptionHandler("drink-from", this);
        for (HerbDefinition h : HerbDefinition.values()) {
            ObjectDefinition.forId(h.getId()).getConfigurations().put("option:pick", this);
        }
        new SqirkJuicePlugin().newInstance(arg);
        new OsmanDialogue().init();
        new SqirkMakingDialogue().init();
        PluginManager.definePlugin(new SorceressGardenObject());
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        GameObject object = (GameObject) node;
        if (node.getId() == 20150) {
            return false;
        }
        if (option.equals("pick")) {
            final HerbDefinition herbDef = HerbDefinition.forId(object.getId());
            if (herbDef != null) {
                handleElementalGarden(player, object, herbDef);
                return true;
            }
        } else if (option.equals("pick-fruit")) {
            final SeasonDefinitions def = SeasonDefinitions.forTreeId(object.getId());
            if (def != null) {
                player.lock();
                player.animate(PICK_FRUIT);
                player.getSkills().addExperience(Skills.THIEVING, def.getExp());
                World.submit(new Pulse(2, player) {

                    int delay = 0;

                    @Override
                    public boolean pulse() {
                        if (delay == 1) {
                            player.getInventory().add(new Item(def.getFruitId()));
                            player.getInterfaceState().openComponent(8677);
                            PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
                        } else if (delay == 3) {
                            player.getProperties().setTeleportLocation(Location.create(def.getRespawn()));
                        } else if (delay == 4) {
                            player.unlock();
                            player.getActionSender().sendMessage("An elemental force enamating from the garden teleports you away.");
                            PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                            player.getInterfaceState().close();
                            player.unlock();
                            return true;
                        }
                        delay++;
                        return false;
                    }
                });
            }
        } else if (option.equals("drink-from")) {
            player.lock();
            World.submit(new Pulse(1, player) {

                int counter = 0;

                @Override
                public boolean pulse() {
                    switch (counter++) {
                        case 1:
                            player.animate(DRINK_ANIM);
                            break;
                        case 4:
                            player.graphics(GRAPHICS);
                            break;
                        case 5:
                            player.animate(TELE);
                            break;
                        case 6:
                            player.getInterfaceState().openComponent(8677);
                            break;
                        case 7:
                            PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
                            break;
                        case 9:
                            player.getProperties().setTeleportLocation(new Location(3321, 3141, 0));
                            break;
                        case 11:
                            player.unlock();
                            player.animate(new Animation(-1));
                            player.getInterfaceState().close();
                            PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                            return true;
                    }
                    return false;
                }
            });
        }
        return true;
    }

    /**
     * Method used to handle the elemental garden picking.
     *
     * @param player  the player.
     * @param object  the object.
     * @param herbDef the herbdef.
     */
    private void handleElementalGarden(final Player player, GameObject object, final GardenObjectsPlugin.HerbDefinition herbDef) {
        player.lock();
        player.animate(ANIMATION);
        player.getSkills().addExperience(Skills.FARMING, herbDef.getExp());
        World.submit(new Pulse(2, player) {

            int delay = 0;

            @Override
            public boolean pulse() {
                if (delay == 1) {
                    player.getInventory().add(new Item(HERBS[RandomUtil.random(0, HERBS.length - 1)]));
                    player.getInventory().add(new Item(HERBS[RandomUtil.random(0, HERBS.length - 1)]));
                    player.getActionSender().sendMessage("You pick up a herb.");
                    player.getInterfaceState().openComponent(8677);
                    PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
                } else if (delay == 3) {
                    player.getProperties().setTeleportLocation(Location.create(herbDef.getRespawn()));
                } else if (delay == 4) {
                    player.unlock();
                    player.getActionSender().sendMessage("An elemental force enamating from the garden teleports you away.");
                    PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
                    player.getInterfaceState().close();
                    player.unlock();
                    return true;
                }
                delay++;
                return false;
            }
        });
    }

    /**
     * Represents the herb definitions.
     *
     * @author SonicForce41
     * @version 1.0
     */
    public enum HerbDefinition {
        WINTER(21671, 30, new Location(2907, 5470, 0)),
        SPRING(21668, 40, new Location(2916, 5473, 0)),
        AUTUMN(21670, 50, new Location(2913, 5467, 0)),
        SUMMER(21669, 60, new Location(2910, 5476, 0));

        /**
         * Represents the id.
         */
        private int id;

        /**
         * Represents the experience.
         */
        private double exp;

        /**
         * Represents the respawn location.
         */
        private Location respawn;

        /**
         * Constructs a new {@code GardenObjectsPlugin} {@code Object}.
         *
         * @param id      the id.
         * @param exp     the exp.
         * @param respawn the respawn.
         */
        HerbDefinition(int id, double exp, Location respawn) {
            this.id = id;
            this.exp = exp;
            this.respawn = respawn;
        }

        /**
         * Gets the exp
         *
         * @return the Exp
         */
        public double getExp() {
            return exp;
        }

        /**
         * Gets the Id
         *
         * @return the Id
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the respawn Location
         *
         * @return the Location.
         */
        public Location getRespawn() {
            return respawn;
        }

        /**
         * Gets the herb definition by the id.
         *
         * @param id the objectId
         * @return the definition.
         */
        public static HerbDefinition forId(int id) {
            for (HerbDefinition def : HerbDefinition.values()) {
                if (def.getId() == id) {
                    return def;
                }
            }
            return null;
        }
    }

    /**
     * Represents the season definitions.
     *
     * @author SonicForce41
     * @version 1.0
     */
    public enum SeasonDefinitions {
        WINTER(21769, 1, 30, 70.0, 10847, 10851, 5, 0, 10, 17.5, 21709, new Location(2907, 5470, 0)),
        SPRING(21767, 25, 40, 337.5, 10844, 10848, 4, 1, 20, 67.5, 21753, new Location(2916, 5473, 0)),
        AUTUMN(21768, 45, 50, 783.3, 10846, 10850, 3, 2, 30, 117.5, 21731, new Location(2913, 5467, 0)),
        SUMMER(21766, 65, 60, 1500, 10845, 10849, 2, 3, 40, 150, 21687, new Location(2910, 5476, 0));

        /**
         * The treeId
         */
        private int treeId;

        /**
         * The level
         */
        private int level;

        /**
         * The farming experience recieved from picking
         */
        private double farmExp;

        /**
         * The thieving experience recieved from picking
         */
        private double treeExp;

        /**
         * The fruitId id
         */
        private int fruitId;

        /**
         * The juice id
         */
        private int juiceId;

        /**
         * The fruit amt
         */
        private int fruitAmt;

        /**
         * The boost
         */
        private int boost;

        /**
         * The energy
         */
        private int energy;

        /**
         * The experience recieved from osman
         */
        private double osmanExp;

        /**
         * The gate id
         */
        private int gateId;

        /**
         * The respawn location
         */
        private Location respawn;

        /**
         * Constructs a new {@code GardenObjectsPlugin.java} {@code Object}.
         *
         * @param treeId   the tree id.
         * @param level    the level.
         * @param farmExp  the farm exp.
         * @param treeExp  the tree exp.
         * @param fruitId  the fruit id.
         * @param juiceId  the juice id.
         * @param fruitAmt the fruit amt.
         * @param boost    the boost.
         * @param energy   the energy.
         * @param osmanExp the osman exp.
         * @param gateId   the gate id.
         * @param respawn  the respawn.
         */
        SeasonDefinitions(int treeId, int level, double farmExp, double treeExp, int fruitId, int juiceId, int fruitAmt, int boost, int energy, double osmanExp, int gateId, Location respawn) {
            this.treeId = treeId;
            this.level = level;
            this.farmExp = farmExp;
            this.treeExp = treeExp;
            this.fruitId = fruitId;
            this.juiceId = juiceId;
            this.fruitAmt = fruitAmt;
            this.boost = boost;
            this.energy = energy;
            this.osmanExp = osmanExp;
            this.gateId = gateId;
            this.respawn = respawn;
        }

        /**
         * Gets the theiving boost
         *
         * @return the boost
         */
        public int getBoost() {
            return boost;
        }

        /**
         * Gets the run energy restoring
         *
         * @return the energy
         */
        public int getEnergy() {
            return energy;
        }

        /**
         * Gets the exp from tree
         *
         * @return the treeExp
         */
        public double getExp() {
            return treeExp;
        }

        /**
         * Gets the farmExp
         *
         * @return the farmExp
         */
        public double getFarmExp() {
            return farmExp;
        }

        /**
         * Gets the fruid amt
         *
         * @return the fruitAmt
         */
        public int getFruitAmt() {
            return fruitAmt;
        }

        /**
         * Gets the fruit Id
         *
         * @return the fruitId
         */
        public int getFruitId() {
            return fruitId;
        }

        /**
         * Gets the gate Id
         *
         * @return the gateId
         */
        public int getGateId() {
            return gateId;
        }

        /**
         * Gets the juice Id
         *
         * @return the juiceId
         */
        public int getJuiceId() {
            return juiceId;
        }

        /**
         * Gets the level
         *
         * @return the Level
         */
        public int getLevel() {
            return level;
        }

        /**
         * Gets the experience recieved from osman
         *
         * @return the osmanExp
         */
        public double getOsmanExp() {
            return osmanExp;
        }

        /**
         * Gets the respawn location
         *
         * @return the respawn Location
         */
        public Location getRespawn() {
            return respawn;
        }

        /**
         * Gets the treeId
         *
         * @return the treeId
         */
        public int getTreeId() {
            return treeId;
        }

        /**
         * Gets the def by the fruit id.
         *
         * @param fruitId the fruit id.
         * @return the definition.
         */
        public static SeasonDefinitions forFruitId(int fruitId) {
            for (SeasonDefinitions def : SeasonDefinitions.values()) {
                if (def == null) {
                    continue;
                }
                if (fruitId == def.getFruitId()) {
                    return def;
                }
            }
            return null;
        }

        /**
         * Gets the def by the gate Id.
         *
         * @param gateId the gateId
         * @return the def.
         */
        public static SeasonDefinitions forGateId(int gateId) {
            for (SeasonDefinitions def : SeasonDefinitions.values()) {
                if (gateId == def.getGateId()) {
                    return def;
                }
            }
            return null;
        }

        /**
         * Gets the def by the juice id.
         *
         * @param juiceId the juice id.
         * @return the def.
         */
        public static SeasonDefinitions forJuiceId(int juiceId) {
            for (SeasonDefinitions def : SeasonDefinitions.values()) {
                if (def == null) {
                    continue;
                }
                if (juiceId == def.getJuiceId()) {
                    return def;
                }
            }
            return null;
        }

        /**
         * Gets the season def by the tree id.
         *
         * @param treeId the tree id.
         * @return the def.
         */
        public static SeasonDefinitions forTreeId(int treeId) {
            for (SeasonDefinitions def : SeasonDefinitions.values()) {
                if (def == null) {
                    continue;
                }
                if (treeId == def.getTreeId()) {
                    return def;
                }
            }
            return null;
        }
    }

    /**
     * Use with Plugin for Sq'irk Juice making
     *
     * @author SonicForce41
     */
    public static final class SqirkJuicePlugin extends UseWithHandler {

        /**
         * The crushing an item with pestle and mortar animation.
         */
        private static final Animation CRUSH_ITEM = new Animation(364);

        public SqirkJuicePlugin() {
            super(10844, 10845, 10846, 10847);
        }

        @Override
        public boolean handle(NodeUsageEvent event) {
            Item item = event.getUsedItem();
            Item with = event.getBaseItem();
            Player player = event.getPlayer();
            GardenObjectsPlugin.SeasonDefinitions def = GardenObjectsPlugin.SeasonDefinitions.forFruitId(item.getId());
            if (item == null || with == null || player == null || def == null) {
                return true;
            }
            int amt = player.getInventory().getCount(item);
            if (!player.getInventory().containItems(1919)) {
                player.getDialogueInterpreter().open(43382, 0);
                return true;
            }
            if (amt < def.getFruitAmt()) {
                player.getDialogueInterpreter().open(43382, 1, item.getId());
                return true;
            }
            player.animate(CRUSH_ITEM);
            player.getSkills().addExperience(Skills.COOKING, 5);
            player.getInventory().remove(new Item(item.getId(), def.getFruitAmt()));
            player.getInventory().remove(new Item(1919));
            player.getInventory().add(new Item(def.getJuiceId()));
            player.getDialogueInterpreter().sendPlaneMessage("You squeeze " + def.getFruitAmt() + " sq'irks into an empty glass.");
            return true;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            addHandler(233, ITEM_TYPE, this);
            return this;
        }

    }

    /**
     * Represents the dialougue of the osman NPC.
     *
     * @author 'Vexia
     * @author SonicForce41
     * @date 31/12/2013
     */
    public static final class OsmanDialogue extends DialoguePlugin {

        /**
         * Represents the key print item.
         */
        private static final Item KEY_PRINT = new Item(2423);

        /**
         * Represents the bronze bar item.
         */
        private static final Item BRONZE_BAR = new Item(2349);

        /**
         * Represents the rope item.
         */
        private static final Item ROPE = new Item(954);

        /**
         * Represents the pink skirt item.
         */
        private static final Item SKIRT = new Item(1013);

        /**
         * Represents the yellow wig item.
         */
        private static final Item YELLOW_WIG = new Item(2419);

        /**
         * Represents the skin paste item.
         */
        private static final Item PASTE = new Item(2424);

        /**
         * Represents the juices.
         */
        private static final int[] JUICES = new int[]{ 10848, 10849, 10850, 10851 };

        /**
         * Represents the fruits.
         */
        private static final int[] FRUITS = new int[]{ 10844, 10845, 10846, 10847 };

        /**
         * Represents the count of materials you have gathered.
         */
        private int itemCount;


        /**
         * Constructs a new {@code OsmanDialogue} {@code Object}.
         */
        public OsmanDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code OsmanDialogue} {@code Object}.
         *
         * @param player the Player
         */
        public OsmanDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new OsmanDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            interpreter.sendDialogues(player, null, "I'd like to talk about sq'irks.");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    if (!hasSqirks()) {
                        interpreter.sendOptions("Select an Option", "Where do I get sq'irks?", "Why can't you get the sq'irks yourself?", "How should I squeeze the fruit?", "Is there a reward for getting these sq'irks?", "What's so good about sq'irk juice then?");
                        stage = 200;
                    } else {
                        player(hasSqirkJuice() ? "I have some sq'riks juice for you." : "I have some sq'irks for you.");
                        stage = 300;
                    }
                    break;
                case 300:
                    if (hasSqirkJuice()) {
                        double exp = getExperience();
                        player.getSkills().addExperience(Skills.THIEVING, exp);
                        interpreter.sendPlaneMessage("Osman imparts some Thieving advice to", "you ( " + (exp) + " Thieving experience points )", "as a reward for the sq'irk juice.");
                        stage = 304;
                    } else {
                        npc("Uh, thanks, but is there any chance that you", "could squeeze the fruit into a glass for me?");
                        stage = 301;
                    }
                    break;
                case 301:
                    interpreter.sendOptions("Select an Option", "How should I squeeze the fruit?", "Can't yu do that yourself?");
                    stage = 302;
                    break;
                case 302:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            player("How should I squeeze the fruit?");
                            stage = 130;
                            break;
                        case TWO_OPTION_TWO:
                            player("Can't you do that yourself?");
                            stage = 303;
                            break;
                    }
                    break;
                case 304:
                    end();
                    break;
                case 303:
                    player("I only carry knives or other such devices on me", "when I'm on the job.");
                    stage = 119;
                    break;
                case 200:
                    switch (optionSelect) {
                        case FIVE_OPTION_ONE:
                            interpreter.sendDialogues(player, null, "Where do I get sq'irks?");
                            stage = 110;
                            break;
                        case FIVE_OPTION_TWO:
                            interpreter.sendDialogues(player, null, "Why can't you get the sq'irks yourself?");
                            stage = 120;
                            break;
                        case FIVE_OPTION_THREE:
                            interpreter.sendDialogues(player, null, "How should I squeeze the fruit?");
                            stage = 130;
                            break;
                        case FIVE_OPTION_FOUR:
                            interpreter.sendDialogues(player, null, "Is there a reward for getting these sq'irks?");
                            stage = 140;
                            break;
                        case FIVE_OPTION_FIVE:
                            interpreter.sendDialogues(player, null, "What's so good about sq'irk juice then?");
                            stage = 150;
                            break;
                    }
                    break;
                case 110:
                    npc("There is a sorceress near the south-eastern edge of Al", "Kharid who grows them. Once upon a time, we", "considered each other friends.");
                    stage = 111;
                    break;
                case 111:
                    player("What happened?");
                    stage = 112;
                    break;
                case 112:
                    npc("We fell out, and now she won't give me any more", "fruit.");
                    stage = 113;
                    break;
                case 113:
                    player("So all I have to do is ask her for some fruit for you?");
                    stage = 114;
                    break;
                case 114:
                    npc("I doubt it will be that easy. She is not renowned for", "her generosity and is very secretive about her garden's", "location.");
                    stage = 115;
                    break;
                case 115:
                    player("Oh come on, it should be easy enough to find.");
                    stage = 116;
                    break;
                case 116:
                    npc("Her garden has remained hidden even to me - the chief", "spy of Al Kharid. I believe her garden must be hidden", "by magical means.");
                    stage = 117;
                    break;
                case 117:
                    player("This should be an interesting task. How many sq'irks do", "you want?");
                    stage = 118;
                    break;
                case 118:
                    npc("I'll reward you for as many as you can get your", "hands on, but could you please squeeze the fruit into a", "glass first?");
                    stage = 119;
                    break;
                case 119:
                    interpreter.sendOptions("Select an Option", "I've another question about sq'irks.", "Thanks for the information.");
                    stage = 98;
                    break;
                case 98:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            interpreter.sendOptions("Select an Option", "Where do I get sq'irks?", "Why can't you get the sq'irks yourself?", "How should I squeeze the fruit?", "Is there a reward for getting these sq'irks?", "What's so good about sq'irk juice then?");
                            stage = 200;
                            break;
                        case TWO_OPTION_TWO:
                            player("Thanks for the information.");
                            stage = 99;
                            break;
                    }
                    break;
                case 99:
                    end();
                    break;
                case 120:
                    npc("I may have mentioned that I had a falling out with the", "Sorceress. Well, unsurprisingly, she refuses to give me", "any more of her garden's produce.");
                    stage = 119;
                    break;
                case 130:
                    npc("Use a pestle and mortar to squeeze the sr'irks. Make", "sure you have an empty glass with you to collect the", "juice.");
                    stage = 119;
                    break;
                case 140:
                    npc("Of course there is. I am a generous man. I'll teach", "you the art of Thieving for your troubles.");
                    stage = 141;
                    break;
                case 141:
                    player("How much training will you give?");
                    stage = 142;
                    break;
                case 142:
                    npc("That depends on the quantity and ripeness of the", "sq'irks you put into the juice.");
                    stage = 143;
                    break;
                case 143:
                    player("That sounds fair enough.");
                    stage = 119;
                    break;
                case 150:
                    npc("Ah it's sweet, sweet nectar for a thief or spy; it makes", "light fingers lighter, fleet feet flightier and comes in four", "different colours for those who are easily amused.");
                    stage = 151;
                    break;
                case 151:
                    interpreter.sendPlaneMessage("Osman starts salivating at the thought of sq'irk juice.");
                    stage = 152;
                    break;
                case 152:
                    player("It wouldn't have addictive properties, would it?");
                    stage = 153;
                    break;
                case 153:
                    npc("It only holds power over those with poor self-control,", "something which I have an abundance of.");
                    stage = 154;
                    break;
                case 154:
                    player("I see.");
                    stage = 119;
                    break;
            }
            return true;
        }

        /**
         * Gets the experience the player can recieve.
         *
         * @return the experience.
         */
        public double getExperience() {
            double total = 0;
            for (int juiceId : JUICES) {
                GardenObjectsPlugin.SeasonDefinitions def = GardenObjectsPlugin.SeasonDefinitions.forJuiceId(juiceId);
                if (def == null) {
                    continue;
                }
                int amount = player.getInventory().getCount(new Item(juiceId));
                total += (amount * def.getOsmanExp());
                player.getInventory().remove(new Item(juiceId, amount));
            }
            player.getInventory().refresh();
            return total;
        }

        /**
         * Checks wether the <b>Player</b> has sq'irk.
         *
         * @return <code>True</code>: Player has sq'irk.
         */
        public boolean hasSqirkFruit() {
            for (int i : FRUITS) {
                if (player.getInventory().contains(i, 1)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Checks wether the <b>Player</b> has sq'irk juice
         *
         * @return <code>True</code>: Player has sq'irk juice.
         */
        public boolean hasSqirkJuice() {
            for (int i : JUICES) {
                if (player.getInventory().contains(i, 1)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Checks wether the <b>Player</b> has sq'irks (fruit/juice).
         *
         * @return <code>True</code>: Player has either a fruit or squeezed juice.
         */
        public boolean hasSqirks() {
            return hasSqirkFruit() || hasSqirkJuice();
        }

        @Override
        public int[] getIds() {
            return new int[]{ 924, 5282 };
        }
    }

    /**
     * Dialogue for Sqirk making
     *
     * @author SonicForce41
     */
    public class SqirkMakingDialogue extends DialoguePlugin {

        private int dialogueId;

        private GardenObjectsPlugin.SeasonDefinitions definition;

        /**
         * Constructs a new {@code SqirkMakingDialogue.java} {@code Object}.
         */
        public SqirkMakingDialogue() {

        }

        /**
         * Constructs a new {@code SqirkMakingDialogue.java} {@code Object}.
         *
         * @param player the Player
         */
        public SqirkMakingDialogue(Player player) {
            super(player);
        }

        @Override
        public int[] getIds() {
            return new int[]{ 43382 };
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (dialogueId) {
                case 0:
                    end();
                    break;
                case 1:
                    switch (stage) {
                        case 0:
                            interpreter.sendPlaneMessage("You need " + definition.getFruitAmt() + " sq'irks of this kind to fill a glass of juice.");
                            stage = 1;
                            break;
                        case 1:
                            end();
                            break;
                    }
                    break;
            }
            return true;
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new SqirkMakingDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            dialogueId = (int) args[0];
            switch (dialogueId) {
                case 0:
                    interpreter.sendDialogues(player, FacialExpression.ANNOYED, "I should get an empty beer glass to", "hold the juice before I squeeze the fruit.");
                    break;
                case 1:
                    definition = GardenObjectsPlugin.SeasonDefinitions.forFruitId((int) args[1]);
                    if (definition == null) {
                        end();
                    }
                    interpreter.sendDialogues(player, FacialExpression.ANNOYED, "I think I should wait till I have", "enough fruits to make a full glass.");
                    break;
            }
            stage = 0;
            return true;
        }

    }

}
