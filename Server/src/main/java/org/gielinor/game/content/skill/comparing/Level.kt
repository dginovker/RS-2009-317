package org.gielinor.game.content.skill.comparing

/**
 * Represents a singular level for a requirement
 * @author Arham 4
 */
class Level(val skill: Int, val level: Int) {
    override fun equals(other: Any?): Boolean {
        if (other is Requirement) {
            if (other.skill == skill && other.level == level) {
                return true
            }
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return 31 * skill + level
    }
}
