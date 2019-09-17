package org.gielinor.content.loyaltytitle

import org.gielinor.database.PlayerLists
import org.gielinor.game.node.entity.player.Player
import org.gielinor.util.extensions.color
import org.gielinor.util.extensions.compareDays
import org.gielinor.util.extensions.toHex
import java.awt.Color
import java.util.*

/**
 * Represents the Gielinor point monitor.

 * @author Arham 4
 * *
 * @author [Gielinor Logan G.](https://Gielinor.org)
 * *
 *
 * */
class LoyaltyPointMonitor(private val player: Player) {

    private fun check(): Boolean {
        val daysDifference = Calendar.getInstance().compareDays(System.currentTimeMillis(), player.details.lastLogin)

        when (daysDifference) {
            0 -> return false
            1 -> return true
            else -> resetStreak(true)
        }
        return false
    }

    private fun resetStreak(failureToLogin: Boolean) {
        player.savedData.globalData.resetStreak()
        if (failureToLogin) {
            player.actionSender.sendMessage("You failed to log in sequentially, so your streak has been reset!".color(Color.RED.toHex()))
        } else {
            player.actionSender.sendMessage("Your streak has been reset back to 0 now.")
        }
    }

    private fun increasePoints(points: Int) {
        val totalPoints = player.savedData.globalData.loyaltyPoints
        player.savedData.globalData.increaseLoyaltyPoints(points)
        player.actionSender.sendMessage("You have been rewarded $points Loyalty points! You now have $totalPoints Loyalty points.".color(Color.GREEN.toHex()))
    }

    /**
     * Sends the values to their respective variables with checks.
     */
    fun sendValues() {
        if (check()) {
            player.savedData.globalData.incrementStreak()
            val streak = player.savedData.globalData.loginStreak
            sendStreakMessage()
            PlayerLists.addPlayerOnlineToday()
            when (streak) {
                2, 3 -> increasePoints(3)
                in 4..6 -> increasePoints(5)
                7 -> {
                    increasePoints(10)
                    resetStreak(false)
                }
            }
        }
    }

    private fun sendStreakMessage() {
        val streak = player.savedData.globalData.loginStreak
        if (streak == 1) {
            player.actionSender.sendMessage("You have begun your login streak! Log back in every day to earn Loyalty points!".color(Color.GREEN.toHex()))
        } else {
            player.actionSender.sendMessage("You have logged in sequentially for $streak days!".color(Color.GREEN.toHex()))
        }
    }

}
