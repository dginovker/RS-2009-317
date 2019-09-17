package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.Context;

/**
 * The XP Drop {@link org.gielinor.net.packet.Context}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class XPDropContext implements Context {

    /**
     * The player.
     */
    private final Player player;
    /**
     * The skill id.
     */
    private final int skillId;
    /**
     * The experience.
     */
    private final long experience;

    /**
     * Constructs a new <code>XPDropContext</code>.
     *
     * @param player     The player.
     * @param skillId    The skill id.
     * @param experience The experience.
     */
    public XPDropContext(Player player, int skillId, long experience) {
        this.player = player;
        this.skillId = skillId;
        this.experience = experience;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the skill id.
     *
     * @return The skill id.
     */
    public int getSkillId() {
        return skillId;
    }

    /**
     * Gets the experience.
     *
     * @return The experience.
     */
    public long getExperience() {
        return experience;
    }

}