package org.gielinor.content.statistics.boards

import org.gielinor.game.component.Component
import org.gielinor.game.node.entity.player.Player
import org.slf4j.LoggerFactory

val log = LoggerFactory.getLogger(StatisticBoard::class.java)!!

abstract class StatisticBoard {
    val interfaceId = 6308
    private val titleInterfaceStringId = 6400
    private val subtitleInterfaceStringId = 6399

    val statisticList = arrayListOf<String>()

    abstract val databaseTable: String
    abstract fun init(player: Player)

    fun makeInterface(player: Player, preliminarySets: () -> Unit) {
        writeToInterface { _, lineId ->
            player.actionSender.sendString("", lineId)
        }
        preliminarySets()
        writeToInterface { index, lineId ->
            if (index < statisticList.size) {
                player.actionSender.sendString(lineId, statisticList[index])
            }
        }
        player.interfaceState.open(Component(interfaceId))
    }

    fun setTitle(player: Player, title: String) {
        player.actionSender.sendString(titleInterfaceStringId, title)
    }

    fun setSubtitle(player: Player, subtitle: String) {
        player.actionSender.sendString(subtitleInterfaceStringId, subtitle)
    }

    private inline fun writeToInterface(crossinline writeText: (index: Int, lineId: Int) -> Unit) {
        var lineId = 6402
        for (i in 0..49) {
            if (lineId == 6412) {
                lineId = 8578
            }
            writeText(i, lineId)
            lineId++
        }
    }
}

object StatisticBoards {
    val statisticBoards = hashMapOf<String, StatisticBoard>()

    fun load() {
        log.info("Setting up statistic boards.")
        statisticBoards.put("latest_kills", LatestKillsBoard)
        statisticBoards.put("latest_duels", LatestDuelsBoard)
        statisticBoards.put("highest_kill_counts", HighestKillCountBoard)
        statisticBoards.put("highest_stakes", HighestStakesBoard)
        log.info("Finished setting up statistic boards.")
    }

}
