package org.gielinor.game.node.entity.player.info.donor;

import org.gielinor.game.node.item.Item;

/**
 * Represents items that can be purchased in the Donor shop.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum DonorItem {
    //TODO donator items
    DONOR_ORB(Item.DONOR_ORB, 0, DonorStatus.SAPPHIRE_MEMBER),
    DONOR_STATUS(Item.DONOR_STATUS, 107, DonorStatus.NONE),
    SUPER_DONOR_STATUS(Item.SUPER_DONOR_STATUS, 334, DonorStatus.NONE),
    EXTREME_DONOR_STATUS(Item.EXTREME_DONOR_STATUS, 517, DonorStatus.NONE),
    HELL_KITTEN(7583, 53, DonorStatus.SAPPHIRE_MEMBER),
    TZHAAR_DART(11954, 48, DonorStatus.NONE),
    VOLCANIC_WHIP_MIX(32771, 200, DonorStatus.NONE),
    FROZEN_WHIP_MIX(32769, 200, DonorStatus.NONE),
    DRAGON_PICKAXE_UPGRADE_KIT(32800, 150, DonorStatus.NONE),
    WARD_UPGRADE_KIT(32802, 200, DonorStatus.NONE),
    ARCEUUS_HOUSE_HOOD(40113, 50, DonorStatus.NONE),
    SAMURAI_KASA(40035, 50, DonorStatus.NONE),
    SAMURAI_SHIRT(40038, 50, DonorStatus.NONE),
    SAMURAI_GRIEVES(40044, 50, DonorStatus.NONE),
    SAMURAI_GLOVES(40041, 50, DonorStatus.NONE),
    SAMURAI_BOOTS(40047, 50, DonorStatus.NONE),
    RING_OF_COINS(40017, 500, DonorStatus.DRAGONSTONE_MEMBER),;

    /**
     * The id of the item.
     */
    private final int itemId;
    /**
     * The cost in Gielinor tokens.
     */
    private final int cost;
    /**
     * The minimum {@link org.gielinor.game.node.entity.player.info.donor.DonorStatus} required.
     */
    private final DonorStatus donorStatus;

    /**
     * Constructs a new {@link org.gielinor.game.node.entity.player.info.donor.DonorItem}.
     *
     * @param itemId      The id of the item.
     * @param cost        The cost in Gielinor tokens.
     * @param donorStatus The minimum {@link org.gielinor.game.node.entity.player.info.donor.DonorStatus} required.
     */
    DonorItem(int itemId, int cost, DonorStatus donorStatus) {
        this.itemId = itemId;
        this.cost = cost;
        this.donorStatus = donorStatus;
    }

    /**
     * Gets the id of the item.
     *
     * @return The id.
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Gets the cost in Gielinor tokens.
     *
     * @return The cost.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Gets the minimum {@link org.gielinor.game.node.entity.player.info.donor.DonorStatus} required.
     *
     * @return The donor status.
     */
    public DonorStatus getDonorStatus() {
        return donorStatus;
    }

    /**
     * Gets a donor item by the id of an item.
     *
     * @param itemId The id of the item.
     * @return The donor item.
     */
    public static DonorItem forId(int itemId) {
        for (DonorItem donorItem : values()) {
            if (donorItem.getItemId() == itemId) {
                return donorItem;
            }
        }
        return null;
    }
}
