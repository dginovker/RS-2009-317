package org.gielinor.content.statistics.boards

import org.gielinor.database.DataSource
import org.gielinor.database.loadDataFromDatabase
import org.gielinor.database.saveDataToDatabase
import org.gielinor.game.node.entity.player.Player
import org.gielinor.rs2.config.Constants
import org.gielinor.util.extensions.highlight
import org.gielinor.utilities.string.TextUtils

object HighestStakesBoard : StatisticBoard() {
    override val databaseTable = "highest_stakes_board"
    private val databaseWinner = "winner"
    private val databaseLoser = "loser"
    private val databaseStakeAmount = "stake_amount"

    init {
        loadData()
    }

    private fun loadData() {
        statisticList.clear()
        DataSource.getGameConnection().loadDataFromDatabase("SELECT $databaseWinner, $databaseLoser, $databaseStakeAmount FROM $databaseTable ORDER BY $databaseStakeAmount DESC LIMIT 50") {
            val winner = getString(databaseWinner)
            val loser = getString(databaseLoser)
            val stakeAmount = getLong(databaseStakeAmount)
            statisticList.add(formattedEntry(winner, loser, stakeAmount))
        }
    }

    fun addStatistic(winner: String, loser: String, stakeAmount: Long) {
        DataSource.getGameConnection().saveDataToDatabase("INSERT INTO $databaseTable ($databaseWinner, $databaseLoser, $databaseStakeAmount) VALUES (?, ?, ?)") {
            setString(1, winner)
            setString(2, loser)
            setLong(3, stakeAmount)
        }
    }

    private fun formattedEntry(winner: String, loser: String, stakeAmount: Long): String {
        val winner1 = TextUtils.formatDisplayName(winner)
        val loser1 = TextUtils.formatDisplayName(loser)
        val stakeAmount1 = TextUtils.format(stakeAmount)
        return "${winner1.highlight()} beat ${loser1.highlight()} to win ${"$stakeAmount1 GP".highlight()}!"
    }

    override fun init(player: Player) {
        makeInterface(player) {
            loadData()
            setTitle(player, "Highest Stakes Board")
            setSubtitle(player, "Top fifty stakes in ${Constants.SERVER_NAME}:")
        }
    }
}
