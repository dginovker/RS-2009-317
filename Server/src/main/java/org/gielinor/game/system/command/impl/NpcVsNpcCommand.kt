package org.gielinor.game.system.command.impl

import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.entity.player.info.Rights
import org.gielinor.game.system.command.Command
import org.gielinor.game.system.command.CommandDescription
import plugin.npc.osrs.crazy_archeologist.CrazyArcheologistNPC
import plugin.npc.osrs.chaos.fanatic.ChaosFanaticNPC

/**
 * Spawns an NPC under the player.
 *
 * @author [Gielinor Logan G.](https://Gielinor.org)
 */
class NpcVsNpcCommand : Command() {

    override fun getRights(): Rights {
        return Rights.GIELINOR_MODERATOR
    }

    override fun canUse(player: Player): Boolean {
        return true
    }

    override fun getCommands(): Array<String> {
        return arrayOf("nvn")
    }

    override fun init() {
        CommandDescription
            .add(CommandDescription("nvn", "Spawns 2 npcs to fight each other.", rights, "::nvn <lt>npc_id1> <lt>npc_id2>"))
    }

    override fun execute(player: Player, args: Array<String>) {
        if (args.size != 3) {
            player.actionSender.sendMessage("Use as ::nvn <lt>npc_id1> <lt>npc_id2>")
            return
        }
        val id1 = args[1].toInt()
        val id2 = args[2].toInt()
        val npc1 = ChaosFanaticNPC(id1, player.location.transform(-3, 0, 0))
        val npc2 = CrazyArcheologistNPC(id2, player.location.transform(3, 0, 0))
        npc1.setTeleportTarget(npc1.location)
        npc1.init()
        npc2.setTeleportTarget(npc2.location)
        npc2.init()
        npc1.attack(npc2)
        npc2.attack(npc1)
    }
}
