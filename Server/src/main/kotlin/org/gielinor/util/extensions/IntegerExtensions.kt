package org.gielinor.util.extensions

import org.gielinor.game.node.item.Item

/**
 * Got annoyed doing Item(itemId) instead of doing itemId.asItem()
 */
fun Int.asItem(): Item = Item(this)