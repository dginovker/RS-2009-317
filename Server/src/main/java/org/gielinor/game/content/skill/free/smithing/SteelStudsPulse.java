package org.gielinor.game.content.skill.free.smithing;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;

/**
 * Represents the {@link org.gielinor.game.content.skill.SkillPulse} for smithing steel studs.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class SteelStudsPulse extends SkillPulse<Item> {

    /**
     * The smithing {@link org.gielinor.game.world.update.flag.context.Animation}.
     */
    private static final Animation SMITH_ANIMATION = new Animation(898);
    /**
     * The steel bar item.
     */
    private static final Item STEEL_BAR = new Item(2353);
    /**
     * The steel studs item.
     */
    public static final Item STEEL_STUDS = new Item(2370);

    /**
     * Represents the {@link org.gielinor.game.interaction.NodeUsageEvent}.
     */
    private final NodeUsageEvent nodeUsageEvent;
    /**
     * Represents the amount to make.
     */
    private int amount;
    /**
     * Represents the ticks passed.
     */
    private int ticks;

    /**
     * Constructs a new {@code SteelStudsPulse} {@Code Object}
     *
     * @param player         The player.
     * @param nodeUsageEvent The {@link org.gielinor.game.interaction.NodeUsageEvent}.
     * @param amount         The amount to offer.
     */
    public SteelStudsPulse(Player player, NodeUsageEvent nodeUsageEvent, int amount) {
        super(player, nodeUsageEvent.getUsedItem());
        this.nodeUsageEvent = nodeUsageEvent;
        this.amount = amount;
        this.resetAnimation = false;
    }

    @Override
    public boolean checkRequirements() {
        if (player.getSkills().getLevel(Skills.SMITHING) < 36) {
            player.getActionSender().sendMessage("You need a level of at least 36 Smithing to do that.");
            return false;
        }
        if (!player.getInventory().contains(Item.HAMMER)) {
            player.getActionSender().sendMessage("You need a hammer to do that.");
            return false;
        }
        return player.getInventory().contains(STEEL_BAR);
    }

    @Override
    public void animate() {
        if (ticks % 2 == 0 || ticks < 1) {
            player.animate(SMITH_ANIMATION);
            player.faceLocation(nodeUsageEvent.getUsedWith().getLocation());
        }
    }

    @Override
    public boolean reward() {
        if (++ticks % 2 != 0) {
            return false;
        }
        player.getInventory().remove(STEEL_BAR);
        player.getInventory().add(STEEL_STUDS);
        player.getSkills().addExperience(Skills.SMITHING, 37.5);
        player.getActionSender().sendMessage("You smith the steel bar into studs.", 1);
        if (!player.getInventory().contains(STEEL_BAR)) {
            amount = 0;
            return true;
        }
        amount--;
        return amount < 1;
    }
}
