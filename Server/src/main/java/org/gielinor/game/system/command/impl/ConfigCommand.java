package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ConfigCommand extends Command {

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
        return new String[]{ "cfg", "icfg", "config", "iconfig" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("cfg", "Sets a client configuration value", getRights(),
            "::cfg <lt>config id> <lt>config value>"));
        CommandDescription.add(new CommandDescription("icfg", "Sets a client interface configuration value",
            getRights(), "::icfg <lt>config id> <lt>config value>"));
    }

    @Override
    public void execute(final Player player, String[] args) {
        if (args.length < 3) {
            player.getActionSender().sendMessage("Use as ::" + args[0].toLowerCase() + " <lt>config id> <lt>value>");
            return;
        }
        if (args[0].equalsIgnoreCase("cfg")) {
            int value = -1;
            if (args.length > 3) {
                String s = toString(args, 2);
                if (!s.contains(" ")) {
                    player.getActionSender()
                        .sendMessage("Use as ::" + args[0].toLowerCase() + " <lt>config id> <lt># #> (shift)");
                    return;
                }
                String[] data = s.split(" ");
                int shift = Integer.parseInt(data[0]);
                int bits = Integer.parseInt(data[1]);
                value = shift << bits;
            } else {
                value = Integer.parseInt(args[2]);
            }
            player.getConfigManager().set(Integer.parseInt(args[1]), value, false);
            return;
        }
        player.getActionSender().sendInterfaceConfig(Integer.parseInt(args[1]), Integer.decode(args[2]));
    }
}
