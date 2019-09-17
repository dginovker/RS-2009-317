package org.gielinor.util.extensions

import org.gielinor.utilities.misc.RandomUtil

fun <T> List<T>.random(): T {
    return this[RandomUtil.random(this.size)]
}

fun <T> ArrayList<T>.addMany(arg: T, amount: Int) {
    repeat(times = amount) {
        this.add(arg)
    }
}

inline fun <reified T> toArray(list: List<T>): Array<T> {
    return list.toTypedArray()
}
