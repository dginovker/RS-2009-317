package org.gielinor.test

import org.gielinor.game.node.item.Item
import org.gielinor.rs2.model.container.Container
import org.junit.Assert.*
import org.junit.Test

class ContainerTest {

    companion object {
        internal const val SHARK = 385
    }

    @Test
    fun `container return leftovers`() {
        val container = Container(3)
        assertTrue(container.addAndReturnResult(Item(SHARK)).success())
        assertEquals(container.addAndReturnResult(Item(SHARK)).succeeded, 1)
        assertEquals(container.addAndReturnResult(Item(SHARK, 3)).succeeded, 1)
        assertEquals(container.addAndReturnResult(Item(SHARK)).succeeded, 0)
    }

}
