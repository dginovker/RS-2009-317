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
 * Handles the 'Coal bag' item from the Motherload Mine shop
 * @author Corey
 *
 * TODO act as extended inventory when smelting
 * TODO extra op to deposit all to bank when bank interface is open
 */
class CoalBagPlugin : OptionHandler() {
    
    companion object {
        const val FILL = "fill"
        const val CHECK = "check"
        const val EMPTY = "empty"
    
        /**
         * The coal bag item id.
         */
        const val COAL_BAG = 32019
    
        /**
         * The maximum amount of coal allowed to be stored in a coal bag.
         */
        private const val MAX_STORAGE = 27
    
        /**
         * Valid coal items.
         */
        val COAL: IntArray = intArrayOf(Item.COAL, ItemDefinition.forId(Item.COAL).noteId)
        
        /**
         * If the given item id is coal (noted or unnoted).
         */
        fun isCoal(itemId: Int): Boolean {
            return COAL.contains(itemId)
        }
    
        /**
         * Add a given item to the coal bag.
         *
         * Optional inventory slot argument.
         */
        fun addCoal(player: Player, coal: Item, slot: Int = coal.slot) {
            if (!player.inventory.contains(COAL_BAG)) {
                throw Exception("Player ${player.pidn} does not have a coal bag in their inventory!")
            }
            if (!isCoal(coal.id)) {
                return
            }
            if (!hasSpace(player)) {
                player.actionSender.sendMessage("Your coal bag is too full to store any more coal.")
                return
            }
            
            val amount = if (coal.count > spaceRemaining(player))
                spaceRemaining(player)
            else
                coal.count
            
            player.inventory.remove(Item(coal.id, amount), slot, true)
    
            getCoalBag(player).charge += amount
        }
    
        /**
         * If the player has at least 1 free space in their coal bag.
         */
        fun hasSpace(player: Player): Boolean {
            return spaceRemaining(player) > 0
        }
    
        /**
         * How much coal is in the player's coal bag.
         */
        fun spaceUsed(player: Player): Int {
            return getCoalBag(player).charge
        }
    
        /**
         * Whether or not the player's coal bag is empty or not.
         */
        fun isEmpty(player: Player): Boolean {
            return spaceRemaining(player) == MAX_STORAGE
        }
    
        /**
         * Get the coal bag item from the player's inventory.
         */
        fun getCoalBag(player: Player): Item {
            return player.inventory.getById(COAL_BAG)
        }
    
        /**
         * How much space there is left in the player's coal bag.
         */
        private fun spaceRemaining(player: Player): Int {
            return MAX_STORAGE - spaceUsed(player)
        }
    }
    
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(COAL_BAG).configurations["option:" + FILL] = this
        ItemDefinition.forId(COAL_BAG).configurations["option:" + CHECK] = this
        ItemDefinition.forId(COAL_BAG).configurations["option:" + EMPTY] = this
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
        if (!hasSpace(player)) {
            player.actionSender.sendMessage("Your coal bag is too full to store any more coal.")
            return
        }
        player.inventory.items.forEach { item ->
            if (item == null) {
                return@forEach
            }
            if (!isCoal(item.id)) {
                return@forEach
            }
            if (!hasSpace(player)) {
                return@forEach
            }
            addCoal(player, item)
        }
        player.inventory.refresh()
    }
    
    /**
     * Reports back to the user much coal is stored in the bag.
     */
    private fun checkBag(player: Player) {
        fun quantity(): String {
            val amount = spaceUsed(player)
            return amount.toString() + " piece" + if (amount == 1) "" else "s"
        }
        
        when (hasSpace(player)) {
            true -> if (isEmpty(player))
                player.actionSender.sendMessage("Your coal bag is empty.")
            else
                player.actionSender.sendMessage("Your coal bag contains ${quantity()} of coal.")
            false -> player.actionSender.sendMessage("Your coal bag is full.")
        }
    }
    
    /**
     * Empties the bag of all it's possible contents in unnoted form.
     */
    private fun emptyBag(player: Player) {
        if (isEmpty(player)) {
            player.actionSender.sendMessage("Your coal bag is already empty.")
            return
        }
        if (player.inventory.freeSlots() <= 0) {
            player.actionSender.sendMessage("You don't have enough space in your inventory to do this.")
            return
        }
        val freeSlots = player.inventory.freeSlots()
        val amountToEmpty = if (freeSlots > spaceUsed(player)) spaceUsed(player) else freeSlots
    
        getCoalBag(player).charge -= amountToEmpty
        player.inventory.add(Item(Item.COAL, amountToEmpty))
        player.inventory.refresh()
    }
    
}

class CoalBagUseWithPlugin : UseWithHandler(*CoalBagPlugin.COAL) {
    
    override fun handle(event: NodeUsageEvent): Boolean {
        val coal = event.usedWith as Item
        val player = event.player
        
        CoalBagPlugin.addCoal(player, coal, coal.slot)
        return true
    }
    
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(CoalBagPlugin.COAL_BAG, ITEM_TYPE, this)
        return this
    }
    
}
