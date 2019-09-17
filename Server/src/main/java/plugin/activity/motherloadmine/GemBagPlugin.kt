package plugin.activity.motherloadmine

import org.gielinor.cache.def.impl.ItemDefinition
import org.gielinor.game.interaction.NodeUsageEvent
import org.gielinor.game.interaction.OptionHandler
import org.gielinor.game.interaction.UseWithHandler
import org.gielinor.game.node.Node
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.item.Item
import org.gielinor.rs2.plugin.Plugin

/**
 * Handles the 'Gem bag' item from the Motherload Mine shop
 * @author Corey
 *
 * TODO loading/saving
 * TODO extra op to deposit all to bank when bank interface is open
 */
class GemBagPlugin : OptionHandler() {
    
    companion object {
        const val FILL = "fill"
        const val CHECK = "check"
        const val EMPTY = "empty"
        
        const val GEM_BAG = 32020
        const val MAX_STORAGE = 60
        val GEMS: IntArray = intArrayOf(Item.UNCUT_SAPPHIRE, Item.UNCUT_EMERALD, Item.UNCUT_RUBY, Item.UNCUT_DIAMOND, Item.UNCUT_DRAGONSTONE)
    
        /**
         * If the given item id is a gem (noted or unnoted).
         */
        fun isGem(itemId: Int): Boolean {
            GEMS.forEach {
                if (itemId == it) {
                    return true
                }
                if (itemId == it + 1) {
                    return true
                }
            }
            return false
        }
    }
    
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(GEM_BAG).configurations["option:" + FILL] = this
        ItemDefinition.forId(GEM_BAG).configurations["option:" + CHECK] = this
        ItemDefinition.forId(GEM_BAG).configurations["option:" + EMPTY] = this
        return this
    }
    
    override fun handle(player: Player, node: Node?, option: String): Boolean {
        when (option.toLowerCase()) {
            FILL -> {
                fillBag(player)
                return true
            }
            CHECK -> {
                checkBag(player)
                return true
            }
            EMPTY -> {
                emptyBag(player)
                return true
            }
        }
        return false
    }
    
    /**
     * Fills the bag from the player's inventory as much as possible.
     */
    private fun fillBag(player: Player) {
        val sapphires = player.inventory.getCount(Item.UNCUT_SAPPHIRE) + player.inventory.getCount(Item.UNCUT_SAPPHIRE + 1)
        val emeralds = player.inventory.getCount(Item.UNCUT_EMERALD) + player.inventory.getCount(Item.UNCUT_EMERALD + 1)
        val rubies = player.inventory.getCount(Item.UNCUT_RUBY) + player.inventory.getCount(Item.UNCUT_RUBY + 1)
        val diamonds = player.inventory.getCount(Item.UNCUT_DIAMOND) + player.inventory.getCount(Item.UNCUT_DIAMOND + 1)
        val dragonstones = player.inventory.getCount(Item.UNCUT_DRAGONSTONE) + player.inventory.getCount(Item.UNCUT_DRAGONSTONE + 1)
        
        player.inventory.getById(GEM_BAG).charge = (sapphires or emeralds shl 6 or rubies shl 12 or diamonds shl 18 or dragonstones shl 24)
    
        val amt = (player.inventory.getById(GEM_BAG).charge shr 12 and 0x3F)
        
        player.actionSender.sendMessage("Rubies: " + amt)
        player.actionSender.sendMessage("Rubies should be $rubies")
        player.actionSender.sendMessage("Charge: ${(sapphires or emeralds shl 6 or rubies shl 12 or diamonds shl 18 or dragonstones shl 24)}")
    
        val firstValue = 25811
        val secondValue = 500000
        val packedValue = (firstValue or (secondValue shl 15)).toLong()
    
        val toPrint = packedValue and 0x7FFF
        
        player.actionSender.sendMessage(toPrint.toString())
        
        /*player.inventory.items.forEach { item ->
            if (item == null) {
                return@forEach
            }
            if (!isGem(item.id)) {
                return@forEach
            }
            player.gemBag.add(Item(item.id, player.inventory.getCount(item)))
        }
        player.inventory.refresh()*/
    }
    
    /**
     * Reports back to the user how many of each gem are stored in the bag.
     */
    private fun checkBag(player: Player) {
        val gemBag = player.inventory.getById(GEM_BAG)
        val contents = gemBag.charge
    
        for (i in 0 until GEMS.size) {
            val count = contents.ushr(i * 6) and 0x3F
            if (count > 0) {
                val item = ItemDefinition.forId(GEMS[i])
                player.actionSender.sendMessage("Your gem bag contains $count ${item.name}.")
            }
        }
        
        /*if (player.gemBag.isEmpty) {
            player.actionSender.sendMessage("Your gem bag is empty.")
            return
        }
        player.gemBag.items.filter { it != null }.forEach {
            val name = it.name.toLowerCase() + if (it.count > 1) "s" else ""
            player.actionSender.sendMessage("Your gem bag contains ${it.count} $name.")
        }*/
    }
    
    /**
     * Empties the bag of all it's possible contents in unnoted form.
     */
    private fun emptyBag(player: Player) {
        /*if (player.gemBag.isEmpty) {
            player.actionSender.sendMessage("Your gem bag is already empty.")
            return
        }
        player.gemBag.items.filter { it != null && it.count > 0 }.forEach { _item ->
            val item = _item.copy()
            val maxAdd = player.inventory.getMaximumAdd(item)
            if (maxAdd < 1) {
                player.actionSender.sendMessage("Not all gems could be removed.")
                return
            }
            val amountToAdd = if (item.count > maxAdd) maxAdd else item.count
            val itemToMove = Item(item.id, amountToAdd)
            
            player.gemBag.remove(itemToMove)
            player.inventory.add(itemToMove)
        }
        if (!player.gemBag.isEmpty) {
            player.actionSender.sendMessage("Not all gems could be removed.")
        }*/
    }
    
}

class GemBagUseWithPlugin : UseWithHandler(*GemBagPlugin.GEMS, *GemBagPlugin.GEMS.map { return@map ItemDefinition.forId(it).noteId }.toIntArray()) {
    
    override fun handle(event: NodeUsageEvent): Boolean {
        val gem = event.usedWith as Item
        val player = event.player
        
        //player.gemBag.add(gem)
        return true
    }
    
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(GemBagPlugin.GEM_BAG, ITEM_TYPE, this)
        return this
    }
    
}
