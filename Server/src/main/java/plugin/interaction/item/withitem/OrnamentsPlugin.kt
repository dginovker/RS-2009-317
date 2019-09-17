package plugin.interaction.item.withitem

import org.gielinor.game.interaction.NodeUsageEvent
import org.gielinor.game.interaction.UseWithHandler
import org.gielinor.game.node.entity.player.info.donor.DonorItem
import org.gielinor.rs2.plugin.Plugin
import org.gielinor.util.extensions.asItem
import org.gielinor.util.labelledAs
import org.gielinor.utilities.voting.VoteReward

class OrnamentsPlugin : UseWithHandler {

    constructor() : super()
    constructor(vararg ids: Int) : super(*ids)

    override fun newInstance(arg: Any?): Plugin<Any?> {
        Ornament.values.forEach { ornament ->
            addHandler(ornament.kitId, ITEM_TYPE, OrnamentsPlugin(ornament.useWithId))
            addHandler(ornament.useWithId, ITEM_TYPE, OrnamentsPlugin(ornament.useWithId))
            addHandler(ornament.product, ITEM_TYPE, OrnamentsPlugin(ornament.useWithId))
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        val ornaments = Ornament.getOrnament(event.usedItem.id, event.usedWith.id) ?: Ornament.getOrnament(event.usedWith.id, event.usedItem.id)
        if (ornaments != null && event.containsItem(ornaments.kitId) && event.containsItem(ornaments.useWithId)) {
            labelledAs("remove items") {
                player.inventory.remove(ornaments.kitId.asItem(), ornaments.useWithId.asItem())
            }
            labelledAs("add item(s)") {
                player.inventory.add(ornaments.product)
            }
        }
        return true
    }

    enum class Ornament(val kitId: Int, val useWithId: Int, val product: Int) {
        DRAGON_FULL_HELM(VoteReward.DRAGON_FULL_HELM_ORNAMENT_KIT.item.id, 11335, 32417),
        DRAGON_PLATESKIRT(VoteReward.DRAGON_LEGS_SKIRT_ORNAMENT_KIT.item.id, 4585, 32416),
        DRAGON_PLATELEGS(VoteReward.DRAGON_LEGS_SKIRT_ORNAMENT_KIT.item.id, 4087, 32415),
        DRAGON_CHAINBODY(VoteReward.DRAGON_CHAINBODY_ORNAMENT_KIT.item.id, 2513, 32414),
        DRAGON_SQUARE_SHIELD(VoteReward.DRAGON_SQ_SHIELD_ORNAMENT_KIT.item.id, 1187, 32418),
        DRAGON_SCIMITAR(VoteReward.DRAGON_SCIMITAR_ORNAMENT_KIT.item.id, 4587, 40000),
        ARMADYL_GODSWORD(40068, 11694, 40368),
        BANDOS_GODSWORD(40071, 11696, 40370),
        SARADOMIN_GODSWORD(40074, 11698, 40372),
        ZAMORAK_GODSWORD(40077, 11700, 40374),
        DRAGON_PICKAXE(DonorItem.DRAGON_PICKAXE_UPGRADE_KIT.itemId, 15259, 12797),
        ODIUM_WARD(DonorItem.WARD_UPGRADE_KIT.itemId, 31926, 32807),
        MALEDICTION_WARD(DonorItem.WARD_UPGRADE_KIT.itemId, 31924, 32806),
        DRAGON_DEFENDER(40143, 32954, 39722),
        OCCULT_NECKLACE(40065, 32002, 39720),
        AMULET_OF_TORTURE(40062, 39553, 40366),
        AMULET_OF_FURY(32526, 26585, 32436),
        MYSTIC_STEAM_STAFF(32798, 31789, 32796),
        STEAM_BATTLESTAFF(32798, 31787, 32795),
        MYSTIC_LAVA_STAFF(41202, 23054, 41200),
        LAVA_BATTLESTAFF(41202, 23053, 41198),
        GRANITE_MAUL(32849, 4153, 32848);

        companion object {
            val values by lazy { Ornament.values() }
            fun getOrnament(kitId: Int, useWithId: Int): Ornament? {
                values.forEach {
                    if (it.kitId == kitId && it.useWithId == useWithId) {
                        return it
                    }
                }
                return null
            }

            fun forProduct(productId: Int): Ornament? {
                values.forEach {
                    if (it.product == productId) {
                        return it
                    }
                }
                return null
            }
        }
    }
}
