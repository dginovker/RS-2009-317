package plugin.interaction.npc

import org.gielinor.cache.def.impl.NPCDefinition
import org.gielinor.game.content.skill.member.slayer.Master
import org.gielinor.game.interaction.OptionHandler
import org.gielinor.game.node.Node
import org.gielinor.game.node.entity.npc.NPC
import org.gielinor.game.node.entity.player.Player
import org.gielinor.rs2.plugin.Plugin

/**
 * Right-click Assignment option for slayer masters
 * @author Corey
 */
class SlayerMasterAssignmentPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any?> {
        for (master in Master.ALL) {
            NPCDefinition.forId(master.npc).configurations.put("option:assignment", this);
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        player.dialogueInterpreter.open((node as NPC).id, node, true)
        return true
    }

}
