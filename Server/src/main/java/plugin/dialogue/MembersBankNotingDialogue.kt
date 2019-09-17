package plugin.dialogue

import org.gielinor.constants.MembersBankNotingNpcConstants.MEMBERS_BANK_NOTING_NPC_ID
import org.gielinor.game.content.dialogue.DialoguePlugin
import org.gielinor.game.content.dialogue.OptionSelect
import org.gielinor.game.node.entity.npc.NPC
import org.gielinor.game.node.entity.player.Player
import plugin.interaction.item.withnpc.MembersBankNotingPlugin

/**
 * Member's bank noting NPC dialogue
 *
 * @author Arham 4
 */
class MembersBankNotingDialogue : DialoguePlugin {

    constructor(player: Player) : super(player)

    constructor()

    override fun newInstance(player: Player): DialoguePlugin {
        return MembersBankNotingDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Hey, can you..")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, optionSelect: OptionSelect?): Boolean {
        when (stage) {
            0 -> {
                if (MembersBankNotingPlugin.hasRequirements(player)) {
                    MembersBankNotingPlugin.note(player)
                    npc("Yeah, yeah. Next time be smart and just use the", "item on me instead of talking to me. You", "think I like talking to you stupid players?")
                    stage = 1
                } else {
                    npc("You stupid and talk instead of use item! Then you", "come without money! Go away!!")
                    stage = END
                }
            }
            1 -> {
                player("I'll have you know I'm not just any player,", "I'm a member. ${player.rights} to you, actually.")
                stage = 2
            }
            2 -> {
                npc("Oh my, I'm very sorry. Very sorry. Still, save", "your time by right clicking me next time, okay friend?")
                stage = 3
            }
            3 -> {
                player("Alright.")
                stage = END
            }
            END -> {
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(MEMBERS_BANK_NOTING_NPC_ID)
    }
}