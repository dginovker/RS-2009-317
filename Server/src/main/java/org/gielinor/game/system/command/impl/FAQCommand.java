package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.URLContext;
import org.gielinor.net.packet.out.URLPacket;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.config.ServerVar;

/**
 * Opens the FAQ web-page for the player.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class FAQCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.REGULAR_PLAYER;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "faq" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("faq", "Opens the " + Constants.SERVER_NAME + " FAQ page", getRights(), null));
    }

    @Override
    public void execute(final Player player, String[] args) {
        PacketRepository.send(URLPacket.class, new URLContext(player, ServerVar.fetch("faq_url")));
    }
}
