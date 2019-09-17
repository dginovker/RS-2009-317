package org.gielinor.content.periodicity.weekly.impl

import org.gielinor.content.periodicity.weekly.WeeklyPulse
import org.gielinor.game.node.entity.npc.drop.DropFrequency
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.item.ChanceItem
import org.gielinor.game.world.repository.Repository
import org.gielinor.utilities.misc.RandomUtil
import plugin.activity.duelarena.DuelSession
import java.util.*

class StakingSaturday : WeeklyPulse() {
    override fun pulse() {
        Repository.getPlayers().forEach { sendMessage(it) }
    }

    override val DAY_OF_WEEK: Int = Calendar.SATURDAY

    private val MESSAGE = "<col=ff0000>It's Staking Saturday!"
    private val REWARDS = arrayOf(
        ChanceItem(4708, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4710, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4712, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4714, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4716, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4718, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4720, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4722, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4724, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4726, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4728, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4730, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4732, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4734, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4736, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4738, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4745, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4747, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4749, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4751, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4753, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4755, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4757, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4759, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(11710, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(11712, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(11714, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(11690, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(11708, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(11704, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(11706, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(11702, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(10330, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(10332, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(10334, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(10336, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(10338, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(10340, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(10342, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(10344, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(10346, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(10348, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(10350, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(10352, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(11335, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(3140, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(7158, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE),
        ChanceItem(4151, 1, 1, 1000, 0.0, DropFrequency.VERY_RARE)
    )

    fun giveReward(player: Player) {
        if (active) {
            if (player.inventory.freeSlots() >= 1) {
                var reward: ChanceItem? = null
                val list = Arrays.asList<ChanceItem>(*REWARDS)
                Collections.shuffle(list, Random())
                val duelSession = DuelSession.getExtension(player)
                val opponentSession = DuelSession.getExtension(duelSession.opponent)
                val combinedValue = duelSession.spoilsValue + opponentSession.spoilsValue
                for (item in list) {
                    val rarity = Int.MAX_VALUE
                    val randomCombinedValue = RandomUtil.random(combinedValue.toInt())
                    val randomRarity = RandomUtil.random(rarity)
                    if (randomCombinedValue >= randomRarity) {
                        reward = item
                        break
                    }
                }
                player.inventory.add(reward)
            }
        }
    }

    fun sendMessage(player: Player) {
        if (active) {
            player.actionSender.sendMessage(MESSAGE)
        }
    }
}
