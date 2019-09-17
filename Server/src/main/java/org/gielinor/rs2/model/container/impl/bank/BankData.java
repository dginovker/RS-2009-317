package org.gielinor.rs2.model.container.impl.bank;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.InterfaceConfiguration;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.rs2.config.Constants;
import org.gielinor.utilities.misc.RunScript;

/**
 * Represents a player's bank data for tabs.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class BankData implements SavingModule {

    /**
     * The {@link org.gielinor.game.node.entity.player.Player}.
     */
    private final Player player;
    /**
     * The current open bank tab.
     */
    private int openTab = 0;
    /**
     * The tab amounts.
     */
    private int[] tabAmounts;
    /**
     * If the tab amounts were updated.
     */
    private boolean tabsUpdated;
    /**
     * If the player is searching their bank.
     */
    private boolean searchMode;

    /**
     * Constructs a new {@link org.gielinor.rs2.model.container.impl.bank.BankData}.
     *
     * @param player The {@link org.gielinor.game.node.entity.player.Player}.
     */
    public BankData(Player player) {
        this.player = player;
        this.tabAmounts = new int[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    }

    /**
     * Gets the current open bank tab.
     *
     * @return The open bank tab.
     */
    public int getOpenTab() {
        return openTab;
    }

    /**
     * Sets the current open bank tab.
     *
     * @param openTab The current open bank tab.
     */
    public void setOpenTab(int openTab) {
        this.openTab = openTab;
        player.getInterfaceState().force(InterfaceConfiguration.CURRENT_BANK_TAB, getOpenTab(), true);
        if (Arrays.stream(getTabAmounts()).sum() != player.getBank().itemCount()) {
            setTabAmounts(new int[]{ player.getBank().itemCount(), 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        }
        player.getActionSender().sendString((getOpenTab() == 0 ? "The Bank of " + Constants.SERVER_NAME : "Tab " + getOpenTab()), 5383);
        player.getBank().updateScroll();
    }

    /**
     * Gets the count of bank tabs.
     *
     * @return The count.
     */
    public int getBankTabCount() {
        int tabCount = 0;
        for (int tabAmount : getTabAmounts()) {
            if (tabAmount > 0) {
                tabCount += 1;
            }
        }
        return tabCount;
    }

    /**
     * Gets the item count before a tab.
     *
     * @param bankTab The bank tab.
     * @return The tab amount.
     */
    public int getTabAmountBefore(int bankTab) {
        int itemCount = 0;
        for (int index = 0; index < bankTab; index++) {
            itemCount += tabAmounts[index];
        }
        return itemCount;
    }

    /**
     * Gets the tab amounts.
     *
     * @return The tab amounts.
     */
    public int[] getTabAmounts() {
        return tabAmounts;
    }

    /**
     * Gets a tab amount.
     *
     * @param bankTab The bank tab.
     * @return The tab amount.
     */
    public int getTabAmount(int bankTab) {
        return tabAmounts[bankTab];
    }

    /**
     * Gets a tab amount from 0.
     *
     * @param bankTab The bank tab.
     * @return The tab amount.
     */
    public int getTabIndex(int bankTab) {
        return tabAmounts[bankTab] - 1;
    }

    /**
     * Sets the tab amounts.
     *
     * @param tabAmounts The tab amounts.
     */
    public void setTabAmounts(int[] tabAmounts) {
        this.tabAmounts = tabAmounts;
    }

    /**
     * Sets a tab amount.
     *
     * @param bankTab   The bank tab.
     * @param tabAmount The tab amount to set.
     */
    public void setTabAmount(int bankTab, int tabAmount) {
        this.tabAmounts[bankTab] = tabAmount;
    }

    /**
     * Resets the tab amounts.
     */
    public void resetTabAmounts() {
        Arrays.fill(tabAmounts, 0);
    }

    /**
     * Gets the last withdraw-x number.
     *
     * @return The last withdrawn x number.
     */
    public int getLastX() {
        return player.getInterfaceState().get(InterfaceConfiguration.BANK_LAST_X);
    }

    /**
     * Sets the last withdraw-x number.
     *
     * @param lastX The last withdrawn x number.
     */
    public void setLastX(int lastX) {
        player.getInterfaceState().force(InterfaceConfiguration.BANK_LAST_X, lastX, true);
    }

    /**
     * Gets if the player is in search mode.
     *
     * @return <code>True</code> if so.
     */
    public boolean isSearchMode() {
        return searchMode;
    }

    /**
     * Toggles if the player is in search mode.
     */
    public void setSearchMode(boolean searchMode) {
        this.searchMode = searchMode;
        player.getConfigManager().send(1012, isSearchMode() ? 1 : 0);
        if (isSearchMode()) {
            player.setAttribute("runscript", new RunScript() {

                @Override
                public boolean handle() {
                    setSearchMode(false);
                    return true;
                }
            });
            player.getDialogueInterpreter().sendInput(true, "Enter search term:");
        }
    }

    /**
     * Gets a deposit slot.
     *
     * @return The deposit slot.
     */
    public int getDepositSlot() {
        return openTab == 0 ? 0 : (tabAmounts[openTab - 1] - 1);
    }

    /**
     * Gets the offset of a tab.
     *
     * @param bankTab The bank tab.
     * @return The first slot index.
     */
    public int getTabOffset(int bankTab) {
        int offset = 0;
        for (int index = 0; index < getTabAmounts().length; index++) {
            if (index == bankTab) {
                break;
            } else if (getTabAmounts()[index] > 0) {
                offset += getTabAmounts()[index];
            }
        }
        return offset;
    }

    /**
     * Gets the bank tab for an item.
     *
     * @param itemSlot The slot of the item.
     * @param deposit  If we're depositing into a tab.
     * @return The tab.
     */
    public int getBankTab(int itemSlot, boolean deposit) {
        if (!deposit) {
            if (getOpenTab() > 0) {
                return getOpenTab();
            }
            for (int bankTab = 0; bankTab < 9; bankTab++) {
                int start = getTabAmount(bankTab) + getTabAmountBefore(bankTab) + 1;
                int ending = bankTab == 0 ? 0 : getTabIndex(bankTab - 1);
                if (itemSlot >= ending && itemSlot < start) {
                    return bankTab;
                }
            }
            return 0;
        }
        int totalSlots = 0;
        for (int bankTab = 0; bankTab < (itemSlot + 1); bankTab++) {
            totalSlots += tabAmounts[bankTab];
        }
        return totalSlots - 1;
    }

    /**
     * Handles bank tab swapping.
     *
     * @param slot    The slot the item is currently in.
     * @param toTab   The bank tab the item is being moved to.
     * @param refresh Whether or not to refresh the container.
     */
    public void handleBankTab(int slot, int toTab, boolean refresh) {
        int fromTab = getBankTab(slot, false);
        if (fromTab == toTab || (toTab > 1 && tabAmounts[toTab - 1] == 0 && tabAmounts[toTab] == 0)) {
            return;
        }
        int to = getData(toTab, 1);

        if (to > slot) {
            changeTabAmount(toTab, 1, false);
            changeTabAmount(fromTab, -1, refresh, false);
            player.getBank().insert(slot, to, false);
        } else {
            changeTabAmount(toTab, 1, false);
            changeTabAmount(fromTab, -1, refresh, false);
            to = getData(toTab, 1);
            player.getBank().insert(slot, to, false);
        }
        if (refresh) {
            player.getBank().update();
            player.getBank().refresh();
        }
    }

    public void changeTabAmount(int tab, int amount, boolean collapse, boolean collapseAll) {
        tabAmounts[tab] += amount;
        if (tabAmounts[tab] <= 0 && collapse) {
            collapse(tab, 0, collapseAll);
        }
    }

    public void changeTabAmount(int tab, int amount, boolean collapse) {
        changeTabAmount(tab, amount, collapse, true);
    }

    /**
     * Updates a tab's count.
     *
     * @param bankTab  The bank tab.
     * @param amount   The amount of items in the tab.
     * @param collapse Whether or not we're collapsing the tab.
     */
    public int decreaseTabCount(int bankTab, int amount) {
        tabAmounts[bankTab] -= amount;
        return tabAmounts[bankTab];
    }

    public void collapse(int tab, int toTab) {
        collapse(tab, toTab, true);
    }

    public void collapse(int tab, int toTab, boolean collapseAll) {
        if (tab == 0 && collapseAll) {
            Arrays.fill(tabAmounts, 0);
            tabAmounts[0] = player.getBank().itemCount();
            player.getBank().shift();
            player.getBank().update();
            player.getBank().refresh();
            player.getBank().updateScroll();
            return;
        }

        if (toTab == 0) {
            player.getInterfaceState().force(InterfaceConfiguration.CURRENT_BANK_TAB, 0, true);
            openTab = 0;
        }

        if (toTab == tab && tab != 0 || tab > 9) {
            player.getBank().shift();
            player.getBank().update();
            player.getBank().refresh();
            player.getBank().updateScroll();
            return;
        }

        final int initialTabAmount = tabAmounts[tab];

        for (int fromSlot = 0; fromSlot < initialTabAmount; fromSlot++) {
            changeTabAmount(tab, -1, false);
            changeTabAmount(toTab, 1, false);
            player.getBank().insert(getData(tab, 1) - tabAmounts[tab], getData(toTab, 1));
        }

        collapse(tab + 1, tab);
    }

    public int getData(int input, int type) {
        int totalSlots = 0;
        for (int tab = 0; tab < (type == 1 ? input + 1 : 10); tab++) {
            if (type == 0 && input <= totalSlots + tabAmounts[tab] - 1 && input >= totalSlots) {
                return tab;
            }
            totalSlots += tabAmounts[tab];
        }
        return totalSlots - 1;
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        for (int tabAmount : tabAmounts) {
            byteBuffer.putInt(tabAmount);
        }
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        for (int bankTab = 0; bankTab < tabAmounts.length; bankTab++) {
            tabAmounts[bankTab] = byteBuffer.getInt();
        }
        if (Arrays.stream(getTabAmounts()).sum() != player.getBank().itemCount()) {
            setTabAmounts(new int[]{ player.getBank().itemCount(), 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        }
    }

    /**
     * @return
     */
    public boolean tabsUpdated() {
        return tabsUpdated;
    }
}
