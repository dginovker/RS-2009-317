package org.gielinor.content.periodicity

import org.gielinor.content.periodicity.daily.impl.GiveLoyaltyPoints
import org.gielinor.content.periodicity.daily.impl.MinigameSpotlight
import org.gielinor.content.periodicity.daily.impl.ResetWebsitePlayers
import org.gielinor.content.periodicity.weekly.impl.StakingSaturday
import org.gielinor.game.world.World
import org.slf4j.LoggerFactory

/**
 * Handles everything related the pulses, such as the ArrayLists and the pulse and all that good stuff
 */
class PeriodicityPulseManager {

    companion object {
        val log = LoggerFactory.getLogger(PeriodicityTable::class.java)!!

        val PULSES = arrayListOf<PeriodicityPulse>()

        private val DAILY_CHECK_PULSE = DailyCheckPulse()
        val STAKING_SATURDAY_INSTANCE = StakingSaturday()

        private fun populateLists() {
            PULSES.add(MinigameSpotlight())
            PULSES.add(STAKING_SATURDAY_INSTANCE)
            PULSES.add(ResetWebsitePlayers())
            PULSES.add(GiveLoyaltyPoints())
        }

        fun init() {
            log.info("Setting up periodicity pulse manager.")
            populateLists()
            PULSES.pulse()
            PULSES.init()
            World.submit(DAILY_CHECK_PULSE)
            log.info("Finished setting up periodicity pulse manager.")
        }
    }

}
