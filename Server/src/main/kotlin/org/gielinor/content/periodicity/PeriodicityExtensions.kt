package org.gielinor.content.periodicity

fun ArrayList<PeriodicityPulse>.init() {
    forEach { it.init() }
}

fun ArrayList<PeriodicityPulse>.check() {
    forEach { it.check() }
}

fun ArrayList<PeriodicityPulse>.pulse() {
    forEach { it.pulse() }
}

fun ArrayList<PeriodicityPulse>.save() {
    forEach { it.save() }
}