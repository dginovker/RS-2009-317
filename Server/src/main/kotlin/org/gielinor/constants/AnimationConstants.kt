package org.gielinor.constants

import org.gielinor.game.node.entity.impl.Animator
import org.gielinor.game.world.update.flag.context.Animation

object AnimationConstants {
    val PRAY_ANIMATION = Animation(645)
    val BURY_ANIMATION = Animation(827)
}

object AbyssalBludgeonAnimationConstants {
    val SPECIAL_ANIMATION = Animation(-1, Animator.Priority.HIGH) // TODO find animation
}

object ArmadylCrossbowAnimationConstants {
    val SPECIAL_ANIMATION = Animation(24230, Animator.Priority.HIGH)
}

object StaffOfTheDeadAnimationConstants {
    val SPECIAL_ANIMATION = Animation(30518, 10, Animator.Priority.HIGH)
}