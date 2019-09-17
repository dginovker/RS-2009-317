package org.gielinor.game.content.skill.member.fletching;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the skill pulse of generic fletching.
 * @author 'Vexia
 *
 */
public final class FletchingPulse extends SkillPulse<Item> {

    /**
     * Represents the animation used in this generic pulse.
     */
    private static final Animation ANIMATION = new Animation(1248);

    /**
     * Represents the fletch fletch to fletch.
     */
    private FletchItem fletch;

    /**
     * Represents the amount to fletch.
     */
    private int amount = 0;

    /**
     * Constructs a new {@code FletchingPulse.java} {@code Object}.
     * @param player
     * @param node
     */
    public FletchingPulse(final Player player, final Item node, final int amount, final FletchItem fletch) {
        super(player, node);
        this.amount = amount;
        this.fletch = fletch;
    }

    @Override
    public boolean checkRequirements() {
        if (player.getSkills().getLevel(Skills.FLETCHING) < fletch.getLevel()) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a Fletching skill of " + fletch.getLevel() + " or above to make " + (TextUtils.isPlusN(fletch.getProduct().getName().replace("(u)", "").trim()) ? "an" : "a") + " " + fletch.getProduct().getName().replace("(u)", "").trim());
            return false;
        }
        if (amount > player.getInventory().getCount(fletch.getType().getLog())) {
            amount = player.getInventory().getCount(fletch.getType().getLog());
        }
        return true;
    }

    @Override
    public void animate() {
        player.animate(ANIMATION);
    }

    @Override
    public boolean reward() {
        if (getDelay() == 1) {
            super.setDelay(2);
            return false;
        }
        if (player.getInventory().remove(fletch.getType().getLog())) {
            player.getInventory().add(fletch.getProduct());
            player.getSkills().addExperience(Skills.FLETCHING, fletch.getExperience());
            String message = getMessage();
            player.getActionSender().sendMessage(message, 1);
        } else {
            return true;
        }
        amount--;
        return amount == 0;
    }

    @Override
    public void message(int type) {
    }

    /**
     * Method used to get the message of the fletch.
     * @return the message.
     */
    public String getMessage() {
        switch (fletch) {
            case ARROW_SHAFT:
                return "You carefully cut the wood into 15 arrow shafts.";
            default:
                return "You carefully cut the wood into " + (TextUtils.isPlusN(fletch.getProduct().getName()) ? "an" : "a") + " " + fletch.getProduct().getName().replace("(u)", "").trim() + ".";
        }
    }
}
