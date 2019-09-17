package org.gielinor.game.content.skill.free.crafting;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;

/**
 * Handles cutting Dramen branches.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class DramenBranchPulse extends SkillPulse<Item> {

    /**
     * Represents the animation used in this generic pulse.
     */
    private static final Animation ANIMATION = new Animation(1248);
    /**
     * Represents the amount to fletch.
     */
    private int amount = 0;

    /**
     * Creates the Dramen branch pulse.
     *
     * @param player The player.
     * @param node   The item node.
     */
    public DramenBranchPulse(final Player player, final Item node, final int amount) {
        super(player, node);
        this.amount = amount;
    }

    @Override
    public boolean checkRequirements() {
        if (player.getSkills().getLevel(Skills.CRAFTING) < 31) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a Crafting level of at least 31 to cut this.");
            return false;
        }
        if (amount > player.getInventory().getCount(node)) {
            amount = player.getInventory().getCount(node);
        }
        return true;
    }

    @Override
    public void animate() {
        player.playAnimation(ANIMATION);
    }

    @Override
    public boolean reward() {
        if (getDelay() == 1) {
            super.setDelay(4);
            return false;
        }
        if (player.getInventory().remove(node)) {
            player.getInventory().add(new Item(772, 1));
            player.getActionSender().sendMessage("You carefully carve the branch into a staff.", 1);
        } else {
            return true;
        }
        amount--;
        return amount == 0;
    }

    @Override
    public void message(int type) {
    }
}
