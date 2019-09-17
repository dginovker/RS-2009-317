package org.gielinor.game.content.skill.member.fletching.items.bow;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the skill pulse of stringing.
 *
 * @author 'Vexia
 */
public class StringPulse extends SkillPulse<Item> {

    /**
     * Represents the bow string item.
     */
    private final Item BOW_STRING = new Item(1777);

    /**
     * Represents the string bow.
     */
    private final StringBow bow;

    /**
     * The amount.
     */
    private int amount;

    /**
     * Constructs a new {@code StringbowPlugin.java} {@code Object}.
     *
     * @param player the player.
     * @param node   the node.
     */
    public StringPulse(Player player, Item node, final StringBow bow, int amount) {
        super(player, node);
        this.bow = bow;
        this.amount = amount;
    }

    @Override
    public boolean checkRequirements() {
        if (player.getSkills().getLevel(Skills.FLETCHING) < bow.getLevel()) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a fletching level of " + bow.getLevel() + " to string this bow.");
            return false;
        }
        if (!player.getInventory().containsItem(BOW_STRING)) {
            player.getDialogueInterpreter().sendPlaneMessage("You seem to have run out of bow strings.");
            return false;
        }
        return true;
    }

    @Override
    public void animate() {
        player.animate(bow.getAnimation());
    }

    @Override
    public boolean reward() {
        if (getDelay() == 1) {
            super.setDelay(2);
            return false;
        }
        if (player.getInventory().remove(bow.getItem(), BOW_STRING)) {
            player.getInventory().add(bow.getProduct());
            player.getSkills().addExperience(Skills.FLETCHING, bow.getExperience());
            player.getActionSender().sendMessage("You add a string to the bow.");
        }
        if (!player.getInventory().containsItem(BOW_STRING) || !player.getInventory().containsItem(bow.getItem())) {
            return true;
        }
        amount--;
        return amount == 0;
    }

    @Override
    public void message(int type) {
        switch (type) {
            case 0:
                break;
            case 1:
                break;
        }
    }

}
