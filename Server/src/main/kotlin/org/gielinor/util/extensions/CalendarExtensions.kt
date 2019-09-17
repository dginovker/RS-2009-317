package org.gielinor.util.extensions

import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Compares two days with the milliseconds.
 */
fun Calendar.compareDays(instanceMilli: Long, comparativeMilli: Long): Int {
    timeInMillis = instanceMilli
    val instanceDay = get(Calendar.DAY_OF_YEAR)

    val comparative = Calendar.getInstance()
    comparative.timeInMillis = comparativeMilli
    val comparativeDay = comparative.get(Calendar.DAY_OF_YEAR)

    return instanceDay - comparativeDay
}

fun Long.compareHours(comparativeMilli: Long): Long {
    return TimeUnit.MILLISECONDS.toHours(this - comparativeMilli)
}

fun Long.compareDays(comparativeMilli: Long): Long {
    return TimeUnit.MILLISECONDS.toDays(this - comparativeMilli)
}