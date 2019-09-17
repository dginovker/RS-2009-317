package plugin.dialogue

import org.gielinor.content.statistics.boards.StatisticBoards
import org.gielinor.game.content.dialogue.DialoguePlugin
import org.gielinor.game.content.dialogue.OptionSelect
import org.gielinor.game.node.entity.npc.NPC
import org.gielinor.game.node.entity.player.Player

/**
 * The NPC with the statistic boards
 *
 * @author Arham 4
 */
class StatisticsNpcDialogue : DialoguePlugin {

    constructor(player: Player) : super(player)

    constructor()

    private val NPC_ID = 23562

    override fun newInstance(player: Player): DialoguePlugin {
        return StatisticsNpcDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Hey. Can I see a statistic feed?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, optionSelect: OptionSelect?): Boolean {
        when (stage) {
            0 -> {
                npc("Sure, which one?")
                stage = 1
            }
            1 -> {
                options("Latest Kills", "Highest Killcounts", "Lastest Duels", "Highest Stakes")
                stage = 2
            }
            2 -> {
                end()
                if (optionSelect == OptionSelect.FOUR_OPTION_ONE) StatisticBoards.statisticBoards["latest_kills"]?.init(player)
                else if (optionSelect == OptionSelect.FOUR_OPTION_TWO) StatisticBoards.statisticBoards["highest_kill_counts"]?.init(player)
                else if (optionSelect == OptionSelect.FOUR_OPTION_THREE) StatisticBoards.statisticBoards["latest_duels"]?.init(player)
                else if (optionSelect == OptionSelect.FOUR_OPTION_FOUR) StatisticBoards.statisticBoards["highest_stakes"]?.init(player)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPC_ID)
    }
}