package org.gielinor.game.content.skill.member.summoning.pet;

import java.nio.ByteBuffer;

import org.gielinor.parser.player.SavingModule;

/**
 * A class containing pet details for a certain pet.
 *
 * @author Emperor
 */
public final class PetDetails implements SavingModule {

    /**
     * The hunger rate.
     */
    private double hunger = 0.0;

    /**
     * The growth rate.
     */
    private double growth = 0.0;

    /**
     * The current stage of the pet (0 - baby, 1 - grown, 2 - overgrown).
     */
    private int stage;

    /**
     * Is the pet insured through pet insurance?
     */
    private boolean insured;

    /**
     * Constructs a new {@code PetDetails} {@code Object}.
     *
     * @param growth The growth value.
     */
    public PetDetails(double growth) {
        this.setGrowth(growth);
    }

    @Override
    public void save(ByteBuffer buffer) {
        buffer.putDouble(hunger);
        buffer.putDouble(growth);
        buffer.put((byte) stage);
        buffer.put((byte) (insured ? 1 : 0));
    }

    @Override
    public void parse(ByteBuffer buffer) {
        setHunger(buffer.getDouble());
        setGrowth(buffer.getDouble());
        stage = buffer.get();
        insured = buffer.get() != 0;
    }

    /**
     * Increases the hunger value by the given amount.
     *
     * @param amount The amount.
     */
    public void updateHunger(double amount) {
        setHunger(hunger + amount);
        if (hunger < 0.0) {
            setHunger(0.0);
        } else if (hunger > 100.0) {
            setHunger(100.0);
        }
    }

    /**
     * Increases the growth value by the given amount.
     *
     * @param amount The amount.
     */
    public void updateGrowth(double amount) {
        setGrowth(growth + amount);
        if (growth < 0.0) {
            setGrowth(0.0);
        } else if (growth > 100.0) {
            setGrowth(100.0);
        }
    }

    /**
     * Gets the hunger.
     *
     * @return The hunger.
     */
    public double getHunger() {
        return hunger;
    }

    /**
     * Gets the growth.
     *
     * @return The growth.
     */
    public double getGrowth() {
        return growth;
    }

    /**
     * Gets the stage.
     *
     * @return The stage.
     */
    public int getStage() {
        return stage;
    }

    /**
     * Sets the stage.
     *
     * @param stage The stage to set.
     */
    public void setStage(int stage) {
        this.stage = stage;
    }

    public void setHunger(double hunger) {
        this.hunger = hunger;
    }

    public void setGrowth(double growth) {
        this.growth = growth;
    }

    public boolean isInsured() {
        return insured;
    }

    public void setInsured(boolean insured) {
        this.insured = insured;
    }
}