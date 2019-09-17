package org.gielinor.database

object PlayerLists {

    const val databaseTable = "player_lists"
    private const val databasePlayersInWild = "players_in_wild"
    private const val databaseMostPlayersOnline = "most_players_online"
    const val databasePlayersOnlineToday = "players_online_today"

    var playersInWilderness = 0
    var playersOnlineToday = 0

    fun addPlayerToWilderness() {
        playersInWilderness++
        updateWildernessPlayerCount()
    }

    fun removePlayerFromWilderness() {
        playersInWilderness--
        updateWildernessPlayerCount()
    }

    @JvmStatic
    fun addPlayerOnlineToday() {
        playersOnlineToday++
        updatePlayersOnlineToday()
        updateMostOnline()
    }

    @JvmStatic
    fun loadPlayersOnlineToday() {
        DataSource.getGameConnection().loadDataFromDatabase("SELECT $databasePlayersOnlineToday FROM $databaseTable") {
            playersOnlineToday = getInt(1)
        }
    }

    /**
     * Compares the players online today to the previous record of most players online and sets whichever one is
     * greatest.
     */
    private fun updateMostOnline() {
        DataSource.getGameConnection().performDatabaseFunction("UPDATE $databaseTable SET $databaseMostPlayersOnline=GREATEST($databaseMostPlayersOnline,$databasePlayersOnlineToday)")
    }

    private fun updateWildernessPlayerCount() {
        DataSource.getGameConnection().performDatabaseFunction("UPDATE $databaseTable SET $databasePlayersInWild=$playersInWilderness")
    }

    private fun updatePlayersOnlineToday() {
        DataSource.getGameConnection().performDatabaseFunction("UPDATE $databaseTable SET $databasePlayersOnlineToday=$playersOnlineToday")
    }
}