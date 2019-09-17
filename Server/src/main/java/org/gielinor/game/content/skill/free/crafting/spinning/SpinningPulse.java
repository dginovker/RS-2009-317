package org.gielinor.game.content.skill.free.crafting.spinning;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;

/**
 * Represents the pulse used to spin an item.
 *
 * @author 'Vexia
 */
public final class SpinningPulse extends SkillPulse<Item> {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(896);
    /**
     * Represents the {@link org.gielinor.game.content.skill.free.crafting.spinning.Spinnable}.
     */
    private Spinnable spinnable;
    /**
     * Represents the amount to spin.
     */
    private int inputAmount;
    /**
     * Represents the ticks passed.
     */
    private int ticks;

    /**
     * Constructs a new <code>SpinningPulse</code>.
     *
     * @param player      The player.
     * @param node        The spinning node.
     * @param inputAmount the input amount.
     * @param spinnable   The {@link org.gielinor.game.content.skill.free.crafting.spinning.Spinnable}.
     */
    public SpinningPulse(Player player, Item node, int inputAmount, Spinnable spinnable) {
        super(player, node);
        this.spinnable = spinnable;
        this.inputAmount = inputAmount;
    }

    @Override
    public boolean checkRequirements() {
        player.getInterfaceState().close();
        if (player.getSkills().getLevel(Skills.CRAFTING) < spinnable.getLevel()) {
            player.getActionSender().sendMessage("You need a crafting level of " + spinnable.getLevel() + " to make this.", 1);
            return false;
        }
        if (!player.getInventory().contains(spinnable.getItem())) {
            String itemName = spinnable.name().contains("TREE_ROOT") ? "tree roots" : spinnable.getItem().getName().toLowerCase();
            itemName = itemName.equalsIgnoreCase("hair") ? "yak hair" : itemName;
            player.getActionSender().sendMessage("You need " + itemName + " to make this.", 1);
            return true;
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
        if (++ticks % 2 != 0) {
            return false;
        }
        if (player.getInventory().remove(node)) {
            player.getInventory().add(spinnable.getProduct());
            player.getSkills().addExperience(Skills.CRAFTING, spinnable.getExperience());
        }
        inputAmount--;
        return inputAmount < 1;
    }

    @Override
    public void message(int type) {
    }

}
