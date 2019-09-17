package plugin.interaction.item.withitem

import org.gielinor.game.interaction.NodeUsageEvent
import org.gielinor.game.interaction.UseWithHandler
import org.gielinor.game.node.item.Item
import org.gielinor.rs2.plugin.Plugin

class CerberusCrystalPlugin : UseWithHandler {

    constructor() : super(33227, 33229, 33231)

    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(33227, ITEM_TYPE, this); // Eternal crystal
        addHandler(33229, ITEM_TYPE, this); // Pegasian crystal
        addHandler(33231, ITEM_TYPE, this); // Primordial crystal
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        var result: Item = if (event.usedItem.id == 33227 && event.usedWith.id == 26920) {
            Item.of(33235)
        } else if (event.usedItem.id == 33229 && event.usedWith.id == 22577) {
            Item.of(33237)
        } else if (event.usedItem.id == 33231 && event.usedWith.id == 11732) {
            Item.of(33239)
        } else {
            // Shouldn't reach this since we only listen for the above crystals.
            return false;
        }
        player.inventory.remove(event.usedItem.id)
        player.inventory.remove(event.usedWith.id)
        player.inventory.add(result)
        player.actionSender.sendMessage("You make some ${result.name.toLowerCase()}.")
        return true
    }

}
