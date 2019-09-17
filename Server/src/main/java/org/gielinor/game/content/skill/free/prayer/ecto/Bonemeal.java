package org.gielinor.game.content.skill.free.prayer.ecto;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a bone meal.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum Bonemeal {

    BONES(526, 4255),
    BAT_BONES(530, 4256),
    BIG_BONES(532, 4257),
    BABY_DRAGON_BONES(534, 4260),
    DRAGON_BONES(536, 4261),
    DAGANNOTH_BONES(6729, 6728),
    WYVERN_BONES(6812, 6810),
    OURG_BONES(4834, 4855);

    private int boneId;
    private int boneMealId;

    private static Map<Integer, Bonemeal> boneMealMap = new HashMap<>();
    private static Map<Integer, Bonemeal> bones = new HashMap<>();

    public static Bonemeal forBoneId(int itemId) {
        return boneMealMap.get(itemId);
    }

    public static Bonemeal forMealId(int itemId) {
        return bones.get(itemId);
    }

    static {
        for (final Bonemeal bonemeal : Bonemeal.values()) {
            boneMealMap.put(bonemeal.boneId, bonemeal);
        }
        for (final Bonemeal bonemeal : Bonemeal.values()) {
            bones.put(bonemeal.boneMealId, bonemeal);
        }
    }

    private Bonemeal(int boneId, int boneMealId) {
        this.boneId = boneId;
        this.boneMealId = boneMealId;
    }

    public int getBoneId() {
        return boneId;
    }

    public int getBonemealId() {
        return boneMealId;
    }
}
