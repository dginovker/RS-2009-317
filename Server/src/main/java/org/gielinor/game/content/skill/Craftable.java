package org.gielinor.game.content.skill;

import org.gielinor.game.node.item.Item;

/**
 * Subclass for controlling craftable data while skilling.
 *
 * @author <a href="http://www.rune-server.org/members/mike/">Mike</a>
 */
public class Craftable {

    /**
     * The item received upon crafting
     */
    private final Item item;

    /**
     * The item required to craft this item
     */
    private Item itemRequired;

    /**
     * The level requirement to craft the item
     */
    private final int requirement;

    /**
     * The experience given upon crafting the item
     */
    private final double experience;

    /**
     * Craftable constructor
     *
     * @param item        The item received upon crafting
     * @param requirement The level requirement to craft the item
     * @param experience  The experience given upon crafting the item
     */
    public Craftable(Item item, int requirement, double experience) {
        this.item = item;
        this.requirement = requirement;
        this.experience = experience;
    }

    /**
     * Craftable constructor
     *
     * @param item         The item received upon crafting
     * @param itemRequired The item required to craft this item
     * @param requirement  The level requirement to craft the item
     * @param experience   The experience given upon crafting the item
     */
    public Craftable(Item item, Item itemRequired, int requirement, double experience) {
        this.item = item;
        this.itemRequired = itemRequired;
        this.requirement = requirement;
        this.experience = experience;
    }

    /**
     * Gets the item received upon crafting
     *
     * @return the crafted item
     */
    public Item getItem() {
        return item;
    }

    /**
     * Gets the item required to craft this item
     *
     * @return the item required
     */
    public Item getItemRequired() {
        return itemRequired;
    }

    /**
     * Gets the level requirement to craft the item
     *
     * @return the level required
     */
    public int getLevel() {
        return requirement;
    }

    /**
     * Gets the experience given upon crafting the item
     *
     * @return the experience given
     */
    public double getExperience() {
        return experience;
    }
}
