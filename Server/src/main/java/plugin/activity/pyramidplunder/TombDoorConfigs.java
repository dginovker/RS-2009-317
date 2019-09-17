package plugin.activity.pyramidplunder;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds information for the tomb doors unlocked in the minigame.
 *
 * @author Unknown
 */
public enum TombDoorConfigs {

    TOMB_DOOR_1(16540, 10),
    TOMB_DOOR_2(16539, 9),
    TOMB_DOOR_3(16542, 12),
    TOMB_DOOR_4(16541, 11);

    /**
     * The object id.
     */
    private int objectId;

    /**
     * The open id for the object.
     */
    private int openId;

    /**
     * A map of the tomb door configs.
     */
    private static Map<Integer, TombDoorConfigs> configsMap = new HashMap<Integer, TombDoorConfigs>();

    static {
        for (TombDoorConfigs urnConfigs : TombDoorConfigs.values()) {
            configsMap.put(urnConfigs.objectId, urnConfigs);
        }
    }

    /**
     * Constructs a new {@Code TombDoorConfigs} {@Code Object}
     *
     * @param objectId
     * @param openId
     */
    TombDoorConfigs(int objectId, int openId) {
        this.objectId = objectId;
        this.openId = openId;
    }

    /**
     * The tomb doro configs.
     *
     * @param objectId The objet id.
     * @return
     */
    public static TombDoorConfigs forId(int objectId) {
        return configsMap.get(objectId);
    }

    /**
     * Gets the config to use.
     *
     * @return
     */
    public int getConfig() {
        return 1 << openId;
    }

    // public int getConfig(TreasuresData.TreasureState treasureState){
    //    switch(treasureState) {
    //       case OPEN:
    //          return 1 << openId;
    // }
    //return -1;
    //}

    /**
     * Gets the object id.
     *
     * @return
     */
    public int getObjectId() {
        return objectId;
    }

    /**
     * Gets the open id.
     *
     * @return
     */
    public int getOpenId() {
        return openId;
    }

}
