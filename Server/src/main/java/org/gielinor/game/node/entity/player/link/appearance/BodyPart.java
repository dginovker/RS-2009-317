package org.gielinor.game.node.entity.player.link.appearance;

import java.nio.ByteBuffer;

import org.gielinor.parser.player.SavingModule;

/**
 * Represents a body part of a player encapsulating the type and colour.
 *
 * @author 'Vexia
 */
public final class BodyPart implements SavingModule {

    /**
     * Represents the look value of the part.
     */
    private int look;

    /**
     * Represents the colour of this part.
     */
    private int colour;

    /**
     * Constructs a new {@code BodyPart} {@code Object}.
     */
    public BodyPart() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code BodyPart} {@code Object}.
     *
     * @param look  the look.
     * @param color the colour.
     */
    public BodyPart(final int look, final int color) {
        this.look = look;
        this.colour = color;
    }

    /**
     * Constructs a new {@code BodyPart} {@code Object}.
     *
     * @param look the look.
     */
    public BodyPart(final int look) {
        this(look, 0);
    }

    @Override
    public void save(ByteBuffer buffer) {
        buffer.putInt(look);
        buffer.putInt(colour);
    }

    @Override
    public void parse(ByteBuffer buffer) {
        setLook(buffer.getInt());
        setColour(buffer.getInt());
    }

    /**
     * Method used to change the look value.
     *
     * @param look the look value.
     */
    public void setLook(final int look) {
        this.look = look;
    }

    /**
     * Method used to change the colour value.
     *
     * @param colour the colour value.
     */
    public void setColour(final int colour) {
        this.colour = colour;
    }

    /**
     * Gets the look.
     *
     * @return The look.
     */
    public int getLook() {
        return look;
    }

    /**
     * Gets the colour.
     *
     * @return The colour.
     */
    public int getColour() {
        return colour;
    }

}
