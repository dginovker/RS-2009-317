package org.gielinor.game.node.item;

import org.gielinor.game.content.global.distraction.treasuretrail.TreasureTrailManager;
import org.gielinor.game.node.entity.npc.drop.DropFrequency;
import org.gielinor.game.node.entity.npc.drop.NPCDropTables;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents an item with a chance-rate.
 *
 * @author Emperor
 */
public final class ChanceItem extends Item {

    /**
     * The chance rate of this item.
     */
    private double chanceRate;

    /**
     * The minimum amount.
     */
    private int minimumAmount;

    /**
     * The maximum amount.
     */
    private int maximumAmount;

    /**
     * The table slot.
     */
    private int tableSlot;

    /**
     * The drop frequency.
     */
    private DropFrequency dropFrequency;

    /**
     * Constructs a new {@code ChanceItem} {@code Object}.
     *
     * @param id The item id.
     */
    public ChanceItem(int id) {
        this(id, 1, 1, 1000, 1.0);
    }

    /**
     * Constructs a new {@code ChanceItem} {@code Object}.
     *
     * @param id            The item id.
     * @param minimumAmount The minimum amount.
     */
    public ChanceItem(int id, int minimumAmount, double chanceRate) {
        this(id, minimumAmount, minimumAmount, 1000, chanceRate);
    }

    /**
     * Constructs a new {@code ChanceItem} {@code Object}.
     *
     * @param id            The item id.
     * @param minimumAmount The minimum amount.
     * @param maximumAmount The maximum amount.
     */
    public ChanceItem(int id, int minimumAmount, int maximumAmount, double chanceRate) {
        this(id, minimumAmount, maximumAmount, 1000, chanceRate);
    }

    /**
     * Constructs a new {@code ChanceItem} {@code Object}.
     *
     * @param id            The item id.
     * @param minimumAmount The minimum amount.
     * @param maximumAmount The maximum amount.
     * @param charge        The charge.
     */
    public ChanceItem(int id, int minimumAmount, int maximumAmount, int charge, double chanceRate) {
        this(id, minimumAmount, maximumAmount, charge, chanceRate, DropFrequency.UNCOMMON);
    }

    public ChanceItem(int id, int minimumAmount, int maximumAmount, DropFrequency dropFrequency) {
        this(id, minimumAmount, maximumAmount, 1000, NPCDropTables.DROP_RATES[dropFrequency.ordinal() - 1]);
    }

    public ChanceItem(boolean clue, int id, int minimumAmount, int maximumAmount, DropFrequency dropFrequency) {
        this(id, minimumAmount, maximumAmount, 1000, clue ? TreasureTrailManager.DROP_RATES[dropFrequency.ordinal() - 1] : NPCDropTables.DROP_RATES[dropFrequency.ordinal() - 1]);
    }

    /**
     * Constructs a new {@code ChanceItem} {@code Object}.
     *
     * @param id            The item id.
     * @param minimumAmount The minimum amount.
     * @param maximumAmount The maximum amount.
     * @param charge        The charge.
     * @param dropFrequency The drop frequency.
     */
    public ChanceItem(int id, int minimumAmount, int maximumAmount, int charge, double chanceRate, DropFrequency dropFrequency) {
        super(id, minimumAmount, charge);
        this.minimumAmount = minimumAmount;
        this.maximumAmount = maximumAmount;
        this.chanceRate = chanceRate;
        this.dropFrequency = dropFrequency;
    }

    /**
     * Constructs a new {@code ChanceItem}.
     *
     * @param id            The item id.
     * @param minimumAmount The minimum amount.
     * @param maximumAmount The maximum amount.
     * @param charge        The charge.
     * @param dropFrequency The drop frequency.
     */
    public ChanceItem(int id, int minimumAmount, int maximumAmount, int charge, double chanceRate, String dropFrequency) {
        super(id, minimumAmount, charge);
        this.minimumAmount = minimumAmount;
        this.maximumAmount = maximumAmount;
        this.chanceRate = chanceRate;
        for (DropFrequency dropFrequency1 : DropFrequency.values()) {
            if (dropFrequency1.name().equalsIgnoreCase(dropFrequency)) {
                this.dropFrequency = dropFrequency1;
                return;
            }
        }
        this.dropFrequency = DropFrequency.VERY_RARE;
    }

    /**
     * Gets the item instance.
     *
     * @return the item.
     */
    public Item getRandomItem() {
        if (minimumAmount == maximumAmount) {
            return new Item(getId(), minimumAmount);
        }
        return new Item(getId(), RandomUtil.random(minimumAmount, maximumAmount));
    }

    /**
     * @return the chanceRate.
     */
    public double getChanceRate() {
        return chanceRate;
    }

    /**
     * @param chanceRate the chanceRate to set.
     */
    public void setChanceRate(double chanceRate) {
        this.chanceRate = chanceRate;
    }

    /**
     * @return the minimumAmount.
     */
    public int getMinimumAmount() {
        return minimumAmount;
    }

    /**
     * @param minimumAmount the minimumAmount to set.
     */
    public void setMinimumAmount(int minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    /**
     * @return the maximumAmount.
     */
    public int getMaximumAmount() {
        return maximumAmount;
    }

    /**
     * @param maximumAmount the maximumAmount to set.
     */
    public void setMaximumAmount(int maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    /**
     * @return the dropFrequency.
     */
    public DropFrequency getDropFrequency() {
        return dropFrequency;
    }

    /**
     * @param dropFrequency the dropFrequency to set.
     */
    public void setDropFrequency(DropFrequency dropFrequency) {
        this.dropFrequency = dropFrequency;
    }

    /**
     * @return the tableSlot.
     */
    public int getTableSlot() {
        return tableSlot;
    }

    /**
     * @param tableSlot the tableSlot to set.
     */
    public void setTableSlot(int tableSlot) {
        this.tableSlot = tableSlot;
    }

}