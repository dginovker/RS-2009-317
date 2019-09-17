package org.gielinor.game.content.global.consumable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.item.Item;

/**
 * Represents a repository of active consumables.
 *
 * @author 'Vexia
 */
public enum Consumables {
    /**
     * meats
     */
    CHICKEN(new Food(2140, 2138, 2144, new ConsumableProperties(3), new CookingProperties(1, 30, 30))),
    UGTHANKI(new Food(1861, 1859, 2146, new ConsumableProperties(2), new CookingProperties(1, 40, 40))),
    RABBIT(new Food(3228, 3226, 7222, new ConsumableProperties(5), new CookingProperties(1, 30, 30))),
    CRAB(new Food(7521, 7518, 7520, new ConsumableProperties(10), new CookingProperties(21, 100, 100))),
    /**
     * fish
     */
    KARAMBWANJI(new Food(3151, 3150, 592, new ConsumableProperties(3), new CookingProperties(1, 10, 30, "You cook the karambwanji.", "You accidentally burn the karambwanji to ashes."))),
    SARDINE(new Food(325, 327, 369, new ConsumableProperties(4), new CookingProperties(1, 40, 38))),
    ANCHOVIES(new Food(319, 321, 323, new ConsumableProperties(1), new CookingProperties(1, 30, 34))),
    HERRING(new Food(347, 345, 357, new ConsumableProperties(5), new CookingProperties(5, 50, 41))),
    MACKEREL(new Food(355, 353, 357, new ConsumableProperties(6), new CookingProperties(10, 60, 45))),
    TROUT(new Food(333, 335, 343, new ConsumableProperties(7), new CookingProperties(15, 70, 50, 49, 50))),
    COD(new Food(339, 341, 343, new ConsumableProperties(7), new CookingProperties(18, 75, 52))),
    PIKE(new Food(351, 349, 343, new ConsumableProperties(8), new CookingProperties(20, 80, 53))),
    SALMON(new Food(329, 331, 343, new ConsumableProperties(9), new CookingProperties(25, 58, 90))),
    SLIMY_EEL(new Food(3381, 3379, 3383, new ConsumableProperties(6), new CookingProperties(28, 95, 58))),
    TUNA(new Food(361, 359, 367, new ConsumableProperties(10), new CookingProperties(30, 100, 64, 63, 63))),
    RAINBOW_FISH(new Food(10136, 10138, 10140, new ConsumableProperties(7), new CookingProperties(35, 110, 60))),
    CAVE_EEL(new Food(5003, 5001, 5002, new ConsumableProperties(7), new CookingProperties(38, 115, 40))),
    LOBSTER(new Food(379, 377, 381, new ConsumableProperties(12), new CookingProperties(40, 120, 74, 74, 68))),
    BASS(new Food(365, 363, 367, new ConsumableProperties(13), new CookingProperties(43, 130, 80, 80, 70))),
    SWORDFISH(new Food(373, 371, 375, new ConsumableProperties(14), new CookingProperties(45, 140, 86, 86, 81))),
    LAVA_EEL(new Food(2149, 2148, 3383, new ConsumableProperties(14), new CookingProperties(53, 30, 53))),
    MONKFISH(new Food(7946, 7944, 7948, new ConsumableProperties(16), new CookingProperties(62, 150, 92, 90, 89))),
    SHARK(new Food(385, 383, 387, new ConsumableProperties(20), new CookingProperties(80, 210, 100, 100, 94))),
    ANGLERFISH(new Food(13441, 13439, 13443, new ConsumableProperties(22), new CookingProperties(84, 120, 100, 100, 97))),
    SEA_TURTLE(new Food(397, 395, 399, new ConsumableProperties(21), new CookingProperties(82, 212, 100))),
    DARK_CRAB(new Food(11936, 11934, 3146, new ConsumableProperties(22), new CookingProperties(90, 215, 100))),
    MANTA_RAY(new Food(391, 389, 393, new ConsumableProperties(22), new CookingProperties(91, 216, 100))),
    KARAMBWAN(new Food(3144, 3142, 11938, new ConsumableProperties(18), new CookingProperties(1, 80, 30))),

    /**
     * leaping fish
     */
    ROE(new Food(11324, 3)),
    /**
     * snails
     */
    THIN_SNAIL(new Food(3369, 3363, 3375, new ConsumableProperties(5), new CookingProperties(12, 70, 70))),
    LEAN_SNAIL(new Food(3371, 3365, 3375, new ConsumableProperties(8), new CookingProperties(17, 80, 80))),
    FAT_SNAIL(new Food(3373, 3367, 3375, new ConsumableProperties(9), new CookingProperties(22, 95, 95))),
    /**
     * cakes
     */
    TWO_THIRD_CAKE(new Food(1893, new ConsumableProperties(4, 1895))),
    SLICE_OF_CAKE(new Food(1895, new ConsumableProperties(4))),
    CHOCOLATE_CAKE(new Food(1897, new ConsumableProperties(5, 1899))),
    TWO_THIRD_CHOCOLATE_CAKE(new Food(1899, new ConsumableProperties(5, 1901))),
    SLICE_OF_CHOCOLATE_CAKE(new Food(1901, new ConsumableProperties(5))),
    /**
     * special
     */
    PUMPKIN(new Food(1959, 14)),
    EASTER_EGG(new Food(1961, 14)),
    /**
     * fruits
     */
    BANANA(new Food(1963, 2)),
    LEMON(new Food(2102, 2)),
    LIME(new Food(2120, 2)),
    ORANGE(new Food(2108, 2)),
    PAPAYA_FRUIT(new Food(5972, 2)),
    STRAWBERRY(new Food(5504, 2)),
    TOMATO(new Food(1982, 2)),
    PEACH(new Food(6883, 8)),
    /**
     * vegetables
     */
    CABAGE(new Food(1965, 2, "You eat the cabbage. Yuck!")),
    ONION(new Food(1957, 2, "It's always sad to see a grown man/woman cry")),
    /**
     * misc
     */
    SWEET_CORN(new Food(5988, 5986, 5990, new ConsumableProperties(3), new CookingProperties(28, 104, 70))),
    SODA_ASH(new Food(1781, 401, 1781, null, new CookingProperties(1, 1, 1, "You burn the seaweed into soda ash."))),
    BAKED_POTATO(new Food(6701, 1942, 6699, new ConsumableProperties(2), new CookingProperties(7, 15, 36, "You succesfully bake the potato.", CookingProperties.FAIL_MESSAGE))),
    POTATO_WITH_BUTTER(new Food(6703, new ConsumableProperties(7))),
    POTATO_WITH_CHEESE(new Food(6705, new ConsumableProperties(9))),
    EGG_POTATO(new Food(7056, new ConsumableProperties(11))),
    MUSHROOM_POTATO(new Food(7058, new ConsumableProperties(16))),
    TUNA_POTATO(new Food(7060, new ConsumableProperties(22))),
    CHILLI_POTATO(new Food(7054, new ConsumableProperties(14))),
    POT_OF_FLOUR(new Food(1933, 1931, "You empty the contents of the pot onto the floor.", null)),
    BUCKET_OF_SAND(new Food(1783, 1925, "You empty the contents of the bucket onto the floor.", null)),
    BUCKET_OF_MILK(new Food(1927, 1925, "You empty the contents of the bucket onto the floor.", null)),
    BUCKET_OF_WATER(new Food(1929, 1925, "You empty the contents of the bucket onto the floor.", null)),
    BUCKET_OF_COMPOST(new Food(6032, 1925, "You empty the bucket of compost.", null)),
    BUCKET_OF_SUPERCOMPOST(new Food(6034, 1925, "You empty the bucket of supercompost.", null)),
    VIAL_OF_WATER(new Food(227, Consumable.VIAL.getId(), "You empty the vial.", null)),
    BOWL_OF_SAND(new Food(1921, 1923, "You empty the contents of the bowl onto the floor.", null)),
    JUG_OF_WATER(new Food(1937, 1935, "You empty the contents of the jug onto the floor.", null)),
    FIELD_RATION(new Food(7934, new ConsumableProperties(12))),
    /**
     * drinks
     */
    CHOCOLATEY_MILK(new Drink(1977, new ConsumableProperties(2, 1925))),
    CUP_OF_NETTLE_TEA(new Drink(4838, new ConsumableProperties(2, 1980))),
    GIN(new Drink(2019, new ConsumableProperties(2, 7921))),
    WHISKY(new Drink(2017, new ConsumableProperties(2, 7921))),
    VODKA(new Drink(2015, new ConsumableProperties(2, 7921))),
    JUG_OF_WINE(new Drink(1993, new ConsumableProperties(7, 1935))),
    KARAMJAN_RUN(new Drink(431, new ConsumableProperties(2))),
    KHALI_BREW(new Drink(77, new ConsumableProperties(2, 7921))),
    NETTLE_TEA(new Drink(4239, new ConsumableProperties(4, 1923))),
    BOTTLE_OF_WINE(new Drink(7919, new ConsumableProperties(2, 7921))),
    /**
     * potions
     */
    STRENGTH_POTION(new Potion(PotionEffect.STRENGTH_POTION)),
    ATTACK_POTION(new Potion(PotionEffect.ATTACK_POTION)),
    DEFENCE_POTION(new Potion(PotionEffect.DEFENCE_POTION)),
    RANGING_POTION(new Potion(PotionEffect.RANGING_POTION)),
    MAGIC_POTION(new Potion(PotionEffect.MAGIC_POTION)),
    SUPER_STRENGTH_POTION(new Potion(PotionEffect.SUPER_STRENGTH)),
    SUPER_ATTACK_POTION(new Potion(PotionEffect.SUPER_ATTACK)),
    SUPER_DEFENCE_POTION(new Potion(PotionEffect.SUPER_DEFENCE)),
    AGILITY_POTION(new Potion(PotionEffect.AGILITY_POTION)),
    HUNTER_POTION(new Potion(PotionEffect.HUNTER_POTION))
    ;

    /**
     * Represents the consumable.
     */
    private Consumable consumable;

    /**
     * Represents the list of foods only. This list can be used for direct searching.
     */
    private static final List<Food> FOODS = new ArrayList<>();

    /**
     * Represents te list of drinks only. This list can be used for direct searching.
     */
    private static final List<Drink> DRINKS = new ArrayList<>();

    /**
     * Represents the list of all consumables.
     */
    private static final List<Consumable> CONSUMABLES = new ArrayList<>();

    /**
     * Constructs a new {@code Consumables} {@code Object}.
     *
     * @param consumable the consumbale.
     */
    Consumables(Consumable consumable) {
        this.consumable = consumable;
    }

    /**
     * Constructs a new {@code Consumables} {@code Object}.
     *
     * @param drink the consumable.
     */
    Consumables(Drink drink) {
        this.consumable = drink;
    }

    /**
     * Gets the food.
     *
     * @return The food.
     */
    public Consumable getConsumable() {
        return consumable;
    }

    /**
     * Gets the list of foods.
     *
     * @return the foods.
     */
    public static List<Food> getFoods() {
        return FOODS;
    }

    /**
     * Method used to get the {@link Consumable} by the item associated with it.
     *
     * @param item the raw item.
     * @return the consumable.
     */
    public static Optional<Consumable> find(final Item item) {
        for (Consumable consumable : CONSUMABLES) {
            if (consumable.isDrink()) {
                final Optional<Consumable> optionalConsumable = findDrink(item);
                if (optionalConsumable.isPresent())
                    return optionalConsumable;
            }
            if (consumable.getItem().getId() == item.getId())
                return Optional.of(consumable);
        }
        return Optional.empty();
    }

    /**
     * Method used to get the {@link Food} by the item associated with it.
     *
     * @param item the item.
     * @return the food.
     * @note this is a more direct search.
     */
    public static Optional<Food> findFood(final Item item) {
        for (Food food : FOODS)
            if (food.getItem().getId() == item.getId())
                return Optional.of(food);
        return Optional.empty();
    }

    /**
     * Method used to get the {@link Food} by the raw item associated with it.
     *
     * @param raw the raw item.
     * @return the food.
     */
    public static Food forRaw(final Item raw) {
        for (Food food : FOODS) {
            if (food.hasRaw() && food.getRaw().getId() == raw.getId()) {
                return food;
            }
        }
        return null;
    }

    /**
     * Method used to get the {@link Food} by the raw item associated with it.
     *
     * @param raw the raw item.
     * @return the food.
     */
    public static Food forRaw(final int raw) {
        for (Food food : FOODS) {
            if (food.hasRaw() && food.getRaw().getId() == raw) {
                return food;
            }
        }
        return null;
    }

    /**
     * Method used to get the {@link Drink} by the item id.
     *
     * @param item the item.
     * @return the drink.
     */
    public static Optional<Consumable> findDrink(final Item item) {
        for (Drink drink : DRINKS) {

            if(Objects.isNull(drink) || Objects.isNull(drink.getItem()))
                continue;

            if (item.getId() == drink.getItem().getId())
                return Optional.of(drink);

            if (Objects.isNull(drink.getDrinks()))
                continue;

            for (Item i : drink.getDrinks())
                if (i.getId() == item.getId())
                    return Optional.of(drink);
        }
        return Optional.empty();
    }

    /**
     * Method used to add a consumable to its search engine.
     *
     * @param consumable the consumable.
     */
    public static void add(final Consumable consumable) {
        if (consumable.isDrink()) {
            DRINKS.add(consumable.asDrink());
        } else if (consumable.isFood()) {
            FOODS.add(consumable.asFood());
        }
        CONSUMABLES.add(consumable);
    }

    /**
     * Static modifier used to populate search engine lists.
     */
    static {
        for (Consumables consumable : Consumables.values()) {
            if (consumable.getConsumable().isFood()) {
                FOODS.add(consumable.getConsumable().asFood());
            } else if (consumable.getConsumable().isDrink()) {
                DRINKS.add(consumable.getConsumable().asDrink());
            }
        }
    }
}
