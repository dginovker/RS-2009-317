package org.gielinor.game.system.command.impl

import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.entity.player.info.Rights
import org.gielinor.game.system.command.Command
import org.gielinor.game.system.command.CommandDescription
import org.gielinor.game.world.map.Location

/**
 * Spawns an NPC under the player.
 *
 * @author [Gielinor Logan G.](https://Gielinor.org)
 */
class ZulrahCommand : Command() {

    override fun getRights(): Rights {
        return Rights.GIELINOR_MODERATOR
    }

    override fun canUse(player: Player): Boolean {
        return true
    }

    override fun getCommands(): Array<String> {
        return arrayOf("zulrah")
    }

    override fun init() {
        CommandDescription
            .add(CommandDescription("zulrah", "Teleport to zulrah", rights, "::zulrah"))
    }

    override fun execute(player: Player, args: Array<String>) {
        player.teleport(Location(2199, 3056, 0))
    }
}
