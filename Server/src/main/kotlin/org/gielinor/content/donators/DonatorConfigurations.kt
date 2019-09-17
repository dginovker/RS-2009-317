package org.gielinor.content.donators

import org.gielinor.game.node.entity.player.Player
import org.gielinor.utilities.misc.RandomUtil

object DonatorConfigurations {

    @JvmStatic
    fun loginConfigurations(player: Player) {
        setPoisonImmunity(player)
    }

    fun enterWildernessConfigurations(player: Player) {
        resetPoisonImmunity(player)
    }

    fun leaveWildernessConfigurations(player: Player) {
        setPoisonImmunity(player)
    }

    private fun setPoisonImmunity(player: Player) {
        if (player.donorManager.donorStatus.poisonImmunity) {
            player.setAttribute("poison:immunity", Long.MAX_VALUE)
        }
    }

    private fun resetPoisonImmunity(player: Player) {
        if (player.donorManager.donorStatus.poisonImmunity) {
            player.setAttribute("poison:immunity", -1)
        }
    }

    @JvmStatic
    fun handleDoubleResources(player: Player, doubleResourcesAction: () -> Unit): Boolean {
        val randomNumber = RandomUtil.random(101)
        if (randomNumber <= player.donorManager.donorStatus.doubleResourceChance && player.donorManager.donorStatus.doubleResourceChance != -1) {
            doubleResourcesAction()
            player.actionSender.sendMessage("Due to your membership status, you get double the resources!", 1)
            return true
        }
        return false
    }

    @JvmStatic
    fun getHealingMultiplier(player: Player): Double {
        return when {
            player.getAttribute<Any>("combat-attacker") is Player -> 1.0
            else -> player.donorManager.donorStatus.healingMultiplier
        }
    }
}
