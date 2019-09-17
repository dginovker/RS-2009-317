package org.gielinor.game.content.skill.menu;

/**
 * Represents a {@link org.gielinor.game.content.skill.SkillMenu} definition.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SkillMenuDefinition {

    /**
     * The id of the skill.
     */
    private final int skillId;

    /**
     * The subsection.
     */
    private final int subsection;

    /**
     * The level required.
     */
    private final int level;

    /**
     * The id of the item.
     */
    private final int itemId;

    /**
     * The message.
     */
    private final String message;

    /**
     * If this menu has text.
     */
    private boolean hasText;

    /**
     * Creates a new <code>SkillMenuDefinition</code>.
     *
     * @param skillId    The id of the skill.
     * @param subsection The subsection.
     * @param level      The level required.
     * @param itemId     The id of the item.
     * @param message    The message.
     * @param hasText    If this menu has text.
     */
    public SkillMenuDefinition(int skillId, int subsection, int level, int itemId, String message, boolean hasText) {
        this.skillId = skillId;
        this.subsection = subsection;
        this.level = level;
        this.itemId = itemId;
        this.message = message;
        this.hasText = hasText;
    }

    /**
     * Gets the id of the skill.
     *
     * @return The id of the skill.
     */
    public int getSkillId() {
        return skillId;
    }

    /**
     * Gets the subsection.
     *
     * @return The subsection.
     */
    public int getSubsection() {
        return subsection;
    }

    /**
     * Gets the level required.
     *
     * @return The level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the id of the item.
     *
     * @return The id of the item.
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Gets the message.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * If this menu has text.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasText() {
        return hasText;
    }
}
