package org.gielinor.content.periodicity

import org.gielinor.rs2.pulse.Pulse
import org.gielinor.util.extensions.compareDays
import java.util.*

/**
 * Looped every minute, seeing if we need to run our pulses.
 * @author Arham 4
 */
class DailyCheckPulse : Pulse(100) {
    private var previousMillis: Long = 0
    override fun pulse(): Boolean {
        val dayDifference = Calendar.getInstance().compareDays(System.currentTimeMillis(), previousMillis)
        if (dayDifference >= 1) {
            PeriodicityPulseManager.PULSES.pulse()
        }
        PeriodicityPulseManager.PULSES.save()
        previousMillis = System.currentTimeMillis()
        return false
    }
}