package bot

import bot.commands.RewardCommand
import de.btobastian.javacord.entities.User
import org.gielinor.database.DataSource
import org.gielinor.database.loadDataFromDatabase
import org.gielinor.database.saveDataToDatabase
import org.gielinor.game.node.entity.player.Player
import org.gielinor.util.extensions.compareHours
import org.gielinor.utilities.misc.RandomUtil
import org.slf4j.LoggerFactory

/**
 * Handles the functions for linking the discord and in-game accounts.
 */
object AccountLinkage {
    private val verificationCodesDatabaseTable = "verification_codes"
    private val verificationCodesDatabaseVerificationCode = "verification_code"
    private val verificationCodesDatabaseDiscordId = "discord_id"
    private val verificationCodesDatabasePlayerId = "pidn"
    private val verificationCodesDatabaseVerified = "verified"

    private val rewardTimesDatabaseTable = "reward_times"
    private val rewardTimesDatabasePlayerId = "pidn"
    private val rewardTimesDatabaseLastRewardedTime = "last_rewarded"

    private val logger = LoggerFactory.getLogger(javaClass)

    @JvmStatic
    fun verify(player: Player, attemptedCode: String) {
        if (databaseHasVerificationCode(attemptedCode)) {
            if (verificationCodeVerified(attemptedCode)) {
                player.actionSender.sendMessage("That code is already verified!")
                return
            }
            DataSource.getGameConnection().saveDataToDatabase("UPDATE $verificationCodesDatabaseTable SET $verificationCodesDatabasePlayerId=?,$verificationCodesDatabaseVerified=? WHERE $verificationCodesDatabaseVerificationCode=?") {
                setString(1, player.pidn.toString())
                setBoolean(2, true)
                setString(3, attemptedCode)
            }
            DataSource.getGameConnection().saveDataToDatabase("INSERT INTO $rewardTimesDatabaseTable ($rewardTimesDatabasePlayerId) VALUES (?)") {
                setInt(1, player.pidn)
            }
            player.actionSender.sendMessage("Your discord and in-game account are now linked!")
            DiscordBot.getBot().sendMessage(DiscordConstants.BOT_SPAM_CHANNEL_ID, "<@${getDiscordUserFromDatabase(player.pidn)}> just linked their in-game account with their discord account! Hurrah! \uD83D\uDCAF")
        } else {
            player.actionSender.sendMessage("That verification code does not exist!")
            player.actionSender.sendMessage("If you feel this is an error, report this to staff.")
            logger.warn("${player.username} (${player.pidn}) attempted the following code: " + attemptedCode)
        }
    }

    internal fun reward(pidn: Int, bankSlot: Int) {
        var actualBankSlot = bankSlot
        DataSource.getGameConnection().saveDataToDatabase("INSERT INTO player_bank (pidn, bank_slot, item_id, item_quantity, item_charge) VALUES(?, ?, ?, ?, ?)") {
            setInt(1, pidn)
            for (reward in RewardCommand.REWARDS) {
                setInt(2, actualBankSlot)
                setInt(3, reward.id)
                setInt(4, reward.count)
                setInt(5, reward.charge)
                addBatch()
                actualBankSlot++
            }
            executeBatch()
        }
        saveTime(pidn, System.currentTimeMillis())
    }

    internal fun userVerified(user: User): Boolean {
        DataSource.getGameConnection().loadDataFromDatabase("SELECT $verificationCodesDatabaseVerified FROM $verificationCodesDatabaseTable WHERE $verificationCodesDatabaseDiscordId=${user.id}") {
            return getBoolean(1)
        }
        return false
    }

    internal tailrec fun generateVerificationCode(user: User): String? {
        val string = "abcdefghijklmnopqrstuvwxyz0123456789".split("")
        var code = ""
        for (i in 0..9) {
            code += string[RandomUtil.random(0, string.size - 1)]
        }
        if (databaseHasVerificationCode(code)) {
            return generateVerificationCode(user)
        }
        DataSource.getGameConnection().saveDataToDatabase("INSERT INTO $verificationCodesDatabaseTable($verificationCodesDatabaseDiscordId,$verificationCodesDatabaseVerificationCode, $verificationCodesDatabaseVerified) VALUES (?,?,?)") {
            setString(1, user.id)
            setString(2, code)
            setBoolean(3, false)
        }
        return code
    }

    internal fun databaseHasDiscordUser(user: User): Boolean {
        DataSource.getGameConnection().loadDataFromDatabase("SELECT $verificationCodesDatabaseDiscordId FROM $verificationCodesDatabaseTable") {
            val discordId = getString(1)
            if (discordId == user.id) {
                return true
            }
        }
        return false
    }

    internal fun getVerificationCodeFromDatabase(user: User): String? {
        DataSource.getGameConnection().loadDataFromDatabase("SELECT $verificationCodesDatabaseVerificationCode FROM $verificationCodesDatabaseTable WHERE $verificationCodesDatabaseDiscordId=${user.id}") {
            return getString(1)
        }
        return null
    }

    internal fun getPidnFromDatabase(user: User): Int? {
        DataSource.getGameConnection().loadDataFromDatabase("SELECT $verificationCodesDatabasePlayerId FROM $verificationCodesDatabaseTable WHERE $verificationCodesDatabaseDiscordId=${user.id}") {
            val pidn = getString(1)
            return pidn.toInt()
        }
        return null
    }

    internal fun canBeRewarded(pidn: Int, cooldownTime: Int): Boolean {
        DataSource.getGameConnection().loadDataFromDatabase("SELECT $rewardTimesDatabaseLastRewardedTime FROM $rewardTimesDatabaseTable WHERE $rewardTimesDatabasePlayerId=$pidn") {
            val lastRewarded = getLong(1)
            return System.currentTimeMillis().compareHours(lastRewarded) >= cooldownTime
        }
        return false
    }

    private fun getDiscordUserFromDatabase(pidn: Int): String {
        DataSource.getGameConnection().loadDataFromDatabase("SELECT $verificationCodesDatabaseDiscordId FROM $verificationCodesDatabaseTable WHERE $verificationCodesDatabasePlayerId=$pidn") {
            return getString(1)
        }
        return ""
    }

    private fun saveTime(pidn: Int, time: Long) {
        DataSource.getGameConnection().saveDataToDatabase("UPDATE $rewardTimesDatabaseTable SET $rewardTimesDatabaseLastRewardedTime=? WHERE $rewardTimesDatabasePlayerId=$pidn") {
            setLong(1, time)
        }
    }

    private fun databaseHasVerificationCode(code: String): Boolean {
        DataSource.getGameConnection().loadDataFromDatabase("SELECT $verificationCodesDatabaseVerificationCode FROM $verificationCodesDatabaseTable") {
            val verificationCode = getString(1)
            if (verificationCode == code) {
                return true
            }
        }
        return false
    }

    private fun verificationCodeVerified(code: String): Boolean {
        DataSource.getGameConnection().loadDataFromDatabase("SELECT $verificationCodesDatabaseVerified FROM $verificationCodesDatabaseTable") {
            val verificationCode = getString(1)
            if (verificationCode == code) {
                return getBoolean(1)
            }
        }
        return false
    }
}