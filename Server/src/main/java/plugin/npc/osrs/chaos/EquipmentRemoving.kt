package plugin.npc.osrs.chaos

import org.gielinor.game.node.entity.Entity
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.item.Item
import org.gielinor.utilities.misc.RandomUtil

object EquipmentRemoving {
    fun strip(entity: Entity) {
        if (entity is Player) {
            if (entity.inventory.freeSlots() < 1 || entity.equipment.itemCount() < 1) {
                return
            }
            var e: Item? = null
            var tries = 0
            while (e == null && tries < 30) {
                e = entity.equipment.toArray()[RandomUtil.random(entity.equipment.itemCount())]
                tries++
                if (e != null && entity.inventory.hasRoomFor(e)) {
                    break
                }
                e = null
            }
            if (e == null) {
                return
            }
            entity.lock(1)
            if (!entity.equipment.containsItem(e)) {
                return
            }
            if (entity.equipment.remove(e)) {
                entity.inventory.add(e)
            }
        }
    }
}
