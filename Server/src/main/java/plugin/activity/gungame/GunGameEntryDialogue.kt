package plugin.activity.gungame

import org.gielinor.game.content.activity.ActivityManager
import org.gielinor.game.content.dialogue.DialoguePlugin
import org.gielinor.game.content.dialogue.OptionSelect
import org.gielinor.game.node.entity.player.Player
import org.gielinor.util.extensions.compareDays

/**
 * @author Arham 4
 */
class GunGameEntryDialogue : DialoguePlugin {

    constructor(player: Player) : super(player)

    constructor()

    private val NPC_ID = -1
    private val CANT_EQUIP_ITEMS = 100

    private var entryCost: Int = ReverseGunGameActivityPlugin.ENTRY_COST
    private val formattedEntryCost = "$entryCost GP"

    private val startDialogue = arrayOf("This minigame requires an entry fee of $formattedEntryCost to be put",
        "in a jackpot for a chance to win it all. This will be taken from",
        "you when you enter the game (not the lobby). Do you still",
        "wish to enter?")

    override fun newInstance(player: Player): DialoguePlugin {
        return GunGameEntryDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        if (player.savedData.activityData.lastGunGameEntry.compareDays(System.currentTimeMillis()) >= 7) {
            entryCost = (ReverseGunGameActivityPlugin.ENTRY_COST * player.donorManager.donorStatus.gunGameDiscount).toInt()
        }
        stage = if (player.getAttribute("reverse_gun_game", false)) {
            interpreter.sendPlaneMessage("You're already playing that minigame!")
            END
        } else if (player.familiarManager.hasFamiliar()) {
            interpreter.sendPlaneMessage("You cannot bring a familiar to this minigame. Dismiss it and try again please.")
            END
        } else {
            interpreter.sendPlaneMessage(*startDialogue)
            0
        }
        return true
    }

    fun start(){
        end()
        ActivityManager.start(player, "Reverse gun game", false)
        if (player.savedData.activityData.lastGunGameEntry.compareDays(System.currentTimeMillis()) >= 7) {
            player.savedData.activityData.lastGunGameEntry = System.currentTimeMillis()
        }
    }

    override fun handle(interfaceId: Int, optionSelect: OptionSelect?): Boolean {
        when (stage) {
            0 -> {
                options("$formattedEntryCost? Pfft, that's nothing!", "No, that's too much!")
                stage = 1
            }
            1 -> {
                if (optionSelect == OptionSelect.TWO_OPTION_ONE) {
                    if (!ReverseGunGameActivityPlugin.hasEntryCost(entryCost, player)) {
                        interpreter.sendPlaneMessage("You don't have $formattedEntryCost!")
                        stage = END
                    } else {
                        if (!player.equipment.isEmpty || !player.inventory.isEmpty) {
                            interpreter.sendPlaneMessage("You cannot bring items to this minigame.", "Would you like for your stuff to be banked?")
                            stage = CANT_EQUIP_ITEMS
                        } else start()
                    }
                } else if (optionSelect == OptionSelect.TWO_OPTION_TWO) end()
            }
            CANT_EQUIP_ITEMS -> {
                options("Yes, bank my stuff and move me in.", "No, don't bank my stuff.")
                stage = CANT_EQUIP_ITEMS + 1
            }
            CANT_EQUIP_ITEMS + 1 -> {
                if (optionSelect == OptionSelect.TWO_OPTION_ONE) {
                    player.inventoryListener.toBank()
                    player.equipment.toBank()
                    interpreter.sendPlaneMessage(*startDialogue)
                    start()
                } else if (optionSelect == OptionSelect.TWO_OPTION_TWO) end()
            }
            END -> {
                end()
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPC_ID)
    }
}
