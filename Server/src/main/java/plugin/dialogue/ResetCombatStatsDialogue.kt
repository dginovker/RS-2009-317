package plugin.dialogue

import org.gielinor.game.content.dialogue.DialogueInterpreter
import org.gielinor.game.content.dialogue.DialoguePlugin
import org.gielinor.game.content.dialogue.OptionSelect
import org.gielinor.game.content.skill.Skills
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.item.Item
import org.gielinor.utilities.misc.RunScript
import org.gielinor.utilities.security.BCrypt
import org.slf4j.LoggerFactory

/**
 * DESCRIPTION
 *
 * @author Arham 4
 */
class ResetCombatStatsDialogue : DialoguePlugin {

    private var log = LoggerFactory.getLogger(javaClass)

    constructor(player: Player) : super(player)

    constructor()

    private val NPC_ID = 0
    private var statToChange = ""
    private var interfaceId = 0
    private var skillIds = intArrayOf()

    override fun newInstance(player: Player): DialoguePlugin {
        return ResetCombatStatsDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        interpreter.sendPlaneMessage(false, "Enter your current password to proceed:")
        stage = 0
        return true
    }

    private var currentPassword = ""

    override fun handle(interfaceId: Int, optionSelect: OptionSelect?): Boolean {
        this.interfaceId = interfaceId
        val cost = Item(Item.COINS, if (skillIds.size > 1) 3000000 else 1000000)
        when (stage) {
            0 -> {
                promptCurrentPassword()
                stage = 1
            }
            1 -> stage = if (checkPassword(currentPassword)) {
                interpreter.sendPlaneMessage("The entered password is correct,", "you may now change your password.")
                2
            } else {
                interpreter.sendPlaneMessage("Incorrect password.")
                END
            }
            2 -> {
                npc(0, "Hey, I'm a magical NPC who can reset your", "combat stats, even individually. You wanna?")
                stage = 3
            }
            3 -> {
                options("Yes, reset combat stats.", "No, don't reset combat stats.")
                stage = 4
            }
            4 -> {
                if (optionSelect == OptionSelect.TWO_OPTION_ONE) {
                    player("Yeah sure!")
                    stage = 5
                } else if (optionSelect == OptionSelect.TWO_OPTION_TWO) {
                    player("No! Why the hell would I want to do", "that??")
                    stage = END
                }
            }
            5 -> stage = if (player.equipment.isEmpty) {
                npc(0, "Okay man, which one?")
                6
            } else {
                npc(0, "Sorry to tell you, but you need to have", "nothing equipped in order to do this.")
                END
            }
            6 -> {
                options("All (3M)", "Attack (1M)", "Strength (1M)", "Defence (1M)", "Next")
                stage = 7
            }
            7 -> {
                when (optionSelect) {
                    OptionSelect.FIVE_OPTION_ONE -> handleSkillSelection("all of them",
                        intArrayOf(Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGE, Skills.PRAYER,
                            Skills.MAGIC, Skills.HITPOINTS, Skills.SUMMONING))
                    OptionSelect.FIVE_OPTION_TWO -> handleSkillSelection("Attack", Skills.ATTACK)
                    OptionSelect.FIVE_OPTION_THREE -> handleSkillSelection("Strength", Skills.STRENGTH)
                    OptionSelect.FIVE_OPTION_FOUR -> handleSkillSelection("Defence", Skills.DEFENCE)
                    OptionSelect.FIVE_OPTION_FIVE -> {
                        options("Ranged (1M)", "Prayer (1M)", "Magic (1M)", "Hitpoints (1M)", "Next")
                        stage = 8
                    }
                    else -> log.warn("{} tried to use bad option: {}.", player.name, optionSelect)
                }
            }
            8 -> when (optionSelect) {
                OptionSelect.FIVE_OPTION_ONE -> handleSkillSelection("Ranged", Skills.RANGE)
                OptionSelect.FIVE_OPTION_TWO -> handleSkillSelection("Prayer", Skills.PRAYER)
                OptionSelect.FIVE_OPTION_THREE -> handleSkillSelection("Magic", Skills.MAGIC)
                OptionSelect.FIVE_OPTION_FOUR -> handleSkillSelection("Hitpoints", Skills.HITPOINTS)
                OptionSelect.FIVE_OPTION_FIVE -> {
                    options("Summoning (1M)", "Front")
                    stage = 9
                }
                else -> log.warn("{} tried to use bad option: {}.", player.name, optionSelect)
            }
            9 -> when (optionSelect) {
                OptionSelect.TWO_OPTION_ONE -> handleSkillSelection("Summoning", Skills.SUMMONING)
                OptionSelect.TWO_OPTION_TWO -> {
                    stage = 6
                    handle(interfaceId, null)
                }
                else -> log.warn("{} tried to use bad option: {}.", player.name, optionSelect)
            }
            10 -> {
                npc(0, "So you want to reset $statToChange? Alright", "man, but this is dangeous. You sure", "you wanna do this?")
                stage = 11
            }
            11 -> {
                options("Yeah man.", "Nevermind bro.")
                stage = 12
            }
            12 -> {
                if (optionSelect == OptionSelect.TWO_OPTION_ONE) {
                    player("Yeah man!")
                    stage = 13
                } else if (optionSelect == OptionSelect.TWO_OPTION_TWO) {
                    player("Nevermind bro.")
                    stage = END
                }
            }
            13 -> stage = if (player.hasItem(cost)) {
                npc(0, "Alright, here we go...")
                14
            } else {
                npc(0, "Arham told me you have no money. You're broke,", "go train.")
                END
            }
            14 -> {
                player.removeItem(cost)
                for (skillId in skillIds) {
                    player.skills.resetSkill(skillId)
                }
                end()
            }
            END -> {
                end()
            }
        }
        return true
    }

    private fun handleSkillSelection(statToChange: String, skillId: Int) {
        return handleSkillSelection(statToChange, intArrayOf(skillId))
    }

    private fun handleSkillSelection(statToChange: String, skillIds: IntArray) {
        this.statToChange = statToChange
        this.skillIds = skillIds
        stage = 10
        handle(interfaceId, null)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPC_ID, DialogueInterpreter.getDialogueKey("ResetCombatStatsDialogue"))
    }

    private fun promptCurrentPassword() {
        player.setAttribute("runscript", object : RunScript() {
            override fun handle(): Boolean {
                val input = value as String
                currentPassword = input
                interpreter.sendPlaneMessage("You entered:", "'" + currentPassword.replace("_", " ") + "'")
                return true
            }
        })
        interpreter.sendPWInput(true, "Enter current password:")
    }

    private fun checkPassword(toCheck: String): Boolean {
        return BCrypt.hashpw(toCheck, "$2a$13$" + player.details.salt) == player.details.password
    }
}
