package plugin.combat.special

import org.gielinor.constants.StaffOfTheDeadAnimationConstants.SPECIAL_ANIMATION
import org.gielinor.constants.StaffOfTheDeadGraphicsConstants.SPECIAL_GFX
import org.gielinor.constants.StaffOfTheDeathItemConstants.STAFF_OF_THE_DEAD
import org.gielinor.game.node.entity.Entity
import org.gielinor.game.node.entity.combat.BattleState
import org.gielinor.game.node.entity.combat.CombatStyle
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.entity.state.EntityState
import org.gielinor.rs2.plugin.Plugin

/**
 * Handles the Toxic staff of the dead special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Toxic_staff_of_the_dead
 *
 * @author Splinter
 * @version 1.0
 */
class PowerOfDeathSpecial : MeleeSwingHandler(), Plugin<Any> {

    override fun fireEvent(identifier: String, vararg args: Any): Any? {
        return when (identifier) {
            "instant_spec", "ncspec" -> return true
            else -> null
        }
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        CombatStyle.MELEE.swingHandler.register(STAFF_OF_THE_DEAD, this)
        return this
    }

    override fun swing(entity: Entity, victim: Entity, state: BattleState): Int {
        val p = entity as Player
        if (p.stateManager.hasState(EntityState.STAFF_OF_THE_DEAD)) {
            p.actionSender.sendMessage("You are already affected by the special move of the staff.")
            p.settings.toggleSpecialBar()
            return -1
        }
        if (!p.settings.drainSpecial(SPECIAL_ENERGY))
            return -1
        p.visualize(SPECIAL_ANIMATION, SPECIAL_GFX)
        p.stateManager.set(EntityState.STAFF_OF_THE_DEAD, 100)
        return -1
    }

    companion object {
        private val SPECIAL_ENERGY = 100
    }

}
