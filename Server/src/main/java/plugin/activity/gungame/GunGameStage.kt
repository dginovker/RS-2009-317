package plugin.activity.gungame

import org.gielinor.game.node.entity.combat.equipment.AutocastSpell
import org.gielinor.game.node.item.Item

/**
 * Highest -> lowest game stages
 */
enum class GunGameStage(val equipment: Array<Item>, val inventory: Array<Item>? = null, val autocastSpell: AutocastSpell? = null) {
    ARMADYL(arrayOf(*Item.getUnnotedItemsInRange(11718, 11722), Item(4212))),
    DHAROKS(arrayOf(*Item.getUnnotedItemsInRange(4716, 4722))),
    INFINITY(arrayOf(*Item.getUnnotedItemsInRange(6916, 6924), Item(6889), Item(6914)),
        arrayOf(Item(554, 1000), Item(556, 1000), Item(565, 1000)), AutocastSpell.FIRE_WAVE),
    RED_DHIDE(arrayOf(Item(2489), Item(2495), Item(2501), Item(861), Item(892, 1000))),
    RUNE(arrayOf(Item(1079), Item(1127), Item(1163), Item(1201), Item(1333))),
    MYSTIC(arrayOf(*Item.getUnnotedItemsInRange(4089, 4097), Item(6912)),
        arrayOf(Item(554, 1000), Item(556, 1000), Item(560, 1000)), AutocastSpell.FIRE_BLAST),
    GREEN_DHIDE(arrayOf(Item(1065), Item(1099), Item(1135), Item(9183), Item(9143, 1000))),
    BLACK(arrayOf(Item(1077), Item(1125), Item(1195), Item(1327), Item(1165))),
    NORMAL_MAGE_ROBES(arrayOf(Item(577), Item(1011), Item(579), Item(1381)),
        arrayOf(Item(554, 1000), Item(558, 1000)), AutocastSpell.FIRE_STRIKE),
    LEATHER(arrayOf(*Item.getUnnotedItemsInRange(1061, 1063), Item(1095), Item(1129), Item(1167), Item(841), Item(882, 1000))),
    BRONZE(arrayOf(Item(1075), Item(1117), Item(1155), Item(1189), Item(1321))),
    ;

    companion object {
        val lastStage: GunGameStage = values()[values().size - 1]
    }
}
