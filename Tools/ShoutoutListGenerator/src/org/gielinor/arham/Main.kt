import java.io.File
import java.util.*

fun main(args: Array<String>) {
    println("[hr][/hr]")
    println("Shoutout to our ${randomAdjective()}!")
    println("[list]")
    File("H:/Mega/RSPS/Servers/Gielinor/PLAYERS IN DRAWING.md").useLines { lines ->
        lines.forEach { line ->
            if (line.startsWith("*")) {
                val end = line.indexOf("]")
                println("[*][user]${line.substring(3, end)}[/user]")
            }
        }
    }
    println("[/list]")
    println("Come be apart of this list and get shoutouts and a free chance to win sapphire, emerald, or ruby membership!")
    println("[code][noparse][img]https://vgy.me/1PRlAO.gif[/img][/noparse][/code]")
    println("[url=https://lyasin.me/?til]Click here to edit your signature with ours![/url] ;)")
}

fun randomAdjective(): String {
    val random = Random().nextInt(10)
    return when (random) {
        0,1 -> "amazing supporters"
        2,3 -> "awesome Gielinorians"
        4,5 -> "awesome supporters"
        6,7 -> "favorite Gielinorians"
        8 -> "Gielinorians"
        else -> "supporters"
    }
}