package plugin.interaction.item.withnpc

import org.gielinor.game.node.item.Item.COINS
import org.gielinor.constants.MembersBankNotingItemConstants.NOTEABALE_ITEMS
import org.gielinor.constants.MembersBankNotingNpcConstants.MEMBERS_BANK_NOTING_NPC_ID
import org.gielinor.game.interaction.NodeUsageEvent
import org.gielinor.game.interaction.UseWithHandler
import org.gielinor.game.node.entity.npc.NPC
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.item.Item
import org.gielinor.rs2.plugin.Plugin
import org.gielinor.util.extensions.asItem
import org.gielinor.util.labelledAs
import org.gielinor.util.labelledConditional

class MembersBankNotingPlugin : UseWithHandler {

    constructor(itemId: Int) : super(itemId)

    override fun newInstance(arg: Any?): Plugin<Any> {
        for (noteableItem in NOTEABALE_ITEMS) {
            UseWithHandler.addHandler(MEMBERS_BANK_NOTING_NPC_ID, UseWithHandler.NPC_TYPE, MembersBankNotingPlugin(noteableItem))
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        val npc = event.usedWith as NPC

        npc.face(player)
        /**
         * I started seeing a usage of a non-static system and using companion objects from this. In Java, I couldn't make
         * note() static because then I would need to have handle() as static to call it. I find it need I don't need to be
         * verbose and do "MembersBankNotingPlugin.note(player)" and instead can directly call it.
         *
         * Still find companion objects annoying though.
         */
        note(player)
        return true
    }

    companion object {
        private val COST = 10000
        fun note(player: Player) {
            if (hasRequirements(player)) {
                val items = NOTEABALE_ITEMS.filter { player.hasItem(it.asItem()) } // makes a list of noteable items the player has
                labelledConditional("starts the noting process if the above list isn't empty", items.isNotEmpty()) {
                    val items1 = arrayOfNulls<Item>(items.size) // added for aesthetics to make it look pretty when adding items back
                    labelledAs("take the money") {
                        player.inventory.remove(Item(COINS, COST))
                    }
                    labelledAs("loop each item and remove them, then add the removed items noted to an array") {
                        items.forEachIndexed { index, item ->
                            val itemCount = player.inventory.getCount(item)

                            player.inventory.remove(Item(item, itemCount))
                            items1[index] = Item(item + 1, itemCount)
                        }
                    }
                    labelledAs("add the noted items back") {
                        items1.forEach { player.inventory.add(it) }
                    }
                }
            } else {
                labelledAs("what to do if the player doesn't have money") {
                    player.actionSender.sendMessage("You don't have money to do that!")
                }
            }
        }

        fun hasRequirements(player: Player): Boolean {
            return player.inventory.contains(Item(COINS, COST))
        }
    }

    override fun isWalks(): Boolean {
        return false
    }
}
