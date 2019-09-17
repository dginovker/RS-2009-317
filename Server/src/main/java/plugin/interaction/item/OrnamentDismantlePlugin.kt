package plugin.interaction.item

import org.gielinor.cache.def.impl.ItemDefinition
import org.gielinor.game.interaction.OptionHandler
import org.gielinor.game.node.Node
import org.gielinor.game.node.entity.player.Player
import org.gielinor.rs2.plugin.Plugin
import plugin.interaction.item.withitem.OrnamentsPlugin.Ornament
import java.util.*

/**
 * Handles dismantling/reverting items with an (or) kit
 *
 * @author Corey
 */
class OrnamentDismantlePlugin : OptionHandler() {

    private val options = Arrays.asList("revert", "dismantle")

    override fun newInstance(arg: Any?): Plugin<Any?> {
        Ornament.values().forEach { ornament ->
            options.forEach { op ->
                ItemDefinition.forId(ornament.product).configurations.put("option:" + op, this)
            }
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (options.contains(option)) {
            val productId = node.id
            val ornament = Ornament.forProduct(productId)

            if (ornament != null) {
                if (player.inventory.freeSlots() >= 1) {
                    player.inventory.remove(productId)
                    player.inventory.add(ornament.kitId)
                    player.inventory.add(ornament.useWithId)
                }
            }

            return true
        }
        return false
    }

    override fun isWalk(): Boolean {
        return false
    }

}
