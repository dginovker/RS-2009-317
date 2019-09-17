package org.gielinor.content.statistics.boards

import org.gielinor.cache.def.impl.ItemDefinition
import org.gielinor.database.DataSource
import org.gielinor.database.loadDataFromDatabase
import org.gielinor.database.saveDataToDatabase
import org.gielinor.game.node.entity.player.Player
import org.gielinor.rs2.config.Constants
import org.gielinor.util.extensions.highlight
import org.gielinor.util.extensions.withArticle
import org.gielinor.utilities.string.TextUtils
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

object LatestKillsBoard : StatisticBoard() {

    override val databaseTable = "latest_kills_board"
    private val databaseKillDate = "kill_date"
    private val databaseKillerName = "killer_name"
    private val databaseVictimName = "victim_name"
    private val databaseWeaponId = "weapon_id"

    init {
        if (statisticList.size == 0) {
            DataSource.getGameConnection().loadDataFromDatabase("SELECT kill_date, killer_name, victim_name, weapon_id FROM $databaseTable ORDER BY $databaseKillDate DESC LIMIT 50") {
                val killDate = getDate(databaseKillDate)
                val killerName = getString(databaseKillerName)
                val victimName = getString(databaseVictimName)
                val weaponId = getInt(databaseWeaponId)
                statisticList.add(formattedEntry(killDate, killerName, victimName, weaponId))
            }
        }
    }

    override fun init(player: Player) {
        makeInterface(player) {
            setTitle(player, "Latest Kills Board")
            setSubtitle(player, "Last fifty PvP kills in ${Constants.SERVER_NAME}:")
        }
    }

    fun addStatistic(killerName: String, victimName: String, weaponId: Int) {
        statisticList.add(formattedEntry(Date(System.currentTimeMillis()), killerName, victimName, weaponId))
        DataSource.getGameConnection().saveDataToDatabase("INSERT INTO $databaseTable ($databaseKillerName, $databaseVictimName, $databaseWeaponId) VALUES (?, ?, ?)") {
            setString(1, killerName)
            setString(2, victimName)
            setInt(3, weaponId)
        }
    }

    private fun String?.getWeapon(): String {
        if (this == null) return "${"their bare hands".highlight()}!"
        return "${this.highlight().withArticle()}."
    }

    private fun formattedEntry(killDate: Date, killerName: String, victimName: String, weaponId: Int): String {
        val df = SimpleDateFormat("MMM. dd", Locale.US)
        val killDate1 = df.format(killDate)
        val killerName1 = TextUtils.formatDisplayName(killerName)
        val victimName1 = TextUtils.formatDisplayName(victimName)
        return "On ${killDate1.highlight()}, ${killerName1.highlight()} killed ${victimName1.highlight()} with " +
            ItemDefinition.forId(weaponId).name.getWeapon().highlight()
    }

}
