package bot.commands;

import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import org.gielinor.utilities.misc.RandomUtil

class DiceCommand : CommandExecutor {

    @Command(aliases = arrayOf("::roll"), description = "Returns a random number between 0-100, possibly for dicing. Code submission by: Jedzio")
    fun onRollCommand(): String {
        // Jedzio: a random number between 1-100
        // RandomUtil.random(101) previously generated 0-100.
        val roll = 1 + RandomUtil.random(100)
        return "You have rolled: $roll!"
    }

}
