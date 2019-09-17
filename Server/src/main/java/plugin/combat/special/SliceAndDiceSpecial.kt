package plugin.combat.special

import org.gielinor.game.node.entity.Entity
import org.gielinor.game.node.entity.combat.BattleState
import org.gielinor.game.node.entity.combat.CombatStyle
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler
import org.gielinor.game.node.entity.impl.Animator
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.entity.state.EntityState
import org.gielinor.game.world.update.flag.context.Animation
import org.gielinor.game.world.update.flag.context.Graphics
import org.gielinor.rs2.plugin.Plugin
import org.gielinor.utilities.misc.RandomUtil
/**
 * Handles the Dragon claws special attack.
 *
 * TODO: add graphic
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Dragon_claws
 *
 * @author Emperor
 * @version 1.0
 */
class SliceAndDiceSpecial : MeleeSwingHandler(), Plugin<Any> {

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        CombatStyle.MELEE.swingHandler.register(33652, this)
        return this
    }

    override fun fireEvent(identifier: String, vararg args: Any): Any? {
        return ""
    }

    override fun swing(entity: Entity, victim: Entity, state: BattleState): Int {
        if (!(entity as Player).settings.drainSpecial(SPECIAL_ENERGY)) {
            return -1
        }
        var hits = intArrayOf(0, 1)
        var hit: Int
        var amountCalled = 0
        tailrec fun regenerateHits() {
            hit = RandomUtil.random(calculateHit(entity, victim, 1.0))
            if (hit > 0) {
                amountCalled++
                when (amountCalled) {
                    1 -> hits = intArrayOf(hit, hit / 2, (hit / 2) / 2, (hit / 2) - ((hit / 2) / 2))
                    2 -> hits = intArrayOf(0, hit, hit / 2, hit - hit / 2)
                    3 -> hits = intArrayOf(0, 0, hit / 2, (hit / 2) + 10)
                    4 -> hits = intArrayOf(0, 0, 0, (hit * 1.5).toInt())
                }
            } else {
                if (amountCalled < 4) {
                    regenerateHits()
                } else {
                    hits = intArrayOf(0, 0, 0, RandomUtil.getRandom(7))
                }
            }
        }
        regenerateHits()
        for ((index, value) in hits.withIndex()) {
            when (index) {
                0 -> state.estimatedHit = value
                1 -> state.secondaryHit = value
                2 -> state.thirdHit = value
                3 -> state.fourthHit = value
            }
        }
        return 1
    }

    override fun visualize(entity: Entity, victim: Entity, state: BattleState) {
        entity.visualize(ANIMATION, GRAPHIC)
    }

    override fun calculateAccuracy(entity: Entity?): Int {
        return super.calculateAccuracy(entity) * RandomUtil.random(1.5, 4.0).toInt()
    }

    companion object {
        private val SPECIAL_ENERGY = 50
        private val ANIMATION = Animation(10961, Animator.Priority.HIGH)
        private val GRAPHIC = Graphics(-1)
    }
}
