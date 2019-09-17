package org.gielinor.game.content.skill.free.cooking;

import org.gielinor.game.content.global.consumable.Food;
import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.update.flag.context.Animation;

/**
 * Represents the pulse used to cook a consumable.
 *
 * @author 'Vexia
 */
public final class CookingPulse extends SkillPulse<GameObject> {

    /**
     * Represents the animation used when cooking over a range.
     */
    private static final Animation RANGE_ANIMATION = new Animation(883);

    /**
     * Represents the animation used when cooking over a fire.
     */
    private static final Animation FIRE_ANIMATION = new Animation(897);

    /**
     * Represents the food we're cooking.
     */
    private final Food food;

    /**
     * Represents the amount to make.
     */
    private int amount;

    /**
     * Constructs a new {@code CookingPulse} {@code Object}.
     *
     * @param player the player.
     * @param node   the object node (fire/range)
     * @param food   the food to cook.
     */
    public CookingPulse(Player player, GameObject node, final Food food, final int amount) {
        super(player, node);
        this.food = food;
        this.amount = amount;
    }

    @Override
    public boolean checkRequirements() {
        if (!node.isActive()) {
            return true;
        }
        if (player.getSkills().getLevel(Skills.COOKING) < food.getCookingProperties().getLevel()) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a cooking level of " + food.getCookingProperties().getLevel() + " to cook this.");
            return false;
        }
        int inventoryAmount = player.getInventory().getCount(food.getRaw());
        if (amount > inventoryAmount) {
            amount = inventoryAmount;
        }
        if (amount < 1) {
            return false;
        }
        return true;
    }

    @Override
    public void animate() {
        player.animate(getAnimation(node));
    }

    @Override
    public boolean reward() {
        if (getDelay() == 1) {
            setDelay(node.getName().toLowerCase().equals("range") ? 4 : 3);
            return false;
        }
        if (food.getCookingProperties().cookAll(player, node)) {
            amount = 0;
        }
        if (food.getCookingProperties().cook(food, player, node)) {
            amount--;
        } else {
            return true;
        }
        return amount < 1;
    }

    /**
     * Gets the animation to use based on the interacting object.
     *
     * @param object the object.
     * @return the animation.
     */
    private Animation getAnimation(final GameObject object) {
        return !object.getName().toLowerCase().equals("fire") ? RANGE_ANIMATION : FIRE_ANIMATION;
    }
}
