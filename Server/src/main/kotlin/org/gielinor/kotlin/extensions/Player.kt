package org.gielinor.kotlin.extensions

import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.world.World
import org.gielinor.rs2.pulse.Pulse


inline fun Player.delayed(delay: Int, crossinline action: Player.() -> Unit) {
    lock()
    World.submit(object: Pulse(delay, this) {
        override fun pulse(): Boolean {
            try {
                this@delayed.action()
                return true
            } finally {
                unlock()
            }
        }
    })
}

fun Player.sendMessage(message: String) {
    this.actionSender.sendMessage(message)
}
