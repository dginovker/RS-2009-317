package org.gielinor.game.content.dialogue;

/**
 * Represents the facial expressions (the animations the entity does when talking).
 *
 * @author Emperor
 */
public enum FacialExpression {

    /**
     * Old facial expressions used for irregular NPCs.
     */
    OLD_NORMAL(590),
    OLD_HAPPY(588),
    OLD_CALM(590),
    OLD_DEFAULT(591),
    OLD_EVIL(592),
    OLD_EVIL2(593),
    OLD_DELIGHTED_EVIL(594),
    OLD_ANNOYED(595),
    OLD_DISTRESSED(596),
    OLD_DISTRESSED2(597),
    OLD_ALMOST_CRYING(598),
    OLD_HEAD_BOW(599),
    OLD_SLEEPY_LEFT(600),
    OLD_SLEEPY_RIGHT(601),
    OLD_SLEEPY(602),
    OLD_TIPPING_HEAD(603),
    OLD_VERY_EVIL(604),
    OLD_LAUGH(605),
    OLD_LONGER_LAUGH(606),
    OLD_EVIL_LAUGH(609),
    OLD_SLIGHTLY_SAD(610),
    OLD_VERY_SAD(611),
    OLD_ON_ONE_HAND(612),
    OLD_ANGRY(614),
    OLD_ANGRY2(615),

    /**
     * Talking expressions used for regular NPCs. // TODO get the rest of the expressions
     */
    NORMAL(9847),
    NORMAL_STILL(9855),
    HAPPY(9850),
    DIDNT_DO_IT(9863),
    TALKING(9808),
    NO_EXPRESSION(9760),
    CRYING(9765),
    SAD(9764),
    SAD_2(9768),
    LOOKING_DOWN(9812),
    CONFUSED(9827),
    ANGRY(9788),
    EYES_WIDE(9824),
    DISGUSTED(9820),
    ANGRY_YELLING(9790),
    TALK_SWING(9844),
    LAUGHING(9840),
    GRUMPY(9784),
    SCARED(9780),
    PLAIN(9808),
    WORRIED(9770),
    ANNOYED(9796),
    SNEAKY(9812),
    DRUNK(9853),
    INTERROGATING(9820),
    EYEBROWS_UP(9848);

    /**
     * The animation id.
     */
    private final int animationId;

    /**
     * Constructs a new {@code FacialExpression} {@code Object}.
     *
     * @param animationId The animation id.
     */
    private FacialExpression(int animationId) {
        this.animationId = animationId;
    }

    /**
     * Gets the animation id.
     *
     * @return The animation id.
     */
    public int getAnimationId() {
        return animationId;
    }
}