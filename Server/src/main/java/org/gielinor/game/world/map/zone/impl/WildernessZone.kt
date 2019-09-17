package org.gielinor.game.world.map.zone.impl

import org.gielinor.content.donators.DonatorConfigurations
import org.gielinor.database.PlayerLists
import org.gielinor.game.component.Component
import org.gielinor.game.interaction.Option
import org.gielinor.game.node.Node
import org.gielinor.game.node.entity.Entity
import org.gielinor.game.node.entity.combat.CombatStyle
import org.gielinor.game.node.entity.npc.NPC
import org.gielinor.game.node.entity.npc.agg.AggressiveBehavior
import org.gielinor.game.node.entity.npc.agg.AggressiveHandler
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.item.Item
import org.gielinor.game.world.map.Location
import org.gielinor.game.world.map.RegionManager
import org.gielinor.game.world.map.zone.MapZone
import org.gielinor.game.world.map.zone.ZoneBorders

/**
 * Handles the wilderness zone.
 *
 * @author Emperor
 * @author Arham 4 <-- lol
 */
class WildernessZone(vararg val borders: ZoneBorders) : MapZone("Wilderness", true) {

    override fun configure() {
        for (border in borders) {
            register(border)
        }
    }

    override fun enter(e: Entity): Boolean {
        if (e is Player) {
//            if (player.getInterfaceState().get(InterfaceConfiguration.WILDERNESS_WARNING) == 0) {
            //                // if (player.getSkullManager().getLevel() == 0) { // TODO = getWildernessLevel( ?
            //                player.getWalkingQueue().reset();
            //                player.getPulseManager().clear();
            //                leave(e, false);
            //                player.setLocation(player.getWalkingQueue().getFootPrint());
            //                player.getInterfaceState().open(new Component(46900));
            //                // }
            //            }
            show(e)
            if (e.familiarManager.hasFamiliar() && !e.familiarManager.hasPet()) {
                val familiar = e.familiarManager.familiar
                if (familiar.isCombatFamiliar) {
                    familiar.transform(familiar.originalId + 1)
                }
            }
            PlayerLists.addPlayerToWilderness()
            DonatorConfigurations.enterWildernessConfigurations(e)
            e.appearance.sync()
        } else if (e is NPC) {
            if (e.definition.hasAttackOption()) {
                if (e.id != 197) { // Rogues
                    e.isAggressive = false
                } else {
                    e.isAggressive = true
                    e.aggressiveHandler = AggressiveHandler(e, AggressiveBehavior.WILDERNESS)
                }
            }
        }
        return true
    }

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (!logout && e is Player) {
            leave(e)
            if (e.familiarManager.hasFamiliar() && !e.familiarManager.hasPet()) {
                val familiar = e.familiarManager.familiar
                if (familiar.isCombatFamiliar) {
                    familiar.reTransform()
                }
            }
            PlayerLists.removePlayerFromWilderness()
            DonatorConfigurations.leaveWildernessConfigurations(e)
            e.appearance.sync()
        }
        return true
    }

    /**
     * Method used to remove traces of being in the zone.
     *
     * @param p the player.
     */
    fun leave(p: Player) {
        val overlay = p.interfaceState.overlay
        if (overlay != null && overlay.id == 197) {
            p.interfaceState.closeOverlay()
        }
        p.interaction.remove(Option._P_ATTACK)
        p.skullManager.isWilderness = false
        p.skullManager.level = 0
    }

    override fun teleport(e: Entity, type: Int, node: Node?): Boolean {
        if (e is Player) {
            if (e.details.rights.isAdministrator) {
                return true
            }
            if (!checkTeleport(e, if (node != null && node is Item && node.name.contains("glory")) 30 else 20)) {
                return false
            }
        }
        return true
    }

    /**
     * Method used to check if a player can teleport past a level.
     *
     * @param p     the player.
     * @param level the level.
     * @return `True` if they can teleport.
     */
    private fun checkTeleport(p: Player, level: Int): Boolean {
        if (p.skullManager.level > level && !p.skullManager.isWildernessDisabled) {
            message(p, "You can't teleport this deep in the wilderness!")
            return false
        }
        return true
    }

    override fun continueAttack(e: Entity, target: Node, style: CombatStyle, message: Boolean): Boolean {
        if (e is Player && target is Player) {
            var level = e.skullManager.level
            if (target.skullManager.level < level) {
                level = target.skullManager.level
            }
            val combat = e.properties.currentCombatLevel - if (e.familiarManager.isUsingSummoning && e.familiarManager.summoningCombatLevel > 0) e.familiarManager.summoningCombatLevel else 0
            val targetCombat = target.properties.currentCombatLevel - if (target.familiarManager.isUsingSummoning && target.familiarManager.summoningCombatLevel > 0) target.familiarManager.summoningCombatLevel else 0
            if (combat - level > targetCombat || combat + level < targetCombat) {
                if (message) {
                    e.actionSender.sendMessage("The level difference between you and your opponent is too great.")
                }
                return false
            }
        }
        return true
    }

    override fun locationUpdate(e: Entity, last: Location) {
        if (e is Player) {
            e.skullManager.level = getWildernessLevel(e)
            val overlay = e.interfaceState.overlay
            if (overlay == null || overlay.id != 197) {
                show(e)
            }
            if (!e.skullManager.isWildernessDisabled) {
                e.actionSender.sendString("Level: " + e.skullManager.level, 199)
            }
        }
    }

    companion object {

        /**
         * The wilderness zone.
         */
        /**
         * Gets the instance.
         *
         * @return The instance.
         */
        val instance = WildernessZone(
            ZoneBorders(2944, 3525, 3400, 3975),
            ZoneBorders(3070, 9924, 3135, 10002),
            ZoneBorders.forRegion(12193),
            ZoneBorders.forRegion(11937)
        )

        /**
         * Method used to show being the wilderness.
         *
         * @param p the player.
         */
        fun show(p: Player) {
            if (p.skullManager.isWildernessDisabled) {
                return
            }
            p.interfaceState.openOverlay(Component(197))
            p.skullManager.level = getWildernessLevel(p)
            p.actionSender.sendString("Level: " + p.skullManager.level, 199)
            p.interaction.set(Option._P_ATTACK)
            p.skullManager.isWilderness = true
        }

        /**
         * Checks if the entity is inside the wilderness.
         *
         * @param e The entity.
         * @return `True` if so.
         */
        fun isInZone(e: Entity): Boolean {
            val l = e.location
            return e.viewport.region.regionZones.any { it.zone === instance && it.borders.insideBorder(l.x, l.y) }
        }

        /**
         * Checks if the location is inside the wilderness.
         *
         * @param location The location.
         * @return `True` if so.
         */
        fun isInZone(location: Location?): Boolean {
            if (location == null) {
                return false
            }
            val region = RegionManager.forId(location.regionId)
            return region.regionZones.any { it.zone === instance && it.borders.insideBorder(location.x, location.y) }
        }

        /**
         * Gets the distance to level 1 in the wilderness.
         *
         * @param e The entity.
         * @return The level.
         */
        fun getDistance(e: Entity): Int {
            /*val regionId = e.viewport.region.id
            var offsetY = 3524
            if (regionId == 12443 || e.viewport.region.id == 12444) {
                offsetY = 9923
            } else if (regionId == 12193) {
                offsetY = 10000 - 80
            } else if (regionId == 11937) {
                offsetY = 9920
            }*/
            return (1 + (e.location.y - 3520) / 8.0).toInt()
            //return (int) Math.ceil((double) (e.getLocation().getY() - offsetY) / 8d);
        }

        /**
         * Gets the wilderness level for the entity.
         *
         * @param e The entity.
         * @return The level.
         */
        fun getWildernessLevel(e: Entity): Int {
            val regionId = e.viewport.region.id
            var offsetY = 3524
            if (regionId == 12443 || e.viewport.region.id == 12444) {
                offsetY = 9923
            } else if (regionId == 12193) {
                offsetY = 10000 - 80
            } else if (regionId == 11937) {
                offsetY = 9920
            }
            val level = Math.ceil((e.location.y - offsetY).toDouble() / 8.0).toInt()
            return when {
                level < 0 -> 1
                else -> level
            }
        }
    }

}
