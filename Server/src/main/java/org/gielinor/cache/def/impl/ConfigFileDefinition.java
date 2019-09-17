package org.gielinor.cache.def.impl;

import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.node.entity.player.Player;

/**
 * Handles config definition reading.
 *
 * @author Emperor
 */
public final class ConfigFileDefinition {

    /**
     * The config definitions mapping.
     */
    private static final Map<Integer, ConfigFileDefinition> MAPPING = new HashMap<>();

    /**
     * The bit size flags.
     */
    private static final int[] BITS = new int[32];

    /**
     * The file id.
     */
    private final int id;

    /**
     * The config id.
     */
    private int configId;

    /**
     * The bit shift amount.
     */
    private int bitShift;

    /**
     * The bit amount.
     */
    private int bitSize;

    /**
     * Constructs a new {@code ConfigFileDefinition} {@code Object}.
     *
     * @param id The file id.
     */
    public ConfigFileDefinition(int id) {
        this.id = id;
    }

    /**
     * Initializes the bit flags.
     */
    static {
        int flag = 2;
        for (int i = 0; i < 32; i++) {
            BITS[i] = flag - 1;
            flag += flag;
        }
    }

    /**
     * Gets the config file definitions for the given file id.
     *
     * @param id The file id.
     * @return The definition.
     */
    public static StringBuilder sb = new StringBuilder();

    public static ConfigFileDefinition forId(int id) {
        return MAPPING.get(id);
    }

    /**
     * Gets the current config value for this file.
     *
     * @param player The player.
     * @return The config value.
     */
    public int getValue(Player player) {
        int size = BITS[bitSize - bitShift];
        return size & player.getConfigManager().get(configId) >> bitShift;
    }

    /**
     * Gets the mapping.
     *
     * @return The mapping.
     */
    public static Map<Integer, ConfigFileDefinition> getMapping() {
        return MAPPING;
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public int getId() {
        return id;
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
     * Gets the bitShift.
     *
     * @return The bitShift.
     */
    public int getBitShift() {
        return bitShift;
    }

    /**
     * Gets the bitSize.
     *
     * @return The bitSize.
     */
    public int getBitSize() {
        return bitSize;
    }


    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public void setBitShift(int bitShift) {
        this.bitShift = bitShift;
    }

    public void setBitSize(int bitSize) {
        this.bitSize = bitSize;
    }
}