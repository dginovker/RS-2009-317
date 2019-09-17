package org.gielinor.game.content.skill.member.fletching.items.bolts;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the bolt pulse class to make bolts.
 *
 * @author 'Vexia
 */
public final class BoltPulse extends SkillPulse<Item> {

    /**
     * Represents the feather item.
     */
    private final Item feather = new Item(314);

    /**
     * Represents the bolt.
     */
    private final Bolt bolt;

    /**
     * Represents the sets to do.
     */
    private int sets;

    /**
     * Represents if we're using sets.
     */
    private boolean useSets = false;

    /**
     * Constructs a new {@code BoltPulse.java} {@code Object}.
     *
     * @param player the player.
     * @param node   the node.
     */
    public BoltPulse(Player player, Item node, final Bolt bolt, final int sets) {
        super(player, node);
        this.bolt = bolt;
        this.sets = sets;
    }

    @Override
    public boolean checkRequirements() {
        if (player.getSkills().getLevel(Skills.FLETCHING) < bolt.getLevel()) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a fletching level of " + bolt.getLevel() + " in order to do this.");
            return false;
        }
        if (!player.getInventory().containsItem(feather)) {
            return false;
        }
        if (!player.getInventory().containsItem(bolt.getItem())) {
            return false;
        }
        if (player.getInventory().contains(bolt.getItem().getId(), 10) && player.getInventory().contains(feather.getId(), 10)) {
            useSets = true;
        } else {
            useSets = false;
        }
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
        final Item boltt = bolt.getItem();
        if (useSets) {
            feather.setCount(10);
            boltt.setCount(10);
            player.getActionSender().sendMessage("You fletch 10 bolts.");
        } else {
            player.getActionSender().sendMessage("You attach a feather to a bolt.");
        }
        if (player.getInventory().remove(feather, boltt)) {
            Item product = bolt.getProduct();
            if (useSets) {
                product.setCount(10);
            } else {
                product.setCount(1);
            }
            player.getSkills().addExperience(Skills.FLETCHING, useSets ? bolt.getExperience() * 10 : bolt.getExperience());
            player.getInventory().add(product);
        }
        feather.setCount(1);
        if (!player.getInventory().containsItem(feather)) {
            return true;
        }
        if (!player.getInventory().containsItem(bolt.getItem())) {
            return true;
        }
        sets--;
        return sets == 0;
    }

    @Override
    public void message(int type) {
    }

}
