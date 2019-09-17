package bot.commands

import bot.AccountLinkage
import de.btobastian.javacord.entities.User
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import org.gielinor.database.DataSource
import org.gielinor.database.loadVariableFromDatabase
import org.gielinor.game.node.entity.player.info.donor.DonorStatus
import org.gielinor.game.node.item.Item
import org.gielinor.game.world.repository.Repository

class RewardCommand : CommandExecutor {

    companion object {
        val REWARDS = arrayOf(Item(4151), Item(861))
    }

    private val errorMessage = "Something appears to be wrong. \uD83E\uDD14 Report this to an admin!"

    @Command(aliases = arrayOf("::reward"), description = "Rewards a verified account in-game every 24 hours.")
    fun onRewardCommand(user: User): String {
        if (AccountLinkage.userVerified(user)) {
            val pidn = AccountLinkage.getPidnFromDatabase(user)
            pidn ?: return errorMessage
            val rights = DataSource.getGameConnection().loadVariableFromDatabase("SELECT rights FROM player_detail WHERE pidn=$pidn") as Int
            var cooldownHours = DonorStatus.forMemberId(rights)?.rewardCommandCooldown
            if (cooldownHours == null) {
                cooldownHours = 24
            }
            if (AccountLinkage.canBeRewarded(pidn, cooldownHours)) {
                if (Repository.getPlayerByPidn(pidn) != null) {
                    Repository.getPlayerByPidn(pidn).actionSender.sendMessage("Please log out before claiming a reward.")
                    return "Please log out in-game to be rewarded."
                }
                val initialBankSlot = DataSource.getGameConnection().loadVariableFromDatabase("SELECT MAX(bank_slot) FROM player_bank WHERE pidn=$pidn") {
                    val bankSlot = getInt(1)
                    bankSlot + 1
                }
                initialBankSlot ?: return errorMessage
                AccountLinkage.reward(pidn, initialBankSlot)
                return "Your reward is in your bank. Thank you!"
            } else {
                return "You can only be rewarded once every 24 hours!"
            }
        } else {
            return "You don't have an account verified! <:PogChamp:307458204340584460>"
        }
    }
}