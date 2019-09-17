package plugin.interaction.item.withobject

import org.gielinor.game.interaction.NodeUsageEvent
import org.gielinor.game.interaction.UseWithHandler
import org.gielinor.game.node.`object`.GameObject
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.item.Item
import org.gielinor.parser.item.ItemConfiguration
import org.gielinor.rs2.plugin.Plugin
import plugin.interaction.`object`.BankingPlugin

class QuickBankingPlugin : UseWithHandler(ALL_NODES_ALLOWED) {
    
    override fun newInstance(arg: Any?): Plugin<Any?> {
        val bankObjects = BankingPlugin.BANK_OBJECTS
        bankObjects.forEach {
            addHandler(it, UseWithHandler.OBJECT_TYPE, this)
        }
        return this
    }
    
    override fun handle(event: NodeUsageEvent): Boolean {
        val item = event.used as Item
        val `object` = event.usedWith as GameObject
        val player = event.player
        player.faceLocation(`object`.location)
        if (canBank(player, item) && player.inventory.remove(item)) player.bank.add(item)
        return true
    }
    
    private fun canBank(player: Player, item: Item): Boolean {
        return player.bank.hasRoomFor(item) && item.definition.getConfiguration(ItemConfiguration.BANKABLE, true) && player.bank.canAdd(item)
    }
    
}
