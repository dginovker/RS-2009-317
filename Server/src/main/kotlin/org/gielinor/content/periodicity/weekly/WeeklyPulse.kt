package org.gielinor.content.periodicity.weekly

import org.gielinor.content.periodicity.PeriodicityPulse
import java.util.*

/**
 * Represents something that happens weekly (i.e. staking saturday)
 * @author Arham 4
 */
abstract class WeeklyPulse : PeriodicityPulse() {

    override fun check() {
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == DAY_OF_WEEK) {
            active = true
            pulse()
        } else {
            active = false
        }
    }

    /**
     * To be assigned with Calendar.#day
     */
    abstract val DAY_OF_WEEK: Int
    @JvmField
    var active: Boolean = false
}
