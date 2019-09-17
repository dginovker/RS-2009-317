package org.gielinor.util

inline fun <T> labelledAs(label: String, block: (String) -> T): T {
    return block(label)
}

inline fun <T> labelledConditional(label: String, condition: Boolean, block: (String) -> T): T? {
    if (condition) return block(label)
    return null
}