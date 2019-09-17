package org.gielinor.cache.def.impl;

import java.util.HashMap;
import java.util.Map;

import org.gielinor.cache.def.Definition;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.entity.impl.Animator;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.npc.drop.NPCDropTables;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.parser.npc.NPCConfiguration;
import org.gielinor.rs2.config.Constants;
import org.gielinor.utilities.string.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an NPC definition.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class NPCDefinition extends Definition<NPC> {

    private static final Logger log = LoggerFactory.getLogger(NPCDefinition.class);

    /**
     * Temporary npc ids.
     */
    public static final int GENERAL_GRAARDOR_JR = 8591;
    /**
     * The pet k'ril tsutsaroth npc id.
     */
    public static final int KRIL_TSUTSAROTH_JR = 8592;
    /**
     * The pet kree'arra npc id.
     */
    public static final int KREE_ARRA_JR = 8593;
    /**
     * The pet zilyana npc id.
     */
    public static final int ZILYANA_JR = 8594;
    /**
     * The max hit dummy npc id.
     */
    public static final int MAX_HIT_DUMMY = 8595;
    /**
     * The Adam npc id.
     */
    public static final int ADAM = 20311;
    /**
     * The Paul npc id.
     */
    public static final int PAUL = 20317;
    /**
     * The Juan npc id.
     */
    public static final int JUAN = 23369;
    /**
     * The TzRek-Jad npc id.
     */
    public static final int TZREK_JAD = 8601;
    /**
     * The Mac npc id.
     */
    public static final int MAC = 8599;

    /**
     * The definitions.
     */
    private static final Map<Integer, NPCDefinition> DEFINITIONS = new HashMap<>();

    /**
     * The default option handlers.
     */
    private static final Map<String, OptionHandler> OPTION_HANDLERS = new HashMap<>();

    /**
     * The examine option value
     */
    public String examine;

    /**
     * The drop tables.
     */
    private NPCDropTables dropTables;

    public int turnCCWAnimation;
    public int configFileId;
    public int turn180Animation;
    public int configId;
    public int combatLevel;
    public final int anInt64;
    public int walkAnimation;
    public byte size;
    public int[] anIntArray70;
    public int[] anIntArray73;
    public int headIcons;
    public int[] anIntArray76;
    public int standAnimation;
    public int anInt79;
    public int turnCWAnimation;
    public boolean aBoolean84;
    public int anInt85;
    public int anInt86;
    public boolean isVisibleOnMap;
    public int childNPCIds[];
    public byte description[];
    public int anInt91;
    public int anInt92;
    public boolean aBoolean93;
    public int[] modelIds = new int[0];

    /**
     * The combat animations.
     */
    private Animation[] combatAnimations = new Animation[5];

    /**
     * The minimum combat distance (0 uses default distances).
     */
    private int combatDistance;

    /**
     * The combat graphics.
     */
    private Graphics[] combatGraphics = new Graphics[3];

    /**
     * Constructs a new {@code NPCDefinition}.
     *
     * @param id The NPC id.
     */
    public NPCDefinition(int id) {
        this.id = id;
        turnCCWAnimation = -1;
        configFileId = -1;
        turn180Animation = -1;
        configId = -1;
        combatLevel = -1;
        anInt64 = 1834;
        walkAnimation = -1;
        size = 1;
        headIcons = -1;
        standAnimation = -1;
        anInt79 = 32;
        turnCWAnimation = -1;
        aBoolean84 = true;
        anInt86 = 128;
        isVisibleOnMap = true;
        anInt91 = 128;
        aBoolean93 = false;
        options = new String[5];
    }

    /**
     * Gets the NPC definition for the id given.
     *
     * @param id The id of the NPC.
     * @return The NPC definition.
     */
    public static NPCDefinition forId(int id) {
        if (DEFINITIONS.get(id) == null) {
            if (id != -1 && id != 6392) {
                log.warn("Missing NPC definition for: {}.", id);
            }
            DEFINITIONS.put(id, new NPCDefinition(id));
        }
        return DEFINITIONS.get(id);
    }

    /**
     * Gets the first NPC definition for this name.
     *
     * @param name The name of the NPC.
     * @return The NPC definition object.
     */
    public static NPCDefinition forName(String name) {
        for (NPCDefinition def : getDefinitions().values()) {
            if (def.getName().equalsIgnoreCase(name)) {
                return def;
            }
        }
        return null;
    }

    /**
     * Gets the child object definitions.
     *
     * @param player The player to get it for.
     * @return The object definition.
     */
    public NPCDefinition getChildNPC(Player player) {
        if (childNPCIds == null || childNPCIds.length < 1) {
            return this;
        }
        int configValue = -1;
        if (player != null) {
            if (configFileId != -1) {
                configValue = ConfigFileDefinition.forId(configFileId).getValue(player);
            } else if (configId != -1) {
                configValue = player.getConfigManager().get(configId);
            }
        } else {
            configValue = 0;
        }
        if (configValue < 0 || configValue >= childNPCIds.length - 1 || childNPCIds[configValue] == -1) {
            int objectId = childNPCIds[childNPCIds.length - 1];
            if (objectId != -1) {
                return forId(objectId);
            }
            return this;
        }
        return forId(childNPCIds[configValue]);
    }

    /**
     * Checks if this NPC has an attack option.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasAttackOption() {
        for (String option : options) {
            if (option != null && option.equalsIgnoreCase("attack")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the option handler for the given option name.
     *
     * @param nodeId The node id.
     * @param name   The name.
     * @return The option handler, or {@code null} if there was no default
     * option handler.
     */
    public static OptionHandler getOptionHandler(int nodeId, String name) {
        NPCDefinition def = forId(nodeId);
        OptionHandler handler = def.getConfiguration("option:" + name);
        if (handler != null) {
            return handler;
        }
        return OPTION_HANDLERS.get(name);
    }

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
     * Sets the default option handler for an option.
     *
     * @param name          The option name.
     * @param optionHandler The default option handler.
     * @return <code>True</code> if there was a previous default handler mapped.
     */
    public static boolean setOptionHandler(String name, OptionHandler optionHandler) {
        return OPTION_HANDLERS.put(name, optionHandler) != null;
    }

    /**
     * Gets the option handlers.
     *
     * @return The option handlers.
     */
    public static Map<String, OptionHandler> getOptionHandlers() {
        return OPTION_HANDLERS;
    }

    /**
     * Gets the definitions mapping.
     *
     * @return The mapping.
     */
    public static Map<Integer, NPCDefinition> getDefinitions() {
        return DEFINITIONS;
    }

    /**
     * Method returns the value for 'examine'.
     *
     * @return The examine.
     */
    public final String getExamine() {
        String string = getConfiguration(NPCConfiguration.EXAMINE, examine);
        if (string != null) {
            if (string.startsWith("One of " + Constants.SERVER_NAME)) {
                return ("One of " + Constants.SERVER_NAME + "\'s many citizens.");
            }
            return string;
        }
        return "It's a" + (TextUtils.isPlusN(name) ? "n " : " ") + name + ".";
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
     * Sets the combat animations for this definition.
     *
     * @param anims The animation ids to set.
     */
    public void setCombatAnimations(int... anims) {
        for (int i = 0; i < anims.length; i++) {
            if (i == combatAnimations.length) {
                break;
            }
            combatAnimations[i] = new Animation(anims[i], i == 3 ? Animator.Priority.MID : Animator.Priority.HIGH);
        }
    }

    /**
     * Gets the size.
     *
     * @return The size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the headIcons.
     *
     * @return The headIcons.
     */
    public int getHeadIcons() {
        return headIcons;
    }

    /**
     * Gets the isVisibleOnMap.
     *
     * @return The isVisibleOnMap.
     */
    public boolean isVisibleOnMap() {
        return isVisibleOnMap;
    }

    /**
     * Gets the configFileId.
     *
     * @return The configFileId.
     */
    public int getConfigFileId() {
        return configFileId;
    }

    /**
     * Gets the childNPCIds.
     *
     * @return The childNPCIds.
     */
    public int[] getChildNPCIds() {
        return childNPCIds;
    }

    /**
     * Gets the configId.
     *
     * @return The configId.
     */
    public int getConfigId() {
        return configId;
    }

    /**
     * Gets the standAnimation.
     *
     * @return The standAnimation.
     */
    public int getStandAnimation() {
        return standAnimation;
    }

    /**
     * Gets the walkAnimation.
     *
     * @return The walkAnimation.
     */
    public int getWalkAnimation() {
        return walkAnimation;
    }

    int turnAnimation;

    /**
     * Gets the turnAnimation.
     *
     * @return The turnAnimation.
     */
    public int getTurnAnimation() {
        return turnAnimation;
    }

    /**
     * Gets the turn180Animation.
     *
     * @return The turn180Animation.
     */
    public int getTurn180Animation() {
        return turn180Animation;
    }

    /**
     * Gets the turnCWAnimation.
     *
     * @return The turnCWAnimation.
     */
    public int getTurnCWAnimation() {
        return turnCWAnimation;
    }

    /**
     * Gets the turnCCWAnimation.
     *
     * @return The turnCCWAnimation.
     */
    public int getTurnCCWAnimation() {
        return turnCCWAnimation;
    }

    /**
     * @return the dropTables.
     */
    public NPCDropTables getDropTables() {
        return dropTables;
    }

    /**
     * @param dropTables the dropTables to set.
     */
    public void setDropTables(NPCDropTables dropTables) {
        this.dropTables = dropTables;
    }

    /**
     * Gets the combatLevel.
     *
     * @return The combatLevel.
     */
    public int getCombatLevel() {
        return combatLevel;
    }

    /**
     * Sets the combatLevel.
     *
     * @param combatLevel The combatLevel to set.
     */
    public void setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
    }

    /**
     * Gets the combatAnimations.
     *
     * @return The combatAnimations.
     */
    public Animation[] getCombatAnimations() {
        return combatAnimations;
    }

    /**
     * Gets the combatDistance.
     *
     * @return The combatDistance.
     */
    public int getCombatDistance() {
        return combatDistance;
    }

    /**
     * Sets the combatDistance.
     *
     * @param combatDistance The combatDistance to set.
     */
    public void setCombatDistance(int combatDistance) {
        this.combatDistance = combatDistance;
    }

    /**
     * Gets the combatGraphics.
     *
     * @return The combatGraphics.
     */
    public Graphics[] getCombatGraphics() {
        return combatGraphics;
    }

    /**
     * Initializes the combat graphics.
     *
     * @param config The configurations.
     */
    public void initCombatGraphics(Map<String, Object> config) {
        if (config.containsKey(NPCConfiguration.START_GRAPHIC)) {
            combatGraphics[0] = new Graphics((Integer) config.get(NPCConfiguration.START_GRAPHIC), getConfiguration(NPCConfiguration.START_HEIGHT, 0));
        }
        if (config.containsKey(NPCConfiguration.PROJECTILE)) {
            combatGraphics[1] = new Graphics((Integer) config.get(NPCConfiguration.PROJECTILE), getConfiguration(NPCConfiguration.PROJECTILE_HEIGHT, 42));
        }
        if (config.containsKey(NPCConfiguration.END_GRAPHIC)) {
            combatGraphics[2] = new Graphics((Integer) config.get(NPCConfiguration.END_GRAPHIC), getConfiguration(NPCConfiguration.END_HEIGHT, 96));
        }
    }

}
