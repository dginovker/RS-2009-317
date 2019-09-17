package org.gielinor.content.periodicity.daily.impl

import org.gielinor.content.periodicity.daily.DailyPulse
import org.gielinor.game.world.repository.Repository

class GiveLoyaltyPoints : DailyPulse() {
    override fun pulse() {
        Repository.getPlayers().forEach {
            it.loyaltyPointMonitor.sendValues()
        }
    }
}