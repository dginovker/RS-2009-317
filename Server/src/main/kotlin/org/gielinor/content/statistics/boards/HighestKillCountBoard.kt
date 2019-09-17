package org.gielinor.content.statistics.boards

import org.gielinor.database.DataSource
import org.gielinor.database.loadDataFromDatabase
import org.gielinor.database.saveDataToDatabase
import org.gielinor.game.node.entity.player.Player
import org.gielinor.rs2.config.Constants
import org.gielinor.util.extensions.highlight
import org.gielinor.utilities.string.TextUtils

object HighestKillCountBoard : StatisticBoard() {

    override val databaseTable = "highest_kill_count_board"
    private val databaseUsername = "username"
    private val databaseKillCount = "kill_count"

    init {
        loadData()
    }

    private fun loadData() {
        statisticList.clear()
        DataSource.getGameConnection().loadDataFromDatabase("SELECT $databaseUsername, $databaseKillCount FROM $databaseTable ORDER BY $databaseKillCount DESC LIMIT 50") {
            val username = getString(databaseUsername)
            val killCount = getInt(databaseKillCount)
            statisticList.add(formattedEntry(username, killCount))
        }
    }

    override fun init(player: Player) {
        makeInterface(player) {
            loadData()
            setTitle(player, "Highest Killcounts Board")
            setSubtitle(player, "Top fifty killcounts in ${Constants.SERVER_NAME}:")
        }
    }

    fun addStatistic(username: String, killCount: Int) {
        DataSource.getGameConnection().saveDataToDatabase("INSERT INTO $databaseTable ($databaseUsername, $databaseKillCount) VALUES (?, ?) " +
            "ON DUPLICATE KEY UPDATE $databaseKillCount=$databaseKillCount = GREATEST($databaseKillCount, VALUES($databaseKillCount))") {
            setString(1, username)
            setInt(2, killCount)
        }
    }

    private fun formattedEntry(username: String, killCount: Int): String {
        val username1 = TextUtils.formatDisplayName(username)
        return "${username1.highlight()} got a killcount of ${killCount.toString().highlight()}!"
    }

}
