package org.gielinor.game.content.skill.free.smithing.smelting;

import org.gielinor.game.content.skill.SkillPulse;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.utilities.misc.RandomUtil;
import org.gielinor.utilities.string.TextUtils;

import plugin.interaction.inter.SmeltingInterface.Smeltable;
import plugin.interaction.item.MaxCapePlugin;

/**
 * Represents the pulse used to smelt.
 *
 * @author 'Vexia
 */
public class SmeltingPulse extends SkillPulse<Item> {

    /**
     * Represents the Smeltable.
     */
    private final Smeltable smeltable;

    /**
     * Represents the amount to produce.
     */
    private int amount;

    /**
     * Represents if using the super heat spell.
     */
    private final boolean superHeat;

    /**
     * Constructs a new {@code SmeltingPulse} {@code Object}.
     *
     * @param player    the player.
     * @param node      the node.
     * @param smeltable the smeltable.
     * @param amount    the amount.
     */
    public SmeltingPulse(Player player, Item node, Smeltable smeltable, int amount) {
        super(player, node);
        this.smeltable = smeltable;
        this.amount = amount;
        this.superHeat = false;
        if (amount == 1) {
            setDelay(2);
        }
    }

    /**
     * Constructs a new {@code SmeltingPulse} {@code Object}.
     *
     * @param player    the player.
     * @param node      the node.
     * @param smeltable the smeltable.
     * @param amount    the amount.
     * @param heat      the heat.
     */
    public SmeltingPulse(Player player, Item node, Smeltable smeltable, int amount, boolean heat) {
        super(player, node);
        this.smeltable = smeltable;
        this.amount = amount;
        this.superHeat = heat;
        if (amount == 1) {
            super.setDelay(2);
        }
    }

    @Override
    public boolean checkRequirements() {
        player.getInterfaceState().closeChatbox();
        if (smeltable == null) {
            return false;
        }
//		if (smeltable == Smeltable.BLURITE && !player.getQuestRepository().isComplete("The Knight's Quest")) {
//			return false;
//		}
        if (player.getSkills().getLevel(Skills.SMITHING) < smeltable.getLevel()) {
            player.getActionSender().sendMessage("You need a Smithing level of " + smeltable.getLevel() + " in order to smelt " + smeltable.getBar().getName().toLowerCase().replace("bar", "") + ".");
            player.getInterfaceState().closeChatbox();
            return false;
        }
        if (!player.getInventory().contains(smeltable.getPrimary())) {
            player.getActionSender().sendMessage("You do not have the required ores to smelt this bar.");
            return false;
        }
        if (smeltable.getSecondary() != null && !player.getInventory().contains(smeltable.getSecondary())) {
            player.getActionSender().sendMessage("You do not have the required ores to smelt this bar.");
            return false;
        }
        return true;
    }

    @Override
    public void animate() {
        if (superHeat) {
            player.visualize(new Animation(723), new Graphics(148, 96));
        } else {
            player.animate(new Animation(899));
        }
    }

    @Override
    public void stop() {
        super.stop();
        animate();
    }

    @Override
    public boolean reward() {
        setDelay(5);
        if (!superHeat) {
            player.getActionSender().sendMessage("You place the required ores and attempt to create a smeltable of " + TextUtils.formatDisplayName(smeltable.toString().toLowerCase()) + ".", 1);
        }
        if (!player.getInventory().remove(smeltable.getPrimary()) || (smeltable.getSecondary() != null && !player.getInventory().remove(smeltable.getSecondary()))) {
            return true;
        }
        if (success(player)) {
            player.getInventory().add(smeltable.getBar());
            player.getSkills().addExperience(Skills.SMITHING,
                ((player.getEquipment().contains(776) || MaxCapePlugin.isWearing(player)) && smeltable == Smeltable.GOLD) ? smeltable.getExperience() * 2 : smeltable.getExperience());
            if (!superHeat) {
                player.getActionSender().sendMessage("You retrieve a smeltable of " + smeltable.getBar().getName().toLowerCase().replace(" smeltable", "") + ".", 1);
            }
        } else {
            player.getActionSender().sendMessage("The ore is too impure and you fail to refine it.", 1);
        }
        amount--;
        return amount < 1;
    }

    /**
     * Checks if the forging is a succes.
     *
     * @param player the player.
     * @return <code>True</code> if success.
     */
    public boolean success(Player player) {
        if (smeltable == Smeltable.IRON) {
            if (player.getDonorManager().getDonorStatus().getNoImpurityChance()) {
                return true;
            }
            if (player.getEquipment().get(Equipment.SLOT_RING) != null && player.getEquipment().get(Equipment.SLOT_RING).getId() == 2568) {
                return true;
            } else {
                return RandomUtil.getRandom(100) <= (player.getSkills().getLevel(Skills.SMITHING) >= 45 ? 80 : 50);
            }
        }
        return true;
    }

    @Override
    public void message(int type) {
    }

}
