package org.gielinor.game.system.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.MusicContext;
import org.gielinor.net.packet.out.MusicPacket;

/**
 * Plays a sound.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PlaySoundCommand extends Command {

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
        return new String[]{ "sound", "playsound", "music", "playmusic" };
    }

    @Override
    public void init() {
        CommandDescription.add(
            new CommandDescription("playsound", "Plays a sound by id", getRights(), "::playsound <lt>sound_id>"));
        CommandDescription.add(
            new CommandDescription("playmusic", "Plays music by id", getRights(), "::playmusic <lt>music_id>"));
    }

    @Override
    public void execute(Player player, String[] args) {
        boolean music = args[0].toLowerCase().contains("music");
        if (args.length < 2 || !StringUtils.isNumeric(args[1])) {
            player.getActionSender()
                .sendMessage("Use as ::" + args[0] + " <lt>" + (music ? "music" : "sound") + "_id>");
            return;
        }
        if (music) {
            PacketRepository.send(MusicPacket.class, new MusicContext(player, Integer.parseInt(args[1]), false));
        } else {
            player.getAudioManager().send(new Audio(Integer.parseInt(args[1]), 0, 0));
        }
    }
}
