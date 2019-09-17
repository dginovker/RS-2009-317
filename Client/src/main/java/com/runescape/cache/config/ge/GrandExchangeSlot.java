package com.runescape.cache.config.ge;

import com.runescape.cache.media.RSComponent;

/**
 * Represents a Grand Exchange slot.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public enum GrandExchangeSlot {

    SLOT_1(
            new int[]{24926, 24927, 24929, 24930},
            new int[]{24998, 24999},
            new int[]{24967, 24968, 24969, 24994, 24995, 24996, 24997, 24998, 24999},
            24967, 24996, 24995, 24997
    ),
    SLOT_2(
            new int[]{24933, 24934, 24936, 24937},
            new int[]{25004, 25005},
            new int[]{24971, 24972, 24973, 25000, 25001, 25002, 25003, 25004, 25005},
            24971, 25002, 25001, 25003
    ),
    SLOT_3(
            new int[]{24940, 24941, 24943, 24944},
            new int[]{25010, 25011},
            new int[]{24975, 24976, 24977, 25006, 25007, 25008, 25009, 25010, 25011},
            24975, 25008, 25007, 25009
    ),
    SLOT_4(
            new int[]{24947, 24948, 24950, 24951},
            new int[]{25016, 25017},
            new int[]{24979, 24980, 24981, 25012, 25013, 25014, 25015, 25016, 25017},
            24979, 25014, 25013, 25015
    ),
    SLOT_5(
            new int[]{24954, 24955, 24957, 24958},
            new int[]{25022, 25023},
            new int[]{24983, 24984, 24985, 25018, 25019, 25020, 25021, 25022, 25023},
            24983, 25020, 25019, 25021
    ),
    SLOT_6(
            new int[]{24961, 24962, 24964, 24965},
            new int[]{25028, 25029},
            new int[]{24987, 24988, 24989, 25024, 25025, 25026, 25027, 25028, 25029},
            24987, 25026, 25025, 25027
    ),;

    /**
     * The buy and sell button ids.
     */
    private final int[] buttonIds;
    /**
     * The status bar and submitting text ids.
     */
    private final int[] statusIds;
    /**
     * The offer ids.
     */
    private final int[] offerIds;
    /**
     * The tooltip status text id.
     */
    private final int statusId;
    /**
     * The item name id.
     */
    private final int itemNameId;
    /**
     * The item sprite id.
     */
    private final int itemId;
    /**
     * The offered value text id.
     */
    private final int valueId;
    /**
     * If this slot is disabled currently.
     */
    private boolean disabled;

    /**
     * Constructs a <code>GrandExchangeSlot</code>.
     *
     * @param buttonIds  The buy and sell button ids.
     * @param statusIds  The status bar and submitting text ids.
     * @param offerIds   The offer ids.
     * @param statusId   The tooltip status text id.
     * @param itemNameId The item name id.
     * @param itemId     The item sprite id.
     * @param valueId    The offered value text id.
     */
    GrandExchangeSlot(int[] buttonIds, int[] statusIds, int[] offerIds, int statusId, int itemNameId, int itemId, int valueId) {
        this.buttonIds = buttonIds;
        this.statusIds = statusIds;
        this.offerIds = offerIds;
        this.statusId = statusId;
        this.itemNameId = itemNameId;
        this.itemId = itemId;
        this.valueId = valueId;
    }

    /**
     * Gets a <code>GrandExchangeSlot</code> by the index.
     *
     * @param index The index.
     * @return The slot.
     */
    public static GrandExchangeSlot forId(int index) {
        for (GrandExchangeSlot grandExchangeSlot : values()) {
            if (grandExchangeSlot.ordinal() == (index - 1)) {
                return grandExchangeSlot;
            }
        }
        return null;
    }

    /**
     * Gets a <code>GrandExchangeSlot</code> by the first button id.
     *
     * @param buttonId The id.
     * @return The slot.
     */
    public static GrandExchangeSlot forButtonId(int buttonId) {
        for (GrandExchangeSlot grandExchangeSlot : values()) {
            if (grandExchangeSlot.getButtonIds()[0] == buttonId) {
                return grandExchangeSlot;
            }
        }
        return null;
    }

    /**
     * Gets the buy and sell button ids.
     *
     * @return The button ids.
     */
    public int[] getButtonIds() {
        return buttonIds;
    }

    /**
     * Gets the status bar and submitting text ids.
     *
     * @return The status ids.
     */
    public int[] getStatusIds() {
        return statusIds;
    }

    /**
     * Gets the offer ids.
     *
     * @return The offer ids.
     */
    public int[] getOfferIds() {
        return offerIds;
    }

    /**
     * Gets the tooltip status text id.
     *
     * @return The status id.
     */
    public int getStatusId() {
        return statusId;
    }

    /**
     * Gets the item name text id.
     *
     * @return The id.
     */
    public int getItemNameId() {
        return itemNameId;
    }

    /**
     * Gets the item sprite id.
     *
     * @return The id.
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Gets the offered value text id.
     *
     * @return The id.
     */
    public int getValueId() {
        return valueId;
    }

    /**
     * Gets if this slot is currently disabled.
     *
     * @return <code>True</code> if so.
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Gets if this slot is currently disabled.
     *
     * @param disabled If this slot is disabled.
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Clears this slot.
     */
    public void clear() {
        RSComponent.forId(getItemNameId()).disabledMessage = "";
        RSComponent.forId(getItemId()).inventory[0] = -1;
        RSComponent.forId(getItemId()).inventoryValue[0] = -1;
        RSComponent.forId(getStatusId()).popupString = "";
        RSComponent.forId(getValueId()).disabledMessage = "";
    }
}
