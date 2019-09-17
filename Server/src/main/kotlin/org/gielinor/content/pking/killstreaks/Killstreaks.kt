package org.gielinor.content.pking.killstreaks

import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.item.GroundItemManager
import org.gielinor.game.node.item.Item
import org.gielinor.game.world.World
import org.gielinor.utilities.misc.RandomUtil
import java.util.*

enum class Reward(val kills: IntRange, val bountyPoints: Int, val extraMessage: String = "", vararg val bountyItems: Item) {
    FIVE(5..9, 10),
    TEN(10..14, 20),
    FIFTEEN(15..19, 35),
    TWENTY(20..24, 50),
    TWENTY_FIVE(25..34, 80),
    THIRTY_FIVE(35..44, 100, " and a piece of Dragon equipment"),
    FORTY_FIVE(45..49, 125, " and a piece of Barrows equipment", *Item.getUnnotedItemsInRange(4708, 4738), *Item.getUnnotedItemsInRange(4745, 4759)),
    FIFTY(50..Int.MAX_VALUE, 150, " and a GWD item", *Item.getUnnotedItemsInRange(11710, 11714), Item(11690), *Item.getUnnotedItemsInRange(11702, 11708));

    companion object {
        val VALUES: MutableList<Array<Reward>> = Collections.unmodifiableList(Arrays.asList(values()))
    }
}

private val PREVIOUS_KILLS_MAC_ADDRESSES = arrayListOf<String>()
private val MAC_ADDRESS_CAP = 8

private fun ArrayList<String>.remainderTillReset(): Int {
    return size % MAC_ADDRESS_CAP
}

private fun MutableList<Array<Reward>>.getReward(kills: Int): Reward? {
    forEach {
        it.forEach {
            if (it.kills.contains(kills)) {
                return it
            }
        }
    }
    return null
}

private fun Array<out Item>.getRandom(): Item {
    return this[RandomUtil.random(size)]
}

fun check(killer: Player, victim: Player) {
    val macAddress = victim.details.macAddress

    if (PREVIOUS_KILLS_MAC_ADDRESSES.contains(macAddress)) {
        killer.actionSender.sendMessage("To be rewarded for killing ${killer.name}s, you must kill ${PREVIOUS_KILLS_MAC_ADDRESSES.remainderTillReset()} more player(s)!")
        return
    }

    PREVIOUS_KILLS_MAC_ADDRESSES.add(macAddress)
    if (PREVIOUS_KILLS_MAC_ADDRESSES.remainderTillReset() == 0) {
        PREVIOUS_KILLS_MAC_ADDRESSES.clear()
    }

    increment(killer)
}

private fun increment(killer: Player) {
    killer.savedData.globalData.killstreak++
    val killstreak = killer.savedData.globalData.killstreak
    val reward = Reward.VALUES.getReward(killstreak)
    if (reward != null && killstreak % 5 == 0) {
        World.sendWorldMessage("News: ${killer.name} has achieved a killstreak of $killstreak!")
        World.sendWorldMessage("News: A bounty of ${reward.bountyPoints} PK Points${reward.extraMessage} has been placed")
        World.sendWorldMessage("on ${killer.name}'s head!")
    }
    killer.actionSender.sendMessage("You now have a killstreak of $killstreak!")
}

fun death(victim: Player, killer: Player) {
    val killstreak = victim.savedData.globalData.killstreak
    val reward = Reward.VALUES.getReward(killstreak)
    if (reward != null) {
        World.sendWorldMessage("News: ${killer.name} has been rewarded ${reward.bountyPoints} PK Points${reward.extraMessage}")
        World.sendWorldMessage("for breaking ${victim.name}'s killstreak of $killstreak!")
        killer.savedData.globalData.pkPoints += reward.bountyPoints
        if (reward.bountyItems.isNotEmpty()) {
            val rewardItem = reward.bountyItems.getRandom()
            if (killer.inventory.add(rewardItem)) {
                killer.actionSender.sendMessage("Your reward has been added to your inventory!")
            } else if (killer.bank.add(rewardItem)) {
                killer.actionSender.sendMessage("Your reward has been added to your bank!")
            } else {
                killer.actionSender.sendMessage("Your reward has been dropped on the ground at your current location!")
                GroundItemManager.create(rewardItem, killer.location)
            }
        }
    }
    victim.savedData.globalData.resetKillstreak()
}

