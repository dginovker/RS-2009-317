package org.gielinor.content.statistics.boards

import org.gielinor.database.DataSource
import org.gielinor.database.loadDataFromDatabase
import org.gielinor.database.saveDataToDatabase
import org.gielinor.game.node.entity.player.Player
import org.gielinor.rs2.config.Constants
import org.gielinor.util.extensions.highlight
import org.gielinor.utilities.string.TextUtils

object LatestDuelsBoard : StatisticBoard() {

    override val databaseTable = "duel_arena"
    private val databaseWinner = "winner"
    private val databaseLoser = "loser"
    private val databaseWinnerCombatLevel = "winner_combat_level"
    private val databaseLoserCombatLevel = "loser_combat_level"

    init {
        if (statisticList.size == 0) {
            DataSource.getGameConnection().loadDataFromDatabase("SELECT * FROM $databaseTable ORDER BY dateline DESC LIMIT 50") {
                val winner = getString(databaseWinner)
                val defeated = getString(databaseLoser)
                val winnerCombatLevel = getInt(databaseWinnerCombatLevel)
                val defeatedCombatLevel = getInt(databaseLoserCombatLevel)
                statisticList.add(formattedEntry(winner, defeated, winnerCombatLevel, defeatedCombatLevel))
            }
        }
    }

    fun addStatistic(winner: String, loser: String, winnerCombatLevel: Int, loserCombatLevel: Int) {
        statisticList.add(formattedEntry(winner, loser, winnerCombatLevel, loserCombatLevel))
        DataSource.getGameConnection().saveDataToDatabase("INSERT INTO $databaseTable ($databaseWinner, $databaseLoser, $databaseWinnerCombatLevel, $databaseLoserCombatLevel) VALUES (?, ?, ?, ?)") {
            setString(1, winner)
            setString(2, loser)
            setInt(3, winnerCombatLevel)
            setInt(4, loserCombatLevel)
        }
    }

    private fun formattedEntry(winner: String, loser: String, winnerCombatLevel: Int, loserCombatLevel: Int): String {
        val winner1 = TextUtils.formatDisplayName(winner)
        val loser1 = TextUtils.formatDisplayName(loser)
        return "${winner1.highlight()} ($winnerCombatLevel) beat ${loser1.highlight()} ($loserCombatLevel)."
    }

    override fun init(player: Player) {
        makeInterface(player) {
            setTitle(player, "Latest Duels Board")
            setSubtitle(player, "Last fifty duels in ${Constants.SERVER_NAME}:")
        }
    }
}
