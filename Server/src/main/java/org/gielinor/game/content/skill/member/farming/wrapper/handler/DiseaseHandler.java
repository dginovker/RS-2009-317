package org.gielinor.game.content.skill.member.farming.wrapper.handler;

import org.gielinor.game.content.skill.member.farming.wrapper.PatchCycle;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the class used to handle patch diseasing.
 *
 * @author 'Vexia
 */
public final class DiseaseHandler {

    /**
     * Represents the patch cycle.
     */
    private final PatchCycle cycle;

    /**
     * Constructs a new {@code DiseaseHandler} {@code Object}.
     *
     * @param cycle the cycle.
     */
    public DiseaseHandler(final PatchCycle cycle) {
        this.cycle = cycle;
    }

    /**
     * Method used to handle the disease effect.
     *
     * @return <code>True</code> if diseased or set to death.
     */
    public boolean handle() {
        if (isDiseased()) {
            cycle.getDeathHandler().handle();
            return true;
        } else if (cycle.getDiseaseHandler().canDisease()) {
            setDisease();
            return true;
        }
        return false;
    }

    /**
     * Methhod used to remove the disease config.
     */
    public void removeDisease() {
        cycle.addConfigValue(cycle.getNode().getBase() + cycle.getState() - getDiseaseBase());
    }

    /**
     * Method used to set the disease config.
     */
    public void setDisease() {
        cycle.addConfigValue(getDiseaseBase() + (cycle.getState() - cycle.getNode().getBase()));
    }

    /**
     * Method used to cure the patch.
     *
     * @param player the player.
     */
    public void cure(final Player player, boolean message) {
        if (isDiseased()) {
            removeDisease();
            cycle.getGrowthHandler().setGrowthUpdate();
            if (message) {
                player.getActionSender().sendMessage("You treat the " + cycle.getWrapper().getName() + " with plant cure.");
                player.getActionSender().sendMessage("It restored health.");
            }
        }
    }

    /**
     * Checks if we should disease the plant.
     *
     * @return <code>True</code> if so.
     */
    public boolean canDisease() {
        return cycle.getNode().canDisease(cycle);
    }

    /**
     * Checks if the patch is diseased.
     *
     * @return <code>True</code> if so.
     */
    public boolean isDiseased() {
        if (cycle.getNode() == null) {
            return false;
        }
        int state = cycle.getState();
        for (int i = 1; i < cycle.getNode().getGrowthCycles(); i++) {
            if (state == getDiseaseBase() + i) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the diseased base.
     *
     * @return the diseased base config.
     */
    public int getDiseaseBase() {
        return (cycle.getNode().getBase() + cycle.getNode().getDiseaseBase());
    }

}
