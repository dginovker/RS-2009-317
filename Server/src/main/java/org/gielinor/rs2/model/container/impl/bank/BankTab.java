package org.gielinor.rs2.model.container.impl.bank;

/**
 * Represents a bank tab.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum BankTab {

    TAB_ALL,
    TAB_TWO,
    TAB_THREE,
    TAB_FOUR,
    TAB_FIVE,
    TAB_SIX,
    TAB_SEVEN,
    TAB_EIGHT,
    TAB_NINE,
    TAB_TEN;

    /**
     * Constructs a new {@link org.gielinor.rs2.model.container.impl.bank.BankTab}.
     */
    BankTab() {
        /**
         * empty.
         */
    }

    /**
     * Gets a bank tab by the ordinal.
     *
     * @param ordinal The ordinal.
     * @return The {@link org.gielinor.rs2.model.container.impl.bank.BankTab}.
     */
    public static BankTab forIndex(int ordinal) {
        for (BankTab bankTab : values()) {
            if (bankTab.ordinal() == ordinal) {
                return bankTab;
            }
        }
        return null;
    }

    /**
     * Gets a bank tab by the id of the button.
     *
     * @param id The id of the button.
     * @return The {@link org.gielinor.rs2.model.container.impl.bank.BankTab}.
     */
    public static BankTab forId(int id) {
        int index = 0;
        for (BankTab bankTab : values()) {
            if ((32856 + index) == id) {
                return bankTab;
            }
            index += 4;
        }
        return null;
    }
}
