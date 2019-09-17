package org.gielinor.game.content.dialogue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gielinor.game.content.dialogue.impl.Dialogue;

/**
 * Represents an item select dialogue for easy button handling.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum OptionSelect {

    OPTION_SELECT(-1, -1),
    TWO_OPTION_ONE(2461, 2459),
    TWO_OPTION_TWO(2462, 2459),
    THREE_OPTION_ONE(2471, 2469),
    THREE_OPTION_TWO(2472, 2469),
    THREE_OPTION_THREE(2473, 2469),
    FOUR_OPTION_ONE(2482, 2480),
    FOUR_OPTION_TWO(2483, 2480),
    FOUR_OPTION_THREE(2484, 2480),
    FOUR_OPTION_FOUR(2485, 2480),
    FIVE_OPTION_ONE(2494, 2492),
    FIVE_OPTION_TWO(2495, 2492),
    FIVE_OPTION_THREE(2496, 2492),
    FIVE_OPTION_FOUR(2497, 2492),
    FIVE_OPTION_FIVE(2498, 2492),
    ONE_ITEM_SELECT_ONE(Dialogue.ITEM_SELECT_1.getInterfaceId(), new int[]{ 2799, 2798, 1748, 1747 }, new int[]{ 1, 5, -1, 28 }),
    TWO_ITEM_SELECT_ONE(Dialogue.ITEM_SELECT_2.getInterfaceId(), new int[]{ 8874, 8873, 8872, 8871 }, new int[]{ 1, 5, 10, -1 }),
    TWO_ITEM_SELECT_TWO(Dialogue.ITEM_SELECT_2.getInterfaceId(), new int[]{ 8878, 8877, 8876, 8875 }, new int[]{ 1, 5, 10, -1 }),
    THREE_ITEM_SELECT_ONE(Dialogue.ITEM_SELECT_3.getInterfaceId(), new int[]{ 8889, 8888, 8887, 8886 }, new int[]{ 1, 5, 10, -1 }),
    THREE_ITEM_SELECT_TWO(Dialogue.ITEM_SELECT_3.getInterfaceId(), new int[]{ 8893, 8892, 8891, 8890 }, new int[]{ 1, 5, 10, -1 }),
    THREE_ITEM_SELECT_THREE(Dialogue.ITEM_SELECT_3.getInterfaceId(), new int[]{ 8897, 8896, 8895, 8894 }, new int[]{ 1, 5, 10, -1 }),
    FOUR_ITEM_SELECT_ONE(Dialogue.ITEM_SELECT_4.getInterfaceId(), new int[]{ 8909, 8908, 8907, 8906 }, new int[]{ 1, 5, 10, -1 }),
    FOUR_ITEM_SELECT_TWO(Dialogue.ITEM_SELECT_4.getInterfaceId(), new int[]{ 8913, 8912, 8911, 8910 }, new int[]{ 1, 5, 10, -1 }),
    FOUR_ITEM_SELECT_THREE(Dialogue.ITEM_SELECT_4.getInterfaceId(), new int[]{ 8917, 8916, 8915, 8914 }, new int[]{ 1, 5, 10, -1 }),
    FOUR_ITEM_SELECT_FOUR(Dialogue.ITEM_SELECT_4.getInterfaceId(), new int[]{ 8921, 8920, 8919, 8918 }, new int[]{ 1, 5, 10, -1 }),
    FIVE_ITEM_SELECT_ONE(Dialogue.ITEM_SELECT_5.getInterfaceId(), new int[]{ 8949, 8948, 8947, 8946 }, new int[]{ 1, 5, 10, -1 }),
    FIVE_ITEM_SELECT_TWO(Dialogue.ITEM_SELECT_5.getInterfaceId(), new int[]{ 8953, 8952, 8951, 8950 }, new int[]{ 1, 5, 10, -1 }),
    FIVE_ITEM_SELECT_THREE(Dialogue.ITEM_SELECT_5.getInterfaceId(), new int[]{ 8957, 8956, 8955, 8954 }, new int[]{ 1, 5, 10, -1 }),
    FIVE_ITEM_SELECT_FOUR(Dialogue.ITEM_SELECT_5.getInterfaceId(), new int[]{ 8961, 8960, 8959, 8958 }, new int[]{ 1, 5, 10, -1 }),
    FIVE_ITEM_SELECT_FIVE(Dialogue.ITEM_SELECT_5.getInterfaceId(), new int[]{ 8965, 8964, 8963, 8962 }, new int[]{ 1, 5, 10, -1 }),
    SMELTING_BRONZE(-1, new int[]{ 3987, 3986, 2807, 2414 }, new int[]{ 1, 5, 10, -1 }),
    SMELTING_IRON(-1, new int[]{ 3991, 3990, 3989, 3988 }, new int[]{ 1, 5, 10, -1 }),
    SMELTING_SILVER(-1, new int[]{ 3995, 3994, 3993, 3992 }, new int[]{ 1, 5, 10, -1 }),
    SMELTING_STEEL(-1, new int[]{ 3999, 3998, 3997, 3996 }, new int[]{ 1, 5, 10, -1 }),
    SMELTING_GOLD(-1, new int[]{ 4003, 4002, 4001, 4000 }, new int[]{ 1, 5, 10, -1 }),
    SMELTING_MITHRIL(-1, new int[]{ 7441, 7440, 6397, 4158 }, new int[]{ 1, 5, 10, -1 }),
    SMELTING_ADAMANT(-1, new int[]{ 7446, 7444, 7443, 7442 }, new int[]{ 1, 5, 10, -1 }),
    SMELTING_RUNE(-1, new int[]{ 7450, 7449, 7448, 7447 }, new int[]{ 1, 5, 10, -1 }),
    GLASS_VIAL(-1, new int[]{ 11474, 11473, 11472, 11471 }, new int[]{ 1, 5, 10, -1 }),
    GLASS_ORB(-1, new int[]{ 12396, 12395, 12394, 11475 }, new int[]{ 1, 5, 10, -1 }),
    GLASS_BEER_GLASS(-1, new int[]{ 12400, 12399, 12398, 12397 }, new int[]{ 1, 5, 10, -1 }),
    GLASS_CANDLE_LANTERN(-1, new int[]{ 12404, 12403, 12402, 12401 }, new int[]{ 1, 5, 10, -1 }),
    GLASS_OIL_LAMP(-1, new int[]{ 12408, 12407, 12406, 12405 }, new int[]{ 1, 5, 10, -1 }),
    GLASS_FISHBOWL(-1, new int[]{ 6203, 6202, 6201, 6200 }, new int[]{ 1, 5, 10, -1 }),
    GLASS_LANTERN_LENS(-1, new int[]{ 12412, 12411, 12410, 12409 }, new int[]{ 1, 5, 10, -1 }),
    COOKING(1743, new int[]{ 13720, 13719, 13718, 13717 }, new int[]{ 1, 5, -1, 28 }),
    EXPERIENCE_LAMP_ATTACK(2808, 2812),
    EXPERIENCE_LAMP_STRENGTH(2808, 2813),
    EXPERIENCE_LAMP_RANGED(2808, 2814),
    EXPERIENCE_LAMP_MAGIC(2808, 2815),
    EXPERIENCE_LAMP_DEFENCE(2808, 2816),
    EXPERIENCE_LAMP_HITPOINTS(2808, 2817),
    EXPERIENCE_LAMP_PRAYER(2808, 2818),
    EXPERIENCE_LAMP_AGILITY(2808, 2819),
    EXPERIENCE_LAMP_HERBLORE(2808, 2820),
    EXPERIENCE_LAMP_THIEVING(2808, 2821),
    EXPERIENCE_LAMP_CRAFTING(2808, 2822),
    EXPERIENCE_LAMP_RUNECRAFT(2808, 2823),
    EXPERIENCE_LAMP_SLAYER(2808, 12034),
    EXPERIENCE_LAMP_FARMING(2808, 13914),
    EXPERIENCE_LAMP_MINING(2808, 2824),
    EXPERIENCE_LAMP_SMITHING(2808, 2825),
    EXPERIENCE_LAMP_FISHING(2808, 2826),
    EXPERIENCE_LAMP_COOKING(2808, 2827),
    EXPERIENCE_LAMP_FIREMAKING(2808, 2828),
    EXPERIENCE_LAMP_WOODCUTTING(2808, 2829),
    EXPERIENCE_LAMP_FLETCHING(2808, 2830),
    EXPERIENCE_LAMP_CONFIRM(2808, 2831),;

    /**
     * The id of a single option.
     */
    private int id;
    /**
     * The interface id of a single option.
     */
    private int interfaceId;
    /**
     * The ids of the buttons clicked on the interface.
     */
    private int[] ids;
    /**
     * The amounts these buttons represent.
     */
    private int[] amounts;
    /**
     * The child id.
     */
    private int childId;

    /**
     * Creates a select interface dialogue.
     *
     * @param id The id of the button clicked on the interface.
     */
    OptionSelect(int id, int interfaceId) {
        this.id = id;
        this.setInterfaceId(interfaceId);
    }

    /**
     * Creates an item select interface dialogue.
     *
     * @param interfaceId The id of the interface.
     * @param buttonIds   The ids of the buttons clicked on the interface.
     * @param amounts     The amounts these buttons represent.
     */
    OptionSelect(int interfaceId, int[] buttonIds, int[] amounts) {
        this.id = interfaceId;
        this.ids = buttonIds;
        this.amounts = amounts;
    }

    /**
     * Gets the id of the button clicked on the interface.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the interface id.
     *
     * @return The interface id.
     */
    public int getInterfaceId() {
        return interfaceId;
    }

    /**
     * Gets the ids of the buttons for this interface.
     *
     * @return The ids.
     */
    public int[] getIds() {
        return ids;
    }

    /**
     * Gets the amount this button represents.
     *
     * @return the amount.
     */
    public int[] getAmounts() {
        return amounts;
    }

    /**
     * Gets an option select for the button id.
     *
     * @param id The id of the button.
     * @return The option select.
     */
    public static OptionSelect forInterfaceId(int id) {
        OptionSelect optionSelect = null;
        for (OptionSelect os : OptionSelect.values()) {
            if (os.getInterfaceId() > -1 && os.getInterfaceId() == id) {
                optionSelect = os;
                break;
            }
        }
        return optionSelect;
    }

    /**
     * Gets an option select for the button id.
     *
     * @param id The id of the button.
     * @return The option select.
     */
    public static OptionSelect forId(int id) {
        OptionSelect optionSelect = null;
        for (OptionSelect os : OptionSelect.values()) {
            if (os.getId() > -1 && os.getId() == id) {
                optionSelect = os;
                break;
            }
        }
        // Assume its an item select
        if (optionSelect == null) {
            for (OptionSelect os : OptionSelect.values()) {
                if (os.getIds() != null) {
                    for (int buttonId : os.getIds()) {
                        if (id == buttonId) {
                            return os;
                        }
                    }
                }
            }
        }
        return optionSelect;
    }

    /**
     * Gets an amount for the item select dialogue.
     *
     * @param id The id of the button.
     * @return The amount.
     */
    public int amountForId(int id) {
        for (int i = 0; i < getIds().length; i++) {
            if (getIds()[i] == id) {
                return getAmounts()[i];
            }
        }
        return 0;
    }

    /**
     * Gets an amount for any of the item select dialogues from a button id.
     *
     * @param id The id of the button.
     * @return The amount.
     */
    public int getAmount(int id) {
        for (OptionSelect optionSelect : OptionSelect.values()) {
            if (optionSelect.getId() == -1 || optionSelect.getId() != this.id) {
                continue;
            }
            for (int index = 0; index < optionSelect.getIds().length; index++) {
                if (optionSelect.getIds()[index] == id) {
                    return optionSelect.getAmounts()[index];
                }
            }
        }
        return 0;
    }

    /**
     * Gets an index for the single option select by interface.
     *
     * @return The index.
     */
    public int getIndex() {
        List<OptionSelect> optionSelectList = new ArrayList<>();
        int index = 0;
        for (OptionSelect optionSelect : OptionSelect.values()) {
            if (optionSelect.getInterfaceId() != this.interfaceId) {
                continue;
            }
            optionSelectList.add(optionSelect);
        }
        for (OptionSelect optionSelect : optionSelectList) {
            if (optionSelect == null || optionSelect.getInterfaceId() == -1) {
                continue;
            }
            if (this.id == optionSelect.getId()) {
                return index;
            }
            index++;
        }
        throw new IndexOutOfBoundsException("Index 0 for OptionSelect provided");
    }

    /**
     * Gets the button id.
     *
     * @return The id of the button.
     */
    public int getButtonId() {
        return getIndex() + 1;
    }

    /**
     * Gets an index for any of the item select dialogues from a button id.
     *
     * @param button The id of the button.
     * @return The index.
     */
    public int getIndex(int button) {
        OptionSelect[] optionSelects = new OptionSelect[10];
        int i = 0;
        int index = 0;
        for (OptionSelect optionSelect : OptionSelect.values()) {
            if (optionSelect.getId() != this.id) {
                continue;
            }
            optionSelects[i] = optionSelect;
            i++;
        }
        for (OptionSelect optionSelect : optionSelects) {
            if (optionSelect == null || optionSelect.getIds() == null) {
                continue;
            }
            for (int buttonId : optionSelect.getIds()) {
                if (buttonId == button) {
                    return index;
                }
            }
            index++;
        }
        return 0;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setInterfaceId(int interfaceId) {
        this.interfaceId = interfaceId;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    @Override
    public String toString() {
        return OptionSelect.class.getName() + " [id=" + id + " interface=" + interfaceId + " childId=" + childId + " ids=" + (Arrays.toString(ids)) + " amounts=" + (Arrays.toString(amounts)) + "]";
    }
}
