package org.gielinor.game.content.skill.free.prayer;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;

import plugin.skill.prayer.BoneBuryingOptionPlugin.Bones;

/**
 * Represents the {@link org.gielinor.game.content.skill.SkillPulse} for offering bones on an altar.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class OfferBonesPulse extends SkillPulse<Item> {

    /**
     * The additional experience a player is given.
     */
    private static final double ADDITIONAL_EXPERIENCE = 250D;
    /**
     * The offer {@link org.gielinor.game.world.update.flag.context.Animation}.
     */
    private static final Animation OFFER_ANIMATION = new Animation(896);
    /**
     * The offer {@link org.gielinor.game.world.update.flag.context.Graphics} to play.
     */
    private static final Graphics OFFER_GRAPHICS = Graphics.create(624);
    /**
     * Represents the {@link plugin.skill.prayer.BoneBuryingOptionPlugin.Bones} to offer.
     */
    private final Bones bones;
    /**
     * Represents the {@link org.gielinor.game.node.object.GameObject} of the altar used.
     */
    private final GameObject gameObject;
    /**
     * Represents the amount to make.
     */
    private int amount;
    /**
     * Represents the ticks passed.
     */
    private int ticks;

    /**
     * Constructs a new {@code OfferBonesPulse} {@Code Object}
     *
     * @param player     The player.
     * @param item       The bone item.
     * @param gameObject The altar {@link org.gielinor.game.node.object.GameObject}.
     * @param amount     The amount to offer.
     * @param bones      The {@link plugin.skill.prayer.BoneBuryingOptionPlugin.Bones} to offer.
     */
    public OfferBonesPulse(Player player, Item item, GameObject gameObject, int amount, final Bones bones) {
        super(player, item);
        this.gameObject = gameObject;
        this.amount = amount;
        this.bones = bones;
        this.resetAnimation = false;
    }

    @Override
    public boolean checkRequirements() {
        return player.getInventory().contains(bones.getId());
    }

    @Override
    public void animate() {
        if (ticks % 5 == 0 || ticks < 1) {
            player.animate(OFFER_ANIMATION);
            Graphics.send(OFFER_GRAPHICS, gameObject.getLocation());
        }
    }

    @Override
    public boolean reward() {
        if (++ticks % 5 != 0) {
            return false;
        }
        double additionalExperience = ((bones.getExperience() * ADDITIONAL_EXPERIENCE) / 100);
        boolean removeBones = true;
        boolean savedBones = player.getPerkManager().isTriggered(Perk.BONE_SAVIOR);
        if (!savedBones) {
            removeBones = player.getInventory().remove(new Item(bones.getId()));
        }
        if (removeBones || savedBones) {
            player.getSkills().addExperience(Skills.PRAYER, bones.getExperience() + additionalExperience);
            player.getActionSender().sendMessage("The gods are very pleased with your offering.", 1);
            if (savedBones) {
                player.getActionSender().sendMessage("Your Bone savior perk saves the bones from being used!", 1);
            }
        }
        amount--;
        return amount < 1;
    }
}
