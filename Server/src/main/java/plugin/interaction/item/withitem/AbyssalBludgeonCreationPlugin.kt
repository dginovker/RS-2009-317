package plugin.interaction.item.withitem

import org.gielinor.constants.ABYSSAL_WHIP
import org.gielinor.constants.AbyssalBludgeonItemConstants.ABYSSAL_BLUDGEON
import org.gielinor.constants.AbyssalBludgeonItemConstants.ABYSSAL_DAGGER
import org.gielinor.constants.AbyssalBludgeonItemConstants.ABYSSAL_HEAD
import org.gielinor.constants.AbyssalBludgeonItemConstants.ABYSSAL_ORPHAN
import org.gielinor.constants.AbyssalBludgeonItemConstants.BLUDGEON_AXON
import org.gielinor.constants.AbyssalBludgeonItemConstants.BLUDGEON_CLAW
import org.gielinor.constants.AbyssalBludgeonItemConstants.BLUDGEON_SPINE
import org.gielinor.constants.AbyssalBludgeonItemConstants.JAR_OF_MIASMA
import org.gielinor.constants.AbyssalBludgeonItemConstants.OVERSEERS_BOOK
import org.gielinor.constants.AbyssalBludgeonItemConstants.UNSIRED
import org.gielinor.constants.AbyssalBludgeonObjectConstants.FONT_OF_CONSUMPTION
import org.gielinor.constants.AnimationConstants.BURY_ANIMATION
import org.gielinor.game.interaction.NodeUsageEvent
import org.gielinor.game.interaction.UseWithHandler
import org.gielinor.game.node.entity.player.Player
import org.gielinor.game.node.item.Item
import org.gielinor.game.world.World
import org.gielinor.kotlin.extensions.sendMessage
import org.gielinor.rs2.plugin.Plugin
import org.gielinor.rs2.pulse.Pulse
import org.gielinor.util.extensions.asItem
import org.gielinor.util.labelledConditional
import org.gielinor.utilities.misc.RandomUtil

@Suppress("unused") // Called by reflection
class AbyssalBludgeonCreationPlugin : UseWithHandler {

    constructor() : super()
    constructor(vararg ids: Int) : super(*ids)

    override fun newInstance(arg: Any?): Plugin<Any?> {
        addHandler(BLUDGEON_AXON, ITEM_TYPE, AbyssalBludgeonCreationPlugin(OVERSEERS_BOOK))
        addHandler(BLUDGEON_CLAW, ITEM_TYPE, AbyssalBludgeonCreationPlugin(OVERSEERS_BOOK))
        addHandler(BLUDGEON_SPINE, ITEM_TYPE, AbyssalBludgeonCreationPlugin(OVERSEERS_BOOK))
        addHandler(OVERSEERS_BOOK, ITEM_TYPE, AbyssalBludgeonCreationPlugin(BLUDGEON_AXON, BLUDGEON_CLAW, BLUDGEON_SPINE))
        addHandler(FONT_OF_CONSUMPTION, OBJECT_TYPE, AbyssalBludgeonCreationPlugin(UNSIRED))
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player

        labelledConditional("font of consumption returns random reward and consumes unsired", event.containsItem(UNSIRED)) {
            player.dialogueInterpreter.sendItemMessage(UNSIRED, "You place the Unsired into the Font of Consumption...")
            player.faceLocation(event.used.location)
            player.animate(BURY_ANIMATION)
            player.lock(3)
            World.submit(object: Pulse(2) {
                override fun pulse(): Boolean {
                    // Graphics.send(GREEN_SPLAT, GREEN_SPLAT_LOCATION) // Broken (crashes client)
                    val slot = event.usedItem.slot
                    val lootdrop = generateUnsiredLootdrop(player)
                    player.inventory.remove(event.usedItem, slot, true) // unsired
                    player.inventory.addSlot(lootdrop, true, slot)
                    player.sendMessage("<col=005F00>You received: ${lootdrop.name}.")

                    // TODO handle duplicate pet?
                    if (lootdrop.id == ABYSSAL_ORPHAN)
                        player.dialogueInterpreter.sendItemMessage(lootdrop, "The Font appears to have revitalised the Unsired!")
                    else
                        player.dialogueInterpreter.sendItemMessage(lootdrop, "The Font consumes the Unsired and returns you a", "reward.")
                    return true
                }
            })
        }

        labelledConditional("making abyssal bludgeon with overseers book", event.containsItem(OVERSEERS_BOOK)) {
            if (player.inventory.containItems(BLUDGEON_AXON, BLUDGEON_CLAW, BLUDGEON_SPINE)) {
                player.inventory.remove(BLUDGEON_AXON.asItem(), BLUDGEON_CLAW.asItem(), BLUDGEON_SPINE.asItem())
                player.inventory.add(ABYSSAL_BLUDGEON)
            }
        }

        return true
    }

    private fun generateUnsiredLootdrop(player: Player): Item {
        val roll = RandomUtil.random(128)
        return when {
            roll < 5  -> Item(ABYSSAL_ORPHAN)
            roll < 15 -> Item(ABYSSAL_HEAD)
            roll < 27 -> Item(ABYSSAL_WHIP)
            roll < 40 -> Item(JAR_OF_MIASMA)
            roll < 66 -> Item(ABYSSAL_DAGGER)
            else      -> nextBludgeonPiece(player)
        }
    }

    private fun nextBludgeonPiece(player: Player): Item {
        var result = BLUDGEON_CLAW to Long.MAX_VALUE

        for (id in listOf(BLUDGEON_CLAW, BLUDGEON_SPINE, BLUDGEON_AXON)) {
            val count = player.inventory.getCount(id).toLong() + player.bank.getCount(id).toLong()
            if (count < result.second)
                result = id to count
        }

        return result.first.asItem()
    }

}
