package org.gielinor.game.content.dialogue;

import org.gielinor.game.node.entity.player.Player;

/**
 * Represents a dialogue action.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public interface DialogueAction {

    /**
     * Handles a {@link org.gielinor.game.content.dialogue.DialoguePlugin} action.
     *
     * @param player       The player.
     * @param optionSelect The {@link org.gielinor.game.content.dialogue.OptionSelect}.
     */
    public void handle(Player player, OptionSelect optionSelect);

}
