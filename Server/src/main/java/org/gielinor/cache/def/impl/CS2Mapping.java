package org.gielinor.cache.def.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * The CS2 mapping.
 *
 * @author Emperor
 */
public final class CS2Mapping {

    /**
     * The CS2 mappings.
     */
    private static final Map<Integer, CS2Mapping> maps = new HashMap<>();

    /**
     * The script id.
     */
    private final int scriptId;

    /**
     * Unknown value.
     */
    private int unknown;

    /**
     * Second unknown value.
     */
    private int unknown1;

    /**
     * The default string value.
     */
    private String defaultString;

    /**
     * The default integer value.
     */
    private int defaultInt;

    /**
     * The mapping.
     */
    private HashMap<Integer, Object> map;

    /**
     * Constructs a new {@code CS2Mapping} {@code Object}.
     *
     * @param scriptId The script id.
     */
    public CS2Mapping(int scriptId) {
        this.scriptId = scriptId;
    }

    /**
     * Gets the mapping for the given script id.
     *
     * @param scriptId The script id.
     * @return The mapping.
     */
    public static CS2Mapping forId(int scriptId) {
        return maps.get(scriptId);
    }

    /**
     * Adds a CS2 mapping.
     */
    public static void add(int scriptId, CS2Mapping cs2Mapping) {
        maps.put(scriptId, cs2Mapping);
    }

    /**
     * Gets the scriptId.
     *
     * @return The scriptId.
     */
    public int getScriptId() {
        return scriptId;
    }

    /**
     * Gets the unknown.
     *
     * @return The unknown.
     */
    public int getUnknown() {
        return unknown;
    }

    /**
     * Sets the unknown.
     *
     * @param unknown The unknown to set.
     */
    public void setUnknown(int unknown) {
        this.unknown = unknown;
    }

    /**
     * Gets the unknown1.
     *
     * @return The unknown1.
     */
    public int getUnknown1() {
        return unknown1;
    }

    /**
     * Sets the unknown1.
     *
     * @param unknown1 The unknown1 to set.
     */
    public void setUnknown1(int unknown1) {
        this.unknown1 = unknown1;
    }

    /**
     * Gets the defaultString.
     *
     * @return The defaultString.
     */
    public String getDefaultString() {
        return defaultString;
    }

    /**
     * Sets the defaultString.
     *
     * @param defaultString The defaultString to set.
     */
    public void setDefaultString(String defaultString) {
        this.defaultString = defaultString;
    }

    /**
     * Gets the defaultInt.
     *
     * @return The defaultInt.
     */
    public int getDefaultInt() {
        return defaultInt;
    }

    /**
     * Sets the defaultInt.
     *
     * @param defaultInt The defaultInt to set.
     */
    public void setDefaultInt(int defaultInt) {
        this.defaultInt = defaultInt;
    }

    /**
     * Gets the map.
     *
     * @return The map.
     */
    public HashMap<Integer, Object> getMap() {
        return map;
    }

    /**
     * Sets the map.
     *
     * @param map The map to set.
     */
    public void setMap(HashMap<Integer, Object> map) {
        this.map = map;
    }
}