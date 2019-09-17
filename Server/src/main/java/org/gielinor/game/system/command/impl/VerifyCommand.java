package org.gielinor.game.system.command.impl;

import bot.AccountLinkage;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

public class VerifyCommand extends Command {

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
        return new String[]{ "verify" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("verify", "Links your in-game account to discord.", getRights(),
            "::verify <lt>verification code provided>"));
    }

    @Override
    public void execute(final Player player, String[] args) {
        if (args.length < 2) {
            player.getActionSender().sendMessage("Use as ::verify <lt>verification code provided>");
            return;
        }
        AccountLinkage.verify(player, args[1]);
    }
}
