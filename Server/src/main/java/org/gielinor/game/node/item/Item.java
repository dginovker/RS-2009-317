package org.gielinor.game.node.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.DestinationFlag;
import org.gielinor.game.interaction.Interaction;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.combat.equipment.DegradableEquipment;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an item.
 *
 * @author Emperor
 */
public class Item extends Node {

    /**
     * The item id
     */
    private int id;

    /**
     * The item count.
     */
    private int count;
    
    /**
     * The item's 'charge'
     */
    private int charge;

    /**
     * The item definition.
     */
    private ItemDefinition definition;

    /** The default charge for items created without a specified charge. */
    private static final int DEFAULT_CHARGE_COUNT = 1000;

    /** Primary currency item */
    public static final int COINS = 995;
    
    public static final int HAMMER = 2347;

    /** Ghostspeak amulet - required when talking to ghosts */
    public static final int GHOSTSPEAK_AMULET = 552;

    /* Uncut gemstones */
    public static final int UNCUT_SAPPHIRE = 21623;
    public static final int UNCUT_EMERALD = 21621;
    public static final int UNCUT_RUBY = 21619;
    public static final int UNCUT_DIAMOND = 21617;
    public static final int UNCUT_DRAGONSTONE = 21631;
    public static final int UNCUT_ONYX = 26571;
    public static final int UNCUT_OPAL = 21625;
    public static final int UNCUT_JADE = 21627;
    public static final int UNCUT_RED_TOPAZ = 21629;
    public static final int UNCUT_ZENYTE = 39496;
    
    public static final int COAL = 453;

    /* Explorers ring variants */
    public static final int EXPLORERS_RING_1 = 33125;
    public static final int EXPLORERS_RING_2 = 33126;
    public static final int EXPLORERS_RING_3 = 33127;
    public static final int EXPLORERS_RING_4 = 33128; // TODO

    /* Resource packs */
    public static final int EMPTY_VIAL_PACK = 31877;
    public static final int WATER_FILLED_VIAL_PACK = 31879;
    public static final int FEATHER_PACK = 31881;
    public static final int BAIT_PACK = 31883;
    public static final int EYE_OF_NEWT_PACK = 32859;

    /* Variants of the slayer helmet */
    public static final int SLAYER_HELMET = 31864;
    public static final int BLACK_SLAYER_HELMET = 39639;
    public static final int GREEN_SLAYER_HELMET = 39643;
    public static final int RED_SLAYER_HELMET = 39647;
    public static final int PURPLE_SLAYER_HELMET = 41264;

    /* Variants of the slayer ring */
    public static final int SLAYER_RING_8 = 14672;
    public static final int SLAYER_RING_7 = 14673;
    public static final int SLAYER_RING_6 = 14674;
    public static final int SLAYER_RING_5 = 14675;
    public static final int SLAYER_RING_4 = 14676;
    public static final int SLAYER_RING_3 = 14677;
    public static final int SLAYER_RING_2 = 14678;
    public static final int SLAYER_RING_1 = 14679;

    /* Achievement capes */
    public static final int ACHIEVEMENT_DIARY_CAPE_T = 14682;
    public static final int ACHIEVEMENT_DIARY_HOOD = 14683;

    /* Variants of the max cape */
    public static final int MAX_CAPE = 33342;
    public static final int MAX_HOOD = 33281;
    public static final int FIRE_MAX_CAPE = 14685;
    public static final int FIRE_MAX_HOOD = 14686;
    public static final int SARADOMIN_MAX_CAPE = 14687;
    public static final int SARADOMIN_MAX_HOOD = 14688;
    public static final int ZAMORAK_MAX_CAPE = 14689;
    public static final int ZAMORAK_MAX_HOOD = 14690;
    public static final int GUTHIX_MAX_CAPE = 14691;
    public static final int GUTHIX_MAX_HOOD = 14692;
    public static final int AVAS_MAX_CAPE = 14693;
    public static final int AVAS_MAX_HOOD = 14694;

    /* Godwars items */
    public static final int PET_KREE_ARRA = 14717;
    public static final int PET_GENERAL_GRAARDOR = 14716;
    public static final int PET_KRIL_TSUTSAROTH = 14718;
    public static final int PET_ZILYANA = 14695;
    public static final int ZAMORAKIAN_HASTA = 14696;

    /* Corporeal Beast items */
    public static final int SPIRIT_SHIELD = 13734;
    public static final int HOLY_ELIXIR = 13754;
    public static final int BLESSED_SPIRIT_SHIELD = 13736;
    public static final int ARCANE_SIGIL = 13746;
    public static final int ARCANE_SPIRIT_SHIELD = 13738;
    public static final int ELYSIAN_SIGIL = 13750;
    public static final int ELYSIAN_SPIRIT_SHIELD = 32817;
    public static final int SPECTRAL_SIGIL = 13752;
    public static final int SPECTRAL_SPIRIT_SHIELD = 13744;

    /* Donator items */
    public static final int DONOR_ORB = 14698;
    public static final int DONOR_STATUS = 14699;
    public static final int SUPER_DONOR_STATUS = 14700;
    public static final int EXTREME_DONOR_STATUS = 14701;

    /*  Ironman armour */
    public static final int IRONMAN_HELM = 32810;
    public static final int IRONMAN_PLATEBODY = 32811;
    public static final int IRONMAN_PLATELEGS = 32812;
    public static final int ULTIMATE_IRONMAN_HELM = 32813;
    public static final int ULTIMATE_IRONMAN_PLATELEGS = 32814;
    public static final int ULTIMATE_IRONMAN_PLATEBODY = 32815;
    public static final int HARDCORE_IRONMAN_HELM = 40792;
    public static final int HARDCORE_IRONMAN_BODY = 40794;
    public static final int HARDCORE_IRONMAN_LEGS = 40796;

    /**
     * The ashes of Ahrim the Blighted.
     */
    public static final int AHRIM_ASHES = 14708;
    /**
     * The ashes of Dharok the Wretched.
     */
    public static final int DHAROK_ASHES = 14709;
    /**
     * The ashes of Guthan the Infested.
     */
    public static final int GUTHAN_ASHES = 14710;
    /**
     * The ashes of Karil the Tainted
     */
    public static final int KARIL_ASHES = 14711;
    /**
     * The ashes of Torag the corrupted.
     */
    public static final int TORAG_ASHES = 14712;
    /**
     * The ashes of Verac the Defiled.
     */
    public static final int VERAC_ASHES = 14713;
    /**
     *
     **/
    public static final int STRANGE_POTION = 14714;
    /**
     *
     **/
    public static final int TZREK_JAD = 14720;
    /**
     *
     */
    public static final int RUNE_POUCH = 32791;

    public static final int[] BOSS_PET_ITEMS = { PET_GENERAL_GRAARDOR, PET_KRIL_TSUTSAROTH, PET_KREE_ARRA, PET_ZILYANA, TZREK_JAD };

    /**
     * Moderator Ban Hammer Tool
     **/
    public static final int BAN_HAMMER = 27668;

    /**
     * Administrative rotten potato tool
     */
    public static final int ROTTEN_POTATO = 5733;

    /**
     * Constructs a new {@code Item} {@code Object}.
     * <br>The id will be -1 (thus <b>definition will be <code>null</code></b>
     */
    public Item() {
        super("null", null);
        super.setInteraction(interaction = new Interaction(this));
        this.id = -1;
    }

    /**
     * Constructs a new fully charged {@code Item} {@code Object}.
     *
     * @param id The item id.
     */
    public Item(int id) {
        this(id, 1, DEFAULT_CHARGE_COUNT);
    }

    /**
     * Constructs a new fully charged {@code Item} {@code Object}.
     *
     * @param id    The item id.
     * @param count The count.
     */
    public Item(int id, int count) {
        this(id, count, DEFAULT_CHARGE_COUNT);
    }

    /**
     * Constructs a new {@code Item} {@code Object}.
     *
     * @param id     The item id.
     * @param count  The count.
     * @param charge The charge.
     */
    public Item(int id, int count, int charge) {
        super(ItemDefinition.forId(id) == null ? "null" : ItemDefinition.forId(id).getName(), null);
        super.setDestinationFlag(DestinationFlag.ITEM);
        super.setIndex(-1);
        super.setInteraction(new Interaction(this));
        this.id = id;
        this.count = count < 0 ? 0 : count;
        this.charge = charge;
        this.definition = ItemDefinition.forId(id);
        super.getInteraction().setDefault();
    }

    /**
     * Constructs a new {@code Item} from an {@code ItemDefinition}, {@code count} and {@code charge}.
     *
     * @param definition            The definition type of item to stack. Not null.
     * @param count                 The stack count.
     * @param charge                The charge count.
     *
     * @throws NullPointerException if {@code definition} is {@code null}.
     */
    public Item(ItemDefinition definition, int count, int charge) {
        super(definition.getName(), /* location */ null);
        this.count = count;
        this.definition = definition;
        this.charge = charge;
        id = definition.getId();
        destinationFlag = DestinationFlag.ITEM;
        interaction = new Interaction(this);
        interaction.setDefault();
        index = -1;
    }

    /**
     * Constructs a new {@code Item} from an item.
     *
     * @param item The item.
     */
    public Item(Item item) {
        this(item.getId(), item.getCount(), item.getCharge());
    }

    /**
     * Gets the operate option handler.
     *
     * @return The option handler for the operate option.
     */
    public OptionHandler getOperateHandler() {
        return ItemDefinition.getOptionHandler(getId(), "operate");
    }

    /**
     * Gets the value of the item.
     *
     * @return The value.
     */
    public long getValue() {
        long value = 1;
        if (definition.getValue() > value) {
            value = definition.getValue();
        }
        if (definition.getAlchemyValue(true) > value) {
            value = definition.getAlchemyValue(true);
        }
        return value * getCount();
    }

    /**
     * Gets a copy of the item.
     *
     * @return The item copy.
     */
    public Item copy() {
        return new Item(this);
    }

    /**
     * Gets the item id of the exchange item for this item.
     *
     * @return The note item id, if this item is unnoted, or the unnoted item id if this item is noted.
     */
    public int getNoteChange() {
        int noteId = definition.getNoteId();
        if (noteId > -1) {
            return noteId;
        }
        return getId();
    }

    /**
     * Decide whether this and the supplied items are to be considered "type-equal":
     * Same item type and metadata.
     *
     * @param  item The item to compare type equality with.
     * @return {@code true} if, and only if, this and the supplied items are "type-equal".
     *         {@code false} otherwise.
     */
    public boolean isTypeEqual(Item item) {
        return item.id == id && item.charge == charge;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.definition = ItemDefinition.forId(id);
        this.id = id;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     * @return The item.
     */
    public Item setCount(int count) {
        if (count < 0) {
            count = 0;
        }
        this.count = count;
        return this;
    }

    /**
     * Increases the count.
     *
     * @param count The count to increase by.
     * @return The new count.
     */
    public int increase(int count) {
        this.count += count;
        return this.count;
    }

    /**
     * Decreases the count.
     *
     * @param count The count to decrease by.
     * @return The new count.
     */
    public int decrease(int count) {
        this.count -= count;
        return this.count;
    }

    /**
     * Checks if the item has atleast one charge left.
     *
     * @return <code>True</code> if so.
     */
    public boolean isCharged() {
        return getCharge() > 0;
    }

    /**
     * @return the charge
     */
    public int getCharge() {
        return charge;
    }

    /**
     * @param charge the charge to set
     */
    public void setCharge(int charge) {
        this.charge = charge;
    }

    /**
     * Checks if the item has a wrapper plugin.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasItemPlugin() {
        return getPlugin() != null;
    }

    /**
     * Gets this item as a ground item.
     *
     * @return The item.
     */
    public Item getDropItem() {
        int itemId = DegradableEquipment.getDropReplacement(getId());
        if (itemId != getId()) {
            return new Item(itemId, getCount());
        }
        return this;
    }

    /**
     * Gets the item plugin.
     *
     * @return the plugin.
     */
    public ItemPlugin getPlugin() {
        if (definition == null) {
            return null;
        }
        return definition.getItemPlugin();
    }

    /**
     * Gets the definition.
     *
     * @return The definition.
     */
    public ItemDefinition getDefinition() {
        return definition;
    }

    /**
     * Sets the definition.
     *
     * @param definition The definition to set.
     */
    public void setDefinition(ItemDefinition definition) {
        this.definition = definition;
    }

    /**
     * Gets the slot of this item.
     *
     * @return The container slot, or {@code -1} if the item wasn't added to a container.
     */
    public int getSlot() {
        return index;
    }

    public int getSlotsUsed(){
        if(definition.isStackable() || !definition.isUnnoted())
            return 1;
        else
            return getCount();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(64).append(Item.class.getSimpleName()).append("(");
        builder.append("id=").append(id);
        builder.append(", count=").append(count);
        // Charge is unused most of the time (by far)
        if (charge != DEFAULT_CHARGE_COUNT)
            builder.append(", charge=").append(charge);
        return builder.append(')').toString();
    }

    /**
     * Gets items with a defined increment
     */
    public static Item[] getItemsByIncrementInRange(int minRange, int maxRange, int increment) {
        List<Item> itemArrayList = new ArrayList<>();
        for (int i = minRange; i <= maxRange; i += increment) {
            itemArrayList.add(new Item(i));
        }
        return itemArrayList.toArray(new Item[itemArrayList.size()]);
    }

    /**
     * Gets items with an increment of 2, useful for getting ids of unnoted items.
     */
    public static Item[] getUnnotedItemsInRange(int minRange, int maxRange) {
        return getItemsByIncrementInRange(minRange, maxRange, 2);
    }


    /* Factory methods. These throw when definition doesn't exist! */


    public static Item of(int id) {
        ItemDefinition definition = ItemDefinition.forId(id, false);
        return new Item(definition, 1, DEFAULT_CHARGE_COUNT);
    }

    public static Item of(int id, int count) {
        ItemDefinition definition = ItemDefinition.forId(id, false);
        if (count < 1)
            throw new IllegalArgumentException("count < 1: " + count);
        return new Item(definition, count, DEFAULT_CHARGE_COUNT);
    }

}
