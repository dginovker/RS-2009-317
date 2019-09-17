package plugin.npc.osrs.chaos.fanatic

import org.gielinor.game.node.entity.combat.CombatSwingHandler
import org.gielinor.game.node.entity.npc.AbstractNPC
import org.gielinor.game.node.entity.npc.agg.AggressiveBehavior
import org.gielinor.game.node.entity.npc.agg.AggressiveHandler
import org.gielinor.game.world.map.Location
import org.gielinor.rs2.plugin.Plugin

class ChaosFanaticNPC : AbstractNPC {

    constructor() : super(-1, null) {
        super.setAggressive(true)
    }

    constructor(id: Int, location: Location) : super(id, location)

    override fun getIds(): IntArray {
        return intArrayOf(26619)
    }

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return ChaosFanaticNPC(id, location)
    }

    override fun setDefaultBehavior() {
        isAggressive = true
        aggressiveHandler = AggressiveHandler(this, AggressiveBehavior.DEFAULT)
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
        return ChaosFanaticCombatSwingHandler()
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        init()
        return super.newInstance(arg)
    }
}
