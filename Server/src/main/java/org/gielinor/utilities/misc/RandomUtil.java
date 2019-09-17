package org.gielinor.utilities.misc;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.item.ChanceItem;

/**
 * Represents a class used for random methods.
 *
 * @author 'Vexia;
 *
 */
public class RandomUtil {

    /**
     * The random instance.
     */
    public static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    /**
     * Constructs a new {@code RandomFunction} {@code Object}.
     */
    private RandomUtil() {

    }

    /**
     * Method used to ease the access of the random class.
     *
     * @param min
     *            the minium random value.
     * @param max
     *            the maximum random value.
     * @return the value as an {@link Double}.
     */
    public static double random(double min, double max) {
        final double n = Math.abs(max - min);

        return Math.min(min, max) + (n == 0 ? 0 : random((int) n));
    }

    /**
     * Method used to ease the access of the random class.
     *
     * @param min
     *            the minium random value.
     * @param max
     *            the maximum random value.
     * @return the value as an {@link Integer}.
     */
    public static int random(int min, int max) {
        final int n = Math.abs(max - min);

        return Math.min(min, max) + (n == 0 ? 0 : random(n));
    }

    /**
     * Method used to return a random integer.
     *
     * @param value
     *            the value.
     * @return the integer.
     */
    public static int getRandomizer(int value) {
        return getRandom(1) == 0 ? value : -value;
    }

    /**
     * Method used to return the integer.
     *
     * @param maxValue
     *            the value.
     * @return the value.
     */
    public static int getRandom(int maxValue) {
        return (int) (RANDOM.nextDouble() * (maxValue + 1));
    }

    /**
     * Method used to return the random double.
     *
     * @param maxValue
     *            the value.
     * @return the double.
     */
    public static double getRandomDouble(double maxValue) {
        return (RANDOM.nextDouble() * (maxValue + 1));
    }

    /**
     * Method used to ease the access of the random class.
     *
     * @param maxValue
     *            the maximum value.
     * @return the random integer.
     */
    public static int random(int maxValue) {
        if (maxValue <= 0) {
            return 0;
        }

        return RANDOM.nextInt(maxValue);
    }

    /**
     * Gets a chance item.
     *
     * @param items
     *            the items.
     * @return the chance.
     */
    public static ChanceItem getChanceItem(final ChanceItem[] items) {
        double total = 0;
        for (ChanceItem i : items) {
            total += i.getChanceRate();
        }
        final int random = random((int) total);
        double subTotal = 0;
        for (ChanceItem i : items) {
            subTotal += i.getChanceRate();
            if (random < subTotal) {
                return i;
            }
        }
        return null;
    }
    /**
     * Gets a optional chance item.
     *
     * @param items
     *            the items.
     * @return the chance.
     */
    public static Optional<ChanceItem> findChanceItem(final ChanceItem[] items) {

        double total = 0;
        for (ChanceItem i : items)
            total += i.getChanceRate();

        final int random = random((int) total);
        double subTotal = 0;

        for (ChanceItem chanceItem : items) {
            subTotal += chanceItem.getChanceRate();
            if (random < subTotal)
                return Optional.of(chanceItem);
        }
        return Optional.empty();
    }
    /**
     * Gets a random element from a {@code List}. All elements are equally weighted.
     * @param list A list of elements to select from at random. All elements have equal weight. Not null.
     * @return A random element in the list (unless there is only 1 item in the list; that singular item is guaranteed).
     *         {@code null} elements in lists are allowed.
     * @throws NullPointerException if supplied list is {@code null}.
     * @throws IllegalStateException if supplied a list where {@code list.isEmpty()} returns {@code true}.
     */
    public static <T> T random(List<T> list) {
        int size = list.size();
        if (size == 0)
            throw new IllegalStateException("empty list");
        int choice = random(size);
        return list.get(choice);
    }

    /**
     * Gets a random value from the array.
     *
     * @param array
     *            The array.
     * @return A random element of the array.
     */
    public static <T> T getRandomElement(T[] array) {
        return array[randomize(array.length)];
    }

    /**
     * Randomizes the value.
     *
     * @param value
     *            the value to randomize.
     * @return The random value.
     */
    public static int randomize(int value) {
        if (value < 1) {
            return 0;
        }

        return RANDOM.nextInt(value);
    }
    public enum Odds {

        NOT_LIKELY(10),
        LIKELY(5),
        VERY_LIKELY(3);

        private int odds;

        Odds(int odds) {
            this.odds = odds;
        }

        public int getRange() {
            return odds;
        }

        public boolean hit() {
            return RandomUtil.random(odds) == 1;
        }
    }
}
