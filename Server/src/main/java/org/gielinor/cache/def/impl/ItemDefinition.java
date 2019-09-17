package org.gielinor.cache.def.impl;

import org.gielinor.cache.def.Definition;
import org.gielinor.game.content.eco.grandexchange.GrandExchangeDatabase;
import org.gielinor.game.content.eco.grandexchange.offer.GrandExchangeEntry;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.item.ItemPlugin;
import org.gielinor.game.world.World;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.out.WeightUpdate;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.rs2.config.Constants;
import org.gielinor.spring.service.impl.ItemDefinitionService;
import org.gielinor.utilities.misc.RandomUtil;
import org.gielinor.utilities.string.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an item definition.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ItemDefinition extends Definition<Item> {

    private static final Logger log = LoggerFactory.getLogger(ItemDefinition.class);

    /**
     * The definitions.
     */
    private static Map<Integer, ItemDefinition> DEFINITIONS = new HashMap<>();

    /**
     * The default option handlers.
     */
    private static final Map<String, OptionHandler> OPTION_HANDLERS = new HashMap<>();

    /**
     * The id of the model this item uses.
     */
    private int modelId;
    /**
     * Whether or not this item is stackable.
     */
    private boolean stackable;
    /**
     * The value.
     */
    private int value;
    /**
     * If this item is members only.
     */
    private boolean members;
    /**
     * The ground options.
     */
    private String[] groundOptions;
    /**
     * If this item is unnoted.
     */
    private boolean unnoted = true;
    /**
     * The note id.
     */
    private int noteId;
    /**
     * The note template id.
     */
    private int noteTemplateId;
    /**
     * The team id.
     */
    private int teamId;
    /**
     * The weight equip id.
     */
    private int weightId;
    /**
     *
     */
    private boolean equipment;
    /**
     * The item's skill requirements
     */
    private Map<Integer, Integer> itemRequirements;

    /**
     * Constructs a new {@code ItemDefinition}.
     */
    public ItemDefinition() {
        setGroundOptions(new String[]{ null, null, "take", null, null });
        options = new String[]{ null, null, null, null, "drop" };
    }

    /**
     * Gets the item definition for the id given.
     *
     * @param id The id of the item.
     * @return The item definition.
     */
    public static ItemDefinition forId(int id) {
        if (Constants.CONVERT_IDS) {
            if (DEFINITIONS.get(id) == null) {
                ItemDefinition itemDefinition = new ItemDefinition();
                itemDefinition.setId(id);
                itemDefinition.setOptions(new String[]{ "wear", "wear", "wear", "wear", "drop" });
                itemDefinition.getConfigurations().put(ItemConfiguration.EQUIP_SLOT, 3);
                return itemDefinition;
            }
        }
        return DEFINITIONS.get(id);
    }

    /**
     * Gets the item definition for the id given.
     *
     * @param id The id of the item.
     * @return The item definition.
     */
    public static ItemDefinition forId(int id, @SuppressWarnings("unused") boolean newDefinition) {
        return DEFINITIONS.get(id);
    }

    /**
     * Parses the item definitions and requirements.
     */
    public static void parse() {
        ((ItemDefinitionService) World.getWorld().getApplicationContext().
            getBean("itemDefinitionService")).initializeDefinitions();
    }

    /**
     * Gets the first NPC definition for this name.
     *
     * @param name          The name of the NPC.
     * @param caseSensitive If the name search is case-sensitive.
     * @return The ItemDefinition object.
     */
    public static ItemDefinition forName(String name, boolean caseSensitive) {
        if (name == null)
            throw new NullPointerException("name");
        // Note:
        // Please do not change this to one loop.
        // It is more efficient to check this once, than it is to check each iteration.
        if (caseSensitive) {
            for (ItemDefinition item : values()) {
                if (name.equals(item.getName()))
                    return item;
            }
        } else {
            for (ItemDefinition item : values()) {
                if (name.equalsIgnoreCase(item.getName()))
                    return item;
            }
        }
        // No such item found ...
        return null;
    }


    /**
     * Gets the first NPC definition for this name.
     *
     * @param name The name of the NPC.
     * @return The ItemDefinition object.
     */
    public static ItemDefinition forName(String name) {
        return forName(name, true);
    }

    /**
     * Method returns the value for 'examine'.
     *
     * @return The examine.
     */
    public final String getExamine() {
        String string = getConfiguration(ItemConfiguration.EXAMINE, examine);
        return string == null ? "It's a" + (TextUtils.isPlusN(name) ? "n " : " ") + name + "." : string;
    }

    /**
     * Gets whether or not this item is stackable.
     *
     * @return <code>True</code> if so.
     */
    public boolean isStackable() {
        return stackable;
    }

    /**
     * Gets the note id.
     *
     * @return The note id.
     */
    public int getNoteId() {
        return noteId;
    }

    public String[] getGroundOptions() {
        return groundOptions;
    }

    /**
     * Gets the value.
     *
     * @return The value.
     */
    public int getValue() {
        return value;
    }

    /**
     * @return The value.
     */
    public int getMaxValue() {
        if (id == 6571) {
            return 1800000;
        }
        if ((int) (getValue() * 1.05) <= 0) {
            return 1;
        }
        return (int) (getValue() * 1.05);
    }

    /**
     * @return The value.
     */
    public int getMinValue() {
        if ((int) (getValue() * .95) <= 0) {
            return 1;
        }
        return (int) (getValue() * .95);
    }

    /**
     * Method sets the value for 'examine'
     *
     * @param examine the examine to set.
     */
    public final void setExamine(String examine) {
        this.examine = examine;
    }

    /**
     * Gets the alchemy value.
     *
     * @param highAlchemy If the value is for high alchemy (instead of low).
     * @return The alchemy value.
     */
    public int getAlchemyValue(boolean highAlchemy) {
        if (!isUnnoted() && noteId > -1 && noteId != id) {
            final ItemDefinition unnoted = forId(noteId);
            if (unnoted == null) {
                log.warn("Missing attributed un-noted [{}] for item [{} - {}]. Can't find alchemy value.",
                    noteId, id, name);
                return 0;
            }
            return unnoted.getAlchemyValue(highAlchemy);
        }
        if (highAlchemy) {
            return getConfiguration(ItemConfiguration.HIGH_ALCHEMY, 0);
        }
        return getConfiguration(ItemConfiguration.LOW_ALCHEMY, 0);
    }

    /**
     * Gets the Grand Exchange value, if null will get the high alchemy value.
     *
     * @param grandExchange If the value should be fetched with Grand Exchange.
     * @param highAlchemy   If we should fetch the high alchemy value or cached value.
     * @return The value.
     */
    public int getValue(boolean grandExchange, boolean highAlchemy) {
        if (!isUnnoted() && noteId > -1 && noteId != id) {
            final ItemDefinition unnoted = forId(noteId);
            if (unnoted == null) {
                log.warn("Missing attributed un-noted [{}] for item [{} - {}]. Can't find value.",
                    noteId, id, name);
                return 0;
            }
            return unnoted.getValue(grandExchange, highAlchemy);
        }
        if (!grandExchange) {
            return highAlchemy ? getConfiguration(ItemConfiguration.HIGH_ALCHEMY, 0) : getValue();
        }
        GrandExchangeEntry grandExchangeEntry = GrandExchangeDatabase.getDatabase().get(id);
        if (grandExchangeEntry == null) {
            return highAlchemy ? getConfiguration(ItemConfiguration.HIGH_ALCHEMY, 0) : getValue();
        }
        return grandExchangeEntry.getValue();
    }

    /**
     * Checks if the item is tradeable.
     *
     * @return {@code True} if so.
     */
    public boolean isTradeable() {
        if (hasDestroyAction()) {
            return false;
        }
        if ((getId() == 10942 || getId() == 10943 || getId() == 10944)) {
            return false;
        }
        return !isUnnoted() || getConfiguration(ItemConfiguration.TRADEABLE, false) || GrandExchangeDatabase.getDatabase().get(getId()) != null;
    }


    /**
     * The id of the model this item uses.
     */
    public int getModelId() {
        return modelId;
    }

    /**
     * Sets the id of the model this item uses.
     *
     * @param modelId The id of the model.
     */
    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    /**
     * If this item is members only.
     */
    public boolean isMembers() {
        return members;
    }

    /**
     * If this item is unnoted.
     */
    public boolean isUnnoted() {
        return unnoted;
    }

    /**
     * The note template id.
     */
    public int getNoteTemplateId() {
        return noteTemplateId;
    }

    /**
     * The team id.
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * Gets the weight equip id.
     */
    public int getWeightId() {
        return weightId;
    }

    /**
     * If the item has the specified item.
     *
     * @param optionName The action.
     * @return If the item has the specified action <code>True</code>.
     */
    public boolean hasAction(String optionName) {
        if (options == null) {
            return false;
        }
        for (String action : options) {
            if (action == null) {
                continue;
            }
            if (action.equalsIgnoreCase(optionName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * If the item has the destroy action.
     *
     * @return If the item has the destroy action <code>True</code>.
     */
    public boolean hasDestroyAction() {
        return hasAction("destroy");
    }

    /**
     * If the item has the wear action.
     *
     * @return If the item has the wear action <code>True</code>.
     */
    public boolean hasWearAction() {
        if (options == null) {
            return false;
        }
        for (String action : options) {
            if (action == null) {
                continue;
            }
            if (action.equalsIgnoreCase("wield") || action.equalsIgnoreCase("wear") || action.equalsIgnoreCase("equip")) {
                return true;
            }
        }
        return false;
    }

    /**
     * If the item has a special bar.
     *
     * @return If the item has a special bar <code>True</code>.
     */
    public boolean hasSpecialBar() {
        return getConfiguration(ItemConfiguration.HAS_SPECIAL, false);
    }

    /**
     * Gets the definitions.
     *
     * @return The definitions.
     */
    public static Map<Integer, ItemDefinition> getDefinitions() {
        return DEFINITIONS;
    }

    /**
     * Gets the values of the definitions.
     *
     * @return The values.
     */
    public static Collection<ItemDefinition> values() {
        return DEFINITIONS.values();
    }

    /**
     * Returns a random {@link org.gielinor.cache.def.impl.ItemDefinition}.
     *
     * @param noted Whether or not the definition can be noted.
     * @return The definition.
     */
    public static ItemDefinition getRandomDefinition(boolean noted) {
        ItemDefinition itemDefinition = DEFINITIONS.get(new SecureRandom().nextInt(DEFINITIONS.size() - 1));
        if (noted) {
            while (itemDefinition == null || itemDefinition.isUnnoted()) {
                itemDefinition = DEFINITIONS.get(new SecureRandom().nextInt(DEFINITIONS.size() - 1));
            }
        } else {
            while (itemDefinition == null || !itemDefinition.isUnnoted()) {
                itemDefinition = DEFINITIONS.get(new SecureRandom().nextInt(DEFINITIONS.size() - 1));
            }
        }
        return itemDefinition;
    }

    public static ItemDefinition getRandomOSRSDefinition(boolean noted) {
        final int OSRS_CAP = 34761;
        ItemDefinition itemDefinition = DEFINITIONS.get(RandomUtil.random(OSRS_CAP, DEFINITIONS.size() - 1));
        if (noted) {
            while (itemDefinition == null || itemDefinition.isUnnoted()) {
                itemDefinition = DEFINITIONS.get(RandomUtil.random(OSRS_CAP, DEFINITIONS.size() - 1));
            }
        } else {
            while (itemDefinition == null || !itemDefinition.isUnnoted()) {
                itemDefinition = DEFINITIONS.get(RandomUtil.random(OSRS_CAP, DEFINITIONS.size() - 1));
            }
        }
        return itemDefinition;
    }

    /**
     * Gets the option handler for the given option name.
     *
     * @param nodeId
     * @param name   The name.
     * @return The option handler, or {@code null} if there was no default option handler.
     */
    public static OptionHandler getOptionHandler(int nodeId, String name) {
        ItemDefinition def = forId(nodeId);
        if (def == null) {
            log.debug("Missing definition for [{}]. Tried to get option: [{}].",
                nodeId, name);
            return null;
        }
        OptionHandler handler = def.getConfiguration("option:" + name);
        if (handler != null) {
            return handler;
        }
        return OPTION_HANDLERS.get(name);
    }

    /**
     * Updates the equipment stats interface.
     *
     * @param player The player to update for.
     */
    public static void statsUpdate(Player player) {
        if (!player.getAttribute("equip_stats_open", false)) {
            return;
        }
        PacketRepository.send(WeightUpdate.class, player.getActionSender().getContext());
        int[] bonuses = player.getProperties().getBonuses();
        for (int index = 0; index < STRING_ID.length; index++) {
            int bonus = bonuses[index];
            if (index == 10) {
                continue;
            }
            String bonusValue = bonus > -1 ? ("+" + bonus) : Integer.toString(bonus);
            player.getActionSender().sendString(Integer.valueOf(STRING_ID[index][0]), STRING_ID[index][1] + ": " + bonusValue);
        }
    }

    public static final String[][] STRING_ID = {
        { "1675", "Stab" }, // 0
        { "1676", "Slash" }, // 1
        { "1677", "Crush" }, // 2
        { "1678", "Magic" }, // 3
        { "1679", "Ranged" }, // 4

        { "1680", "Stab" }, // 5
        { "1681", "Slash" }, // 6
        { "1682", "Crush" }, // 7
        { "1683", "Magic" }, // 8
        { "1684", "Ranged" }, // 9

        { "16526", "Ranged Strength" }, // - 10 Not in use
        { "1686", "Strength" }, // 11
        { "1687", "Prayer" }, // 12
        //	{"16527", "Magic Damage"} // 13
    };

    /**
     * Checks if it has a plugin.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasPlugin() {
        return getItemPlugin() != null;
    }

    /**
     * Gets the item plugin.
     *
     * @return the plugin.
     */
    public ItemPlugin getItemPlugin() {
        return getConfiguration("wrapper", null);
    }

    /**
     * Sets the item plugin.
     *
     * @param plugin the plugin.
     */
    public void setItemPlugin(ItemPlugin plugin) {
        getConfigurations().put("wrapper", plugin);
    }

    /**
     * Sets the default option handler for an option.
     *
     * @param name    The option name.
     * @param handler The default option handler.
     * @return <code>True</code> if there was a previous default handler mapped.
     */
    public static boolean setOptionHandler(String name, OptionHandler handler) {
        return OPTION_HANDLERS.put(name, handler) != null;
    }

    /**
     * @return the optionHandlers
     */
    public static Map<String, OptionHandler> getOptionHandlers() {
        return OPTION_HANDLERS;
    }

    /**
     * Checks if the player has the needed requirements to use this item.
     *
     * @param player  The player.
     * @param wield   If requirements are checked for wearing the item.
     * @param message If a message should be sent when the player does not meet a requirement.
     * @return <code>True</code> if so.
     */
    public boolean hasRequirement(Player player, boolean wield, boolean message) {
        // Steel pickaxe
        if (getId() == 1269) {
            if (player.getSkills().getStaticLevel(Skills.MINING) < 6) {
                if (message) {
                    String name = Skills.SKILL_NAME[Skills.MINING];
                    player.getActionSender().sendMessage("You need a" + (TextUtils.isPlusN(name) ? "n " : " ") + name + " level of 6 to wear this.");
                }
                return false;
            }
            if (player.getSkills().getStaticLevel(Skills.ATTACK) < 5) {
                if (message) {
                    String name = Skills.SKILL_NAME[Skills.ATTACK];
                    player.getActionSender().sendMessage("You need a" + (TextUtils.isPlusN(name) ? "n " : " ") + name + " level of 5 to wear this.");
                }
                return false;
            }
            return true;
        }
        if (itemRequirements == null) {
            return true;
        }
        for (int skill : itemRequirements.keySet()) {
            if (wield) {
                if (skill > Skills.MAGIC && skill != Skills.SLAYER) {
                    continue;
                }
            } else if (skill <= Skills.MAGIC && skill != Skills.SLAYER) {
                continue;
            }
            if (skill < 0 || skill >= Skills.SKILL_NAME.length) {
                continue;
            }
            int level = itemRequirements.get(skill);
            if (player.getSkills().getStaticLevel(skill) < level) {
                if (message) {
                    String name = Skills.SKILL_NAME[skill];
                    player.getActionSender().sendMessage("You need a" + (TextUtils.isPlusN(name) ? "n " : " ") + name + " level of " + level + " to " + (wield ? "wear " : "use ") + "this.");
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the level requirement for this item.
     *
     * @param skillId The skill id.
     * @return The level required.
     */
    public int getRequirement(int skillId) {
        if (itemRequirements == null) {
            return 1;
        }
        Integer level = itemRequirements.get(skillId);
        return level == null ? 1 : level;
    }


    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setMembers(boolean members) {
        this.members = members;
    }

    public void setGroundOptions(String[] groundOptions) {
        String[] groundOps = new String[groundOptions.length];
        for (int i = 0; i < groundOptions.length; i++) {
            if (groundOptions[i] != null) {
                groundOps[i] = groundOptions[i].trim().toLowerCase();
            }
        }
        this.groundOptions = groundOps;
    }

    public void setUnnoted(boolean unnoted) {
        this.unnoted = unnoted;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setNoteTemplateId(int noteTemplateId) {
        this.noteTemplateId = noteTemplateId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setWeightId(int weightId) {
        this.weightId = weightId;
    }

    public void setEquipment(boolean equipment) {
        this.equipment = equipment;
    }

    public boolean isEquipment() {
        return equipment;
    }

    public Map<Integer, Integer> getItemRequirements() {
        return itemRequirements;
    }

    public void setItemRequirements(Map<Integer, Integer> itemRequirements) {
        this.itemRequirements = itemRequirements;
    }

    @Override
    public String toString() {
        return ItemDefinition.class.getSimpleName() + "[id=" + id + ", name=\"" + name + "\"]";
    }

}
