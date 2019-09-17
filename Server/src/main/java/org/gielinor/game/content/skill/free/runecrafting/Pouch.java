package org.gielinor.game.content.skill.free.runecrafting;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.model.container.Container;

/**
 * Represents a rune pouch.
 *
 * @author 'Vexia
 *         <p>
 *         TODO degrading
 */
public enum Pouch {

    SMALL(new Item(5509), 1, 3, 3, 0),
    MEDIUM(new Item(5510), 25, 6, 9, 45),
    LARGE(new Item(5512), 50, 9, 18, 29),
    GIANT(new Item(5514), 75, 12, 30, 10);

    /**
     * Constructs a new {@code Pouch} {@code Object}.
     *
     * @param pouch    the pouch.
     * @param level    the level.
     * @param capacity the capacity.
     */
    Pouch(Item pouch, int level, int capacity, int cumulativeCapacity, int uses) {
        this.pouch = pouch;
        this.level = level;
        this.capacity = capacity;
        this.cumulativeCapacity = cumulativeCapacity;
        this.uses = uses;
    }

    /**
     * Represents the pouch item.
     */
    private final Item pouch;

    /**
     * Represents the level required to use this pouch.
     */
    private final int level;

    /**
     * Represents the capacity of the pouch.
     */
    private final int capacity;

    /**
     * Represents the cumulativeCapacity.
     */
    private final int cumulativeCapacity;

    /**
     * Represents the uses before decay.
     */
    private final int uses;

    /**
     * Gets the pouch.
     *
     * @return The pouch.
     */
    public Item getPouch() {
        return pouch;
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
     * Gets the capacity.
     *
     * @return The capacity.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets the cumulativeCapacity.
     *
     * @return The cumulativeCapacity.
     */
    public int getCumulativeCapacity() {
        return cumulativeCapacity;
    }

    /**
     * Gets the uses.
     *
     * @return The uses.
     */
    public int getUses() {
        return uses;
    }

    /**
     * Checks if the pouch is decayable.
     *
     * @return <code>True</code> if so.
     */
    public boolean isDecayable() {
        return this != SMALL;
    }

    /**
     * Method used to get the <code>Pouch</code>.
     *
     * @param pouch the pouch.
     * @return the <code>Pouch</code> or <code>Null</code>.
     */
    public static Pouch forItem(final Item pouch) {
        for (Pouch p : Pouch.values()) {
            if (p.getPouch().getId() == pouch.getId()) {
                return p;
            }
        }
        return null;
    }

    /**
     * Method used to handle the action.
     *
     * @param player The player.
     * @param option The option.
     */
    public final void action(final Player player, final String option) {
        if (player.getSkills().getLevel(Skills.RUNECRAFTING) < getLevel()) {
            player.getActionSender().sendMessage("You need a Runecrafting level of at least " + getLevel() + " in order to use this pouch.");
            return;
        }
        // The container to use.
        Container container = null;
        switch (this) {
            case SMALL:
                container = player.getSavedData().getGlobalData().getSmallPouch();
                break;
            case MEDIUM:
                container = player.getSavedData().getGlobalData().getMediumPouch();
                break;
            case LARGE:
                container = player.getSavedData().getGlobalData().getLargePouch();
                break;
            case GIANT:
                container = player.getSavedData().getGlobalData().getGiantPouch();
                break;
        }
        if (container == null) {
            return;
        }
        switch (option) {
            case "fill":
                fill(player, container);
                break;
            case "empty":
                empty(player, container);
                break;
            case "check":
                check(player, container);
                break;
        }
    }

    /**
     * Method used to fill a pouch.
     *
     * @param player    The player.
     * @param container The container to use.
     */
    public final void fill(final Player player, Container container) {
        int currentEssence = container.itemCount();
        if (currentEssence >= getCapacity()) {
            player.getActionSender().sendMessage("Your pouch is full.");
            return;
        }
        if (!player.getInventory().contains(7936)) {
            player.getActionSender().sendMessage("You do not have any pure essence.");
            return;
        }
        int toAdd = player.getInventory().getCount(new Item(7936)) > getCapacity() ? getCapacity() : (player.getInventory().getCount(new Item(7936)) - currentEssence);
        if (player.getInventory().remove(new Item(7936, toAdd))) {
            container.add(new Item(7936, toAdd));
        }
    }

    /**
     * Method used to empty a pouch.
     *
     * @param player    The player.
     * @param container The container to use.
     */
    public final void empty(final Player player, Container container) {
        int currentEssence = container.itemCount();
        if (currentEssence == 0) {
            player.getActionSender().sendMessage("Your pouch has no essence left in it.");
            return;
        }
        int toRemove = player.getInventory().freeSlots() < currentEssence ? player.getInventory().freeSlots() : currentEssence;
        if ((container.itemCount() - toRemove) < 0) {
            return;
        }
        if (player.getInventory().add(new Item(7936, toRemove))) {
            container.remove(new Item(7936, toRemove));
        }
    }

    /**
     * Method used to check a pouch.
     *
     * @param player    The player.
     * @param container The container to use.
     */
    public final void check(final Player player, Container container) {
        if (container.itemCount() > 0) {
            player.getActionSender().sendMessage("Your pouch currently contains " + container.itemCount() + " essence.");
            return;
        }
        player.getActionSender().sendMessage("Your pouch has no essence left in it.");
    }
}

