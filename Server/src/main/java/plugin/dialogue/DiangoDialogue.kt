package plugin.dialogue

import org.gielinor.cache.def.impl.ItemDefinition
import org.gielinor.game.content.dialogue.DialoguePlugin
import org.gielinor.game.content.dialogue.OptionSelect
import org.gielinor.game.content.skill.member.summoning.pet.Pet
import org.gielinor.game.content.skill.member.summoning.pet.Pets
import org.gielinor.game.node.entity.npc.NPC
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.entity.player.info.donor.DonorStatus
import org.gielinor.game.node.item.Item
import org.gielinor.utilities.string.TextUtils

/**
 * Handles the dialogue for the Pet handling NPC
 *
 * @author Arham 4
 */
class DiangoDialogue : DialoguePlugin {

    constructor(player: Player) : super(player) {
        INSURANCE_DISCOUNT = player.donorManager.donorStatus.petInsuranceDiscount
    }

    constructor()

    private val NPC_ID = 970
    private var INSURANCE_DISCOUNT: Double = 1.0
    private val INSURANCE_COST = (25000000 * INSURANCE_DISCOUNT).toInt()
    private val INSURANCE_COST_FORMATTED = TextUtils.getFormattedNumber(INSURANCE_COST) + " GP"
    private val CLAIM_COST = 5000000
    private val CLAIM_COST_FORMATTED = TextUtils.getFormattedNumber(CLAIM_COST) + " GP"
    private val SCAMMING_DIALOGUE = arrayOf("Hey! Don't try scamming me! Arham knows you're", "trying to! Bring that money!")

    private var options = 0
    private var petOptions = arrayOf<String?>()
    private var petItemDefinition: ItemDefinition? = null
    private var lostPetIndex = 0
    private var pet = -1

    override fun newInstance(player: Player): DialoguePlugin {
        return DiangoDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Hey, I got a question in regards to my pets.")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, optionSelect: OptionSelect?): Boolean {
        val INSURE_PET_STAGE = 3
        val GET_PET_STAGE = 7
        val UNINSURE_PET_STAGE = 14
        when (stage) {
            0 -> {
                npc("Sure, what's the question?")
                stage = 1
            }
            1 -> {
                options("Can I get a boss pet of mine insured?", "Do you have any boss pets of mine with you?", "Can I un-insure a boss pet of mine?")
                stage = 2
            }
            2 -> {
                if (optionSelect == OptionSelect.THREE_OPTION_ONE) {
                    player("Can I get a pet of mine insured?")
                    stage = INSURE_PET_STAGE
                } else if (optionSelect == OptionSelect.THREE_OPTION_TWO) {
                    player("Do you have any boss pets of mine with you?")
                    stage = GET_PET_STAGE
                } else if (optionSelect == OptionSelect.THREE_OPTION_THREE) {
                    player("Can I un-insure a boss pet of mine?")
                    stage = UNINSURE_PET_STAGE
                }
            }
            INSURE_PET_STAGE -> {
                if (player.familiarManager.familiar == null || !player.familiarManager.hasPet()) {
                    npc("You have to have the pet summoned to insure it!")
                    stage = END
                    return true
                } else if (player.savedData.globalData.petsInsured >= 5) {
                    npc("You can only save a maximum of 5 pets!")
                    stage = END
                    return true
                } else if ((player.familiarManager.familiar as Pet).details.isInsured) {
                    npc("That pet is already insured!")
                    stage = END
                    return true
                }
                val isBossPet = Pets.isBossPet((player.familiarManager.familiar as Pet).itemId)
                if (isBossPet) {
                    npc("That will cost you $INSURANCE_COST_FORMATTED.")
                    stage = INSURE_PET_STAGE + 1
                } else {
                    npc("That's not a boss pet!")
                    stage = END
                }
            }
            INSURE_PET_STAGE + 1 -> {
                options("Pay $INSURANCE_COST_FORMATTED", "That's too much money!")
                stage = INSURE_PET_STAGE + 2
            }
            INSURE_PET_STAGE + 2 -> {
                if (optionSelect == OptionSelect.TWO_OPTION_ONE) {
                    player("Deal!")
                    stage = INSURE_PET_STAGE + 3
                } else if (optionSelect == OptionSelect.TWO_OPTION_TWO) {
                    player("That's too much money!")
                    stage = END
                }
            }
            INSURE_PET_STAGE + 3 -> {
                if (player.inventory.contains(Item.COINS, INSURANCE_COST)) {
                    npc("Done! Arham has a message for you:", "\"15 minutes could save you 15% or more", "on pet insurance. ;)\"")
                    (player.familiarManager.familiar as Pet).insure()
                    player.inventory.remove(Item(Item.COINS, INSURANCE_COST))
                } else {
                    npc(*SCAMMING_DIALOGUE)
                }
                stage = END
            }
            GET_PET_STAGE -> {
                if (player.savedData.globalData.lostPets.size > 0) {
                    npc("Sure, which one?")
                    stage = GET_PET_STAGE + 1
                } else {
                    npc("Nope, I don't have any. Arham says, \"If you", "think this is a mistake, report this!\"")
                    stage = END
                }
            }
            GET_PET_STAGE + 1 -> {
                options = player.savedData.globalData.lostPets.size
                petOptions = arrayOfNulls<String>(player.savedData.globalData.lostPets.size)
                for (i in petOptions.indices) {
                    petOptions[i] = ItemDefinition.forId(player.savedData.globalData.lostPets[i]).name
                }
                if (petOptions.size > 1) {
                    optionsWithCustomTitle("Which pet would you like back?", *petOptions)
                } else {
                    npc("Oh wait, never mind, I'm an idiot, you've only lost one.")
                    pet = player.savedData.globalData.lostPets[0]
                }
                stage = GET_PET_STAGE + 2
            }
            GET_PET_STAGE + 2 -> {
                if (optionSelect == OptionSelect.TWO_OPTION_ONE || optionSelect == OptionSelect.THREE_OPTION_ONE
                    || optionSelect == OptionSelect.FOUR_OPTION_ONE || optionSelect == OptionSelect.FIVE_OPTION_ONE) {
                    lostPetIndex = 0
                } else if (optionSelect == OptionSelect.TWO_OPTION_TWO || optionSelect == OptionSelect.THREE_OPTION_TWO
                    || optionSelect == OptionSelect.FOUR_OPTION_TWO || optionSelect == OptionSelect.FIVE_OPTION_TWO) {
                    lostPetIndex = 1
                } else if (optionSelect == OptionSelect.THREE_OPTION_THREE || optionSelect == OptionSelect.FOUR_OPTION_THREE
                    || optionSelect == OptionSelect.FIVE_OPTION_THREE) {
                    lostPetIndex = 2
                } else if (optionSelect == OptionSelect.FOUR_OPTION_FOUR || optionSelect == OptionSelect.FIVE_OPTION_FOUR) {
                    lostPetIndex = 3
                } else if (optionSelect == OptionSelect.FIVE_OPTION_FIVE) {
                    lostPetIndex = 4
                }
                if (player.savedData.globalData.lostPets[lostPetIndex] != -1) {
                    petItemDefinition = ItemDefinition.forId(pet)
                    npc("So you want your ${petItemDefinition?.name}?", "That will cost $CLAIM_COST_FORMATTED.")
                    stage = GET_PET_STAGE + 3
                } else {
                    throw RuntimeException("Pet is not supposed to be -1.")
                }
            }
            GET_PET_STAGE + 3 -> {
                options("Pay $CLAIM_COST_FORMATTED", "That's too much money!")
                stage = GET_PET_STAGE + 4
            }
            GET_PET_STAGE + 4 -> {
                if (optionSelect == OptionSelect.TWO_OPTION_ONE) {
                    player("Deal!")
                    stage = GET_PET_STAGE + 5
                } else if (optionSelect == OptionSelect.TWO_OPTION_TWO) {
                    player("That's too much money!")
                    stage = END
                }
            }
            GET_PET_STAGE + 5 -> {
                if (player.inventory.contains(Item.COINS, CLAIM_COST)) {
                    npc("Sounds good!")
                    stage = GET_PET_STAGE + 6
                } else {
                    npc(*SCAMMING_DIALOGUE)
                    stage = END
                }
            }
            GET_PET_STAGE + 6 -> {
                player.inventory.remove(Item(Item.COINS, CLAIM_COST))
                player.familiarManager.summon(Item(petItemDefinition?.id as Int, 1), true, false)
                (player.familiarManager.familiar as Pet).details.isInsured = true
                player.savedData.globalData.lostPets.removeAt(lostPetIndex)
                end()
            }
            UNINSURE_PET_STAGE -> {
                npc("This is a feature coming soon. Go bug Arham about it.")
                stage = END
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
