package org.gielinor.game.node.entity.player.requirement.impl;


import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.requirement.Requirement;
import org.gielinor.game.node.item.Item;

/**
 * Represents an item {@link Requirement}.
 *
 * @author David Insley
 */
public class ItemRequirement extends Requirement {

    private final Item item;
    private final boolean remove;
    private final String error;

    public ItemRequirement(int itemId, boolean remove) {
        this(new Item(itemId, 1), remove, null);
    }

    public ItemRequirement(int itemId, int amount, boolean remove) {
        this(new Item(itemId, amount), remove, null);
    }

    public ItemRequirement(int itemId, boolean remove, String error) {
        this(new Item(itemId, 1), remove, error);
    }

    public ItemRequirement(int itemId, int amount, boolean remove, String error) {
        this(new Item(itemId, amount), remove, error);
    }

    public ItemRequirement(Item item, boolean remove) {
        this(item, remove, null);
    }

    public ItemRequirement(Item item, boolean remove, String error) {
        this.item = item;
        this.remove = remove;
        this.error = error;
    }

    @Override
    public boolean hasRequirement(Player player) {
        return player.getInventory().getCount(item) >= item.getCount();
    }

    @Override
    public void displayErrorMessage(Player player) {
        if (error != null) {
            player.getActionSender().sendMessage(error);
        }
    }

    @Override
    public void fulfill(Player player) {
        if (remove) {
            player.getInventory().remove(item);
        }
    }

}
