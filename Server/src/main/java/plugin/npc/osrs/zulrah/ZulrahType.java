package plugin.npc.osrs.zulrah;

/**
 * Represents the Zulrah Type.
 *
 * @author Empathy
 */
public enum ZulrahType {
    MAGIC(22044),
    RANGE(22042),
    MELEE(22043),
    JAD(22042);

    /**
     * The npc id of a zulrah type.
     */
    private final int npcId;

    /**
     * Constructs a new {@code ZulrahType} object.
     *
     * @param npcId The npcId
     */
    ZulrahType(int npcId) {
        this.npcId = npcId;
    }

    /**
     * @return The npc id.
     */
    public int getNpcId() {
        return npcId;
    }
}
