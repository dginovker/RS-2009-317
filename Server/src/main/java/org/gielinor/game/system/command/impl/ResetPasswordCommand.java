package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;

/**
 * Allows an owner to reset a player's current password.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ResetPasswordCommand extends Command {

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
        return new String[]{ "resetpass", "resetpassword" };
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 3) {
            player.getActionSender().sendMessage("Use as ::resetpassword <lt>playername> <lt>new password>");
            return;
        }
        String playerName = args[0];
        player.getActionSender().sendMessage("Currently unavailable. Contact Mike.");
        // String passwordHashed = null;
        // try {
        // passwordHashed = MD5.hash(MD5.hash(playerName) +
        // player.getDetails().getSalt());
        // } catch (NoSuchAlgorithmException e) {
        // e.printStackTrace();
        // player.getActionSender().sendMessage("Use as ::setpassword
        // <lt>current password> <lt>new password>");
        // return;
        // }
        // if (passwordHashed == null) {
        // player.getActionSender().sendMessage("Use as ::setpassword
        // <lt>current password> <lt>new password>");
        // return;
        // }
        // String newPassword = args[1];
        // if (!StringUtils.isAlphanumeric(newPassword)) {
        // player.getActionSender().sendMessage("Your new password must be
        // alpha-numeric.");
        // return;
        // }
        // if (newPassword.length() > 18) {
        // player.getActionSender().sendMessage("Your new password cannot exceed
        // 18 characters.");
        // return;
        // }
        // try {
        // player.getDetails().setPassword(MD5.hash(MD5.hash(newPassword) +
        // player.getDetails().getSalt()));
        // } catch (NoSuchAlgorithmException e) {
        // e.printStackTrace();
        // player.getActionSender().sendMessage("Use as ::setpassword
        // <lt>current password> <lt>new password>");
        // return;
        // }
        // player.getActionSender().sendMessage("Your password has been changed
        // to \"" + newPassword + "\".");
    }
}
