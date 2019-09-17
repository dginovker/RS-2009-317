package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

import plugin.interaction.inter.EmoteTabInterface.Emote;

/**
 * Unlocks all of the player's emotes.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class UnlockEmotesCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "unlockemotes" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("unlockemotes", "Unlocks all emotes", getRights(), null));
    }

    @Override
    public void execute(final Player player, String[] args) {
        for (Emote emote : Emote.values()) {
            if (emote.getConfigId() > 0) {
                player.getEmotes().unlock(emote);
            }
        }
    }
}
