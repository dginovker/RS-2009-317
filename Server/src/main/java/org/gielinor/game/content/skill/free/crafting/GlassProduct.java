package org.gielinor.game.content.skill.free.crafting;

import org.gielinor.game.content.dialogue.OptionSelect;

/**
 * Represents the glass product.
 *
 * @author 'Vexia
 */
public enum GlassProduct {
    VIAL(OptionSelect.GLASS_VIAL, 229, 1, 33, 35),
    ORB(OptionSelect.GLASS_ORB, 567, 1, 46, 52.5),
    BEER(OptionSelect.GLASS_BEER_GLASS, 1919, 1, 1, 17.5),
    CANDLE(OptionSelect.GLASS_CANDLE_LANTERN, 4527, 1, 4, 19),
    OIL_LAMP(OptionSelect.GLASS_OIL_LAMP, 4522, 1, 12, 25),
    FISHBOWL(OptionSelect.GLASS_FISHBOWL, 6667, 1, 42, 42.5),
    LANTERN_LENS(OptionSelect.GLASS_LANTERN_LENS, 4542, 1, 49, 55);

    /**
     * Represents the {@link org.gielinor.game.content.dialogue.OptionSelect}.
     */
    private final OptionSelect optionSelect;
    /**
     * Represents the product.
     */
    private final int product;
    /**
     * Represents the amount.
     */
    private final int amount;
    /**
     * Represents the level needed.
     */
    private final int level;
    /**
     * Represents the experience gained.
     */
    private final double experience;

    /**
     * Constructs a new {@code GlassDefinitions.java}.
     *
     * @param optionSelect The {@link org.gielinor.game.content.dialogue.OptionSelect}.
     * @param result       the product.
     * @param amount       the amount.
     * @param level        the level.
     * @param experience   the exp.
     */
    private GlassProduct(OptionSelect optionSelect, int result, int amount, int level, double experience) {
        this.optionSelect = optionSelect;
        this.product = result;
        this.amount = amount;
        this.level = level;
        this.experience = experience;
    }

    /**
     * Gets {@link org.gielinor.game.content.dialogue.OptionSelect}.
     *
     * @return The option select.
     */
    public OptionSelect getOptionSelect() {
        return optionSelect;
    }

    /**
     * Gets the product.
     *
     * @return The product.
     */
    public int getProduct() {
        return product;
    }

    /**
     * Gets the amount.
     *
     * @return The amount.
     */
    public int getAmount() {
        return amount;
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
     * Gets a {@link org.gielinor.game.content.skill.free.crafting.GlassProduct} by a button id.
     *
     * @param id The id.
     * @return The glass product.
     */
    public static GlassProduct forId(int id) {
        for (GlassProduct glassProduct : GlassProduct.values()) {
            for (int buttonId : glassProduct.getOptionSelect().getIds()) {
                if (id == buttonId) {
                    return glassProduct;
                }
            }
        }
        return null;
    }
}
