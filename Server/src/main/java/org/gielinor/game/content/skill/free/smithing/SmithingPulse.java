package org.gielinor.game.content.skill.free.smithing;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the pulse used to smith a bar.
 *
 * @author 'Vexia
 */
public class SmithingPulse extends SkillPulse<Item> {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(898);

    /**
     * Represents the {@link org.gielinor.game.content.skill.free.smithing.Smithable}.
     */
    private final Smithable smithable;

    /**
     * Represents the amount to make.
     */
    private int amount;

    /**
     * Constructs a new {@code SmithingPulse} {@code Object}.
     *
     * @param player the player.
     * @param item   the item.
     */
    public SmithingPulse(Player player, Item item, Smithable smithable, int amount) {
        super(player, item);
        this.smithable = smithable;
        this.amount = amount;
    }

    @Override
    public boolean checkRequirements() {
        if (!player.getInventory().contains(new Item(smithable.getBar().getId(), smithable.getBar().getCount() * amount))) {
            amount = player.getInventory().getCount(new Item(smithable.getBar().getId()));
        }
        player.getInterfaceState().close();
        if (player.getSkills().getLevel(Skills.SMITHING) < smithable.getCraftable().getLevel()) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a Smithing level of " + smithable.getCraftable().getLevel() + " to make a " + smithable.getCraftable().getItem().getName() + ".");
            return false;
        }
        if (!player.getInventory().contains(smithable.getBar())) {
            player.getDialogueInterpreter().sendPlaneMessage("You don't have enough " + smithable.getBar().getName().toLowerCase() + "s to make a " + smithable.getCraftable().getItem().getName().toLowerCase() + ".");
            return false;
        }
        if (player.getDonorManager().getDonorStatus().getToolsRequired()) {
            if (!player.getInventory().contains(Item.HAMMER, 1)) {
                player.getDialogueInterpreter().sendPlaneMessage("You need a hammer to work the metal with.");
                return false;
            }
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
            setDelay(4);
            return false;
        }
        if (!player.getInventory().remove(smithable.getBar())) {
            player.getDialogueInterpreter().sendPlaneMessage("You have run out of bars.");
            return true;
        }
        player.getInventory().add(smithable.getCraftable().getItem());
        player.getSkills().addExperience(Skills.SMITHING, smithable.getBaseExperience() * smithable.getBar().getCount());
        String message = TextUtils.isPlusN(smithable.getCraftable().getItem().getName().toLowerCase()) ? "an" : "a";
        player.getActionSender().sendMessage("You hammer the " + smithable.getBar().getName().toLowerCase() + " and make " + message + " " + smithable.getCraftable().getItem().getName().toLowerCase() + ".", 1);
        amount--;
        return amount < 1;
    }

    @Override
    public void message(int type) {

    }

}
