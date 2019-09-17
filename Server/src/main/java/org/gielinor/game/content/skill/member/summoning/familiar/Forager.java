package org.gielinor.game.content.skill.member.summoning.familiar;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Representa a forager familiar.
 *
 * @author Vexia
 */
public abstract class Forager extends BurdenBeast {

    /**
     * The items to product if passive with no restrictions.
     */
    private final Item[] items;

    /**
     * The delay until the next random passive product.
     */
    private int passiveDelay;

    /**
     * Constructs a new {@code Forager} {@code Object}.
     *
     * @param owner         the owner.
     * @param id            the id.
     * @param ticks         the ticks.
     * @param pouchId       the pouch id.
     * @param specialCost   the special cost.
     * @param containerSize the container size.
     */
    public Forager(Player owner, int id, int ticks, int pouchId, int specialCost, final Item... items) {
        super(owner, id, ticks, pouchId, specialCost, 30);
        this.items = items;
        setRandomPassive();
    }

    @Override
    public void handleFamiliarTick() {
        super.handleFamiliarTick();
        if (items != null && passiveDelay < World.getTicks()) {
            if (RandomUtil.random(getRandom()) < 4) {
                produceItem(items[RandomUtil.random(items.length)]);
            }
            setRandomPassive();
        }
    }

    /**
     * Adds an item to the container.
     *
     * @param item the item.
     * @return <code>True</code> if so.
     */
    public boolean produceItem(final Item item) {
        if (!container.hasRoomFor(item)) {
            owner.getActionSender().sendMessage("Your familiar is too full to collect items.");
            return false;
        }
        owner.getActionSender().sendMessage(item.getCount() == 1 ? "Your familiar has produced an item." : "Your familiar has produced items.");
        return container.add(item);
    }

    /**
     * Wrapper method for {@link #produceItem()}.
     *
     * @return <code>True</code> if produced.
     */
    public boolean produceItem() {
        return produceItem(items[RandomUtil.random(items.length)]);
    }

    /**
     * Handles the passive action of a forager.
     */
    public void handlePassiveAction() {

    }

    /**
     * Gets the random mod.
     *
     * @return the random mod.
     */
    public int getRandom() {
        return 11;
    }

    /**
     * Sets a random passive delay.
     */
    public void setRandomPassive() {
        passiveDelay = World.getTicks() + RandomUtil.random(100, 440);
    }
}
