package bot.commands;

import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import org.gielinor.utilities.misc.RandomUtil

class StackCommand : CommandExecutor {

    @Command(aliases = arrayOf("::stack"), description = "Returns a message faking a blitz and dds attack on an input. Code submission by: Jedzio")
    fun onStackCommand(args: Array<String>): String {
        if (args.isEmpty()) {
            return "Incorrect input! The command goes: ::stack Arham"
        } else if (args[0].startsWith("<@")) {
            return "Incorrect input! You cannot mention a Discord user in this command!"
        }
        val a: Int = RandomUtil.random(30)
        val b: Int = RandomUtil.random(47)
        val c: Int = RandomUtil.random(47)
        val d: Int = a + b + c
        val name = args[0].toLowerCase().capitalize()

        return when (args[0].toLowerCase()) {
            "logan" -> "You try to attack $name, but he teleports away before your attack lands."
            "substance", "izz" -> "You try to attack $name, but he deflects the attack back at you with his paintbrush for a total damage of $d."
            "harry" -> "You try to attack $name, but he bans you in real life. Good game."
            "corey" -> "You try to attack $name, but he programs the ban hammer to attack you back for a total damage of $d and a ban on top of that!"
            "arham" -> {
                val e: Int = RandomUtil.random(3)
                when (e) {
                    0 -> "You can't attack $name! Better luck next time."
                    1 -> "You kill yourself attempting to attack $name."
                    else -> "You manage to rip through the barriers $name has and attack him, hitting a $a with blitz along with a spec of $b and $c with your dds, all for a total damage of $d. Well played."
                }
            }
            "nouish", "erik" -> {
                val roll: Int = RandomUtil.random(128)
                when {
                    roll < 5 -> "You don't have the heart to attack $name when he's eating cake."
                    roll < 35 -> "All your attacks miss. $name clearly set his defence level very high with cheats."
                    roll < 55 -> "Failed to calculate attack - did $name drop the table?" // Added this for you Izz.
                    else -> "You attack $name, hitting a $a with blitz along with a spec of $b and $c with your dds, all for a total damage of $d."
                }
            }
            else -> "You attack $name, hitting a $a with blitz along with a spec of $b and $c with your dds, all for a total damage of $d."
        }
    }

}