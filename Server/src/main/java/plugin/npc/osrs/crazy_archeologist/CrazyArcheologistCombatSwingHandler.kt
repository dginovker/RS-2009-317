package plugin.npc.osrs.crazy_archeologist

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
import plugin.npc.osrs.crazy_archeologist.CrazyArcheologistCombatSwingHandler.Locations.endLocation1
import plugin.npc.osrs.crazy_archeologist.CrazyArcheologistCombatSwingHandler.Locations.endLocation2
import plugin.npc.osrs.crazy_archeologist.CrazyArcheologistCombatSwingHandler.Locations.endLocation3
import plugin.npc.osrs.crazy_archeologist.CrazyArcheologistCombatSwingHandler.Locations.subLocation1
import plugin.npc.osrs.crazy_archeologist.CrazyArcheologistCombatSwingHandler.Locations.subLocation2

class CrazyArcheologistCombatSwingHandler(var style: CombatStyle = CombatStyle.RANGE) : CombatSwingHandler(style) {

    companion object Constants {
        private val QUOTES = arrayOf("I'm Bellock - respect me!",
            "Get off my site!",
            "No-one messes with Bellock's dig!",
            "These ruins are mine!",
            "Taste my knowledge!",
            "You belong in a museum!")
        private val MUSEUM_QUOTE = QUOTES.last()
        private val SPECIAL_QUOTE = "Rain of knowledge!"
        const val DEATH_QUOTE = "Ow!"
        private val PROJECTILE_ID = 100
        private val SPECIAL_PROJECTILE_ID = 99
        private val MUSEUM_PROJECTILE_ID = 98
        private val MUSEUM_IMPACT_GFX = 305
        private val IMPACT_GFX = 157
    }

    object Locations {
        var endLocation1: Location = Location(-1, -1, -1)
        var endLocation2: Location = endLocation1.transform(1, 0, 0)
        var endLocation3: Location = endLocation1.transform(2, 1, 0)
        var subLocation1: Location = endLocation1.transform(-2, 0, 0)
        var subLocation2: Location = endLocation1.transform(0, 2, 0)

        fun inSubLocation(entity: Entity): Boolean {
            return entity.location == subLocation1 || entity.location == subLocation2
        }

        fun inEndLocation(entity: Entity): Boolean {
            return entity.location == endLocation1 || entity.location == endLocation2 || entity.location == endLocation3
        }
    }

    private var special: Boolean = false
    private var musuemAttack: Boolean = false

    override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
        return style.swingHandler.swing(entity, victim, state)
    }

    override fun impact(entity: Entity, victim: Entity, state: BattleState) {
        if (special) {
            entity.sendChat(SPECIAL_QUOTE)
            RegionManager.getLocalEntitys(victim).forEach { regionEntity ->
                if (Locations.inEndLocation(regionEntity)) {
                    regionEntity.impactHandler.manualHit(entity, RandomUtil.random(style.swingHandler.calculateHit(entity, regionEntity, 1.0)), ImpactHandler.HitsplatType.NORMAL)
                }
                onSubLocations {
                    if (Locations.inSubLocation(regionEntity)) {
                        regionEntity.impactHandler.manualHit(entity, RandomUtil.random(style.swingHandler.calculateHit(entity, regionEntity, 1.0)), ImpactHandler.HitsplatType.NORMAL)
                    }
                }
            }

            special = false
        } else {
            style.swingHandler.impact(entity, victim, state)
        }
    }

    override fun visualizeImpact(entity: Entity, victim: Entity, state: BattleState) {
        val randomInt = RandomUtil.random(10)
        if (randomInt == 1) {
            special = true
        }
        if (special) {
            state.maximumHit = 25
            endLocation1 = victim.location
            endLocation2 = endLocation1.transform(1, 0, 0)
            endLocation3 = endLocation1.transform(2, 1, 0)
            subLocation1 = endLocation1.transform(-2, 0, 0)
            subLocation2 = endLocation1.transform(0, 2, 0)

            fun sendProjectile(startLocation: Location, endLocation: Location) {
                Projectile.ranged(startLocation, endLocation, SPECIAL_PROJECTILE_ID, 0, 0, 46, 1).send()
            }

            fun sendGraphic(location: Location) {
                Graphics.send(Graphics.create(IMPACT_GFX), location)
            }

            fun onImpact(timeForImpact: Int, action: () -> Unit) {
                World.submit(object : Pulse(timeForImpact) {
                    override fun pulse(): Boolean {
                        action()
                        return true
                    }
                })
            }

            sendProjectile(entity.location, endLocation1)
            sendProjectile(entity.location, endLocation2)
            sendProjectile(entity.location, endLocation3)
            onImpact(victim.location.getDelay(entity.location)) {
                sendGraphic(endLocation1)
                sendGraphic(endLocation2)
                sendGraphic(endLocation3)
                sendProjectile(endLocation1, subLocation1)
                sendProjectile(endLocation1, subLocation2)
                onSubLocations {
                    sendGraphic(subLocation1)
                    sendGraphic(subLocation2)
                }
            }
            return
        }
        val randomQuote = QUOTES.random()
        entity.sendChat(randomQuote)
        if (randomQuote == MUSEUM_QUOTE) {
            musuemAttack = true
        }
        if (Location.getDistance(entity.location, victim.location) <= 1) {
            style = if (RandomUtil.random(10) <= 2) {
                CombatStyle.RANGE
            } else {
                CombatStyle.MELEE
            }
        } else {
            style
        }
        if (style == CombatStyle.RANGE) {
            state.maximumHit = 15
            Projectile.ranged(entity, victim, if (musuemAttack) MUSEUM_PROJECTILE_ID else PROJECTILE_ID, 0, 0, 46, 1).send()
        }
        style.swingHandler.visualizeImpact(entity, victim, state)
    }

    override fun onImpact(entity: Entity?, victim: Entity?, state: BattleState?) {
        if (musuemAttack) {
            Graphics.send(Graphics.create(MUSEUM_IMPACT_GFX), victim?.location)
            musuemAttack = false
        }
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

    private fun onSubLocations(action: () -> Unit) {
        World.submit(object : Pulse(endLocation1.getDelay(subLocation1)) {
            override fun pulse(): Boolean {
                action()
                return true
            }
        })
    }
}
