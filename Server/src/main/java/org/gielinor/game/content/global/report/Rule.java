package org.gielinor.game.content.global.report;

import org.gielinor.game.node.entity.player.Player;

/**
 * Represents a <b>Rule</b> a <b>Player</b> has broken.
 *
 * @author 'Vexia
 *
 */
public enum Rule {

    /**
     * Represents the <b>Rule</b>.
     */
    OFFENSIVE_LANGUAGE(0),
    /**
     * Represents the <b>Rule</b>.
     */
    ITEM_SCAMMING(1),
    /**
     * Represents the <b>Rule</b>.
     */
    PASSWORD_SCAMMING(2),
    /**
     * Represents the <b>Rule</b>.
     */
    BUG_ABUSE(3),
    /**
     * Represents the <b>Rule</b>.
     */
    JAGEX_STAFF_IMPERSONATION(4),
    /**
     * Represents the <b>Rule</b>.
     */
    ACCCOUNT_SHARING(5),
    /**
     * Represents the <b>Rule</b>.
     */
    MACROING(6),
    /**
     * Represents the <b>Rule</b>.
     */
    MULTIPLE_LOGGING(7),
    /**
     * Represents the <b>Rule</b>.
     */
    ENCOURAGING_TO_BREAK_TULES(8),
    /**
     * Represents the <b>Rule</b>.
     */
    MISUSE_OF_CUSTOMER_SUPPORT(9),
    /**
     * Represents the <b>Rule</b>.
     */
    ADVERISTING(10),
    /**
     * Represents the <b>Rule</b>.
     */
    REAL_WORLD_ITEM_TRADING(11),
    /**
     * Represents the <b>Rule</b>.
     */
    ASKING_PERSONAL_DETAILS(12);

    /**
     * Constructs a new {@code Rule.java} {@code Object}.
     *
     * @param rule the rule.
     */
    Rule(int rule) {
        this.setRule(rule);
    }

    /**
     * The rule id opcode.
     */
    private int rule;

    /**
     * Represents if the rule is relative to the sequences that has happend.
     *
     * @param player the <b>Player</b>.
     *
     * @return <code>True</code> if so.
     */
    public boolean canRequest(Player target) {
        return target != null && target.getSavedData().getGlobalData().getChatPing() >= System.currentTimeMillis();
    }

    /**
     * @return the rule.
     */
    public int getRule() {
        return rule;
    }

    /**
     * @param rule the rule to set.
     */
    public void setRule(int rule) {
        this.rule = rule;
    }

    /**
     * Gets the rule.
     *
     * @param id the id.
     *
     * @return the rule.
     */
    public static Rule forId(int id) {
        for (Rule rule : Rule.values()) {
            if (rule.getRule() == id) {
                return rule;
            }
        }
        return null;
    }
}
