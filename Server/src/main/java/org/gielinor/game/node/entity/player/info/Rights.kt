package org.gielinor.game.node.entity.player.info

import org.gielinor.game.node.entity.player.Player
import java.util.*

/**
 * Represent the rights of a player.
 *
 * @author 'Vexia
 *
 * */
enum class Rights
/**
 * Constructs a new
 * [org.gielinor.game.node.entity.player.info.Rights].
 *
 * @param imageId
 * The image.
 */
private constructor(
    /**
     * The id of the forum member group which represents this rank.
     */
    /**
     * Gets the forum member group id.
     *
     * @return The id.
     */
    val memberGroupId: Int,
    /**
     * The id of the image for this rank.
     */
    /**
     * Gets the id of the image.
     *
     * @return The id.
     */
    val imageId: Int = -1,
    /**
     * Does this status get announced on login?
     */
    var announcementOnLogin: Boolean = false) {

    REGULAR_PLAYER(3),
    SAPPHIRE_MEMBER(8),
    EMERALD_MEMBER(9),
    RUBY_MEMBER(10),
    DIAMOND_MEMBER(16),
    DRAGONSTONE_MEMBER(17),
    ONYX_MEMBER(18),
    ZENYTE_MEMBER(19, announcementOnLogin = true),
    PLAYER_MODERATOR(7, 0),
    GIELINOR_MODERATOR(4, 1, announcementOnLogin = true) {
        override fun isVisible(player: Player): Boolean {
            val visible = player.getAttribute("visible_rank", GIELINOR_MODERATOR) === GIELINOR_MODERATOR
            if (!visible) announcementOnLogin = false
            return visible
        }
    },
    DEVELOPER(12, 32, announcementOnLogin = true);

    /**
     * Checks if the player is a [.PLAYER_MODERATOR].
     *
     * @return `True` if so.
     */
    val isPlayerModerator: Boolean
        get() = this === PLAYER_MODERATOR

    /**
     * Checks if the player is a [.GIELINOR_MODERATOR].
     *
     * @return `True` if so.
     */
    val isAdministrator: Boolean
        get() = this === GIELINOR_MODERATOR || this === DEVELOPER

    /**
     * Checks if the player is a [.GIELINOR_MODERATOR].
     *
     * @return `True` if so.
     */
    val isDeveloper: Boolean
        get() = this === DEVELOPER

    override fun toString(): String {
        return this.name.replace("_", " ").toLowerCase().capitalize()
    }

    /**
     * Checks if the player's rank is visible.
     */
    open fun isVisible(player: Player): Boolean {
        return true
    }

    companion object {

        /**
         * Method used to get the credentials based off the id.
         *
         * @param id
         * the id.
         * @return the credential.
         */
        fun forId(id: Int, vararg secondaryGroupIds: Int): Rights {
            val ids = ArrayList<Int>()
            ids.add(id)
            if (secondaryGroupIds.isNotEmpty()) {
                Arrays.stream(secondaryGroupIds).forEach { rank -> ids.add(rank) }
            }
            for (rankId in ids) {
                Rights.values()
                    .filter { it.memberGroupId == rankId }
                    .forEach { return it }
            }
            return REGULAR_PLAYER
        }

        /**
         * Gets the chat icon.
         *
         * @param player
         * The player to get the chat icon for.
         * @return The chat icon.
         */
        fun getChatIcons(player: Player): IntArray {
            // TODO improve this method
            val temp = intArrayOf(255, 255, 255, 255, 255, 255, 255, 255) // max of 8 icons

            val visibleRank = player.getAttribute("visible_rank", player.details.rights)
            val rightsIcon = player.getAttribute("chat-icon", visibleRank.imageId)

            if (rightsIcon > -1) {
                temp[0] = rightsIcon
            }

            if (player.donorManager.donorStatus.chatIcon != -1 && !player.donorManager.isIconHidden) {
                temp[1] = player.donorManager.donorStatus.chatIcon
            }

            if (player.savedData.globalData.ironmanMode != null) {
                temp[2] = player.savedData.globalData.ironmanMode.crownId
            }

            return temp
        }
    }

}
