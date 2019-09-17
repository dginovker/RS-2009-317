package org.gielinor.parser.npc;

/**
 * Handles the NPC configuration parsing.
 *
 * @author Emperor
 */
public final class NPCConfiguration {

    /**
     * The weakness attribute.
     */
    public static final String WEAKNESS = "weakness";

    /**
     * The lifepoints attribute.
     */
    public static final String LIFEPOINTS = "lifepoints";

    /**
     * The attack level attribute.
     */
    public static final String ATTACK_LEVEL = "attack_level";

    /**
     * The strength level attribute.
     */
    public static final String STRENGTH_LEVEL = "strength_level";

    /**
     * The defence level attribute.
     */
    public static final String DEFENCE_LEVEL = "defence_level";

    /**
     * The range level attribute.
     */
    public static final String RANGE_LEVEL = "range_level";

    /**
     * The range level attribute.
     */
    public static final String MAGIC_LEVEL = "magic_level";

    /**
     * The examine attribute.
     */
    public static final String EXAMINE = "examine";

    /**
     * The slayer task attribute.
     */
    public static final String SLAYER_TASK = "slayer_task";

    /**
     * The poisonous attribute.
     */
    public static final String POISONOUS = "poisonous";

    /**
     * The aggressive attribute.
     */
    public static final String AGGRESSIVE = "aggressive";

    /**
     * The respawn delay attribute.
     */
    public static final String RESPAWN_DELAY = "respawn";

    /**
     * The attack speed attribute.
     */
    public static final String ATTACK_SPEED = "attack_speed";

    /**
     * The poison immune attribute.
     */
    public static final String POISON_IMMUNE = "poison_immune";

    /**
     * The bonuses attribute.
     */
    public static final String BONUSES = "bonuses";

    /**
     * The start graphic attribute.
     */
    public static final String START_GRAPHIC = "start_gfx";

    /**
     * The projectile attribute.
     */
    public static final String PROJECTILE = "projectile";

    /**
     * The end graphic attribute.
     */
    public static final String END_GRAPHIC = "end_gfx";

    /**
     * The combat style attribute.
     */
    public static final String COMBAT_STYLE = "combat_style";

    /**
     * The aggressive radius attribute.
     */
    public static final String AGGRESSIVE_RADIUS = "agg_radius";

    /**
     * The slayer experience attribute.
     */
    public static final String SLAYER_EXP = "slayer_exp";

    /**
     * The amount to poison.
     */
    public static final String POISON_AMOUNT = "poison_amount";

    /**
     * The movement radius.
     */
    public static final String MOVEMENT_RADIUS = "movement_radius";

    /**
     * The spawn animation.
     */
    public static final String SPAWN_ANIMATION = "spawn_animation";

    /**
     * The start height.
     */
    public static final String START_HEIGHT = "start_height";

    /**
     * The projectile height.
     */
    public static final String PROJECTILE_HEIGHT = "prj_height";

    /**
     * The end height.
     */
    public static final String END_HEIGHT = "end_height";

    /**
     * The clue level.
     */
    public static final String CLUE_LEVEL = "clue_level";

    /**
     * The spell id.
     */
    public static final String SPELL_ID = "spell_id";

    /**
     * The combat audio.
     */
    public static final String COMBAT_AUDIO = "combat_audio";

    /**
     * Constructs a new {@code NPCConfiguration} {@Code Object}.
     */
    public NPCConfiguration() {
        /*
         * empty.
         */
    }

    /**
     * Represents a weakness of an NPC.
     *
     * @author Emperor
     */
    public static enum Weakness {
        STAB,
        SLASH,
        CRUSH,
        ARROW,
        BOLT,
        THROWN,
        AIR,
        WATER,
        EARTH,
        FIRE,
        NONE;
    }
}
