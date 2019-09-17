package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The {@link org.gielinor.net.packet.out.InputDialogue} context.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class InputDialogueContext implements Context {

    /**
     * The player.
     */
    private Player player;

    /**
     * The context / question.
     */
    private String context;

    /**
     * Whether or not it is a numeric input.
     */
    private boolean numeric;

    /**
     * Constructs a new {@code InputDialogueContext} {@code Object}.
     *
     * @param player  The player.
     * @param context The context / question.
     * @param numeric Whether or not it is a numeric input.
     */
    public InputDialogueContext(Player player, String context, boolean numeric) {
        this.player = player;
        this.context = context;
        this.setNumeric(numeric);
    }

    /**
     * Constructs a new {@code InputDialogueContext} {@code Object}.
     *
     * @param player The player.
     */
    public InputDialogueContext(Player player) {
        this.player = player;
    }

    /**
     * Transforms this context for the new player.
     *
     * @param player The player.
     * @return The input dialogue context created.
     */
    public InputDialogueContext transform(Player player) {
        return new InputDialogueContext(player, context, isNumeric());
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player.
     *
     * @param player The player.
     * @return This context instance.
     */
    public Context setPlayer(Player player) {
        this.player = player;
        return this;
    }

    /**
     * Gets the context / question.
     *
     * @return The context.
     */
    public String getContext() {
        return context;
    }

    /**
     * Gets whether or not it is a numeric input.
     *
     * @return <code>True</code> if so.
     */
    public boolean isNumeric() {
        return numeric;
    }

    /**
     * Sets whether or not it is a numeric input.
     *
     * @param numeric Whether or not this context is numeric.
     */
    public void setNumeric(boolean numeric) {
        this.numeric = numeric;
    }
}