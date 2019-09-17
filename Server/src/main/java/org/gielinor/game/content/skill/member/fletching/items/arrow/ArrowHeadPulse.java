package org.gielinor.game.content.skill.member.fletching.items.arrow;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the arrow head pulse to complete the headless arrow.
 * @author 'Vexia
 *
 */
public class ArrowHeadPulse extends SkillPulse<Item> {

    /**
     * Represents the headless arrow item.
     */
    private static final Item HEADLESS_ARROW = new Item(53);

    /**
     * Represents the arrow head.
     */
    private final ArrowHead arrow;

    /**
     * Represents the sets to do.
     */
    private int sets;

    /**
     * Represents if we should use sets, meaning we have 15 & 15 arrow shafts and feathers.
     */
    private boolean useSets = false;

    /**
     * Constructs a new {@code ArrowHeadPulse.java} {@code Object}.
     * @param player the player.
     * @param node the node.
     * @param arrow the arrow.
     * @param sets the sets.
     */
    public ArrowHeadPulse(final Player player, final Item node, final ArrowHead arrow, final int sets) {
        super(player, node);
        this.arrow = arrow;
        this.sets = sets;
    }

    @Override
    public boolean checkRequirements() {
        if (player.getSkills().getLevel(Skills.FLETCHING) < arrow.getLevel()) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a fletching level of " + arrow.getLevel() + " to do this.");
            return false;
        }
        if (!player.getInventory().containsItem(HEADLESS_ARROW)) {
            player.getDialogueInterpreter().sendPlaneMessage("You need headless arrows in order to do this.");
            return false;
        }
        if (!player.getInventory().contains(arrow.getTips().getId(), 1)) {
            player.getDialogueInterpreter().sendPlaneMessage("You need arrow heads in order to do this.");
            return false;
        }
        useSets = player.getInventory().contains(HEADLESS_ARROW.getId(), 15) && player.getInventory().contains(arrow.getTips().getId(), 15);
        return true;
    }

    @Override
    public void animate() {
    }

    @Override
    public boolean reward() {
        if (getDelay() == 1) {
            super.setDelay(2);
        }
        Item tip = arrow.getTips();
        player.getActionSender().sendMessage("You attach " + arrow.getTips().getName().toLowerCase() + " to some of your arrows.");
        if (useSets) {
            HEADLESS_ARROW.setCount(15);
            tip.setCount(15);
            player.getActionSender().sendMessage("You attach arrow heads to 15 arrow shafts.");
        } else {
            HEADLESS_ARROW.setCount(1);
            tip.setCount(1);
            player.getActionSender().sendMessage("You attach an arrow head to an arrow shaft.");
        }
        if (player.getInventory().remove(HEADLESS_ARROW, tip)) {
            player.getSkills().addExperience(Skills.FLETCHING, useSets ? arrow.getExperience() * 15 : arrow.getExperience());
            Item product = arrow.getProduct();
            if (useSets) {
                product.setCount(15);
            } else {
                product.setCount(1);
            }
            player.getInventory().add(product);
        }
        HEADLESS_ARROW.setCount(1);
        tip.setCount(1);
        if (!player.getInventory().containsItem(HEADLESS_ARROW)) {
            return true;
        }
        if (!player.getInventory().containsItem(tip)) {
            return true;
        }
        useSets = false;
        sets--;
        return sets == 0;
    }

    @Override
    public void message(int type) {

    }

}
