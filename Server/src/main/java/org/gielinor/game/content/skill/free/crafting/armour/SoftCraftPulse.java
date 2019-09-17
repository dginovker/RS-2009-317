package org.gielinor.game.content.skill.free.crafting.armour;

import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.crafting.armour.LeatherCrafting.SoftLeather;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents a pulse used to craft soft leather.
 *
 * @author 'Vexia
 */
public final class SoftCraftPulse extends SkillPulse<Item> {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = Animation.create(1249);

    /**
     * Represents the leather to use.
     */
    private SoftLeather soft;

    /**
     * Represents the amount to make.
     */
    private int amount;

    /**
     * Represents the ticks passed.
     */
    private int ticks;

    /**
     * Constructs a new {@code SoftCraftPulse} {@code Object}.
     *
     * @param player  the player.
     * @param node    the node.
     * @param leather the soft.
     * @param amount  the amount.
     */
    public SoftCraftPulse(Player player, Item node, SoftLeather leather, int amount) {
        super(player, node);
        this.soft = leather;
        this.amount = amount;
    }

    @Override
    public boolean checkRequirements() {
        if (player.getSkills().getLevel(Skills.CRAFTING) < soft.getLevel()) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a crafting level of " + soft.getLevel() + " to make " + (TextUtils.isPlusN(soft.getProduct().getName()) ? "an" : "a" + " " + soft.getProduct().getName()).toLowerCase() + ".");
            return false;
        }
        if (!player.getInventory().contains(LeatherCrafting.NEEDLE, 1)) {
            return false;
        }
        if (!player.getInventory().contains(LeatherCrafting.LEATHER, 1)) {
            return false;
        }
        if (!player.getInventory().containsItem(LeatherCrafting.THREAD) && !Perk.NICE_THREADS.enabled(player)) {
            player.getDialogueInterpreter().sendPlaneMessage("You need thread to make this.");
            amount = 0;
            return false;
        }
        player.getInterfaceState().close();
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
        if (++ticks % 5 != 0) {
            return false;
        }
        if (player.getInventory().remove(new Item(LeatherCrafting.LEATHER))) {
            if (soft == SoftLeather.GLOVES || soft == SoftLeather.BOOTS || soft == SoftLeather.VAMBRACES) {
                player.getActionSender().sendMessage("You make a pair of " + soft.getProduct().getName().toLowerCase() + ".", 1);
            } else {
                player.getActionSender().sendMessage("You make " + (TextUtils.isPlusN(soft.getProduct().getName()) ? "an" : "a") + " " + soft.getProduct().getName().toLowerCase() + ".", 1);
            }
            if (soft == SoftLeather.ARMOUR) {
                AchievementDiary.decrease(player, AchievementTask.MAKING_FONZIE_PROUD, 1);
            }
            player.getInventory().add(soft.getProduct());
            player.getSkills().addExperience(Skills.CRAFTING, soft.getExperience());
            Location l = player.getLocation();
            boolean lumbridgeCowPen = l.inArea(new ZoneBorders(3253, 3255, 3265, 3271)) ||
                l.inArea(new ZoneBorders(3253, 3271, 3266, 3299)) ||
                l.inArea(new ZoneBorders(3240, 3298, 3256, 3298)) ||
                ((l.getX() >= 3240 && l.getX() <= 3288) && (l.getY() >= 3255 && l.getY() <= 3298));
            if (soft == SoftLeather.COIF && lumbridgeCowPen) {
                AchievementDiary.finalize(player, AchievementTask.CRAFT_COIF);
            }
            if (!Perk.NICE_THREADS.enabled(player)) {
                LeatherCrafting.decayThread(player);
                if (LeatherCrafting.isLastThread(player)) {
                    LeatherCrafting.removeThread(player);
                }
            }
        }
        amount--;
        return amount < 1;
    }


}
