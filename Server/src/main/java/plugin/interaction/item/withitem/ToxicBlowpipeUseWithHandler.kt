package plugin.interaction.item.withitem

import org.gielinor.constants.ToxicBlowpipeConstants
import org.gielinor.game.interaction.NodeUsageEvent
import org.gielinor.game.interaction.UseWithHandler
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.item.Item
import org.gielinor.rs2.plugin.Plugin
import org.gielinor.util.extensions.color
import org.gielinor.util.extensions.toHex
import org.gielinor.util.labelledConditional
import org.gielinor.utilities.string.TextUtils
import java.awt.Color
import java.text.NumberFormat

class ToxicBlowpipeUseWithHandler : UseWithHandler {

    constructor() : super()
    constructor(vararg ids: Int) : super(*ids)

    override fun newInstance(arg: Any?): Plugin<Any?> {
        UseWithHandler.addHandler(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_UNLOADED, UseWithHandler.ITEM_TYPE, ToxicBlowpipeUseWithHandler(ToxicBlowpipeConstants.ZULRAHS_SCALES))
        UseWithHandler.addHandler(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_LOADED, UseWithHandler.ITEM_TYPE, ToxicBlowpipeUseWithHandler(ToxicBlowpipeConstants.ZULRAHS_SCALES, *ToxicBlowpipeConstants.DARTS))
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        labelledConditional("loading scales", event.containsItem(ToxicBlowpipeConstants.ZULRAHS_SCALES)) {
            val scalesAmount = run {
                val scalesInInventory = player.inventory.getCount(ToxicBlowpipeConstants.ZULRAHS_SCALES)
                val totalScales = player.savedData.globalData.blowpipeDartScales + scalesInInventory
                if (totalScales > CAP) CAP else totalScales
            }
            if (player.inventory.remove(Item(ToxicBlowpipeConstants.ZULRAHS_SCALES, scalesAmount))) {
                labelledConditional("add blowpipe if loading empty blowpipe", event.containsItem(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_UNLOADED)) {
                    if (player.inventory.remove(Item(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_UNLOADED))) {
                        player.inventory.add(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_LOADED)
                    }
                }
                player.savedData.globalData.blowpipeDartScales = scalesAmount
                player.actionSender.sendMessage(getMessage(player))
                return true
            }
        }
        labelledConditional("loading darts", event.containsItem(ToxicBlowpipeConstants.TOXIC_BLOWPIPE_LOADED)) {
            val dart = run {
                var dart: Int? = null
                ToxicBlowpipeConstants.DARTS.forEach { if (player.inventory.contains(it)) dart = it }
                dart
            }
            val dartAmount = dart?.let {
                val dartsInInventory = player.inventory.getCount(it)
                val totalDarts = player.savedData.globalData.blowpipeDartAmount + dartsInInventory
                if (totalDarts > CAP) if (CAP - player.savedData.globalData.blowpipeDartAmount > 0) CAP - player.savedData.globalData.blowpipeDartAmount else null else totalDarts - player.savedData.globalData.blowpipeDartAmount
            }
            dart?.let {
                dartAmount?.let { dartAmount ->
                    if (it != player.savedData.globalData.blowpipeDartId && player.savedData.globalData.blowpipeDartAmount > 0) {
                        player.actionSender.sendMessage("You can only add the dart type that is already in the blowpipe.")
                    } else {
                        if (player.inventory.remove(Item(it, dartAmount))) {
                            player.savedData.globalData.blowpipeDartId = it
                            player.savedData.globalData.blowpipeDartAmount += dartAmount
                            player.actionSender.sendMessage(getMessage(player))
                            return true
                        }
                    }
                }
            }
        }
        return true
    }

    companion object {
        private val CAP = 16383

        fun getMessage(player: Player): String {
            val dartName = if (player.savedData.globalData.blowpipeDartId > 0) Item(player.savedData.globalData.blowpipeDartId).definition.name else "None"
            val dartAmount = let {
                if (dartName != "None") " x ${TextUtils.getFormattedNumber(player.savedData.globalData.blowpipeDartAmount)}" else ""
            }
            val nf = NumberFormat.getInstance()
            nf.maximumFractionDigits = 1
            val scalesPercentage = nf.format((player.savedData.globalData.blowpipeDartScales.toDouble() / CAP) * 100)
            player.actionSender.sendMessage("test".color(Color.GREEN.toHex()))
            return "Darts: ${"$dartName$dartAmount".color(Color.GREEN.toHex())}. Scales: ${"$scalesPercentage%".color(Color.GREEN.toHex())}"
        }
    }
}
