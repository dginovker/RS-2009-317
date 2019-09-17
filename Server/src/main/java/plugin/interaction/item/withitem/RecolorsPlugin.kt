package plugin.interaction.item.withitem

import org.gielinor.constants.ABYSSAL_WHIP
import org.gielinor.constants.DARK_BOW
import org.gielinor.game.interaction.NodeUsageEvent
import org.gielinor.game.interaction.UseWithHandler
import org.gielinor.game.node.entity.player.info.donor.DonorItem
import org.gielinor.rs2.plugin.Plugin
import org.gielinor.util.extensions.asItem
import org.gielinor.util.labelledAs

class RecolorsPlugin : UseWithHandler {

    constructor() : super()
    constructor(vararg ids: Int) : super(*ids)

    override fun newInstance(arg: Any?): Plugin<Any?> {
        Recolor.values.forEach { _, recolor ->
            addHandler(recolor.recolorMixerId, ITEM_TYPE, RecolorsPlugin(recolor.useWithId))
            addHandler(recolor.useWithId, ITEM_TYPE, RecolorsPlugin(recolor.useWithId))
            addHandler(recolor.product, ITEM_TYPE, RecolorsPlugin(recolor.useWithId))
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        val ornament = Recolor.fromRecolorMixerId(event.usedItem.id) ?: Recolor.fromRecolorMixerId(event.usedWith.id)
        if (ornament != null && event.containsItem(ornament.recolorMixerId) && event.containsItem(ornament.useWithId)) {
            labelledAs("remove items") {
                player.inventory.remove(ornament.recolorMixerId.asItem(), ornament.useWithId.asItem())
            }
            labelledAs("add item(s)") {
                player.inventory.add(ornament.product)
            }
        }
        return true
    }

    private enum class Recolor(val recolorMixerId: Int, val useWithId: Int, val product: Int) {
        GREEN_DARK_BOW(32759, DARK_BOW, 32766),
        WHITE_DARK_BOW(32763, DARK_BOW, 32768),
        YELLOW_DARK_BOW(32761, DARK_BOW, 32767),
        BLUE_DARK_BOW(32757, DARK_BOW, 32765),
        VOLCANIC_ABYSSAL_WHIP(DonorItem.VOLCANIC_WHIP_MIX.itemId, ABYSSAL_WHIP, 32773),
        FROZEN_ABYSSAL_WHIP(DonorItem.FROZEN_WHIP_MIX.itemId, ABYSSAL_WHIP, 32774)
        ;

        companion object {
            val values by lazy { Recolor.values().associateBy(Recolor::recolorMixerId) }
            fun fromRecolorMixerId(type: Int) = values[type]
        }
    }
}