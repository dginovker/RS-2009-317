package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Handles punishment commands, such as ::ban and ::mute.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PunishmentCommand extends Command {

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
        return new String[]{ "mute", "ban", "permban", "ipban", "macban", "unban", "unmute", "unmacban", "unipban" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("ban", "Bans a player from the server", getRights(),
            "::ban <lt>length_in_days> <lt>player_name>"));
        CommandDescription.add(new CommandDescription("permban", "Permanently bans a player from the server",
            getRights(), "::permban <lt>player_name>"));
        CommandDescription.add(new CommandDescription("ipban", "Bans a player's ip address from the server",
            getRights(), "::ipban <lt>player_name>"));
        CommandDescription.add(new CommandDescription("macban", "Bans a player's mac address from the server",
            getRights(), "::macban <lt>player_name>"));
    }

    @Override
    public void execute(Player player, String[] args) {
		/*if (args.length < 2) {
			player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>length in days> <lt>player name>");
			return;
		}

		String playerName = (args[0].equalsIgnoreCase("unmute") || args[0].equalsIgnoreCase("unban")
				|| args[0].equalsIgnoreCase("unmacban") || args[0].equalsIgnoreCase("unipban")) ? toString(args, 1)
						: toString(args, 2);
		int days = -1;
		if (!(args[0].equalsIgnoreCase("unmute") || args[0].equalsIgnoreCase("unban")
				|| args[0].equalsIgnoreCase("unmacban") || args[0].equalsIgnoreCase("unipban"))) {
			if (!StringUtils.isNumeric(args[1])) {
				player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>length in days> <lt>player name>");
				return;
			}
			days = Integer.parseInt(args[1]);
			if (args[0].equalsIgnoreCase("permban") || playerName.startsWith("perm")) {
				days = Integer.MAX_VALUE;
			}
		}
		Player target = Repository.getPlayerByName(playerName);
		if (target == null) {
			playerName = playerName.replaceAll(" ", "_");
			target = PlayerLoader.getPlayerFile(playerName);
			if (target == null) {
				player.getActionSender().sendMessage("The player \"" + playerName + "\" could not be found.");
				return;
			}
		}
		if (target == null) {
			player.getActionSender().sendMessage("The player \"" + playerName + "\" could not be found.");
			return;
		}
		switch (args[0].toLowerCase()) {
		case "ban":
		case "permban":
			if (target.getDetails().getPortal().getBan().punish(target, days)) {
				target.getDetails().save();
				player.getActionSender()
						.sendMessage("You have banned player - " + target.getUsername() + ", for " + days + " days.");
			} else {
				player.getActionSender().sendMessage("Unable to ban player - " + target.getUsername());
			}
			break;
		case "ipban":
			if (target.getDetails().getPortal().getBan().punish(target, Integer.MAX_VALUE)
					&& IPBanRepository.getRepository().ban(target.getDetails().getIp())) {
				target.getDetails().save();
				player.getActionSender().sendMessage(
						"You have ip banned player - " + target.getUsername() + ", ip=" + target.getDetails().getIp());
				if (target.isActive()) {
					target.clear();
				}
			}
			break;
		case "macban":
			// if (target.getDetails().getPortal().getBan().punish(target,
			// Integer.MAX_VALUE) &&
			// MacBanRepository.getRepository().ban(target.getDetails().getMacAddress()))
			// {
			// target.getDetails().save();
			// player.getActionSender().sendMessage("You have mac banned player
			// - " + target.getUsername() + ", ip=" +
			// target.getDetails().getIp());
			// if (target.isActive()) {
			// target.clear();
			// }
			// } else {
			// player.getActionSender().sendMessage("Unable to mac ban player -
			// " + target.getUsername());
			// }
			break;
		case "unban":
			player.getActionSender()
					.sendMessage("You have successfully unbanned player - " + target.getUsername() + ".");
			target.getDetails().getPortal().getBan().reset();
			target.getDetails().save();
			IPBanRepository.getRepository().remove(target.getDetails().getIp());
			MacBanRepository.getRepository().remove(target.getDetails().getMacAddress());
			break;
		case "unmute":
			target = Repository.getPlayerByName(toString(args, 1));
			if (target == null) {
				target = PlayerLoader.getPlayerFile(toString(args, 1).replaceAll(" ", "_"));
				if (target == null) {
					player.debug("error: character was not found.");
					return;
				}
			}
			player.getActionSender()
					.sendMessage("You have successfully unmuted player - " + target.getUsername() + ".");
			target.getDetails().getPortal().getMute().reset();
			target.getDetails().save();
			break;
		}*/
    }
}
