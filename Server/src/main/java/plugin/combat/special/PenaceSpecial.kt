package plugin.combat.special

import org.gielinor.constants.AbyssalBludgeonAnimationConstants.SPECIAL_ANIMATION
import org.gielinor.constants.AbyssalBludgeonAttributeConstants.BLUDGEON_SPECIAL_MULTIPLIER
import org.gielinor.constants.AbyssalBludgeonGraphicsConstants.SPECIAL_GFX
import org.gielinor.constants.AbyssalBludgeonItemConstants.ABYSSAL_BLUDGEON
import org.gielinor.game.content.skill.Skills
import org.gielinor.game.node.entity.Entity
import org.gielinor.game.node.entity.combat.BattleState
import org.gielinor.game.node.entity.combat.CombatStyle
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.world.update.flag.context.Graphics
import org.gielinor.rs2.plugin.Plugin

/**
 * Handles the Abyssal bludgeon's special attack.
 *
 * TODO: find corresponding graphic id.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Abyssal_bludgeon
 *
 * @author ?
 * @version 1.0
 */
class PenaceSpecial() : MeleeSwingHandler(), Plugin<Any> {

    override fun newInstance(arg: Any?): Plugin<Any> {
        CombatStyle.MELEE.swingHandler.register(ABYSSAL_BLUDGEON, this)
        return this
    }

    override fun fireEvent(identifier: String, vararg args: Any): Any? {
        return ""
    }

    override fun swing(entity: Entity, victim: Entity, state: BattleState): Int {
        if (!(entity as Player).settings.drainSpecial(SPECIAL_ENERGY)) {
            println(1)
            return -1
        }
        val bludgeonMultiplier = ((0.5 * (entity.skills.getStaticLevel(Skills.PRAYER) - entity.skills.getLevel(Skills.PRAYER))) / 100) + 1
        entity.setAttribute(BLUDGEON_SPECIAL_MULTIPLIER, bludgeonMultiplier)
        return 1
    }

    override fun visualize(entity: Entity, victim: Entity, state: BattleState) {
        entity.animate(SPECIAL_ANIMATION)
        Graphics.send(SPECIAL_GFX, victim.location)
    }

    companion object {
        /**
         * The special energy required.
         */
        private val SPECIAL_ENERGY = 50
    }
}
