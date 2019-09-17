package org.gielinor.game.content.skill.member.summoning;

import org.gielinor.cache.def.impl.CS2Mapping;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a utility class of creating summoning nodes.
 *
 * @author 'Vexia
 */
public final class SummoningCreator {

    /**
     * Represents the animation used when creating a node.
     */
    private static final Animation ANIMATION = new Animation(9068);
    private static final Object[][] SCROLL_DATA = new Object[][]{
        { 1, 12377, 12425, "Howl scroll" },
        { 2, 12293, 12445, "Dreadfowl<br>Strike scroll" },
        { 3, 12353, 12428, "Egg Spawn<br>scroll" },
        { 4, 12359, 12459, "Slime Spray<br>scroll" },
        { 5, 12355, 12533, "Stony Shell<br>scroll" },
        { 6, 12409, 12838, "Pester scroll" },
        { 7, 12381, 12460, "Electric Lash<br>scroll" },
        { 8, 12357, 12432, "Venom Shot<br>scroll" },
        { 9, 12415, 12839, "Fireball<br>Assault<br>scroll" },
        { 10, 12351, 12430, "Cheese Feast<br>scroll" },
        { 11, 12327, 12446, "Sandstorm<br>scroll" },
        { 12, 12313, 12440, "Generate<br>Compost<br>scroll" },
        { 13, 12395, 12834, "Explode<br>scroll" },
        { 14, 12375, 12447, "Vampire<br>Touch scroll" },
        { 15, 12321, 12433, "Insane<br>Ferocity<br>scroll" },
        { 16, 12303, 12429, "Multichop<br>scroll" },
        { 17, 12407, 12443, "Call to Arms<br>scroll" },
        { 18, 12407, 12443, "Call to Arms<br>scroll" },
        { 19, 12407, 12443, "Call to Arms<br>scroll" },
        { 20, 12407, 12443, "Call to Arms<br>scroll" },
        { 21, 12301, 12431, "Unburden<br>scroll" },
        { 22, 12335, 12422, "Herbcall<br>scroll" },
        { 23, 12371, 12448, "Evil Flames<br>scroll" },
        { 24, 12311, 12458, "Petrifying<br>Gaze scroll" },
        { 25, 12311, 12458, "Petrifying<br>Gaze scroll" },
        { 26, 12311, 12458, "Petrifying<br>Gaze scroll" },
        { 27, 12311, 12458, "Petrifying<br>Gaze scroll" },
        { 28, 12311, 12458, "Petrifying<br>Gaze scroll" },
        { 29, 12311, 12458, "Petrifying<br>Gaze scroll" },
        { 30, 12311, 12458, "Petrifying<br>Gaze scroll" },
        { 31, 12411, 12829, "Immense Heat<br>scroll" },
        { 32, 12337, 12426, "Thieving<br>Fingers scroll" },
        { 33, 12347, 12444, "Blood Drain<br>scroll" },
        { 34, 12345, 12441, "Tireless Run<br>scroll" },
        { 35, 12299, 12454, "Abyssal<br>Drain scroll" },
        { 36, 12325, 12453, "Dissolve<br>scroll" },
        { 37, 12363, 12424, "Fish Rain<br>scroll" },
        { 38, 12391, 12836, "Ambush<br>scroll" },
        { 39, 12419, 12840, "Rending<br>scroll" },
        { 40, 12393, 12835, "Goad scroll" },
        { 41, 12329, 12455, "Doomsphere<br>Device scroll" },
        { 42, 12317, 12468, "Dust Cloud<br>scroll" },
        { 43, 12297, 12427, "Abyssal<br>Stealth scroll" },
        { 44, 12361, 12436, "Ophidian<br>Incubation<br>scroll" },
        { 45, 12365, 12467, "Poisonous<br>Blast scroll" },
        { 46, 12367, 12452, "Toad Bark<br>scroll" },
        { 47, 12369, 12439, "Testudo<br>scroll" },
        { 48, 12309, 12438, "Swallow<br>Whole scroll" },
        { 49, 12319, 12423, "Fruitfall<br>scroll" },
        { 50, 12333, 12830, "Famine scroll" },
        { 51, 12349, 12451, "Arctic Blast<br>scroll" },
        { 52, 12401, 12826, "Volcanic<br>Strength<br>scroll" },
        { 53, 12331, 12449, "Crushing<br>Claw scroll" },
        { 54, 12339, 12450, "Mantis Strike<br>scroll" },
        { 55, 12417, 12841, "Inferno scroll" },
        { 56, 12343, 12831, "Deadly Claw<br>scroll" },
        { 57, 12385, 12457, "Acorn Missile<br>scroll" },
        { 58, 12389, 12824, "Titan's<br>Constitution<br>scroll" },
        { 59, 12389, 12824, "Titan's<br>Constitution<br>scroll" },
        { 60, 12389, 12824, "Titan's<br>Constitution<br>scroll" },
        { 61, 12323, 12442, "Regrowth<br>scroll" },
        { 62, 12295, 12456, "Spike Shot<br>scroll" },
        { 63, 12397, 12837, "Ebon Thunder<br>scroll" },
        { 64, 12387, 12832, "Swamp<br>Plague scroll" },
        { 65, 12341, 12461, "Bronze Bull<br>Rush scroll" },
        { 66, 12341, 12462, "Iron Bull Rush<br>scroll" },
        { 67, 12341, 12463, "Steel Bull<br>Rush scroll" },
        { 68, 12341, 12464, "Mithril Bull<br>Rush scroll" },
        { 69, 12341, 12465, "Adamant Bull<br>Rush scroll" },
        { 70, 12341, 12466, "Rune Bull<br>Rush scroll" },
        { 71, 12373, 12434, "Healing Aura<br>scroll" },
        { 72, 12405, 12833, "Boil scroll" },
        { 73, 12379, 12437, "Magic Focus<br>scroll" },
        { 74, 12403, 12827, "Essence<br>Shipment<br>scroll" },
        { 75, 12413, 12828, "Iron Within<br>scroll" },
        { 76, 12383, 12435, "Winter<br>Storage<br>scroll" },
        { 77, 12399, 12825, "Steel of<br>Legends<br>scroll" }
    };
    private static final Object[][] DATA = new Object[][]{
        { 1, 12231, 12047, "Spirit wolf<br>pouch" },
        { 2, 12225, 12043, "Dreadfowl<br>pouch" },
        { 3, 12236, 12059, "Spirit spider<br>pouch" },
        { 4, 12255, 12019, "Thorny snail<br>pouch" },
        { 5, 12226, 12009, "Granite crab<br>pouch" },
        { 6, 12286, 12778, "Spirit<br>mosquito<br>pouch" },
        { 7, 12256, 12049, "Desert wyrm<br>pouch" },
        { 8, 12240, 12055, "Spirit<br>scorpion<br>pouch" },
        { 9, 12290, 12808, "Spirit Tz-Kih<br>pouch" },
        { 10, 12245, 12067, "Albino rat<br>pouch" },
        { 11, 12241, 12063, "Spirit<br>kalphite<br>pouch" },
        { 12, 12237, 12091, "Compost<br>mound pouch" },
        { 13, 12276, 12800, "Giant<br>chinchompa pouch" },
        { 14, 12227, 12053, "Vampire bat<br>pouch" },
        { 15, 12246, 12065, "Honey badger<br>pouch" },
        { 16, 12232, 12021, "Beaver pouch" },
        { 17, 12282, 12818, "Void ravager<br>pouch" },
        { 18, 12284, 12780, "Void spinner<br>pouch" },
        { 19, 12283, 12798, "Void torcher<br>pouch" },
        { 20, 12285, 12814, "Void shifter<br>pouch" },
        { 21, 12243, 12087, "Bull ant<br>pouch" },
        { 22, 12228, 12071, "Macaw pouch" },
        { 23, 12229, 12051, "Evil turnip<br>pouch" },
        { 24, 12266, 12095, "Spirit<br>cockatrice<br>pouch" },
        { 25, 12266, 12097, "Spirit<br>guthatrice<br>pouch" },
        { 26, 12266, 12099, "Spirit<br>saratrice<br>pouch" },
        { 27, 12266, 12101, "Spirit<br>zamatrice<br>pouch" },
        { 28, 12266, 12103, "Spirit<br>pengatrice<br>pouch" },
        { 29, 12266, 12105, "Spirit<br>coraxatrice<br>pouch" },
        { 30, 12266, 12107, "Spirit<br>vulatrice<br>pouch" },
        { 31, 12288, 12816, "Pyrelord<br>pouch" },
        { 32, 12235, 12041, "Magpie pouch" },
        { 33, 12259, 12061, "Bloated leech<br>pouch" },
        { 34, 12238, 12007, "Spirit<br>terrorbird<br>pouch" },
        { 35, 12258, 12035, "Abyssal<br>parasite<br>pouch" },
        { 36, 12257, 12027, "Spirit jelly<br>pouch" },
        { 37, 12233, 12531, "Ibis pouch" },
        { 38, 12274, 12812, "Spirit kyatt<br>pouch" },
        { 39, 12289, 12784, "Spirit larupia<br>pouch" },
        { 40, 12275, 12810, "Spirit graahk<br>pouch" },
        { 41, 12267, 12023, "Karamthulhu<br>overlord<br>pouch" },
        { 42, 12239, 12085, "Smoke devil pouch" },
        { 43, 12234, 12037, "Abyssal<br>lurker pouch" },
        { 44, 12254, 12015, "Spirit cobra<br>pouch" },
        { 45, 12265, 12045, "Stranger<br>plant pouch" },
        { 46, 12252, 12123, "Barker toad<br>pouch" },
        { 47, 12253, 12031, "War tortoise<br>pouch" },
        { 48, 12262, 12029, "Bunyip pouch" },
        { 49, 12230, 12033, "Fruit bat pouch" },
        { 50, 12244, 12820, "Ravenous<br>locust pouch" },
        { 51, 12249, 12057, "Arctic bear<br>pouch" },
        { 52, 12279, 12792, "Obsidian<br>golem pouch" },
        { 53, 12242, 12069, "Granite<br>lobster<br>pouch" },
        { 54, 12247, 12011, "Praying<br>mantis pouch" },
        { 55, 12291, 12782, "Forge regent<br>pouch" },
        { 56, 12261, 12794, "Talon beast<br>pouch" },
        { 57, 12269, 12013, "Giant ent<br>pouch" },
        { 58, 12271, 12802, "Fire titan<br>pouch" },
        { 59, 12273, 12804, "Moss titan<br>pouch" },
        { 60, 12272, 12806, "Ice titan<br>pouch" },
        { 61, 12263, 12025, "Hydra pouch" },
        { 62, 12260, 12017, "Spirit<br>dagannoth<br>pouch" },
        { 63, 12277, 12788, "Lava titan<br>pouch" },
        { 64, 12270, 12776, "Swamp titan<br>pouch" },
        { 65, 12264, 12073, "Bronze<br>minotaur<br>pouch" },
        { 66, 12264, 12075, "Iron minotaur<br>pouch" },
        { 67, 12264, 12077, "Steel<br>minotaur<br>pouch" },
        { 68, 12264, 12079, "Mithril<br>minotaur<br>pouch" },
        { 69, 12264, 12081, "Adamant<br>minotaur<br>pouch" },
        { 70, 12264, 12083, "Rune<br>minotaur<br>pouch" },
        { 71, 12250, 12039, "Unicorn<br>stallion<br>pouch" },
        { 72, 12280, 12786, "Geyser titan<br>pouch" },
        { 73, 12268, 12089, "Wolpertinger<br>pouch" },
        { 74, 12281, 12796, "Abyssal titan<br>pouch" },
        { 75, 12287, 12822, "Iron titan<br>pouch" },
        { 76, 12251, 12093, "Pack yak<br>pouch" },
        { 77, 12278, 12790, "Steel titan<br>pouch" }
    };

    /**
     * Represents the summoning component.
     */
    private static final Component SUMMONING_COMPONENT = new Component(25717);

    /**
     * Represents the scroll component.
     */
    private static final Component SCROLL_COMPONENT = new Component(25810);

    /**
     * Method used to open the creation screen.
     *
     * @param player the player.
     */
    public static void open(final Player player, final boolean pouch) {
        configure(player, pouch);
    }

    /**
     * Method used to configure a creation interface.
     *
     * @param player the player.
     * @param pouch  the pouch.
     */
    public static void configure(final Player player, final boolean pouch) {
        int index;
        boolean requirements;
        if (pouch) {
            for (Object[] data : DATA) {
                index = ((Integer) data[0]) - 1;
                SummoningPouch summoningPouch = SummoningPouch.forSlot(index);
                if (summoningPouch == null) {
                    continue;
                }
                requirements = player.getInventory().contains(summoningPouch.getItems()) &&
                    player.getSkills().getStaticLevel(Skills.SUMMONING) >= summoningPouch.getLevelRequired()
                    && player.getInventory().contains(12155);
                String name = ((String) data[3]).replaceAll("<br>", "<br><col=" + (requirements ? "FFFF64" : "111111") + ">");
                player.getActionSender().sendString(25729 + index, "<col=" + (requirements ? "FFFF64" : "111111") + ">" + name);
                player.getActionSender().sendUpdateItem(25728, index, (Integer) (requirements ? data[2] : data[1]));
            }
        } else {
            for (Object[] data : SCROLL_DATA) {
                index = ((Integer) data[0]) - 1;
                SummoningScroll summoningScroll = SummoningScroll.forId(index);
                if (summoningScroll == null) {
                    continue;
                }
                requirements = player.getInventory().contains(summoningScroll.getItems()) &&
                    player.getSkills().getStaticLevel(Skills.SUMMONING) >= summoningScroll.getLevel();
                String name = ((String) data[3]).replaceAll("<br>", "<br><col=" + (requirements ? "FFFF64" : "111111") + ">");
                player.getActionSender().sendString(25822 + index, "<col=" + (requirements ? "FFFF64" : "111111") + ">" + name);
                player.getActionSender().sendUpdateItem(25821, index, (Integer) (requirements ? data[2] : data[1]));
            }
        }
        player.getInterfaceState().setOpened(pouch ? SUMMONING_COMPONENT : SCROLL_COMPONENT);
        player.getActionSender().sendInterface(pouch ? SUMMONING_COMPONENT.getId() : SCROLL_COMPONENT.getId());
    }

    /**
     * Method used to create a summoning node type.
     *
     * @param player the player.
     * @param amount the amount.
     * @param node   the node.
     */
    public static void create(final Player player, final int amount, Object node) {
        if (node == null) {
            return;
        }
        player.getPulseManager().run(new CreatePulse(player, null, SummoningNode.parse(node), amount));
    }

    /**
     * Method used to list the items needed for a pouch.
     *
     * @param pouch the pouch.
     */
    public static void list(final Player player, final SummoningPouch pouch) {
        player.getActionSender().sendMessage((String) CS2Mapping.forId(1186).getMap().get(pouch.getPouchId()));
    }

    /**
     * Represents the skill pulse used to create a summoning node.
     *
     * @author 'Vexia
     */
    public static final class CreatePulse extends SkillPulse<Item> {

        /**
         * Represents the summoning node type.
         */
        private final SummoningCreator.SummoningNode type;

        /**
         * Represents the object.
         */
        private GameObject object;

        /**
         * Represents the amount to make.
         */
        private int amount;

        /**
         * The charm that was saved (from Summoning-related perks).
         */
        private Item charmItemSaved;

        /**
         * Amount of charms saved this trip (from Summoning-related perks).
         */
        private int charmsSaved = 0;

        /**
         * Constructs a new {@code SummoningCreator} {@code Object}.
         *
         * @param player the player.
         * @param node   the node.
         * @param type   the type.
         * @param amount the amount.
         */
        public CreatePulse(Player player, Item node, final SummoningCreator.SummoningNode type, final int amount) {
            super(player, node);
            this.type = type;
            this.amount = amount;
            this.object = RegionManager.getObject(new Location(2209, 5344, 0));
        }

        @Override
        public boolean checkRequirements() {
            player.getInterfaceState().close();
            if (player.getSkills().getStaticLevel(Skills.SUMMONING) < type.getLevel()) {
                player.getActionSender().sendMessage("You need a Summoning level of at least " + type.getLevel() + " in order to do this.");
                return false;
            }
            if (amount == 0) {
                player.getActionSender().sendMessage("You don't have the required item(s) to make this.");
                return false;
            }
            for (Item item : type.getRequired()) {
                if (!player.getInventory().containsItem(item)) {
                    player.getActionSender().sendMessage("You don't have the required item(s) to make this.");
                    return false;
                }
            }
            return true;
        }

        @Override
        public void animate() {
            player.lock(3);
            player.animate(ANIMATION);
        }

        @Override
        public void stop() {
            super.stop();
            player.getActionSender().sendObjectAnimation(object, new Animation(8510));
            if (charmsSaved == 1) {
                player.getActionSender().sendMessage("<col=FF0000>You save a " + charmItemSaved.getName() + " while infusing.</col>");
            } else if (charmsSaved > 1) {
                player.getActionSender().sendMessage("<col=FF0000>You save " + charmsSaved + " " + charmItemSaved.getName() + "s while infusing the pouches.</col>");
            }
        }

        @Override
        public boolean reward() {
            if (getDelay() == 1) {
                setDelay(3);
                player.getActionSender().sendObjectAnimation(object, Animation.create(8509));
                return false;
            }
            player.getActionSender().sendObjectAnimation(object, Animation.create(8510));
            for (int i = 0; i < amount; i++) {
                for (Item item : type.getRequired()) {
                    if (!player.getInventory().containsItem(item)) {
                        return true;
                    }
                }
                Item charmToSave = getCharm(type.getRequired());
                List<Item> requiredItems = new ArrayList<>(Arrays.asList(type.getRequired()));

                boolean perkIsTriggered = perkTriggered();

                if (perkIsTriggered) {
                    requiredItems.remove(charmToSave);
                }

                Item[] requiredItemsArray = new Item[requiredItems.size()];
                requiredItemsArray = requiredItems.toArray(requiredItemsArray);

                if (player.getInventory().remove(requiredItemsArray)) {
                    player.getInventory().add(type.getProduct());
                    player.getSkills().addExperience(Skills.SUMMONING, type.getExperience());
                    if (perkIsTriggered) {
                        charmsSaved++;
                        charmItemSaved = charmToSave;
                    }
                }
            }
            return true;
        }

        private Item getCharm(Item[] items) {
            return Arrays.stream(items).filter(item -> item.getName().contains("charm")).findFirst().get();
        }

        /**
         * @return Whether the best possible perk available is triggered; (PRO > AVID > KEEN)
         */
        private boolean perkTriggered() {
            List<Perk> summoningPerks = Arrays.asList(Perk.PRO_SUMMONER, Perk.AVID_SUMMONER, Perk.KEEN_SUMMONER);

            return player.getPerkManager().isTriggered(summoningPerks.stream()
                .filter(perk -> perk.enabled(player))
                .findFirst()
                .get());
        }

    }

    /**
     * Represents a summoning node type.
     *
     * @author 'Vexia
     */
    public static class SummoningNode {

        /**
         * Represents the base object.
         */
        private final Object base;

        /**
         * Represents the required items.
         */
        private final Item[] required;

        /**
         * Represents the product.
         */
        private final Item product;

        /**
         * Represents the experience.
         */
        private final double experience;

        /**
         * Represents the level.
         */
        private final int level;

        /**
         * Constructs a new {@code SummoningCreator} {@code Object}.
         *
         * @param required   the required items.
         * @param product    the product.
         * @param experience the experience.
         * @param level      the level.
         */
        public SummoningNode(final Object base, Item[] required, Item product, double experience, int level) {
            this.base = base;
            this.required = required;
            this.product = product;
            this.experience = experience;
            this.level = level;
        }

        /**
         * Gets the required.
         *
         * @return The required.
         */
        public Item[] getRequired() {
            return required;
        }

        /**
         * Gets the product.
         *
         * @return The product.
         */
        public Item getProduct() {
            return product;
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
         * Gets the level.
         *
         * @return The level.
         */
        public int getLevel() {
            return level;
        }

        /**
         * Gets the base.
         *
         * @return The base.
         */
        public Object getBase() {
            return base;
        }

        /**
         * Method used to check if the base is a pouch.
         *
         * @return the pouch.
         */
        public boolean isPouch() {
            return base instanceof SummoningPouch;
        }

        /**
         * Method used to loadPlayer a summoning node.
         *
         * @param node the node.
         * @return the summoning node.
         */
        public static SummoningNode parse(final Object node) {
            final Item[] required = node instanceof SummoningPouch ? ((SummoningPouch) node).getItems() : createList(((SummoningScroll) node).getItems());
            final Item product = node instanceof SummoningPouch ? new Item(((SummoningPouch) node).getPouchId(), 1) : new Item(((SummoningScroll) node).getItemId(), 10);
            final int level = node instanceof SummoningPouch ? ((SummoningPouch) node).getLevelRequired() : ((SummoningScroll) node).getLevel();
            final double experience = node instanceof SummoningPouch ? ((SummoningPouch) node).getCreateExperience() : ((SummoningScroll) node).getExperience();
            return new SummoningNode(node, required, product, experience, level);
        }

        /**
         * Method used to create the list.
         *
         * @param ids the ids.
         * @return the array of items.
         */
        private static Item[] createList(final int... ids) {
            Item[] list = new Item[ids.length];
            for (int i = 0; i < ids.length; i++) {
                list[i] = new Item(ids[i], 1);
            }
            return list;
        }
    }
}
