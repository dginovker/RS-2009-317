package org.gielinor.game.node.entity.player.ai;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.appearance.Gender;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Used for "building" artificial intelligent players.
 *
 * @author Emperor
 */
public final class AIPBuilder {

    public static Map<Integer, List<ItemDefinition>> equipments = new HashMap<>();

    /**
     * Creates a new artificial intelligent player.
     *
     * @param name The name.
     * @return The AIPlayer object.
     */
    public static AIPlayer create(String name, Location l) {
        return new AIPlayer(name, l);
    }

    /**
     * Makes an artificial intelligent copy of the player.
     *
     * @param player The player.
     * @return The artificial intelligent player with the same name, stats, items, etc.
     */
    public static AIPlayer copy(Player player) {
        return copy(player, player.getName(), player.getLocation());
    }

    /**
     * Makes an artificial intelligent copy of the player.
     *
     * @param player The player.
     * @param l      The location the AIP should spawn on.
     * @return The artificial intelligent player with the same name, stats, items, etc.
     */
    public static AIPlayer copy(Player player, Location l) {
        return copy(player, player.getName(), l);
    }

    /**
     * Makes an artificial intelligent copy of the player.
     *
     * @param player The player.
     * @param name   The AIP's name.
     * @param l      The location the AIP should spawn on.
     * @return The artificial intelligent player with the same name, stats, items, etc.
     */
    public static AIPlayer copy(Player player, String name, Location l) {
        AIPlayer p = new AIPlayer(name, l);
        p.getSkills().copy(player.getSkills());
        p.getInventory().copy(player.getInventory());
        p.getEquipment().copy(player.getEquipment());
        p.getBank().copy(player.getBank());
        p.getAppearance().copy(player.getAppearance());
        p.setController(player);
        return p;
    }

    public static ItemDefinition getRandom(int slot) {
        if (equipments.get(slot) == null) {
            List<ItemDefinition> itemDefinitionList = new ArrayList<>();
            for (ItemDefinition itemDefinition : ItemDefinition.values()) {
                int equipSlot = itemDefinition.getConfiguration(ItemConfiguration.EQUIP_SLOT, -1);
                if (equipSlot == -1 || equipSlot != slot) {
                    continue;
                }
                if (!itemDefinition.hasWearAction()) {
                    continue;
                }
                itemDefinitionList.add(itemDefinition);
            }
            equipments.put(slot, itemDefinitionList);
        }
        List<ItemDefinition> itemDefinitionList = equipments.get(slot);
        return itemDefinitionList.get(new SecureRandom().nextInt(itemDefinitionList.size() - 1));
    }

    static void setRandomAppearance(Player p) {
        SecureRandom random = new SecureRandom();
        Gender gender = random.nextInt(1) == 0 ? Gender.FEMALE : Gender.MALE;
        p.getAppearance().setGender(gender);
        for (int slot = 0; slot < 13; slot++) {
            if (slot == 6 || slot == 8 || slot == 11) {
                continue;
            }
            Item item = new Item(getRandom(slot).getId());
            if (item == null) {
                continue;
            }
            if (slot == 5) {
                if (p.getEquipment().get(3) != null && p.getEquipment().get(3).getDefinition().getConfiguration(ItemConfiguration.TWO_HANDED, false)) {
                    continue;
                }
            }
            p.getEquipment().add(item, true, slot);
        }
        p.getEquipment().refresh();
        p.getAppearance().sync();
    }

    /**
     * Makes an artificial intelligent unique.
     *
     * @param player The player.
     * @param name   The AIP's name.
     * @param l      The location the AIP should spawn on.
     * @return The artificial intelligent player.
     */
    public static AIPlayer makeUnique(Player player, String name, Location l) {
        AIPlayer p = new AIPlayer(name, l);
        for (int skillId = 0; skillId < Skills.SKILL_NAME.length; skillId++) {
            if (skillId == 23) {
                continue;
            }
            int level = RandomUtil.random(20, 99);
            p.getSkills().setStaticLevel(skillId, level);
        }
        setRandomAppearance(p);
        p.setController(player);
        return p;
    }
}