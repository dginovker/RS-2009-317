package org.gielinor.content.periodicity.daily

import org.gielinor.content.periodicity.PeriodicityPulse

/**
 * Represents something that happens every day (i.e. minigame spotlight)
 * @author Arham 4
 */
abstract class DailyPulse : PeriodicityPulse() {
    override fun check() {
        pulse()
    }
}