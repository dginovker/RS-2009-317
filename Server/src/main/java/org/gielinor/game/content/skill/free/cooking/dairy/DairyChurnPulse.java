package org.gielinor.game.content.skill.free.cooking.dairy;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the skill pulse used to make a dairy product.
 *
 * @author 'Vexia
 */
public final class DairyChurnPulse extends SkillPulse<Item> {

    /**
     * Represents the animation.
     */
    private static final Animation ANIMATION = new Animation(2793);

    /**
     * Represents the bucket of milk item.
     */
    private static final Item BUCKET_OF_MILK = new Item(1927, 1);

    /**
     * Represents the bucket item.
     */
    private static final Item BUCKET = new Item(1925, 1);

    /**
     * Represents the dairy product we're making.
     */
    private final DairyProduct dairy;

    /**
     * Represent the amount to make.
     */
    private int amount;

    /**
     * Constructs a new {@code DairyChurnPulse} {@code Object}.
     *
     * @param player  the player.
     * @param item    the item.
     * @param product the product.
     * @param amount  the amount.
     */
    public DairyChurnPulse(Player player, Item item, DairyProduct product, int amount) {
        super(player, item);
        super.setDelay(8);
        this.amount = amount;
        this.dairy = product;
    }


    @Override
    public boolean checkRequirements() {
        player.getInterfaceState().closeChatbox();
        if (!player.getInventory().contains(1927, 1)) {
            player.getActionSender().sendMessage("You need a bucket of milk.");
            return false;
        }
        if (player.getInventory().freeSlots() < 2) {
            player.getActionSender().sendMessage("You don't have enough inventory space.");
            return false;
        }
        if (player.getSkills().getLevel(Skills.COOKING) < dairy.getLevel()) {
            player.getActionSender().sendMessage("You need a cooking level of " + dairy.getLevel() + " to cook this.");
            return false;
        }
        if (amount > player.getInventory().getCount(node)) {
            amount = player.getInventory().getCount(node);
            if (amount == 0) {
                return false;
            }
        }
        if (amount < 1) {
            return false;
        }
        animate();
        return true;
    }

    @Override
    public void animate() {
        player.animate(ANIMATION);
    }

    @Override
    public boolean reward() {
        amount--;
        if (player.getInventory().containsItem(BUCKET_OF_MILK)) {
            if (player.getInventory().remove(BUCKET_OF_MILK)) {
                if (player.getInventory().freeSlots() > 1) {
                    player.getInventory().add(dairy.getProduct());
                    player.getInventory().add(BUCKET);
                }
                player.getActionSender().sendMessage("You make " + (TextUtils.isPlusN(dairy.getProduct().getName().toLowerCase()) ? "an" : "a") + " " + dairy.getProduct().getName().toLowerCase() + ".", 1);
                player.getSkills().addExperience(Skills.COOKING, dairy.getExperience());
            }
        }
        if (player.getInventory().freeSlots() < 2) {
            player.getActionSender().sendMessage("You don't have enough inventory space.");
            return true;
        }
        return amount < 1 || player.getInventory().freeSlots() < 2;
    }


}
