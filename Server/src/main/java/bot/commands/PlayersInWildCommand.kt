package bot.commands

import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import org.gielinor.database.PlayerLists

/**
 * Created by Corey on 14/06/2017.
 * Converted to Kotlin :PogChamp: by Arham 4 on 03/09/2017
 */
class PlayersInWildCommand : CommandExecutor {

    @Command(aliases = arrayOf("::wild", "::wildyplayers"), description = "How many players are in the Wilderness")
    fun onPlayersInWildCommand(): String {
        val playersInWilderness = PlayerLists.playersInWilderness
        return "There ${if (isSingular(playersInWilderness)) "is" else "are"} currently ${PlayerLists.playersInWilderness} Player${if (!isSingular(playersInWilderness)) "s" else ""} in the Wilderness."
    }

    private fun isSingular(playersInWilderness: Int): Boolean {
        return playersInWilderness == 1
    }
}