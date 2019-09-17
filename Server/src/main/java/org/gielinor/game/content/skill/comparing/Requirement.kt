package org.gielinor.game.content.skill.comparing

/**
 * Represents a singular skill requirement, good for things that require varargs for multiple requirements.
 * @author Arham 4
 */
class Requirement(val skill: Int, val level: Int) {
    companion object {
        /**
         * Makes multiple requirements. This is useful for cases like, I want to make a requirement of 99 for all
         * non-combat skills, so I'd do generateMultipleRequirements(skills=7..22, level=99).
         */
        fun generateMultipleRequirements(level: Int, skills: IntRange): Array<Requirement> {
            val requirementArrayList = skills.map { Requirement(it, level) }
            return requirementArrayList.toTypedArray()
        }
    }
}
