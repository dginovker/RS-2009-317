package org.gielinor.game.content.skill.free.crafting.pottery;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the skill pulse of the pottery unfired items.
 *
 * @author 'Vexia
 */
public final class PotteryPulse extends SkillPulse<Item> {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = Animation.create(896);

    /**
     * Represnets the soft clay item.
     */
    private static final Item SOFT_CLAY = new Item(1761);

    /**
     * Represents the pottery item to make.
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
     * Constructs a new {@code PotteryPulse} {@code Object}.
     *
     * @param player  the player.
     * @param node    the node.
     * @param amount  the amount.
     * @param pottery the pottery.
     */
    public PotteryPulse(Player player, Item node, int amount, final PotteryItem pottery) {
        super(player, node);
        this.pottery = pottery;
        this.amount = amount;
    }

    @Override
    public boolean checkRequirements() {
        if (!player.getInventory().contains(1761, 1)) {
            player.getActionSender().sendMessage("You need soft clay in order to do this.");
            return false;
        }
        if (player.getSkills().getLevel(Skills.CRAFTING) < pottery.getLevel()) {
            player.getActionSender().sendMessage("You need a crafting level of " + pottery.getLevel() + " to make this.");
            return false;
        }
        return true;
    }

    @Override
    public void animate() {
        if (ticks % 5 == 0) {
            player.animate(ANIMATION);
        }
    }

    @Override
    public boolean reward() {
        if (++ticks % 3 != 0) {
            return false;
        }
        if (player.getInventory().remove(SOFT_CLAY)) {
            player.getInventory().add(pottery.getUnfinished());
            player.getSkills().addExperience(Skills.CRAFTING, pottery.getExp());
            player.getActionSender().sendMessage("You make the soft clay into " + (TextUtils.isPlusN(pottery.getUnfinished().getName()) ? "an" : "a") + " " + pottery.getUnfinished().getName().toLowerCase() + ".", 1);
        }
        amount--;
        return amount < 1;
    }

}
