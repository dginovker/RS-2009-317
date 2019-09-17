package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.entity.player.link.music.MusicEntry;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Unlocks all musics within the player's jukebox.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class UnlockMusicCommand extends Command {

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
        return new String[]{ "unlockmusic", "unlockallmusic", "unlockmusics" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("unlockmusic", "Unlocks all music data", getRights(), null));
    }

    @Override
    public void execute(final Player player, String[] args) {
        for (MusicEntry musicEntry : MusicEntry.getSongs().values()) {
            player.getMusicPlayer().unlock(musicEntry.getId());
        }
    }
}
