package org.gielinor.game.content.skill.free.crafting.pottery;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;

/**
 * Represents the pulse used to fire pottery.
 *
 * @author 'Vexia
 */
public final class FirePotteryPulse extends SkillPulse<Item> {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(899);

    /**
     * Represents the pottery item.
     */
    private final PotteryItem pottery;

    /**
     * Represents the amount to make.
     */
    private int amount;

    /**
     * Represents the ticks passed.
     */
    private int ticks;

    /**
     * Constructs a new {@code FirePotteryPulse} {@code Object}.
     *
     * @param player  the player.
     * @param node    the node.
     * @param pottery the pottery.
     * @param amount  the amount.
     */
    public FirePotteryPulse(Player player, Item node, final PotteryItem pottery, final int amount) {
        super(player, node);
        this.pottery = pottery;
        this.amount = amount;
    }

    @Override
    public boolean checkRequirements() {
        if (player.getSkills().getLevel(Skills.CRAFTING) < pottery.getLevel()) {
            player.getActionSender().sendMessage("You need a crafting level of " + pottery.getLevel() + " in order to do this.");
            return false;
        }
        return player.getInventory().containsItem(pottery.getUnfinished());
    }

    @Override
    public void animate() {
        if (ticks % 5 == 0) {
            player.animate(ANIMATION);
        }
    }

    @Override
    public boolean reward() {
        if (++ticks % 5 != 0) {
            return false;
        }
        if (player.getInventory().remove(pottery.getUnfinished())) {
            player.getInventory().add(pottery.getProduct());
            player.getSkills().addExperience(Skills.CRAFTING, pottery.getFireExp());
            player.getActionSender().sendMessage("You put the " + pottery.getUnfinished().getName().toLowerCase() + " in the oven.", 1);
            player.getActionSender().sendMessage("You remove a " + pottery.getProduct().getName().toLowerCase() + " from the oven.", 1);
        }
        amount--;
        return amount < 1;
    }

}
