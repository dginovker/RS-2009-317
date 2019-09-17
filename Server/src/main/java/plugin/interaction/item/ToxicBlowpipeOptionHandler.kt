package plugin.interaction.item

import org.gielinor.cache.def.impl.ItemDefinition
import org.gielinor.constants.ToxicBlowpipeConstants
import org.gielinor.game.interaction.OptionHandler
import org.gielinor.game.node.Node
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.item.Item
import org.gielinor.rs2.plugin.Plugin
import plugin.interaction.item.withitem.ToxicBlowpipeUseWithHandler

/**
 * Handles the Check / Dissolve option on an Abyssal tentacle.
 *
 * @author [Gielinor Logan G.](https://Gielinor.org)
 */
class ToxicBlowpipeOptionHandler : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any?> {
        ItemDefinition.forId(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_LOADED).configurations.put("option:check", this)
        ItemDefinition.forId(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_LOADED).configurations.put("option:unload", this)
        ItemDefinition.forId(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_LOADED).configurations.put("option:uncharge", this)
        ItemDefinition.forId(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_UNLOADED).configurations.put("option:dismantle", this)
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val dartsToAdd = Item(player.savedData.globalData.blowpipeDartId, player.savedData.globalData.blowpipeDartAmount)
        fun resetDartData() {
            player.savedData.globalData.blowpipeDartAmount = -1
            player.savedData.globalData.blowpipeDartId = -1
        }

        fun resetScaleData() {
            player.savedData.globalData.blowpipeDartScales = -1
        }
        when (option) {
            "check" -> player.actionSender.sendMessage(ToxicBlowpipeUseWithHandler.getMessage(player))
            "unload" -> {
                val scalesToAdd = Item(ToxicBlowpipeConstants.ZULRAHS_SCALES, player.savedData.globalData.blowpipeDartScales)
                val requiredFreeSlots = if (player.savedData.globalData.blowpipeDartAmount > 0) 3 else 2
                if (player.inventory.freeSlots() >= requiredFreeSlots && player.inventory.remove(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_LOADED)) {
                    player.inventory.add(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_UNLOADED)
                    player.inventory.add(scalesToAdd)
                    if (player.savedData.globalData.blowpipeDartAmount > 0) player.inventory.add(dartsToAdd)
                    resetDartData()
                    resetScaleData()
                }
            }
            "uncharge" -> {
                if (player.inventory.add(dartsToAdd)) {
                    resetDartData()
                    player.actionSender.sendMessage(ToxicBlowpipeUseWithHandler.getMessage(player))
                }
            }
            "dismantle" -> if (player.inventory.remove(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_UNLOADED)) player.inventory.add(Item(ToxicBlowpipeConstants.ZULRAHS_SCALES, 20000))
        }
        return false
    }

    override fun isWalk(): Boolean {
        return false
    }
}

