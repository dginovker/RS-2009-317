package org.gielinor.game.content.skill.free.magic;

import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.model.container.Container;

import plugin.interaction.item.RunePouchPlugin;

/**
 * Represents the <b>Constants</b> of runes.
 *
 * @author 'Vexia
 */
public enum Runes {
    AIR_RUNE(556),
    WATER_RUNE(555),
    EARTH_RUNE(557),
    FIRE_RUNE(554),
    MIND_RUNE(558),
    NATURE_RUNE(561),
    CHAOS_RUNE(562),
    DEATH_RUNE(560),
    COSMIC_RUNE(564),
    BLOOD_RUNE(565),
    SOUL_RUNE(566),
    ASTRAL_RUNE(9075),
    LAW_RUNE(563),
    STEAM_RUNE(4694),
    MIST_RUNE(4695),
    DUST_RUNE(4696),
    SMOKE_RUNE(4697),
    MUD_RUNE(4698),
    LAVA_RUNE(4699),
    BODY_RUNE(559),
    SARADOMIN_STAFF(2415),
    GUTHIX_STAFF(2416),
    ZAMORAK_STAFF(2417);

    /**
     * Constructs a new {@code Runes} {@code Object}.
     *
     * @param id the id.
     */
    Runes(int id) {
        this.id = id;
    }

    /**
     * The id of the rune.
     */
    private int id;


    /**
     * Gets the id of the rune.
     *
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the rune by the id.
     *
     * @param id the id.
     * @return the id.
     */
    public static Runes forId(int id) {
        for (Runes rune : Runes.values()) {
            if (rune.getId() == id) {
                return rune;
            }
        }
        return null;
    }

    /**
     * Checks if the player has an infinite amt of runes.
     *
     * @param rune   the rune.
     * @param weapon the weapon.
     * @return <code>True</code> if so.
     */
    public static final boolean isInfinite(Runes rune, Item weapon, SpellType... type) {
        if (weapon == null || rune == null) {
            return false;
        }
        if (type != null) {
            if (weapon.getId() == 2415 && rune == Runes.SARADOMIN_STAFF && type.length == 1) {
                if (type[0] == SpellType.GOD_STRIKE) {
                    return true;
                }
            }
            if (weapon.getId() == 2416 && rune == Runes.GUTHIX_STAFF && type.length == 1) {
                if (type[0] == SpellType.GOD_STRIKE) {
                    return true;
                }
            }
            if (weapon.getId() == 2417 && rune == Runes.ZAMORAK_STAFF && type.length == 1) {
                if (type[0] == SpellType.GOD_STRIKE) {
                    return true;
                }
            }
        }
        if (rune == Runes.AIR_RUNE) {
            if (weapon.getId() == 1381 || weapon.getId() == 1397) // air staff
            {
                return true;
            }
        } else if (rune == Runes.WATER_RUNE) {
            if (weapon.getId() == 1383 || weapon.getId() == 1395) // water staff
            {
                return true;
            }
        } else if (rune == Runes.EARTH_RUNE) {
            if (weapon.getId() == 1385 || weapon.getId() == 1399) // earth staff
            {
                return true;
            }
        } else if (rune == Runes.FIRE_RUNE) {
            if (weapon.getId() == 1387 || weapon.getId() == 1393) // fire staff
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player's inventory or rune pouch contains a rune.
     *
     * @param player    The player.
     * @param runesItem The runes item.
     * @return <code>True</code> if so.
     */
    public static boolean hasRunes(Player player, Item runesItem) {
        // Use from inventory first.
        if (player.getInventory().contains(runesItem)) {
            return true;
        }
        return player.getInventory().contains(Item.RUNE_POUCH)
            && player.getSavedData().getGlobalData().getRunePouch().contains(runesItem);
    }

    /**
     * Checks if the player's inventory or rune pouch contains a rune.
     *
     * @param player The player.
     * @param runes  The <code>Runes</code>.
     * @param amount The amount.
     * @return <code>True</code> if so.
     */
    public static boolean hasRunes(Player player, Runes runes, int amount) {
        return hasRunes(player, runes.getItem(amount));
    }

    /**
     * Removes runes from the player's inventory or rune pouch if they have one and it has the required runes.
     *
     * @param player The player.
     * @param runes  The <code>Runes</code> as items.
     */
    public static void removeRunes(Player player, Item... runes) {
        for (Item rune : runes) {
            if (!hasRunes(player, rune)) {
                return;
            }
        }
        Container runePouch = player.getSavedData().getGlobalData().getRunePouch();
        for (Item rune : runes) {
            if (player.getInventory().contains(rune)) { // Remove from inventory first (as is on RuneScape).
                player.getInventory().remove(rune);
                continue;
            }
            runePouch.remove(rune);
            runePouch.sendInventory(player, RunePouchPlugin.INVENTORY_COMPONENT);
        }
    }

    /**
     * Method used to transform this item.
     *
     * @return the item.
     */
    public Item transform() {
        return new Item(id);
    }

    /**
     * Gets the item of this rune.
     *
     * @param amount The amount.
     * @return The item instance.
     */
    public Item getItem(int amount) {
        return new Item(id, amount);
    }
}
