package org.gielinor.kotlin.extensions

import org.gielinor.game.node.item.Item
import org.gielinor.rs2.model.container.Container

/** Add item to inventory; with callback with the leftover itemstack. */
inline fun Container.add(item: Item, onFull: (Item) -> Unit) {
    val result = addAndReturnResult(item)
    if (result.failed())
        onFull(Item(item.id, result.requested - result.succeeded, item.charge))
}
