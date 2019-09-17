package plugin.activity.motherloadmine

import org.gielinor.cache.def.impl.ItemDefinition
import org.gielinor.game.interaction.OptionHandler
import org.gielinor.game.node.Node
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.item.ChanceItem
import org.gielinor.game.node.item.Item
import org.gielinor.rs2.plugin.Plugin
import org.gielinor.utilities.misc.RandomUtil

/**
 * Handles the 'Bag full of gems' item from the Motherload Mine shop
 * @author Corey
 */
class BagFullOfGemsPlugin : OptionHandler() {
    
    companion object {
        const val BAG_FULL_OF_GEMS = 39473
        const val NUMBER_OF_GEMS = 40
    }
    
    enum class GemDropTable(val gemId: Int, val chance: Double) {
        SAPPHIRE(Item.UNCUT_SAPPHIRE, 496000.0),
        EMERALD(Item.UNCUT_EMERALD, 348000.0),
        RUBY(Item.UNCUT_RUBY, 120000.0),
        DIAMOND(Item.UNCUT_DIAMOND, 31000.0),
        DRAGONSTONE(Item.UNCUT_DRAGONSTONE, 4500.0),
        ONYX(Item.UNCUT_ONYX, 20.0),
        ZENYTE(Item.UNCUT_ZENYTE, 1.0);
    }
    
    override fun handle(player: Player, node: Node?, option: String?): Boolean {
        if (canOpen(player) && player.removeItem(Item(BAG_FULL_OF_GEMS))) {
            val gemArray = GemDropTable.values().map { return@map ChanceItem(ItemDefinition.forId(it.gemId).noteId, 1, it.chance) }.toTypedArray()
            val drops = HashMap<Int, Int>()
            
            repeat(times = NUMBER_OF_GEMS) {
                val gem = RandomUtil.getChanceItem(gemArray).randomItem
                if (!drops.containsKey(gem.id)) {
                    drops[gem.id] = 1
                } else {
                    drops.replace(gem.id, (drops.getValue(gem.id) + 1))
                }
            }
            
            drops.forEach {
                player.inventory.add(Item(it.key, it.value))
            }
            
            player.actionSender.sendMessage("You open the bag to find $NUMBER_OF_GEMS assorted gems inside.")
            
        }
        return true
    }
    
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(BAG_FULL_OF_GEMS).configurations["option:open"] = this
        return this
    }
    
    private fun canOpen(player: Player): Boolean {
        val minSpaceRequired = GemDropTable.values().size - 1 // -1 because the gem bag will get removed
        if (player.inventory.freeSlots() < minSpaceRequired) {
            player.actionSender.sendMessage("You need at least $minSpaceRequired free inventory slots to do this.")
            return false
        }
        return true
    }
}
