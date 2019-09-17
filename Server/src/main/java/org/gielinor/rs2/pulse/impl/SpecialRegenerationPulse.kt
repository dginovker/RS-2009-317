package org.gielinor.rs2.pulse.impl

import org.gielinor.game.node.entity.Entity
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.world.World
import org.gielinor.rs2.pulse.Pulse

class SpecialRegenerationPulse(private val pulseTime: Int, private val player: Player) : Pulse(pulseTime) {

    private var newPulse: Boolean = false;

    override fun pulse(): Boolean {
        fun heal() {
            if (player.settings.specialEnergy < 100) {
                val heal = 100 - player.settings.specialEnergy
                player.settings.specialEnergy += if (heal > 10) 10 else heal
            }
        }

        if (!newPulse) {
            if (player.getAttribute<Entity?>("combat-attacker") is Player && player.donorManager.hasMembership()) {
                World.submit(object : Pulse(50 - pulseTime) {
                    override fun pulse(): Boolean {
                        heal()
                        newPulse = false
                        return true
                    }
                })
                newPulse = true
            } else {
                heal()
            }
        }
        return false
    }
}