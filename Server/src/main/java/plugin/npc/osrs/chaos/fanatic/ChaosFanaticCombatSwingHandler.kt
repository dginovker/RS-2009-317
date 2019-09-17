package plugin.npc.osrs.chaos.fanatic

import org.gielinor.game.node.entity.Entity
import org.gielinor.game.node.entity.combat.BattleState
import org.gielinor.game.node.entity.combat.CombatStyle
import org.gielinor.game.node.entity.combat.CombatSwingHandler
import org.gielinor.game.node.entity.combat.ImpactHandler
import org.gielinor.game.node.entity.impl.Projectile
import org.gielinor.game.world.World
import org.gielinor.game.world.map.Location
import org.gielinor.game.world.map.RegionManager
import org.gielinor.game.world.update.flag.context.Graphics
import org.gielinor.rs2.pulse.Pulse
import org.gielinor.util.extensions.random
import org.gielinor.utilities.misc.RandomUtil
import plugin.npc.osrs.chaos.EquipmentRemoving
import plugin.npc.osrs.chaos.fanatic.ChaosFanaticCombatSwingHandler.Locations.endLocation1
import plugin.npc.osrs.chaos.fanatic.ChaosFanaticCombatSwingHandler.Locations.endLocation2
import plugin.npc.osrs.chaos.fanatic.ChaosFanaticCombatSwingHandler.Locations.endLocation3

class ChaosFanaticCombatSwingHandler(var style: CombatStyle = CombatStyle.MAGIC) : CombatSwingHandler(style) {

    companion object Constants {
        private val QUOTES = arrayOf("Burn!",
            "WEUGH!",
            "All your wilderness are belong to them!",
            "AhehHeheuhHhahueHuUEehEahAH",
            "I shall call him squidgy and he shall be my squidgy!",
            "Develish Oxen Roll!")
        private val OXEN_QUOTE = QUOTES.last()
        private val PROJECTILE_ID_1 = 554
        private val PROJECTILE_ID_2 = 551
        private val OXEN_IMPACT_GFX = 305
        private val SPECIAL_PROJECTILE_ID = 551
        private val IMPACT_GFX = 157
    }

    object Locations {
        var endLocation1: Location = Location(-1, -1, -1)
        var endLocation2: Location = endLocation1.transform(1, 0, 0)
        var endLocation3: Location = endLocation1.transform(2, -1, 0)

        fun inEndLocation(entity: Entity): Boolean {
            return entity.location == endLocation1 || entity.location == endLocation2 || entity.location == endLocation3
        }
    }

    private var special: Boolean = false
    private var oxenAttack: Boolean = false

    override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
        return style.swingHandler.swing(entity, victim, state)
    }

    override fun impact(entity: Entity, victim: Entity, state: BattleState) {
        if (special) {
            RegionManager.getLocalEntitys(victim).forEach { regionEntity ->
                if (Locations.inEndLocation(regionEntity)) {
                    regionEntity.impactHandler.manualHit(entity, RandomUtil.random(style.swingHandler.calculateHit(entity, regionEntity, 1.0)), ImpactHandler.HitsplatType.NORMAL)
                }
            }
            special = false
        } else {
            style.swingHandler.impact(entity, victim, state)
        }
    }

    override fun visualizeImpact(entity: Entity, victim: Entity, state: BattleState) {
        val randomInt = RandomUtil.random(10)
        when (randomInt) {
            1 -> special = true
            2, 3 -> EquipmentRemoving.strip(victim)
        }
        val randomQuote = QUOTES.random()
        entity.sendChat(randomQuote)
        if (randomQuote == OXEN_QUOTE) {
            oxenAttack = true
        }
        if (special) {
            endLocation1 = victim.location
            endLocation2 = endLocation1.transform(1, 0, 0)
            endLocation3 = endLocation1.transform(2, -1, 0)

            fun sendProjectile(startLocation: Location, endLocation: Location) {
                Projectile.magic(startLocation, endLocation, SPECIAL_PROJECTILE_ID, 0, 0, 46, 1).send()
            }

            fun sendGraphic(location: Location) {
                Graphics.send(Graphics.create(IMPACT_GFX), location)
            }

            fun sendPulse(time: Int, pulse: () -> Unit) {
                World.submit(object : Pulse(time) {
                    override fun pulse(): Boolean {
                        pulse()
                        return true
                    }
                })
            }

            sendProjectile(entity.location, endLocation1)
            sendProjectile(entity.location, endLocation2)
            sendProjectile(entity.location, endLocation3)
            sendPulse(victim.location.getDelay(entity.location)) {
                sendGraphic(endLocation1)
                sendGraphic(endLocation2)
                sendGraphic(endLocation3)
            }
            return
        }
        Projectile.magic(entity, victim, if (RandomUtil.random(5) == 1) PROJECTILE_ID_1 else PROJECTILE_ID_2, 0, 0, 46, 3).send()
        style.swingHandler.visualizeImpact(entity, victim, state)
    }

    override fun onImpact(entity: Entity?, victim: Entity?, state: BattleState?) {
        if (oxenAttack && !special) {
            Graphics.send(Graphics.create(OXEN_IMPACT_GFX), victim?.location)
        }
        oxenAttack = false
        super.onImpact(entity, victim, state)
    }

    override fun calculateAccuracy(entity: Entity?): Int {
        return style.swingHandler.calculateAccuracy(entity)
    }

    override fun calculateHit(entity: Entity, victim: Entity, modifier: Double): Int {
        return style.swingHandler.calculateHit(entity, victim, modifier)
    }

    override fun calculateDefence(entity: Entity, attacker: Entity): Int {
        return style.swingHandler.calculateDefence(entity, attacker)
    }

    override fun getSetMultiplier(e: Entity, skillId: Int): Double {
        return style.swingHandler.getSetMultiplier(e, skillId)
    }
}
