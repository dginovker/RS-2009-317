package org.gielinor.game.system.command.impl

import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.entity.player.info.Ironman
import org.gielinor.game.node.entity.player.info.Rights
import org.gielinor.game.system.command.Command
import org.gielinor.game.system.command.CommandDescription
import org.gielinor.game.world.World
import org.gielinor.game.world.repository.Repository
import org.gielinor.rs2.config.Constants
import org.gielinor.rs2.config.Constants.YELL_TIMER
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 * Lets players communicate to each other through a server message, the very
 * basic and useful "yell" command
 *
 * @author [Gielinor Logan G.](https://Gielinor.org)
 * @author Corey
 * @author Arham
 */
class YellCommand : Command() {

    private val logger = LoggerFactory.getLogger(YellCommand::class.java.name)

    override fun getRights(): Rights {
        return Rights.REGULAR_PLAYER
    }

    override fun canUse(player: Player): Boolean {
        return true
    }

    override fun getCommands(): Array<String> {
        return arrayOf("yell", "modyell", "modhelp", "adminhelp", "adminyell", "message", "servermessage")
    }

    override fun init() {
        CommandDescription.add(CommandDescription("yell", "Sends a global message to all<br>players online", rights, "::yell <lt>message><br>Example:<br>::yell Hey!"))
        CommandDescription.add(CommandDescription("modhelp", "Sends a global message to all<br>moderators online", rights, "::modhelp <lt>message><br>Example:<br>::modhelp There's a spammer at home!"))
        CommandDescription.add(CommandDescription("adminhelp", "Sends a global message to all<br>administrators online", rights, "::adminhelp <lt>message><br>Example:<br>::adminyell Someone is duping items at home!"))
        CommandDescription.add(CommandDescription("message", "Sends a priority message to all<br>players online", Rights.GIELINOR_MODERATOR, "::message <lt>message><br>Example:<br>::message There will be an update in 10 minutes"))
    }

    override fun execute(player: Player, args: Array<String>) {
        if (args.isNotEmpty()) {
            val yellText = toString(args, 1)
            if (yellText == null || yellText.isEmpty()) {
                player.actionSender.sendMessage("Use as ::" + args[0].toLowerCase() + " <lt>words>")
                return
            }
            if (args[0].equals("yell", ignoreCase = true)) {
                performYell(player, yellText)
            } else {
                Repository.getPlayers()
                    .filter { it != null && it.isActive }
                    .forEach {
                        if (args[0].equals("message", ignoreCase = true)
                            || args[0].equals("servermessage", ignoreCase = true) && player.rights === Rights.GIELINOR_MODERATOR) {
                            it.actionSender.sendMessage("[SERVER]: " + yellText)
                        } else if (args[0].startsWith("modyell")
                            || args[0].startsWith("modhelp")) {
                            if (it.rights.ordinal >= Rights.PLAYER_MODERATOR.ordinal) {
                                it.actionSender.sendMessage("<col=666>[NEEDS MOD HELP]<col=000> " + player.username + ": " + yellText.replace("<".toRegex(), "<lt>"))
                            }
                        } else if (args[0].startsWith("adminyell")
                            || args[0].startsWith("adminhelp")) {
                            if (it.rights === Rights.GIELINOR_MODERATOR) {
                                it.actionSender.sendMessage("<col=666>[NEEDS ADMIN HELP]<col=000> " + player.username + ": " + yellText.replace("<".toRegex(), "<lt>"))
                            }
                        }
                    }
            }
            if (args[0].toLowerCase().equals("modyell", ignoreCase = true)
                || args[0].toLowerCase().equals("modhelp", ignoreCase = true)
                || args[0].toLowerCase().equals("adminyell", ignoreCase = true)
                || args[0].toLowerCase().equals("adminhelp", ignoreCase = true)) {
                player.actionSender.sendMessage("Your message has been submitted to all online " +
                    if (args[0].toLowerCase().equals("modyell", ignoreCase = true) || args[0].toLowerCase().equals("modhelp", ignoreCase = true))
                        "player moderators"
                    else
                        Constants.SERVER_NAME + " moderators.")
            }
        } else {
            player.actionSender.sendMessage("Use as ::" + args[0].toLowerCase() + " <lt>words>")
        }
    }

    private fun performYell(player: Player, yellMessage: String) {
        if (canYell(player, yellMessage)) {
            val yellTagContents = getTagContents(player)
            val yellIcons = getCrowns(player)
            val yellTag = "[" + getTagColour(player) + yellTagContents + "</shad></col>]"
            val yellToSend = yellTag + " " + yellIcons + "" + player.username + ": <col=" + Constants.YELL_MESSAGE_COLOUR + ">" + yellMessage

            Repository.getPlayers()
                .filter { it != null && it.isActive }
                .forEach {
                    it.actionSender.sendMessage(yellToSend.capitalize())
                    // TODO log yells
                }
            player.savedData.globalData.setLastYell()
        }
    }

    private fun getTagContents(player: Player): String {
        return when {
            !YellTagCommand.canChange(player) -> {
                player.savedData.globalData.yellTag = ""
                return getDefaultTag(player)
            }
            player.savedData.globalData.yellTag.trim { it <= ' ' } == "" -> getDefaultTag(player)
            else -> player.savedData.globalData.yellTag
        }
    }

    private fun getDefaultTag(player: Player): String {
        return when {
            player.rights == Rights.PLAYER_MODERATOR -> "Moderator"
            player.rights == Rights.DEVELOPER -> "Developer"
            player.rights == Rights.GIELINOR_MODERATOR -> "Gielinor Moderator" // TODO add bold for admin and developer
            player.donorManager.isSapphireMember -> "Sapphire Member"
            player.donorManager.isEmeraldMember -> "Emerald Member"
            player.donorManager.isRubyMember -> "Ruby Member"
            player.donorManager.isDiamondMember -> "Diamond Member"
            player.donorManager.isDragonstoneMember -> "Dragonstone Member"
            player.donorManager.isOnyxMember -> "Onyx Member"
            player.donorManager.isZenyteMember -> "Zenyte Member"
            Ironman.isIronman(player) -> player.savedData.globalData.ironmanMode.shortName // btw
            else -> "Player"
        }
    }

    private fun getCrowns(player: Player): String {
        val sb = StringBuilder()
        when {
            player.rights.imageId != -1 -> sb.append("<img=" + player.rights.imageId + ">")
            player.donorManager.hasMembership() -> sb.append("<img=" + player.donorManager.donorStatus.chatIcon + ">")
            Ironman.isIronman(player) -> sb.append("<img=" + player.savedData.globalData.ironmanMode.crownId + ">")
        }
        return sb.toString().trim { it <= ' ' }
    }

    private fun canYell(player: Player, yell: String): Boolean {
        return when (isAllowed(player, yell)) {
            YellCommand.YellResponse.ALLOWED -> true
            YellCommand.YellResponse.MUTED -> {
                player.details.portal.mute.inflict(player)
                false
            }
            YellCommand.YellResponse.YELL_TIMER -> {
                val duration = if (player.donorManager.isSapphireMember) 10 else 30
                player.actionSender.sendMessage("You have already yelled in the last $duration seconds!")
                false
            }
            YellCommand.YellResponse.DISALLOWED_PHRASE -> {
                player.actionSender.sendMessage("Your yell message contains a disallowed phrase!")
                false
            }
            YellCommand.YellResponse.ERROR -> {
                logger.info("Error when sending yell message [{}].", yell)
                false
            }
        }
    }

    private fun isAllowed(player: Player, yell: String): YellResponse {
        return when {
            player.rights === Rights.GIELINOR_MODERATOR -> YellResponse.ALLOWED
            player.rights == Rights.DEVELOPER -> YellResponse.ALLOWED
            player.details.portal.mute.isPunished -> YellResponse.MUTED
            !YELL_TIMER.canYell(player) -> YellResponse.YELL_TIMER
            containsDisallowedPhrase(yell) -> YellResponse.DISALLOWED_PHRASE
            else -> YellResponse.ALLOWED
        }
    }

    private fun containsDisallowedPhrase(yell: String): Boolean {
        Constants.DISALLOWED_PHRASES.forEach {
            if (yell.toLowerCase().contains(it.toLowerCase())) return true
        }
        return false
    }

    private enum class YellResponse {
        ERROR,
        DISALLOWED_PHRASE,
        YELL_TIMER,
        MUTED,
        ALLOWED
    }

    class YellTimer(private val unit: TimeUnit) {

        fun canYell(player: Player): Boolean {
            val lastYell = player.savedData.globalData.lastYell
            val duration = if (player.donorManager.isSapphireMember) 10L else 30L
            val timerDuration = unit.toMillis(duration)
            val currentTime = System.currentTimeMillis()

            return when {
                player.donorManager.hasMembership() && !player.donorManager.isSapphireMember -> true
                duration <= 0L -> true
                lastYell == 0L -> true
                lastYell + timerDuration < currentTime -> true
                isExempt(player) -> true
                World.getConfiguration().isDevelopmentEnabled -> true
                else -> false
            }
        }

        private fun isExempt(player: Player): Boolean {
            return when {
                player.rights == Rights.PLAYER_MODERATOR -> true
                player.rights == Rights.DEVELOPER -> true
                player.rights == Rights.GIELINOR_MODERATOR -> true
                player.donorManager.hasMembership() -> true
                World.getConfiguration().isDevelopmentEnabled -> true
                else -> false
            }
        }
    }

    companion object {
        @JvmStatic
        fun getTagColour(player: Player): String {
            return when {
                player.rights == Rights.PLAYER_MODERATOR -> "<col=ededed><shad=1>" // a2a2a2
                player.rights == Rights.DEVELOPER -> "<col=7800a0>"
                player.rights == Rights.GIELINOR_MODERATOR -> "<col=f7ff0b><shad=1>" // f7ff0b
                player.donorManager.hasMembership() -> "<col=" + player.donorManager.donorStatus.color + ">"
                Ironman.isIronman(player) -> "<col=" + player.savedData.globalData.ironmanMode.colour + ">" // btw
                else -> ""
            }
        }
    }

}
