package org.gielinor.content.periodicity.util

import org.gielinor.content.periodicity.PeriodicityPulseManager
import org.gielinor.content.periodicity.daily.impl.MinigameSpotlight
import org.gielinor.game.node.entity.player.Player

/**
 * Is there a better way to handle this?
 */
fun sendMessages(player: Player) {
    MinigameSpotlight.sendMessage(player, false)
    PeriodicityPulseManager.STAKING_SATURDAY_INSTANCE.sendMessage(player)
}