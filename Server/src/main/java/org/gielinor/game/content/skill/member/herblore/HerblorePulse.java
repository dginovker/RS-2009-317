package org.gielinor.game.content.skill.member.herblore;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the skill pulse used to handle the creating of potions.
 *
 * @author 'Vexia
 */
public final class HerblorePulse extends SkillPulse<Item> {

    /**
     * Represents the vial of water item.
     */
    public static final Item VIAL_OF_WATER = new Item(227);

    /**
     * Represents the coonut milk item.
     */
    public static final Item COCONUT_MILK = new Item(5935);

    /**
     * Represents the animation to use when making a potion.
     */
    private static final Animation ANIMATION = new Animation(363);

    /**
     * Represents the generic potion.
     */
    private final GenericPotion potion;

    /**
     * Represents the amount to make.
     */
    private int amount;

    /**
     * Represents the initial amount to make.
     */
    private int initialAmount;

    /**
     * Represents the cycles.
     */
    private int cycles;

    /**
     * Constructs a new {@code HerblorePulse} {@code Object}.
     *
     * @param player the player.
     * @param node   the node.
     */
    public HerblorePulse(final Player player, final Item node, final int amount, final GenericPotion potion) {
        super(player, node);
        this.amount = amount;
        this.initialAmount = amount;
        this.potion = potion;
    }

    @Override
    public boolean checkRequirements() {
        if (player.getSkills().getLevel(Skills.HERBLORE) < potion.getLevel()) {
            player.getActionSender().sendMessage("You need a Herblore level of at least " + potion.getLevel() + " in order to do this.");
            return false;
        }
        return true;
    }

    @Override
    public void animate() {
    }

    @Override
    public boolean reward() {
        if (potion.getBase().getId() == VIAL_OF_WATER.getId()) {
            if (initialAmount == 1 && getDelay() == 1) {
                player.animate(ANIMATION);
                setDelay(3);
                return false;
            }
            handleUnfinished();
        } else {
            if (initialAmount == 1 && getDelay() == 1) {
                player.animate(ANIMATION);
                setDelay(3);
                return false;
            }
            if (getDelay() == 1) {
                setDelay(3);
                player.animate(ANIMATION);
                return false;
            }
            handleFinished();
        }
        amount--;
        return !player.getInventory().contains(potion.getBase()) || !player.getInventory().contains(potion.getIngredient()) ||
            amount == 0;
    }

    /**
     * Method used to handle the potion making of an unf-potion.
     */
    public void handleUnfinished() {
        if (cycles == 0) {
            player.animate(ANIMATION);
        }
        if ((player.getInventory().containsItem(potion.getBase()) &&
            player.getInventory().containsItem(potion.getIngredient())) &&
            player.getInventory().remove(potion.getBase(), potion.getIngredient())) {
            player.getInventory().add(potion.getProduct());
            player.getActionSender().sendMessage("You put the" + TextUtils.formatDisplayName(potion.getIngredient().getName().replace("Clean", "")).toLowerCase() + " leaf into the vial of water.");

            if (cycles++ == 3) {
                player.animate(ANIMATION);
                cycles = 0;
            }
        }
    }

    /**
     * Method used to handle the finished potion making.
     */
    public void handleFinished() {
        if ((player.getInventory().containsItem(potion.getBase())
            && player.getInventory().containsItem(potion.getIngredient())) &&
            player.getInventory().remove(potion.getBase(), potion.getIngredient())) {
            player.getInventory().add(potion.getProduct());
            player.getSkills().addExperience(Skills.HERBLORE, potion.getExperience());
            String s = (potion.getIngredient().getCount() > 1 ? "s" : "");
            player.getActionSender().sendMessage("You mix the " + potion.getIngredient().getName().toLowerCase() + s + " into your potion.");
            player.animate(ANIMATION);
        }
    }
}
