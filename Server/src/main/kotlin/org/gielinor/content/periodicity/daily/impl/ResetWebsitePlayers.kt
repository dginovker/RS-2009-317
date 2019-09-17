package org.gielinor.content.periodicity.daily.impl

import org.gielinor.content.periodicity.daily.DailyPulse
import org.gielinor.database.DataSource
import org.gielinor.database.PlayerLists
import org.gielinor.database.performDatabaseFunction

/**
 * Resets players_logged_in_today in player_lists database table every day to however many are on at that current time.
 */
class ResetWebsitePlayers : DailyPulse() {
    override fun pulse() {
        PlayerLists.playersOnlineToday = 0
        DataSource.getGameConnection().performDatabaseFunction("UPDATE ${PlayerLists.databaseTable} SET ${PlayerLists.databasePlayersOnlineToday}=${PlayerLists.playersOnlineToday}")
    }
}