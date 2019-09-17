package org.gielinor.game.node.entity.state.impl

import org.gielinor.game.node.entity.Entity
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.entity.state.EntityState
import org.gielinor.game.node.entity.state.StatePulse
import org.gielinor.util.extensions.color
import java.nio.ByteBuffer

/**
 * Monitors the state of being under the special effect of the Staff of the Dead.
 * @author Splinter
 */
class StaffOfTheDeadPulse(entity: Entity?, private val ticks: Int, private var currentTick: Int) : StatePulse(entity, 1) {

    override fun getSaveValue(): Long {
        return 0
    }

    override fun parseValue(entity: Entity, value: Long): StatePulse? {
        return null
    }

    override fun isSaveRequired(): Boolean {
        return currentTick < ticks
    }

    override fun save(buffer: ByteBuffer) {
        buffer.putInt(ticks)
        buffer.putInt(currentTick)
    }

    override fun parse(entity: Entity, buffer: ByteBuffer): StatePulse {
        return StaffOfTheDeadPulse(entity, buffer.int, buffer.int)
    }

    override fun start() {
        if (currentTick == 0) {
            (entity as? Player)?.asPlayer()?.actionSender?.sendMessage("Spirits of deceased evildoers offer you their protection.".color("006600"))
        }
        super.start()
    }

    override fun pulse(): Boolean {
        val player = entity.asPlayer()
        if (++currentTick >= ticks || player.equipment.get(3) == null || player.equipment.get(3) != null && player.equipment.get(3).id != 14726) {
            player.actionSender.sendMessage("Your protection fades away.".color("006600"))
            player.stateManager.remove(EntityState.STAFF_OF_THE_DEAD)
            return true
        }
        return false
    }

    override fun create(entity: Entity, vararg args: Any): StatePulse {
        return StaffOfTheDeadPulse(entity, args[0] as Int, 0)
    }

}
