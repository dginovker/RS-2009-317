package org.gielinor.content.periodicity.daily.impl

import org.gielinor.content.periodicity.PeriodicityTable
import org.gielinor.content.periodicity.daily.DailyPulse

import org.gielinor.game.content.activity.ActivityManager
import org.gielinor.game.content.activity.ActivityPlugin
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.world.repository.Repository

const val SPOTLIGHT_COLUMN = "minigame_spotlight"

/**
 * Minigame spotlight
 * @author Arham 4
 */
class MinigameSpotlight : DailyPulse() {
    private var cachedSpotlight: String? = null

    override fun pulse() {
        currentSpotlight = ActivityManager.getRandomActivity()
        Repository.getPlayers().forEach { sendMessage(it, true) }
    }

    override fun save() {
        // Spotlight has not changed.
        if (cachedSpotlight == currentSpotlight?.name) return
        // Update cached spotlight and transmit to database.
        cachedSpotlight = currentSpotlight?.name
        saveData(SPOTLIGHT_COLUMN, PeriodicityTable.TABLE_NAME, cachedSpotlight)
    }

    override fun init() {
        val spotlightFromDatabase = loadData(SPOTLIGHT_COLUMN, PeriodicityTable.TABLE_NAME) as String?
        val activityPlugin = ActivityManager.getActivity(spotlightFromDatabase)
        spotlightFromDatabase.run { activityPlugin.run { currentSpotlight = activityPlugin } }
        cachedSpotlight = spotlightFromDatabase
    }

    companion object {
        var currentSpotlight: ActivityPlugin? = ActivityManager.getRandomActivity()

        fun getExpMultiplier(activityName: String): Double {
            if (activityName == currentSpotlight?.name) {
                when (activityName) {
                    "pest control novice" -> return 2.0
                    "Duel arena" -> {/*Suggested no to do*/
                    } // return 1.005
                    "Barrows" -> return 0.95 // 0.95 because RandomUtil.random((int) rarity) need to be <= mod. Mod =
                // cap of 80 randomized. (int) rarity has a cap of 2048. Thus, to make 2048 randomized more likely
                // be 80 randomized, we'd need to decrease 2048 by 5%, 5% decimal is 0.05, 1.0 - 0.05 = 0.95
                    "fight caves", "fight pits" -> return 1.25
                //TODO warriors guild somehow someway
                //"warriors guild" -> return 2
                }
            }
            return 1.0
        }

        fun sendMessage(player: Player, new: Boolean) {
            var message = "<col=ff0000>"
            var message2 = ""
            val message3 = "is ${currentSpotlight?.name?.replace(" novice", "")}."
            if (new) {
                message += "A new minigame spotlight has been assigned! "
            }
            message += "The current minigame spotlight "
            if (new) {
                message2 += "<col=ff0000>$message3"
            } else {
                message += message3
            }
            player.actionSender.sendMessage(message)
            if (message2 != "") {
                player.actionSender.sendMessage(message2)
            }
        }
    }
}
