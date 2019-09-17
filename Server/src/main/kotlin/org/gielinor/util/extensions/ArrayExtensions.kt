package org.gielinor.util.extensions

import org.gielinor.utilities.misc.RandomUtil

fun <T> Array<T>.random(): T {
    return this[RandomUtil.random(this.size)]
}

fun IntArray.plus(ints: IntRange): IntArray {
    var intArray: IntArray = this
    ints.forEach { int -> intArray = plus(int) }
    return intArray
}