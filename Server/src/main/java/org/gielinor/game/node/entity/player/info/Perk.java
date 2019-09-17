package org.gielinor.game.node.entity.player.info;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents an in-game "perk" a player has.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum Perk {

    SLEIGHT_OF_HAND(1, 50, StringColor.LIGHT_PURPLE, "Increases your chances of pickpocketing by 25%."),
    KING_OF_THE_ABYSS(2, 100, "Abyssal creatures will not attack you while in the Abyss Rift."),
    ROCK_SMASHER(3, 100, "Gives a 35% chance of receiving double ores while mining up to Adamantite."),
    COIN_COLLECTOR(4, 200, "Automatically collects coins in your inventory from drops."),
    SEED_SAVIOR(5, 100, "Gives a 40% chance of not using seeds or saplings when planted."),
    BONE_SAVIOR(6, 150, "Gives a 40% chance of not using a bone when buried<br>or offered."),
    SLAYER_COMMANDER(7, 200, "Allows you to reset your Slayer task at any time for up<br>to 5 times, this will not reset your streak.", 5),
    GOD_WARS_EQUAL(8, 350, "God wars boss followers will not attack you while outside a boss room."),
    CHARM_COLLECTOR(9, 200, "Automatically collects charms in your inventory from drops."),

    // these two have not yet been implemented
    DRAGON_SLAYER(10, 300, "Deal 30% more damage to dragons."),
    CLUES_CLUES_CLUES(11, 120, "Allows you to have a 10% chance of finding a clue scroll whilst skilling."),

    FIRESTARTER(12, 30, "Gives a 2.5% chance of burning all logs of a single type in your inventory when firemaking."),

    // not yet implemented
    BRAIINNNSSSS(13, 50, "Damage to the undead is increased by 20%."),

    NICE_THREADS(14, 35, "Thread is no longer needed when crafting."),
    KEEN_FISHERMAN(15, 30, "Gives a 10% chance of an extra catch when fishing."),
    AVID_FISHERMAN(16, 50, "Gives a 25% chance of an extra catch when fishing."),
    PRO_FISHERMAN(17, 400, "Gives a 50% chance of an extra catch when fishing."),
    KEEN_SUMMONER(18, 30, "Gives a 10% chance of saving charms when infusing pouches."),
    AVID_SUMMONER(19, 70, "Gives a 25% chance of saving charms when infusing pouches."),
    PRO_SUMMONER(20, 400, "Gives a 50% chance of saving charms when infusing pouches."),
    KALPHITE_KILLER(21, 50, "Kalphite Queens minions ignore you."),
    CHARMING(22, 30, "3% Chance for a drop to be a box of 5 of each charm."),
    CHARMING_II(23, 50, "7% Chance for a drop to be a box of 5 of each charm."),
    CHARMING_III(24, 100, "14% Chance for a drop to be a box of 5 of each charm."),
    COIN_COLLECTOR_II(25, 230, "Collects coin drops automatically and places them in your bank."),
    PRAY_FOR_ME(26, 20, "Collects all regular bones and automatically banks them."),
    PRAY_FOR_ME_II(27, 50, "Collects all bones and automatically banks them."),
    EVENTS_BE_GONE(28, 200, "Never receive a random event ever again."),
    READY_SET_GO(29, 20, "Allows you to use a chisel on armour sets to open them."),
    CHAOS_IMMUNE(30, 100, "This perk makes you immune to Chaos Elementals Teleport attack."),
    CHAOS_IMMUNE_II(31, 200, "This perk makes you immune to Chaos Elementals Teleport & De-Equip attack."),
    YOU_ADOPTED_THE_DARKNESS(32, 50, "No longer require a lightsource in any cave or dungeon."),
    REPLENISHER(33, 25, "Hitpoints and Prayer are restored if they level up."),
    TRY_AGAIN_BRO(33, 100, "Grants a retry for the barrows brother's chest.", 5);


    private final int cost;
    private final StringColor color;
    private final String description;
    /**
     * The time this perk will last in minutes.
     */
    private final long time;

    /**
     * Constructs a perk.
     *
     * @param cost        The cost of this perk.
     * @param description The description of this perk.
     */
    Perk(int id, int cost, String description) {
        this(id, cost, StringColor.NONE, description);
    }

    /**
     * Constructs a perk.
     *
     * @param cost        The cost of this perk.
     * @param stringColor The color syntax of this perk.
     * @param description The description of this perk.
     */
    Perk(int id, int cost, StringColor stringColor, String description) {
        this(id, cost, stringColor, description, -1);
    }
    /**
     * Constructs a perk.
     *
     * @param cost        The cost of this perk.
     * @param description The description of this perk.
     * @param time        The time this perk will last in minutes.
     */
    Perk(int id, int cost, String description, long time) {
        this(id, cost, StringColor.NONE, description, time);
    }
    /**
     * Constructs a perk.
     *
     * @param cost        The cost of this perk.
     * @param stringColor The color syntax of this perk.
     * @param description The description of this perk.
     * @param time        The time this perk will last in minutes.
     */
    Perk(int id, int cost, StringColor stringColor, String description, long time) {
        this.id = id;
        this.cost = cost;
        this.color = stringColor;
        this.description = description;
        this.time = time;
    }

    /**
     * The id of this <code>Perk</code>.
     */
    private final int id;

    /**
     * Gets the id of this <code>Perk</code>.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the formatted name of this <code>Perk</code>.
     *
     * @return The name.
     */
    public String getFormattedName() {
        return color.getSyntax() + TextUtils.formatDisplayName(name()).replaceAll("Ii", "II");
    }

    /**
     * Gets the cost of this perk.
     *
     * @return The cost.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Gets the description of this perk.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the time this {@link Perk} will last in minutes.
     *
     * @return The time.
     */
    public long getTime() {
        return time;
    }

    /**
     * Gets a {@link Perk} by ID.
     *
     * @param id The ID of the perk.
     * @return The perk.
     */
    public static Perk forId(int id) {
        for (Perk perk : Perk.values()) {
            if (perk.getId() == id) {
                return perk;
            }
        }
        return null;
    }

    /**
     * If this perk is enabled for a player.
     *
     * @param player The player.
     * @return <code>True</code> if so.
     */
    public boolean enabled(Player player) {
        return player.getPerkManager().getPerks().getOrDefault(this, false);
    }

    /**
     * Checks if this <code>Perk</code> is unlocked for a player.
     *
     * @param player The player.
     */
    public boolean isUnlocked(Player player) {
        return player.getPerkManager().getPerks().get(this) != null;
    }

    /**
     * Whether or not a player can purchase this perk.
     *
     * @param player The player.
     * @return <code>True</code> if so.
     */
    public boolean canPurchase(Player player) {
        if (player.getPerkManager().getPerks().containsKey(this)) {
            player.getActionSender().sendMessage("You already own that perk.");
            return false;
        }
        if (player.getDonorManager().getGielinorTokens() < getCost()) {
            player.getActionSender().sendMessage("You do not have enough Gielinor tokens to purchase that perk.");
            return false;
        }
        return true;
    }

    /**
     * Purchases this <code>Perk</code>.
     *
     * @param player The player.
     * @return The status message if purchase was successful.
     */
    public String purchase(Player player) {
        if (player.getDonorManager().getGielinorTokens() < getCost()) {
            return null;
        }
        if (isUnlocked(player)) {
            return null;
        }
        int gielinorTokens = player.getDonorManager().decreaseGielinorTokens(getCost());
        player.getPerkManager().unlock(this);
        return "For " + getCost() + " Gielinor tokens, you now have " + gielinorTokens + " Gielinor Token" +
            (gielinorTokens == 1 ? "" : "s") + " remaining.";
    }
}
enum StringColor {

    NONE(""),
    LIGHT_PURPLE("<col=7800a0>");

    private String colorSyntax;

    StringColor(String colorSyntax) {
        this.colorSyntax = colorSyntax;
    }

    public String getSyntax() {
        return colorSyntax;
    }
}
