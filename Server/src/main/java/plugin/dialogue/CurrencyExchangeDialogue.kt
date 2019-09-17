package plugin.dialogue

import org.gielinor.game.content.dialogue.DialoguePlugin
import org.gielinor.game.content.dialogue.OptionSelect
import org.gielinor.game.node.entity.npc.NPC
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.item.Item
import org.gielinor.utilities.misc.RunScript
import org.gielinor.utilities.string.TextUtils

/**
 * Represents the npc that exchanges donator points for GP

 * @author Arham 4
 */
class CurrencyExchangeDialogue : DialoguePlugin {

    constructor(player: Player) : super(player)

    constructor()

    private var inputAmount = -1

    private val CURRENCY_EXCHANGE_NPC_ID = 26043
    private val EXCHANGE_RATE = 1000000
    private val GP = inputAmount * EXCHANGE_RATE
    private val GP_FORMATTED = TextUtils.getFormattedNumber(GP)

    override fun newInstance(player: Player): DialoguePlugin {
        return CurrencyExchangeDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Hey! Can I get you to exchange my donator points", "for money?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, optionSelect: OptionSelect?): Boolean {
        when (stage) {
            0 -> {
                npc("Yeah sure, Arham always makes me do stuff", "I don't wanna do.")
                stage = 1
            }
            1 -> {
                player("Who's Arham?")
                stage = 2
            }
            2 -> {
                npc("Never mind that, the exchange rate is $EXCHANGE_RATE GP", "per donator point.")
                stage = 3
            }
            3 -> {
                options("Accept $EXCHANGE_RATE GP per donator point", "Decline")
                stage = 4
            }
            4 -> {
                if (optionSelect == OptionSelect.TWO_OPTION_ONE) {
                    player("Deal. $EXCHANGE_RATE GP per donator point.")
                    stage = 5
                } else if (optionSelect == OptionSelect.TWO_OPTION_TWO) {
                    player("Naw man. Freaking scammer.")
                    stage = -1
                }
            }
            5 -> {
                if (player.donorManager.gielinorTokens < 1) {
                    npc("Hey! Arham said you don't got any donator", "points. Tryna scam me or something?")
                    stage = -1
                } else if (player.inventory.freeSlots() < 1 && !player.inventory.contains(Item.COINS)) {
                    npc("Yo, Arham told me you don't have an inventory", "space full. Come back when you have one.")
                    stage = -1
                } else {
                    npc("Alright, how much?")
                    stage = 6
                }
            }
            6 -> {
                player.dialogueInterpreter.sendInput(true, "Enter how many donator points to convert:")
                player.setAttribute("runscript", object : RunScript() {
                    override fun handle(): Boolean {
                        if (getValue() != null) {
                            inputAmount = getValue() as Int
                            if (inputAmount > player.donorManager.gielinorTokens) {
                                npc("Arham said you have ${player.donorManager.gielinorTokens} donator points, so where", "you gonna pull out $inputAmount? Go away.")
                                stage = -1
                            } else {
                                npc("So, you'll get $GP_FORMATTED GP for $inputAmount donator", "points. Sound cool?")
                                stage = 7
                            }

                        }
                        return true
                    }
                })
            }
            7 -> {
                optionsWithCustomTitle("Accept $GP_FORMATTED GP for $inputAmount donator points?", "Yes", "No")
                stage = 8
            }
            8 -> {
                if (optionSelect == OptionSelect.TWO_OPTION_ONE) {
                    player("Yeah, sounds cool.")
                    stage = 9
                } else if (optionSelect == OptionSelect.TWO_ITEM_SELECT_TWO) {
                    player("Nah man, that's a rip off.")
                    stage = -1
                }
            }
            9 -> {
                exchange()
                npc("Pleasure doing business with ya.")
                stage = -1
            }
            -1 -> {
                end()
            }
        }
        return true
    }

    private fun exchange() {
        player.donorManager.gielinorTokens = player.donorManager.gielinorTokens - inputAmount
        player.inventory.add(Item(Item.COINS, GP))
        player.actionSender.sendMessage("You have exchanged $inputAmount donator points for $GP_FORMATTED GP. You now have ${player.donorManager.gielinorTokens} donator points left.")
    }

    override fun getIds(): IntArray {
        return intArrayOf(CURRENCY_EXCHANGE_NPC_ID)
    }
}