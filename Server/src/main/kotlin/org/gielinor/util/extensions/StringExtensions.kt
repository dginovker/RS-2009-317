package org.gielinor.util.extensions

fun String.withArticle(): String {
    val first = this.split("")[0].toLowerCase()
    val vowel = first == "a" || first == "e" || first == "i" || first == "o" || first == "u"
    val article = if (vowel) "an" else "a"
    return "$article $this"
}

fun String.color(color: String): String = "<col=$color>$this</col>"
fun String.highlight(): String = this.color("FFDF00")

fun String.equalsIgnoreCase(other: String): Boolean = this.toLowerCase() == other
fun String.containsIgnoreCase(other: String): Boolean = this.toLowerCase().contains(other)