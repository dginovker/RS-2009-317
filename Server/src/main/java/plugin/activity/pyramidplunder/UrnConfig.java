package plugin.activity.pyramidplunder;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds information for the urns.
 *
 * @author Unknown
 */
public enum UrnConfig {

    URN_1(16529, 22, 23, 0),
    URN_2(16520, 4, 5, 0),
    URN_3(16525, 14, 15, 0),
    URN_4(16530, 24, 25, 0),
    URN_5(16526, 16, 17, 0),
    URN_6(16531, 26, 27, 0),
    URN_7(16522, 8, 9, 0),
    URN_8(16532, 28, 29, 0),
    URN_9(16527, 18, 19, 0),
    URN_10(16518, 0, 1, 0),
    URN_11(16523, 10, 11, 0),
    URN_12(16528, 20, 21, 0),
    URN_13(16519, 2, 3, 0),
    URN_14(16524, 12, 13, 0),
    URN_15(16521, 6, 7, 0),


    URN_16(16519, 4, 4, 0);

    /**
     * The object id.
     */
    private int objectId;

    /**
     * The opened id.
     */
    private int openId;

    /**
     * The snake id.
     */
    private int snakeId;

    /**
     * The charmed snake id.
     */
    private int snakeCharmedId;

    /**
     * A map of the urn configs.
     */
    private static Map<Integer, UrnConfig> configsMap = new HashMap<Integer, UrnConfig>();

    static {
        for (UrnConfig urnConfigs : UrnConfig.values()) {
            configsMap.put(urnConfigs.objectId, urnConfigs);
        }
    }

    /**
     * Constructs a new {@Code UrnConfig} {@Code Object}
     *
     * @param objectId       The object id.
     * @param openId         The open id for the urn.
     * @param snakeId        The snake id for the urn.
     * @param snakeCharmedId The charmed snake id.
     */
    UrnConfig(int objectId, int openId, int snakeId, int snakeCharmedId) {
        this.objectId = objectId;
        this.openId = openId;
        this.snakeId = snakeId;
        this.snakeCharmedId = snakeCharmedId;
    }

    /**
     * Gets the urn config for an object id.
     *
     * @param objectId The object id.
     * @return
     */
    public static UrnConfig forId(int objectId) {
        return configsMap.get(objectId);
    }

    //  public int getConfig(TreasuresData.TreasureState treasureState){
    //             return treasureState.ordinal() << openId;
    //}

    /**
     * The object id.
     *
     * @return
     */
    public int getObjectId() {
        return objectId;
    }

    /**
     * The open id.
     *
     * @return
     */
    public int getOpenId() {
        return openId;
    }

    /**
     * The snake id.
     *
     * @return
     */
    public int getSnakeId() {
        return snakeId;
    }

    /**
     * The charmed snake id.
     *
     * @return
     */
    public int getSnakeCharmedId() {
        return snakeCharmedId;
    }
}
