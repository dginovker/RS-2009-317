package bot.commands

import bot.AccountLinkage
import de.btobastian.javacord.entities.User
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor

class VerifyCommand : CommandExecutor {

    @Command(aliases = arrayOf("::verify"), description = "Sends a verification code to the user to be redeemed in-game.")
    fun onVerifyCommand(user: User): String {
        val defaultMessage = "Check your private message!"
        if (AccountLinkage.userVerified(user)) {
            return "${user.mentionTag} just attempted to verify his already verified account. <:PogChamp:307458204340584460>"
        }
        if (AccountLinkage.databaseHasDiscordUser(user)) {
            val verificationCode = AccountLinkage.getVerificationCodeFromDatabase(user)
            if (verificationCode != null) {
                user.sendMessage("You have already generated a verification code! In-game, do the following command: \n**::verify $verificationCode**")
                return defaultMessage
            } else {
                return "Something appears to be wrong. \uD83E\uDD14 Report this to an admin!"
            }
        }
        val verificationCode = AccountLinkage.generateVerificationCode(user)
        user.sendMessage("Your verification code has been generated. In-game, do the following command: \n**::verify $verificationCode**")
        return defaultMessage
    }
}
