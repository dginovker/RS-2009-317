package org.gielinor.game.node.entity.player.info.donor

import org.gielinor.game.node.item.Item
import org.gielinor.util.extensions.toHex
import java.awt.Color

/**
 * Represents a Donor status.
 *
 * @author [Gielinor Logan G.](https://Gielinor.org)
 */
enum class DonorStatus(
    /**
     * The id of this status.
     */
    /**
     * Gets the id of this status.
     *
     * @return The id.
     */
    val id: Int,
    /**
     * The forum user group id
     */
    /**
     * Get the user group id.
     * @return The group id.
     */
    val memberGroupId: Int,
    /**
     * The chat icon.
     */
    /**
     * Gets the chat icon.
     *
     * @return The chat icon.
     */
    val chatIcon: Int,
    /**
     * The bank spaces.
     */
    /**
     * Gets the bank spaces.
     *
     * @return The bank spaces.
     */
    val bankSpaces: Int,
    /**
     * The id of the item.
     */
    /**
     * Gets the id of the item.
     *
     * @return The id.
     */
    val itemId: Int,
    /**
     * The color of this status.
     */
    /**
     * Gets the color of this status.
     *
     * @return The color.
     */
    val color: String?,
    /**
     * The fight wave ID (-1) this status enters at (upon completion of one previously).
     */
    val waveId: Int,
    /**
     * The cost to buy a yell tag.
     */
    val yellTagCost: Int,
    /**
     * The (decimal) modifier for this status.
     * 100 - desired percentage
     * i.e. 2% = 100 - 0.02 = 0.98
     */
    val specialRegenerationModifier: Double,
    /**
     * The increased chance in thieving.
     * 100 for 100%
     */
    val thievingSuccessModifier: Int,
    /**
     * The (decimal) pet insurance discount for this status.
     * 100 - desired percentage
     * i.e. 2% = 100 - 0.02 = 0.98
     */
    val petInsuranceDiscount: Double,
    /**
     * Chance of all the logs burning.
     * 0 - 100 (chosen from a random of 100)
     * i.e. 10% would be 10
     */
    val burnAllLogsChance: Int,
    /**
     * If this status has poison immunity at all times besides wilderness.
     */
    val poisonImmunity: Boolean,
    /**
     * If this status can decant with Bob Barter.
     */
    val canDecant: Boolean,
    /**
     * Chance of all the food cooking.
     * 0 - 100 (chosen from a random of 100)
     * i.e. 10% would be 10
     */
    val cookAllFoodChance: Int,
    /**
     * Bonus Slayer points (does not stack with task streaks)
     * percent + 1
     * i.e. 15% would be 1.15
     */
    val bonusSlayerPointsMultiplier: Double,
    /**
     * The amount of hours to wait for ::reward in Discord.
     */
    val rewardCommandCooldown: Int,
    /**
     * No chance of iron being impure for this status?
     */
    val noImpurityChance: Boolean,
    /**
     * Can this player skip a task (only once every day)?
     */
    val canResetTask: Boolean,
    /**
     * Can this player block a task (only once every day)?
     */
    val canBlockTask: Boolean,
    /**
     * The amount of times this status can reset a task for free per day.
     * Integer.MAX_INT for infinity.
     */
    //        val resetTaskAmountDaily: Int,
    /**
     * The amount of times this status can block a task for free per day.
     */
    //        val blockTaskAmountDaily: Int,
    /**
     * Automatically enchant jewellery?
     */
    val autoEnchantJewellery: Boolean,
    /**
     * Gun game discount (once a week)
     * 100 - desired percentage
     * i.e. 2% = 100 - 0.02 = 0.98
     */
    val gunGameDiscount: Double,
    /**
     * Chance of all dragonfire immunity.
     * 0 - 100 (chosen from a random of 100)
     * i.e. 10% would be 10
     */
    val dragonImmunityChance: Int,
    /**
     * Chance of getting double resources.
     * 0 - 100 (chosen from a random of 100)
     * i.e. 10% would be 10
     */
    val doubleResourceChance: Int,
    /**
     * Can this status have unlimited run energy?
     */
    val infiniteRunEnergy: Boolean,
    /**
     * The max amount of cannonballs that can be loaded on the DMC.
     */
    val maxCannonballs: Int,
    /**
     * If this status can have their cannon auto reloaded if they are nearby.
     */
    val autoReloadCannon: Boolean,
    /**
     * The amount of extra items to keep on death for PvM.
     */
    val itemsKeptOnDeathPVM: Int,
    /**
     * The amount of extra items to keep on death for PvP.
     */
    val itemsKeptOnDeathPVP: Int,
    /**
     * Bonus health for food (does not work in PvP)
     * Percent + 1
     * i.e. 15% would be 1.15
     */
    val healingMultiplier: Double,
    /**
     * Does this status need tools for skills?
     */
    val toolsRequired: Boolean) {
    NONE(id = 1, memberGroupId = -1, chatIcon = -1, bankSpaces = 800, itemId = -1, color = null, waveId = 0, yellTagCost = 0, specialRegenerationModifier = 1.0, thievingSuccessModifier = 0, petInsuranceDiscount = 1.0, burnAllLogsChance = -1, poisonImmunity = false, canDecant = false, cookAllFoodChance = -1, bonusSlayerPointsMultiplier = 1.0, rewardCommandCooldown = 24, noImpurityChance = false, canResetTask = false, canBlockTask = false, autoEnchantJewellery = false, dragonImmunityChance = -1, gunGameDiscount = 1.0, doubleResourceChance = -1, infiniteRunEnergy = false, maxCannonballs = 30, autoReloadCannon = false, itemsKeptOnDeathPVM = 0, itemsKeptOnDeathPVP = 0, healingMultiplier = 1.0, toolsRequired = true),
    SAPPHIRE_MEMBER(id = 2, memberGroupId = 8, chatIcon = 25, bankSpaces = 810, itemId = Item.DONOR_STATUS, color = "0f52ba", waveId = 4, yellTagCost = 150000000, specialRegenerationModifier = 0.98, thievingSuccessModifier = 1, petInsuranceDiscount = 0.95, burnAllLogsChance = -1, poisonImmunity = false, canDecant = false, cookAllFoodChance = -1, bonusSlayerPointsMultiplier = 1.0, rewardCommandCooldown = 24, noImpurityChance = false, canResetTask = false, canBlockTask = false, autoEnchantJewellery = false, dragonImmunityChance = -1, gunGameDiscount = 1.0, doubleResourceChance = -1, infiniteRunEnergy = false, maxCannonballs = 30, autoReloadCannon = false, itemsKeptOnDeathPVM = 0, itemsKeptOnDeathPVP = 0, healingMultiplier = 1.0, toolsRequired = true),
    EMERALD_MEMBER(id = 3, memberGroupId = 9, chatIcon = 26, bankSpaces = 820, itemId = Item.DONOR_STATUS, color = "13f04a", waveId = 9, yellTagCost = 100000000, specialRegenerationModifier = 0.95, thievingSuccessModifier = 5, petInsuranceDiscount = 0.9, burnAllLogsChance = 10, poisonImmunity = true, canDecant = true, cookAllFoodChance = -1, bonusSlayerPointsMultiplier = 1.0, rewardCommandCooldown = 24, noImpurityChance = false, canResetTask = false, canBlockTask = false, autoEnchantJewellery = false, dragonImmunityChance = -1, gunGameDiscount = 1.0, doubleResourceChance = -1, infiniteRunEnergy = false, maxCannonballs = 30, autoReloadCannon = false, itemsKeptOnDeathPVM = 0, itemsKeptOnDeathPVP = 0, healingMultiplier = 1.0, toolsRequired = true),
    RUBY_MEMBER(id = 4, memberGroupId = 10, chatIcon = 27, bankSpaces = 850, itemId = Item.DONOR_STATUS, color = Color.RED.toHex(), waveId = 14, yellTagCost = 50000000, specialRegenerationModifier = 0.9, thievingSuccessModifier = 10, petInsuranceDiscount = 0.85, burnAllLogsChance = 15, poisonImmunity = true, canDecant = true, cookAllFoodChance = 10, bonusSlayerPointsMultiplier = 1.15, rewardCommandCooldown = 12, noImpurityChance = true, canResetTask = false, canBlockTask = false, autoEnchantJewellery = false, dragonImmunityChance = -1, gunGameDiscount = 1.0, doubleResourceChance = -1, infiniteRunEnergy = false, maxCannonballs = 30, autoReloadCannon = false, itemsKeptOnDeathPVM = 0, itemsKeptOnDeathPVP = 0, healingMultiplier = 1.0, toolsRequired = true),
    DIAMOND_MEMBER(id = 5, memberGroupId = 16, chatIcon = 28, bankSpaces = 1000, itemId = Item.DONOR_STATUS, color = "95dbed", waveId = 24, yellTagCost = 0, specialRegenerationModifier = 0.8, thievingSuccessModifier = 15, petInsuranceDiscount = 0.75, burnAllLogsChance = 20, poisonImmunity = true, canDecant = true, cookAllFoodChance = 15, bonusSlayerPointsMultiplier = 1.25, rewardCommandCooldown = 12, noImpurityChance = true, canResetTask = true, canBlockTask = true, autoEnchantJewellery = false, dragonImmunityChance = -1, gunGameDiscount = 1.0, doubleResourceChance = -1, infiniteRunEnergy = false, maxCannonballs = 30, autoReloadCannon = false, itemsKeptOnDeathPVM = 0, itemsKeptOnDeathPVP = 0, healingMultiplier = 1.0, toolsRequired = true),
    DRAGONSTONE_MEMBER(id = 6, memberGroupId = 17, chatIcon = 29, bankSpaces = 1500, itemId = Item.DONOR_STATUS, color = "800080", waveId = 29, yellTagCost = 0, specialRegenerationModifier = 0.7, thievingSuccessModifier = 25, petInsuranceDiscount = 0.65, burnAllLogsChance = 30, poisonImmunity = true, canDecant = true, cookAllFoodChance = 15, bonusSlayerPointsMultiplier = 1.3, rewardCommandCooldown = 12, noImpurityChance = true, canResetTask = true, canBlockTask = true, autoEnchantJewellery = true, dragonImmunityChance = 25, gunGameDiscount = 0.75, doubleResourceChance = 10, infiniteRunEnergy = false, maxCannonballs = 30, autoReloadCannon = false, itemsKeptOnDeathPVM = 0, itemsKeptOnDeathPVP = 0, healingMultiplier = 1.0, toolsRequired = true),
    ONYX_MEMBER(id = 7, memberGroupId = 18, chatIcon = 30, bankSpaces = 2000, itemId = Item.DONOR_STATUS, color = "5C5D62", waveId = 44, yellTagCost = 0, specialRegenerationModifier = 0.5, thievingSuccessModifier = 50, petInsuranceDiscount = 0.5, burnAllLogsChance = 40, poisonImmunity = true, canDecant = true, cookAllFoodChance = 20, bonusSlayerPointsMultiplier = 1.4, rewardCommandCooldown = 12, noImpurityChance = true, canResetTask = true, canBlockTask = true, autoEnchantJewellery = true, dragonImmunityChance = 30, gunGameDiscount = 0.5, doubleResourceChance = 15, infiniteRunEnergy = true, maxCannonballs = 60, autoReloadCannon = false, itemsKeptOnDeathPVM = 1, itemsKeptOnDeathPVP = 0, healingMultiplier = 1.0, toolsRequired = true),
    ZENYTE_MEMBER(id = 8, memberGroupId = 19, chatIcon = 31, bankSpaces = 2500, itemId = Item.DONOR_STATUS, color = "ffaa00", waveId = 59, yellTagCost = 0, specialRegenerationModifier = 0.25, thievingSuccessModifier = 100, petInsuranceDiscount = 0.0, burnAllLogsChance = 50, poisonImmunity = true, canDecant = true, cookAllFoodChance = 25, bonusSlayerPointsMultiplier = 1.5, rewardCommandCooldown = 8, noImpurityChance = true, canResetTask = true, canBlockTask = true, autoEnchantJewellery = true, dragonImmunityChance = 40, gunGameDiscount = 0.0, doubleResourceChance = 20, infiniteRunEnergy = true, maxCannonballs = 90, autoReloadCannon = true, itemsKeptOnDeathPVM = 2, itemsKeptOnDeathPVP = 1, healingMultiplier = 1.10, toolsRequired = false);

    companion object {

        /**
         * Gets a status by the id.
         *
         * @param id The id of the status.
         * @return The status.
         */
        fun forId(id: Int): DonorStatus? {
            return values().firstOrNull { it.id == id }
        }

        /**
         * Gets a status by the member id.
         *
         * @param id The member id of the status.
         * @return The status.
         */
        fun forMemberId(id: Int): DonorStatus? {
            return values().firstOrNull { it.memberGroupId == id }
        }

        /**
         * Gets a status by the id of an item.
         *
         * @param itemId The id of the item.
         * @return The status.
         */
        fun forItemId(itemId: Int): DonorStatus? {
            return values().firstOrNull { it.itemId == itemId }
        }
    }
}
