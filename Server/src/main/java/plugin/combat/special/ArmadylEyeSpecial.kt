package plugin.combat.special

import org.gielinor.constants.ArmadylCrossbowAnimationConstants.SPECIAL_ANIMATION
import org.gielinor.constants.ArmadylCrossbowItemConstants.ARMADYL_CROSSBOW
import org.gielinor.game.node.entity.Entity
import org.gielinor.game.node.entity.combat.BattleState
import org.gielinor.game.node.entity.combat.CombatStyle
import org.gielinor.game.node.entity.combat.handlers.RangeSwingHandler
import org.gielinor.game.node.entity.impl.Projectile
import org.gielinor.game.node.entity.player.Player
import org.gielinor.rs2.plugin.Plugin
import org.gielinor.utilities.misc.RandomUtil

/**
 * Represents the new Armadyl C'bow special attack.
 *
 * TODO: find the associated audio id.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Armadyl_crossbow
 *
 * @author Splinter
 * @version 1.0
 */
class ArmadylEyeSpecial : RangeSwingHandler(), Plugin<Any> {

    override fun fireEvent(identifier: String, vararg args: Any): Any? {
        return null
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        CombatStyle.RANGE.swingHandler.register(ARMADYL_CROSSBOW, this)
        return this
    }

    override fun swing(entity: Entity, victim: Entity, state: BattleState): Int {
        val p = entity as Player
        configureRangeData(p, state)
        if (state.weapon == null || !RangeSwingHandler.hasAmmo(entity, state)) {
            entity.getProperties().combatPulse.stop()
            p.settings.toggleSpecialBar()
            return -1
        }
        if (!entity.settings.drainSpecial(SPECIAL_ENERGY)) {
            return -1
        }
        var hit = 0
        if (isAccurateImpact(entity, victim, CombatStyle.RANGE, 2.0, 1.0)) {
            hit = RandomUtil.random(calculateHit(entity, victim, 1.0))
        }
        RangeSwingHandler.useAmmo(entity, state, victim.location)
        state.estimatedHit = hit
        return 1
    }

    override fun visualize(entity: Entity, victim: Entity, state: BattleState) {
        entity.animate(SPECIAL_ANIMATION)
        Projectile.create(entity, victim, 698, 36, 25, 35, 50).send()
    }

    companion object {
        private val SPECIAL_ENERGY = 50
    }
}
